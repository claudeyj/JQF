/*
 * Copyright (c) 2017, University of California, Berkeley
 *
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 *
 * 1. Redistributions of source code must retain the above copyright
 * notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 * HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package edu.berkeley.cs.jqf.instrument.tracing;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.concurrent.Callable;
import java.util.function.Consumer;
import java.util.function.Function;

import edu.berkeley.cs.jqf.instrument.tracing.events.AllocEvent;
import edu.berkeley.cs.jqf.instrument.tracing.events.BranchEvent;
import edu.berkeley.cs.jqf.instrument.tracing.events.CallEvent;
import edu.berkeley.cs.jqf.instrument.tracing.events.ReadEvent;
import edu.berkeley.cs.jqf.instrument.tracing.events.ReturnEvent;
import edu.berkeley.cs.jqf.instrument.tracing.events.TraceEvent;
import edu.berkeley.cs.jqf.instrument.util.FastBlockingQueue;
import janala.logger.inst.*;

/**
 * This class is responsible for tracing for an instruction stream
 * generated by a single thread in the application.
 *
 * <p>Each ThreadTracer instance is a shadow thread for one application thread.</p>
 *
 * <p>A ThreadTracer instance holds an instruction queue that is populated by
 * the application thread via {@link SingleSnoop}, and removes items from
 * it to generate {@link TraceEvent}s. The generated events are handled by
 * a callback that is registered via
 * {@link SingleSnoop#setCallbackGenerator(Function)}.</p>
 *
 * @author Rohan Padhye
 */
public class ThreadTracer extends Thread {
    protected final FastBlockingQueue<Instruction> queue = new FastBlockingQueue<>(1024*1024);
    protected final Thread tracee;
    protected final String entryPoint;
    protected final Consumer<TraceEvent> callback;
    private final Deque<Callable<?>> handlers = new ArrayDeque<>();

    // Values set by GETVALUE_* instructions inserted by Janala
    private final Values values = new Values();


    /**
     * Creates a new tracer that will process instructions executed by an application
     * thread.
     *
     * @param tracee the thread to trace
     * @param entryPoint the outermost method call to trace (formatted as fq-class#method)
     * @param callback the callback to invoke whenever a trace event is emitted
     */
    protected ThreadTracer(Thread tracee, String entryPoint, Consumer<TraceEvent> callback) {
        super("__JWIG_TRACER__: " + tracee.getName()); // The name is important to block snooping
        this.tracee = tracee;
        this.entryPoint = entryPoint;
        this.callback = callback;
        this.handlers.push(new BaseHandler());
    }

    /**
     * Spawns a thread tracer for the given thread.
     *
     * @param thread the thread to trace
     * @return a tracer for the given thread
     */
    protected static ThreadTracer spawn(Thread thread) {
        String entryPoint = SingleSnoop.entryPoints.get(thread);
        Consumer<TraceEvent> callback = SingleSnoop.callbackGenerator.apply(thread);
        ThreadTracer t =
                new ThreadTracer(thread, entryPoint, callback);
        t.start();
        return t;
    }

    /**
     * Emits a trace event to be consumed by the registered callback.
     *
     * @param e the event to emit
     */
    protected final void emit(TraceEvent e) {
        callback.accept(e);
    }

    /**
     * Returns whether the instruction queue is empty.
     *
     * @return whether the instruction queue is empty
     */
    protected final boolean isQueueEmpty() {
        return queue.isEmpty();
    }

    /**
     * Sends an instruction to the tracer for processing.
     *
     * @param ins the instruction to process
     */
    protected final void consume(Instruction ins) {
        queue.put(ins);
    }

    /**
     * Retrieves the next yet-unprocessed instruction in FIFO sequeuence.
     *
     * <p>This method blocks for the next instruction up to a fixed timeout.
     * After the timeout, it checks to see if the tracee is alive and if so
     * repeats the timed-block. If the tracee is dead, the tracer is
     * interrupted.</p>
     *
     * @return the next yet-unprocessed instruction in FIFO sequeuence
     * @throws InterruptedException  if the tracee appears to be dead
     */
    protected final Instruction next() throws InterruptedException {
        // If a restored instruction exists, take that out instead of polling the queue
        if (restored != null) {
            Instruction ins = restored;
            restored = null;
            return ins;
        }
        // Keep attempting to get instructions while queue is non-empty or tracee is alive
        while (!queue.isEmpty() || tracee.isAlive()) {
            // Attempt to poll queue with a timeout
            Instruction ins = queue.remove(1_000_000_000L);
            // Return instruction if available, else re-try
            if (ins != null) {
                return ins;
            }
        }
        // If tracee is dead, interrupt this thread
        throw new InterruptedException();
    }

    // Hack on restore() to prevent deadlocks when main thread waits on put() and logger on restore()
    private Instruction restored = null;


    /**
     * Returns an instruction to the queue for processing.
     *
     * <p>This is useful for performing a lookahead in the
     * instruction stream.</p>
     *
     * @param ins the instruction to restore to the queue
     */
    protected final void restore(Instruction ins) {
        if (restored != null) {
            throw new IllegalStateException("Cannot restore multiple instructions");
        } else {
            restored = ins;
        }
    }

    @Override
    public void run() {
        try {
            while (!handlers.isEmpty()) {
                handlers.peek().call();
            }
        } catch (InterruptedException e) {
            // Exit normally
        } catch (Throwable e) {
            e.printStackTrace();
            handlers.clear(); // Don't do anything else
        }
    }

    private static boolean isReturnOrMethodThrow(Instruction inst) {
        return  inst instanceof ARETURN ||
                inst instanceof LRETURN ||
                inst instanceof DRETURN ||
                inst instanceof FRETURN ||
                inst instanceof IRETURN ||
                inst instanceof RETURN  ||
                inst instanceof METHOD_THROW;
    }


    private static boolean isInvoke(Instruction inst) {
        return  inst instanceof INVOKEINTERFACE ||
                        inst instanceof INVOKESPECIAL  ||
                        inst instanceof INVOKESTATIC   ||
                        inst instanceof INVOKEVIRTUAL;
    }

    private static boolean isIfJmp(Instruction inst) {
        return  inst instanceof IF_ACMPEQ ||
                        inst instanceof IF_ACMPNE ||
                        inst instanceof IF_ICMPEQ ||
                        inst instanceof IF_ICMPNE ||
                        inst instanceof IF_ICMPGT ||
                        inst instanceof IF_ICMPGE ||
                        inst instanceof IF_ICMPLT ||
                        inst instanceof IF_ICMPLE ||
                        inst instanceof IFEQ ||
                        inst instanceof IFNE ||
                        inst instanceof IFGT ||
                        inst instanceof IFGE ||
                        inst instanceof IFLT ||
                        inst instanceof IFLE ||
                        inst instanceof IFNULL ||
                        inst instanceof IFNONNULL;

    }


    private static class Values {
        private boolean booleanValue;
        private byte byteValue;
        private char charValue;
        private double doubleValue;
        private float floatValue;
        private int intValue;
        private long longValue;
        private Object objectValue;
        private short shortValue;
    }
    
    private void saveValue(GETVALUE gv) {
        if (gv instanceof GETVALUE_boolean) {
            values.booleanValue = ((GETVALUE_boolean) gv).v;
        } else if (gv instanceof GETVALUE_byte) {
            values.byteValue = ((GETVALUE_byte) gv).v;
        } else if (gv instanceof GETVALUE_char) {
            values.charValue = ((GETVALUE_char) gv).v;
        } else if (gv instanceof GETVALUE_double) {
            values.doubleValue = ((GETVALUE_double) gv).v;
        } else if (gv instanceof GETVALUE_float) {
            values.floatValue = ((GETVALUE_float) gv).v;
        } else if (gv instanceof GETVALUE_int) {
            values.intValue = ((GETVALUE_int) gv).v;
        } else if (gv instanceof GETVALUE_long) {
            values.longValue = ((GETVALUE_long) gv).v;
        } else if (gv instanceof GETVALUE_short) {
            values.shortValue = ((GETVALUE_short) gv).v;
        } else if (gv instanceof GETVALUE_Object) {
            values.objectValue = ((GETVALUE_Object) gv).v;
        }
    }



    private static String getOwnerName(MemberRef mr) {
        return mr.getOwner() + "#" + mr.getName();
    }


    private static boolean sameMemberRef(MemberRef m1, MemberRef m2) {
        return m1 != null && m2 != null &&
                m1.getOwner().equals(m2.getOwner()) &&
                m1.getName().equals(m2.getName()) &&
                m1.getDesc().equals(m2.getDesc());
    }
    
    private static boolean sameNameDesc(MemberRef m1, MemberRef m2) {
        return m1 != null && m2 != null &&
                m1.getName().equals(m2.getName()) &&
                m1.getDesc().equals(m2.getDesc());
    }



    class BaseHandler implements Callable<Void> {
        @Override
        public Void call() throws Exception {
            Instruction ins = next();
            if (ins instanceof METHOD_BEGIN) {
                METHOD_BEGIN begin = (METHOD_BEGIN) ins;
                // Try to match the top-level call with the entry point
                if (getOwnerName(begin).replace("/",".").equals(entryPoint)) {
                    emit(new CallEvent(0, null, 0, begin));
                    handlers.push(new TraceEventGeneratingHandler(begin, 0));
                } else {
                    // Ignore all top-level calls that are not the entry point
                    handlers.push(new MatchingNullHandler());
                }
            } else {
                // Instructions not nested in a METHOD_BEGIN are quite unexpected
                // System.err.println("Unexpected: " + ins); -- XXX: First instruction is a dummy
            }
            return null;
        }
    }

    class TraceEventGeneratingHandler implements Callable<Void> {

        private final int depth;
        private final MemberRef method;
        TraceEventGeneratingHandler(METHOD_BEGIN begin, int depth) {
            this.depth = depth;
            this.method = begin;
            //logger.log(tabs() + begin);
        }

        private String tabs() {
            StringBuffer sb = new StringBuffer(depth);
            for (int i = 0; i < depth; i++) {
                sb.append("  ");
            }
            return sb.toString();
        }

        private MemberRef invokeTarget = null;
        private boolean invokingSuperOrThis = false;
        private int lastIid = 0;
        private int lastMid = 0;


        @Override
        public Void call() throws InterruptedException {
            Instruction ins = next();

            if (ins instanceof METHOD_BEGIN) {
                METHOD_BEGIN begin = (METHOD_BEGIN) ins;
                if (sameNameDesc(begin, this.invokeTarget)) {
                    // Trace continues with callee
                    emit(new CallEvent(lastIid, this.method, lastMid, begin));
                    handlers.push(new TraceEventGeneratingHandler(begin, depth+1));
                } else {
                    // Class loading or static initializer
                    handlers.push(new MatchingNullHandler());
                }
            } else {


                // This should never really happen:
                if (ins instanceof INVOKEMETHOD_EXCEPTION &&
                        (this.invokeTarget == null))  {
                    throw new RuntimeException("Unexpected INVOKEMETHOD_EXCEPTION");
                }
                // This should never really happen:
                if (ins instanceof INVOKEMETHOD_END && this.invokeTarget == null) {
                    throw new RuntimeException("Unexpected INVOKEMETHOD_END");
                }

                // Handle SPECIAL instructions if they haven't been consumed by their predecessors
                if (ins instanceof SPECIAL) {
                    SPECIAL special = (SPECIAL) ins;
                    // Handle marker that says calling super() or this()
                    if (special.i == SPECIAL.CALLING_SUPER_OR_THIS) {
                        this.invokingSuperOrThis = true;
                    }

                    return null; // Do not process SPECIAL instructions further
                }



                // Handle setting or un-setting of invokeTarget buffer
                if (isInvoke(ins)) {
                    // Remember invocation target until METHOD_BEGIN or INVOKEMETHOD_END/INVOKEMETHOD_EXCEPTION
                    this.invokeTarget = (MemberRef) ins;
                } else if (this.invokeTarget != null) {
                    // If we don't step into a method call, we must be stepping over it
                    assert(ins instanceof  INVOKEMETHOD_END || ins instanceof  INVOKEMETHOD_EXCEPTION);

                    // Unset the invocation target for the rest of the instruction stream
                    this.invokeTarget = null;

                    // Handle end of super() or this() call
                    if (invokingSuperOrThis) {
                        if (ins instanceof INVOKEMETHOD_END) {
                            // For normal end, simply unset the flag
                            this.invokingSuperOrThis = false;
                        } else {
                            assert(ins instanceof  INVOKEMETHOD_EXCEPTION);

                            while (true) { // will break when outer caller of <init> found
                                emit(new ReturnEvent(-1, this.method, 0));
                                handlers.pop();
                                Callable<?> handler = handlers.peek();
                                // We should not reach the BaseHandler without finding
                                // the TraceEventGeneratingHandler who called the outer <init>().
                                assert (handler instanceof TraceEventGeneratingHandler);
                                TraceEventGeneratingHandler traceEventGeneratingHandler = (TraceEventGeneratingHandler) handler;
                                if (traceEventGeneratingHandler.invokingSuperOrThis) {
                                    // Go down the stack further
                                    continue;
                                } else {
                                    // Found caller of new()
                                    assert(traceEventGeneratingHandler.invokeTarget.getName().startsWith("<init>"));
                                    restore(ins);
                                    return null; // defer handling to new top of stack
                                }
                            }

                        }
                    }

                }


                // Emit conditional branches
                if (isIfJmp(ins)) {
                    Instruction next = next();
                    int iid = ins.iid;
                    int lineNum = ins.mid;
                    boolean taken;
                    if ((next instanceof SPECIAL) && ((SPECIAL) next).i == SPECIAL.DID_NOT_BRANCH) {
                        // Special marker ==> False Branch
                        taken = false;
                    } else {
                        // Not a special marker ==> True Branch
                        restore(next); // Remember to put this instruction back on the queue
                        taken = true;
                    }
                    emit(new BranchEvent(iid, this.method, lineNum, taken ? 1 : 0));
                }

                // Save values from GETVALUE_* instructions
                if (ins instanceof GETVALUE) {
                    saveValue((GETVALUE) ins);
                }


                // Emit switch instructions
                if (ins instanceof TABLESWITCH) {
                    // Get parameters
                    TABLESWITCH tableSwitch = (TABLESWITCH) ins;
                    int iid = ins.iid;
                    int lineNum = ins.mid;
                    int value = values.intValue;
                    int numCases = tableSwitch.labels.length;
                    // Compute arm index or else default
                    int arm = -1;
                    if (value >= 0 && value < numCases) {
                        arm = value;
                    }
                    // Emit a branch instruction corresponding to the arm
                    emit(new BranchEvent(iid, this.method, lineNum, arm));
                }

                // Emit switch instructions
                if (ins instanceof LOOKUPSWITCH) {
                    // Get parameters
                    LOOKUPSWITCH tableSwitch = (LOOKUPSWITCH) ins;
                    int iid = ins.iid;
                    int lineNum = ins.mid;
                    int value = values.intValue;
                    int[] cases = tableSwitch.keys;
                    // Compute arm index or else default
                    int arm = -1;
                    for (int i = 0; i < cases.length; i++) {
                        if (value == cases[i]) {
                            arm = i;
                            break;
                        }
                    }
                    // Emit a branch instruction corresponding to the arm
                    emit(new BranchEvent(iid, this.method, lineNum, arm));
                }

                // Emit memory access instructions
                if (ins instanceof HEAPLOAD) {
                    HEAPLOAD heapload = (HEAPLOAD) ins;
                    int iid = heapload.iid;
                    int lineNum = heapload.mid;
                    int objectId = heapload.objectId;
                    String field = heapload.field;
                    // Log the object access (unless it was a NPE)
                    if (objectId != 0) {
                        emit(new ReadEvent(iid, this.method, lineNum, objectId, field));
                    }
                }

                // Emit allocation instructions
                if (ins instanceof NEW) {
                    NEW newInst = (NEW) ins;
                    int iid = newInst.iid;
                    int lineNum = newInst.mid;
                    emit(new AllocEvent(iid, this.method, lineNum, 1));
                } else if (ins instanceof NEWARRAY) {
                    // Note: Array size should be stored in a previous
                    // GETVALUE instructions
                    NEWARRAY newArray = (NEWARRAY) ins;
                    int iid = newArray.iid;
                    int lineNum = newArray.mid;
                    int size = values.intValue;
                    emit(new AllocEvent(iid, this.method, lineNum, size));
                }


                if (isReturnOrMethodThrow(ins)) {
                    emit(new ReturnEvent(ins.iid, this.method, ins.mid));
                    handlers.pop();
                }

                // For non-METHOD_BEGIN instructions, set last IID and lineNum
                this.lastIid = ins.iid;
                this.lastMid = ins.mid;

            }


            return null;
        }
    }

    class MatchingNullHandler implements Callable<Void> {
        @Override
        public Void call() throws InterruptedException {
            Instruction ins = next();
            if (ins instanceof METHOD_BEGIN) {
                handlers.push(new MatchingNullHandler());
            } else if (isReturnOrMethodThrow(ins)) {
                handlers.pop();
            }
            return null;
        }
    }
}

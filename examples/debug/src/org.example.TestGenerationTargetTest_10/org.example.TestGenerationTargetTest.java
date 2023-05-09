/*
 * This file was automatically generated by EvoSuite
 * Mon May 01 04:56:00 GMT 2023
 */

package org.example;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.evosuite.runtime.annotation.EvoSuiteClassExclude;
import org.evosuite.runtime.sandbox.Sandbox;
import org.evosuite.runtime.sandbox.Sandbox.SandboxMode;
import org.example.TestGenerationTarget;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;

@RunWith(EvoRunner.class) @EvoRunnerParameters(mockJVMNonDeterminism = true, useVFS = true, useVNET = true, resetStaticState = true, separateClassLoader = true) 
public class TestGenerationTargetTest {

  @org.junit.Rule
  public org.evosuite.runtime.vnet.NonFunctionalRequirementRule nfr = new org.evosuite.runtime.vnet.NonFunctionalRequirementRule();

  private static final java.util.Properties defaultProperties = (java.util.Properties) java.lang.System.getProperties().clone(); 

  private org.evosuite.runtime.thread.ThreadStopper threadStopper =  new org.evosuite.runtime.thread.ThreadStopper (org.evosuite.runtime.thread.KillSwitchHandler.getInstance(), 3000);


  @BeforeClass
  public static void initEvoSuiteFramework() { 
    org.evosuite.runtime.RuntimeSettings.className = "org.example.TestGenerationTarget"; 
    org.evosuite.runtime.GuiSupport.initialize(); 
    org.evosuite.runtime.RuntimeSettings.maxNumberOfThreads = 100; 
    org.evosuite.runtime.RuntimeSettings.maxNumberOfIterationsPerLoop = 10000; 
    org.evosuite.runtime.RuntimeSettings.mockSystemIn = true; 
    org.evosuite.runtime.RuntimeSettings.sandboxMode = org.evosuite.runtime.sandbox.Sandbox.SandboxMode.RECOMMENDED; 
    org.evosuite.runtime.sandbox.Sandbox.initializeSecurityManagerForSUT(); 
    org.evosuite.runtime.classhandling.JDKClassResetter.init();
    setSystemProperties();
    initializeClasses();
    org.evosuite.runtime.Runtime.getInstance().resetRuntime(); 
  } 

  @AfterClass
  public static void clearEvoSuiteFramework(){ 
    Sandbox.resetDefaultSecurityManager(); 
    java.lang.System.setProperties((java.util.Properties) defaultProperties.clone()); 
  } 

  @Before
  public void initTestCase(){ 
    threadStopper.storeCurrentThreads();
    threadStopper.startRecordingTime();
    org.evosuite.runtime.jvm.ShutdownHookHandler.getInstance().initHandler(); 
    org.evosuite.runtime.sandbox.Sandbox.goingToExecuteSUTCode(); 
    setSystemProperties(); 
    org.evosuite.runtime.GuiSupport.setHeadless(); 
    org.evosuite.runtime.Runtime.getInstance().resetRuntime(); 
    org.evosuite.runtime.agent.InstrumentingAgent.activate(); 
  } 

  @After
  public void doneWithTestCase(){ 
    threadStopper.killAndJoinClientThreads();
    org.evosuite.runtime.jvm.ShutdownHookHandler.getInstance().safeExecuteAddedHooks(); 
    org.evosuite.runtime.classhandling.JDKClassResetter.reset(); 
    resetClasses(); 
    org.evosuite.runtime.sandbox.Sandbox.doneWithExecutingSUTCode(); 
    org.evosuite.runtime.agent.InstrumentingAgent.deactivate(); 
    org.evosuite.runtime.GuiSupport.restoreHeadlessMode(); 
  } 

  public static void setSystemProperties() {
 
    java.lang.System.setProperties((java.util.Properties) defaultProperties.clone()); 
  }

  private static void initializeClasses() {
    org.evosuite.runtime.classhandling.ClassStateSupport.initializeClasses(TestGenerationTargetTest.class.getClassLoader() ,
      "org.example.TestGenerationTarget"
    );
  } 

  private static void resetClasses() {
  }

  @Test(timeout = 4000)
  public void test0()  throws Throwable  {
      String string0 = "";
      TestGenerationTarget.isPalindrome(string0);
      int int0 = 0;
      TestGenerationTarget.isPrime(int0);
      int[] intArray0 = new int[5];
      intArray0[0] = int0;
      intArray0[1] = int0;
      intArray0[2] = int0;
      intArray0[3] = int0;
      intArray0[4] = int0;
      TestGenerationTarget.findMax(intArray0);
  }

  @Test(timeout = 4000)
  public void test1()  throws Throwable  {
      int[] intArray0 = null;
      // Undeclared exception!
      try { 
        TestGenerationTarget.findMax(intArray0);
        fail("Expecting exception: IllegalArgumentException");
      
      } catch(IllegalArgumentException e) {
         //
         // Array cannot be null or empty
         //
         verifyException("org.example.TestGenerationTarget", e);
      }
  }

  @Test(timeout = 4000)
  public void test2()  throws Throwable  {
      int int0 = 83;
      TestGenerationTarget.isPrime(int0);
      int int1 = (-935);
      TestGenerationTarget.isPrime(int1);
      int[] intArray0 = new int[6];
      intArray0[0] = int1;
      intArray0[1] = int0;
      intArray0[2] = int1;
      intArray0[3] = int0;
      intArray0[4] = int1;
      int int2 = (-968);
      intArray0[5] = int2;
      TestGenerationTarget.findMax(intArray0);
      String string0 = "-a63o{p>j(U)I*JG-";
      TestGenerationTarget.isPalindrome(string0);
      TestGenerationTarget.isPalindrome(string0);
      TestGenerationTarget.isPalindrome(string0);
      TestGenerationTarget.findMax(intArray0);
      int int3 = 986;
      TestGenerationTarget.isPalindrome(string0);
      TestGenerationTarget.isPalindrome(string0);
      TestGenerationTarget.findMax(intArray0);
      int int4 = 715;
      TestGenerationTarget.isPrime(int4);
      int int5 = 5;
      TestGenerationTarget.isPrime(int5);
      TestGenerationTarget.isPrime(intArray0[2]);
      String string1 = null;
      TestGenerationTarget.isPalindrome(string1);
      TestGenerationTarget.isPrime(intArray0[2]);
      int int6 = (-2076);
      TestGenerationTarget.isPrime(int6);
      TestGenerationTarget.isPrime(intArray0[5]);
      String string2 = "Array cannot be null or empty";
      TestGenerationTarget.isPalindrome(string2);
      TestGenerationTarget.findMax(intArray0);
      TestGenerationTarget.findMax(intArray0);
      int int7 = 1472;
      TestGenerationTarget.isPrime(int7);
  }

  @Test(timeout = 4000)
  public void test3()  throws Throwable  {
      TestGenerationTarget testGenerationTarget0 = new TestGenerationTarget();
      int[] intArray0 = new int[2];
      int int0 = 0;
      intArray0[0] = int0;
      int int1 = 0;
      intArray0[1] = int1;
      TestGenerationTarget.findMax(intArray0);
      int[] intArray1 = new int[7];
      int int2 = (-922);
      intArray1[0] = int2;
      int int3 = 1;
      intArray1[1] = int3;
      int int4 = 14;
      intArray1[2] = int4;
      int int5 = 0;
      intArray1[3] = int5;
      int int6 = (-2672);
      intArray1[4] = int6;
      int int7 = 453816693;
      intArray1[5] = int7;
      int int8 = (-1);
      intArray1[6] = int8;
      TestGenerationTarget.findMax(intArray1);
  }

  @Test(timeout = 4000)
  public void test4()  throws Throwable  {
      String string0 = "";
      TestGenerationTarget.isPalindrome(string0);
      TestGenerationTarget testGenerationTarget0 = new TestGenerationTarget();
      int[] intArray0 = new int[8];
      int int0 = (-1699);
      intArray0[0] = int0;
      int int1 = (-1871278698);
      intArray0[1] = int1;
      int int2 = (-2532);
      intArray0[2] = int2;
      int int3 = (-287);
      intArray0[3] = int3;
      int int4 = 525;
      intArray0[4] = int4;
      int int5 = 1938;
      intArray0[5] = int5;
      int int6 = 312;
      intArray0[6] = int6;
      int int7 = 2;
      intArray0[7] = int7;
      TestGenerationTarget.findMax(intArray0);
      int[] intArray1 = new int[3];
      int int8 = (-71);
      intArray1[0] = int8;
      int int9 = (-2057023229);
      intArray1[1] = int9;
      int int10 = (-1);
      intArray1[2] = int10;
      TestGenerationTarget.findMax(intArray1);
      TestGenerationTarget.isPalindrome(string0);
  }

  @Test(timeout = 4000)
  public void test5()  throws Throwable  {
      TestGenerationTarget testGenerationTarget0 = new TestGenerationTarget();
      int[] intArray0 = new int[7];
      int int0 = (-806);
      intArray0[0] = int0;
      int int1 = (-1);
      intArray0[1] = int1;
      int int2 = 3782;
      intArray0[2] = int2;
      int int3 = 1844;
      intArray0[3] = int3;
      int int4 = 0;
      intArray0[4] = int4;
      int int5 = 1970;
      intArray0[5] = int5;
      int int6 = (-1578608441);
      intArray0[6] = int6;
      TestGenerationTarget.findMax(intArray0);
      int[] intArray1 = new int[7];
      int int7 = 122;
      intArray1[0] = int7;
      int int8 = 1035;
      intArray1[1] = int8;
      int int9 = (-1099);
      intArray1[2] = int9;
      int int10 = 572;
      intArray1[3] = int10;
      int int11 = (-2562);
      intArray1[4] = int11;
      int int12 = 1;
      intArray1[5] = int12;
      int int13 = (-795);
      intArray1[6] = int13;
      TestGenerationTarget.findMax(intArray1);
      int[] intArray2 = new int[1];
      int int14 = 1;
      intArray2[0] = int14;
      TestGenerationTarget.findMax(intArray2);
      int[] intArray3 = new int[0];
      // Undeclared exception!
      try { 
        TestGenerationTarget.findMax(intArray3);
        fail("Expecting exception: IllegalArgumentException");
      
      } catch(IllegalArgumentException e) {
         //
         // Array cannot be null or empty
         //
         verifyException("org.example.TestGenerationTarget", e);
      }
  }

  @Test(timeout = 4000)
  public void test6()  throws Throwable  {
      String string0 = "Array cannot be null or empty";
      TestGenerationTarget.isPalindrome(string0);
      String string1 = "";
      TestGenerationTarget.isPalindrome(string1);
      int int0 = 4;
      TestGenerationTarget.isPrime(int0);
      TestGenerationTarget.isPrime(int0);
      int int1 = 1409;
      TestGenerationTarget.isPrime(int1);
      int[] intArray0 = new int[2];
      intArray0[0] = int1;
      intArray0[1] = int1;
      TestGenerationTarget.findMax(intArray0);
      String string2 = "j";
      TestGenerationTarget.isPalindrome(string2);
      TestGenerationTarget.isPalindrome(string0);
      TestGenerationTarget.findMax(intArray0);
      String string3 = "";
      TestGenerationTarget.isPalindrome(string3);
      int int2 = (-2633);
      TestGenerationTarget.isPrime(int2);
      String string4 = "";
      TestGenerationTarget.isPalindrome(string4);
  }
}

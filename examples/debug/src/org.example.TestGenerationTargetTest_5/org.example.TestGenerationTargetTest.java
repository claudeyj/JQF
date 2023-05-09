/*
 * This file was automatically generated by EvoSuite
 * Wed May 03 03:35:18 GMT 2023
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
  public void test00()  throws Throwable  {
      int[] intArray0 = new int[3];
      int int0 = 1313;
      intArray0[0] = int0;
      int int1 = 0;
      intArray0[1] = int1;
      int int2 = 1;
      intArray0[2] = int2;
      TestGenerationTarget.findMax(intArray0);
      TestGenerationTarget.findMax(intArray0);
      String string0 = ".";
      TestGenerationTarget.isPalindrome(string0);
      TestGenerationTarget.isPrime(int2);
      String string1 = "WU!reE)S~ 4{vlt";
      TestGenerationTarget.isPalindrome(string1);
      TestGenerationTarget.findMax(intArray0);
  }

  @Test(timeout = 4000)
  public void test01()  throws Throwable  {
      int[] intArray0 = new int[6];
      int int0 = 1450;
      intArray0[0] = int0;
      int int1 = (-2768);
      intArray0[1] = int1;
      int int2 = (-1189);
      intArray0[2] = int2;
      int int3 = (-1);
      intArray0[3] = int3;
      int int4 = (-449);
      intArray0[4] = int4;
      int int5 = (-1522);
      intArray0[5] = int5;
      TestGenerationTarget.findMax(intArray0);
      TestGenerationTarget.findMax(intArray0);
      String string0 = "Array cannot be null or empty";
      TestGenerationTarget.isPalindrome(string0);
      int int6 = 2;
      TestGenerationTarget.isPrime(int6);
      TestGenerationTarget.findMax(intArray0);
      TestGenerationTarget.isPalindrome(string0);
      TestGenerationTarget testGenerationTarget0 = new TestGenerationTarget();
      TestGenerationTarget.findMax(intArray0);
      TestGenerationTarget.findMax(intArray0);
      TestGenerationTarget.findMax(intArray0);
      TestGenerationTarget.isPrime(intArray0[5]);
      TestGenerationTarget.findMax(intArray0);
  }

  @Test(timeout = 4000)
  public void test02()  throws Throwable  {
      int[] intArray0 = new int[1];
      int int0 = (-538);
      intArray0[0] = int0;
      TestGenerationTarget.findMax(intArray0);
  }

  @Test(timeout = 4000)
  public void test03()  throws Throwable  {
      TestGenerationTarget testGenerationTarget0 = new TestGenerationTarget();
      int[] intArray0 = new int[5];
      int int0 = (-2391);
      intArray0[0] = int0;
      int int1 = 0;
      intArray0[1] = int1;
      int int2 = 14;
      intArray0[2] = int2;
      int int3 = (-3847);
      intArray0[3] = int3;
      int int4 = 3;
      intArray0[4] = int4;
      TestGenerationTarget.findMax(intArray0);
      String string0 = "";
      TestGenerationTarget.isPalindrome(string0);
  }

  @Test(timeout = 4000)
  public void test04()  throws Throwable  {
      int int0 = 230;
      TestGenerationTarget.isPrime(int0);
      String string0 = "jM!}[pA=IG1czvHK_F";
      TestGenerationTarget.isPalindrome(string0);
      TestGenerationTarget.isPalindrome(string0);
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
  public void test05()  throws Throwable  {
      int int0 = (-363);
      TestGenerationTarget.isPrime(int0);
      int[] intArray0 = new int[4];
      intArray0[0] = int0;
      int int1 = 7;
      intArray0[1] = int1;
      intArray0[2] = int0;
      intArray0[3] = int0;
      int int2 = TestGenerationTarget.findMax(intArray0);
      int int3 = 3147;
      TestGenerationTarget.isPrime(int3);
      TestGenerationTarget.isPrime(int2);
      TestGenerationTarget.isPrime(intArray0[0]);
      TestGenerationTarget testGenerationTarget0 = new TestGenerationTarget();
      TestGenerationTarget.findMax(intArray0);
      int[] intArray1 = null;
      // Undeclared exception!
      try { 
        TestGenerationTarget.findMax(intArray1);
        fail("Expecting exception: IllegalArgumentException");
      
      } catch(IllegalArgumentException e) {
         //
         // Array cannot be null or empty
         //
         verifyException("org.example.TestGenerationTarget", e);
      }
  }

  @Test(timeout = 4000)
  public void test06()  throws Throwable  {
      int int0 = 1620;
      TestGenerationTarget.isPrime(int0);
      TestGenerationTarget testGenerationTarget0 = new TestGenerationTarget();
      int[] intArray0 = new int[3];
      intArray0[0] = int0;
      intArray0[1] = int0;
      intArray0[2] = int0;
      TestGenerationTarget.findMax(intArray0);
      int[] intArray1 = new int[9];
      intArray1[0] = int0;
      intArray1[1] = int0;
      intArray1[2] = int0;
      intArray1[3] = int0;
      intArray1[4] = int0;
      intArray1[5] = int0;
      intArray1[6] = int0;
      intArray1[7] = int0;
      intArray1[8] = int0;
      TestGenerationTarget.findMax(intArray1);
      int[] intArray2 = new int[5];
      intArray2[0] = int0;
      intArray2[1] = int0;
      intArray2[2] = int0;
      int int1 = 2144;
      intArray2[3] = int1;
      intArray2[4] = int0;
      TestGenerationTarget.findMax(intArray2);
      int[] intArray3 = null;
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
  public void test07()  throws Throwable  {
      String string0 = "";
      TestGenerationTarget.isPalindrome(string0);
      int[] intArray0 = new int[1];
      int int0 = 1;
      intArray0[0] = int0;
      TestGenerationTarget.findMax(intArray0);
      TestGenerationTarget.findMax(intArray0);
      TestGenerationTarget testGenerationTarget0 = new TestGenerationTarget();
      TestGenerationTarget.findMax(intArray0);
      int int1 = TestGenerationTarget.findMax(intArray0);
      String string1 = ":^Sge7IS.";
      TestGenerationTarget.isPalindrome(string1);
      String string2 = "(rn9vPdE[*jM!}";
      TestGenerationTarget.isPalindrome(string2);
      int int2 = (-363);
      TestGenerationTarget.isPrime(int2);
      TestGenerationTarget.isPrime(int1);
      TestGenerationTarget.findMax(intArray0);
      TestGenerationTarget.findMax(intArray0);
      int int3 = 91;
      TestGenerationTarget.isPrime(int3);
      TestGenerationTarget.isPrime(intArray0[0]);
      TestGenerationTarget.isPrime(int1);
      String string3 = "";
      TestGenerationTarget.isPalindrome(string3);
  }

  @Test(timeout = 4000)
  public void test08()  throws Throwable  {
      String string0 = "K1\"i\\@J9|L^WXC";
      TestGenerationTarget.isPalindrome(string0);
  }

  @Test(timeout = 4000)
  public void test09()  throws Throwable  {
      int int0 = 75;
      TestGenerationTarget.isPrime(int0);
      int[] intArray0 = new int[6];
      intArray0[0] = int0;
      intArray0[1] = int0;
      intArray0[2] = int0;
      intArray0[3] = int0;
      intArray0[4] = int0;
      intArray0[5] = int0;
      TestGenerationTarget.findMax(intArray0);
      TestGenerationTarget testGenerationTarget0 = new TestGenerationTarget();
      int[] intArray1 = new int[0];
      // Undeclared exception!
      try { 
        TestGenerationTarget.findMax(intArray1);
        fail("Expecting exception: IllegalArgumentException");
      
      } catch(IllegalArgumentException e) {
         //
         // Array cannot be null or empty
         //
         verifyException("org.example.TestGenerationTarget", e);
      }
  }

  @Test(timeout = 4000)
  public void test10()  throws Throwable  {
      String string0 = "Array cannot be null or empty";
      TestGenerationTarget.isPalindrome(string0);
      String string1 = null;
      TestGenerationTarget.isPalindrome(string1);
      String string2 = "eE)S~ 4{vl";
      TestGenerationTarget.isPalindrome(string2);
      int[] intArray0 = new int[0];
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
  public void test11()  throws Throwable  {
      int int0 = 1893;
      TestGenerationTarget.isPrime(int0);
      String string0 = "";
      TestGenerationTarget.isPalindrome(string0);
      int int1 = 1109;
      TestGenerationTarget.isPrime(int1);
      TestGenerationTarget.isPrime(int1);
      TestGenerationTarget.isPalindrome(string0);
      TestGenerationTarget testGenerationTarget0 = new TestGenerationTarget();
      int[] intArray0 = new int[9];
      intArray0[0] = int1;
      intArray0[1] = int1;
      intArray0[2] = int0;
      intArray0[3] = int0;
      intArray0[4] = int1;
      intArray0[5] = int1;
      intArray0[6] = int1;
      intArray0[7] = int1;
      intArray0[8] = int0;
      TestGenerationTarget.findMax(intArray0);
      int[] intArray1 = new int[3];
      int int2 = (-757);
      intArray1[0] = int2;
      intArray1[1] = int0;
      intArray1[2] = int0;
      TestGenerationTarget.findMax(intArray1);
      TestGenerationTarget.isPrime(int0);
  }

  @Test(timeout = 4000)
  public void test12()  throws Throwable  {
      String string0 = "~0Y(H{Tp`EN\\~Brx";
      TestGenerationTarget.isPalindrome(string0);
      int[] intArray0 = new int[0];
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
  public void test13()  throws Throwable  {
      TestGenerationTarget testGenerationTarget0 = new TestGenerationTarget();
      int[] intArray0 = new int[6];
      int int0 = 6;
      intArray0[0] = int0;
      int int1 = 65;
      intArray0[1] = int1;
      int int2 = (-153);
      intArray0[2] = int2;
      int int3 = 2;
      intArray0[3] = int3;
      int int4 = 805;
      intArray0[4] = int4;
      int int5 = (-1647);
      intArray0[5] = int5;
      TestGenerationTarget.findMax(intArray0);
      int[] intArray1 = new int[1];
      int int6 = 20;
      intArray1[0] = int6;
      TestGenerationTarget.findMax(intArray1);
      int[] intArray2 = new int[3];
      int int7 = 0;
      intArray2[0] = int7;
      int int8 = 0;
      intArray2[1] = int8;
      int int9 = (-4246);
      intArray2[2] = int9;
      TestGenerationTarget.findMax(intArray2);
      int[] intArray3 = new int[6];
      int int10 = 1;
      intArray3[0] = int10;
      int int11 = 1893;
      intArray3[1] = int11;
      int int12 = (-2095747799);
      intArray3[2] = int12;
      int int13 = 883029510;
      intArray3[3] = int13;
      int int14 = 5;
      intArray3[4] = int14;
      int int15 = (-1);
      intArray3[5] = int15;
      TestGenerationTarget.findMax(intArray3);
      int int16 = (-613);
      TestGenerationTarget.isPrime(int16);
  }

  @Test(timeout = 4000)
  public void test14()  throws Throwable  {
      String string0 = "Xi`gqPzr#+";
      TestGenerationTarget.isPalindrome(string0);
      int int0 = (-1403);
      TestGenerationTarget.isPrime(int0);
      int[] intArray0 = new int[3];
      intArray0[0] = int0;
      int int1 = 626;
      intArray0[1] = int1;
      intArray0[2] = int0;
      int int2 = TestGenerationTarget.findMax(intArray0);
      TestGenerationTarget.isPalindrome(string0);
      TestGenerationTarget.isPrime(int2);
      int int3 = 1223;
      TestGenerationTarget.isPrime(int3);
      TestGenerationTarget.findMax(intArray0);
      TestGenerationTarget.findMax(intArray0);
      int int4 = TestGenerationTarget.findMax(intArray0);
      int int5 = 65;
      TestGenerationTarget.isPrime(int5);
      int int6 = (-2324);
      TestGenerationTarget.isPrime(int6);
      TestGenerationTarget.isPalindrome(string0);
      TestGenerationTarget.isPalindrome(string0);
      TestGenerationTarget.isPalindrome(string0);
      int int7 = (-121);
      TestGenerationTarget.isPrime(int7);
      TestGenerationTarget.isPrime(int5);
      int int8 = TestGenerationTarget.findMax(intArray0);
      TestGenerationTarget.isPalindrome(string0);
      TestGenerationTarget.isPrime(int8);
      int[] intArray1 = new int[6];
      intArray1[0] = int3;
      intArray1[1] = int1;
      intArray1[2] = int5;
      intArray1[3] = int7;
      intArray1[4] = int4;
      intArray1[5] = int2;
      TestGenerationTarget.findMax(intArray1);
      TestGenerationTarget testGenerationTarget0 = new TestGenerationTarget();
      TestGenerationTarget.findMax(intArray0);
  }

  @Test(timeout = 4000)
  public void test15()  throws Throwable  {
      int int0 = 3411;
      TestGenerationTarget.isPrime(int0);
      TestGenerationTarget.isPrime(int0);
      int int1 = (-2340);
      TestGenerationTarget.isPrime(int1);
      String string0 = "";
      TestGenerationTarget.isPalindrome(string0);
      String string1 = "5z$bog";
      TestGenerationTarget.isPalindrome(string1);
      TestGenerationTarget.isPrime(int0);
      int[] intArray0 = new int[4];
      intArray0[0] = int1;
      int int2 = 1;
      intArray0[1] = int2;
      int int3 = (-1489);
      intArray0[2] = int3;
      intArray0[3] = int1;
      TestGenerationTarget.findMax(intArray0);
      int int4 = TestGenerationTarget.findMax(intArray0);
      TestGenerationTarget.isPalindrome(string0);
      TestGenerationTarget.isPalindrome(string1);
      TestGenerationTarget.isPalindrome(string0);
      String string2 = "";
      TestGenerationTarget.isPalindrome(string2);
      int int5 = TestGenerationTarget.findMax(intArray0);
      TestGenerationTarget.isPrime(int4);
      TestGenerationTarget.isPrime(int5);
      String string3 = "veIwd]Usu]`zeoY";
      TestGenerationTarget.isPalindrome(string3);
      int int6 = 4;
      TestGenerationTarget.isPrime(int6);
      TestGenerationTarget.findMax(intArray0);
      TestGenerationTarget.findMax(intArray0);
      TestGenerationTarget testGenerationTarget0 = new TestGenerationTarget();
      int int7 = 8;
      TestGenerationTarget.isPrime(int7);
      TestGenerationTarget.isPalindrome(string1);
  }

  @Test(timeout = 4000)
  public void test16()  throws Throwable  {
      int int0 = 1052;
      TestGenerationTarget.isPrime(int0);
      String string0 = "4>~>gf*K*lQObfLdX7";
      TestGenerationTarget.isPalindrome(string0);
      TestGenerationTarget.isPrime(int0);
      TestGenerationTarget testGenerationTarget0 = new TestGenerationTarget();
      int[] intArray0 = new int[3];
      intArray0[0] = int0;
      intArray0[1] = int0;
      intArray0[2] = int0;
      TestGenerationTarget.findMax(intArray0);
      TestGenerationTarget.isPalindrome(string0);
  }

  @Test(timeout = 4000)
  public void test17()  throws Throwable  {
      int[] intArray0 = new int[3];
      int int0 = 65;
      intArray0[0] = int0;
      int int1 = (-1041);
      intArray0[1] = int1;
      int int2 = 545;
      intArray0[2] = int2;
      int int3 = TestGenerationTarget.findMax(intArray0);
      TestGenerationTarget.findMax(intArray0);
      TestGenerationTarget.isPrime(int3);
      String string0 = "_4ZkDh>};7~";
      TestGenerationTarget.isPalindrome(string0);
      TestGenerationTarget.findMax(intArray0);
      TestGenerationTarget.isPalindrome(string0);
      int int4 = TestGenerationTarget.findMax(intArray0);
      String string1 = "";
      TestGenerationTarget.isPalindrome(string1);
      int int5 = 611;
      TestGenerationTarget.isPrime(int5);
      String string2 = "F?OqJrG,F";
      TestGenerationTarget.isPalindrome(string2);
      String string3 = "'hA+ua%rV\"^sviz=)II";
      TestGenerationTarget.isPrime(int1);
      TestGenerationTarget.isPrime(int4);
      TestGenerationTarget testGenerationTarget0 = new TestGenerationTarget();
  }
}

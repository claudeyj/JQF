/*
 * This file was automatically generated by EvoSuite
 * Thu May 04 04:30:35 GMT 2023
 */

package org.example;

import org.junit.Test;
import static org.junit.Assert.*;
import org.example.TestGenerationTarget;

public class TestGenerationTarget_ESTest {

  @Test(timeout = 4000)
  public void test00()  throws Throwable  {
      TestGenerationTarget testGenerationTarget0 = new TestGenerationTarget();
  }

  @Test(timeout = 4000)
  public void test01()  throws Throwable  {
      boolean boolean0 = TestGenerationTarget.isPalindrome((String) null);
      assertFalse(boolean0);
  }

  @Test(timeout = 4000)
  public void test02()  throws Throwable  {
      boolean boolean0 = TestGenerationTarget.isPalindrome("");
      assertTrue(boolean0);
  }

  @Test(timeout = 4000)
  public void test03()  throws Throwable  {
      boolean boolean0 = TestGenerationTarget.isPalindrome("PXJD");
      assertFalse(boolean0);
  }

  @Test(timeout = 4000)
  public void test04()  throws Throwable  {
      // Undeclared exception!
      try { 
        TestGenerationTarget.findMax((int[]) null);
        fail("Expecting exception: IllegalArgumentException");
      
      } catch(IllegalArgumentException e) {
         //
         // Array cannot be null or empty
         //
      }
  }

  @Test(timeout = 4000)
  public void test05()  throws Throwable  {
      int[] intArray0 = new int[2];
      int int0 = TestGenerationTarget.findMax(intArray0);
      assertEquals(0, int0);
  }

  @Test(timeout = 4000)
  public void test06()  throws Throwable  {
      int[] intArray0 = new int[0];
      // Undeclared exception!
      try { 
        TestGenerationTarget.findMax(intArray0);
        fail("Expecting exception: IllegalArgumentException");
      
      } catch(IllegalArgumentException e) {
         //
         // Array cannot be null or empty
         //
      }
  }

  @Test(timeout = 4000)
  public void test07()  throws Throwable  {
      boolean boolean0 = TestGenerationTarget.isPrime(2339);
      assertTrue(boolean0);
  }

  @Test(timeout = 4000)
  public void test08()  throws Throwable  {
      boolean boolean0 = TestGenerationTarget.isPrime((-2397));
      assertFalse(boolean0);
  }

  @Test(timeout = 4000)
  public void test09()  throws Throwable  {
      boolean boolean0 = TestGenerationTarget.isPrime(4);
      assertFalse(boolean0);
  }

  @Test(timeout = 4000)
  public void test10()  throws Throwable  {
      int[] intArray0 = new int[2];
      intArray0[0] = (-1777);
      intArray0[1] = (-1777);
      int int0 = TestGenerationTarget.findMax(intArray0);
      assertEquals((-1777), int0);
  }

  @Test(timeout = 4000)
  public void test11()  throws Throwable  {
      boolean boolean0 = TestGenerationTarget.isPalindrome("<so;>0@PYIz");
      assertFalse(boolean0);
  }

  @Test(timeout = 4000)
  public void test12()  throws Throwable  {
      int[] intArray0 = new int[4];
      intArray0[1] = 1153;
      int int0 = TestGenerationTarget.findMax(intArray0);
      assertEquals(1153, int0);
  }
}
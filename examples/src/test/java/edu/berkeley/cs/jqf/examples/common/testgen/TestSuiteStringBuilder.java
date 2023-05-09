package edu.berkeley.cs.jqf.examples.common.testgen;

import java.util.List;

import org.evosuite.junit.naming.methods.NumberedTestNameGenerationStrategy;
import org.evosuite.junit.writer.TestSuiteWriter;
import org.evosuite.testcase.TestCase;
import org.evosuite.testcase.execution.ExecutionResult;

/*
 * Dummy class of the same thing in test directory
 */
public class TestSuiteStringBuilder extends TestSuiteWriter{
    // private NumberedTestNameGenerationStrategy nameGenerator;

    public void setTestCases(List<TestCase> testCases) {
        this.testCases = testCases;
    }
    
    public void setNameGenerator(List<TestCase> testCases, List<ExecutionResult> results) {
        super.nameGenerator = new NumberedTestNameGenerationStrategy(testCases, results);
    }

    public String formattedTest(List<TestCase> testCases, String targetClass, List<ExecutionResult> results) {
        String testClassName = targetClass.substring(targetClass.lastIndexOf('.') + 1) + "Test";
        String content = getUnitTestsAllInSameFile(testClassName, results);
        return content;
        // return content.substring(0, content.lastIndexOf("}"))
        // + getDebugTest() + "\n}";
    }

    public String getDebugTest() {
        return "@Test\n" 
        + "public void test100() throws Throwable {\n" 
        // + "try {\n"
        + "System.out.println(this.getClass().getClassLoader());\n"
        + "System.out.println(TestGenerationTarget.class.getClassLoader());\n"
        + "try {\n"
        + "Class<?> sutClass = java.lang.Class.forName(System.getProperty(\"jqf.cut\"), true, Thread.currentThread().getContextClassLoader());\n"
        + "Object sut = sutClass.getConstructors()[0].newInstance();\n"
        + "java.lang.reflect.Method findMaxMethod = sutClass.getMethod(\"findMax\", int[].class);\n"
        + "Object ret = findMaxMethod.invoke(sut, new Object[]{new int[]{1, 2, 3}});\n"
        + "System.out.println((Integer)ret);\n"
        + "System.out.println(\"Debug for sut class: \" + sutClass.getClassLoader());\n"
        + "System.out.println(\"Debug for Math, see whether it can be traced: \" + org.apache.commons.math3.util.FastMath.abs(300 - 500));\n"
        + "throw new RuntimeException(\"Show me the stack trace\");\n"
        + "} catch (Exception e) {\n"
        + "e.printStackTrace();\n"
        + "}\n"
        + "System.out.println(\"This test is truly executed\");\n"
        // + "} catch (Throwable e) {\n"
        // + "e.printStackTrace();\n"
        // + "}"
        + "\n}";
    }

}

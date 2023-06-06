package edu.berkeley.cs.jqf.examples.testgen;

import static org.junit.jupiter.api.Assumptions.assumeTrue;

import org.junit.runner.RunWith;

import com.pholser.junit.quickcheck.From;

import edu.berkeley.cs.jqf.examples.common.testgen.TestGenerationConfiguration;
import edu.berkeley.cs.jqf.fuzz.Fuzz;
import edu.berkeley.cs.jqf.fuzz.JQF;

@RunWith(JQF.class)
public class CGFDriverTest {

    @Fuzz
    public void testWithGenerator(@From (CGFTestSuiteGenerator.class) 
        @TestGenerationConfiguration(targetClassCanonicalName = "org.example.TestGenerationTarget", 
        projectClassPath = "project/bin") String testSuiteContent){
        try {
            assumeTrue(testSuiteContent != null);
            String targetRootDir = "cut_target";
            String debugRootDir = "debug";
            TestEvaluator evaluator = new TestEvaluator("project/bin", targetRootDir, debugRootDir);
            //log cov here
            
            assumeTrue(evaluator.compile("org.example.TestGenerationTargetTest", testSuiteContent));
            evaluator.execute("org.example.TestGenerationTargetTest");
        } 
        catch (Exception e) {
        // catch (ClassNotFoundException | MalformedURLException e) {
            e.printStackTrace();
        }
        }

    @Fuzz
    public void testWithGenerator2(@From (CGFTestSuiteGenerator.class)
        @TestGenerationConfiguration(targetClassCanonicalName = "com.ib.client.AnyWrapperMsgGenerator",
        projectClassPath = "SF110-20130704/1_tullibee/tullibee.jar") String testSuiteContent){
        try {
            assumeTrue(testSuiteContent != null);
            String targetRootDir = "cut_target/1_tullibee";
            String debugRootDir = "debug/1_tullibee";
            TestEvaluator evaluator = new TestEvaluator("SF110-20130704/1_tullibee/tullibee.jar", targetRootDir, debugRootDir);

            assumeTrue(evaluator.compile("com.ib.client.AnyWrapperMsgGeneratorTest", testSuiteContent));
            evaluator.execute("com.ib.client.AnyWrapperMsgGeneratorTest");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Fuzz
    public void testWithGenerator_A4j(@From (CGFTestSuiteGenerator.class)
        @TestGenerationConfiguration(targetClassCanonicalName = "net.kencochrane.a4j.A4j",
        projectClassPath = "SF110-20130704/2_a4j/a4j.jar" +
        ":SF110-20130704/2_a4j/lib/jox116.jar" +
        ":SF110-20130704/2_a4j/lib/log4j-1.2.4.jar") String testSuiteContent){
        try {
            assumeTrue(testSuiteContent != null);
            String targetRootDir = "cut_target/2_a4j";
            String debugRootDir = "debug/2_a4j";
            TestEvaluator evaluator = new TestEvaluator("SF110-20130704/2_a4j/a4j.jar" +
            ":SF110-20130704/2_a4j/lib/jox116.jar" +
            ":SF110-20130704/2_a4j/lib/log4j-1.2.4.jar", targetRootDir, debugRootDir);

            assumeTrue(evaluator.compile("net.kencochrane.a4j.A4jTest", testSuiteContent));
            evaluator.execute("net.kencochrane.a4j.A4jTest");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Fuzz
    public void testWithGenerator_Accessories(@From (CGFTestSuiteGenerator.class)
        @TestGenerationConfiguration(targetClassCanonicalName = "net.kencochrane.a4j.beans.Accessories",
        projectClassPath = "SF110-20130704/2_a4j/a4j.jar" +
        ":SF110-20130704/2_a4j/lib/jox116.jar" +
        ":SF110-20130704/2_a4j/lib/log4j-1.2.4.jar") String testSuiteContent){
        try {
            assumeTrue(testSuiteContent != null);
            String targetRootDir = "cut_target/2_a4j";
            String debugRootDir = "debug/2_a4j";
            TestEvaluator evaluator = new TestEvaluator("SF110-20130704/2_a4j/a4j.jar" +
            ":SF110-20130704/2_a4j/lib/jox116.jar" +
            ":SF110-20130704/2_a4j/lib/log4j-1.2.4.jar", targetRootDir, debugRootDir);

            assumeTrue(evaluator.compile("net.kencochrane.a4j.beans.AccessoriesTest", testSuiteContent));
            evaluator.execute("net.kencochrane.a4j.beans.AccessoriesTest");
        } catch (Exception e) {
            e.printStackTrace();
        }
    } 

    @Fuzz
    public void testWithGenerator_Artists(@From (CGFTestSuiteGenerator.class)
        @TestGenerationConfiguration(targetClassCanonicalName = "net.kencochrane.a4j.beans.Artists",
        projectClassPath = "SF110-20130704/2_a4j/a4j.jar" +
        ":SF110-20130704/2_a4j/lib/jox116.jar" +
        ":SF110-20130704/2_a4j/lib/log4j-1.2.4.jar") String testSuiteContent){
        try {
            assumeTrue(testSuiteContent != null);
            String targetRootDir = "cut_target/2_a4j";
            String debugRootDir = "debug/2_a4j";
            TestEvaluator evaluator = new TestEvaluator("SF110-20130704/2_a4j/a4j.jar" +
            ":SF110-20130704/2_a4j/lib/jox116.jar" +
            ":SF110-20130704/2_a4j/lib/log4j-1.2.4.jar", targetRootDir, debugRootDir);

            assumeTrue(evaluator.compile("net.kencochrane.a4j.beans.ArtistsTest", testSuiteContent));
            evaluator.execute("net.kencochrane.a4j.beans.ArtistsTest");
        } catch (Exception e) {
            e.printStackTrace();
        }
    } 

    @Fuzz
    public void testWithGenerator_Authors(@From (CGFTestSuiteGenerator.class)
        @TestGenerationConfiguration(targetClassCanonicalName = "net.kencochrane.a4j.beans.Authors",
        projectClassPath = "SF110-20130704/2_a4j/a4j.jar" +
        ":SF110-20130704/2_a4j/lib/jox116.jar" +
        ":SF110-20130704/2_a4j/lib/log4j-1.2.4.jar") String testSuiteContent){
        try {
            assumeTrue(testSuiteContent != null);
            String targetRootDir = "cut_target/2_a4j";
            String debugRootDir = "debug/2_a4j";
            TestEvaluator evaluator = new TestEvaluator("SF110-20130704/2_a4j/a4j.jar" +
            ":SF110-20130704/2_a4j/lib/jox116.jar" +
            ":SF110-20130704/2_a4j/lib/log4j-1.2.4.jar", targetRootDir, debugRootDir);

            assumeTrue(evaluator.compile("net.kencochrane.a4j.beans.AuthorsTest", testSuiteContent));
            evaluator.execute("net.kencochrane.a4j.beans.AuthorsTest");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Fuzz
    public void testWithGenerator_BlendedSearch(@From (CGFTestSuiteGenerator.class)
        @TestGenerationConfiguration(targetClassCanonicalName = "net.kencochrane.a4j.beans.BlendedSearch",
        projectClassPath = "SF110-20130704/2_a4j/a4j.jar" +
        ":SF110-20130704/2_a4j/lib/jox116.jar" +
        ":SF110-20130704/2_a4j/lib/log4j-1.2.4.jar") String testSuiteContent){
        try {
            assumeTrue(testSuiteContent != null);
            String targetRootDir = "cut_target/2_a4j";
            String debugRootDir = "debug/2_a4j";
            TestEvaluator evaluator = new TestEvaluator("SF110-20130704/2_a4j/a4j.jar" +
            ":SF110-20130704/2_a4j/lib/jox116.jar" +
            ":SF110-20130704/2_a4j/lib/log4j-1.2.4.jar", targetRootDir, debugRootDir);

            assumeTrue(evaluator.compile("net.kencochrane.a4j.beans.BlendedSearchTest", testSuiteContent));
            evaluator.execute("net.kencochrane.a4j.beans.BlendedSearchTest");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Fuzz
    public void testWithGenerator_Product(@From (CGFTestSuiteGenerator.class)
        @TestGenerationConfiguration(targetClassCanonicalName = "net.kencochrane.a4j.DAO.Product",
        projectClassPath = "SF110-20130704/2_a4j/a4j.jar" +
        ":SF110-20130704/2_a4j/lib/jox116.jar" +
        ":SF110-20130704/2_a4j/lib/log4j-1.2.4.jar") String testSuiteContent){
        try {
            assumeTrue(testSuiteContent != null);
            String targetRootDir = "cut_target/2_a4j";
            String debugRootDir = "debug/2_a4j";
            TestEvaluator evaluator = new TestEvaluator("SF110-20130704/2_a4j/a4j.jar" +
            ":SF110-20130704/2_a4j/lib/jox116.jar" +
            ":SF110-20130704/2_a4j/lib/log4j-1.2.4.jar", targetRootDir, debugRootDir);

            assumeTrue(evaluator.compile("net.kencochrane.a4j.DAO.ProductTest", testSuiteContent));
            evaluator.execute("net.kencochrane.a4j.DAO.ProductTest");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
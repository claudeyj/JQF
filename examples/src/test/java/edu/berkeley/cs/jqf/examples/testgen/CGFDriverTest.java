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
        projectClassPath = "/home/jun/research/test_gen/JQF/examples/src/main/resources/project/bin") String testSuiteContent){
        try {
            assumeTrue(testSuiteContent != null);
            String targetRootDir = "cut_target";
            String debugRootDir = "debug";
            TestEvaluator evaluator = new TestEvaluator("/home/jun/research/test_gen/JQF/examples/src/main/resources/project/bin", targetRootDir, debugRootDir);
            //log cov here
            
            assumeTrue(evaluator.compile("org.example.TestGenerationTargetTest", testSuiteContent));
            assumeTrue(evaluator.execute("org.example.TestGenerationTargetTest"));
            } 
            catch (Exception e) {
            // catch (ClassNotFoundException | MalformedURLException e) {
                e.printStackTrace();
            }
        }
}

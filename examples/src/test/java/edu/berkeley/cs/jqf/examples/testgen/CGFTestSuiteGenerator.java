package edu.berkeley.cs.jqf.examples.testgen;

import com.pholser.junit.quickcheck.generator.GenerationStatus;
import com.pholser.junit.quickcheck.generator.Generator;
import com.pholser.junit.quickcheck.random.SourceOfRandomness;

import edu.berkeley.cs.jqf.examples.common.testgen.EvoSuiteTestGenerator;
import edu.berkeley.cs.jqf.examples.common.testgen.TestGenerationConfiguration;

/**
 * A generator for Test Suites with CGF.
 * @author Jun Yang
 */
public class CGFTestSuiteGenerator extends Generator<String>{
    private static int counter = 0;

    private EvoSuiteTestGenerator evoSuiteTestGenerator = new EvoSuiteTestGenerator();
    public CGFTestSuiteGenerator() {
        super(String.class);
    }

    public void configure(TestGenerationConfiguration config) {
        evoSuiteTestGenerator.configure(config);
    }

    @Override
    public String generate(SourceOfRandomness random, GenerationStatus status) {
        counter += 1;
        System.out.println("Generating test suite with CGF: " + counter);
        return evoSuiteTestGenerator.generateTestSuite(random);
    }
}

package edu.berkeley.cs.jqf.examples.common.testgen;

import java.nio.file.Paths;
import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.evosuite.Properties;
import org.evosuite.TestSuiteGenerator;
import org.evosuite.result.TestGenerationResult;
import org.evosuite.testcase.TestCase;
import org.evosuite.testcase.execution.ExecutionResult;
import org.evosuite.testsuite.TestSuiteChromosome;
import org.evosuite.utils.Randomness;

import com.pholser.junit.quickcheck.random.SourceOfRandomness;

import edu.berkeley.cs.jqf.fuzz.guidance.StreamBackedRandom;

public class EvoSuiteTestGenerator {
    private String projectCP;
    private String targetClass;
    private final Properties.StoppingCondition stoppingCondition = Properties.StoppingCondition.MAXTIME; //hard coded for now
    private final int searchBudget = 10; //hard coded for now
    private final String logLevel = "OFF"; //hard coded for now
    private final Properties.SelectionFunction selectionFunction = Properties.SelectionFunction.RANK_CROWD_DISTANCE_TOURNAMENT; //hard coded for now
    
    public EvoSuiteTestGenerator() {
    }

    public void configure(TestGenerationConfiguration config) {
        List<String> projectCPList = Arrays.asList(config.projectClassPath().split(":"))
        .stream()
        .map(path -> 
        (Paths.get(path).isAbsolute() && new File(path).exists()) ? path :
                    new File(getClass().getClassLoader().getResource(path).getPath()).getAbsolutePath()
        )
        .collect(Collectors.toList());
        this.projectCP = String.join(":", projectCPList);
        this.targetClass = config.targetClassCanonicalName();

        Properties.getInstance();
        Properties.CP = projectCP;
        Properties.TARGET_CLASS = targetClass;
        Properties.STOPPING_CONDITION = stoppingCondition;
        Properties.SEARCH_BUDGET = searchBudget;
        Properties.LOG_LEVEL = logLevel;
        Properties.SELECTION_FUNCTION = selectionFunction;
        Properties.TEST_SCAFFOLDING = false;
        Properties.STREAM_BACKED_RANDOMNESS = true;
        Properties.RANDOM_SEED = (long) 1;

        // Properties.STRATEGY = Properties.STRATEGY.SIMPLE_RANDOM; // for composing testchromosome
        Properties.STRATEGY = Properties.STRATEGY.RANDOM_FIXED; // for composing testchromosome
        Properties.NUM_RANDOM_TESTS = 20; // for composing testchromosome
        Properties.ALGORITHM = Properties.ALGORITHM.SIMPLE_RANDOM_SEARCH; // for search algorithm
        // Properties.POPULATION = Integer.MAX_VALUE;
        Properties.NO_RUNTIME_DEPENDENCY = true;
        Properties.TIMEOUT_BLOCKED = true;
    }
    
    public String generateTestSuite(SourceOfRandomness random) {
        Randomness.setSourceOfRandomness(random); // update randomness source in each generation
        try {
        TestSuiteGenerator generator = new TestSuiteGenerator();
        TestGenerationResult result = generator.generateTestSuite();
        TestSuiteChromosome suite = result.getTestSuiteChromosome();
        List<TestCase> testCases = suite.getTests();
        TestSuiteStringBuilder testSuiteStringBuilder = new TestSuiteStringBuilder();
        testSuiteStringBuilder.setTestCases(testCases);
        testSuiteStringBuilder.setNameGenerator(testCases, suite.getLastExecutionResults());
        List<ExecutionResult> executionResults = suite.getLastExecutionResults();
        
        Randomness.printInvocationCounterMap();
        StreamBackedRandom.printInvocationCounters();
        
        return testSuiteStringBuilder.formattedTest(testCases, targetClass, executionResults);
    } catch (Exception e) {
        e.printStackTrace();
        Randomness.printInvocationCounterMap();
        StreamBackedRandom.printInvocationCounters();
        System.exit(-1);
        return null;
    }
    }
}

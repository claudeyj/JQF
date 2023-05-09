package edu.berkeley.cs.jqf.examples.common.testgen;

import org.evosuite.EvoSuite;
import org.evosuite.Properties;
import org.evosuite.utils.Randomness;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EvoSuiteTest {
    private static final Logger logger = LoggerFactory.getLogger(EvoSuite.class);

    public static String[] buildArgs() {
        return new String[] {
            "-generateSuite",
            "-class",
            "org.example.TestGenerationTarget",
            "-Dsearch_budget=10",
            "-Dstopping_condition=MaxTime",
            "-projectCP",
            "/home/jun/research/test_gen/JQF/examples/src/main/resources/project/bin"
        };
    }
    public static void main(String[] args) {
        try {
            EvoSuite evosuite = new EvoSuite();
            String[] cmdArgs = buildArgs();
            evosuite.parseCommandLine(cmdArgs);
        } catch (Throwable t) {
            logger.error("Fatal crash on main EvoSuite process. Class "
                    + Properties.TARGET_CLASS + " using seed " + Randomness.getSeed()
                    + ". Configuration id : " + Properties.CONFIGURATION_ID, t);

            //print the stack trace to the console
            t.printStackTrace();
            //type of error that should not happen
            System.out.println("type of throwable: " + t.getClass().getCanonicalName());
            System.exit(-1);
        }

        /*
         * Some threads could still be running, so we need to kill the process explicitly
         */
        System.exit(0);
    }
}

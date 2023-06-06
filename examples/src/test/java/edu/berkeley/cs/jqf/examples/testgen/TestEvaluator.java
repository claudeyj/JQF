package edu.berkeley.cs.jqf.examples.testgen;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.tools.Diagnostic;
import javax.tools.DiagnosticCollector;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.SimpleJavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;
import javax.tools.JavaCompiler.CompilationTask;

import org.apache.commons.io.FileUtils;
import org.junit.internal.TextListener;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;

import edu.berkeley.cs.jqf.instrument.InstrumentingClassLoader;

public class TestEvaluator {
    private JavaCompiler compiler;
    private StandardJavaFileManager fileManager;

    private String projectCP;
    private String junitJarPath = JUnitCore.class.getProtectionDomain().getCodeSource().getLocation().getPath();
    private String mockitoJarPath = "/home/jun/fastd/.m2_mirror/repository/org/mockito/mockito-core/3.12.4/mockito-core-3.12.4.jar";
    private String evosuiteRuntimeJarPath = "/home/jun/fastd/.m2_mirror/repository/org/evosuite/evosuite-standalone-runtime/1.2.1-SNAPSHOT/evosuite-standalone-runtime-1.2.1-SNAPSHOT.jar";
    
    private String targetRootDir;
    private String debugRootDir;
    private static int counter = 0;
    private static boolean cleanFlag = false;
    
    public TestEvaluator(String projectCP, String targetRootDir, String debugRootDir) {
        compiler = ToolProvider.getSystemJavaCompiler();
        fileManager = compiler.getStandardFileManager(null, null, null);
        List<String> projectCPList = Arrays.asList(projectCP.split(":")).stream().map(path ->
        (Paths.get(path).isAbsolute() && new File(path).exists()) ? path :
                new File(getClass().getClassLoader().getResource(path).getPath()).getAbsolutePath()
        ).collect(Collectors.toList());   
        this.projectCP = String.join(":", projectCPList);
        this.targetRootDir = targetRootDir;
        this.debugRootDir = debugRootDir;
        if (!cleanFlag) {
            try {
                FileUtils.deleteDirectory(new File(targetRootDir));
                // FileUtils.deleteDirectory(new File(debugRootDir));
            } catch (IOException e) {
                e.printStackTrace();
            }
            cleanFlag = true;
        }
    }
    
    public boolean compile(String targetClassName, String testSuiteContent) {
        JavaFileObject javaFileObject = new JavaSourceFromString(targetClassName, testSuiteContent);
        Iterable<? extends JavaFileObject> compilationUnits = Arrays.asList(javaFileObject);
        String targetDir = new File(targetRootDir).getAbsolutePath();
        new File(targetDir).mkdirs();
        
        DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<JavaFileObject>();
        List<String> options = Arrays.asList("-classpath", ".:" + projectCP + ":" + junitJarPath + ":" + mockitoJarPath + ":" + evosuiteRuntimeJarPath, "-d", targetDir);
        CompilationTask task = compiler.getTask(null, fileManager, diagnostics, options, null, compilationUnits);
        
        if (task.call()) {
            System.out.println("Compilation succeeded");
            dumpTestSourceCode(testSuiteContent, targetClassName);
            dumpBytecode(targetClassName);
            return true;
        } else {
            System.out.println("Compilation failed");
            for (Diagnostic<? extends JavaFileObject> diagnostic : diagnostics.getDiagnostics()) {
                System.out.format("Error on line %d in %s%n",
                        diagnostic.getLineNumber(),
                        diagnostic.getSource().toUri(),
                        diagnostic.getMessage(null));
            }
            return false;
        }
    }

    public boolean execute(String targetClassName) {
        InstrumentingClassLoader testClassLoader = null;
        URL testClassURL;
        URLClassLoader moduleClassLoader = (URLClassLoader) this.getClass().getModule().getClassLoader();
        try {
            testClassLoader = new InstrumentingClassLoader(new URL[] {
                new File(targetRootDir).toURI().toURL()
            },
            moduleClassLoader);
            testClassURL = new File(targetRootDir).toURI().toURL();
            Class<?> testClass = testClassLoader.findClass(targetClassName);

            JUnitCore junit = new JUnitCore();
            junit.addListener(new TextListener(System.out));
            Result result = junit.run(testClass);
            if (result.wasSuccessful()) {
                System.out.println("Test passed");
            } else {
                System.out.println("Test failed");
                result.getFailures().forEach(failure -> System.out.println(failure.toString()));
            }
            return true;
        } catch (MalformedURLException | ClassNotFoundException | IllegalArgumentException | SecurityException e) {
            e.printStackTrace();
            return false;
        }
        finally {
            counter++;
            try {
                // hackRemoveURL(testClassURL, moduleClassLoader);
                testClassLoader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void dumpTestSourceCode(String testSuiteContent, String targetClassName) {
        try {
            String debugDir = new File(new File(debugRootDir, "src"), targetClassName + "_" + counter).getAbsolutePath();
            if (Files.exists(Paths.get(debugDir))) {
                FileUtils.deleteDirectory(new File(debugDir));
            }
            new File(debugDir).mkdirs();
            Files.write(Paths.get(debugDir, targetClassName + ".java"), testSuiteContent.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void dumpBytecode(String targetClassName) {
        try {
            String debugDir = new File(new File(debugRootDir, "bin"), targetClassName + "_" + counter).getAbsolutePath();
            if (Files.exists(Paths.get(debugDir))) {
                FileUtils.deleteDirectory(new File(debugDir));
            }
            new File(debugDir).mkdirs();
            // copy the class file to the debug directory
            FileUtils.copyDirectory(new File(this.targetRootDir), new File(debugDir));
        } catch (IOException | IllegalStateException e) {
            e.printStackTrace();
        }
    }

    private static class JavaSourceFromString extends SimpleJavaFileObject {
        final String code;

        JavaSourceFromString(String name, String code) {
            super(URI.create("string:///" + name.replace('.', '/') + Kind.SOURCE.extension), Kind.SOURCE);
            this.code = code;
        }

        @Override
        public CharSequence getCharContent(boolean ignoreEncodingErrors) {
            return code;
        }
    }

    public static String fileToContent(String path) {
        try {
            return new String(Files.readAllBytes(Paths.get(path)));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}

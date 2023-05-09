package edu.berkeley.cs.jqf.examples.common.testgen;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import org.apache.commons.lang3.JavaVersion;
import org.evosuite.PackageInfo;
import org.evosuite.Properties;
import org.evosuite.setup.InheritanceTree;
import org.evosuite.setup.InheritanceTreeGenerator;

import com.thoughtworks.xstream.XStream;

public class Debug {

    // final static String JAVA_SPECIFICATION_VERSION = System.getProperty("java.specification.version");
    // final static JavaVersion JAVA_SPECIFICATION_VERSION_AS_ENUM = JavaVersion.get(JAVA_SPECIFICATION_VERSION);
    // public static void main(String[] args) throws IOException {
    //     // Properties.getInstance().writeConfiguration("original_evosuite.properties");
    //     // TestSuiteGeneratorTest.config();
    //     // Properties.getInstance().writeConfiguration("updated_evosuite.properties");
    //     // XStream xstream = new XStream();
    //     // XStream.setupDefaultSecurity(xstream);
    //     // xstream.allowTypesByWildcard(new String[]{"org.evosuite.**", "org.jgrapht.**"});
    //     // // InheritanceTreeGenerator.readJDKData();
    //     // InputStream inheritance = InheritanceTreeGenerator.class.getResourceAsStream("/" + "JDK_inheritance.xml");
    //     // InheritanceTree inheritanceTree = (InheritanceTree) xstream.fromXML(inheritance);
    //     // System.out.println(inheritanceTree.getNumClasses());

        
    //     System.out.println(JAVA_SPECIFICATION_VERSION_AS_ENUM);
    // }
}

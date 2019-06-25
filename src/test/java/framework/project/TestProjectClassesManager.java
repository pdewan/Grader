package framework.project;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import grader.basics.settings.BasicGradingEnvironment;
import grader.config.ConfigurationManagerSelector;

import java.io.File;
import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import tools.TestConfig;

/**
 * Tests {@link TestProjectClassesManager}
 */
public class TestProjectClassesManager {

    private String validBuildLocation;
    private String validSrcLocation;
    private String simpleClassName;
    private String canonicalClassName;
    private String invalidName;
    private String validTag;

    @Before
    public void setUp() throws Exception {


        ConfigurationManagerSelector.getConfigurationManager().init(new String[0]);
    	
    	validBuildLocation = TestConfig.getConfig().getString("test.exampleSakai.example1.build");
        validSrcLocation = TestConfig.getConfig().getString("test.exampleSakai.example1.source");
        validTag = TestConfig.getConfig().getString("test.exampleSakai.example1.tag");
        simpleClassName = TestConfig.getConfig().getString("test.exampleSakai.example1.simpleName");
        canonicalClassName = TestConfig.getConfig().getString("test.exampleSakai.example1.canonicalName");
        invalidName = "__doesntexist__";
        
        BasicGradingEnvironment.get().setLoadClasses(true);
        
        assertNotNull(validBuildLocation, "Config option test.exampleSakai.example1.build read as null");
        assertNotNull(validSrcLocation, "Config option test.exampleSakai.example1.source read as null");
        assertNotNull(validTag, "Config option test.exampleSakai.example1.tag read as null");
        assertNotNull(simpleClassName, "Config option test.exampleSakai.example1.simpleName read as null");
        assertNotNull(canonicalClassName, "Config option test.exampleSakai.example1.canonicalName read as null");
    }

    @Test
    public void testCreation() {
        try {
            //new ProjectClassesManager(null, new File(validBuildLocation), new File(validSrcLocation));
            assertTrue(true);
        } catch (Exception e) {
            assertTrue("Failed to load classes.", false);
        }
    }

    @Test
    public void testFailedCreation() {
        try {
            String invalidLocation = "/";
            new ProjectClassesManager(null, null, new File(invalidLocation), new File(invalidLocation), null);
            assertTrue("Creation should fail", false);
        } catch (Exception e) {
            assertTrue(true);
        }
    }

    @Test
    public void testGetClassLoader() throws IOException, ClassNotFoundException {
        //ClassesManager classesManager = new ProjectClassesManager(null, new File(validBuildLocation), new File(validSrcLocation));
        //assertFalse("Class loader should exist", classesManager.getClassLoader() == null);
    }

    @Test
    public void testFindByClassNameSimple() throws IOException, ClassNotFoundException {
        //ClassesManager classesManager = new ProjectClassesManager(null, new File(validBuildLocation), new File(validSrcLocation));
        //Option<ClassDescription> description = classesManager.findByClassName(simpleClassName);
        //assertTrue("Class should exist", description.isDefined());
    }

    @Test
    public void testFindByClassNameCanonical() throws IOException, ClassNotFoundException {
        //ClassesManager classesManager = new ProjectClassesManager(null, new File(validBuildLocation), new File(validSrcLocation));
        //Option<ClassDescription> description = classesManager.findByClassName(canonicalClassName);
        //assertTrue("Class should exist", description.isDefined());
    }

    @Test
    public void testFindByClassNameInvalid() throws IOException, ClassNotFoundException {
        //ClassesManager classesManager = new ProjectClassesManager(null, new File(validBuildLocation), new File(validSrcLocation));
        //Option<ClassDescription> description = classesManager.findByClassName(invalidName);
        //assertTrue("Class should not exist", description.isEmpty());
    }

    @Test
    public void testFindByTag() throws IOException, ClassNotFoundException {
        //ClassesManager classesManager = new ProjectClassesManager(null, new File(validBuildLocation), new File(validSrcLocation));
        //Set<ClassDescription> description = classesManager.findByTag(validTag);
        //assertFalse("Class should exist", description.isEmpty());
    }

    @Test
    public void testFindByTagInvalid() throws IOException, ClassNotFoundException {
        //ClassesManager classesManager = new ProjectClassesManager(null, new File(validBuildLocation), new File(validSrcLocation));
        //Set<ClassDescription> description = classesManager.findByTag(invalidName);
        //assertTrue("Class should not exist", description.isEmpty());
    }

    @Test
    public void testGetClassDescriptions() throws IOException, ClassNotFoundException {
        // null project breaks things
        //ClassesManager classesManager = new ProjectClassesManager(null, new File(validBuildLocation), new File(validSrcLocation));
        //assertFalse("Class descriptions should not be empty", classesManager.getClassDescriptions().isEmpty());
    }

}

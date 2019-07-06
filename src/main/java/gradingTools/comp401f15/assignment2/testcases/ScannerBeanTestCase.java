package gradingTools.comp401f15.assignment2.testcases;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Constructor;
import java.util.Set;

import wrappers.framework.project.ProjectWrapper;
import framework.execution.ARunningProject;
import grader.basics.config.BasicProjectExecution;
import grader.basics.execution.ResultWithOutput;
import grader.basics.junit.NotAutomatableException;
import grader.basics.junit.TestCaseResult;
import grader.basics.project.BasicProjectIntrospection;
import grader.basics.project.NotGradableException;
import grader.basics.project.Project;
import grader.sakai.project.SakaiProject;
import gradingTools.sharedTestCase.checkstyle.OldOutputAndErrorCheckingTestCase;

public class ScannerBeanTestCase extends OldOutputAndErrorCheckingTestCase{

	public ScannerBeanTestCase() {
        super("Scanner Bean class test case");
    }
	
	static String[] emptyArgs = {};
	
	public Object createScannerBean (Class aClass) {
		Constructor aConstructor = null;
		try {
			aConstructor = aClass.getConstructor();
//			return aConstructor.newInstance();
			Object aResult = BasicProjectExecution.timedInvoke(aConstructor, emptyArgs, 300);
			return aResult;
		} catch (Exception e) {
			try {
				aConstructor = aClass.getConstructor(String.class);
				Object aResult = BasicProjectExecution.timedInvoke(aConstructor, new String[] {""}, 300);

				return aResult;
			} catch (Exception e1) {
				return null;
			}			
		}
		
		
		
	}

    @Override
    public TestCaseResult test(Project project, boolean autoGrade) throws NotAutomatableException, NotGradableException {
        if (project.getClassesManager().isEmpty())
            throw new NotGradableException();
     	SakaiProject aProject = ((ProjectWrapper) project).getProject();
        
//        Set<ClassDescription> aClasses = project.getClassesManager().get().findClassesAndInterfaces(null, "ScannerBean", ".*Bean.*", ".*Bean.*");
        Set<Class> aClasses = BasicProjectIntrospection.findClasses(project, null, "ScannerBean", ".*Bean.*", ".*Bean.*");

        if (aClasses.size() != 1) {
        	return  fail ("Cannot find unique scanner bean class");
        }
//        ClassDescription description = aClasses.iterator().next();
        Class description = aClasses.iterator().next();

//        
//        // Look in each class for something that satisfies the bean class requirements
//        for (ClassDescription description : project.getClassesManager().get().getClassDescriptions()) {

            // There should be a string property with a getter and setter
            try {
//                BeanInfo info = Introspector.getBeanInfo(description.getJavaClass());
                BeanInfo info = Introspector.getBeanInfo(description);

                for (PropertyDescriptor descriptor : info.getPropertyDescriptors()) {
                    if (descriptor.getName().equalsIgnoreCase("ScannedString") && descriptor.getPropertyType() == String.class && descriptor.getReadMethod() != null &&
                            descriptor.getWriteMethod() != null) {
                        Object anInstance = createScannerBean((description));
                        

                    	ResultWithOutput aResultWithOutput = BasicProjectExecution.timedInteractiveInvoke(anInstance, descriptor.getWriteMethod(), new String[]{"22 32 45 "}, 200);
                        String anOutput = aResultWithOutput.getOutput();
                        if (anOutput != null && !anOutput.isEmpty()) {
                        	ARunningProject.appendToTranscriptFile(aProject, getCheckable().getName(), anOutput);
                        }
                    	return pass(autoGrade);
                    }
                }
            } catch (IntrospectionException e) {
                // Do nothing if it fails
            } catch (Throwable e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        
        return fail("Couldn't find a class that satisfies the bean class requirements (string w/ a getter and setter).", autoGrade);
    }
}

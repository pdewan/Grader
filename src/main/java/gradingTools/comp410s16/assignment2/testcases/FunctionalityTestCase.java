package gradingTools.comp410s16.assignment2.testcases;

import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import framework.execution.NotRunnableException;
import framework.execution.RunningProject;
import framework.grading.testing.BasicTestCase;
import framework.grading.testing.NotAutomatableException;
import framework.grading.testing.NotGradableException;
import framework.grading.testing.TestCaseResult;
import framework.project.ClassDescription;
import framework.project.ClassesManager;
import framework.project.Project;
import grader.sakai.project.SakaiProject;
import gradingTools.utils.RunningProjectUtils;
import scala.Option;
import wrappers.framework.project.ProjectWrapper;

import java.lang.reflect.InvocationTargetException;

/**
 * Created by andrewwg94 on 2/11/16.
 */
public class FunctionalityTestCase extends BasicTestCase {
    public FunctionalityTestCase() {
        super("Functional Correctness Test Case");
    }

    @Override
    public TestCaseResult test(Project project, boolean autoGrade) throws NotAutomatableException, NotGradableException {
        try {
            SakaiProject aProject = ((ProjectWrapper) project).getProject();
            aProject.getClassesTextManager().getAllSourcesText();
            Option<ClassesManager> classesManager = project.getClassesManager();

            System.out.println("class descriptions: "+classesManager.get().getClassDescriptions());
            List<ClassDescription> cd = classesManager.get().findByClassName(classesManager.get().getClassDescriptions().toArray()[0].toString());
            Class<String> queue = (Class<String>) cd.get(0).getJavaClass();

            String[] input = new String[4];
            input[0] = "greek";
            input[1] = "alpha";
            input[2] = "beta";
            input[3] = "gamma";

            // Initialize score and feedback message
            String message = "";

            RunningProject runningProject = project.launch("", input,10);

            String output=runningProject.await().toLowerCase();
            System.out.println("output: "+output);
            boolean correct = output.contains("1\n" +
                    "1\n"+
                    "greek\n"+
                    "greek\n"+
                    "0\n"+
                    "0\n"+
                    "alpha\n"+
                    "alpha\n"+
                    "2\n"+
                    "2\n"+
                    "beta\n"+
                    "beta\n"+
                    "1\n"+
                    "1\n"+
                    "gamma\n"+
                    "gamma\n"+
                    "0\n"+
                    "0");
            System.out.println("correct: "+correct);

            if(correct){
                return pass();
            }
            else{
                return partialPass(.5, message);
            }

        } catch (NotRunnableException e) {
            throw new NotGradableException();
        }
    }
}

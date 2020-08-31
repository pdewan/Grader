package gradingTools.sharedTestCase;

import java.util.List;

import grader.basics.execution.NotRunnableException;
import grader.basics.junit.NotAutomatableException;
import grader.basics.junit.TestCaseResult;
import grader.basics.project.NotGradableException;
import grader.basics.project.Project;
import grader.sakai.project.SakaiProject;
import grader.util.GraderCommon;
import gradingTools.sharedTestCase.checkstyle.OldOutputAndErrorCheckingTestCase;
import wrappers.framework.project.ProjectWrapper;

public class DocumentEnclosedTestCase extends OldOutputAndErrorCheckingTestCase {

    public DocumentEnclosedTestCase() {
        super("Document enclosed test case");
    }

   
    
	 public static boolean hasDocument (List<String> aDocuments) {
			return aDocuments.size() > 0;
		};

    @Override
    public TestCaseResult test(Project project, boolean autoGrade) throws NotAutomatableException, NotGradableException {
        try {
        	SakaiProject aProject = ((ProjectWrapper) project).getProject();
        	List<String> aDocuments = aProject.getStudentAssignment().getDocuments();
        	if (hasDocument(aDocuments)) {
        		return pass();
        	} else {
        		return fail("Missing document");
        	}
        	
        	
        } catch (NotRunnableException e) {
            throw new NotGradableException();
        }
    }
}

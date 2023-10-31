package grader.sakai;

import grader.project.flexible.FlexibleProject;

import java.util.HashMap;
import java.util.Map;

public class ASakaiStudentCodingAssignmentsDatabase extends AnAbstractSakaiStudentAssignmentsDatabase<StudentCodingAssignment> {
	public static final String  DEFAULT_DATA_FOLDER_NAME = "C:/Users/dewan/Downloads/GraderData/Assignment11";

	Map<String,FlexibleProject> onyenToProject = new HashMap();

	public ASakaiStudentCodingAssignmentsDatabase(BulkAssignmentFolder aBulkAssignmentFolder) {
		super(aBulkAssignmentFolder);
	}

    public void makeProjects(){
	}

	@Override
	protected StudentCodingAssignment createAssignment(String aStudentDescription, String aFolderName) {
		return new ASakaiStudentCodingAssignment(aStudentDescription, bulkAssignmentFolder.getStudentFolder(aFolderName));
	}
	@Override
    public void cleanAllFeedbackAndSubmissionFolders() {
    	for (StudentAssignment studentAssignment:getStudentAssignments()) {
    		studentAssignment.cleanFeedbackFolder();
//    		studentAssignment.cleanSubmissionFolder();
    	}
    	
    }
	@Override
	public void cleanFeedbackAndSubmissionFolder(String anOnyen) {
		StudentAssignment aStudentAssignment = nameToStudentAssignment.get(anOnyen);
		if (aStudentAssignment != null) {
			aStudentAssignment.cleanFeedbackFolder();
			aStudentAssignment.cleanSubmissionFolder();
		}
	}
}

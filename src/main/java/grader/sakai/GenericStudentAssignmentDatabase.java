package grader.sakai;

import java.beans.PropertyChangeListener;
import java.util.Collection;
import java.util.Set;

public interface GenericStudentAssignmentDatabase<GenericAssignment> {
    public Set<String> getStudentIds();

    public BulkAssignmentFolder getBulkAssignmentFolder();

    public Collection<GenericAssignment> getStudentAssignments();

    public GenericAssignment getStudentAssignmentFromName(String aStudentId);

	void cleanAllFeedbackAndSubmissionFolders();

	void cleanFeedbackAndSubmissionFolder(String anOnyen);

	void createStudentAssignments();

	GenericAssignment getStudentAssignmentFromOnyen(String anOnyen);
	void removeStudentAssignment(String anOnyen);

}

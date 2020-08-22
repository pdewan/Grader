package grader.assignment.timestamp;

import org.joda.time.DateTime;

import framework.navigation.SakaiStudentFolder;

public interface TimestampComputer {
	DateTime computeStudentFolderTimeStamp (SakaiStudentFolder aStudentFolder);

}

package grader.sakai;

import java.util.Date;
import java.util.List;

import grader.basics.file.FileProxy;

public interface StudentAssignment {

    public FileProxy getSubmissionFolder();

    public FileProxy getFeedbackFolder();

    public FileProxy getCommentsFile();

    public FileProxy getTimeStampFile();

    public FileProxy getStudentFolder();

    public Date getDate();

    public String getTimeStamp();

    public String getStudentName();

    public String getStudentDescription();

    public String getOnyen();

    public boolean isSubmitted();

    public List<String> getDocuments();

    String getCommentsFileName();

	void cleanFeedbackFolder();

	void cleanSubmissionFolder();

}

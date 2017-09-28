package grader.sakai;

import grader.file.FileProxy;
import grader.file.RootFolderProxy;

import java.util.Set;

public interface BulkAssignmentFolder {
    public RootFolderProxy getAssignmentFolder();

    Set<String> getStudentFolderNames();

    FileProxy getStudentFolder(String aName);

    FileProxy getSpreadsheet();

    public String getAssignmentName();

    public String getMixedCaseAssignmentName();
    public void clear();

}

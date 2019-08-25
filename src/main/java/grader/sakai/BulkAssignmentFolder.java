package grader.sakai;

import java.util.Set;

import grader.basics.file.FileProxy;
import grader.basics.file.RootFolderProxy;

public interface BulkAssignmentFolder {
    public RootFolderProxy getAssignmentFolder();

    Set<String> getStudentFolderNames();

    FileProxy getStudentFolder(String aName);

    FileProxy getSpreadsheet();

    public String getAssignmentName();

    public String getMixedCaseAssignmentName();
    public void clear();

}

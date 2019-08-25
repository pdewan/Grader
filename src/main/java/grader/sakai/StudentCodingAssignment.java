package grader.sakai;

import grader.basics.file.FileProxy;
import grader.basics.file.RootFolderProxy;
import grader.project.flexible.FlexibleProject;

public interface StudentCodingAssignment extends StudentAssignment {

    public FileProxy getRubrick();

    public RootFolderProxy getProjectFolder();

    public FlexibleProject getProject();

    public void setProject(FlexibleProject newVal);

	FileProxy getZipFile();

	void setZipFile(FileProxy zipFile);


}

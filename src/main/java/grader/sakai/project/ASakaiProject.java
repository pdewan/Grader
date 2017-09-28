package grader.sakai.project;

import grader.file.FileProxy;
import grader.project.flexible.AFlexibleProject;
import grader.sakai.StudentAssignment;
import grader.sakai.StudentCodingAssignment;

import java.util.List;

import javax.swing.Icon;

public class ASakaiProject extends AFlexibleProject implements SakaiProject {
    StudentCodingAssignment studentAssignment;
    Icon studentPhoto;
//    ProjectWrapper wrapper;
    

	

	public ASakaiProject(StudentCodingAssignment aStudentCodingAssignment, String aSourceSuffix, String anOutputSuffix) {
        super(aStudentCodingAssignment, aSourceSuffix, anOutputSuffix);
        studentAssignment = aStudentCodingAssignment;
        List<String> documents = studentAssignment.getDocuments();
        documents.remove(getOutputFileName());
        documents.remove(getSourceFileName());
    }
	@Override
	public void clear() {
		super.clear();
		
	}
    public StudentAssignment getStudentAssignment() {
        return studentAssignment;
    }

    public void displaySource(SakaiProjectDatabase aSakaiProjectDatabase) {
        maybeMakeClassDescriptions();
        aSakaiProjectDatabase.getSourceDisplayer().displaySource(this);
    }
    @Override
    public Icon getStudentPhoto() {
		return studentPhoto;
	}
    @Override
	public void setStudentPhoto(Icon studentPhoto) {
		this.studentPhoto = studentPhoto;
	}
     @Override
    public String getProjectZipFileOrFolderMixedCaseAbsoluteName() {
    	FileProxy zipFile = studentAssignment.getZipFile();
    	if (zipFile != null) {
    		return zipFile.getMixedCaseAbsoluteName();
    	} else {
    		return getRootCodeFolder().getMixedCaseAbsoluteName();
    	}
    }
//     @Override
//     public ProjectWrapper getWrapper() {
// 		return wrapper;
// 	}
//     @Override
//     public void setWrapper(ProjectWrapper newValue) {
// 		this.wrapper = newValue;
// 	}

	
}

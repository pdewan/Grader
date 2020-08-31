package grader.sakai.project;

import grader.basics.file.FileProxy;
import grader.basics.project.BasicProject;
import grader.project.flexible.AFlexibleProject;
import grader.project.folder.RootCodeFolder;
import grader.sakai.StudentAssignment;
import grader.sakai.StudentCodingAssignment;
import util.misc.Common;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.swing.Icon;

import framework.project.StandardProject;

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
    		RootCodeFolder aFolder = getRootCodeFolder();
    		if (aFolder == null) {
    			return null;
    		}
    		return aFolder.getMixedCaseAbsoluteName();
    	}
    }
     @Override
     public String getStoredCheckstyleText() {
    	 if (checkStyleText == null || checkStyleText.isEmpty()) {
    		 File aFolder = StandardProject.getCheckstyleOutFolder(this);
    		 String aFileName = StandardProject.defaultCheckstyleOutputfileName();
    		 String anOutFileName = aFolder.getAbsolutePath() + "/" + aFileName;
    		 try {
    			 checkStyleText = Common.readFile(new File(anOutFileName)).toString();
    		 } catch (IOException e) {
    			 e.printStackTrace();
    		 }
    	 }
    	 return checkStyleText;
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

	
//}

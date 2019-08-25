package grader.spreadsheet.csv;

import grader.assignment.AnAssignmenDataFolder;
import grader.assignment.AssignmentDataFolder;
import grader.basics.file.FileProxy;
import grader.basics.file.filesystem.AFileSystemFileProxy;
import grader.spreadsheet.FeatureGradeRecorder;
import grader.spreadsheet.FinalGradeRecorder;

import java.io.File;
import java.util.List;

public class ASakaiFeatureGradeSheetMerger implements SakaiFeatureGradeSheetMerger{
	AssignmentDataFolder[] sourceAssignmentDataFolders;
	FileProxy finalSpreadsheet;
	FeatureGradeRecorder[] featureGradeRecorders;
	FinalGradeRecorder finalGradeRecorder;
	List<String>[] onyensLists;
	
	public ASakaiFeatureGradeSheetMerger(String[] sourceDataFolders, String  destinationSpreadsheet) {
		sourceAssignmentDataFolders = new AssignmentDataFolder[sourceDataFolders.length];
		featureGradeRecorders = new FeatureGradeRecorder[sourceDataFolders.length];
		onyensLists = new List[sourceDataFolders.length];
		for (int i = 0; i < sourceDataFolders.length; i++ ) {
			sourceAssignmentDataFolders[i] = new AnAssignmenDataFolder(sourceDataFolders[i], null);
			featureGradeRecorders[i] = new ASakaiCSVFeatureGradeManager(sourceAssignmentDataFolders[i].getFeatureGradeFile(), null);
			onyensLists[i] = sourceAssignmentDataFolders[i].getStudentIDs();
		}
		finalSpreadsheet = new AFileSystemFileProxy(new File(destinationSpreadsheet));
		finalGradeRecorder = new ASakaiCSVFinalGradeManager(finalSpreadsheet);		
	}
	
	public void merge() {
		for (int i = 0; i < sourceAssignmentDataFolders.length; i++) {
			for (String onyen:onyensLists[i]) {
				finalGradeRecorder.setGrade(null, onyen, featureGradeRecorders[i].getGrade(null, onyen));
			}
		}
	}

}

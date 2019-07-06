package grader.spreadsheet.display;

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.List;

import grader.settings.GraderSettingsManagerSelector;
import grader.settings.GraderSettingsModelSelector;
import grader.settings.folders.OnyenRangeModel;
import grader.spreadsheet.FinalGradeRecorder;
import grader.spreadsheet.csv.SakaiCSVFinalGradeRecorder;
import gradingTools.assignment9.testCases.ListenerAndPainterTagTestCase;
import util.models.AListenableVector;
import wrappers.grader.sakai.project.ProjectDatabaseFactory;
import wrappers.grader.sakai.project.ProjectDatabaseWrapper;

public class ASpreadsheetDisplay extends AListenableVector implements SpreadsheetDisplay{
	
	FinalGradeRecorder finalGradeRecorder;
	ProjectDatabaseWrapper projectDatabase;
	OnyenRangeModel onyenRangeModel;
//	List<SpreadsheetItemDisplay> contents;
	
	public ASpreadsheetDisplay() {
		projectDatabase = ProjectDatabaseFactory.getOrCreateProjectDatabase();
		refresh();
		ProjectDatabaseFactory.addPropertyChangeListener(this);
		onyenRangeModel = GraderSettingsModelSelector.getGraderSettingsModel().getOnyens();
		onyenRangeModel.addPropertyChangeListener(this); // added after project database
		
	}
	
	void refresh() {
//		projectDatabase = ProjectDatabaseFactory.getOrCreateProjectDatabase();
		finalGradeRecorder = projectDatabase.getGradeRecorder();
		List<String> anOnyenList = projectDatabase.getOnyenNavigationList();
//		if (contents == null) {
//			contents = new ArrayList(anOnyenList.size());
//		}
//		contents.clear();
		this.clear();
		for (String anOnyen: anOnyenList) {
			String aName = finalGradeRecorder.getFullName(anOnyen);
			SpreadsheetItemDisplay anItem = new ASpreadsheetItemDisplay(anOnyen, aName);
			double aScore = finalGradeRecorder.getGrade(aName, anOnyen);
			String aScoreString = "" + 
					(aScore == finalGradeRecorder.DEFAULT_VALUE?finalGradeRecorder.DEFAULT_CHAR:aScore);
				
			
			anItem.setGrade(aScoreString);
			addElement(anItem);			
		}
		
		
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if (evt.getPropertyName() == ProjectDatabaseFactory.PROPERTY_NAME ) {
			projectDatabase = ProjectDatabaseFactory.getOrCreateProjectDatabase();;
		} else if (evt.getSource() == onyenRangeModel) {
//			ProjectDatabaseFactory.createProjectDatabase(); 
			refresh();
		}
	}
	

}

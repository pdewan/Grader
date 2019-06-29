package grader.steppers;

import framework.logging.recorder.ConglomerateRecorder;
import grader.assignment.GradingFeature;
import grader.assignment.GradingFeatureList;
import grader.auto_notes.NotesGenerator;
import grader.sakai.project.SakaiProject;
import grader.sakai.project.SakaiProjectDatabase;
import grader.settings.GraderSettingsModel;
import grader.settings.GraderSettingsModelSelector;
import grader.settings.folders.AnOnyenRangeModel;
import grader.spreadsheet.FeatureGradeRecorder;
import grader.spreadsheet.FinalGradeRecorder;
import grader.trace.multiplier.MultiplierColored;
import grader.trace.multiplier.MultiplierSaved;
import grader.trace.multiplier.MultiplierUserChange;
import grader.trace.overall_notes.MultiplierOverrideNotes;
import grader.trace.overall_notes.OverallScoreOverrideNotes;
import grader.trace.overall_notes.SourcePointOverrideNotes;
import grader.trace.overall_score.OverallScoreColored;
import grader.trace.overall_score.OverallScoreManualChange;
import grader.trace.overall_score.OverallScoreSaved;
import grader.trace.settings.MissingOnyenException;
import grader.trace.source_points.SourcePointsSaved;
import grader.trace.source_points.SourcePointsUserChange;
import grader.trace.steppers.UserOnyenSet;

import java.awt.Color;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import javax.swing.Icon;

import util.annotations.ComponentWidth;
import util.annotations.Explanation;
import util.annotations.Label;
import util.annotations.Row;
import util.annotations.StructurePattern;
import util.annotations.StructurePatternNames;
import util.annotations.Visible;
import util.models.ALabelBeanModel;
import util.models.LabelBeanModel;
import bus.uigen.ObjectEditor;
import bus.uigen.attributes.AttributeNames;
import bus.uigen.introspect.Attribute;

@StructurePattern(StructurePatternNames.BEAN_PATTERN)
public class AGradedProjectTextOverview  implements
		GradedProjectTextOverview {
	PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(
			this);

	String name = "";
	SakaiProjectDatabase projectDatabase;
	double score;

//	// ideally this stuff should really be done through property change as
//	// Josh's wrapper does
	FeatureGradeRecorder featureGradeRecorder;
	double multiplier = 1;
	String onyen = "";
//	String commentsFileName = "";

	SakaiProject project;
//	framework.project.Project wrappedProject;
//	// FinalGradeRecorder gradeRecorder;
//	boolean hasMoreSteps = true;
	FinalGradeRecorder totalScoreRecorder;
	boolean manualOnyen;

	Color currentScoreColor, currentMultiplierColor;
//	currentOverallNotesColor;
	Color nextScoreColor, nextMultiplierColor;

	Icon studentPhoto;
	LabelBeanModel photoLabelBeanModel;
	OverviewProjectStepper projectStepper;
	double sourcePoints;
	GraderSettingsModel graderSettings;

	public AGradedProjectTextOverview() {
		photoLabelBeanModel = new ALabelBeanModel("");
//		addPropertyChangeListener(this); // listen to yourself to see if you have changed
	}
	protected int maxFeatureScore;
	protected void setMaxFeatureScore(SakaiProjectDatabase aProjectDatabase) {
		GradingFeatureList gradingFeatures = projectDatabase
				.getGradingFeatures();
		for (GradingFeature aGradingFeature : gradingFeatures) {
			maxFeatureScore += aGradingFeature.getMax();
		}
	}
	public void setProjectDatabase(SakaiProjectDatabase aProjectDatabase) {
		graderSettings = GraderSettingsModelSelector.getGraderSettingsModel();
		projectDatabase = aProjectDatabase;
		// gradeRecorder = aProjectDatabase.getGradeRecorder();
		featureGradeRecorder = aProjectDatabase.getFeatureGradeRecorder();
		totalScoreRecorder = aProjectDatabase.getTotalScoreRecorder();
		projectStepper = (OverviewProjectStepper) projectDatabase.getProjectStepper();
//		registerWithGradingFeatures();
//		logFile = aProjectDatabase.getAssigmentDataFolder().getLogFileName();
//		gradedFile = aProjectDatabase.getAssigmentDataFolder()
//				.getGradedIdFileName();
//		skippedFile = aProjectDatabase.getAssigmentDataFolder()
//				.getSkippedIdFileName();
//		GraderSettingsModel graderSettings = aProjectDatabase.getGraderSettings();
////		graderSettings.getNavigationSetter().getNavigationFilterSetter().addPropertyChangeListener(this);
//		getNavigationSetter().getNavigationFilterSetter().addPropertyChangeListener(this);
		
		// recordWindows(); // the first project does not wait so we need to
		// record here

	}
	@Override
	public void setFrame(Object aFrame) {
		if (projectStepper.getProject() != null) {
			currentMultiplierColor = null;
			currentScoreColor = null;
			setMultiplierColor();
			setScoreColor();
		}
	}

//	boolean runExecuted;
//
//	boolean runAttempted() {
//		return runExecuted || isAutoRun() || isAutoAutoGrade();
//	}

	@ComponentWidth(150)
	@Row(0)
	@Override
	@Explanation("Editing onyen will navigate to corresponding project")
	@Label("Onyen")
	public String getDisplayedOnyen() {
		if (graderSettings.isPrivacyMode())
			return AnOnyenRangeModel.ANONYMOUS;
			else return getOnyen();
//		return onyen;
	}
	@Visible(false)
	@ComponentWidth(150)
	@Row(0)
	public String getOnyen() {
		
		return onyen;
	}

	@Override
	public void setOnyen(String anOnyen) throws MissingOnyenException {
		internalSetOnyen(anOnyen);
		manualOnyen = true;
		UserOnyenSet.newCase(projectDatabase, projectStepper, anOnyen, this);
		projectStepper.setProject(anOnyen);
	}
	@Override
	 public void internalSetOnyen(String anOnyen) throws MissingOnyenException {
		String oldOnyen = onyen;
		
		onyen = anOnyen;
		// again this will void a getter call when properties are redisplayed
		propertyChangeSupport.firePropertyChange("onyen", oldOnyen, onyen);



	}


	@Visible(false)
	@Row(1)
	@ComponentWidth(150)
	@Override
	public String getName() {
		return name;
	}
	@Row(1)
	@ComponentWidth(150)
	@Override
	@Label("Name")
	public String getDisplayedName() {
		if (graderSettings.isPrivacyMode())
		return AnOnyenRangeModel.ANONYMOUS;
		else return getName();
	}
	
	@Override
	@Visible(false)
	public void setName(String newVal) {
		name = newVal;
		// System.out.println("name changed to" + newVal);
		// as we are postponing notfications, sending the name change is useful
		 propertyChangeSupport.firePropertyChange("Name", null, newVal);
//		notifyPreconditionChanged();
		// System.out.println("precondition notified");
	}

//	void notifyPreconditionChanged() {
//		propertyChangeSupport.firePropertyChange("this", null, this);
//
//	}

	@Row(2)
	@ComponentWidth(150)
	@Label("Feature Total:")
	public double getScore() {
		return score;
	}



	@Visible(false)
	public SakaiProject getProject() {
		return project;
	}



	// Josh: We want to know when a project is set, so I'm adding the project
	// property change event here.
	@Visible(false)
	@Override
	public boolean setProject(SakaiProject newVal) {

		project = newVal;

//		
//		studentPhoto = projectDatabase.getStudentPhoto(onyen, project);
//
//
//		if (studentPhoto != null){
//			photoLabelBeanModel.set("", studentPhoto);
//		} else {
//			photoLabelBeanModel.set(APhotoReader.NO_PHOTO_TITLE, studentPhoto);
//		}

		setColors();


		return true;
	}

	void setColor(String aPath, Color aColor) {
		propertyChangeSupport.firePropertyChange(aPath, null, 
				new Attribute(AttributeNames.CONTAINER_BACKGROUND, aColor));
	}

	@Override
	public void setMultiplierColor() {
		if (projectStepper.isSettingUpProject()) return;
		nextMultiplierColor = projectDatabase.getMultiplierColorer().color(multiplier);
		if (currentMultiplierColor == nextMultiplierColor ) return;
		setColor("Multiplier",  nextMultiplierColor);
		currentMultiplierColor = nextMultiplierColor;
		MultiplierColored.newCase(projectDatabase, projectStepper, project, nextMultiplierColor, multiplier, this);

	}
	@Override
	public void setScoreColor() {
		if (projectStepper.isSettingUpProject()) return;
		double aPercentage = maxFeatureScore == 0?1:score/maxFeatureScore;
		nextScoreColor = projectDatabase.getScoreColorer().color(aPercentage);

//		nextScoreColor = projectDatabase.getScoreColorer().color(score/maxFeatureScore);
		if (currentScoreColor == nextScoreColor ) return;
		setColor("Score",  nextScoreColor);
		currentScoreColor = nextScoreColor;
		OverallScoreColored.newCase(projectDatabase, projectStepper, project, nextScoreColor, score, this);
	}
	


	
	void setColors() {
		// no incremental updates as score and other properties change during auto grade
		if (projectStepper.isSettingUpProject()) 
		    return;
//		computeNextColors();
//		setGradingFeatureColors();
		setMultiplierColor();
//		setOverallNotesColor();
		setScoreColor();
		
	}
	


@Override
	public void internalSetScore(double newVal) {
//		if (projectStepper.isChanged()) return;
		if (score == newVal) return;
		score = newVal;
		if (!projectStepper.isSettingUpProject()) {
			setScoreColor();
			propertyChangeSupport.firePropertyChange("Score", null, newVal);
//			featureGradeRecorder.setGrade(name, getOnyen(), newVal);

		}
		// do not save it in Josh's spreadsheet
//		featureGradeRecorder.setGrade(name, getOnyen(), newVal);


		
	}

	void setGrade(double newVal) {
		// This will be a spurious message to conglomerate as t serves as total
		// and feature recorder
		if (! (totalScoreRecorder instanceof ConglomerateRecorder))
		totalScoreRecorder.setGrade(project.getStudentAssignment()
				.getStudentName(), project.getStudentAssignment().getOnyen(),
				newVal);

	}

	double getGrade() {
		return totalScoreRecorder.getGrade(project.getStudentAssignment()
				.getStudentName(), project.getStudentAssignment().getOnyen());

	}



	@Override
	public void setScore(double newVal) {
		if (score == newVal) return;
		double oldVal = score;
		internalSetScore(newVal);
		OverallScoreManualChange.newCase (projectDatabase, projectStepper, project, score, this);
		if (totalScoreRecorder != null)

			// if (gradeRecorder != null)
			setGrade(newVal);
		featureGradeRecorder.setGrade(getName(), getOnyen(), newVal);
		OverallScoreSaved.newCase(projectDatabase, projectStepper, project, featureGradeRecorder.getFileName(), score, this);

		
//		featureGradeRecorder.setGrade(name, getOnyen(), newVal);
		NotesGenerator notesGenerator = projectDatabase.getNotesGenerator();
		String newNotes = notesGenerator.totalScoreOverrideNotes(projectStepper, oldVal, newVal);
		projectStepper.setOverallNotes(notesGenerator.appendNotes(
				projectStepper.getOverallNotes(), 
				newNotes));
//				notesGenerator.totalScoreOverrideNotes(projectStepper, oldVal, newVal)));
		OverallScoreOverrideNotes.newCase(projectDatabase, projectStepper, project, newNotes, this);
		projectStepper.setChanged(true);
		projectStepper.setOverallNotesColor();
		

	}

	
	@Row(4)
	@Visible(true)
	@Explanation("Weight based on early or late submission")
	public double getMultiplier() {
		return multiplier;
	}
	
	@Override
	@Row(3)
	@Visible(true)
	@Explanation("Points embedded in source code instructor critique")
	public double getSourcePoints() {
		return sourcePoints;
	}
	@Override
	public void internalSetSourcePoints(double newValue) {
		if (newValue == sourcePoints) return;
		double oldValue = sourcePoints;
		sourcePoints = newValue;
		featureGradeRecorder.setSourcePoints(getName(), getOnyen(),
				sourcePoints);
		SourcePointsSaved.newCase(projectDatabase, projectStepper, project, featureGradeRecorder.getFileName(), sourcePoints, this);
//		setMultiplierColor();
	
		propertyChangeSupport.firePropertyChange("sourcePoints", oldValue, newValue);

	}
	
	
	public void internalSetMultiplier(double newValue) {
//		if (newValue == multiplier) return;
		double oldValue = multiplier;
		multiplier = newValue;
		// save this value even if the newValue is == oldValue since the old value may be from previous project
		// need to pass the value from one featur erecorder to another so this extra work may be needed
		featureGradeRecorder.setEarlyLatePoints(getName(), getOnyen(),
				multiplier);
		MultiplierSaved.newCase(projectDatabase, projectStepper, project, featureGradeRecorder.getFileName(), multiplier, this);
		if (oldValue != newValue)
		setMultiplierColor();
		propertyChangeSupport.firePropertyChange("multiplier", oldValue, newValue);
	}
	public void setSourcePoints(double newValue) {
		double oldVal = sourcePoints;
		if (oldVal == newValue) return;
		internalSetSourcePoints(newValue);
		NotesGenerator notesGenerator = projectDatabase.getNotesGenerator();
		String newNotes = notesGenerator.sourcePointsOverrideNotes(projectStepper, oldVal, newValue);
		projectStepper.setOverallNotes(notesGenerator.appendNotes(
				projectStepper.getOverallNotes(),
				newNotes));
		projectStepper.setChanged(true);
		SourcePointsUserChange.newCase(projectDatabase, projectStepper, project, newValue, this);
		SourcePointOverrideNotes.newCase(projectDatabase, projectStepper, project, newNotes, this);

	}
	
	public void setMultiplier(double newValue) {
		double oldVal = multiplier;
		if (oldVal == newValue) return;
		internalSetMultiplier(newValue);
		NotesGenerator notesGenerator = projectDatabase.getNotesGenerator();
		String newNotes = notesGenerator.multiplierOverrideNotes(projectStepper, oldVal, newValue);
		projectStepper.setOverallNotes(notesGenerator.appendNotes(
				projectStepper.getOverallNotes(),
				newNotes));
		featureGradeRecorder.saveMultiplier(newValue);
		projectStepper.setChanged(true);
		MultiplierUserChange.newCase(projectDatabase, projectStepper, project, newValue, this);
		MultiplierOverrideNotes.newCase(projectDatabase, projectStepper, project, newNotes, this);

	}
	
	



	@Override
	public void addPropertyChangeListener(PropertyChangeListener aListener) {
		propertyChangeSupport.addPropertyChangeListener(aListener);
	}

	@Override
	@Visible(false)
	public SakaiProjectDatabase getProjectDatabase() {
		// TODO Auto-generated method stub
		return projectDatabase;
	}


	
	@Override
	public void computeNextColors() {
//		List<Color> colors = new ArrayList();
//		int i = 0;
//		for (GradingFeature aGradingFeature:projectDatabase.getGradingFeatures()) {
//			nextColors.set(i, projectDatabase.getGradingFeatureColorer().color(aGradingFeature) );
//			i++;
//		}
		nextMultiplierColor = projectDatabase.getMultiplierColorer().color(multiplier);
		nextScoreColor = projectDatabase.getScoreColorer().color(score);
//		nextOverallNotesColor = projectDatabase.getOverallNotesColorer().color(overallNotes);
		
	}

	public static void main(String[] args) {
		ObjectEditor.edit(new AGradedProjectTextOverview());
	}
	

}

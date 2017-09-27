package grader.assignment;

import framework.grading.testing.Checkable;
import grader.auto_notes.NotesGenerator;
import grader.basics.project.CurrentProjectHolder;
import grader.checkers.CheckResult;
import grader.checkers.FeatureChecker;
import grader.file.FileProxy;
import grader.sakai.project.SakaiProject;
import grader.sakai.project.SakaiProjectDatabase;
import grader.steppers.OverviewProjectStepper;
import grader.trace.feature.FeatureSelected;
import grader.trace.feature.FeatureUserGraded;
import grader.trace.feature.FeatureValidated;
import grader.trace.feature.auto_notes.FeatureAutoNotesChanged;
import grader.trace.feature.auto_notes.FeatureAutoNotesLoaded;
import grader.trace.feature.auto_notes.FeatureAutoNotesSaved;
import grader.trace.feature.auto_result_format.FeatureAutoResultFormatChanged;
import grader.trace.feature.auto_result_format.FeatureAutoResultFormatLoaded;
import grader.trace.feature.manual_notes.FeatureManualNotesLoaded;
import grader.trace.feature.manual_notes.FeatureManualNotesSaved;
import grader.trace.feature.score.FeatureScoreUserChange;

import java.awt.Color;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

import util.annotations.ComponentWidth;
import util.annotations.Label;
import util.annotations.Position;
import util.annotations.StructurePattern;
import util.annotations.StructurePatternNames;
import util.annotations.Visible;
import util.trace.Tracer;

// a feature to be implemented by a student
@util.annotations.Explanation("grading feature tooltip")
@StructurePattern(StructurePatternNames.BEAN_PATTERN)
public class AGradingFeature implements GradingFeature {
	public static final String FEEDBACK_FILE_PREFIX = "Score-";
	public static final String RESULTS_FILE_PREFIX = "Results-";
	public static final String MANUAL_NOTES_FILE_PREFIX = "ManualNotes-";
	public static final String AUTO_NOTES_FILE_PREFIX = "AutoNotes-";


	public static final String FEEDBACK_FILE_SUFFIX = ".txt";
	Color color;
	boolean scoreSetManually;

	String featureName = "Some feature";
	// String comment = " ";
	double maxScore;
	double score;
	boolean graded;
	boolean autoGradable = true;
	boolean extraCredit;
	FileProxy feedbackFolder;
	String feedbackFolderName;
	FeatureChecker featureChecker;
	String feedbackFileName, resultsFileName, manualNotesFileName, autoNotesFileName;
	SakaiProject project;
	SakaiProjectDatabase projectDatabase;
	String[] outputFiles;
	String[] inputFiles;
	GradingFeature linkedFeature;
	boolean cannotAutoGrade;
	String manualNotes = "";
	String autoNotes = "";
	String resultFormat = "";
	boolean isRestriction;
	String output = "";
	Checkable feature;

	PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(
			this);

	public AGradingFeature(String aFeature, double aMaxScore) {
		featureName = aFeature;

		maxScore = aMaxScore;
		processMaxScore();
	}

	public AGradingFeature(String aFeature, double aMaxScore,
			FeatureChecker aFeatureChecker) {
//		String[] aFeatureParts= aFeature.split(":");
//		featureName = aFeatureParts[0];
		featureName = aFeature;
		maxScore = aMaxScore;
		processMaxScore();
		// score = aMaxScore;
		featureChecker = aFeatureChecker;
		featureChecker.init(this);
	}

	void processMaxScore() {
		if (maxScore > 0) {
//			score = maxScore;
			score = 0;

		} else {
			score = maxScore;
			isRestriction = true;
		}
	}

	@Visible(false)
	@Override
	public boolean isRestriction() {
		return isRestriction;
	}

	@Visible(false)
	public GradingFeature getLinkedFeature() {
		return linkedFeature;
	}

	public void setLinkedFeature(GradingFeature aGradingFeature) {
		linkedFeature = aGradingFeature;
	}

	public AGradingFeature(String aFeature, double aMaxScore,
			boolean anExtraCredit) {
		this(aFeature, aMaxScore);
		extraCredit = anExtraCredit;
	}

	public String toString() {
		return "Feature:" + featureName + "\tMax:" + maxScore + "\tScore: "
				+ score + (extraCredit ? "\tExtra" : "");
	}

	public AGradingFeature(String aFeature, double aMaxScore,
			FeatureChecker aFeatureChecker, boolean anExtraCredit) {
		this(aFeature, aMaxScore, aFeatureChecker);
		extraCredit = anExtraCredit;
	}
	public AGradingFeature(boolean anIsManual, String aFeature, double aMaxScore,
			FeatureChecker aFeatureChecker, boolean anExtraCredit) {
		this(aFeature, aMaxScore, aFeatureChecker, anExtraCredit);
		autoGradable = !anIsManual;
	}

	@Visible(false)
	public FeatureChecker getFeatureChecker() {
		return featureChecker;
	}

	@Visible(false)
	public String[] getOutputFiles() {
		return outputFiles;
	}

	public void setOutputFiles(String[] outputFiles) {
		this.outputFiles = outputFiles;
	}

	@Visible(false)
	public String[] getInputFiles() {
		return inputFiles;
	}

	public void setInputFiles(String[] inputFiles) {
		this.inputFiles = inputFiles;
	}

	@Label("Extra")
	@ComponentWidth(60)
	@Position(4)
	public boolean isExtraCredit() {
		return extraCredit;
	}

	/**
	 * @return If this feature is ready to grade
	 */
	public boolean preAutoGrade() {
		return featureChecker != null && !cannotAutoGrade && !graded
				&& project != null && project.hasBeenRun()
				&& project.canBeRun();

	}
	
	public static String getFeedbackFolderName (SakaiProject aProject) {
		FileProxy feedbackFolder = aProject.getStudentAssignment().getFeedbackFolder();
//		return feedbackFolder.getAbsoluteName() + "/";
		return feedbackFolder.getMixedCaseAbsoluteName() + "/";

	}

	public void setProject(SakaiProject aProject) {
		
		project = aProject;
		if (featureChecker != null)
			featureChecker.setProject(aProject);
		feedbackFolder = aProject.getStudentAssignment().getFeedbackFolder();
		
		String feedbackFolderName = getFeedbackFolderName(aProject);
		
//		feedbackFileName = feedbackFolder.getAbsoluteName() + "/"
//				+ FEEDBACK_FILE_PREFIX + featureName + FEEDBACK_FILE_SUFFIX;
//		notesFileName = feedbackFolder.getAbsoluteName() + "/"
//				+ NOTES_FILE_PREFIX + featureName + FEEDBACK_FILE_SUFFIX;
//		resultsFileName = feedbackFolder.getAbsoluteName() + "/"
//				+ RESULTS_FILE_PREFIX + featureName + FEEDBACK_FILE_SUFFIX;
		feedbackFileName = feedbackFolderName + featureName + FEEDBACK_FILE_SUFFIX;
		manualNotesFileName = feedbackFolderName
				+ MANUAL_NOTES_FILE_PREFIX + featureName + FEEDBACK_FILE_SUFFIX;
		autoNotesFileName = feedbackFolderName
				+ AUTO_NOTES_FILE_PREFIX + featureName + FEEDBACK_FILE_SUFFIX;
		resultsFileName = feedbackFolderName
				+ RESULTS_FILE_PREFIX + featureName + FEEDBACK_FILE_SUFFIX;
		graded = false;
		cannotAutoGrade = false;
		manualNotes = retrieveManualNotes();
		autoNotes = retrieveAutoNotes();
		score = 0;
		
//		if (project.isNoProjectFolder()) {
//			pureSetScore(0);			
//		}
		
		
		// will let project stepper worry abput this
//		result = retrieveResult();
		firePropertyChange("this", null, this);
	}

	public void setProjectDatabase(SakaiProjectDatabase aProjectDatabase) {
		projectDatabase = aProjectDatabase;
	}

	@Visible(false)
	public String getFeedbackFileName() {
		return feedbackFileName;
	}

	@Visible(false)
	public String getManualNotesFileName() {
		return manualNotesFileName;
	}
	
	@Visible(false)
	public String getAutoNotesFileName() {
		return autoNotesFileName;
	}

	@Visible(false)
	public String getResultFileName() {
		return resultsFileName;
	}

	public void autoGrade() {
		if (cannotAutoGrade) {
			return;
		}
		CurrentProjectHolder.getOrCreateCurrentProject().setInfinite(false);
//		propertyChangeSupport.firePropertyChange("AutoGrade", false, true); // change the selected feature before autograding
		
		project.setCurrentGradingFeature(feature); // change the selected feature before autograding

		CheckResult result = featureChecker.check();
		String anAutoNotes = result.getAutoNotes() + result.getLog();
//		setAutoNotes(result.getAutoNotes());
		setAutoNotes(anAutoNotes);

		if (result == null) {
			Tracer.error("Could not autograde:" + this.getFeatureName());
			cannotAutoGrade = true;
			pureSetScore(0);
			return;
		}
		pureSetScore(result.getScore());
		projectDatabase.getAutoFeedback().recordAutoGrade(this, result);
		setSelected(true); 

	}

	@Override
	public void comment() {
		projectDatabase.getManualFeedback().comment(this);
	}

	@Position(0)
	@ComponentWidth(250)
	@Label("Feature")
	public String getFeatureName() {
		return featureName;
	}

	@Position(1)
	@ComponentWidth(30)
	public double getMax() {
		return maxScore;
	}

	@Position(2)
	@ComponentWidth(40)
	public double getScore() {
		return score;
	}
	@Visible(false)
	public String getOutput() {
		return output;
	}

	public void setOutput(String output) {
		this.output = output;
	}

	public void reset() {
		pureSetScore(maxScore);
	}

	public boolean preSetScore() {
//		return featureChecker == null || preAutoGrade();
		return true; // we will allow override through this feature rather than linked feature

	}

	public void setScore(double newVal) {
//		double oldVal = score;
//		// piggyback this notification on score notfication to project stepper
//		if (!isManual()) {
//			NotesGenerator notesGenerator = projectDatabase.getNotesGenerator();
//			setManualNotes(notesGenerator.appendNotes(
//					getManualNotes(), 
//					notesGenerator.autoFeatureScoreOverrideNotes(projectDatabase.getProjectStepper(), this, oldVal, newVal)));
//		}
//		pureSetScore(newVal);
		internalSetScore(newVal);
		scoreSetManually = true;
		FeatureScoreUserChange.newCase(projectDatabase, (OverviewProjectStepper) projectDatabase.getProjectStepper(), project, this, newVal, this);


	}
	@Override
	public void internalSetScore(double newVal) {
		double oldVal = score;
		// piggyback this notification on score notfication to project stepper
		if (!isManual()) {
			NotesGenerator notesGenerator = projectDatabase.getNotesGenerator();
			setManualNotes(notesGenerator.appendNotes(
					getManualNotes(), 
					notesGenerator.autoFeatureScoreOverrideNotes(projectDatabase.getProjectStepper(), this, oldVal, newVal)));
		}
		pureSetScore(newVal);
//		scoreSetManually = true;
//		FeatureScoreUserChange.newCase(projectDatabase, (OverviewProjectStepper) projectDatabase.getProjectStepper(), project, this, newVal, this);
//		if (!isManual()) {
//			NotesGenerator notesGenerator = projectDatabase.getNotesGenerator();
//			setNotes(notesGenerator.appendNotes(
//					getNotes(), 
//					notesGenerator.autoFeatureScoreOverrideNotes(projectDatabase.getProjectStepper(), this, oldVal, newVal)));
//		}
//		if (score != maxScore)
//			comment();

	}

	public void initScore(double score) {
		this.score = score;
	}

	public void pureSetScore(double score) {
		boolean oldFullCredit = isFullCredit();
		pureSetGraded(true);		
		double oldScore = this.score;
		this.score = score;
		boolean newFullCredit = isFullCredit();
		propertyChangeSupport.firePropertyChange("Score", oldScore, score);
		propertyChangeSupport.firePropertyChange("FullCredit", oldFullCredit, newFullCredit);
		firePropertyChange("Graded", null, true);
	}
	
	

	@util.annotations.Explanation("Setting property to true (re)grades the feature")
	@Position(5)
	@ComponentWidth(70)
	public boolean isGraded() {
		return graded; // this value is loaded from spreadsheets
	}
	@Visible(false)
	@Override
	public boolean isAutoWithNotFullCredit() {
		return isAutoGradable() && !isFullCredit();
	}
	@Override
	@util.annotations.Explanation("Setting property to true gives full credit for feature")
	@Position(6)
	@ComponentWidth(90)
	public boolean isFullCredit() {
		return isRestriction? score== 0:score == maxScore;
	}
	@Override
	@Visible(false)
	public boolean isPartialCredit() {
		return score > 0 && !isFullCredit();
	}
	
	
	
	@Override
	public void setFullCredit(boolean newVal) {
		if (newVal) {
			setScore(isRestriction? 0:maxScore);
		}
	}
	boolean validate;
	@Override
	@util.annotations.Explanation("Setting property to true indicates in manual notes that feature has been manually checked. To invalidate, felete text in manual notes.")
	@Position(7)
	@ComponentWidth(90)
	public boolean isValidate() {
		return validate;
	}
	@Override
	public void setValidate(boolean newVal) {	
		boolean oldVal = validate;
//		if (newVal) // let true remain true as once validated, always validated. 
			validate = newVal;
//		propertyChangeSupport.firePropertyChange("Validate", false, true);
		propertyChangeSupport.firePropertyChange("Validate", oldVal, newVal);
		FeatureValidated.newCase(projectDatabase, (OverviewProjectStepper) projectDatabase.getProjectStepper(), project, this, this);

	}
	
	@Override
	public void pureSetValidate(boolean newVal) {
		boolean oldVal = validate;
		validate = newVal;
		propertyChangeSupport.firePropertyChange("Validate", oldVal, newVal);

	}
	
	@Visible(false)
	@Override
	public boolean isManualOverride() {
		return isAutoGradable() && !isFullCredit();
	}
	
	@Visible(false)
	@Override
	public boolean isManualWithNotFullCredit() {
		return !isAutoGradable() && score!= maxScore;
	}
	
	@Visible(false)
	@Override
	public boolean isAutoNotGraded() {
		return isAutoGradable() && !isGraded();		
	}	

	public void pureSetGraded(boolean newValue) {
		if (graded == newValue)
			return;

		graded = newValue;
		if (linkedFeature != null && graded && !cannotAutoGrade) {
			linkedFeature.setGraded(true);
		}
		if (cannotAutoGrade) {
			propertyChangeSupport.firePropertyChange("this", null, this); // our																// update
			return;
		}
	}

	public void setGraded(boolean newValue) {
//		if (graded == newValue)
//			return;
		graded = newValue;
		
		if (featureChecker != null) {
			autoGrade();
		} else {
			correct();
		}
		graded = true;
		FeatureUserGraded.newCase(projectDatabase, (OverviewProjectStepper) projectDatabase.getProjectStepper(), project, this, this);
		setSelected(true);
	}

	// this is not displayed
	// @Position(3)
	public void correct() {
		pureSetScore(maxScore);
	}

	@Override
	@Position(3)
	@Label("Auto")
	@ComponentWidth(60)
	@Visible(false)
	public boolean isAutoGradable() {
		return featureChecker != null && autoGradable;
	}
	
	@Override
	@Position(3)
	@Label("Manual")
	@ComponentWidth(70)
	public boolean isManual() {
		return !isAutoGradable();
	}


	boolean isSelected;

	@util.annotations.Explanation("Setting this property to true shows  auto and manual notes and transcript of this feature.")
	@Position(8)
	@ComponentWidth(80)
	public boolean isSelected() {
		return isSelected;
	}

	public void setSelected(boolean newVal) {
		if (isSelected == newVal)
			return;
		boolean oldVal = isSelected;
		isSelected = newVal;
		// why set graded to true?
//		if (newVal) {
//			setGraded(true);
//		}
		propertyChangeSupport
				.firePropertyChange("Selected", oldVal, isSelected);
		if (newVal)
			FeatureSelected.newCase(projectDatabase, (OverviewProjectStepper) projectDatabase.getProjectStepper(), project, this, this);

	}

	@Visible(false)
	public String getAutoNotes() {
		return autoNotes;
	}

	@Visible(false)
	public void setAutoNotes(String result) {
		String oldVal = result;
		this.autoNotes = result;
		FeatureAutoNotesChanged.newCase(projectDatabase, (OverviewProjectStepper) projectDatabase.getProjectStepper(), project, this, autoNotes, this);
		recordAutoNotes();
		// let project stepper get this value from feature recorder
//		recordResult();
		propertyChangeSupport.firePropertyChange("autoNotes", oldVal, result);

	}

	@Visible(false)
	public String getManualNotes() {
		return manualNotes;
	}
	
	public static boolean maybeDeleteFile(String fileName) {
		File file = new File(fileName);
		if (!file.exists()) return true; // equivalent to deleted file		 
		return file.delete();
		
	}

	void recordManualNotes() {
		String fileName = getManualNotesFileName();
		if (manualNotes.equals("") && maybeDeleteFile(fileName)) {// why create file on empty notes? 
			
			return;
		}
		try {
			FileUtils.writeStringToFile(new File(fileName), manualNotes);
			FeatureManualNotesSaved.newCase(projectDatabase, (OverviewProjectStepper) projectDatabase.getProjectStepper(), project, this, fileName, manualNotes, this);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	void recordAutoNotes() {
		String fileName = getAutoNotesFileName();
		if (autoNotes.equals("") && maybeDeleteFile(fileName)) {// why create file on empty notes? 
			
			return;
		}
		try {
			FileUtils.writeStringToFile(new File(fileName), autoNotes);
			FeatureAutoNotesSaved.newCase(projectDatabase, (OverviewProjectStepper) projectDatabase.getProjectStepper(), project, this, fileName, autoNotes, this);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	// not called currently
	String retrieveResult() {		
		try {
			String fileName = getResultFileName();
			String retVal = FileUtils.readFileToString(new File(fileName));
			FeatureAutoResultFormatLoaded.newCase(projectDatabase, (OverviewProjectStepper) projectDatabase.getProjectStepper(), project, this, fileName, retVal, this);

//			return FileUtils.readFileToString(new File(getResultFileName()));
//			return FileUtils.readFileToString(new File(fileName));
			return retVal;


		} catch (IOException e) {
			return "";
		}
	}
	// not called currently
	void recordResult() {
		if (autoNotes.equals("")) // why create file?
			return;
		try {
			FileUtils.writeStringToFile(new File(getResultFileName()), autoNotes);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	String retrieveManualNotes() {
		String fileName = getManualNotesFileName();
		try {
//			return FileUtils.readFileToString(new File(getManualNotesFileName()));
			String retVal = FileUtils.readFileToString(new File(fileName));
			FeatureManualNotesLoaded.newCase(projectDatabase, (OverviewProjectStepper) projectDatabase.getProjectStepper(), project, this, fileName, retVal, this);
			return retVal;

		} catch (IOException e) {
			return "";
		}
	}
	
	String retrieveAutoNotes() {
		String fileName = getAutoNotesFileName();
		try {
			String retVal = FileUtils.readFileToString(new File(fileName));
			FeatureAutoNotesLoaded.newCase(projectDatabase, (OverviewProjectStepper) projectDatabase.getProjectStepper(), project, this, fileName, retVal, this);

			return retVal;
//			return FileUtils.readFileToString(new File(getAutoNotesFileName()));
		} catch (IOException e) {
			return ""; // not really an exception, bad programming
		}
	}

	@Visible(false)
	public void setManualNotes(String newVal) {
		String oldVal = manualNotes;
		this.manualNotes = newVal;
//		FeatureManualNotesChanged.newCase(projectDatabase, (OverviewProjectStepper) projectDatabase.getProjectStepper(), project, this, newVal, this);
		recordManualNotes();
		propertyChangeSupport.firePropertyChange("ManualNotes", oldVal, newVal);
	}
	
	

	@Override
	public void addPropertyChangeListener(PropertyChangeListener aListener) {
		propertyChangeSupport.addPropertyChangeListener(aListener);
	}

	@Override
	public void removePropertyChangeListener(PropertyChangeListener aListener) {
		propertyChangeSupport.removePropertyChangeListener(aListener);
	}

	public void firePropertyChange(String aName, Object anOldValue,
			Object aNewValue) {
		propertyChangeSupport.firePropertyChange(aName, anOldValue, aNewValue);
	}
	@Visible(false)
	@Override
	public Checkable getFeature() {
		return feature;
	}
	@Override
	@Visible(false)
	public void setFeature(Checkable feature) {
		this.feature = feature;
	}
	@Override
	@Visible(false)
	public String getResultFormat() {
		return resultFormat;
	}
	@Override
	public void setResultFormat(String newValue) {
		this.resultFormat = newValue;
		FeatureAutoResultFormatChanged.newCase(projectDatabase, (OverviewProjectStepper) projectDatabase.getProjectStepper(), project, this, newValue, this);
	}
	
//	@Override
//	public Color computeColor() {
//		if (!getNotes().isEmpty())
//			return Color.GREEN;	
//		if (!isGraded() && isExtraCredit())
//			return Color.BLUE;
//		if (!isGraded())
////		 if (isAutoNotGraded())
//			return Color.RED;
//		// put some notes if these conditions hold
//		 if (isAutoWithNotFullCredit() ||
//				 isManualWithNotFullCredit() ) 
//			return Color.PINK;
//		
////		 if (!isGraded())
////			return Color.BLUE;
////		 if (isManualWithNotFullCredit())
////			return Color.PINK;
//		
//		return null;
//	}

}

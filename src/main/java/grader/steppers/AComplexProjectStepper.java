package grader.steppers;

import grader.assignment.GradingFeature;
import grader.assignment.GradingFeatureList;
import grader.sakai.project.SakaiProject;
import grader.sakai.project.SakaiProjectDatabase;
import grader.settings.navigation.NavigationSetter;
import grader.trace.settings.InvalidOnyenRangeException;
import grader.trace.settings.MissingOnyenException;
import grader.trace.steppers.FeedbackVisited;
import grader.trace.steppers.MainVisited;
import grader.trace.steppers.ProblemHistoryVisited;
import grader.trace.steppers.SourceVisited;
import grader.trace.steppers.StudentHistoryVisited;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JTextArea;

import util.annotations.ComponentsVisible;
import util.annotations.Explanation;
import util.annotations.Label;
import util.annotations.Position;
import util.annotations.PreferredWidgetClass;
import util.annotations.Row;
import util.annotations.StructurePattern;
import util.annotations.StructurePatternNames;
import util.annotations.Visible;
import util.models.DynamicEnum;
import util.models.LabelBeanModel;
@ComponentsVisible(false)
@StructurePattern(StructurePatternNames.BEAN_PATTERN)
public class AComplexProjectStepper implements ComplexProjectStepper{
	MainProjectStepper mainProjectStepper;
	
//	@Position(0)
	@Row(0)
	@Visible(true)
	@Label("Main")
	@Override
	public MainProjectStepper getMainProjectStepper() {
		return mainProjectStepper;
	}
	@Override
	public void setMainProjectStepper(MainProjectStepper mainProjectStepper) {
		this.mainProjectStepper = mainProjectStepper;
	}

	public void setProjectDatabase(SakaiProjectDatabase aProjectDatabase) {
		mainProjectStepper = new AMainProjectStepper();
		mainProjectStepper.setProjectDatabase(aProjectDatabase);
	}

	public void proceed() {
		mainProjectStepper.proceed();
	}

	public void addPropertyChangeListener(PropertyChangeListener aListener) {
		mainProjectStepper.addPropertyChangeListener(aListener);
	}

	public boolean setProject(SakaiProject newVal) {
		return mainProjectStepper.setProject(newVal);
	}

	public void output() {
		mainProjectStepper.output();
	}

	public void setAutoRun(boolean newVal) {
		mainProjectStepper.setAutoRun(newVal);
	}

	public void setHasMoreSteps(boolean newVal) {
		mainProjectStepper.setHasMoreSteps(newVal);
	}

	public boolean runProjectsInteractively() throws InvalidOnyenRangeException {
		return mainProjectStepper.runProjectsInteractively();
	}

	public boolean preGetGradingFeatures() {
		return mainProjectStepper.preGetGradingFeatures();
	}

	public boolean preNext() {
		return mainProjectStepper.preNext();
	}

	public boolean prePrevious() {
		return mainProjectStepper.prePrevious();
	}

	public void previous() {
		mainProjectStepper.previous();
	}

	public boolean preRunProjectsInteractively() {
		return mainProjectStepper.preRunProjectsInteractively();
	}

	public void propertyChange(PropertyChangeEvent arg0) {
		mainProjectStepper.propertyChange(arg0);
	}
//	@Visible(true)
	public void openSource() {
		mainProjectStepper.openSource();
	}

	public double getScore() {
		return mainProjectStepper.getScore();
	}

	public void setScore(double newVal) {
		mainProjectStepper.setScore(newVal);
	}

	public boolean waitForClearance() {
		return mainProjectStepper.waitForClearance();
	}
	@Visible(false)
	public SakaiProjectDatabase getProjectDatabase() {
		return mainProjectStepper.getProjectDatabase();
	}



	public void setOnyen(String anOnyen) throws MissingOnyenException {
		mainProjectStepper.setOnyen(anOnyen);
	}

	public boolean setProject(String anOnyen) {
		return mainProjectStepper.setProject(anOnyen);
	}

	public boolean isAutoRun() {
		return mainProjectStepper.isAutoRun();
	}

	public void autoRun() {
		mainProjectStepper.autoRun();
	}

	public boolean hasMoreSteps() {
		return mainProjectStepper.hasMoreSteps();
	}

	public SakaiProject getProject() {
		return mainProjectStepper.getProject();
	}

	public void configureNavigationList() {
		mainProjectStepper.configureNavigationList();
	}

	public boolean preDone() {
		return mainProjectStepper.preDone();
	}

	public void done() {
		mainProjectStepper.done();
	}

	public boolean preAutoGrade() {
		return mainProjectStepper.preAutoGrade();
	}
	@Visible(true)
	public void autoGrade() {
		mainProjectStepper.autoGrade();
	}

	public GradingFeatureList getGradingFeatures() {
		return mainProjectStepper.getGradingFeatures();
	}

	public boolean isAllGraded() {
		return mainProjectStepper.isAllGraded();
	}

	public void next() {
		mainProjectStepper.next();
	}

	public boolean move(boolean forward, boolean isFiltered) {
		return mainProjectStepper.move(forward, isFiltered);
	}

	public boolean isAutoAutoGrade() {
		return mainProjectStepper.isAutoAutoGrade();
	}

	public void setAutoAutoGrade(boolean newVal) {
		mainProjectStepper.setAutoAutoGrade(newVal);
	}

	public void autoAutoGrade() {
		mainProjectStepper.autoAutoGrade();
	}

	public void setFrame(Object aFrame) {
		mainProjectStepper.setFrame(aFrame);
	}

	public Object getFrame() {
		return mainProjectStepper.getFrame();
	}

	public LabelBeanModel getPhoto() {
		return mainProjectStepper.getPhoto();
	}
	@Visible(true)
//	@Position(1)
	@Row(1)
	@PreferredWidgetClass(JTextArea.class)
	@Explanation("The contents of the summary seen by the student.")
	public String getFeedback() {
		return mainProjectStepper.getFeedback();
	}

	public String getTranscript() {
		return mainProjectStepper.getTranscript();
	}
	@Override
	@Visible(false)
//	@Position(4)
	@Row(4)
	@Explanation("Changes to filter settings will be used in subsequent navigation commands.")
	public NavigationSetter getNavigationSetter() {
		return mainProjectStepper.getNavigationSetter();
	}

	public void validate() {
		mainProjectStepper.validate();
	}

	public boolean runProjectsInteractively(String aGoToOnyen)
			throws MissingOnyenException, InvalidOnyenRangeException {
		return mainProjectStepper.runProjectsInteractively(aGoToOnyen);
	}

	public void setName(String newVal) {
		mainProjectStepper.setName(newVal);
	}

	public String getName() {
		return mainProjectStepper.getName();
	}

	public void resetNoFilteredRecords() {
		mainProjectStepper.resetNoFilteredRecords();
	}

	public String getOnyen() {
		return mainProjectStepper.getOnyen();
	}

	public void setOverallNotes(String newVal) {
		mainProjectStepper.setOverallNotes(newVal);
	}

	public double getMultiplier() {
		return mainProjectStepper.getMultiplier();
	}

	public String getOverallNotes() {
		return mainProjectStepper.getOverallNotes();
	}
	
	public void internalSetOnyen(String anOnyen) throws MissingOnyenException {
		mainProjectStepper.internalSetOnyen(anOnyen);
	}

	public void setMultiplier(double newValue) {
		mainProjectStepper.setMultiplier(newValue);
	}

	public boolean isProceedWhenDone() {
		return mainProjectStepper.isProceedWhenDone();
	}

	public void toggleProceedWhenDone() {
		mainProjectStepper.toggleProceedWhenDone();
	}

	public void internalSetMultiplier(double newValue) {
		mainProjectStepper.internalSetMultiplier(newValue);
	}

	public String getSequenceNumber() {
		return mainProjectStepper.getSequenceNumber();
	}

	public void computeNextColors() {
		mainProjectStepper.computeNextColors();
	}

	public void setComputedFeedback() {
		mainProjectStepper.setComputedFeedback();
	}

	public void setStoredFeedback() {
		mainProjectStepper.setStoredFeedback();
	}

	public void setStoredOutput() {
		mainProjectStepper.setStoredOutput();
	}

	public GradingFeature getSelectedGradingFeature() {
		return mainProjectStepper.getSelectedGradingFeature();
	}

	public void internalSetManualNotes(String newVal) {
		mainProjectStepper.internalSetManualNotes(newVal);
	}

	public void internalSetResult(String newVal) {
		mainProjectStepper.internalSetResult(newVal);
	}

	public void internalSetAutoNotes(String newVal) {
		mainProjectStepper.internalSetAutoNotes(newVal);
	}

	public void internalSetOverallNotes(String newVal) {
		mainProjectStepper.internalSetOverallNotes(newVal);
	}

	public void setColors() {
		mainProjectStepper.setColors();
	}

	public boolean isChanged() {
		return mainProjectStepper.isChanged();
	}

	public void setChanged(boolean changed) {
		mainProjectStepper.setChanged(changed);
	}

	public void setComputedScore() {
		mainProjectStepper.setComputedScore();
	}

	public boolean isSettingUpProject() {
		return mainProjectStepper.isSettingUpProject();
	}

	public void setSettingUpProject(boolean settingUpProject) {
		mainProjectStepper.setSettingUpProject(settingUpProject);
	}

	public boolean shouldVisit() {
		return mainProjectStepper.shouldVisit();
	}

	public void internalSetScore(double newVal) {
		mainProjectStepper.internalSetScore(newVal);
	}

	public void setMultiplierColor() {
		mainProjectStepper.setMultiplierColor();
	}

	public void setScoreColor() {
		mainProjectStepper.setScoreColor();
	}

	public void setOverallNotesColor() {
		mainProjectStepper.setOverallNotesColor();
	}

	public boolean runAttempted() {
		return mainProjectStepper.runAttempted();
	}

	public int getCurrentOnyenIndex() {
		return mainProjectStepper.getCurrentOnyenIndex();
	}

	public void setCurrentOnyenIndex(int currentOnyenIndex) {
		mainProjectStepper.setCurrentOnyenIndex(currentOnyenIndex);
	}

	public int getFilteredOnyenIndex() {
		return mainProjectStepper.getFilteredOnyenIndex();
	}

	public void setFilteredOnyenIndex(int filteredOnyenIndex) {
		mainProjectStepper.setFilteredOnyenIndex(filteredOnyenIndex);
	}

	public String getAutoNotes() {
		return mainProjectStepper.getAutoNotes();
	}

	public String getManualNotes() {
		return mainProjectStepper.getManualNotes();
	}

	public void setManualNotes(String newVal) {
		mainProjectStepper.setManualNotes(newVal);
	}
	@Override
	public OverviewProjectStepper getOverviewProjectStepper() {
		return mainProjectStepper.getOverviewProjectStepper();
	}
	@Override
	public void setOverviewProjectStepper(
			OverviewProjectStepper overviewProjectStepper) {
		mainProjectStepper.setOverviewProjectStepper(overviewProjectStepper);
	}
	@Override
	@Visible(false)
//	@Position(2)
	@Row(2)
	@PreferredWidgetClass(JTextArea.class)
	@Explanation("Correct result (if provided) diffed with actual result.")
	public String getResultDiff() {
		return mainProjectStepper.getResultDiff();
	}
	@Override
	@Visible(true)
//	@Position(2)
	@Row(3)
	@PreferredWidgetClass(JTextArea.class)
	@Explanation("Results of style checker")
	public String getSourceChecks() {
		return mainProjectStepper.getSourceChecks();
	}
	@Override
	@Visible(true)
//	@Position(3)
	@Row(3)
	@PreferredWidgetClass(JTextArea.class)
	@Explanation("Comment on the source code by editing it.")
	public String getSource() {
		return mainProjectStepper.getSource();
	}
	@Override
	@Visible(true)
//	@Position(4)
	@Row(4)
	@PreferredWidgetClass(JTextArea.class)
	@Explanation("History of manual interventions on this problem.")
	public String getProblemHistory() {
		return mainProjectStepper.getProblemHistory();
	}
	
	@Override
	@Visible(true)
//	@Position(5)
	@Row(5)
	@PreferredWidgetClass(JTextArea.class)
	@Explanation("History of manual interventions for this student.")
	public String getStudentHistory() {
		return mainProjectStepper.getStudentHistory();
	}
	
	
	@Override
	public void internalSetSource(String newValue) {
		
		mainProjectStepper.internalSetSource(newValue);
	}
	@Override
	public void setResultDiff(String newValue) {
		
		mainProjectStepper.setResultDiff(newValue);
	}
	@Override
//	@Visible(true)
	public void run() {
		mainProjectStepper.run();
		
	}
	@Override
	public boolean isPlayMode() {
		// TODO Auto-generated method stub
		return mainProjectStepper.isPlayMode();
	}
	@Override
	public void setPlayMode(boolean playMode) {
		mainProjectStepper.setPlayMode(playMode);
	}
	@Override
	public void togglePlayPause() {
		mainProjectStepper.togglePlayPause();
	}
	@Override
	public void setSource(String newVal) {
		mainProjectStepper.setSource(newVal);
		
	}
	@Override
	public boolean preTogglePlayPause() {
		return mainProjectStepper.preTogglePlayPause();
	}
	@Override
	public void save() {
		mainProjectStepper.save();
		
	}
	@Override
	public boolean preSetManualNotes() {
		return mainProjectStepper.preSetManualNotes();
	}
	@Override
	@Visible(true)
	@Position(4)
	public void cleanAllFeedbackFolders() {
		mainProjectStepper.cleanAllFeedbackFolders();
		
	}
	@Override
	@Position(2)
	@Visible(true)
	public void cleanFeedbackFolder() {
		mainProjectStepper.cleanFeedbackFolder();		
	}
	@Visible(true)
	@Position(3)
	public void resetFeatureSpreadsheet() {
		mainProjectStepper.resetFeatureSpreadsheet();
		
	}
	@Override
	
	public boolean preRestoreFeatureSpreadsheet() {		
		return mainProjectStepper.preRestoreFeatureSpreadsheet();
	}
	@Override
	@Visible(true)
	@Position(4)
	public void restoreFeatureSpreadsheet() {
		mainProjectStepper.restoreFeatureSpreadsheet();
		
	}
	
	@Override
	public void quit() {
		mainProjectStepper.quit();		
	}
	@Override
	public void setProceedWhenDone(boolean proceedWhenDone) {
		mainProjectStepper.setProceedWhenDone(proceedWhenDone);
		
	}
	@Override
	@Visible(false)
	public boolean isExitOnQuit() {
		return mainProjectStepper.isExitOnQuit();
	}
	@Override
	public void setExitOnQuit(boolean newVal) {
		mainProjectStepper.setExitOnQuit(newVal);
	}
	public String getTASourceCodeComments() {
		return mainProjectStepper.getTASourceCodeComments();
	}
	public void sync() {
		mainProjectStepper.sync();
		
	}
	@Override
	public void newFocus(String aProperty) {
		
			if (aProperty == null) return;
			if (aProperty.equals("feedback"))
				FeedbackVisited.newCase(getProjectDatabase(), this, getProject(), aProperty, this);
			else if (aProperty.equals("source"))
				SourceVisited.newCase(getProjectDatabase(), this, getProject(), aProperty, this);
			else if (aProperty.equals("mainprojectstepper"))
				MainVisited.newCase(getProjectDatabase(), this, getProject(), aProperty, this);
			else if (aProperty.equals("problemhistory"))
				ProblemHistoryVisited.newCase(getProjectDatabase(), this, getProject(), aProperty, this);
			else if (aProperty.equals("studenthistory"))
				StudentHistoryVisited.newCase(getProjectDatabase(), this, getProject(), aProperty, this);
			
			
	}
	@Override
	public double getSourcePoints() {
		return mainProjectStepper.getSourcePoints();
	}
	@Override
	public void setSourcePoints(double newValue) {
		mainProjectStepper.setSourcePoints(newValue);
		
	}
	
	@Override
	public void internalSetSourcePoints(double newValue) {
		mainProjectStepper.internalSetSourcePoints(newValue);
		
	}
	@Override
	public void loadSourceFromFile() {
		mainProjectStepper.loadSourceFromFile();		
	}
	@Override
	public String getDisplayedOnyen() {
		return mainProjectStepper.getDisplayedOnyen();
	}
	@Override
	public String getDisplayedName() {
		return mainProjectStepper.getDisplayedName();
	}
	@Override
	@Visible(false)
	public String getWaitingThreads() {
		// TODO Auto-generated method stub
		return mainProjectStepper.getWaitingThreads();
	}
	@Override
	@Visible(false)
	public void nextDocument() {
		mainProjectStepper.nextDocument();
		
	}
	@Override
	public boolean preNextDocument() {
		// TODO Auto-generated method stub
		return mainProjectStepper.preNextDocument();
	}
	@Override
	public boolean preFirstDocument() {
		// TODO Auto-generated method stub
		return mainProjectStepper.preFirstDocument();
	}
	@Override
	public void firstDocument() {
		mainProjectStepper.firstDocument();
		
	}
	@Override
	public boolean preTerminate() {
		return mainProjectStepper.preTerminate();
	}
	@Override
	@Visible(true)
	@Position(1)
	public void terminate() {
		mainProjectStepper.terminate();
	}
	@Override
	public boolean preRun() {
		return mainProjectStepper.preRun();
	}
	@Override
	@Visible(true)
	@Position(0)
	public DynamicEnum runArgs() {
		return mainProjectStepper.runArgs();
	}
	@Override
	@Visible(false)

	public GradedProjectOverview getGradedProjectOverview() {
		// TODO Auto-generated method stub
		return mainProjectStepper.getGradedProjectOverview();
	}
	@Override
	
	@Visible(false)
	public GradedProjectTextOverview getTextOverview() {
		// TODO Auto-generated method stub
		return mainProjectStepper.getTextOverview();
	}
	@Override
	public void setOnyenIndex(int onyenIndex)  {
		mainProjectStepper.setOnyenIndex(onyenIndex);

		
	}
	@Override
	public void previousFiltered() {
		mainProjectStepper.previousFiltered();
	}
	@Override
	public void nextFiltered() {
		mainProjectStepper.nextFiltered();
		
	}
	@Override
	public void downloadSource() {
		mainProjectStepper.downloadSource();
		
	}
	@Override
	public void uploadSource() {
		mainProjectStepper.uploadSource();
	}


}

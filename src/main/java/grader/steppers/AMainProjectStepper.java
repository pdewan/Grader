package grader.steppers;

import grader.assignment.GradingFeature;
import grader.assignment.GradingFeatureList;
import grader.sakai.project.SakaiProject;
import grader.sakai.project.SakaiProjectDatabase;
import grader.settings.navigation.NavigationSetter;
import grader.trace.settings.InvalidOnyenRangeException;
import grader.trace.settings.MissingOnyenException;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JTextArea;

import util.annotations.ComponentHeight;
import util.annotations.ComponentWidth;
import util.annotations.Explanation;
import util.annotations.PreferredWidgetClass;
import util.annotations.Row;
import util.annotations.StructurePattern;
import util.annotations.StructurePatternNames;
import util.annotations.Visible;
import util.models.DynamicEnum;
import util.models.LabelBeanModel;
import bus.uigen.ObjectEditor;
@StructurePattern(StructurePatternNames.BEAN_PATTERN)
public class AMainProjectStepper implements MainProjectStepper {
	OverviewProjectStepper overviewProjectStepper;

	public void setProjectDatabase(SakaiProjectDatabase aProjectDatabase) {
		overviewProjectStepper = new AnOverviewProjectStepper();
		overviewProjectStepper.setProjectDatabase(aProjectDatabase);
	}
	@Row(0)
//	@Visible(true)
	@Override
	public OverviewProjectStepper getOverviewProjectStepper() {
		return overviewProjectStepper;
	}
	@Override
	public void setOverviewProjectStepper(
			OverviewProjectStepper overviewProjectStepper) {
		this.overviewProjectStepper = overviewProjectStepper;
	}

	public void proceed() {
		overviewProjectStepper.proceed();
	}

	public void addPropertyChangeListener(PropertyChangeListener aListener) {
		overviewProjectStepper.addPropertyChangeListener(aListener);
	}

	public boolean setProject(SakaiProject newVal) {
		return overviewProjectStepper.setProject(newVal);
	}

	public void output() {
		overviewProjectStepper.output();
	}
	@Visible(true)
	public void openSource() {
		overviewProjectStepper.openSource();
	}

	@Visible(false) 
	public double getScore() {
		return overviewProjectStepper.getScore();
	}

	public void setScore(double newVal) {
		overviewProjectStepper.setScore(newVal);
	}

	public boolean waitForClearance() {
		return overviewProjectStepper.waitForClearance();
	}
	@Override
	@Visible(false) 
 public SakaiProjectDatabase getProjectDatabase() {
		return overviewProjectStepper.getProjectDatabase();
	}

	

	public void setOnyen(String anOnyen) throws MissingOnyenException {
		overviewProjectStepper.setOnyen(anOnyen);
	}
	
	public boolean setProject(String anOnyen) {
		return overviewProjectStepper.setProject(anOnyen);
	}
	
	@Visible(false) 
	public boolean isAutoRun() {
		return overviewProjectStepper.isAutoRun();
	}

	public void setAutoRun(boolean newVal) {
		overviewProjectStepper.setAutoRun(newVal);
	}

	public void autoRun() {
		overviewProjectStepper.autoRun();
	}

	public boolean hasMoreSteps() {
		return overviewProjectStepper.hasMoreSteps();
	}

	public void setHasMoreSteps(boolean newVal) {
		overviewProjectStepper.setHasMoreSteps(newVal);
	}

@Visible(false) 
	public SakaiProject getProject() {
		return overviewProjectStepper.getProject();
	}

	public boolean runProjectsInteractively() throws InvalidOnyenRangeException {
		return overviewProjectStepper.runProjectsInteractively();
	}

	public void configureNavigationList() {
		overviewProjectStepper.configureNavigationList();
	}

	public boolean preDone() {
		return overviewProjectStepper.preDone();
	}

	public void done() {
		overviewProjectStepper.done();
	}

	@Visible(false) 
	public boolean pregetGradingFeatures() {
		return overviewProjectStepper.preGetGradingFeatures();
	}

	public boolean preAutoGrade() {
		return overviewProjectStepper.preAutoGrade();
	}

	public void autoGrade() {
		overviewProjectStepper.autoGrade();
	}
	@Row(2)
	public GradingFeatureList getGradingFeatures() {
		return overviewProjectStepper.getGradingFeatures();
	}

	@Visible(false)
	public boolean isAllGraded() {
		return overviewProjectStepper.isAllGraded();
	}

	public boolean preNext() {
		return overviewProjectStepper.preNext();
	}

	public void next() {
		overviewProjectStepper.next();
	}

	public boolean prePrevious() {
		return overviewProjectStepper.prePrevious();
	}

	public void previous() {
		overviewProjectStepper.previous();
	}

	public boolean preRunProjectsInteractively() {
		return overviewProjectStepper.preRunProjectsInteractively();
	}

	public boolean move(boolean forward, boolean isFiltered) {
		return overviewProjectStepper.move(forward, isFiltered);
	}

	@Visible(false)
	public boolean isAutoAutoGrade() {
		return overviewProjectStepper.isAutoAutoGrade();
	}

	public void propertyChange(PropertyChangeEvent evt) {
		overviewProjectStepper.propertyChange(evt);
	}

	public void setAutoAutoGrade(boolean newVal) {
		overviewProjectStepper.setAutoAutoGrade(newVal);
	}

	public void autoAutoGrade() {
		overviewProjectStepper.autoAutoGrade();
	}

	public void setFrame(Object aFrame) {
		overviewProjectStepper.setFrame(aFrame);
	}

	@Visible(false) 
	public Object getFrame() {
		return overviewProjectStepper.getFrame();
	}

	@Visible(false) 
	public LabelBeanModel getPhoto() {
		return overviewProjectStepper.getPhoto();
	}

	@Visible(false) 
	public String getFeedback() {
		return overviewProjectStepper.getFeedback();
	}
	@Row(5)
	@Explanation("Shows the console transcript associated with selected feature or all transcript if no selected feature.")
	public String getTranscript() {
		return overviewProjectStepper.getTranscript();
	}

	@Visible(false) 
	public NavigationSetter getNavigationSetter() {
		return overviewProjectStepper.getNavigationSetter();
	}

	public void validate() {
		overviewProjectStepper.validate();
	}

	public boolean runProjectsInteractively(String aGoToOnyen)
			throws MissingOnyenException, InvalidOnyenRangeException {
		return overviewProjectStepper.runProjectsInteractively(aGoToOnyen);
	}

	public void setName(String newVal) {
		overviewProjectStepper.setName(newVal);
	}

	@Visible(false) 
	public String getName() {
		return overviewProjectStepper.getName();
	}

	public void resetNoFilteredRecords() {
		overviewProjectStepper.resetNoFilteredRecords();
	}

	@Visible(false) 
	public String getOnyen() {
		return overviewProjectStepper.getOnyen();
	}

	public void setOverallNotes(String newVal) {
		overviewProjectStepper.setOverallNotes(newVal);
	}

	@Visible(false) 
	public double getMultiplier() {
		return overviewProjectStepper.getMultiplier();
	}

	@Visible(true) 
	@PreferredWidgetClass(JTextArea.class)
	@ComponentWidth(700)
	@ComponentHeight(50)
	@Row(1)
	@Explanation ("Summary, feature independent notes. Press return to see color change immediately. Otherwise the notes will be processed when you press some button.")
	public String getOverallNotes() {
		return overviewProjectStepper.getOverallNotes();
	}

	public void internalSetOnyen(String anOnyen) throws MissingOnyenException {
		overviewProjectStepper.internalSetOnyen(anOnyen);
	}

	public void setMultiplier(double newValue) {
		overviewProjectStepper.setMultiplier(newValue);
	}

	@Visible(false)
	public boolean isProceedWhenDone() {
		return overviewProjectStepper.isProceedWhenDone();
	}

	public void toggleProceedWhenDone() {
		overviewProjectStepper.toggleProceedWhenDone();
	}

	public void internalSetMultiplier(double newValue) {
		overviewProjectStepper.internalSetMultiplier(newValue);
	}

	@Visible(false) 
	public String getSequenceNumber() {
		return overviewProjectStepper.getSequenceNumber();
	}

	public void computeNextColors() {
		overviewProjectStepper.computeNextColors();
	}

	public void setComputedFeedback() {
		overviewProjectStepper.setComputedFeedback();
	}

	public void setStoredFeedback() {
		overviewProjectStepper.setStoredFeedback();
	}

	public void setStoredOutput() {
		overviewProjectStepper.setStoredOutput();
	}

	@Visible(false) 
	public GradingFeature getSelectedGradingFeature() {
		return overviewProjectStepper.getSelectedGradingFeature();
	}

	public void internalSetManualNotes(String newVal) {
		overviewProjectStepper.internalSetManualNotes(newVal);
	}

	public void internalSetResult(String newVal) {
		overviewProjectStepper.internalSetResult(newVal);
	}

	public void internalSetAutoNotes(String newVal) {
		overviewProjectStepper.internalSetAutoNotes(newVal);
	}

	public void internalSetOverallNotes(String newVal) {
		overviewProjectStepper.internalSetOverallNotes(newVal);
	}

	public void setColors() {
		overviewProjectStepper.setColors();
	}

	@Visible(false)
	public boolean isChanged() {
		return overviewProjectStepper.isChanged();
	}

	public void setChanged(boolean changed) {
		overviewProjectStepper.setChanged(changed);
	}

	public void setComputedScore() {
		overviewProjectStepper.setComputedScore();
	}

	@Visible(false)
	public boolean isSettingUpProject() {
		return overviewProjectStepper.isSettingUpProject();
	}

	public void setSettingUpProject(boolean settingUpProject) {
		overviewProjectStepper.setSettingUpProject(settingUpProject);
	}

	public boolean shouldVisit() {
		return overviewProjectStepper.shouldVisit();
	}

	public void internalSetScore(double newVal) {
		overviewProjectStepper.internalSetScore(newVal);
	}

	public void setMultiplierColor() {
		overviewProjectStepper.setMultiplierColor();
	}

	public void setScoreColor() {
		overviewProjectStepper.setScoreColor();
	}

	public void setOverallNotesColor() {
		overviewProjectStepper.setOverallNotesColor();
	}

	public boolean runAttempted() {
		return overviewProjectStepper.runAttempted();
	}

	@Visible(false) 
	public int getCurrentOnyenIndex() {
		return overviewProjectStepper.getCurrentOnyenIndex();
	}

	public void setCurrentOnyenIndex(int currentOnyenIndex) {
		overviewProjectStepper.setCurrentOnyenIndex(currentOnyenIndex);
	}

	@Visible(false) 
	public int getFilteredOnyenIndex() {
		return overviewProjectStepper.getFilteredOnyenIndex();
	}

	public void setFilteredOnyenIndex(int filteredOnyenIndex) {
		overviewProjectStepper.setFilteredOnyenIndex(filteredOnyenIndex);
	}
	@Row(3)
	@PreferredWidgetClass(JTextArea.class)
	@ComponentHeight(35)
	@Override
	@Explanation("Shows automatically generated explanation for point deduction for selected feature")
	public String getAutoNotes() {
		return overviewProjectStepper.getAutoNotes();
	}
	@Row(4)
	@PreferredWidgetClass(JTextArea.class)
	@ComponentHeight(35)
	@Override
	@Explanation("Shows manually entered explanation for selected feature")

	public String getManualNotes() {
		// TODO Auto-generated method stub
		return overviewProjectStepper.getManualNotes();
	}
	
//	public boolean preSetManualNotes() {
//		return getSelectedGradingFeature() != null;
//		
//	}

	@Override
	public void setManualNotes(String newVal) {
		overviewProjectStepper.setManualNotes(newVal);
		
	}

	@Override
	public boolean preGetGradingFeatures() {
		return overviewProjectStepper.preGetGradingFeatures();
	}
	@Override
	@Visible(false)
	public String getSource() {
		return overviewProjectStepper.getSource();
	}
	@Override
	@Visible(false)
	public String getProblemHistory() {
		return overviewProjectStepper.getProblemHistory();
	}
	@Override
	public void internalSetSource(String newValue) {
		
		overviewProjectStepper.internalSetSource(newValue);
	}
	@Override
	@Visible(true)
	public void run() {
		overviewProjectStepper.run();
	}
	@Visible(false)
	@Override
	public boolean isPlayMode() {
		return overviewProjectStepper.isPlayMode();
	}
	@Override
	public void setPlayMode(boolean playMode) {
		overviewProjectStepper.setPlayMode(playMode);
		
	}
	@Override
	public void togglePlayPause() {
		overviewProjectStepper.togglePlayPause();
		
	}
	@Override
	public void setSource(String newVal) {
		overviewProjectStepper.setSource(newVal);
		
	}
	@Override
	public boolean preTogglePlayPause() {
		return overviewProjectStepper.preTogglePlayPause();
	}
	@Override
	public void save() {
		overviewProjectStepper.save();		
	}
	@Override
	public boolean preSetManualNotes() {
		// TODO Auto-generated method stub
		return overviewProjectStepper.preSetManualNotes();
	}
	@Override
	public void resetFeatureSpreadsheet() {
		overviewProjectStepper.resetFeatureSpreadsheet();
		
	}
	@Override
	public boolean preRestoreFeatureSpreadsheet() {		
		return overviewProjectStepper.preRestoreFeatureSpreadsheet();
	}
	@Override
	public void restoreFeatureSpreadsheet() {
		 overviewProjectStepper.restoreFeatureSpreadsheet();
		
	}
	@Override
	public void quit() {
		overviewProjectStepper.quit();
		
	}
	@Override
	public void setProceedWhenDone(boolean proceedWhenDone) {
		overviewProjectStepper.setProceedWhenDone(proceedWhenDone);
		
	}
	@Override
	@Visible(false)

	public boolean isExitOnQuit() {
		// TODO Auto-generated method stub
		return overviewProjectStepper.isExitOnQuit();
	}
	@Override
	public void setExitOnQuit(boolean newVal) {
		overviewProjectStepper.setExitOnQuit(newVal);
	}
	@Override
	public void cleanAllFeedbackFolders() {
		overviewProjectStepper.cleanAllFeedbackFolders();
		
	}
	@Override
	public void cleanFeedbackFolder() {
		overviewProjectStepper.cleanFeedbackFolder();		
	}
	@Visible(false)
	public String getTASourceCodeComments() {
		return overviewProjectStepper.getTASourceCodeComments();
	}
	public void sync() {
		overviewProjectStepper.sync();
		
	}
	@Override
	@Visible(false)
	public double getSourcePoints() {
		return overviewProjectStepper.getSourcePoints();
	}
	@Override
	public void setSourcePoints(double newValue) {
		overviewProjectStepper.setSourcePoints(newValue);
		
	}
	
	@Override
	public void internalSetSourcePoints(double newValue) {
		overviewProjectStepper.internalSetSourcePoints(newValue);
		
	}
	@Override
	public void loadSourceFromFile() {
		overviewProjectStepper.loadSourceFromFile();
		
	}
	@Override
	@Visible(false)
	public String getDisplayedOnyen() {
		// TODO Auto-generated method stub
		return overviewProjectStepper.getDisplayedOnyen();
	}
	@Override
	@Visible(false)
	public String getDisplayedName() {
		// TODO Auto-generated method stub
		return overviewProjectStepper.getDisplayedName();
	}
	@Override
	@Visible(false)
	public String getStudentHistory() {
		// TODO Auto-generated method stub
		return overviewProjectStepper.getStudentHistory();
	}
	@Override
	@Visible(false)
	public void setResultDiff(String newValue) {
		overviewProjectStepper.setResultDiff(newValue);
		
	}
	@Override
	@Visible(false)
	public String getResultDiff() {
		return overviewProjectStepper.getResultDiff();
	}
	@Override
	@Visible(false)
	public String getSourceChecks() {
		return overviewProjectStepper.getSourceChecks();
	}
	@Override
	@Visible(false)
	public String getWaitingThreads() {
		// TODO Auto-generated method stub
		return overviewProjectStepper.getWaitingThreads();
	}
	
	@Override
	@Visible(false)
	public void nextDocument() {
		 overviewProjectStepper.nextDocument();
	}
	public static void main(String[] args) {
		ObjectEditor.edit(new AMainProjectStepper());
	}
	@Override
	public boolean preNextDocument() {
		// TODO Auto-generated method stub
		return overviewProjectStepper.preNextDocument();
	}
	@Override
	public boolean preFirstDocument() {
		// TODO Auto-generated method stub
		return overviewProjectStepper.preFirstDocument();
	}
	@Override
	public void firstDocument() {
		 overviewProjectStepper.firstDocument();
		
	}
	@Override
	public boolean preTerminate() {
		return overviewProjectStepper.preTerminate();
	}
	@Override
	public void terminate() {
		 overviewProjectStepper.terminate();
	}
	@Override
	public boolean preRun() {
		return overviewProjectStepper.preRun();
	}
	@Override
	public DynamicEnum runArgs() {
		// TODO Auto-generated method stub
		return overviewProjectStepper.runArgs();
	}
	@Override
	@Visible(false)

	public GradedProjectOverview getGradedProjectOverview() {
		// TODO Auto-generated method stub
		return overviewProjectStepper.getGradedProjectOverview();
	}
	@Override
	@Visible(false)

	public GradedProjectTextOverview getTextOverview() {
		return overviewProjectStepper.getTextOverview();
	}
	@Override
	public void setOnyenIndex(int onyenIndex) {
		overviewProjectStepper.setOnyenIndex(onyenIndex);
		
	}
	@Override
	public void previousFiltered() {
		overviewProjectStepper.previousFiltered();
		
	}
	@Override
	public void nextFiltered() {
		overviewProjectStepper.nextFiltered();
	}
	
//	@Override
//	public void newFocus(String aProperty) {
//		overviewProjectStepper.newFocus(aProperty);		
//	}

}

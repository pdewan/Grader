package grader.steppers;

import grader.feedback.ScoreFeedback;
import grader.interaction_logger.manual_grading_stats.GradingHistoryManagerSelector;
import grader.navigation.NavigationKind;
import grader.navigation.filter.BasicNavigationFilter;
import grader.sakai.project.ASakaiProjectDatabase;
import grader.sakai.project.ProjectStepper;
import grader.sakai.project.SakaiProject;
import grader.sakai.project.SakaiProjectDatabase;
import grader.settings.GraderSettingsModel;
import grader.spreadsheet.FeatureGradeRecorder;
import grader.trace.settings.MissingOnyenException;
import grader.trace.steppers.HasMoreStepsChanged;
import grader.trace.steppers.NavigationListConfigured;
import grader.trace.steppers.ProceedWhenDoneChanged;
import grader.trace.steppers.ProjectStepAborted;
import grader.trace.steppers.ProjectStepEnded;
import grader.trace.steppers.ProjectStepStarted;
import grader.trace.steppers.ProjectStepperEnded;
import grader.trace.steppers.UserNextStep;
import grader.trace.steppers.UserPreviousStep;
import grader.trace.steppers.UserQuit;
import grader.trace.steppers.UserWindowClose;
import gradingTools.Driver;

import java.awt.Frame;
import java.awt.GraphicsEnvironment;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.IOException;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import util.annotations.Column;
import util.annotations.ComponentHeight;
import util.annotations.ComponentWidth;
import util.annotations.Explanation;
import util.annotations.Label;
import util.annotations.Row;
import util.annotations.StructurePattern;
import util.annotations.StructurePatternNames;
import util.annotations.Visible;
import util.misc.Common;
import util.trace.Tracer;
import bus.uigen.OEFrame;
import bus.uigen.ObjectEditor;
import bus.uigen.uiFrame;
import bus.uigen.attributes.AttributeNames;

@StructurePattern(StructurePatternNames.BEAN_PATTERN)
public class AGradedProjectNavigator /*extends AClearanceManager*/ implements
		GradedProjectNavigator, WindowListener {
	boolean playMode;
	boolean exitOnQuit = true;
	boolean knowLastFilteredItem = false;
	

	OverviewProjectStepper projectStepper;
	PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(
			this);

	SakaiProjectDatabase projectDatabase;
	String sequenceNumber = "0/0";

//	// ideally this stuff should really be done through property change as
//	// Josh's wrapper does
	FeatureGradeRecorder featureGradeRecorder;
	String onyen = "";

	SakaiProject project;
//	framework.project.Project wrappedProject;
//	// FinalGradeRecorder gradeRecorder;
	boolean hasMoreSteps = true;
//	FinalGradeRecorder totalScoreRecorder;
	boolean manualOnyen;
	String logFile, gradedFile, skippedFile;
	boolean sourceHasBeenOpened;
	public static final boolean doNotVisitNullProjects = false; // we will make this a property if we ever want to set it to true


	public AGradedProjectNavigator() {

	}
	public void setProjectDatabase(SakaiProjectDatabase aProjectDatabase) {

		projectDatabase = aProjectDatabase;
		// ouch, cast should change once we get rid of old stepper
		projectStepper = (OverviewProjectStepper) projectDatabase.getProjectStepper();
		// gradeRecorder = aProjectDatabase.getGradeRecorder();
//		
		GraderSettingsModel graderSettings = aProjectDatabase.getGraderSettings();
		featureGradeRecorder = aProjectDatabase.getFeatureGradeRecorder();
//		totalScoreRecorder = aProjectDatabase.getTotalScoreRecorder();
//		registerWithGradingFeatures();
		logFile = aProjectDatabase.getAssignmentDataFolder().getLogFileName();
		gradedFile = aProjectDatabase.getAssignmentDataFolder()
				.getGradedIdFileName();
		skippedFile = aProjectDatabase.getAssignmentDataFolder()
				.getSkippedIdFileName();
//		ObjectEditor.setMethodAttribute(AGradedProjectNavigator.class, "togglePlayPause", AttributeNames.LABEL, computeTogglePlayPauseLabel());

		// configuteNavigationList does this
//		setCurrentOnyenIndex(0);

//		ings.getNavigationSetter().getNavigationFilterSetter().addPropertyChangeListener(this);
//		getNavigationSetter().getNavigationFilterSetter().addPropertyChangeListener(this);
		
		// recordWindows(); // the first project does not wait so we need to
		// record here

	}
	// we intercept window closes
	public static void addWindowListener(Object aFrame, WindowListener aListener) {
		if (aFrame instanceof Frame) {
			((Frame) aFrame).addWindowListener(aListener);
		} else if (aFrame instanceof JFrame) {
			((JFrame) aFrame).addWindowListener(aListener);
		} else if (aFrame instanceof uiFrame) {
			
			((OEFrame) aFrame).addWindowListener(aListener);
			((OEFrame) aFrame).setAutoExitEnabled(false);
		}
		
	}
	Object frame;
	@Override
	public void setFrame(Object aFrame) {
		frame = aFrame;
		
		addWindowListener(aFrame, this);
	}



	@Override
	public void internalSetOnyen(String anOnyen) throws MissingOnyenException {
		// project = projectDatabase.getProject(anOnyen);
		String oldOnyen = onyen;
		int onyenIndex = onyens.indexOf(anOnyen);
		if (onyenIndex < 0) {
			Tracer.error("Student:" + anOnyen + " does not exist in specified onyen range");
			throw new MissingOnyenException(anOnyen, this);
//			return;
		}
//		currentOnyenIndex = onyenIndex;
		setCurrentOnyenIndex( onyenIndex);
		maybeSaveState();
		redirectProject();
		boolean retVal = projectStepper.setProject(anOnyen);
		if (!retVal) {
			onyen = oldOnyen;
			propertyChangeSupport.firePropertyChange("onyen", null, onyen);
			return;
		}
		// again this will void a getter call when properties are redisplayed
		propertyChangeSupport.firePropertyChange(oldOnyen, null, onyen);



	}



	void notifyPreconditionChanged() {
		propertyChangeSupport.firePropertyChange("this", null, this);

	}



	@Visible(false)
	public SakaiProject getProject() {
		return project;
	}

	public static void writeScores(ProjectStepper aProjectStepper) {
		ScoreFeedback scoreFeedback = aProjectStepper.getProjectDatabase().getScoreFeedback();
		if (scoreFeedback != null)
		    scoreFeedback
				.writeScores(aProjectStepper);
		

	}

	


	
	@Visible(false)
	public boolean setProject(SakaiProject newVal) {
		sourceHasBeenOpened = false;
		project = newVal;

		return true;
	}
	@Override
	public boolean shouldVisit() {
		if (manualOnyen )
			return true;
		GraderSettingsModel graderSettingsModel = projectDatabase.getGraderSettings();
		if (graderSettingsModel == null) return true;
		if (graderSettingsModel.getNavigationSetter().getNavigationKind() != NavigationKind.MANUAL)
			return true;
		BasicNavigationFilter navigationFilter = projectDatabase.getNavigationFilter();
		return navigationFilter.includeProject(projectStepper, projectDatabase);
	}
	


	boolean settingUpProject;
	

	public boolean preSkip() {
		return !preProceed();
	}


	
	boolean noNextFilteredRecords;
	
	boolean noNextFilteredRecords () {
		return knowLastFilteredItem && currentOnyenIndex >= lastMatchedIndex;
	}
	boolean noPreviousFilteredRecords() {
		return firstMatchedIndex != -1 && currentOnyenIndex == firstMatchedIndex;
	}

	@Override
	public boolean preNext() {
		return /*!noNextFilteredRecords /*&& preProceed()* && */
				!noNextFilteredRecords() &&
				currentOnyenIndex < onyens.size() - 1;
		// this does not make sense, next is a stronger condition than next

		// return !preDone() && nextOnyenIndex < onyens.size() - 1 ;
	}
	@Row(0)
	@Column(0)
	@ComponentWidth(100)
	@Override
	@Explanation("Go to next student after saving current student changes.")
	public synchronized void next() {
//		if (!preProceed()) {
//			JOptionPane.showMessageDialog(null, "Cannot proceed as assignment not completely graded. Turn off the proceed when finished box if you do not want this check.");
//			return ;
//		}

//		try {
//			// Common.appendText(logFile, getOnyen() + " Skipped " +
//			// Common.currentTimeAsDate() + "\n");
//			Common.appendText(skippedFile, projectStepper.getOnyen() + "\n");
//			File file = new File(logFile);
//			if (file.exists())
//				file.createNewFile();
//			List<String> list = FileProxyUtils.toList(file);
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		UserNextStep.newCase(projectDatabase, projectStepper, project, this);
		 userMove(true);
		// should put person in skipped list

	}
	boolean noPreviousFilteredRecords;

	@Override
	public boolean prePrevious() {
		return !noPreviousFilteredRecords() && /*preProceed() &&*/ currentOnyenIndex > 0;
//				&& currentOnyenIndex > firstMatchedIndex;
	}
	@Row(0)
	@Column(1)
	@ComponentWidth(100)
	@Override
	@Explanation("Go to previous student after saving current student changes.")
	public synchronized void previous() {
		
		
		// should have a user move

		// try {
		// Common.appendText(logFile, onyen + " Skipped " +
		// Common.currentTimeAsDate() + "\n");
		// Common.appendText(skippedFile, onyen + "\n");
		// List<String> list = FileProxyUtils.toList(new File(logFile));
		// } catch (IOException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		UserPreviousStep.newCase(projectDatabase, projectStepper, project, this);

		 userMove(false);
		// should put person in skipped list

	}
	String computeTogglePlayPauseLabel() {
		return playMode?"Pause":"Play";	
	}
	@Override
	@Visible(false)
	public boolean isPlayMode() {
		return playMode;
	}
	@Override
	public void setPlayMode(boolean newValue) {
		if (newValue == playMode) {
                    return;
                }
		if (!preTogglePlayPause()) {
                    return;
                }
		this.playMode = newValue;
		ObjectEditor.setMethodAttribute(AGradedProjectNavigator.class, "togglePlayPause", AttributeNames.LABEL, computeTogglePlayPauseLabel());

		
	}
	@Override
	public boolean preTogglePlayPause() {
		return projectDatabase.getGraderSettings().getNavigationSetter().getNavigationKind() != NavigationKind.MANUAL;
	}
	@Row(0)
	@Column(2)
	@ComponentWidth(100)
	@Label("Pause") // initially play mode will be true for manual
	@Visible(false)
	@Override
	public void togglePlayPause() {
		setPlayMode(!playMode);
//		playMode = !playMode;	
//		ObjectEditor.setMethodAttribute(AGradedProjectNavigator.class, "togglePlayPause", AttributeNames.LABEL, computeTogglePlayPauseLabel());
		
	}
	

	
	boolean proceedWhenDone = true;
//	@Visible(false)
	@Row(3)
	@Column(0)
	@Override
	@Label("Stop If Not Done")
	@Explanation("Determines if next or previous command is allowed when current student is not completely graded")
	public boolean isProceedWhenDone() {
		return proceedWhenDone;
		
	}
	@Override
	public void toggleProceedWhenDone() {
		proceedWhenDone = !proceedWhenDone;
	}
	@Override
	public void setProceedWhenDone(boolean newVal) {
		boolean oldVal = proceedWhenDone;
		this.proceedWhenDone = newVal;
		propertyChangeSupport.firePropertyChange("ProceedWhenDone", oldVal, newVal);
		ProceedWhenDoneChanged.newCase(projectDatabase, projectStepper, project, newVal, this);
	}

	public boolean preProceed() {
		// if manuelOnyen then we will go to next step
		// cannot proceed if it is not all graded
		// why check for manualOnyen?
		// removing first clause
		return //(hasMoreSteps || manualOnyen) && 
				projectStepper.isAllGraded() || !proceedWhenDone || projectDatabase.getGraderSettings().getNavigationSetter().getNavigationKind() != NavigationKind.MANUAL;
	}
	@Override
	@Visible(false)
	public boolean hasMoreSteps() {
		return hasMoreSteps;
	}

	@Override
	@Visible(false)
	public void setHasMoreSteps(boolean newVal) {
		if (hasMoreSteps == newVal)
			return;
		hasMoreSteps = newVal;
		if (hasMoreSteps == false)
			writeScores(projectStepper);

		notifyPreconditionChanged();
		HasMoreStepsChanged.newCase(projectDatabase, projectStepper, project, newVal, this);

	}

	

//	@Override
//	public synchronized void waitForClearance() {
//
//		super.waitForClearance();
//		
//
//	}

	@Override
	public void addPropertyChangeListener(PropertyChangeListener aListener) {
		propertyChangeSupport.addPropertyChangeListener(aListener);
	}
	@Override
	public void resetNoFilteredRecords() {
		noNextFilteredRecords = false;
		noPreviousFilteredRecords = false;
	}




	@Override
	@Visible(false)
	public SakaiProjectDatabase getProjectDatabase() {
		// TODO Auto-generated method stub
		return projectDatabase;
	}

	List<String> onyens;
	int currentOnyenIndex = -1; // negative so when it is set to zero a proeprty change is fired for navigation status
	int filteredOnyenIndex = 0;
//	String nextOnyen;
//
	@Override
	@Visible(false)
	public void configureNavigationList() {
		onyens = projectDatabase.getOnyenNavigationList();
		NavigationListConfigured.newCase(projectDatabase, projectStepper, project, onyens, this);
		setCurrentOnyenIndex( 0);
//		hasMoreSteps = true;
		setHasMoreSteps(true);
		

		
	}

	

	@Override
	public boolean preDone() {
		// return preProceed();
		// we are done but do not have another step
		return preProceed() && !preNext();
	}
	void maybeSaveState() {
		if (projectStepper.getProject().isNoProjectFolder())
			return;
		projectDatabase.restoreGraderDirectory();
//		projectDatabase.resetIO();
//		projectDatabase.clearWindows();
		// josh's code
				// no serialization otherwise
				if (projectStepper.isChanged() || 
						!featureGradeRecorder.logSaved()) {
					if (sourceHasBeenOpened)
					sync(); // get ta comments from any un synced source files
				featureGradeRecorder.finish();
				projectStepper.setChanged(false);
				}

				// my original code
				// should this not be at the start of this method
				projectDatabase.resetIO();
				projectDatabase.clearWindows();
				ProjectStepEnded.newCase(projectDatabase, projectStepper, project, this);
	}
	void redirectProject() {
		projectDatabase.initIO();
		projectDatabase.recordWindows();
	}
	
	void setFailedMoveFlags(boolean forward) {
		if (forward)
			noNextFilteredRecords = true;
		else
			noPreviousFilteredRecords = true;
	}
	
	void setSuccessfulMoveFlags(boolean forward) {
		if (forward)
			noNextFilteredRecords = false;
		else
			noPreviousFilteredRecords = false;
	}
	
	boolean checkLeave() {
		if (!preProceed() && !projectStepper.isSettingUpProject()) {
			if (!GraphicsEnvironment.isHeadless() && !Driver.isHeadless())
			JOptionPane.showMessageDialog(null, "Cannot proceed as assignment not completely graded. Turn off the Stop-If-Not-Done checkbox if you do not want this check.");
			return false;
		}
		return true;
	}
	
	void userMove(boolean forward) {
//		if (!preProceed() && !projectStepper.isSettingUpProject()) {
//			JOptionPane.showMessageDialog(null, "Cannot proceed as assignment not completely graded. Turn off the Stop-If-Not-Done checkbox if you do not want this check.");
//			return ;
//		}
		if (!checkLeave()) return;
		if (move(forward)) {
		setSuccessfulUserMove(currentOnyenIndex);
		}
	}
	int firstMatchedIndex = -1;
	int lastMatchedIndex = -1;
	
	protected void processReachedEnd() {
		hasMoreSteps = false;
		knowLastFilteredItem = true;
		if (firstMatchedIndex == -1) { // have matched nothing
//			if (GraphicsEnvironment.isHeadless() || Driver.isHeadless()) {
//				JOptionPane.showMessageDialog(null, "No entries matchin filter, exiting");
//			}
			Driver.maybeShowMessage("No entries matchin filter, exiting");
//			move(false); // go back one
			notifyPreconditionChanged();
		} else {
			Driver.maybeShowMessage("Reached the end of items matching filter");
		}
	}
	@Override
	@Visible(false)
	public synchronized boolean move(boolean forward) {
		
		maybeSaveState();
//		// josh's code
//		// no serialization otherwise
//		if (changed || 
//				!featureGradeRecorder.logSaved()) 
//		featureGradeRecorder.finish();
//
//		// my original code
//		projectDatabase.resetIO();
//		projectDatabase.clearWindows();
		if (forward) {
//			currentOnyenIndex++;
			setCurrentOnyenIndex(currentOnyenIndex+1);

			if (currentOnyenIndex >= onyens.size()) {
//				hasMoreSteps = false;
//				knowLastFilteredItem = true;
//				if (firstMatchedIndex == -1) { // have matched nothing
//					if (GraphicsEnvironment.isHeadless() || Driver.isHeadless()) {
//						JOptionPane.showMessageDialog(null, "No entries matchin filter, exiting");
//					}
//					move(false); // go back one
//					notifyPreconditionChanged();
//				}
				processReachedEnd();
				
//				lastMatchedIndex = currentOnyenIndex - 1;
//				JOptionPane.showMessageDialog(null, "No more entries matchin filter, exiting");
//				System.exit(0);
//				return false;
			}
		} else {
//			currentOnyenIndex--;
			setCurrentOnyenIndex(currentOnyenIndex-1);
			
			if (currentOnyenIndex < 0 || currentOnyenIndex < firstMatchedIndex) {
				hasMoreSteps = false;
				return false;
			}

		}
		
		redirectProject();
		String anOnyen = onyens.get(currentOnyenIndex);
		SakaiProject aProject = projectDatabase.getProject(anOnyen);
//		redirectProject();
//		projectDatabase.initIO();
//		projectDatabase.recordWindows();
		if (aProject == null && doNotVisitNullProjects) {
			ProjectStepStarted.newCase(projectDatabase, projectStepper, null, this);

			ProjectStepAborted.newCase(projectDatabase, projectStepper, null, this);
			return 	false;
		}
		boolean projectSet = projectStepper.setProject(anOnyen);
		if (!projectSet) {
			if (!hasMoreSteps || currentOnyenIndex >= onyens.size() - 1) {
				processReachedEnd();
				move(false); // go back one
				return false;
			}
			boolean retVal = move(forward);
			if (!hasMoreSteps)
				return false;
			
			if (!retVal && filteredOnyenIndex != currentOnyenIndex) {
//				currentOnyenIndex = filteredOnyenIndex;
				setCurrentOnyenIndex(filteredOnyenIndex);
				try {
					projectStepper.internalSetOnyen(onyens.get(filteredOnyenIndex));
				} catch (MissingOnyenException e) {
					e.printStackTrace(); // this should never be executed
				}
				if (projectStepper.getProject().isNoProjectFolder())
					return false;
				String message = "Cannot move as no more records that satisfy selection condition. You can change the filter settings.";
				Tracer.error(message);
				if (!GraphicsEnvironment.isHeadless())
				JOptionPane.showMessageDialog(null, message);
				setFailedMoveFlags(forward);
			} else {
				
				
//				setFilteredOnyenIndex(filteredOnyenIndex);
				setFilteredOnyenIndex(currentOnyenIndex);
//				setSuccessfulMove(currentOnyenIndex);
//				filteredOnyenIndex = currentOnyenIndex;
				setSuccessfulMoveFlags(forward);
			}
			return retVal;
		}
//		filteredOnyenIndex = currentOnyenIndex;
		setFilteredOnyenIndex(currentOnyenIndex);
//		setSuccessfulMove(currentOnyenIndex);
		return true;
			
		// these two steps should go into setProject unless there is something
		// subttle here, specially as the stepProject step below is commented
		// put
		// if (isAutoRun())
		// projectDatabase.runProject(anOnyen, aProject);
		// if (isAutoAutoGrade())
		// autoGrade();

		// setProject(anOnyen);
	}

	@Override
//	@Column(2)
	@Visible(false)
	@ComponentWidth(100)
	public synchronized void done() {

		if (manualOnyen)
			writeScores(projectStepper);
		try {
			// Common.appendText(logFile, onyen + " Skipped " +
			// Common.currentTimeAsDate() + "\n\r");
			Common.appendText(gradedFile, projectStepper.getOnyen() + "\n");

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		move(true);
	

	}
	@Override
	public int getCurrentOnyenIndex() {
		return currentOnyenIndex;
	}
	@Override
	public void setCurrentOnyenIndex(int newValue) {
		if (newValue == currentOnyenIndex) return;
		this.currentOnyenIndex = newValue;
		setSequenceNumber();
		
	}
	
	@Override
	public int getFilteredOnyenIndex() {
		return filteredOnyenIndex;
	}
	
	protected void setSuccessfulUserMove (int anIndex) {
		if (firstMatchedIndex == -1) {
			firstMatchedIndex = anIndex;
		}
		if (anIndex > lastMatchedIndex) {
			lastMatchedIndex = anIndex;	
		 }
		
	}
	@Override
	public void setFilteredOnyenIndex(int filteredOnyenIndex) {
		this.filteredOnyenIndex = filteredOnyenIndex;
//		if (firstMatchedIndex == -1) {
//			firstMatchedIndex = currentOnyenIndex;
//		}
//		if (currentOnyenIndex > lastMatchedIndex) {
//			lastMatchedIndex = currentOnyenIndex;	
//		 }
	}


	
	String computeSequenceNumber() {
		return "" + (currentOnyenIndex + 1) + "/" + onyens.size();
	}
	void setSequenceNumber() {
		String oldValue = sequenceNumber;
		String newValue = computeSequenceNumber() ;
		sequenceNumber = newValue;
		propertyChangeSupport.firePropertyChange("sequenceNumber", oldValue, newValue);
		
		
	}
	
	@Override
	@Row(3)
	@Column(1)
	@ComponentWidth(30)
	@ComponentHeight(27)
	@Label("Navigation Distance:")
	@Explanation("How far into onyen range have you travelled - the sequence number of the current record.")
	public String getSequenceNumber() {
		return sequenceNumber;
	}
	@Visible(false)
	@ComponentWidth(100)
	@Row(0)
	@Column(3)
	@Override
	public void save() {
		maybeSaveState();
//		System.exit(0);
	}
	
//	@Visible(false)
	@ComponentWidth(100)
	@Row(1)
	@Column(2)
	@Override
	@Explanation("Save uncomitted changes in the text areas wihout hitting and entering a return and sync with changes to source code made using external text editor.")
	public void sync() {		
		projectStepper.loadSourceFromFile();
		
			
	}
	@Row(1)
	@Column(0)
	@ComponentWidth(100)
	@Override
	@Explanation("View and add comments to source using the text editor registered with the OS.")
	public void openSource() {
		
		project.setHasBeenRun(true);

		project.displaySource(projectDatabase);
		sourceHasBeenOpened = true;
		projectStepper.setChanged(true);
//		projectStepper.openSource();
	}
	
	@Row(1)
	@Column(1)
	@ComponentWidth(100)
	@Explanation("To be implemented.")
	public void exploreSource() {
		projectStepper.openSource();
	}
	@ComponentWidth(100)
	@Row(2)
	@Column(0)
	@Override
	@Explanation("Navigate through the submmitted documents")
	public void nextDocument() {
		projectStepper.nextDocument();		
	}
	@ComponentWidth(100)
	@Row(2)
	@Column(1)
	@Override
	@Explanation("Go back to first document")
	public void firstDocument() {
		projectStepper.firstDocument();
		
	}
	
	void doQuit() {
		if (projectStepper.preTerminate()) {
			projectStepper.terminate();
		}
		maybeSaveState();
		ProjectStepperEnded.newCase(projectDatabase, projectStepper, this);
		GradingHistoryManagerSelector.getGradingHistoryManager().setProblemHistoryTextOfCurrentModuleProblem();
		String aggregateStats = GradingHistoryManagerSelector.getGradingHistoryManager().getAggregateProblemHistoryTextOfCurrentModuleProblem();
		GradingHistoryManagerSelector.getGradingHistoryManager().saveStudentHistories();
		System.out.println(aggregateStats);
	}
	void showAggregateStats() {
		
	}
	@ComponentWidth(100)
	@Row(0)
	@Column(2)
	@Override
	@Explanation("Quit session after saving current student changes.")
	public void quit() {
		if (!checkLeave()) return;
		doQuit();
		UserQuit.newCase(projectDatabase, projectStepper, project, this);

//		maybeSaveState();
//		UserQuit.newCase(projectDatabase, projectStepper, project, this);
//		ProjectStepperEnded.newCase(projectDatabase, projectStepper, this);
		if (exitOnQuit)

		System.exit(0);
		else
			ASakaiProjectDatabase.dispose(frame);
			
	}
	@Override
	@ComponentWidth(100)
	@Row(2)
	@Column(2)
	@Explanation("Run program after terminating previous run")
	public void run() {
		 projectStepper.run();
	}
	@Override
	public boolean preTerminate() {
		return projectStepper.preTerminate();
	}
	@Override
	public void terminate() {
		projectStepper.terminate();
	}
	
	
	public boolean preRun() {
		return projectStepper.preRun();
	}	
	
	
	
	@Override
	public void windowActivated(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void windowClosed(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void windowClosing(WindowEvent arg0) {
		if (!checkLeave()) return;
//		maybeSaveState();
//		ProjectStepperEnded.newCase(projectDatabase, projectStepper, this);
		doQuit();

		UserWindowClose.newCase(projectDatabase, projectStepper, project, this);
		System.exit(0);
		
	}
	@Override
	public void windowDeactivated(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void windowDeiconified(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void windowIconified(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void windowOpened(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	public static void main(String[] args) {
		ObjectEditor.edit(new AGradedProjectNavigator());
	}
	@Override
	@Visible(false)
	public boolean isExitOnQuit() {
		// TODO Auto-generated method stub
		return exitOnQuit;
	}
	@Override
	public void setExitOnQuit(boolean newVal) {
		exitOnQuit = newVal;
		
	}
	@Override
	public boolean preNextDocument() {
		// TODO Auto-generated method stub
		return projectStepper.preNextDocument();
	}
	@Override
	public boolean preFirstDocument() {
		return projectStepper.preFirstDocument();
	}
	
	
}

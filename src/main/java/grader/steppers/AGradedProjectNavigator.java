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
import java.util.Collection;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

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
	protected boolean hasSeeked = false;
	

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
		internalSetCurrentOnyenIndex( onyenIndex);
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
	/**
	 * Thisis different from setCurrentOnyenIndex, meant tomove items
	 */
	@Override
	public void setOnyenIndex(int onyenIndex) throws MissingOnyenException {
		// project = projectDatabase.getProject(anOnyen);
		if (onyenIndex >= onyens.size()) {
		
			Tracer.error("Onyen index too large in setOnyenIndex ");
		return;
//			throw new MissingOnyenException(anOnyen, this);
//			return;
		}
		hasSeeked = true;
		doSetOnyenIndex(onyenIndex);
//		String anOnyen = onyens.get(onyenIndex);
////		currentOnyenIndex = onyenIndex;ring oldOnyen = onyen;
//		String oldOnyen = onyen;
//		setCurrentOnyenIndex( onyenIndex);
//		maybeSaveState();
//		redirectProject();
//		boolean retVal = projectStepper.setProject(anOnyen);
//		if (!retVal) {
//			onyen = oldOnyen;
//			propertyChangeSupport.firePropertyChange("onyen", null, onyen);
//			return;
//		}
//		// again this will void a getter call when properties are redisplayed
//		propertyChangeSupport.firePropertyChange(oldOnyen, null, onyen);
	}

	protected void doSetOnyenIndex (int onyenIndex) {
		String anOnyen = onyens.get(onyenIndex);
//		currentOnyenIndex = onyenIndex;ring oldOnyen = onyen;
		String oldOnyen = onyen;
		internalSetCurrentOnyenIndex( onyenIndex);
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
		if (manualOnyen ) // what is this?
			return true;
		
		GraderSettingsModel graderSettingsModel = projectDatabase.getGraderSettings();
		if (graderSettingsModel == null) return true;
		if (graderSettingsModel.getNavigationSetter().getNavigationKind() != NavigationKind.MANUAL)
			return true;
		BasicNavigationFilter navigationFilter = projectDatabase.getNavigationFilter();
		return navigationFilter.includeProject(projectStepper, projectDatabase);
	}
	protected boolean matchesFilter() {
		BasicNavigationFilter navigationFilter = projectDatabase.getNavigationFilter();

		return navigationFilter.includeProject(projectStepper, projectDatabase);

	}
	 

	boolean settingUpProject;
	

	public boolean preSkip() {
		return !preProceed();
	}


	
	boolean noNextFilteredRecords;
	
	boolean hasNextFilteredRecords () {
//		return !knowLastFilteredItem || 
//				currentOnyenIndex < lastMatchedIndex;
		return lastTreeSetIndex != -1 && currentOnyenIndex < lastTreeSetIndex;
	}
	boolean hasPreviousFilteredRecords() {
//		return firstMatchedIndex != -1 && currentOnyenIndex > firstMatchedIndex;
		return firstTeeeSetIndex != -1 && currentOnyenIndex > firstTeeeSetIndex;

	}

	@Override
	public boolean preNext() {
		return /*!noNextFilteredRecords /*&& preProceed()* && */
//				hasNextFilteredRecords() &&
				currentOnyenIndex < onyens.size() - 1;
		// this does not make sense, next is a stronger condition than next

		// return !preDone() && nextOnyenIndex < onyens.size() - 1 ;
	}
	public boolean preNextFiltered() {
		return !noEntriesMatchedFilter &&
				
				hasNextFilteredRecords() &&
				preNext();
	
	}
	@Row(0)
	@Column(2)
	@ComponentWidth(100)
	@Override
	@Explanation("Go to next student after saving current student changes.")
	@Label(">")
	public synchronized void next() {

		UserNextStep.newCase(projectDatabase, projectStepper, project, this);
		 userMove(true, false);
		// should put person in skipped list

	}
	@Row(0)
	@Column(0)
	@ComponentWidth(100)
	@Explanation("Go to next student after saving current student changes.")
	@Label(">>")
	public synchronized void nextFiltered() {

		UserNextStep.newCase(projectDatabase, projectStepper, project, this);
		 userMove(true, true);
		// should put person in skipped list

	}
	boolean noPreviousFilteredRecords;
	

	@Override
	public boolean prePrevious() {
		return 
//				(hasSeeked || hasPreviousFilteredRecords()) &&  
				
			currentOnyenIndex > 0;
//				
	}
	public boolean prePreviousFiltered() {
		return !noEntriesMatchedFilter &&
				(hasSeeked || hasPreviousFilteredRecords()) &&  
				
			prePrevious();
//				
	}
	@Row(0)
	@Column(3)
	@ComponentWidth(100)
	@Override
	@Explanation("Go to previous student after saving current student changes.")
	@Label("<")
	public synchronized void previous() {		
		UserPreviousStep.newCase(projectDatabase, projectStepper, project, this);

		 userMove(false, false);
		// should put person in skipped list

	}
	@Override
	@Row(0)
	@Column(1)
	@ComponentWidth(100)
	@Explanation("Go to previous student after saving current student changes.")
	@Label("<<")
	public synchronized void previousFiltered() {		
		UserPreviousStep.newCase(projectDatabase, projectStepper, project, this);

		 userMove(false, true);
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
		if (!hasMoreSteps)
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
		internalSetCurrentOnyenIndex( 0);
//		hasMoreSteps = true;
		setHasMoreSteps(true);
		firstMatchedIndex = -1;
		lastMatchedIndex = -1;
		

		
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
				double aManuallyGraded = projectStepper.isAllGraded()?FeatureGradeRecorder.TRUE_VALUE:FeatureGradeRecorder.DEFAULT_VALUE;
				
				featureGradeRecorder.setFullyGraded(projectStepper.getName(), projectStepper.getOnyen(), aManuallyGraded);
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
	
	void userMove(boolean forward, boolean isFiltered) {

		if (!checkLeave()) 
			return;
		move(forward, isFiltered);

	}
	int firstMatchedIndex = -1;
//	int firstMatchedIndex = Integer.MAX_VALUE;

	int lastMatchedIndex = -1;
	TreeSet<Integer> knownFilteredItems = new TreeSet();
	// These should replace matched indices above
	int firstTeeeSetIndex = -1;
	int lastTreeSetIndex = -1;
	
	protected boolean noEntriesMatchedFilter = false;
//	protected void processReachedEnd() {
//		hasMoreSteps = false;
//		knowLastFilteredItem = true;
//		if (firstMatchedIndex == -1 || firstMatchedIndex == onyens.size()) { // have matched nothing
////			if (GraphicsEnvironment.isHeadless() || Driver.isHeadless()) {
////				JOptionPane.showMessageDialog(null, "No entries matchin filter, exiting");
////			}
////			Driver.maybeShowMessage("No entries matching filter, exiting");
//			Driver.maybeShowMessage("No entries matching filter, skipping to last record");
//			noEntriesMatchedFilter = true;
//			hasMoreSteps = true;
//			
////			quit();
////			notifyPreconditionChanged();
//		} else {
//			Driver.maybeShowMessage("Reached the end of items matching filter");
//		}
//	}
	protected void processReachedEndOfFilteredItems() {
		Driver.maybeShowMessage("Reached the end of filtered items, not moving position");
	}
	protected void processReachedEnd() {
		Driver.maybeShowMessage("Reached the end of items, not moving position");

//		hasMoreSteps = false;
//		knowLastFilteredItem = true;
//		if (firstMatchedIndex == -1 || firstMatchedIndex == onyens.size()) { // have matched nothing
////			if (GraphicsEnvironment.isHeadless() || Driver.isHeadless()) {
////				JOptionPane.showMessageDialog(null, "No entries matchin filter, exiting");
////			}
////			Driver.maybeShowMessage("No entries matching filter, exiting");
//			Driver.maybeShowMessage("No entries matching filter, skipping to last record");
//			noEntriesMatchedFilter = true;
//			hasMoreSteps = true;
//			
////			quit();
////			notifyPreconditionChanged();
//		} else {
//			Driver.maybeShowMessage("Reached the end of items matching filter");
//		}
	}
	
//	@Visible(false)
//	public synchronized boolean oldMove(boolean forward, boolean isFiltered) {
//		hasSeeked = false;
//		maybeSaveState();
//
//		if (forward) {
//			internalSetCurrentOnyenIndex(currentOnyenIndex+1);
//
//			if (currentOnyenIndex >= onyens.size()) {
//
//				processReachedEnd();
//				if (noEntriesMatchedFilter) {
//					setOnyenIndex(0);
//					return false;
//				}
//				
//
//			}
//		} else {
//			internalSetCurrentOnyenIndex(currentOnyenIndex-1);
//			
//			if (currentOnyenIndex < 0 || currentOnyenIndex < firstMatchedIndex) {
//				hasMoreSteps = false;
//				return false;
//			}
//
//		}
//		
//		redirectProject();
//		String anOnyen = onyens.get(currentOnyenIndex);
//		SakaiProject aProject = projectDatabase.getOrCreateProject(anOnyen);
//
//		if (aProject == null && doNotVisitNullProjects) {
//			ProjectStepStarted.newCase(projectDatabase, projectStepper, null, this);
//
//			ProjectStepAborted.newCase(projectDatabase, projectStepper, null, this);
//			return 	false;
//		}
//		boolean projectSet = projectStepper.setProject(anOnyen);
////		if (!projectSet) {
//		if (!projectSet && isFiltered) {
//			if (!hasMoreSteps || currentOnyenIndex >= onyens.size() - 1) {
//				processReachedEnd();
//				// we are allowing non filter items
//				if (noEntriesMatchedFilter) {
//					setOnyenIndex(0);					
//					return false;
//				} else {
//					setOnyenIndex(onyens.size() - 1);
//					return false;
//				}
//				// removing this as we are allowing non filter records
////				move(false); // go back one
////				return false;
//			}
//			boolean retVal = move(forward, isFiltered);
//			if (!hasMoreSteps)
//				return false;
//			
//			if (!retVal && filteredOnyenIndex != currentOnyenIndex) {
//				internalSetCurrentOnyenIndex(filteredOnyenIndex);
//				try {
//					projectStepper.internalSetOnyen(onyens.get(filteredOnyenIndex));
//				} catch (MissingOnyenException e) {
//					e.printStackTrace(); // this should never be executed
//				}
//				if (projectStepper.getProject().isNoProjectFolder())
//					return false;
//				String message = "Cannot move as no more records that satisfy selection condition. You can change the filter settings.";
//				Tracer.error(message);
//				if (!GraphicsEnvironment.isHeadless())
//				JOptionPane.showMessageDialog(null, message);
//				setFailedMoveFlags(forward);
//			} else {
//				
//				
//				setFilteredOnyenIndex(currentOnyenIndex);
//
//				setSuccessfulMoveFlags(forward);
//			}
//			return retVal;
//		}
//		if (isFiltered) {
//			setFilteredOnyenIndex(currentOnyenIndex);
//		}
//		return true;
//			
//		
//	}
	
	protected int findNextFiltered() {
		for (int anIndex:knownFilteredItems) {
			if (anIndex > currentOnyenIndex)
				return anIndex;
		}
		return currentOnyenIndex;
	}
	protected int findPreviousFiltered() {
		for (int anIndex:knownFilteredItems) {
			if (anIndex < currentOnyenIndex)
				return anIndex;
		}
		return currentOnyenIndex;
	}
	@Override
	public synchronized boolean move(boolean forward, boolean isFiltered) {
		hasSeeked = false;
		maybeSaveState();
		int aNewIndex = currentOnyenIndex;
		if (isFiltered) {
			 aNewIndex = forward?findNextFiltered():findPreviousFiltered();
			if (aNewIndex == currentOnyenIndex) {
				processReachedEndOfFilteredItems();
				redirectProject();
				return false;
			}
		} else {
			 aNewIndex = forward?currentOnyenIndex + 1:currentOnyenIndex - 1;
			 if (aNewIndex < 0 || aNewIndex >= onyens.size()) {
				 processReachedEnd();
					redirectProject();

				 return false;
			 }
		}
		currentOnyenIndex = aNewIndex;
//
//		if (forward) {
//			if (currentOnyenIndex >= onyens.size() - 1) {
//
//				processReachedEnd();
//				return false;
////				if (noEntriesMatchedFilter) {
////					setOnyenIndex(0);
////					return false;
////				}
//				
//
//			}
//			internalSetCurrentOnyenIndex(currentOnyenIndex+1);
////
////			if (currentOnyenIndex >= onyens.size()) {
////
////				processReachedEnd();
////				return false;
//////				if (noEntriesMatchedFilter) {
//////					setOnyenIndex(0);
//////					return false;
//////				}
////				
////
////			}
//		} else {
//			
//			if (currentOnyenIndex <= 0 ) {
//				processReachedEnd();
//				return false;
//			}
//			internalSetCurrentOnyenIndex(currentOnyenIndex-1);
//
//		}
//		if (isFiltered) {
//			int aNewIndex = forward?findNextFiltered():findPreviousFiltered();
//			if (aNewIndex == currentOnyenIndex) {
//				processReachedEndOfFilteredItems();
//				return false;
//			}
//			currentOnyenIndex = aNewIndex;
//		}
		internalSetCurrentOnyenIndex(aNewIndex);
		redirectProject();
		String anOnyen = onyens.get(currentOnyenIndex);
		SakaiProject aProject = projectDatabase.getOrCreateProject(anOnyen);

		if (aProject == null && doNotVisitNullProjects) {
			ProjectStepStarted.newCase(projectDatabase, projectStepper, null, this);

			ProjectStepAborted.newCase(projectDatabase, projectStepper, null, this);
			return 	false;
		}
		boolean projectSet = projectStepper.setProject(anOnyen);
		return projectSet;
////		if (!projectSet) {
//		if (!projectSet && isFiltered) {
//			if (!hasMoreSteps || currentOnyenIndex >= onyens.size() - 1) {
//				processReachedEnd();
//				// we are allowing non filter items
//				if (noEntriesMatchedFilter) {
//					setOnyenIndex(0);					
//					return false;
//				} else {
//					setOnyenIndex(onyens.size() - 1);
//					return false;
//				}
//				// removing this as we are allowing non filter records
////				move(false); // go back one
////				return false;
//			}
//			boolean retVal = move(forward, isFiltered);
//			if (!hasMoreSteps)
//				return false;
//			
//			if (!retVal && filteredOnyenIndex != currentOnyenIndex) {
//				internalSetCurrentOnyenIndex(filteredOnyenIndex);
//				try {
//					projectStepper.internalSetOnyen(onyens.get(filteredOnyenIndex));
//				} catch (MissingOnyenException e) {
//					e.printStackTrace(); // this should never be executed
//				}
//				if (projectStepper.getProject().isNoProjectFolder())
//					return false;
//				String message = "Cannot move as no more records that satisfy selection condition. You can change the filter settings.";
//				Tracer.error(message);
//				if (!GraphicsEnvironment.isHeadless())
//				JOptionPane.showMessageDialog(null, message);
//				setFailedMoveFlags(forward);
//			} else {
//				
//				
//				setFilteredOnyenIndex(currentOnyenIndex);
//
//				setSuccessfulMoveFlags(forward);
//			}
//			return retVal;
//		}
//		if (isFiltered) {
//			setFilteredOnyenIndex(currentOnyenIndex);
//		}
//		return true;
			
		
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
		move(true, true);
	

	}
	@Override
	public int getCurrentOnyenIndex() {
		return currentOnyenIndex;
	}
	/**
	 * Called when one goes to a specific onyen from the settings model
	 * Actually called for setting the first project to be visited
	 */
	@Override
	public void setCurrentOnyenIndex(int newValue) {
		hasSeeked = true;
		internalSetCurrentOnyenIndex(newValue);
//		if (newValue == currentOnyenIndex) return;
//		this.currentOnyenIndex = newValue;
//		setSequenceNumber();
		
	}
	protected void refreshFilteredSet() {
		boolean aMatchesFilter = matchesFilter();
		if (aMatchesFilter) {
			knownFilteredItems.add(currentOnyenIndex);
		} else {
			knownFilteredItems.remove(currentOnyenIndex);
		}
		if (knownFilteredItems.size() == 0) {
			firstTeeeSetIndex = -1;
			lastTreeSetIndex = -1;
		} else {
			firstTeeeSetIndex = knownFilteredItems.first();
			lastTreeSetIndex = knownFilteredItems.last();
		}
	}
	public void internalSetCurrentOnyenIndex(int newValue) {
		if (newValue == currentOnyenIndex) return;
		if (currentOnyenIndex != - 1) {
		refreshFilteredSet();// check the current item, probbaly will be removed from filter
		}
		this.currentOnyenIndex = newValue;
		setSequenceNumber();
		refreshFilteredSet(); // now check the new item
		
	}
	
	@Override
	public int getFilteredOnyenIndex() {
		return filteredOnyenIndex;
	}
	
//	protected void setSuccessfulFilteredMove (int anIndex) {
//		if (firstMatchedIndex == -1) {
//			firstMatchedIndex = anIndex;
//		}
//		if (anIndex > lastMatchedIndex) {
//			lastMatchedIndex = anIndex;	
//		 }
//		
//	}
	
	protected void setSuccessfulFilteredMove (int anIndex) {
		
		if (firstMatchedIndex == -1) {
			firstMatchedIndex = anIndex;
		}
		if (anIndex > lastMatchedIndex) {
			lastMatchedIndex = anIndex;	
		 }
		
	}
	// this seems to be called externally only for the first onyen in onyen list
	public void oldSetFilteredOnyenIndex(int filteredOnyenIndex) {
		if (noEntriesMatchedFilter)
			return;
		this.filteredOnyenIndex = filteredOnyenIndex;
		
//		if (firstMatchedIndex == -1) {
//			firstMatchedIndex = currentOnyenIndex;
//		}
//		if (currentOnyenIndex > lastMatchedIndex) {
//			lastMatchedIndex = currentOnyenIndex;	
//		 }
		setSuccessfulFilteredMove(filteredOnyenIndex);
	}
	
	// this seems to be called externally only for the first onyen in onyen list
	@Override
	public void setFilteredOnyenIndex(int filteredOnyenIndex) {
		if (noEntriesMatchedFilter)
			return;
		this.filteredOnyenIndex = filteredOnyenIndex;
		
//		if (firstMatchedIndex == -1) {
//			firstMatchedIndex = currentOnyenIndex;
//		}
//		if (currentOnyenIndex > lastMatchedIndex) {
//			lastMatchedIndex = currentOnyenIndex;	
//		 }
		setSuccessfulFilteredMove(filteredOnyenIndex);
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
//	@ComponentWidth(100)
	@ComponentWidth(150)

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
//	@ComponentWidth(100)
	@ComponentWidth(150)
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
//	@Row(0)
//	@Column(2)
	@Row(2)
	@Column(3)
	@Override
	@Explanation("Quit session after saving current student changes.")
	public void quit() {
		if (!checkLeave()) return;
		doQuit();
		UserQuit.newCase(projectDatabase, projectStepper, project, this);

//		maybeSaveState();
//		UserQuit.newCase(projectDatabase, projectStepper, project, this);
//		ProjectStepperEnded.newCase(projectDatabase, projectStepper, this);
		if (exitOnQuit) {
			Driver.clear();

		System.exit(0);
		} else {
			ASakaiProjectDatabase.dispose(frame);
		}
		
			
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
		Driver.clear();
		
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

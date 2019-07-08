package grader.sakai.project;

import grader.assignment.GradingFeature;
import grader.assignment.GradingFeatureList;
import grader.settings.navigation.NavigationSetter;
import grader.trace.settings.InvalidOnyenRangeException;
import grader.trace.settings.MissingOnyenException;

import java.beans.PropertyChangeListener;

import util.misc.ClearanceManager;
import util.models.LabelBeanModel;
import util.models.PropertyListenerRegisterer;


public interface ProjectStepper  extends ClearanceManager, PropertyListenerRegisterer, PropertyChangeListener{
	public boolean setProject(SakaiProject newVal) ;
	
	public void output();
	
	public void openSource() ;
	public double getScore() ;
	public void setScore(double newVal) ;
	public  boolean waitForClearance() ;
	public SakaiProjectDatabase getProjectDatabase() ;

	public void setProjectDatabase(SakaiProjectDatabase aProjectDatabase) ;
	public void setOnyen(String anOnyen) throws MissingOnyenException ;
	public boolean setProject(String anOnyen) ;
	public boolean isAutoRun() ;
    public void setAutoRun(boolean newVal);
    public void autoRun() ;
    public boolean hasMoreSteps();
	
	public void setHasMoreSteps(boolean newVal);
    public SakaiProject getProject();
    boolean runProjectsInteractively() throws InvalidOnyenRangeException;
    public void configureNavigationList();

	boolean preDone();

	void done();

//	String getNavigationFilter();
//
//	void setNavigationFilter(String newVal);

	boolean preGetGradingFeatures();

	boolean preAutoGrade();

	void autoGrade();

	GradingFeatureList getGradingFeatures();

	boolean isAllGraded();

	boolean preNext();

	void next();

	boolean prePrevious();

	void previous();

	boolean preRunProjectsInteractively();

	boolean move(boolean forward, boolean isFiltered);
	public boolean isAutoAutoGrade() ;
    public void setAutoAutoGrade(boolean newVal) ;
    public void autoAutoGrade() ;

	void setFrame(Object aFrame);

	Object getFrame();

	LabelBeanModel getPhoto();

	String getFeedback();

	String getTranscript();

	NavigationSetter getNavigationSetter();

	void validate();

	boolean runProjectsInteractively(String aGoToOnyen) throws MissingOnyenException, InvalidOnyenRangeException;

	void setName(String newVal);

	String getName();

	String getOnyen();

	void setOverallNotes(String newVal);

	String getOverallNotes();

	void internalSetOnyen(String anOnyen) throws MissingOnyenException;

	boolean isProceedWhenDone();

	void toggleProceedWhenDone();

	void internalSetMultiplier(double newValue);

	void setComputedFeedback();

	void setStoredFeedback();

	void setStoredOutput();

	GradingFeature getSelectedGradingFeature();

	void internalSetManualNotes(String newVal);

	void internalSetResult(String newVal);

	void internalSetAutoNotes(String newVal);

	void internalSetOverallNotes(String newVal);

	void setColors();

	boolean isChanged();

	void setChanged(boolean changed);

	void setComputedScore();

	boolean isSettingUpProject();

	void setSettingUpProject(boolean settingUpProject);

	boolean shouldVisit();

	void internalSetScore(double newVal);

	void setMultiplierColor();

	void setScoreColor();

	void setOverallNotesColor();

	boolean runAttempted();

	int getCurrentOnyenIndex();

	void setCurrentOnyenIndex(int currentOnyenIndex);

	int getFilteredOnyenIndex();

	void setFilteredOnyenIndex(int filteredOnyenIndex);

	String getAutoNotes();

	String getManualNotes();

	void setManualNotes(String newVal);
	
	void save();

	void run();
	boolean isPlayMode();

	void setPlayMode(boolean playMode);

	void togglePlayPause();
	public void quit();
	public boolean isExitOnQuit();
	public void setExitOnQuit(boolean newVal);
	

}

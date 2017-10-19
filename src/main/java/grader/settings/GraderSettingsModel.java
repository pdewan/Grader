package grader.settings;

import grader.modules.ModuleProblemSelector;
import grader.settings.folders.GraderFilesSetterModel;
import grader.settings.folders.OnyenRangeModel;
import grader.settings.navigation.NavigationSetter;

import java.beans.PropertyChangeListener;

import util.models.PropertyListenerRegisterer;

public interface GraderSettingsModel extends PropertyListenerRegisterer, PropertyChangeListener{
	public GraderFilesSetterModel getFileBrowsing() ;
	public void setFileBrowsing(GraderFilesSetterModel fileBrowsing);
	public OnyenRangeModel getOnyens() ;
	public void setOnyens(OnyenRangeModel onyens) ;
        public void preSettings();
        public void postSettings();
	public void begin() ;
	public void awaitBegin() ;
	NavigationSetter getNavigationSetter();
	void setNavigationSetter(NavigationSetter navigationSetter);
	boolean isGraderStarted();
	void setGraderStarted(boolean graderStarted);
	String getCurrentProblem();
	ModuleProblemSelector getModuleProblemSelector();
	void setModuleProblemSelector(ModuleProblemSelector moduleProblemSelector);
	void cleanAllSubmissionFolders();
	void resetFeatureSpreadsheet();
	void cleanSlateAll();
	String getCurrentModule();
	boolean isSettingsLoaded();
	void init();
	public boolean isPrivacyMode() ;
	public void setPrivacyMode(boolean newValue) ;
	public void togglePrivacyMode() ;
	boolean getCompileMode();
	boolean maybePreCompile();
	void cleanSlate(String anOnyen);
	boolean maybePreUnzip();
	void compileExecutor();
	void cleanSlateSpecified();
	void setGraceDays(int newVal);
	int getGraceDays();
	void unzipSelectedOnyens();

}

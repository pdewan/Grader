package grader.settings;

import framework.utils.GraderSettings;
import grader.basics.settings.BasicGradingEnvironment;
import grader.config.ConfigurationManagerSelector;
import grader.modules.ModuleProblemManager;
import grader.modules.ModuleProblemManagerSelector;
import grader.navigation.NavigationKind;

import java.util.List;
import java.util.Map;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

import util.misc.Common;
import util.trace.Tracer;

public class AGraderSettingsManager implements GraderSettingsManager {

    public static final String EDITOR = "editor";
    public static final String DIFF = "diff";
    public static final String MODULE = "currentModule";
    public static final String PROBLEM_PATH = "path";
    public static final String PROBLEM_NAME = "problem";
    public static final String START_ONYEN = "start";
    public static final String END_ONYEN = "end";
    public static final String WORD_PATH = "Word.path";
    public static final String C_COMPILER_PATH = "CCompiler.path";
    public static final String PYTHON_INTERPRETER_PATH = "PythonInterpreter.path";

    PropertiesConfiguration dynamicConfiguration
            = ConfigurationManagerSelector.getConfigurationManager().getDynamicConfiguration();
    ModuleProblemManager moduleProblemManager = ModuleProblemManagerSelector.getModuleProblemManager();

    public AGraderSettingsManager() {
        maybeConvertToDynamicConfiguration();
    }
    @Override
    public void init() {
    	dynamicConfiguration = ConfigurationManagerSelector.getConfigurationManager().getDynamicConfiguration();
    	moduleProblemManager = ModuleProblemManagerSelector.getModuleProblemManager();
    	maybeConvertToDynamicConfiguration();
    }
//	   @Override
//	   public void init (ModuleProblemManager initValue) {
//		   moduleProblemManager = initValue;
//	   }

    void maybeConvertToDynamicConfiguration() {
        Map<String, String> settings = GraderSettings.get().getSettings();
//	    	PropertiesConfiguration dynamicConfiguration = GradingEnvironment.get().getConfigurationManager().getDynamicConfiguration();
        if (dynamicConfiguration == null || !dynamicConfiguration.isEmpty()) {
            return;
        }
        for (String key : settings.keySet()) {
            dynamicConfiguration.setProperty(key, settings.get(key));
        }
        try {
            dynamicConfiguration.save();
        } catch (ConfigurationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    @Override
    public String getStartingOnyen(String aModule) {
        String startingOnyen = dynamicConfiguration.getString(aModule + "." + START_ONYEN,
                dynamicConfiguration.getString(START_ONYEN));
        return startingOnyen;
    }

    @Override
    public void setStartingOnyen(String aModule,
            String aStartOnyen) {
        dynamicConfiguration.setProperty(aModule + "." + START_ONYEN, aStartOnyen);
        dynamicConfiguration.setProperty(START_ONYEN, aStartOnyen);

    }

    @Override
    public String getEndingOnyen(String aModule) {
        String endingOnyen = dynamicConfiguration.getString(aModule + "." + END_ONYEN,
                dynamicConfiguration.getString(END_ONYEN));
        return endingOnyen;
    }

    @Override
    public void setEndingOnyen(String aModule,
            String anEndOnyen) {
        dynamicConfiguration.setProperty(aModule + "." + END_ONYEN, anEndOnyen);
        dynamicConfiguration.setProperty(END_ONYEN, anEndOnyen);

    }
    public static final String NAVIGATION_KIND = "navigationKind";

    @Override
    public NavigationKind getNavigationKind(String aModule) {
        String aString = dynamicConfiguration.getString(aModule + "." + NAVIGATION_KIND,
                dynamicConfiguration.getString(NAVIGATION_KIND));
        if (aString == null) {
            return null;
        }
        return (NavigationKind) Common.fromString(NavigationKind.class, aString);

    }

    @Override
    public void setNavigationKind(String aModule,
            NavigationKind aNavigationKind) {
        dynamicConfiguration.setProperty(aModule + "." + NAVIGATION_KIND, aNavigationKind.toString());
        dynamicConfiguration.setProperty(NAVIGATION_KIND, aNavigationKind.toString());

    }

    public static final String NAVIGATION_FILTER = "navigationFilter";
//	@Override
//	public NavigationFilter getNavigationFilter(String aModule) {
//		String aString = dynamicConfiguration.getString(aModule + "." + NAVIGATION_FILTER,     	
//        		
//        		dynamicConfiguration.getString(NAVIGATION_FILTER));
//		return (NavigationFilter) Common.fromString(NavigationFilter.class, aString);
//	
//	}
//	@Override
//	public void setNavigationFilter(String aModule,
//			NavigationFilter aNavigationFilter) {
//		dynamicConfiguration.setProperty(aModule + "." + NAVIGATION_FILTER, aNavigationFilter.toString());
//		dynamicConfiguration.setProperty(NAVIGATION_FILTER, aNavigationFilter.toString()); 
//		
//	}

    @Override
    public String getNavigationFilter(String aModule) {
        String aString = dynamicConfiguration.getString(aModule + "." + NAVIGATION_FILTER,
                dynamicConfiguration.getString(NAVIGATION_FILTER));
        return aString;

    }

    @Override
    public void setNavigationFilter(String aModule,
            String aNavigationFilter) {
        dynamicConfiguration.setProperty(aModule + "." + NAVIGATION_FILTER, aNavigationFilter);
        dynamicConfiguration.setProperty(NAVIGATION_FILTER, aNavigationFilter);

    }

    public static final String FILTER_OPTION = "filterOption";

//	@Override
//	public void setNavigationFilterOption(String aModule,
//			NavigationFilter aNavigationFilter, Object anOption) {
//		dynamicConfiguration.setProperty(aModule + "." + aNavigationFilter + "." + FILTER_OPTION, anOption.toString());
//		dynamicConfiguration.setProperty(aNavigationFilter + "." + FILTER_OPTION, anOption.toString()); 
//		
//	}
//	
//	@Override
//	public Object getNavigationFilterOption(String aModule, NavigationFilter aNavigationFilter) {
//		String aString = dynamicConfiguration.getString(aModule + "." + aNavigationFilter + "." + FILTER_OPTION,     	
//        		
//        		dynamicConfiguration.getString(aNavigationFilter + "." + FILTER_OPTION));
//		return Common.fromString(aNavigationFilter.getClass(), aString);
//	
//	}
    @Override
    public String getNavigationFilterOption(String aModule, String aNavigationFilter) {
        String aString = dynamicConfiguration.getString(aModule + "." + aNavigationFilter + "." + FILTER_OPTION,
                dynamicConfiguration.getString(aNavigationFilter + "." + FILTER_OPTION));
//		return Common.fromString(aNavigationFilter.getClass(), aString);
        return aString;

    }

    @Override
    public void setNavigationFilterOption(String aModule,
            String aNavigationFilter, Object anOption) {
        dynamicConfiguration.setProperty(aModule + "." + aNavigationFilter + "." + FILTER_OPTION, anOption.toString());
        dynamicConfiguration.setProperty(aNavigationFilter + "." + FILTER_OPTION, anOption.toString());

    }

    public static final String ANIMATE_GRADES = "animateGrades";

    @Override
    public Boolean getAnimateGrades(String aModule) {
        return dynamicConfiguration.getBoolean(aModule + "." + ANIMATE_GRADES,
                dynamicConfiguration.getBoolean(ANIMATE_GRADES, false));
    }

    @Override
    public void setAnimateGrades(String aModule, boolean newVal) {
        dynamicConfiguration.setProperty(aModule + "." + ANIMATE_GRADES, newVal);
        dynamicConfiguration.setProperty(ANIMATE_GRADES, newVal);
    }

    public static final String ANIMATION_PAUSE_TIME = "animatePauseTime";

    @Override
    public Integer getAnimationPauseTime(String aModule) {
        return dynamicConfiguration.getInteger(aModule + "." + ANIMATION_PAUSE_TIME,
                dynamicConfiguration.getInteger(ANIMATION_PAUSE_TIME, 2));
    }

    @Override
    public void setAnimationPauseTime(String aModule, Integer newVal) {
        dynamicConfiguration.setProperty(aModule + "." + ANIMATION_PAUSE_TIME, newVal);
        dynamicConfiguration.setProperty(ANIMATION_PAUSE_TIME, newVal);
    }

    @Override
    public String getEditor() {
        String editor = dynamicConfiguration.getString(EDITOR);
        if (editor != null) {
//				editor = GraderSettings.get().get("editor");
            BasicGradingEnvironment.get().setEditor(editor); // why not for path also, perhaps its not used later?
        } else {
            editor = BasicGradingEnvironment.get().getEditor();
        }
        return editor;
    }

    @Override
    public void setEditor(String newValue) {
        dynamicConfiguration.setProperty(EDITOR, newValue);

    }
    
    @Override
    public String getDiff() {
        String diff = dynamicConfiguration.getString(DIFF);
        if (diff != null) {
//				editor = GraderSettings.get().get("editor");
            BasicGradingEnvironment.get().setDiff(diff); // why not for path also, perhaps its not used later?
        } else {
        	diff = BasicGradingEnvironment.get().getDiff();
        }
        return diff;
    }

    @Override
    public void setDiff(String newValue) {
        dynamicConfiguration.setProperty(DIFF, newValue);

    }

    @Override
    public String getModule() {
        List<String> modules = moduleProblemManager.getModules();
        if (dynamicConfiguration == null) {
        	System.err.println(" Null dynamic configuration");
        	return null;
        }
        if (modules.size() == 0) {
        	System.err.println(" No modules specified");
        	return "";
        }

        String aModule = dynamicConfiguration.getString(MODULE, modules.get(0));
//        System.out.println ("Returning module:" + aModule);
        return aModule;
    }

    @Override
    public void setModule(String newValue) {
        dynamicConfiguration.setProperty(MODULE, newValue);

    }

    @Override
    public String getDownloadPath(String aModule) {
        String problemDownloadPath = dynamicConfiguration.getString(aModule + "." + PROBLEM_PATH,
//                dynamicConfiguration.getString(PROBLEM_PATH, "Browse to a valid download path"));
        dynamicConfiguration.getString(PROBLEM_PATH, ""));

        return problemDownloadPath;
    }

    @Override
    public void setDownloadPath(String aModule, String aNewValue) {
        dynamicConfiguration.setProperty(aModule + "." + PROBLEM_PATH, aNewValue);

    }

    @Override
    public String getWordPath() {
        String wordPath = dynamicConfiguration.getString(WORD_PATH);
        if (wordPath == null || wordPath.isEmpty()) {
            wordPath = ConfigurationManagerSelector.getConfigurationManager().getStaticConfiguration().getString(WORD_PATH, "C:/Program Files/Microsoft Office/Office" + 14 + "/WINWORD");
        }
        return wordPath;
    }

    @Override
    public void setWordPath(String aNewValue) {
        dynamicConfiguration.setProperty(WORD_PATH, aNewValue);

    }

    @Override
    public String getCCompilerPath() {
    	String defaultPath = ConfigurationManagerSelector.
    			getConfigurationManager().
    				getStaticConfiguration().getString(C_COMPILER_PATH, "cl");
    	if (dynamicConfiguration == null) {
    		return defaultPath;
    	}
        String path = dynamicConfiguration.getString(C_COMPILER_PATH);
        if (path == null) {
        	return defaultPath;
//            path = ConfigurationManagerSelector.getConfigurationManager().getStaticConfiguration().getString(C_COMPILER_PATH, "C:/Program Files (x86)/Microsoft Visual Studio 11.0/VC/bin/cl.exe");
//            path = ConfigurationManagerSelector.getConfigurationManager().getStaticConfiguration().getString(C_COMPILER_PATH, "cl");

        }
        return path;
    }

    @Override
    public void setCCompilerPath(String aNewValue) {
        dynamicConfiguration.setProperty(C_COMPILER_PATH, aNewValue);

    }
    
    @Override
    public String getPythonInterpreterPath() {
        String path = dynamicConfiguration.getString(PYTHON_INTERPRETER_PATH);
        if (path == null) {
//            path = ConfigurationManagerSelector.getConfigurationManager().getStaticConfiguration().getString(C_COMPILER_PATH, "C:/Program Files (x86)/Microsoft Visual Studio 11.0/VC/bin/cl.exe");
            path = ConfigurationManagerSelector.getConfigurationManager().getStaticConfiguration().getString(PYTHON_INTERPRETER_PATH, "python");

        }
        return path;
    }

    @Override
    public void setPythonInterpreterPath(String aNewValue) {
        dynamicConfiguration.setProperty(PYTHON_INTERPRETER_PATH, aNewValue);

    }

    @Override
    public String getProblem(String aModule) {
        if (dynamicConfiguration == null
                || dynamicConfiguration.getString(aModule + "." + PROBLEM_NAME,
                        dynamicConfiguration.getString(PROBLEM_NAME)) == null) {
            return "";
        }
        String problemDownloadPath = dynamicConfiguration.getString(aModule + "." + PROBLEM_NAME,
                dynamicConfiguration.getString(PROBLEM_NAME));
        if (problemDownloadPath == null) {
            return "";
        }
        return problemDownloadPath;
    }

    @Override
    public void setProblem(String aModule, String aNewValue) {
//		dynamicConfiguration.setProperty(aModule + "." + PROBLEM_NAME, aNewValue.replaceAll("\\s+", ""));		
        dynamicConfiguration.setProperty(aModule + "." + PROBLEM_NAME, aNewValue);

    }

    @Override
    public String getNormalizedProblem(String aModule) {
        return getProblem(aModule).replaceAll("\\s+", "");
    }

    @Override
    public String replaceModuleProblemVars(String original) {
        String moduleName = getModule();
        String problemName = getNormalizedProblem(moduleName);
        Tracer.info (this, "Problem name:" + problemName);
        String retVal = original;
//		String problemName = dynamicConfiguration.getString(AGraderSettingsModel.MODULE + "." + AGraderSettingsModel.MODULE);
        retVal = retVal.replace("{moduleName}", moduleName);
        retVal = retVal.replace("{ModuleName}", moduleName);
        retVal = retVal.replace("{modulename}", moduleName.toLowerCase());

        retVal = retVal.replace("{problemName}", problemName);
        retVal = retVal.replace("{ProblemName}", problemName);
        retVal = retVal.replace("{problemname}", problemName.toLowerCase());
        return retVal;

    }

    @Override
    public void save() {
        try {
            dynamicConfiguration.save();
        } catch (ConfigurationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

}
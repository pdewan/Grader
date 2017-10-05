package grader.interaction_logger;

import grader.basics.settings.BasicGradingEnvironment;
import grader.basics.trace.CSVSerializable;
import grader.config.ConfigurationManagerSelector;
import grader.interaction_logger.manual_grading_stats.GradingHistoryManagerSelector;
import grader.trace.interaction_logger.InteractionLogEntryAdded;
import grader.trace.interaction_logger.InteractionLogFileCreatedOrLoaded;
import grader.trace.interaction_logger.InteractionLogFolderCreated;
import grader.trace.settings.GraderSettingsEnded;
import grader.trace.settings.NavigationStarted;
import grader.trace.steppers.ProjectStepEnded;
import grader.trace.steppers.ProjectStepStarted;
import grader.trace.steppers.ProjectStepperEnded;
import grader.trace.steppers.ProjectStepperStarted;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import util.misc.Common;

public class AnInteractionLogWriter implements InteractionLogWriter {

    File curentLogFile;
    Logger logger;
    public static final String LOG_FILE_PREFIX = "interactionLog";
    String fileName;
    String currentAssignment;
    String currentModule;
    PrintWriter out = null;
    BufferedWriter bufWriter;
    PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);
    List<InteractionLogListener> listeners = new ArrayList();
    final Class[] staticDoNotLogEventsArray = {
        InteractionLogEntryAdded.class,
        InteractionLogFileCreatedOrLoaded.class,
        InteractionLogFolderCreated.class
    };
    Set<Class> doNotLogEventsSet = Common.arrayToSet(staticDoNotLogEventsArray);

//    String interactionLogFolder;
    String interactionLogFolder;

    GraderSettingsEnded lastGraderSettingsEnded;

    public static final String SEPARATOR = "_";

    boolean stepperEnded = false;
    List<String> buffer = new ArrayList();

    public AnInteractionLogWriter() {
        stepperEnded = false;

        String suffix = getTimeStampSuffix();

        interactionLogFolder = getOrCreateInteractionFolder();
        if (interactionLogFolder == null) {
            return;
        }
        String userName = BasicGradingEnvironment.get().getUserName();

        if (userName == null || userName.isEmpty()) {
            userName = "";
        }

        createOrLoadAppendableFile(getSettingsSuffix());

    }

    public static String getOrCreateInteractionFolder() {
        String interactionLogFolder
                = ConfigurationManagerSelector.getConfigurationManager().
                getStaticConfiguration().getString("grader.logger.interactionLogDirectory"); // + "/" + GradingEnvironment.get().getUserName();
        if (interactionLogFolder == null) {
            System.err.println("Null interaction log folder");
            return null;
        }
        File folder = new File(interactionLogFolder);
        if (folder.mkdirs()) { // true if dirs made, false otherwise
            InteractionLogFolderCreated.newCase(folder.getAbsolutePath(), AnInteractionLogWriter.class);
        }
        return interactionLogFolder;

    }

    public static String getTimeStampSuffix() {
        long currentTime = System.currentTimeMillis();
        Date currentDate = new Date(currentTime);
        String dateString = currentDate.toString();
        String[] parts = dateString.split(" ");
        String suffix = parts[1] + parts[2] + parts[5];
        return suffix;

    }

    public static final String SETTINGS_SUFFIX = "Settings";

    String getSettingsSuffix() {

        return SEPARATOR + SETTINGS_SUFFIX;

    }

    String getAssignmentProblemSuffix() {
        if (lastGraderSettingsEnded == null) {
            return "";
        }
        String aModuleName = lastGraderSettingsEnded.getGradingSettingsModel().getCurrentModule();
        String aProblemName = lastGraderSettingsEnded.getGradingSettingsModel().getCurrentProblem();
        return SEPARATOR + aModuleName + SEPARATOR + aProblemName;
    }

    public String createLogFileName(String suffix) {
        String userName = BasicGradingEnvironment.get().getUserName();

        if (userName == null || userName.isEmpty()) {
            userName = "";
        }
//			else
//				userName = userName;
        fileName = interactionLogFolder + "/" + userName + SEPARATOR + LOG_FILE_PREFIX + suffix + ".csv";
        return fileName;
    }

    @Override
    public String createModuleProblemInteractionLogName() {
        return createLogFileName(getAssignmentProblemSuffix());
    }

    void createOrLoadAppendableFile(String suffix) {
        String userName = BasicGradingEnvironment.get().getUserName();
//
//		if (userName == null || userName.isEmpty())
//			userName = "";
////		else
////			userName = userName;
//		fileName = interactionLogFolder + "/" + userName + SEPARATOR + LOG_FILE_PREFIX + suffix + ".csv";
        fileName = createLogFileName(suffix);
        out = null;

        try {
            bufWriter
                    = Files.newBufferedWriter(
                            Paths.get(fileName),
                            Charset.forName("UTF8"),
                            StandardOpenOption.WRITE,
                            StandardOpenOption.APPEND,
                            StandardOpenOption.CREATE);
            out = new PrintWriter(bufWriter, true);
        } catch (IOException e) {
            e.printStackTrace();
            //Oh, no! Failed to create PrintWriter
        }

        InteractionLogFileCreatedOrLoaded.newCase(fileName, this);

    }
    
    @Override
    public void clear() {
    	if (out != null) {
    		out.close();
    	}
    }

    public static final String NEW_LINE_REPLACEMENT = "New Line";

    boolean navigationPhase;

    @Override
    public void newEvent(Exception aTraceable) {
        if (interactionLogFolder == null) {
            return;
        }
//		if (aTraceable.getClass().getPackage() != AGraderTracer.class.getPackage()) return;
        if (!(aTraceable instanceof CSVSerializable)
                || doNotLogEventsSet.contains(aTraceable.getClass())) {
            return;
        }
        String rawCSVRow = ((CSVSerializable) aTraceable).toCSVRow();
//		String csvRow = rawCSVRow.replaceAll("\n", NEW_LINE_REPLACEMENT);
        String csvRow = rawCSVRow;

        InteractionLogEntryAdded.newCase(fileName, csvRow, this);
        propertyChangeSupport.firePropertyChange("logAddition", null, csvRow);
//		out.println(((CSVSerializable) aTraceable).toCSVRow());
        out.println(csvRow);

        out.flush();
//		if (aTraceable instanceof ProjectStepStarted) {
//			buffer.clear();
//		} else 

        if (stepperEnded) {
            if (aTraceable instanceof NavigationStarted) {
                navigationPhase = true;
            }
//			buffer.add(csvRow);
            if (aTraceable instanceof ProjectStepStarted && navigationPhase) {

                notifyListeners();
                buffer.clear();
                navigationPhase = false;
                buffer.add(csvRow);

            } else if (aTraceable instanceof ProjectStepEnded) {
                buffer.add(csvRow);

                notifyListeners();
                buffer.clear();
//			notifyListeners();
//			buffer.clear();
            } else {
                buffer.add(csvRow);
            }
        }

        if (aTraceable instanceof GraderSettingsEnded) {
            lastGraderSettingsEnded = (GraderSettingsEnded) aTraceable;
        }
        if (aTraceable instanceof ProjectStepperStarted) {
            out.close();
            stepperEnded = true;
            createOrLoadAppendableFile(getAssignmentProblemSuffix());
            GradingHistoryManagerSelector.getGradingHistoryManager().connectToCurrentHistory();

            out.println(csvRow);
        }
        if (aTraceable instanceof ProjectStepperEnded) {
            out.close();
        }

    }

    @Override
    public void addPropertyChangeListener(PropertyChangeListener aListener) {
        propertyChangeSupport.addPropertyChangeListener(aListener);

    }

    @Override
    public Set<Class> getDoNotLogEventsSet() {
        return doNotLogEventsSet;
    }

    @Override
    public void setDoNotLogEventsSet(Set<Class> doNotLogEventsSet) {
        this.doNotLogEventsSet = doNotLogEventsSet;
    }
//	public void startTracing() {
//		
//	}

    @Override
    public String getInteractionLogFolder() {
        return interactionLogFolder;
    }

    public String getCurrentLogFileName() {
        return fileName;
    }

    public void setCurrentLogFileName(String fileName) {
        this.fileName = fileName;
    }

    void notifyListeners() {
//		String[] aRow = buffer.toArray(new String[0]);

        for (InteractionLogListener listener : listeners) {
            if (navigationPhase) {
                listener.newNavigation(buffer);
            } else {
                listener.newStep(buffer);
            }
        }
    }

    @Override
    public void addLogListener(InteractionLogListener aListener) {
        listeners.add(aListener);
    }

    public static void main(String[] args) {
        new AnInteractionLogWriter();
    }

}

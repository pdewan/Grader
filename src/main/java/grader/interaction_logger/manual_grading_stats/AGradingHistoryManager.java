package grader.interaction_logger.manual_grading_stats;

import grader.interaction_logger.AnInteractionLogWriter;
import grader.interaction_logger.InteractionLogWriter;
import grader.interaction_logger.InteractionLogWriterSelector;
import grader.settings.GraderSettingsModelSelector;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import util.misc.Common;

public class AGradingHistoryManager implements GradingHistoryManager {

    public static final String PROBLEM_STATS = "problems";
    public static final String STUDENT_STATS = "students";
    public static final String SUFFIX = ".txt";

    String interactionDirectory;
    File[] interactionFiles;
    List<AllStudentsProblemHistory> problemHistory = new ArrayList();
    Map<String, AllStudentsProblemHistory> descriptionToHistory = new HashMap();
    Map<String, AllProblemsStudentHistory> onyenToAllProblemsHistory = new HashMap();

    GradingHistoryParser parser;
    GradingHistoryUnparser unparser;

    AllStudentsProblemHistory currentProblemHistory;
    InteractionLogWriter interactionLogWriter;

    /* (non-Javadoc)
     * @see grader.interaction_logger.InteractionHistoryManager#readInteractionDirectory()
     */
    public AGradingHistoryManager() {
        interactionLogWriter = InteractionLogWriterSelector.getInteractionLogWriter();
        parser = GradingHistoryParserSelector.getSavedGradingHistoryParser();
        unparser = GradingHistoryUnparserSelector.getSavedGradingHistoryUnparser();
        interactionDirectory = AnInteractionLogWriter.getOrCreateInteractionFolder();

    }

    @Override
    public String getProblemHistoryFileName(String aModule, String aProblem) {
        return interactionDirectory + "/" + PROBLEM_STATS + "/" + aModule + "/" + aProblem + SUFFIX;

    }

    public String getStudentHistoryFileName(String aModule, String anOnyen) {
//		return interactionDirectory + "/" + STUDENT_STATS + "/" + aModule + "/" + anOnyen + SUFFIX;
        return interactionDirectory + "/" + STUDENT_STATS + "/" + anOnyen + SUFFIX;

    }

    @Override
    public File getOrCreateProblemHistoryFile(String aModule, String aProblem) {
        File file = new File(getProblemHistoryFileName(aModule, aProblem));
        if (!file.exists()) {
            boolean success = file.getParentFile().mkdirs();
            try {
                file.createNewFile();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return file;
    }

    @Override
    public File getOrCreateStudentHistoryFile(String aModule, String anOnyen) {
        File file = new File(getStudentHistoryFileName(aModule, anOnyen));
        if (!file.exists()) {
            boolean success = file.getParentFile().mkdirs();
            try {
                file.createNewFile();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return file;
    }

    @Override
    public String getSavedProblemHistoryTextOfCurrentModuleProblem() {
        String aModule = GraderSettingsModelSelector.getGraderSettingsModel().getCurrentModule();
        String aProblem = GraderSettingsModelSelector.getGraderSettingsModel().getCurrentProblem();
        return getProblemHistoryText(aModule, aProblem);
    }

    @Override
    public void setProblemHistoryTextOfCurrentModuleProblem(String newVal) {
        String aModule = GraderSettingsModelSelector.getGraderSettingsModel().getCurrentModule();
        String aProblem = GraderSettingsModelSelector.getGraderSettingsModel().getCurrentProblem();
        setProblemHistoryText(aModule, aProblem, newVal);
    }

    @Override
    public void setProblemHistoryTextOfCurrentModuleProblem() {
        String log = getProblemHistoryTextOfCurrentModuleProblem();
        String aggregate = getAggregateProblemHistoryTextOfCurrentModuleProblem();
        setProblemHistoryTextOfCurrentModuleProblem(aggregate + "\n" + log);
    }

    @Override
    public String getProblemHistoryTextOfCurrentModuleProblem() {
        return unparser.unparseAllStudentsProblemGradingHistory(currentProblemHistory);

    }

    @Override
    public String getAggregateProblemHistoryTextOfCurrentModuleProblem() {
        return unparser.getAggregateStatistics(currentProblemHistory);

    }

    @Override
    public String getStudentHistoryText(String aModule, String anOnyen) {
        File file = getOrCreateStudentHistoryFile(aModule, anOnyen);
        return Common.toText(file);
    }

    @Override
    public void setStudentHistoryText(String aModule, String anOnyen, String aText) {
        File file = getOrCreateStudentHistoryFile(aModule, anOnyen);
        try {
            Common.writeText(file, aText);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void saveStudentHistoryText(AllProblemsStudentHistory aHistory) {
        String anOnyen = aHistory.getOnyen();
        String aModule = GraderSettingsModelSelector.getGraderSettingsModel().getCurrentModule();
        String text = unparser.unparseAllProblemsStudentGradingHistory(aHistory);
        setStudentHistoryText(aModule, anOnyen, text);
    }

    @Override
    public String getProblemHistoryText(String aModule, String aProblem) {
        File file = getOrCreateProblemHistoryFile(aModule, aProblem);
        return Common.toText(file);
    }

    @Override
    public void setProblemHistoryText(String aModule, String aProblem, String aText) {
        File file = getOrCreateProblemHistoryFile(aModule, aProblem);
        try {
            Common.writeText(file, aText);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    public void readInteractionDirectory() {
        interactionFiles = FileTimeSorterAndComparator.sort(new File(interactionDirectory));

    }

    @Override
    public void connectToCurrentHistory() {
        buildCurrentProblemHistory();
        InteractionLogWriterSelector.getInteractionLogWriter().addLogListener(this);
    }

    /* (non-Javadoc)
     * @see grader.interaction_logger.InteractionHistoryManager#buildHistories()
     */
    @Override
    public void buildHistories() {
        buildProblemHistories();
        buildStudentHistories();

    }
    /* (non-Javadoc)
     * @see grader.interaction_logger.InteractionHistoryManager#buildProblemHistories()
     */

    @Override
    public void buildProblemHistories() {
        for (File interactionFile : interactionFiles) {
            if (interactionFile.isDirectory()
                    || !interactionFile.getName().endsWith(".csv")
                    || interactionFile.getName().contains(AnInteractionLogWriter.SETTINGS_SUFFIX)) {
                continue;
            }
//            AllStudentsProblemHistory newVal = parser.parseAllStudentsProblemGradingHistory(interactionFile.getAbsolutePath());
            AllStudentsProblemHistory newVal;
			try {
//				newVal = parser.parseAllStudentsProblemGradingHistory(interactionFile.getCanonicalPath());
				newVal = parser.parseAllStudentsProblemGradingHistory(interactionFile);

			} catch (Exception e) {
				newVal = null;
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

            if (newVal != null) {
                String description = newVal.getModuleName() + ":" + newVal.getProblemName();
                AllStudentsProblemHistory oldProblemHistory = descriptionToHistory.get(description);
                if (oldProblemHistory != null) {
                    oldProblemHistory.merge(newVal);
                } else {
                    problemHistory.add(newVal);
                    descriptionToHistory.put(description, newVal);
                }
            }
        }
    }

    public void buildStudentHistories() {
        for (AllStudentsProblemHistory problem : problemHistory) {
            Map<String, StudentProblemHistory> nameToStudentHistory = problem.getOnyenToStudentHistory();
            Set<String> onyens = nameToStudentHistory.keySet();
            for (String onyen : onyens) {
                AllProblemsStudentHistory allProblemsHistory = onyenToAllProblemsHistory.get(onyen);
                if (allProblemsHistory == null) {
                    allProblemsHistory = new AnAllProblemsStudentHistory();
                    onyenToAllProblemsHistory.put(onyen, allProblemsHistory);
                }
                allProblemsHistory.addSavedStudentProblemGradingHistory(nameToStudentHistory.get(onyen));
            }

        }
    }

    /* (non-Javadoc)
     * @see grader.interaction_logger.InteractionHistoryManager#unparseProblemHistories()
     */
    @Override
    public void unparseProblemHistories() {
        for (AllStudentsProblemHistory history : problemHistory) {
            String unparsedValue = unparser.unparseAllStudentsProblemGradingHistory(history);
            System.out.println(unparsedValue);
        }

    }

    @Override
    public void unparseStudentHistories() {
        Set<String> students = onyenToAllProblemsHistory.keySet();
        for (String student : students) {
            AllProblemsStudentHistory history = onyenToAllProblemsHistory.get(student);
            System.out.println(unparser.unparseAllProblemsStudentGradingHistory(history));
        }
    }

    @Override
    public void saveLoadedStudentHistories() {
        Set<String> students = onyenToAllProblemsHistory.keySet();
        for (String student : students) {
            AllProblemsStudentHistory history = onyenToAllProblemsHistory.get(student);
            saveStudentHistoryText(history);
        }
    }

    @Override
    public void saveStudentHistories() {
        readInteractionDirectory();
        buildHistories();
        saveLoadedStudentHistories();
    }

    /* (non-Javadoc)
     * @see grader.interaction_logger.InteractionHistoryManager#buildCurrentProblemHistory()
     */
    @Override
    public void buildCurrentProblemHistory() {
        String fileName = interactionLogWriter.createModuleProblemInteractionLogName();
//		currentProblemHistory =  parser.parseAllStudentsProblemGradingHistory(interactionDirectory + "/" + fileName );
        currentProblemHistory = parser.parseAllStudentsProblemGradingHistory(fileName);
//		System.out.println(unparser.unparseAllStudentsProblemGradingHistory(currentProblemHistory));

    }
    /* (non-Javadoc)
     * @see grader.interaction_logger.InteractionHistoryManager#newCSVRow(java.lang.String[])
     */

    public static String[] scanCSVRow(String aCSVRow) {
        return aCSVRow.split(",");
    }

    public static List<String[]> scanCSVRow(List<String> aCSVRows) {
        List<String[]> retVal = new ArrayList<>(aCSVRows.size());
        for (String aCSVRow : aCSVRows) {
            retVal.add(scanCSVRow(aCSVRow));
        }
        return retVal;
    }

    @Override
    public void newStep(List<String> aRows) {

//		System.out.println(aRows);
//		System.out.println(scanCSVRow(aRows));
        StudentProblemHistory newVisit = parser.parseStudentHistory(scanCSVRow(aRows));
        if (newVisit != null) {
           currentProblemHistory.newStudentHistory(newVisit.getOnyen(), newVisit);
        }

//		String newState = unparser.unparseAllStudentsProblemGradingHistory(currentProblemHistory);
//		System.out.println(newState);
    }

    @Override
    public void newNavigation(List<String> aRows) {
        parser.initNewNavigation(scanCSVRow(aRows));

    }

    public static void main(String[] args) {
        GradingHistoryManager manager = new AGradingHistoryManager();
        manager.saveStudentHistories();
//		manager.readInteractionDirectory();
//		manager.buildHistories();
//		manager.unparseProblemHistories();
//		manager.unparseStudentHistories();
//		manager.getOrCreateProblemHistoryFile("comp110", "Assignment 3");
//		manager.buildCurrentProblemHistory();
    }

}

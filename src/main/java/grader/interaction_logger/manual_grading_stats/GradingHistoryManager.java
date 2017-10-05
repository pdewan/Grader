package grader.interaction_logger.manual_grading_stats;

import grader.interaction_logger.InteractionLogListener;

import java.io.File;
import java.util.List;

public interface GradingHistoryManager extends InteractionLogListener{

	public abstract void readInteractionDirectory();

	public abstract void buildHistories();

	public static final int PARTS_IN_LOG_FILE_NAME = 4;

	//	public  SavedAllStudentsProblemGradingHistory createSavedAllStudentsProblemGradingHistory(File aFile) {
	//		String fileName = aFile.getName();
	//		String[] parts = fileName.split(AnInteractionLogWriter.SEPARATOR);
	//		if (parts.length != PARTS_IN_LOG_FILE_NAME)
	//			return null;
	//		String aGraderName = parts[0];
	//		String aModuleName = parts[2];
	//		String aProblemName = parts[3];
	//		return new ASavedAllStudentsProblemGradingHistory(aGraderName, aModuleName, aProblemName);
	//		
	//		
	//	}

	public abstract void buildProblemHistories();

	public abstract void unparseProblemHistories();

	public abstract void buildCurrentProblemHistory();

	public abstract void newStep(List<String> aRow);

	public abstract void buildStudentHistories();

	void connectToCurrentHistory();

	String getProblemHistoryFileName(String aModule, String aProblem);

	File getOrCreateProblemHistoryFile(String aModule, String aProblem);

	String getProblemHistoryText(String aModule, String aProblem);

	void setProblemHistoryText(String aModule, String aProblem, String aText);

	String getSavedProblemHistoryTextOfCurrentModuleProblem();

	void setProblemHistoryTextOfCurrentModuleProblem(String newVal);

	String getProblemHistoryTextOfCurrentModuleProblem();

	void setProblemHistoryTextOfCurrentModuleProblem();

	String getAggregateProblemHistoryTextOfCurrentModuleProblem();

	void unparseStudentHistories();

	File getOrCreateStudentHistoryFile(String aModule, String anOnyen);

	String getStudentHistoryText(String aModule, String anOnyen);

	void setStudentHistoryText(String aModule, String anOnyen, String aText);

	void saveLoadedStudentHistories();

	void saveStudentHistories();

	void clear();

}
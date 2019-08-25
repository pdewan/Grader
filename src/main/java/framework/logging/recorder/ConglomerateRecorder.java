package framework.logging.recorder;

import framework.grading.ProjectRequirements;
import framework.grading.testing.CheckResult;
import framework.grading.testing.Feature;
import framework.grading.testing.Restriction;
import framework.logging.loggers.FeedbackTextSummaryLogger;
import framework.logging.loggers.Logger;
import framework.logging.serializers.SerializationUtils;
import framework.navigation.StudentFolder;
import framework.utils.GraderSettings;
import grader.assignment.GradingFeature;
import grader.assignment.GradingFeatureList;
import grader.basics.file.FileProxy;
import grader.basics.junit.TestCaseResult;
import grader.basics.util.DirectoryUtils;
import grader.feedback.AutoFeedback;
import grader.feedback.ManualFeedback;
import grader.spreadsheet.FeatureGradeRecorder;
import grader.spreadsheet.csv.AllStudentsHistoryManager;
import grader.spreadsheet.csv.AllStudentsHistoryManagerFactory;
import grader.spreadsheet.csv.AnAllStudentsHistoryManager;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;

import util.trace.Tracer;

/**
 * Created with IntelliJ IDEA.
 * User: josh
 * Date: 11/12/13
 * Time: 2:47 PM
 * To change this template use File | Settings | File Templates.
 */
public class ConglomerateRecorder implements FeatureGradeRecorder, AutoFeedback, ManualFeedback {

    // Static singleton boilerplate code
    private static final ConglomerateRecorder ourInstance = new ConglomerateRecorder();

    public static ConglomerateRecorder getInstance() {
        return ourInstance;
    }

    // Actual definition
    private ProjectRequirements projectRequirements;
    private RecordingSession recordingSession = null;
    private final List<Logger> loggers;
    private String featureComments;
    private List<TestCaseResult> featureResults;

    private ConglomerateRecorder() {
        loggers = new ArrayList<>();
    }

    public ProjectRequirements getProjectRequirements() {
        return projectRequirements;
    }

    public void setProjectRequirements(ProjectRequirements projectRequirements) {
        this.projectRequirements = projectRequirements;
    }

    public void addLogger(Logger logger) {
        loggers.add(logger);
    }

    // Recording methods
    public void save(List<CheckResult> results) {
        for (CheckResult result : results) {
            save(result);
        }
    }

    public void save(CheckResult result) {
        for (CheckResult r : recordingSession.getFeatureResults()) {
            if (r.getTarget() == result.getTarget()) {
                r.setScore(result.getScore());
                r.setNotes(result.getNotes());
                r.setResults(result.getResults());
                return;
            }
        }
        for (CheckResult r : recordingSession.getRestrictionResults()) {
            if (r.getTarget() == result.getTarget()) {
                r.setScore(result.getScore());
                r.setNotes(result.getNotes());
                r.setResults(result.getResults());
                return;
            }
        }
    }

    @Override
    public void saveOverallNotes(String comments) {
        recordingSession.setComments(comments);
    }

    @Override
    public void saveSourceCodeComments(String comments) {
        recordingSession.setSourceCodeComments(comments);
    }

    @Override
    public void saveMultiplier(double gradePercentage) {
    	System.out.println ("save Multiplier:Setting Late Penalty:" + gradePercentage);

        recordingSession.setLatePenalty(gradePercentage);
//        basicFeatureGradeRecorder.setEarlyLatePoints(aStudentName, anOnyen, aScore);
    }

//    public void saveScore(double aScore) {
//        recordingSession.setScore(aScore);
////        basicFeatureGradeRecorder.setEarlyLatePoints(aStudentName, anOnyen, aScore);
//    }
    public void save(String featureName, double score) {
        for (CheckResult r : recordingSession.getFeatureResults()) {
            if (r.getTarget().getName().equals(featureName)) {
                r.setScore(score);
                return;
            }
        }
        for (CheckResult r : recordingSession.getRestrictionResults()) {
            if (r.getTarget().getName().equals(featureName)) {
                r.setScore(score);
                return;
            }
        }
    }

    public void save(String featureName, String notes) {
        for (CheckResult r : recordingSession.getFeatureResults()) {
            if (r.getTarget().getName().equals(featureName)) {
                r.setNotes(notes);
                return;
            }
        }
        for (CheckResult r : recordingSession.getRestrictionResults()) {
            if (r.getTarget().getName().equals(featureName)) {
                r.setNotes(notes);
                return;
            }
        }
    }

    public void save(String featureName, List<TestCaseResult> results) {
        for (CheckResult r : recordingSession.getFeatureResults()) {
            if (r.getTarget().getName().equals(featureName)) {
                r.setResults(results);
                return;
            }
        }
        for (CheckResult r : recordingSession.getRestrictionResults()) {
            if (r.getTarget().getName().equals(featureName)) {
                r.setResults(results);
                return;
            }
        }
    }

    // Session methods
    @Override
    public void newSession(final String onyen, StudentFolder aStudentFolder) {
        if (onyen == null) {
            recordingSession = null;
            return;
        }

        // Get the user id from the onyen
        String userId = DirectoryUtils.find(new File(GraderSettings.get().get("path")), new FileFilter() {
            @Override
            public boolean accept(File file) {
                return !file.getName().contains("._") && file.getName().contains("(" + onyen + ")");
            }
        }).get().getName();

        // Create empty results. Don't worry, they'll be filled in later
        List<CheckResult> featureResults = new ArrayList<>();
        if (projectRequirements != null) {
            for (Feature feature : projectRequirements.getFeatures()) {
                featureResults.add(new CheckResult(0, "", CheckResult.CheckStatus.NotGraded, feature));
            }
        }
        List<CheckResult> restrictionResults = new ArrayList<>();
        if (projectRequirements != null) {
            for (Restriction restriction : projectRequirements.getRestrictions()) {
                featureResults.add(new CheckResult(0, "", CheckResult.CheckStatus.NotGraded, restriction));
            }
        }
        Tracer.info(this, "created recording session");
        recordingSession = new RecordingSession(onyen,  userId, aStudentFolder, featureResults, restrictionResults, "", 1, gradingFeatures);

    }

    @Override
    public void finish() {
        for (Logger logger : loggers) {
            logger.save(recordingSession);
        }
        recordingSession = null;
        featureComments = "";
    }

    @Override
    public String getStoredSummary() {
        return FeedbackTextSummaryLogger.restore(recordingSession);
    }

    @Override
    public String computeSummary() {
        return SerializationUtils.getSerializer("text").serialize(recordingSession);

    }

    /*
     The following was added so that the ConglomerateRecorder can work as the recorder within ASakaiProjectDatabase.
     To use it there, just do:
     FeatureGradeRecorderSelector.setFactory(new ConglomerateRecorderFactory());
     Before creating the project database.
     */
    FeatureGradeRecorder basicFeatureGradeRecorder; // this is the original factory-based recorder that this  dispatching conglomerate recorder replaces

    public FeatureGradeRecorder getBasicFeatureGradeRecorder() {
        return basicFeatureGradeRecorder;
    }

    public void setBasicFeatureGradeRecorder(FeatureGradeRecorder featureGradeRecorder) {
        this.basicFeatureGradeRecorder = featureGradeRecorder;
    }

    /**
     * Feature score setter. This is needed so that when setScore or
     * pureSetScore are called it comes here.
     */
    @Override
    public void setGrade(String aStudentName, String anOnyen, String aFeature, double aScore) {
        checkSession(anOnyen);
        save(aFeature, aScore);
        basicFeatureGradeRecorder.setGrade(aStudentName, anOnyen, aFeature, aScore);
        AllStudentsHistoryManagerFactory.getAllStudentsHistoryManager().setGrade(aStudentName, anOnyen, aFeature, aScore);
    }
    
    /**
     * Feature score setter. This is needed so that when setScore or
     * pureSetScore are called it comes here.
     */
    @Override
    public void setGrade(String aStudentName, String anOnyen, String aFeature, double aScore, List<CheckResult> results) {
        checkSession(anOnyen);
        save(results);
        basicFeatureGradeRecorder.setGrade(aStudentName, anOnyen, aFeature, aScore);
        AllStudentsHistoryManagerFactory.getAllStudentsHistoryManager().setGrade(aStudentName, anOnyen, aFeature, aScore, results);
    }

    /**
     * The ConglomerateRecorder is for recording only. This will return 0
     * always.
     *
     * @deprecated Don't use this. Write only
     */
    /**
     * Need the get method to allow browsing of past graded students
     */
    @Override
//    @Deprecated
    public double getGrade(String aStudentName, String anOnyen, String aFeature) {
        return basicFeatureGradeRecorder.getGrade(aStudentName, anOnyen, aFeature);
    }

    /**
     * This method is supposed to save the total score, but the conglomerate
     * recorder only saves and writes things out when the {@link #finish()}
     * method is called. The
     * {@link wrappers.grader.sakai.project.ProjectStepperDisplayerWrapper}
     * calls finish so it's ok that this is empty.
     */
    @Override
    public void setGrade(String aStudentName, String anOnyen, double aScore) {
        checkSession(anOnyen);
        basicFeatureGradeRecorder.setGrade(aStudentName, anOnyen, aScore); // so this is pd's recorder, and the recording session is josh's.
        AllStudentsHistoryManagerFactory.getAllStudentsHistoryManager().setGrade(aStudentName, anOnyen, aScore);
        recordingSession.setScore(aScore);
    }

    /**
     * The ConglomerateRecorder is for recording only. This will return 0
     * always.
     *
     * @deprecated Don't use this. Write only
     */
    /**
     * Need the get method to allow browsing of past graded students
     */
    @Override
//    @Deprecated
    public double getGrade(String aStudentName, String anOnyen) {
        return basicFeatureGradeRecorder.getGrade(aStudentName, anOnyen);
    }

    private void checkSession(String onyen) {
        if (recordingSession == null) {
            newSession(onyen, null);
        } else if (!recordingSession.getUserId().contains("(" + onyen + ")")) {
            finish();
            newSession(onyen, null);
        }
    }

    /*
     The following was added so that auto feedback gets mixed into the score.
     */
    @Override
    public void recordAutoGrade(GradingFeature aGradingFeature, grader.checkers.CheckResult result) {
        System.out.println("TODO: recordAutoGrade");
    }

    /*
     The following was added so that manual feedback is used.
     */
    @Override
    public void comment(GradingFeature aGradingFeature) {
        // Instead of asking the user, pull it from a variable which is updated.
        save(aGradingFeature.getFeatureName(), featureComments);
        if (featureResults != null) {
            save(aGradingFeature.getFeatureName(), featureResults);
        }
    }

    @Override
    public void setFeatureComments(String comments) {
        featureComments = comments;
    }

    @Override
    public void setFeatureResults(List<TestCaseResult> results) {
        featureResults = results;
    }

    @Override
    public void setNotes(String aStudentName, String anOnyen, String aFeature,
            String aNotes) {
        basicFeatureGradeRecorder.setNotes(aStudentName, anOnyen, aFeature, aNotes);
        AllStudentsHistoryManagerFactory.getAllStudentsHistoryManager().setNotes(aStudentName, anOnyen, aFeature, aNotes);

        save(aFeature, aNotes);
    }

    @Override
    public String getNotes(String aStudentName, String anOnyen, String aFeature) {
        // TODO Auto-generated method stub
        return "";
    }

    GradingFeatureList gradingFeatures;

    @Override
    public void setGradingFeatures(GradingFeatureList newVal) {
        gradingFeatures = newVal;

    }

    @Override
    public GradingFeatureList getGradingFeatures() {
        return gradingFeatures;
    }

    // version of save called by AProjectStepper, these two should be combined
    @Override
    public void setEarlyLatePoints(String aStudentName, String anOnyen,
            double aScore) {
    	System.out.println ("setEarlyLatePoints:Setting Late Penalty:" + anOnyen + " score " + aScore);
        recordingSession.setLatePenalty(aScore);

        basicFeatureGradeRecorder.setEarlyLatePoints(aStudentName, anOnyen, aScore);
        AllStudentsHistoryManagerFactory.getAllStudentsHistoryManager().setEarlyLatePoints(aStudentName, anOnyen, aScore);
    }

    @Override
    public double getEarlyLatePoints(String aStudentName, String anOnyen) {
        return basicFeatureGradeRecorder.getEarlyLatePoints(aStudentName, anOnyen);
    }

    // this should be integrated with whatever method saves results here
    @Override
    public void setResultFormat(String aStudentName, String anOnyen, String aFeature,
            String aResult) {
        basicFeatureGradeRecorder.setResultFormat(aStudentName, anOnyen, aFeature, aResult);
        AllStudentsHistoryManagerFactory.getAllStudentsHistoryManager().setResultFormat(aStudentName, anOnyen, aFeature, aResult);

    }

    @Override
    public String getResult(String aStudentName, String anOnyen, String aFeature) {
        return basicFeatureGradeRecorder.getResult(aStudentName, anOnyen, aFeature);
    }

    @Override
    public boolean logSaved() {
        for (Logger logger : loggers) {
        	System.out.println ("Wrtiing to logger:" + logger);
            // recordingSession is null if we are examining this entry to determine if we should visit
            if (recordingSession != null && !logger.isSaved(recordingSession.getUserId())) {
                return false;
            }
        }
        return true;
    }

    @Override
    public String getFileName() {
        return basicFeatureGradeRecorder.getFileName();
    }

    @Override
    public FileProxy getGradeSpreadsheet() {
        return basicFeatureGradeRecorder.getGradeSpreadsheet();
    }

    @Override
    public double getSourcePoints(String aStudentName, String anOnyen) {
        return basicFeatureGradeRecorder.getSourcePoints(aStudentName, anOnyen);
    }

    @Override
    public void setSourcePoints(String aStudentName, String anOnyen,
            double aScore) {
        basicFeatureGradeRecorder.setSourcePoints(aStudentName, anOnyen, aScore);
        AllStudentsHistoryManagerFactory.getAllStudentsHistoryManager().setSourcePoints(aStudentName, anOnyen, aScore);
        recordingSession.setSourcePoints(aScore);

    }

    @Override
    public void clearGrades(String anOnyen, String aStudentName) {
        basicFeatureGradeRecorder.clearGrades(anOnyen, aStudentName);

    }

	@Override
	public String getFullName(String anOnyen) {
		return basicFeatureGradeRecorder.getFullName(anOnyen);
	}

	@Override
	public void addPropertyChangeListener(PropertyChangeListener aListener) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public double getManuallyGraded(String aStudentName, String anOnyen) {
		return basicFeatureGradeRecorder.getManuallyGraded(aStudentName, anOnyen);
	}

	@Override
	public void setFullyGraded(String aStudentName, String anOnyen, double aScore) {
		basicFeatureGradeRecorder.setFullyGraded(aStudentName, anOnyen, aScore);
		
	}
	// no need to react to this. component spradsheet will take care of that.
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		
	}

}

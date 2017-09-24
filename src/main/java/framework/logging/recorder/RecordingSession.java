package framework.logging.recorder;

import java.util.List;

import framework.grading.testing.CheckResult;
import framework.navigation.StudentFolder;
import grader.assignment.GradingFeature;
import grader.assignment.GradingFeatureList;

/**
 * A class that can be converted to JSON.
 */
public class RecordingSession {

    private String userId;
    private List<CheckResult> featureResults;
    private List<CheckResult> restrictionResults;
    private String comments = "";
    private double latePenalty;
    double sourcePoints;

    double score;
    String sourceCodeComments = "";

    List<GradingFeature> gradingFeatures;
    String onyen;
    StudentFolder studentFolder;

    public RecordingSession(String anOnyen, String userId, StudentFolder aStudentFolder, List<CheckResult> featureResults, List<CheckResult> restrictionResults,
            String comments, double latePenalty, GradingFeatureList newGradingFeatures) {
        onyen = anOnyen;
        studentFolder = aStudentFolder;
    	this.userId = userId;
        this.featureResults = featureResults;
        this.restrictionResults = restrictionResults;
        this.comments = comments;
        this.latePenalty = latePenalty;
        gradingFeatures = newGradingFeatures;
    }

    public String getUserId() {
        return userId;
    }

    public List<CheckResult> getFeatureResults() {
        return featureResults;
    }

    public List<CheckResult> getRestrictionResults() {
        return restrictionResults;
    }
    public StudentFolder getStudentFolder() {
    	return studentFolder;
    }
    public String getComments() {
        return comments;
    }

    public double getLatePenalty() {
        return latePenalty;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
    public String getOnyen() {
    	return onyen;
    }
    public void setFeatureResults(List<CheckResult> featureResults) {
        this.featureResults = featureResults;
    }

    public void setRestrictionResults(List<CheckResult> restrictionResults) {
        this.restrictionResults = restrictionResults;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public void setLatePenalty(double latePenalty) {
        this.latePenalty = latePenalty;
    }

    public List<GradingFeature> getGradingFeatures() {
        return gradingFeatures;
    }

    public void setGradingFeatures(List<GradingFeature> gradingFeatures) {
        this.gradingFeatures = gradingFeatures;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        if (score < 0) {
            System.out.println("Negative score, making it 0: " + score);
            score = 0;
        }
        this.score = score;
    }

    public String getSourceCodeTAComments() {
        return sourceCodeComments;
    }

    public void setSourceCodeComments(String sourceCodeComments) {
        this.sourceCodeComments = sourceCodeComments;
    }

    public double getSourcePoints() {
        return sourcePoints;
    }

    public void setSourcePoints(double sourcePoints) {
        this.sourcePoints = sourcePoints;
    }

}

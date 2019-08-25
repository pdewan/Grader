package grader.feedback;

import grader.assignment.GradingFeature;
import grader.basics.file.FileProxy;
import grader.sakai.project.ASakaiProjectDatabase;
import grader.sakai.project.ProjectStepper;

import java.util.List;

import util.misc.Common;

public class AScoreFeedbackFileWriter implements ScoreFeedback {


    public void writeScores(ProjectStepper aProjectStepper) {
        if (aProjectStepper.getProject() == null) return;
        FileProxy feedbackFolder = aProjectStepper.getProject().getStudentAssignment().getFeedbackFolder();
        String totalScoresFile = feedbackFolder.getAbsoluteName() + "/" + ASakaiProjectDatabase.DEFAULT_SCORE_FILE_NAME;
        try {
            Common.writeText(totalScoresFile, scoresText(aProjectStepper).toString());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static StringBuffer scoresText(ProjectStepper aProjectStepper) {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("Total Score:" + aProjectStepper.getScore());
        List<GradingFeature> gradingFeatures = aProjectStepper.getProjectDatabase().getGradingFeatures();

        for (GradingFeature aGradingFeature : gradingFeatures) {
            stringBuffer.append("\n");
            stringBuffer.append(aGradingFeature.toString());
        }
        return stringBuffer;

    }

}

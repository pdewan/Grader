package framework.grading;

import framework.grading.testing.CheckResult;
import framework.grading.testing.Feature;
import framework.grading.testing.Restriction;
import framework.logging.recorder.ConglomerateRecorder;
import framework.navigation.BulkDownloadFolder;
import framework.navigation.NotValidDownloadFolderException;
import framework.navigation.SakaiBulkDownloadFolder;
import framework.navigation.StudentFolder;
import grader.basics.project.Project;
import grader.basics.settings.BasicGradingEnvironment;
import grader.basics.util.Option;
import grader.settings.GraderSettingsManager;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.joda.time.DateTime;

/**
 * Created by Andrew on 1/1/14.
 */
public class AHeadlessGradingManager implements GradingManager {

    private final PropertiesConfiguration configuration;
    private String downloadPath;
    private String end;
    private final GraderSettingsManager graderSettingsManager;
    private final String projectName;
    private final ProjectRequirements projectRequirements;

    private String start;

    public AHeadlessGradingManager(String projectName, ProjectRequirements projectRequirements, PropertiesConfiguration config, GraderSettingsManager graderSettingsManager) {
        this.projectName = projectName;
        this.projectRequirements = projectRequirements;
        this. graderSettingsManager = graderSettingsManager;
        configuration = config;
    }

    @Override
    public void run() {
        try {
            getGradingOptions();
        } catch (ConfigurationException e) {
            e.printStackTrace();
        }

        try {
            // Get the student folders, starting and ending with the specified onyens
            System.out.println(downloadPath);
            BulkDownloadFolder downloadFolder = new SakaiBulkDownloadFolder(downloadPath);
            List<StudentFolder> folders = downloadFolder.getStudentFolders(start, end);
//            loggers.add(new FeedbackJsonLogger(downloadFolder.getFolder()));
//            loggers.add(new FeedbackTextSummaryLogger(downloadFolder.getFolder()));

            // Grade each one
            for (StudentFolder folder : folders) {
                Option<Project> project = folder.getProject(projectName);
                List<CheckResult> featureResults;
                List<CheckResult> restrictionResults;

                // If there is a  project then attempt to auto grade
                if (project.isDefined()) {

                    // Run all the checks/test cases
                    featureResults = projectRequirements.checkFeatures(project.get());
                    restrictionResults = projectRequirements.checkRestrictions(project.get());
                } else {

                    // Gracefully handle absence of project by not doing auto grading
                    featureResults = new ArrayList<CheckResult>();
                    restrictionResults = new ArrayList<CheckResult>();
                    for (Feature feature : projectRequirements.getFeatures()) {
                        featureResults.add(new CheckResult(0, "", CheckResult.CheckStatus.NotGraded, feature));
                    }
                    for (Restriction restriction : projectRequirements.getRestrictions()) {
                        restrictionResults.add(new CheckResult(0, "", CheckResult.CheckStatus.NotGraded, restriction));
                    }
                }

                // Do manual grading and verification
                //GradingWindow window = GradingWindow.create(projectRequirements, folder, project, featureResults, restrictionResults);
                //boolean continueGrading = window.awaitDone();
                //String comments = window.getComments();

                // Figure out the late penalty
                Option<DateTime> timestamp = folder.getTimestamp();
                double gradePercentage = timestamp.isDefined() ? projectRequirements.checkDueDate(project.get(), timestamp.get()) : 0;

                // Save the results
                ConglomerateRecorder.getInstance().newSession(folder.getOnyen());
                ConglomerateRecorder.getInstance().save(featureResults);
                ConglomerateRecorder.getInstance().save(restrictionResults);
                //ConglomerateRecorder.getInstance().save(comments);
                ConglomerateRecorder.getInstance().saveMultiplier(gradePercentage);
                ConglomerateRecorder.getInstance().finish();

                //if (!continueGrading)
                //    System.exit(0);
            }
            System.out.println("Done!");
        } catch (NotValidDownloadFolderException e) {
            System.out.println("Not a valid Sakai download folder");
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    private void getGradingOptions() throws ConfigurationException {

        downloadPath = configuration.getString("grader.headless.path", null);
        if (downloadPath != null) {
            graderSettingsManager.setDownloadPath("grader.headless", downloadPath);
        }
        
        start = configuration.getString("grader.headless.start", null);
        if (start != null) {
            graderSettingsManager.setStartingOnyen("grader.headless", start); // is this ever retrieved? why is the module grader.headless?
        }

        end = configuration.getString("grader.headless.end", null);
        if (end != null) {
            graderSettingsManager.setEndingOnyen("grader.headless", end);
        }
        
        
        String editor = graderSettingsManager.getEditor();
        if (editor != null) {
            BasicGradingEnvironment.get().setEditor(editor);
        }
    }

//    private void logResults(StudentFolder folder, List<CheckResult> featureResults,
//                            List<CheckResult> restrictionResults, String comments, double gradePercentage) {
//
//        // Log the results
//        for (Logger logger : loggers)
//            logger.save(projectName, folder.getUserId(), featureResults, restrictionResults, comments, gradePercentage);
//    }

}

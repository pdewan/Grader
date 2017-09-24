package wrappers.grader.sakai.project;

import framework.grading.FrameworkProjectRequirements;
import framework.grading.ProjectRequirements;
import framework.grading.testing.CheckResult;
import framework.grading.testing.Feature;
import framework.grading.testing.Restriction;
import framework.gui.GradingWindow;
import framework.logging.recorder.ConglomerateRecorder;
import framework.navigation.StudentFolder;
import framework.utils.GraderSettings;
import grader.assignment.GradingFeature;
import grader.assignment.GradingFeatureList;
import grader.basics.settings.BasicGradingEnvironment;
import grader.basics.util.DirectoryUtils;
import grader.basics.util.Option;
import grader.sakai.project.AProjectStepper;
import grader.sakai.project.ProjectStepper;
import grader.sakai.project.ProjectStepperDisplayer;
import grader.sakai.project.SakaiProject;
import grader.sakai.project.SakaiProjectDatabase;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;

import wrappers.framework.grading.testing.TestCaseWrapper;
import wrappers.framework.project.ProjectWrapper;

/**
 * This wraps the "framework" GUI in a {@link ProjectStepperDisplayer}
 */
public class ProjectStepperDisplayerWrapper implements ProjectStepperDisplayer, PropertyChangeListener {

    private ProjectRequirements requirements;
    private ProjectStepper projectStepper;
    private SakaiProject project = null;
    private SakaiProjectDatabase projectDatabase;
    grader.basics.project.Project wrappedProject = null;

    @Override
    public Object display(ProjectStepper aProjectStepper) {
        projectStepper = aProjectStepper;
        projectStepper.configureNavigationList();
        projectDatabase = projectStepper.getProjectDatabase();
        aProjectStepper.addPropertyChangeListener(this);
        return null;
    }

    private void initRequirements() {
        // If the database happens to be a ProjectDatabaseWrapper, then pull out the requirements, otherwise create new
        if (requirements == null) {
            if (projectStepper.getProjectDatabase() instanceof ProjectDatabaseWrapper)
                requirements = ((ProjectDatabaseWrapper) projectStepper.getProjectDatabase()).getProjectRequirements();
            else {
                requirements = new FrameworkProjectRequirements();
                for (GradingFeature feature : projectStepper.getProjectDatabase().getGradingFeatures()) {
                    requirements.addFeature(feature.getFeatureName(), feature.getMax(), feature.isExtraCredit(), new TestCaseWrapper(feature));
                }
            }
        }
    }

    /**
     * Look for event changes and, when they happen, check the project. If it changed then display a new window
     *
     * @param evt
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {

        // Go off the project event
        if (evt.getPropertyName().equalsIgnoreCase("Project")) {

            // Get the project and the student information (onyen and name)
            project = (SakaiProject) evt.getNewValue();
            final String onyen = ((AProjectStepper) projectStepper).getOnyen();
            String studentName = DirectoryUtils.find(new File(GraderSettings.get().get("path")), new FileFilter() {
                @Override
                public boolean accept(File pathname) {
                    return pathname.getName().contains("(" + onyen + ")");
                }
            }).get().getName();
            studentName = studentName.substring(0, studentName.indexOf("("));

            // Make sure that the project requirements are all ready
            initRequirements();
            StudentFolder studentFolder = ProjectWrapper.getStudentFolder(onyen);
            List<CheckResult> featureResults;
            List<CheckResult> restrictionResults;
            GradingWindow window;

            try {
                if (project == null)
                    throw new FileNotFoundException();

                // Attempt to get the student's project and check it
                // these are no longer needed
//                project.setCanBeRun(true);
//                project.setHasBeenRun(true);
                project.maybeMakeClassDescriptions();
                grader.basics.project.Project wrappedProject = new ProjectWrapper(this.project, BasicGradingEnvironment.get().getAssignmentName());
                featureResults = requirements.checkFeatures(wrappedProject);
                restrictionResults = requirements.checkRestrictions(wrappedProject);
                window = GradingWindow.create(requirements, studentFolder, Option.apply(wrappedProject), featureResults, restrictionResults);
            } catch (FileNotFoundException e) {

                // We were unable to load the project for some reason, so start with all zeros
                featureResults = new ArrayList<CheckResult>();
                restrictionResults = new ArrayList<CheckResult>();
                for (Feature feature : requirements.getFeatures())
                    featureResults.add(new CheckResult(0, "", CheckResult.CheckStatus.NotGraded, feature));
                for (Restriction restriction : requirements.getRestrictions())
                    restrictionResults.add(new CheckResult(0, "", CheckResult.CheckStatus.NotGraded, restriction));
                Option<grader.basics.project.Project> wrappedProject = Option.empty();
                window = GradingWindow.create(requirements, studentFolder, wrappedProject, featureResults, restrictionResults);
            }

            // Wait for the grader window to finish then we'll start recording data
            boolean continueGrading = window.awaitDone();
            ConglomerateRecorder.getInstance().newSession(onyen, studentFolder);

            // Figure out the late penalty
            Option<DateTime> timestamp = studentFolder.getTimestamp();
            double gradePercentage = timestamp.isDefined() ? requirements.checkDueDate(wrappedProject, timestamp.get()) : 0;
            ConglomerateRecorder.getInstance().saveMultiplier(gradePercentage);

            // Save the comments
            String comments = window.getComments();
            ConglomerateRecorder.getInstance().saveOverallNotes(comments);

            // Save the results (grades + notes)
            GradingFeatureList features = projectDatabase.getGradingFeatures();
            for (int i = 0; i < features.size(); i++) {
                // Figure out the score for the feature/restriction
                double score = (i < featureResults.size()) ? featureResults.get(i).getScore() : restrictionResults.get(i - featureResults.size()).getScore();

                // Save the comments. We save them in the ConglomerateRecorder so that, if it is being used as the
                // manual feedback, they will be pulled in.
                ConglomerateRecorder.getInstance().setFeatureComments(featureResults.get(i).getNotes());
                ConglomerateRecorder.getInstance().setFeatureResults(featureResults.get(i).getResults());
                features.get(i).setScore(score);

                List<CheckResult> results = new ArrayList<>(featureResults.size() + restrictionResults.size());
                results.addAll(featureResults);
                results.addAll(restrictionResults);
                
                // Save the score
                projectDatabase.getFeatureGradeRecorder().setGrade(studentName, onyen, features.get(i).getFeatureName(), score, results);
            }

            // Finish up
            ConglomerateRecorder.getInstance().finish();
            if (continueGrading)
                projectStepper.next();
            else
                System.exit(0);
        }
    }

}

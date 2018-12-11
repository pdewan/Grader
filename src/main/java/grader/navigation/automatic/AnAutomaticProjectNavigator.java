package grader.navigation.automatic;

import grader.navigation.manual.AManualProjectNavigator;
import grader.sakai.project.ASakaiProjectDatabase;
import grader.sakai.project.ProjectStepper;
import grader.sakai.project.SakaiProjectDatabase;
import grader.settings.GraderSettingsModel;
import grader.trace.settings.AutomaticNavigationEnded;
import grader.trace.settings.AutomaticNavigationStarted;
import grader.trace.settings.InvalidOnyenRangeException;
import grader.trace.settings.MissingOnyenException;
import gradingTools.Driver;
import util.misc.ClearanceManager;
import util.misc.ThreadSupport;
import bus.uigen.OEFrame;

public class AnAutomaticProjectNavigator implements AutomaticProjectNavigator {

    SakaiProjectDatabase database;
    ClearanceManager clearanceManager;
    ProjectStepper projectStepper;

    public AnAutomaticProjectNavigator(SakaiProjectDatabase aDatabase) {
        database = aDatabase;
        clearanceManager = database.getClearanceManager();

    }

    @Override
    public void navigate(GraderSettingsModel settingsModel,
            OEFrame settingsFrame, boolean exitOnCompletion) {
        boolean animate = settingsModel.getNavigationSetter().getAutomaticNavigationSetter().getAnimateGrades();
        AutomaticNavigationStarted.newCase(settingsModel, database, this);

        while (true) {
//		if (animate && settingsFrame != null)
//			settingsFrame.dispose(); // keep only one frame around at a time
            try {
                database.startProjectStepper("");// first step
                if (animate && settingsFrame != null) {
                    settingsFrame.dispose(); // keep only one frame around at a time
                }
                projectStepper = database.getProjectStepper();
                break;

            } catch (MissingOnyenException e) {
                e.printStackTrace(); // should never come here
            } catch (InvalidOnyenRangeException e) {
                AManualProjectNavigator.maybeTryAgain(settingsModel, false, e.getMessage() + ". Try again.");
                if (Driver.isHeadless()) {
                	System.out.println("Headless mode, returning from navigate");
                	return;
                }
//			continue;
            }
            settingsModel.awaitBegin();
        }
//		if (settingsFrame != null)
//			settingsFrame.dispose();
//		boolean animate = settingsModel.getNavigationSetter().getAutomaticNavigationSetter().getAnimateGrades();
        Object frame = null;
        if (animate) {
            frame = database.displayProjectStepper(database.getProjectStepper());
        }
        long sleepTime = settingsModel.getNavigationSetter().getAutomaticNavigationSetter().getAnimationPauseTime() * 1000;
        projectStepper.setPlayMode(true);
        int onyensSize = database.getOnyenNavigationList().size();
        while (true) {
            if (!animate) {
                // ThreadSupport.sleep(1000); // to avoid race conditions
            } else {
                ThreadSupport.sleep(sleepTime);
                if (!projectStepper.isPlayMode()) {
                    clearanceManager.waitForClearance();
                }
            }
//            System.out.println("&&& " + Arrays.toString(database.getOnyenNavigationList().toArray()));
            if (projectStepper.getCurrentOnyenIndex() < onyensSize - 1) {
//                System.out.println("&&& " + projectStepper.getOnyen());
                projectStepper.move(true);
            } else {
                projectStepper.save();
                break;
            }

        }
        if (frame != null) {
            ASakaiProjectDatabase.dispose(frame);
        } else if (settingsFrame != null) {
        	// this can hang also
        	ASakaiProjectDatabase.executor().submit(() -> {
        		settingsFrame.setVisible(false);
        		settingsFrame.dispose(); // this hangs if another dispose before hangs
			    return null;
			});
//            settingsFrame.dispose(); // visual indication things are complete
        }
        AutomaticNavigationEnded.newCase(settingsModel, database, this);

        String automaticExitMessage = "Automatic grading complete.";
        System.out.println(automaticExitMessage);
//		JOptionPane.showMessageDialog(null, "Automatic grading complete.");
        if (exitOnCompletion) {
        	return;
//            System.exit(0);
        }

    }

}

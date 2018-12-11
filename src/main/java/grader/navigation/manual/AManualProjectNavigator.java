package grader.navigation.manual;

import grader.sakai.project.ASakaiProjectDatabase;
import grader.sakai.project.SakaiProjectDatabase;
import grader.settings.GraderSettingsModel;
import grader.trace.settings.InvalidOnyenRangeException;
import grader.trace.settings.ManualNavigationEnded;
import grader.trace.settings.ManualNavigationStarted;
import grader.trace.settings.MissingOnyenException;

import java.awt.GraphicsEnvironment;

import javax.swing.JOptionPane;

import util.trace.Tracer;
import bus.uigen.OEFrame;
//import framework.execution.reflectionObjects.ManualProject;

public class AManualProjectNavigator implements ManualProjectNavigator {
	SakaiProjectDatabase database;
	public AManualProjectNavigator(SakaiProjectDatabase aDatabase) {
		database = aDatabase;
	}
	
	public static void maybeTryAgain(GraderSettingsModel settingsModel, boolean  exitOnCompletion, String message) {
		if (!exitOnCompletion) {
			
//			message = "Did not find any entries matching filter. Try again.";
			settingsModel.setGraderStarted(false);
	} 
		
	Tracer.error("remove me: " + message);
	if (!GraphicsEnvironment.isHeadless())
	JOptionPane.showMessageDialog(null, message );
	if (exitOnCompletion)
		System.exit(0);
		
		
	}
	
	public void navigate(GraderSettingsModel settingsModel, OEFrame settingsFrame, boolean exitOnCompletion) {
		ManualNavigationStarted.newCase(settingsModel, database, this);
		if (settingsModel == null) return;
		while (true) {
		String goToOnyen = settingsModel.getOnyens().getGoToOnyens();
		Object aFrame = null;
		try {
		
			boolean retVal = database.startProjectStepper(goToOnyen);
//			 aFrame = database.displayProjectStepper(database.getProjectStepper());
			if (retVal)
				break;
			else {
				String message = "";
				if (!exitOnCompletion) {
						
						message = "Did not find any entries matching filter. Try again.";
//						settingsModel.setGraderStarted(false);
				} else
					message = "Did not find any entries  matching filter for manual mode. Exiting";
				maybeTryAgain(settingsModel, exitOnCompletion, message);
//				Tracer.error(message);
//				JOptionPane.showMessageDialog(null, message);
//				if (exitOnCompletion)
//					System.exit(0);
//				ASakaiProjectDatabase.dispose(aFrame);
			}
			
		} catch (MissingOnyenException moe) {
			String message = "Student:" + goToOnyen + " not in specified range. Try again.";
			Tracer.error(message);
			if (!GraphicsEnvironment.isHeadless())
			JOptionPane.showMessageDialog(null, message);

	//			ASakaiProjectDatabase.dispose(aFrame);
		} catch (InvalidOnyenRangeException e) {
			String message = e.getMessage() + " Try again.";
			maybeTryAgain(settingsModel, false, message);
			
//			continue;

		}
		
			
			settingsModel.awaitBegin();
//
//	}
//	// if (!goToOnyen.isEmpty()) {
//	//
//	// ASakaiProjectDatabase.setVisible(database.getProjectStepper().getFrame(),
//	// true);
	}
	if (settingsFrame != null){
		ASakaiProjectDatabase.executor().submit(() -> {
			settingsFrame.dispose();
		});
//		settingsFrame.dispose();
	}
	ManualNavigationEnded.newCase(settingsModel, database, this);

	database.displayProjectStepper(database.getProjectStepper());

	}
//	public void navigate(GraderSettingsModel settingsModel, OEFrame settingsFrame, boolean exitOnCompletion) {
//		if (settingsModel == null) return;
//		while (true) {
//		String goToOnyen = settingsModel.getOnyens().getGoToOnyen();
//		Object aFrame = null;
//		try {
//		
//			boolean retVal = database.startProjectStepper(goToOnyen);
////			 aFrame = database.displayProjectStepper(database.getProjectStepper());
//			if (retVal)
//				break;
//			else {
//				String message = "";
//				if (!exitOnCompletion) {
//						
//						message = "Did not find any entries matching filter. Try again.";
//						settingsModel.setGraderStarted(false);
//				} else
//					message = "Did not find any entries  matching filter for manual mode. Exiting";
//				Tracer.error(message);
//				JOptionPane.showMessageDialog(null, message);
//				if (exitOnCompletion)
//					System.exit(0);
////				ASakaiProjectDatabase.dispose(aFrame);
//			}
//			
//		} catch (MissingOnyenException moe) {
//			String message = "Student:" + goToOnyen + " not in specified range. Try again.";
//			Tracer.error(message);
//			JOptionPane.showMessageDialog(null, message);
//
//	//			ASakaiProjectDatabase.dispose(aFrame);
//		} catch (InvalidOnyenRangeException e) {
//			String message = e.getMessage() + ". Try again.";
//			continue;
//
//		}
//		
//			
//			settingsModel.awaitBegin();
////
////	}
////	// if (!goToOnyen.isEmpty()) {
////	//
////	// ASakaiProjectDatabase.setVisible(database.getProjectStepper().getFrame(),
////	// true);
//	}
//	if (settingsFrame != null)
//		settingsFrame.dispose();
//	database.displayProjectStepper(database.getProjectStepper());
//
//	}


}

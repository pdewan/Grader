package grader.sakai.project;

import grader.assignment.GradingFeatureList;
import grader.basics.settings.BasicGradingEnvironment;
import util.trace.TraceableWarning;
import bus.uigen.CompleteOEFrame;
import bus.uigen.ObjectEditor;
import bus.uigen.uiFrame;
import bus.uigen.trace.IllegalSourceOfPropertyNotification;
import bus.uigen.trace.UnknownPropertyNotification;

public class AnOEProjectStepperDisplayer implements ProjectStepperDisplayer<CompleteOEFrame> {
	public CompleteOEFrame display(ProjectStepper aProjectStepper) {
//		ObjectEditor.setPropertyAttribute(AProjectStepper.class, "summary",  AttributeNames.SCROLLED, true);
		
		// because of delegating property change listeners
		TraceableWarning.doNotWarn(UnknownPropertyNotification.class);
		TraceableWarning.doNotWarn(IllegalSourceOfPropertyNotification.class);

		CompleteOEFrame oeFrame = ObjectEditor.edit(aProjectStepper);
//		uiFrame oeFrame = ObjectEditor.tabEdit(aProjectStepper);

		GradingFeatureList gradingFeatures = aProjectStepper.getGradingFeatures();
		String assignmentName = aProjectStepper.getProjectDatabase().getBulkAssignmentFolder().getAssignmentName();
		String userName = BasicGradingEnvironment.get().getUserName();
		if (userName != null && !userName.isEmpty()) {
			oeFrame.setTitle("Grading Assistant to " + userName + " for " + assignmentName);

		} else
			oeFrame.setTitle("Grading Assistant for " + assignmentName);
		oeFrame.setLocation(450, 0);
		oeFrame.setSize(880, 790);
		return oeFrame;
	}

}

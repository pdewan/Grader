package grader.trace.feature;

import grader.assignment.GradingFeature;
import grader.sakai.project.SakaiProject;
import grader.sakai.project.SakaiProjectDatabase;
import grader.settings.GraderSettingsModel;
import grader.steppers.OverviewProjectStepper;
import grader.trace.steppers.SerializableStepperInfo;
import grader.trace.steppers.StepperInfo;
import bus.uigen.trace.ConstantsMenuAdditionEnded;
import util.trace.TraceableInfo;

public class GradingFeatureInfo extends StepperInfo {
GradingFeature feature;



public GradingFeatureInfo(String aMessage,
			SakaiProjectDatabase aSakaiProjectDatabase,
			OverviewProjectStepper aProjectStepper, SakaiProject aProject,
			GradingFeature aFeature,
			Object aFinder) {
		super(aMessage, aSakaiProjectDatabase, aProjectStepper, aProject, aFinder);
		feature = aFeature;
		// TODO Auto-generated constructor stub
	}

public GradingFeature getGradingFeature() {
	return feature;
}



public void setGradingFeature(GradingFeature aFeature) {
	this.feature = aFeature;
}

//@Override
//public String toCSVRow() {
//	return super.toCSVRow() 
//			+ "," + featureAutoNotes;
//}

	
//	public static SerializableFeatureAutoNotesInfo newCase(SakaiProjectDatabase aSakaiProjectDatabase, 
//			OverviewProjectStepper aProjectStepper, 
//			SakaiProject aProject,
//			String aNotes,
//			Object aFinder) {
//		String aMessage = "Overview Notes Autoly Changed to:" + aNotes;
//		SerializableFeatureAutoNotesInfo retVal = new SerializableFeatureAutoNotesInfo(aMessage, aSakaiProjectDatabase, aProjectStepper, aProject, aNotes, aFinder);
//		retVal.announce();		
//		return retVal;
//	}
	

}

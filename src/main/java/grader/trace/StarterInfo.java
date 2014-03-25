package grader.trace;

import grader.settings.GraderSettingsModel;
import bus.uigen.trace.ConstantsMenuAdditionEnded;
import util.trace.TraceableInfo;

public class StarterInfo extends GraderInfo {
	GraderSettingsModel gradingSettingsModel; 

	
	public StarterInfo(String aMessage, GraderSettingsModel aGradingSettingsModel, Object aFinder) {
		super(aMessage, aFinder);
		 gradingSettingsModel = aGradingSettingsModel;
	}
	
	public GraderSettingsModel getGradingSettingsModel() {
		return gradingSettingsModel;
	}
	
	
	

}
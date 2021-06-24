package grader.navigation;
import grader.basics.config.BasicStaticConfigurationUtils;
public enum NavigationKind  {
//	AUTOMATIC ("Automatic"),
//	MANUAL ("Manual"),
//	HYBRID ("Automatic and then manual");
	
	AUTOMATIC (BasicStaticConfigurationUtils.AUTOMATIC_GRADING),
	MANUAL (BasicStaticConfigurationUtils.MANUAL_GRADING),
	HYBRID (BasicStaticConfigurationUtils.HYBRID_GRADING);
	
	// display name constructor and variable
	
	String name;
	NavigationKind(String aName) {
		name = aName;		
	}
	public String toString() {
		return name;
	}

}

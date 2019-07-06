package grader.steppers;

public class GradedProjectNavigatorSelector {
	static GradedProjectNavigator gradedProjectNavigator;
	public static GradedProjectNavigator getOrCreateGradedProjectNavigator() {
		if (gradedProjectNavigator == null) {
			gradedProjectNavigator = new AGradedProjectNavigator();
		}
		return gradedProjectNavigator;
	}
	public static void setGradedProjectNavigator(GradedProjectNavigator newVal) {
		gradedProjectNavigator = newVal;
	}

}

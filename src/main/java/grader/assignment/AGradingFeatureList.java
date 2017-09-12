package grader.assignment;

import util.annotations.Visible;
import util.models.AListenableVector;

public class AGradingFeatureList extends AListenableVector<GradingFeature> implements GradingFeatureList{
	
	@Override
	public void open(GradingFeature element) {
		element.comment();
	}
	@Override
	public boolean hasASelection() {
		for (GradingFeature gradingFeature:this) {
			if (gradingFeature.isSelected())				
					return true;
			
		}
		return false;
	}
	@Visible(false)
	public boolean isAllGraded() {
		for (GradingFeature gradingFeature:this) {
			if ((! gradingFeature.isGraded() && 
					gradingFeature.isAutoGradable()) // auto case
					|| (!gradingFeature.isFullCredit() && 
							gradingFeature.getManualNotes().isEmpty() && !gradingFeature.isExtraCredit())	
					|| (gradingFeature.isExtraCredit() && gradingFeature.isPartialCredit() && gradingFeature.getManualNotes().isEmpty()) // assume zero credit means not attempted
					
					)
					return false;
			
		}
		return true;
	}
	
	@Visible(false)
	@Override
	public boolean hasSomeNotes() {
		for (GradingFeature gradingFeature:this) {
			if (!gradingFeature.getManualNotes().isEmpty())				
					return true;
			
		}
		return false;
	}
	
	@Visible(false)
	public boolean isAllAutoGraded() {
		for (GradingFeature gradingFeature:this) {
//			if (! (gradingFeature.isGraded() && gradingFeature.isAutoGradable()))
			if ((!gradingFeature.isGraded() && gradingFeature.isAutoGradable()))

					return false;
		}
		return true;
	}

	@Visible(false)
	@Override
	public boolean isSomeAutoGraded() {
		for (GradingFeature gradingFeature:this) {
			if ( (gradingFeature.isGraded() && gradingFeature.isAutoGradable()))
					return true;
		}
		return false;
	}
}

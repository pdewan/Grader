package grader.assignment.shift;

public class AShiftAndPercentage implements ShiftAndPercentage {
	double percentage;
	int shift;

	@Override
	public double getPercentage() {
		return percentage;
	}

	@Override
	public void setPercentage(double percentage) {
		this.percentage = percentage;
	}
	
	@Override
	public int getShift() {
		return shift;
	}
	public AShiftAndPercentage(double percentage, int shift) {
		super();
		this.percentage = percentage;
		this.shift = shift;
	}
	
	@Override
	public void setShift(int shift) {
		this.shift = shift;
	}

}

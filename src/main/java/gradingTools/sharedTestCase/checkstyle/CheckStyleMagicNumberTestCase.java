package gradingTools.sharedTestCase.checkstyle;

public class CheckStyleMagicNumberTestCase extends CheckStyleCountingWarningsTestCase {
	;
	public static final String WARNING_NAME = "magic";
	public static final String MESSAGE = "No magic number";
	public static final double DEFAULT_PENALTY_PER_MISTAKE = 0.1;

	public CheckStyleMagicNumberTestCase(String aMessage) {
		super(null, aMessage);
		penaltyPerMistake = DEFAULT_PENALTY_PER_MISTAKE;
	}

	public CheckStyleMagicNumberTestCase() {
		super(null, MESSAGE);
		penaltyPerMistake = DEFAULT_PENALTY_PER_MISTAKE;
	}

	public CheckStyleMagicNumberTestCase(double aPenaltyPerMistake) {
		super(null, MESSAGE, aPenaltyPerMistake);

	}

	@Override
	protected String warningName() {
		return WARNING_NAME;
	}

}

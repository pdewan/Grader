package gradingTools.sharedTestCase.checkstyle;

public class CheckStyleMissingActualPropertyTestCase extends CheckStyleCountingWarningsTestCase {
	;
	public static final String WARNING_NAME = "missingActualProperty";
	public static final String MESSAGE = "Missing Actual Property";
	public static final double DEFAULT_PENALTY_PER_MISTAKE = 0.1;

	public CheckStyleMissingActualPropertyTestCase(String aMessage) {
		super(null, aMessage);
		penaltyPerMistake = DEFAULT_PENALTY_PER_MISTAKE;
	}

	public CheckStyleMissingActualPropertyTestCase() {
		super(null, MESSAGE);
		penaltyPerMistake = DEFAULT_PENALTY_PER_MISTAKE;
	}

	public CheckStyleMissingActualPropertyTestCase(double aPenaltyPerMistake) {
		super(null, MESSAGE, aPenaltyPerMistake);

	}

	@Override
	protected String warningName() {
		return WARNING_NAME;
	}

}

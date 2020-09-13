package gradingTools.sharedTestCase.checkstyle;

public class ClassHasAtLeastOneInterfaceTestCase extends CheckStyleCountingWarningsTestCase {
	;
	public static final String WARNING_NAME = "classHasAtLeastOneInterface";
	public static final String MESSAGE = "Interfaces defined";

	public ClassHasAtLeastOneInterfaceTestCase(String aMessage) {
		super(null, aMessage);
	}

	public ClassHasAtLeastOneInterfaceTestCase() {
		super(null, MESSAGE);
	}

	public ClassHasAtLeastOneInterfaceTestCase(double aPenaltyPerMistake) {
		super(null, MESSAGE, aPenaltyPerMistake);

	}

	@Override
	protected String warningName() {
		return WARNING_NAME;
	}

}

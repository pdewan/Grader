package gradingTools.comp110Inc.assignment1.testcases;

import util.pipe.AnAbstractInputGenerator;

public class AnOutputBasedMixedArithmeticInputGenerator extends AnAbstractInputGenerator implements OutputBasedMixedArithmeticInputGenerator{
	protected Integer intInput;
	protected Double doubleInput;
//	protected StringBuilder output;
	protected boolean foundIntPrompt;
	protected boolean foundDoublePrompt;
//	protected boolean foundComputedResult;
	protected boolean foundOutput;
	public AnOutputBasedMixedArithmeticInputGenerator(Integer anIntInput, Double aDoubleInput) {
		intInput = anIntInput;
		doubleInput = aDoubleInput;
		
		
	}
	
	protected boolean terminationConditionMet(String aProcessName) {
		return ((intInput == null || foundIntPrompt())) &&
				((doubleInput == null) || foundDoublePrompt());
	}
	protected void maybeTerminateInput(String aProcessName) {
		setTerminatedSuccessfully(aProcessName, terminationConditionMet(aProcessName));
		if (isTerminatedSuccessfully(aProcessName)) {
			maybeNotifyTermination(aProcessName);
		}
	}
	/** the subclasses can teminate process when all input is available.
	 * this class assumes some indeterminate number of lines will be output and
	 * the process will voluntarily terminate 
	 * @param aProcessName
	 */
	protected void maybeNotifyTermination(String aProcessName) {
//		maybeNotifyTermination(aProcessName);

		
	}
	protected void setFoundIntPrompt(String aProcessName, boolean newVal) {
		System.out.println("Found int prompt " + newVal );
		foundIntPrompt = newVal;
	}
	protected void setFoundDoublePrompt(String aProcessName, boolean newVal) {
		System.out.println("Found double prompt " + newVal );

		foundDoublePrompt = newVal;
	}
	
	protected void setFoundOutput(String aProcessName, boolean newVal) {
		foundOutput = newVal;
	}
	@Override
	public void newOutputLine(String aProcessName, String anOutputLine) {
		if (isTerminatedSuccessfully(aProcessName))
			return; //ignore additional input
		System.out.println(aProcessName + " output:" + anOutputLine );
		setFoundOutput(aProcessName, true);
		if (IncrementalInputPromptTestCase.hasIntegerPrompt(anOutputLine)) {
//			foundIntPrompt = true;
			setFoundIntPrompt(aProcessName, true);
			if (intInput != null)
				notifyNewInputLine(aProcessName, intInput + "");
			
		} else if (IncrementalInputPromptTestCase.hasDoublePrompt(anOutputLine)) {
			setFoundDoublePrompt(aProcessName, true);
			if (doubleInput != null)
				notifyNewInputLine(aProcessName, doubleInput + "");
			
		} 
		maybeTerminateInput(aProcessName);
//	  else { // new output, shouldk wait for two lines actually
////			maybeTerminate(aProcessName);
//		}
		

		
	}
	@Override
	public boolean foundIntPrompt() {
		return foundIntPrompt;
	}
	@Override
	public boolean foundDoublePrompt() {
		return foundDoublePrompt;
	}
	@Override
	public boolean foundOutput() {
		return foundOutput;
	}
	

}

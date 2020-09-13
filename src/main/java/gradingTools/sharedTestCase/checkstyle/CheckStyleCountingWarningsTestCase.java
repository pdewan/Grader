package gradingTools.sharedTestCase.checkstyle;

import java.util.List;

import grader.basics.junit.TestCaseResult;
import grader.sakai.project.SakaiProject;

public abstract class CheckStyleCountingWarningsTestCase extends CheckStyleTestCase {
	public static final double DEFAULT_PENALTY_PER_MISTAKE = 0.2;
	protected double penaltyPerMistake = DEFAULT_PENALTY_PER_MISTAKE;

	public CheckStyleCountingWarningsTestCase(String aTypeName, String aMessage) {
		super(aTypeName, aMessage);
	}
	
	public CheckStyleCountingWarningsTestCase(String aTypeName, String aMessage, double aPenaltyPerMistake) {
		super(aTypeName, aMessage);
		penaltyPerMistake = aPenaltyPerMistake;

	}
	@Override
	public String regexLineFilter() {
		// TODO Auto-generated method stub
		return ".*" + warningName() + ".*";
	}

	@Override
	public String failMessageSpecifier(List<String> aFailedLines) {
		return name + "failed in:\n" + beautify(aFailedLines);
	}
	
//	String beautify (String aCheckstyleString) {
//		return aCheckstyleString.substring(aCheckstyleString.indexOf(WARNING_NAME)) + "\n";
//	}
//	String beautify (List<String> aList) {
//		StringBuffer sb = new StringBuffer();
//		for (String aString: aList) {
//			String beautifiedString = beautify(aString);
//			sb.append(beautifiedString);
//			
//		}
//		return sb.toString();
//	}
	@Override
	 protected abstract  String warningName();
	
	protected  TestCaseResult test (SakaiProject aProject, String[] aCheckStyleLines, List<String> aMatchedLines, boolean autoGrade) {
		if (aMatchedLines.size() == 0) {
			return pass();
		}
//		double score = Math.min(aMatchedLines.size(), 5) / 5.0;
//        score = Math.max(0, 1 - score);
//        return partialPass(score, beautify(aMatchedLines));
		double aPenalty = aMatchedLines.size() * penaltyPerMistake;
        double aScore  = Math.max(0, 1 - aPenalty);
        return partialPass(aScore, beautify(aMatchedLines));
    	
    }
	 

}

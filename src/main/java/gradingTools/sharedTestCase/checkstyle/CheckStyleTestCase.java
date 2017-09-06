package gradingTools.sharedTestCase.checkstyle;

import java.util.ArrayList;
import java.util.List;

import wrappers.framework.project.ProjectWrapper;
import framework.grading.testing.BasicTestCase;
import grader.basics.junit.NotAutomatableException;
import grader.basics.junit.TestCaseResult;
import grader.basics.project.NotGradableException;
import grader.basics.project.Project;
import grader.sakai.project.SakaiProject;
import grader.util.ProjectIntrospection;


public abstract class CheckStyleTestCase extends BasicTestCase {
	public static final String COMMENT_START = "//";

	protected boolean foundType;
	protected String typeTag;
//	 protected String typeName;


    public CheckStyleTestCase(String aTypeTag, String aName) {
        super(aName);
        foundType = false;
//        if (aTypeTag == null) {
//        	System.out.println ("Null type tag");
//        }
        typeTag = aTypeTag;
    }
    
	protected String typeTag() {
		return typeTag;
	}
	protected boolean foundType() {
		return foundType;
	}
	// interface defined should also use similar syntax
//	protected String typeRegex(String aTypeTag) {
//		return "(.*)" + "Class" + "(.*)" + "matching" + "(.*)" + aTypeTag + "(.*)" + "defined" + "(.*)" ;
//	}
	
	protected String typeRegex(String aTypeTag) {
//        return "(.*)" + "Class" + "(.*)" + "matching" + "(.*)[@ ]" + aTypeTag + "[ /](.*)" + "defined" + "(.*)" ;
        return "(.*)" + "Class" + "(.*)" + "matching" + "(.*)[@ ]" + aTypeTag + "(.*)" + "defined" + "(.*)" ;

    }
    
    protected boolean failOnMatch() {
    	return true;
    }
    
    protected String toLinesString(List<String> aLines) {
    	StringBuilder aString = new StringBuilder();
    	for (String aLine:aLines) {
    		aString.append(aLine);
    	}
    	return aString.toString();
    }
    protected  String warningName(){
    	return "";
    }
	protected String beautify (String aCheckstyleString) {
		return aCheckstyleString.substring(aCheckstyleString.indexOf(warningName())) + "\n";
	}
	protected String beautify (List<String> aList) {
		StringBuffer sb = new StringBuffer();
		for (String aString: aList) {
			String beautifiedString = beautify(aString);
			sb.append(beautifiedString);
			
		}
		return sb.toString();
	}
    
    public static  List<String> matchedLines (String[] aLines, String aRegex) {
    	List<String> result = new ArrayList();    
//    	int aCount = 0;
    	for (String aLine:aLines) {
//    		if (aLine.contains("Number")) {
//    			System.out.println ("Found number");
//    		}
    		if (aLine.matches(aRegex))
    			result.add(aLine);
    	}
    	return result;
    }
    public static  int numMatches (String[] aLines, String aRegex) {
    	return matchedLines(aLines, aRegex).size();
    }
    public abstract String regexLineFilter();
    public  String failMessageSpecifier(List<String> aMatchedLines) {
    	return beautify(aMatchedLines);
    }
    protected String actualType = null;
    public String getActualType() {
    	return actualType;
    }
    public static String maybeStripComment(String aString) {	 
	 	int aCommentStart = aString.indexOf(COMMENT_START);
	 	if (aCommentStart < 0)
	 		return aString.trim();
	 	return aString.substring(0, aCommentStart).trim();
	 }
    protected String getType(String aLine) {
    	final String prefix = "Class ";
    	int beginIndex = aLine.indexOf(prefix);
    	String aSuffix = aLine.substring(beginIndex+prefix.length());
    	int endIndex = aSuffix.indexOf(" ");
    	
    	return aSuffix.substring(0, endIndex);
    }
    protected TestCaseResult test(SakaiProject aProject, String[] aCheckStyleLines, boolean autoGrade) {
  
    	String aTypeTag = typeTag();
    	if (aTypeTag != null) {
    	List<String> aTypeDefinedLines = matchedLines(aCheckStyleLines, typeRegex(aTypeTag));
    	  this.foundType = aTypeDefinedLines.size() > 0;
    	  if (!foundType) {
    		  return fail (aTypeTag + " not found by checkstyle");
    	  }
    	  actualType = getType(aTypeDefinedLines.get(0));
    	}
    	List<String> aFailedLines = matchedLines(aCheckStyleLines, regexLineFilter());
    	
    	return test(aProject, aCheckStyleLines, aFailedLines, autoGrade);    	
    }
    
    protected TestCaseResult classFractionResult (SakaiProject aProject, String[] aCheckStyleLines, List<String> aMatchedLines, boolean autoGrade) {
    	int aNumMatchedInstances = aMatchedLines.size();    	
        int aTotalClassCount = aProject.getClassesManager().getClassDescriptions().size();
        String aNotes = failMessageSpecifier(aMatchedLines) + " in " + aNumMatchedInstances + " out of " + aTotalClassCount + " classes ";
        return partialPass((aTotalClassCount - aNumMatchedInstances)/aTotalClassCount, aNotes, autoGrade);    
    	
    }
    protected TestCaseResult numMatchesResult (SakaiProject aProject, String[] aCheckStyleLines, List<String> aFailedLines, boolean autoGrade) {
    	int aNumFailedInstances = aFailedLines.size();   
    	int i = 0;
    	double aScore = scoreForMatches(aNumFailedInstances);
        String aNotes = failMessageSpecifier(aFailedLines) + " " + aNumFailedInstances + " number of times";
        return partialPass((1 - aScore), aNotes, autoGrade);    
    	
    }
    
    protected TestCaseResult singleMatchScore (SakaiProject aProject, String[] aCheckStyleLines, List<String> aFailedLines, boolean autoGrade) {
    	
        String aNotes = failMessageSpecifier(aFailedLines); 
        return fail(aNotes, autoGrade);    
    	
    }
    
    protected TestCaseResult computeResult (SakaiProject aProject, String[] aCheckStyleLines, List<String> aFailedLines, boolean autoGrade) {
    	return numMatchesResult(aProject, aCheckStyleLines, aFailedLines, autoGrade);
    	
    }
    protected boolean isPassed(int aNumMatchedInstances) {
    	return aNumMatchedInstances == 0 && failOnMatch() || aNumMatchedInstances == 1 && !failOnMatch();
    }
    protected  TestCaseResult test (SakaiProject aProject, String[] aCheckStyleLines, List<String> aMatchedLines, boolean autoGrade) {
//    	int aNumFailedInstances = aFailedLines.size();
//        int aTotalClassCount = aProject.getClassesManager().getClassDescriptions().size();
//        String aNotes = failMessageSpecifier() + " in " + aNumFailedInstances + " out of " + aTotalClassCount + " classes ";
//        return partialPass((aTotalClassCount - aNumFailedInstances)/aTotalClassCount, aNotes, autoGrade);  
    	int aNumMatchedInstances = aMatchedLines.size();
//    	if (aNumMatchedInstances == 0 && failOnMatch() || aNumMatchedInstances == 1 && !failOnMatch())
        if (isPassed(aNumMatchedInstances))

    		return pass();
    	return computeResult(aProject, aCheckStyleLines, aMatchedLines, autoGrade);
    	
    }
    protected double scoreForMatchNumber(int aMistakeNumber) {
    	return 1.0/(Math.pow(2, aMistakeNumber+1)); // starting at 0
    }
    protected double scoreForMatches(int aNumMistakes) {
    	double aScore = 0;
    	for (int aMistakeNumber = 0; aMistakeNumber < aNumMistakes; aMistakeNumber++) {
    		aScore += scoreForMatchNumber(aMistakeNumber);
    	}
    	return aScore;
    }
//    protected TestCaseResult test (SakaiProject aProject, String[] aCheckStyleLines, List<String> aFailedLines, boolean autoGrade) {
//    	int aNumFailedInstances = aFailedLines.size();
//    	double penaltyPerMistake = 0.2;
//        int aTotalClassCount = aProject.getClassesManager().getClassDescriptions().size();
//        String aNotes = failMessageSpecifier() + " in " + aNumFailedInstances + " out of " + aTotalClassCount + " classes ";
//        return partialPass((aTotalClassCount - aNumFailedInstances)/aTotalClassCount, aNotes, autoGrade);    
//    	
//    }
    
    

//    @Override
    public TestCaseResult test(Project project, boolean autoGrade) throws NotAutomatableException, NotGradableException {
        if (project.getClassesManager().isEmpty())
            throw new NotGradableException();
        String aTypeTag = typeTag();
//        if (aTypeTag != null) {
//        Class aClass = IntrospectionUtil.getOrFindClass(project, this, typeTag); 
        	// class exists check should have cached the class
//        Class aClass = ProjectIntrospection.getClass(project, this, typeTag); 
//
//        if (aClass == null) {
//	    	 return fail("Type " + aTypeTag + " not defined, cannot check");
//	     }
//	     typeName = aClass.getSimpleName();
//        }
        SakaiProject aProject = ((ProjectWrapper) project).getProject();
        String aCheckStyleText = aProject.getCheckstyleText();
        String aCheckStyleFileName = aProject.getCheckStyleFileName(); // can read lines from this, maybe more efficient
        String[] aCheckStyleLines = aCheckStyleText.split(System.getProperty("line.separator"));
        return test(aProject, aCheckStyleLines, autoGrade);
        
    }
    public   String toClassName(String aCheckstyleMessage) {
		 int anIndex1 = aCheckstyleMessage.indexOf("(");
		 int anIndex2 = aCheckstyleMessage.indexOf (")");
		 if (anIndex1 < 0 || anIndex2 < 0 || anIndex2 <= anIndex1)
			 return "";
		 return aCheckstyleMessage.substring(anIndex1 + 1, anIndex2);
				
	 }
}


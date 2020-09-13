package gradingTools.sharedTestCase.checkstyle;

import java.util.List;

import grader.basics.junit.TestCaseResult;
import grader.sakai.project.SakaiProject;


public class CheckStylePackageDeclarationTestCase extends CheckStyleTestCase {
    public CheckStylePackageDeclarationTestCase() {
        super(null, "Package declaration test case");
    }
    
 
@Override
public String regexLineFilter() {
	return "(.*)Missing package declaration(.*)";
}

protected  TestCaseResult computeResult (SakaiProject aProject, String[] aCheckStyleLines, List<String> aFailedLines, boolean autoGrade) {

	return classFractionResult(aProject, aCheckStyleLines, aFailedLines, autoGrade);
	
}


@Override
public String failMessageSpecifier(List<String> aFailedLines) {
	// TODO Auto-generated method stub
	return "Missing package declaration";
}
}


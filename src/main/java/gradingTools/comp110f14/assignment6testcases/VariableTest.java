package gradingTools.comp110f14.assignment6testcases;

import grader.basics.junit.NotAutomatableException;
import grader.basics.junit.TestCaseResult;
import grader.basics.project.NotGradableException;
import grader.basics.project.Project;
import gradingTools.sharedTestCase.CodeInspectorTestCase;

//import com.github.antlrjavaparser.api.expr.VariableDeclarationExpr;

public class VariableTest extends CodeInspectorTestCase{
	boolean hasList=false;
	boolean listVis=false;
	boolean hasPrice=false;
	boolean priceVis=false;
	public VariableTest() {
		super("Has correct variables");
		// TODO Auto-generated constructor stub
	}

	@Override
	public void resetVariablesForEachProject() {
		hasList=false;
		listVis=false;
		hasPrice=false;
		priceVis=false;
		
	}

	@Override
	public TestCaseResult test(Project project, boolean autoGrade) throws NotAutomatableException,
			NotGradableException {
		hasList=false;
		listVis=false;
		hasPrice=false;
		priceVis=false;
		return super.test(project, autoGrade);
	}
	
//	@Override
//	protected void inspectVariableDeclarationExpr(VariableDeclarationExpr expr) {
//		// Code to check if it's there
//		String line=expr.getType().toString().toLowerCase();
//		if(line.contains("ingredientlist")&&line.contains("[]")&&line.contains("int"))hasList=true;
//		if(line.contains("ingredientlist")&&line.contains("[]")&&line.contains("int")&&line.contains("private"))listVis=true;
//		if(line.contains("totalprice")&&line.contains("double"))hasPrice=true;
//		if(line.contains("totalprice")&&line.contains("double")&&line.contains("private"))priceVis=true;
//		super.inspectVariableDeclarationExpr(expr);
//	}

	@Override
	public TestCaseResult codeInspectionResult() {
		if (hasList&&listVis&&hasPrice&&priceVis) {
			return pass();
		} else if(!(hasList||listVis||hasPrice||priceVis)){
			return fail("Variables desired not found!");
		}
		else{
			int numwrong=0;
			String partialMessage="";
			if(!hasList){
				numwrong++;
				partialMessage+="not finding an int array called ingredientList\n";
			}
			if(!listVis){
				numwrong++;
				partialMessage+="array not private\n";
			}
			if(!hasPrice){
				numwrong++;
				partialMessage+="not finding a double called totalPrice\n";
			}
			if(!priceVis){
				numwrong++;
				partialMessage+="double not private";
			}
			return partialPass((4-numwrong)/4,partialMessage);
		}
	}

}

package grader.modules;

import java.util.List;

import util.annotations.Column;
import util.annotations.Explanation;
import util.annotations.StructurePattern;
import util.annotations.StructurePatternNames;
import util.misc.Common;
import util.models.ADynamicEnum;
import util.models.DynamicEnum;
import bus.uigen.ObjectEditor;
@StructurePattern(StructurePatternNames.BEAN_PATTERN)
public class AModuleProblemSelector implements ModuleProblemSelector {
	DynamicEnum<String> module;
	DynamicEnum<String> problem;
	
	public AModuleProblemSelector(List<String> aModules, List<String> aProblems) {
		module = new ADynamicEnum<String>(aModules);
		problem = new ADynamicEnum<String>(aProblems);		
	}
	
	
	@Column(0)
	@Explanation("The set of modules that can be graded. Usually you will work on a single module.")
	public DynamicEnum<String> getModule() {
		return module;
	}

	public void setModule(DynamicEnum<String> module) {
		this.module = module;
//		problem.addChoice("15");
//		problem.setChoices(Common.arrayToArrayList(new String[] {"one", "two"}));
	}
	@Column(1)
	@Explanation("The set of problems in the selected module for which folders have been downloaded. Changing the problem automatically selects the corresponding download folder provided a valid folder has been slected for one of the problems in the module once.")

	public DynamicEnum<String> getProblem() {
		return problem;
	}

	public void setProblem(DynamicEnum<String> problem) {
		this.problem = problem;
	}
	
	public static void main (String[] args) {
//		System.out.println(Common.intSuffix("Assignment2"));
//		System.out.println(Common.intSuffix("Assignment"));

		List<String> modules = Common.arrayToArrayList(new String[] {"Comp110", "Comp401"});
		List<String> problems = Common.arrayToArrayList(new String[] {"1", "2"});
		AModuleProblemSelector moduleProblem = new AModuleProblemSelector(modules, problems);
		ObjectEditor.edit(moduleProblem);
		

	}
	

}

package grader.requirements.interpreter;

import framework.grading.FrameworkProjectRequirements;
import framework.grading.testing.BasicTestCase;
import grader.basics.requirements.interpreter.specification.CSVRequirementsSpecification;

public class AnInterpretedRequirements extends FrameworkProjectRequirements implements InterpretedRequirements  {
	CSVRequirementsSpecification csvRequirementsSpecification;	
	public AnInterpretedRequirements(CSVRequirementsSpecification aCSVRequirementsSpecification) {
		csvRequirementsSpecification = aCSVRequirementsSpecification;
		addRequirements();
	}
	
	public void addRequirements() {
		int aNumRequirements = csvRequirementsSpecification.getNumberOfRequirements();
		for (int i = 0; i < aNumRequirements; i++) {
			addRequirement(i);
		}		
	}
	
	public void addRequirement(int aRequirementNum) {
		String aType = "";
		try {
		 aType = csvRequirementsSpecification.getType(aRequirementNum);
		if (aType == null) return;
		String aTypeLC = aType.toLowerCase();
		if (DUE_DATE.equals(aTypeLC)) {
			 addDueDate(aRequirementNum);
		} else if (FEATURE.equals(aTypeLC)) {
			addFeature(aRequirementNum);
		} else if (RESTRICTION.equals(aTypeLC)) {
			addRestriction(aRequirementNum);
		}
		} catch (Exception e) {
			System.out.println("Could not add requirement#:" + aRequirementNum + " " + aType );
			e.printStackTrace();
		}
	}
	
	public void addDueDate(int aRequirementNum) {
			String aDateTime = csvRequirementsSpecification.getDescription(aRequirementNum);
			Double aPercentage = csvRequirementsSpecification.getMaxScore(aRequirementNum);
			addDueDate(aDateTime, aPercentage);
	}
	
    public void addFeature(int aRequirementNum) {
    	boolean isExtraCredit = csvRequirementsSpecification.isExtraCredit(aRequirementNum);
    	boolean isManual = csvRequirementsSpecification.isManual(aRequirementNum);

    	String aName = csvRequirementsSpecification.getDescription(aRequirementNum);
    	Double aMaxScore =  csvRequirementsSpecification.getMaxScore(aRequirementNum);
    	BasicTestCase aTestCase = new AnInterpretedTestCase(aName, csvRequirementsSpecification, aRequirementNum);
    	addFeature(isManual, aName, aMaxScore, isExtraCredit, aTestCase);		
	}
    
    public void addRestriction(int aRequirementNum) {
    	boolean isExtraCredit = csvRequirementsSpecification.isExtraCredit(aRequirementNum);
    	boolean isManual = csvRequirementsSpecification.isManual(aRequirementNum);
    	String aName = csvRequirementsSpecification.getDescription(aRequirementNum);
    	Double aMaxScore =  csvRequirementsSpecification.getMaxScore(aRequirementNum);
    	BasicTestCase aTestCase = new AnInterpretedTestCase(aName, csvRequirementsSpecification, aRequirementNum);
    	addRestriction(isManual, aName, aMaxScore, isExtraCredit, aTestCase);		
   	}

}

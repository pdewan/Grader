package grader.assignment.shift;

import java.io.File;

import org.joda.time.DateTime;

import framework.grading.ProjectRequirements;
import grader.basics.config.BasicExecutionSpecificationSelector;
import grader.basics.project.Project;
import grader.basics.trace.CSVSerializable;
import grader.sakai.project.SakaiProject;
import grader.spreadsheet.TotalScoreRecorderSelector;
import grader.spreadsheet.csv.ASakaiStudentNumberAssociator;
import grader.spreadsheet.csv.SakaiStudentNumberAssociator;
import gradingTools.Driver;
import util.trace.Tracer;
import wrappers.framework.project.ProjectWrapper;

public class AnAssignmentShiftManager {
    public static final String ASSIGNENT_ID = "ssignment";
    public static final String SHIFT_FILE_NAME = "shifts.csv";
 
    static SakaiStudentNumberAssociator csvAssignmentShiftRecorder;
    
    static void maybeInitializeShiftSpreadsheet() {
    	if (csvAssignmentShiftRecorder == null) {
    		String bulkFolderName = Driver.getDatabase().getBulkAssignmentFolder().getAssignmentFolder().getMixedCaseAbsoluteName();
    		File aBulkFolderParent = (new File(bulkFolderName)).getParentFile();
    		
    		String shiftFileName = aBulkFolderParent.getAbsolutePath() + "/" + SHIFT_FILE_NAME ;
    		csvAssignmentShiftRecorder = new ASakaiStudentNumberAssociator(shiftFileName);
    	}
    	
    }
    
    public static void cleanupShiftFile() {
    	String aFileName = csvAssignmentShiftRecorder.getFileName();
    	
    	File aFile = new File(aFileName);
    	if (aFile.exists()) {
    		aFile.delete();
    	}
    }

	 public static ProjectRequirements getRequirement(ProjectRequirements anOriginal, int aShift) {
	    	if (aShift == 0)
	    		return anOriginal;
		 	Class anOriginalClass = anOriginal.getClass();
	    	String aClassName = anOriginalClass.getName();
	    	String aNewClassName = "";
	    	int anAssignmentStartIndex = aClassName.indexOf(ASSIGNENT_ID);
	    	if (anAssignmentStartIndex == -1)
	    		return null;
	    	if (anAssignmentStartIndex == -1) {
	    		return anOriginal;
	    	}
	    	int anAssignmentNumberStartIndex = anAssignmentStartIndex + ASSIGNENT_ID.length();
	    	int anAssignmentNumberEndIndex;
	    	for (anAssignmentNumberEndIndex = anAssignmentNumberStartIndex +1;
	    			
	    			anAssignmentNumberEndIndex < aClassName.length() && 
	    			Character.isDigit(aClassName.charAt(anAssignmentNumberEndIndex));
	    			anAssignmentNumberEndIndex++);   
	    	
	    	// This may not have a number actually
	        String anAssignmentNumberString = aClassName.substring(
	        		anAssignmentNumberStartIndex, anAssignmentNumberEndIndex);
	             	
	    	try {
	    		int anAssignmentNumber = Integer.parseInt(anAssignmentNumberString);
	    		int aNewAssignmentNumber = anAssignmentNumber + aShift;
	    		String aNewAssignmentNumberString = anAssignmentNumberString.replaceFirst(Integer.toString(anAssignmentNumber), Integer.toString(aNewAssignmentNumber));
//	    		String aNewAssignmentClass = ASSIGNENT_ID + aNewAssignmentNumber;
	    		aNewClassName = aClassName.replaceAll(
//	    				ASSIGNENT_ID + anAssignmentNumber,
//	    				ASSIGNENT_ID + aNewAssignmentNumber);
	    		ASSIGNENT_ID + anAssignmentNumberString,
				ASSIGNENT_ID + aNewAssignmentNumberString);
	    		if (aNewClassName.equals(aClassName)) {
	    			return anOriginal;
	    		}
	    		Class<?> aNewClass = Class.forName(aNewClassName);

				return (ProjectRequirements) aNewClass.newInstance();
	    		
	    		
	    	} catch (NumberFormatException e) {
	    		Tracer.error(aClassName + " does not have a number following " + ASSIGNENT_ID );
	    		return anOriginal;
	    	} catch (InstantiationException e) {			
				e.printStackTrace();
				return anOriginal;
			} catch (IllegalAccessException e) {
				e.printStackTrace();
				return anOriginal;
			} catch (ClassNotFoundException e) {
//				e.printStackTrace();
				Tracer.info(AnAssignmentShiftManager.class, "Requirement class " + aNewClassName + " not  found for shift:" + aShift);
				return anOriginal;

			}
	    }
	 public static  ShiftAndPercentage findShiftedNonZeroPercentage(ProjectRequirements anOriginal, DateTime aDateTime) {
		 ProjectRequirements aRequirements = anOriginal;
		 int aShift = 0;
		 double aPercentage = 0.0;
		 
		 while (true) {
			 aPercentage = aRequirements.checkDueDate(aDateTime);
			 if (!BasicExecutionSpecificationSelector.getBasicExecutionSpecification()
						.getShiftAssignmentDates() ||
					 
					 aPercentage > 0 ) {
				 break;
			 }
			 aShift++;
			 ProjectRequirements aNewRequirements = getRequirement(anOriginal, aShift);
			 if (aNewRequirements == anOriginal) {
				 aShift = 0;
				 System.err.println("Grading sacrificed assignment:" + aNewRequirements);
//				 aShift--;
				 break;
			 }
			 aRequirements = aNewRequirements;			 
		 }
		 return new AShiftAndPercentage(aPercentage, aShift);	
	 }
	public static int getShift(String anOnyen) {
    	maybeInitializeShiftSpreadsheet();
    	double aDouble = csvAssignmentShiftRecorder.getNumber(anOnyen);
    	return (int) (aDouble);

	}
    public static double checkDueDate(
    		ProjectRequirements anOriginal,
    		Project aProject, 
    		DateTime dateTime) {
    	maybeInitializeShiftSpreadsheet();
    	SakaiProject aSakaiProject = ((ProjectWrapper) aProject).getProject();
    	String anOnyen = aSakaiProject.getStudentAssignment().getOnyen();
    	int aStoredShift = getShift(anOnyen);
    	ProjectRequirements aBaseRequirements = getRequirement(anOriginal, aStoredShift);
    	ShiftAndPercentage aShiftAndPercentage = findShiftedNonZeroPercentage(aBaseRequirements, dateTime);
    	
    	if ( aShiftAndPercentage.getShift() > 0) {
    		int aNewShift = aStoredShift + aShiftAndPercentage.getShift();
        	String aFullName = TotalScoreRecorderSelector.getFactory().getOrCreateFeatureGradeRecorder(Driver.getDatabase()).getFullName(anOnyen);
        	csvAssignmentShiftRecorder.setNumber(anOnyen, aFullName, aNewShift );
    	}
    	return aShiftAndPercentage.getPercentage();
    	
//    	String aFullName = TotalScoreRecorderSelector.getFactory().getGradeRecorder(Driver.getDatabase()).getFullName(anOnyen);
//    	System.out.println ("Current shift of" + anOnyen + " is " + getShift(anOnyen));

//    	csvAssignmentShiftRecorder.setNumber(anOnyen, aFullName, 0);
    	
//    	return anOriginal.checkDueDate(dateTime);
    }
	 public static void testShiftedClass() {
		 ProjectRequirements anOriginal = new gradingTools.comp401f15.assignment1.Assignment1Requirements();
		 System.out.println("Shifted class" + getRequirement(anOriginal, 3));
	 }
	 /*
	  * public Assignment1Requirements() {
    	addDueDate("08/26/2015 23:55:00", 1.05);
    	addDueDate("08/28/2015 23:55:00", 1);
    	addDueDate("09/02/2015 23:55:00", 0.9);
    	addDueDate("09/04/2015 23:55:00", 0.75);
    	
    	-----------------------------------
        public Assignment2Requirements() {
    	addDueDate("09/02/2015 23:59:00", 1.05);
    	addDueDate("09/04/2015 23:59:00", 1);
    	addDueDate("09/09/2015 23:59:00", 0.9);
    	addDueDate("09/11/2015 23:59:00", 0.75);
		----------------------------------------
		public Assignment3Requirements() {
    	addDueDate("09/09/2015 23:59:00", 1.05);
    	addDueDate("09/13/2015 23:59:00", 1);
    	addDueDate("09/16/2015 23:59:00", 0.9);
    	addDueDate("09/18/2015 23:59:00", 0.75);
		--------------------------------------
		 public Assignment12Requirements() {
        addDueDate("12/03/2015 00:30:00", 1.05);
        addDueDate("12/08/2015 00:30:00", 1.0); 
		
	  */
	 static DateTime intimeA1 = new DateTime(2015, 8, 28, 0, 0);
	 static DateTime inTimeA2AndA3 = new DateTime(2015, 9, 4, 0, 0);
	 static DateTime inTimeA3 = new DateTime(2015, 9, 13, 0, 0);
	 public static void testDates(ProjectRequirements anOriginal) {
		 ShiftAndPercentage aShiftAndPercentage = 
				 findShiftedNonZeroPercentage(anOriginal, intimeA1);
		  aShiftAndPercentage = 
				 findShiftedNonZeroPercentage(anOriginal, inTimeA2AndA3);
		  aShiftAndPercentage = 
					 findShiftedNonZeroPercentage(anOriginal, inTimeA3);
		 
		 
//		 DateTime aDateTime = new DateTime(year, monthOfYear, dayOfMonth, hourOfDay, minuteOfHour)
	 }
	 public static void main (String[] args) {
		 ProjectRequirements anOriginal = new gradingTools.comp401f15.assignment1.Assignment1Requirements();
//		 System.out.println("Shifted class" + getRequirement(anOriginal, 3));
		 testDates(anOriginal);
//		 testShiftedClass();
	 }

}

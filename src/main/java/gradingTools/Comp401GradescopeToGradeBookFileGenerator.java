package gradingTools;

import java.io.File;
import java.util.Map;

import gradingTools.sakai.gradebook.GradebookGradescopeConverter;

public class Comp401GradescopeToGradeBookFileGenerator {
//	static String[] onyensToEmail = new String[] {
//		"whglaser:whglaser@ad.unc.edu",
//		"ellenecs:ellenecs@email.unc.edu"
//	};
//	public static final String GRADES_FOLDER = "D:\\UNCGoogleDrive\\401-f15\\grades\\";

//	public static final String GRADESCOPE_FILE_NAME = "Final_scores.csv";
//	public static final String SAKAI_FILE_NAME = "Final_scores_F17_Sakai.csv";
//	public static final String GRADE_COLUMN_NAME = "Final";
	public static final String GRADESCOPE_FILE_NAME = 
			"Midterm_scores.csv";
	public static final String GRADESCOPE_FULL_FILE_NAME = 
			Comp401GradeBookToGradesScopeFileGenerator.GRADES_DIR +
			GRADESCOPE_FILE_NAME;

	public static final String SAKAI_FILE_NAME = 
			Comp401GradeBookToGradesScopeFileGenerator.GRADES_DIR +
			"Midterm_scores_F18_Sakai.csv";
//	public static final String GRADE_COLUMN_NAME = "Midterm";
//	public static final String GRADE_COLUMN_NAME = "Assignment 11";
//	public static final String GRADE_COLUMN_NAME = "Assignment 10";
//	public static final String GRADE_COLUMN_NAME = "Assignment 12";
	public static final String GRADE_COLUMN_NAME = "Final";




    public static final String gradeScopeFileName() {
    	 return Comp401GradeBookToGradesScopeFileGenerator.GRADES_DIR + GRADE_COLUMN_NAME +"_scores.csv";
    }
    public static final String sakaiFileName() {
   	 return Comp401GradeBookToGradesScopeFileGenerator.GRADES_DIR + GRADE_COLUMN_NAME + ".csv";
   }
	
	static String[] onyensToEmail = new String[] {
//		"whglaser:whglaser@ad.unc.edu"
	};

	public static void main (String[] args) {
//		Map aMap = GradebookConverter.gradeBookToMap(new File("D:\\UNCGoogleDrive\\401-f15\\grades\\gradebook_export-F17 -Template.csv"));
//		GradebookGradescopeConverter.gradescopeToGradebook(
//				GRADESCOPE_FILE_NAME,
//				SAKAI_FILE_NAME,
////				"D:\\UNCGoogleDrive\\401-f15\\grades\\Final_scores.csv", 
////				"D:\\UNCGoogleDrive\\401-f15\\grades\\Final_scores_F17_Sakai.csv",
//				Comp401GradeBookToGradesScopeFileGenerator.GRADEBOOK_TEMPLATE_FILE_NAME,
//				onyensToEmail,
//				GRADE_COLUMN_NAME);
		GradebookGradescopeConverter.gradescopeToGradebook(
				gradeScopeFileName(),
				sakaiFileName(),
//				"D:\\UNCGoogleDrive\\401-f15\\grades\\Final_scores.csv", 
//				"D:\\UNCGoogleDrive\\401-f15\\grades\\Final_scores_F17_Sakai.csv",
				Comp401GradeBookToGradesScopeFileGenerator.GRADEBOOK_TEMPLATE_FILE_NAME,
				onyensToEmail,
				GRADE_COLUMN_NAME
//				,true
				);
		
	 
	}

}

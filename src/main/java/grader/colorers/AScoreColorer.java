package grader.colorers;

import grader.letter_grade.AScoreToLetterGradeMapper;
import grader.letter_grade.LetterGrade;
import grader.letter_grade.ScoreToLetterGradeMapper;
import grader.sakai.project.SakaiProjectDatabase;

import java.awt.Color;

public class AScoreColorer implements Colorer<Double>{
	SakaiProjectDatabase database; // context for coloring
	public static final Color LIGHT_GREEN = new Color(144, 238, 144);
	public static final Color GREEN_YELLOW = new Color(173,255,47);
	public static final Color DARK_GREEN = new Color(0, 100, 0);
	public static final Color PALE_GREEN = new Color (152,251,152);
	public static final Color SPRING_GREEN = new Color (0,250,154);
	public static final Color SEA_GREEN = new Color (0,250,154);

	public static final Color DARK_KHAKI = new Color (189,183,107); 
//	public static final Color MY_HYBRID_GREEN = new Color (150,255,0); 
	public static final Color MY_HYBRID_GREEN = new Color (200,255,0);
	public static final Color YELLOW_GREEN = new Color (154,205,50);
	public static final Color KHAKI = new Color (240,230,140); 
	public static final Color OLIVE = new Color (128,128,0); 

	double maxValue;
	double notAThreshold = 0.9;
	double failThreshold = 0.6;
	Color notAColor = Color.PINK;
	Color moreThanFullCreditColor = Color.GREEN;
	Color failColor = Color.RED;
	ScoreToLetterGradeMapper mapper = new AScoreToLetterGradeMapper(); // use a factory to unite it with navigation?
	
	public AScoreColorer(SakaiProjectDatabase aDatabase, double aMaxValue) {
		database = aDatabase;
		maxValue = aMaxValue;
	}
	@Override
	public Color color(Double aFraction) {
		LetterGrade grade = mapper.toCoarseLetterGrade(aFraction, maxValue);
		switch (grade) {
		case A: return Color.GREEN;
		case B: return MY_HYBRID_GREEN;
		case C: return Color.YELLOW;
		case D: return Color.PINK;
		case F: return Color.RED;
		}
		return null;
		
				
	}

}

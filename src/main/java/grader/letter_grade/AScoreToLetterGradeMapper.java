package grader.letter_grade;

public class AScoreToLetterGradeMapper implements ScoreToLetterGradeMapper {
	public static double A_THRESHOLD = 0.9;
	public static double B_THRESHOLD = 0.8;
	public static double C_THRESHOLD = 0.7;
	public static double D_THRESHOLD = 0.6;
	/*
	 * aMaxValue is not used here
	 */
	public LetterGrade toCoarseLetterGrade (double aFraction, double aMaxValue) {
//		double percent = aFraction/aMaxValue;
		if (aFraction >= A_THRESHOLD)
			return LetterGrade.A;
		if (aFraction >= B_THRESHOLD)
			return LetterGrade.B;
		if (aFraction >= C_THRESHOLD)
			return LetterGrade.C;
		if (aFraction >= D_THRESHOLD)
			return LetterGrade.D;
		return LetterGrade.F;
			
		
	}
	
    public LetterGrade toCoarseLetterGrade (double aScore) {
		return toCoarseLetterGrade(aScore, 100);
	}

}

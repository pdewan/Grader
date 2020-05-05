package gradingTools.sakai.gradebook;

import static gradingTools.sakai.gradebook.GradebookUtils.gradebookToMap;
import static gradingTools.sakai.gradebook.GradebookUtils.toGradebookHeader;
import static gradingTools.sakai.gradebook.GradebookUtils.toGradebookRow;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.joda.time.DateTime;

import framework.grading.FrameworkProjectRequirements;
import util.misc.Common;


//Student Email, Student Name, Grading Period, My Q&A, Class Q&A, Total Grade, Grading TA, TA's Email, Last Post Date,


public class SummaryDiaryManagement extends DiaryManagement {
	public static final int EMAIL_COLUMN = 0;
	public static final int FULL_NAME_COLUMN = 1;
	public static final int DATE_COLUMN = 2;
	public static final int QA_GRADE_COLUMN = 3;
	public static final int DIARY_GRADE_COLUMN = 4;
	public static final int TOTAL_GRADE_COLUMN = 5;
	public static final int GRADER_NAME_COLUMN = 6;
	public static final int GRADER_EMAIL_COLUMN = 7;
	public static final int LAST_DATE_EMAIL_COLUMN = 8;
	public static final int MIN_COLUMNS = TOTAL_GRADE_COLUMN;

	
	public static final String CLASS_QA_COLUMN = "Class Q&A";
	public static final String MY_QA_COLUMN = "My Q&A";
	
	protected int emailColumn() {
		return EMAIL_COLUMN;
	}
	protected int fullNameColumn() {
		return FULL_NAME_COLUMN;
	}
	protected int diaryGradeColumn() {
		return DIARY_GRADE_COLUMN;
	}
	protected int qAGradeColumn() {
		return QA_GRADE_COLUMN;
	}
	protected int graderNameColumn() {
		return GRADER_NAME_COLUMN;
	}
	protected int graderEmailColumn() {
		return GRADER_EMAIL_COLUMN;
	}
	protected int graderCommentsColumn() {
		return GRADER_COMMENTS_COLUMN;
	}
	protected int diaryTextColumn() {
		return DIARY_TEXT_COLUMN;
	}
	protected int dateColumn() {
		return DATE_COLUMN;
	}
	protected int minColumns() {
		return MIN_COLUMNS;
	}
	protected boolean isSummary() {
		return true;
	}



}

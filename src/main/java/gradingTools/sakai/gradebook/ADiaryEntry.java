package gradingTools.sakai.gradebook;

import org.joda.time.DateTime;

public class ADiaryEntry implements DiaryEntry {
	DateTime date;
	String email;
	String fullName;
	int diaryPoints;
	int questionPoints;
	String grader;
	String comment;
	GradebookEntry gradebookEntry;
	StringBuffer diaryText = new StringBuffer();
	
	
	public ADiaryEntry(DateTime date, String email, String fullName,
			int diaryPoints, int questionPoint, String grader,
			String comment) {
		super();
		this.date = date;
		this.email = email;
		this.fullName = fullName;
		this.diaryPoints = diaryPoints;
		this.questionPoints = questionPoint;
		this.grader = grader;
		this.comment = comment;
	}
	@Override
	public DateTime getDate() {
		return date;
	}
	@Override
	public String getEmail() {
		return email;
	}
	@Override
	public String getFullName() {
		return fullName;
	}
	@Override
	public int getDiaryPoints() {
		return diaryPoints;
	}
	@Override
	public int getQuestionPoints() {
		return questionPoints;
	}
	@Override
	public void incrementDiaryPoints(int anIncrement) {
		diaryPoints += anIncrement;
	}
	@Override
	public void incrementQuestionPoints(int anIncrement) {
		questionPoints += anIncrement;
	}
	@Override
	public String getGrader() {
		return grader;
	}
	@Override
	public String getComment() {
		return comment;
	}
	@Override
	public GradebookEntry getGradebookEntry() {
		return gradebookEntry;
	}
	@Override
	public void setGradebookEntry(GradebookEntry gradebookEntry) {
		this.gradebookEntry = gradebookEntry;
	}	
	@Override
	public StringBuffer getDiaryText() {
		return diaryText;
	}
	@Override
	public void setDiaryText(StringBuffer diaryText) {
		this.diaryText = diaryText;
	}
}

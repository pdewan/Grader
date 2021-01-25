package gradingTools.gradebook;

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
	StringBuffer diaryTextStringBuffer = new StringBuffer();
	String diaryText;
	DiaryEntry nextDiaryEntry = null;
	
	
	
	public ADiaryEntry(DateTime date, String email, String fullName,
			int diaryPoints, int questionPoint, String grader,
			String comment, String aDiaryText) {
		super();
		this.date = date;
		this.email = email;
		this.fullName = fullName;
		this.diaryPoints = diaryPoints;
		this.questionPoints = questionPoint;
		this.grader = grader;
		this.comment = comment;
		diaryText = aDiaryText;
		diaryTextStringBuffer.append(diaryText);
	}
	public ADiaryEntry(DateTime date, String email, String fullName,
			int diaryPoints, int questionPoint) {
		super();
		this.date = date;
		this.email = email;
		this.fullName = fullName;
		this.diaryPoints = diaryPoints;
		this.questionPoints = questionPoint;
		
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
		return diaryTextStringBuffer;
	}
	@Override
	public void setDiaryText(StringBuffer diaryText) {
		this.diaryTextStringBuffer = diaryText;
	}
	@Override
	public DiaryEntry getNextDiaryEntry() {
		return nextDiaryEntry;
	}
	@Override
	public void setNextDiaryEntry(DiaryEntry nextDiaryEntry) {
		this.nextDiaryEntry = nextDiaryEntry;
	}
	public static DiaryEntry lastDiaryEntry(DiaryEntry aDiaryEntry) {
		DiaryEntry aNextDiaryEntry = aDiaryEntry.getNextDiaryEntry();
		if (aNextDiaryEntry == null) {
			return aDiaryEntry;
		} else {
			return lastDiaryEntry(aNextDiaryEntry);
		}
	}
	@Override
	public DiaryEntry lastDiaryEntry() {
		return lastDiaryEntry(this);
	}
}

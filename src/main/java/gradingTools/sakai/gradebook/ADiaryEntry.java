package gradingTools.sakai.gradebook;

public class ADiaryEntry implements DiaryEntry {
	String date;
	String email;
	String fullName;
	String diaryPoints;
	String questionPoint;
	String grader;
	String comment;
	public ADiaryEntry(String date, String email, String fullName,
			String diaryPoints, String questionPoint, String grader,
			String comment) {
		super();
		this.date = date;
		this.email = email;
		this.fullName = fullName;
		this.diaryPoints = diaryPoints;
		this.questionPoint = questionPoint;
		this.grader = grader;
		this.comment = comment;
	}
	@Override
	public String getDate() {
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
	public String getDiaryPoints() {
		return diaryPoints;
	}
	@Override
	public String getQuestionPoint() {
		return questionPoint;
	}
	@Override
	public String getGrader() {
		return grader;
	}
	@Override
	public String getComment() {
		return comment;
	}
	
	

}

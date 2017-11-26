package gradingTools.sakai.gradebook;

import org.joda.time.DateTime;

public interface DiaryEntry {

	public DateTime getDate();

	public String getEmail();

	public String getFullName();

	public int getDiaryPoints();

	public int getQuestionPoints();

	public String getGrader();

	public String getComment();

	void incrementQuestionPoints(int anIncrement);

	void incrementDiaryPoints(int anIncrement);

	GradebookEntry getGradebookEntry();

	void setGradebookEntry(GradebookEntry gradebookEntry);

}
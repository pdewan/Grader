package gradingTools.gradebook;

import org.joda.time.DateTime;

public interface ChatEntry {

	public DateTime getDate();

	public String getFullName();
	
	GradebookEntry getGradebookEntry();	

	String getChat();

	ChatEntry getNextChatEntry();

	void setNextChatEntry(ChatEntry nextDiaryEntry);

	ChatEntry lastChatEntry();

}
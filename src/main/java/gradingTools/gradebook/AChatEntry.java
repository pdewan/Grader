package gradingTools.gradebook;

import org.joda.time.DateTime;

public class AChatEntry implements ChatEntry{
	
	DateTime date;
	String fullName;
	GradebookEntry gradeBookEntry;
	String chat;
	ChatEntry nextChatEntry;
	ChatEntry lastChatEntry;

	public AChatEntry(DateTime date, String fullName, GradebookEntry gradeBookEntry, String chat) {
		super();
		this.date = date;
		this.fullName = fullName;
		this.gradeBookEntry = gradeBookEntry;
		this.chat = chat;
	}

	@Override
	public DateTime getDate() {
		return null;
	}

	@Override
	public String getFullName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public GradebookEntry getGradebookEntry() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getChat() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ChatEntry getNextChatEntry() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setNextChatEntry(ChatEntry nextDiaryEntry) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public ChatEntry lastChatEntry() {
		// TODO Auto-generated method stub
		return null;
	}

}

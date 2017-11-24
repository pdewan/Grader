package gradingTools.sakai.gradebook;
import static gradingTools.sakai.gradebook.GradebookUtils.toGradebookRow;
public class AGradebookEntry implements GradebookEntry {
	String studentID;
	String firstName;
	String lastName;
	String pid;
	public AGradebookEntry(String studentID, String firstName, String lastName,
			String pid) {
		super();
		this.studentID = studentID;
		this.firstName = firstName;
		this.lastName = lastName;
		this.pid = pid;
	}

	/* (non-Javadoc)
	 * @see gradingTools.sakai.gradebook.GradebookEntry#getStudentID()
	 */
	@Override
	public String getStudentID() {
		return studentID;
	}

	/* (non-Javadoc)
	 * @see gradingTools.sakai.gradebook.GradebookEntry#getFirstName()
	 */
	@Override
	public String getFirstName() {
		return firstName;
	}

	/* (non-Javadoc)
	 * @see gradingTools.sakai.gradebook.GradebookEntry#getLastName()
	 */
	@Override
	public String getLastName() {
		return lastName;
	}
	
	

	/* (non-Javadoc)
	 * @see gradingTools.sakai.gradebook.GradebookEntry#getPid()
	 */
	@Override
	public String getPid() {
		return pid;
	}
	
	public String toString() {
		return toGradebookRow(this, "");
	}

	
	/* (non-Javadoc)
	 * @see gradingTools.sakai.gradebook.GradebookEntry#toGradebookRow(java.lang.String)
	 */
	
}

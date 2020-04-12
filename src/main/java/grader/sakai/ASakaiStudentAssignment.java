package grader.sakai;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.apache.commons.io.FileUtils;

import grader.basics.config.BasicExecutionSpecificationSelector;
import grader.basics.file.FileProxy;
import grader.basics.file.FileProxyUtils;
import grader.basics.file.filesystem.AFileSystemFileProxy;
import grader.config.ExecutionSpecificationSelector;
import grader.trace.sakai_bulk_folder.CommentsFileLoaded;
import grader.trace.sakai_bulk_folder.DocumentFileLoaded;
import grader.trace.sakai_bulk_folder.FeedbackFolderLoaded;
import grader.trace.sakai_bulk_folder.SubmissionFolderLoaded;
import grader.trace.sakai_bulk_folder.SubmissionFolderNotFound;
import grader.trace.sakai_bulk_folder.TimestampFileLoaded;
import grader.util.GraderCommon;
import grader.util.SakaiDateUtil;
import util.trace.Tracer;

public class ASakaiStudentAssignment implements StudentAssignment {

	public static final String ENTRY_NAME_EXCLUDE_CONSENT = "consent";
	private static final String	ENTRY_NAME_EXCLUDE_SUFFIX_TXT	= ".txt";
	public static final String	ENTRY_NAME_EXCLUDE_TMP_METHOD		= "tmpmethod";
	public static String		SUBMISSION_LOCAL_NAME		= "Submission attachment(s)";
	public static String		FEEDBACK_LOCAL_NAME			= "Feedback Attachment(s)";
	public static String		COMMENTS_LOCAL_NAME			= "comments.txt";
	// public static String CHECKSTYLE_LOCAL_NAME = "checkstyle.txt";
	public static String		TIMESTAMP_LOCAL_NAME		= "timestamp.txt";

	FileProxy					submissionFolder, feedbackFolder, commentsFile, timeStampFile;
	// FileProxy checkStyleFile;
	Date						date;
	String						timeStamp;
	String						studentDescription;
	String						name, onyen;
	boolean						submitted;
	List<String>				documents					= new ArrayList();
	String						commentsFileName, checkStyleFileName;
	FileProxy					studentFolder;

	// for remote grading, this will need to take a file as an argument, which
	// is then convreted to a file proxy
	public ASakaiStudentAssignment(String aStudentDescription, FileProxy aFileProxy) {
		try {
			studentDescription = aStudentDescription;
			int parenIndex = aStudentDescription.indexOf("(");
			name = aStudentDescription.substring(0, parenIndex);
			onyen = aStudentDescription.substring(parenIndex + 1, studentDescription.length() - 1);
			studentFolder = aFileProxy;
			if (aFileProxy == null) {
				System.out.println("Null file proxy, could not create student assignment for:" + onyen);
			}
			submissionFolder = aFileProxy.getFileEntryFromLocalName(SUBMISSION_LOCAL_NAME);
			if (submissionFolder != null) {
				SubmissionFolderLoaded.newCase(submissionFolder.getAbsoluteName(), this);
			} else {
				throw SubmissionFolderNotFound.newCase(onyen, this);
			}
			// System.out.println("*^*^* " +
			// aFileProxy.getMixedCaseAbsoluteName());
			feedbackFolder = aFileProxy.getFileEntryFromLocalName(FEEDBACK_LOCAL_NAME);
			if (feedbackFolder != null)

				FeedbackFolderLoaded.newCase(feedbackFolder.getAbsoluteName(), this);
			else {
				System.err.println("No feedback folder for: " + onyen);
				if (aFileProxy instanceof AFileSystemFileProxy) {
					File aFile = Paths.get(aFileProxy.getAbsoluteName(),FEEDBACK_LOCAL_NAME ).toFile();
					aFile.mkdir();
					System.err.println("Created feedback folder for: " + onyen + " but you may have to restart grader");
					feedbackFolder = aFileProxy.getFileEntryFromLocalName(FEEDBACK_LOCAL_NAME);

				}
			}

			commentsFile = aFileProxy.getFileEntryFromLocalName(COMMENTS_LOCAL_NAME);
			if (commentsFile != null) {
				// commentsFileName = commentsFile.getAbsoluteName();
				commentsFileName = commentsFile.getMixedCaseAbsoluteName();
				CommentsFileLoaded.newCase(commentsFileName, this);

			}
			// checkStyleFile =
			// aFileProxy.getFileEntryFromLocalName(CHECKSTYLE_LOCAL_NAME);
			// if (checkStyleFile != null) {
			// //commentsFileName = commentsFile.getAbsoluteName();
			// checkStyleFileName = checkStyleFile.getMixedCaseAbsoluteName();
			// CommentsFileLoaded.newCase(checkStyleFileName, this);
			//
			// }

			timeStampFile = aFileProxy.getFileEntryFromLocalName(TIMESTAMP_LOCAL_NAME);
			try {
				timeStamp = FileProxyUtils.toText(timeStampFile);
				if (timeStamp != null) {
					date = SakaiDateUtil.toDate(timeStamp);
					TimestampFileLoaded.newCase(timeStampFile.getAbsoluteName(), this);
				}
			} catch (Exception e) {
				// Don't stop here
			}
			// System.out.println("&&& " + Boolean.toString(timeStamp == null) +
			// ", " + Boolean.toString(date == null));
			submitted = timeStamp != null && date != null;
			findDocuments();
		} catch (SubmissionFolderNotFound sfnf) {
			// sfnf.printStackTrace();
			Tracer.error(sfnf.getMessage());

		} catch (Exception e) {
			// System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}

	void findDocuments() {
		Set<String> entryNames = null;
		if (ExecutionSpecificationSelector.getExecutionSpecification().isSubDocuments()) {
			entryNames = studentFolder.getDescendentEntryNames(submissionFolder);
		} else {
			entryNames = studentFolder.getChildrenNames();
		}
		
//		studentFolder.getDescendentEntryNames(submissionFolder);
//		studentFolder.getChildrenNames();

		for (String entryName : entryNames) {

			if ((entryName.contains(ENTRY_NAME_EXCLUDE_TMP_METHOD) || entryName.contains(ENTRY_NAME_EXCLUDE_CONSENT))
				&& entryName.endsWith(ENTRY_NAME_EXCLUDE_SUFFIX_TXT)) {
				continue;
			}
			if (GraderCommon.isDocumentName(entryName)) {
				documents.add(entryName);
				DocumentFileLoaded.newCase(entryName, this);
			}
		}
	}
	// @Override
	// public void cleanFeedbackFolder() {
	// if (feedbackFolder ==null)
	// return;
	// String name = feedbackFolder.getMixedCaseAbsoluteName();
	// if (name == null)
	// return;
	// try {
	// FileUtils.cleanDirectory(new File(name));
	// } catch (IOException e) {
	// e.printStackTrace();
	// }
	// }

	@Override
	public void cleanFeedbackFolder() {
		if (feedbackFolder == null) {
			return;
		}
		String name = feedbackFolder.getMixedCaseAbsoluteName();
		if (name == null) {
			return;
		}
		try {
			// FileUtils.cleanDirectory(new File(name));
			File feedbackFile = new File(name);
			File[] children = feedbackFile.listFiles();
			for (File child : children) {
				if (child.isDirectory()) {
					FileUtils.cleanDirectory(child); // generated directory
					child.delete();
				} else {
					if (child.getName().endsWith(".ini")) {
						continue; // keep desktop.ini file around for git
					} else {
						child.delete();
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void cleanSubmissionFolder() {
		if (submissionFolder == null) {
			return;
		}
		String name = submissionFolder.getMixedCaseAbsoluteName();
		if (name == null) {
			return;
		}

		// try {
		File submissionFile = new File(name);
		File[] children = submissionFile.listFiles();
		for (File child : children) {
			if (child.isDirectory()) {
				try {
					FileUtils.cleanDirectory(child); // generated directory
					child.delete();
				} catch (IOException e) {
					System.err.println("Could not delete " + child.getAbsolutePath() + " " + e.getMessage());
				}
			}
		}
		// } catch (IOException e) {
		// e.printStackTrace();
		// }
	}

	public boolean isSubmitted() {
		return submitted;
	}

	public String getStudentName() {
		return name;
	}

	public String getOnyen() {
		return onyen;
	}

	public Date getDate() {
		return date;
	}

	public String getTimeStamp() {
		return timeStamp;
	}

	public FileProxy getSubmissionFolder() {
		return submissionFolder;
	}

	public FileProxy getFeedbackFolder() {
		return feedbackFolder;
	}

	public FileProxy getCommentsFile() {
		return commentsFile;
	}

	@Override
	public String getCommentsFileName() {
		return commentsFileName;
	}

	public FileProxy getTimeStampFile() {
		return timeStampFile;
	}

	public FileProxy getStudentFolder() {
		return studentFolder;
	}

	public List<String> getDocuments() {
		return documents;
	}

	@Override
	public String getStudentDescription() {
		return studentDescription;
	}
}

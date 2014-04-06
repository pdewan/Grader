package grader.trace.file.sakai_bulk_folder.student;

import grader.trace.file.SerializableFileInfo;

public class DocumentFileLoaded extends SerializableFileInfo {

	public DocumentFileLoaded(String aMessage, String aFileName,
			Object aFinder) {
		super(aMessage, aFileName, aFinder);
	}
	
	public static DocumentFileLoaded newCase(String aFileName,
			Object aFinder) {
		String aMessage =  "Document file loaded: " + aFileName;
		DocumentFileLoaded retVal = new DocumentFileLoaded(aMessage, aFileName, aFinder);
		retVal.announce();
		return retVal;
	}

}
package grader.trace.file.project;

import grader.trace.file.SerializableFileInfo;

public class SourceFolderIdentified extends SerializableFileInfo {

	public SourceFolderIdentified(String aMessage, String aFileName,
			Object aFinder) {
		super(aMessage, aFileName, aFinder);
	}
	
	public static SourceFolderIdentified newCase(String aFileName,
			Object aFinder) {
		String aMessage =  "Source folder identified: " + aFileName;
		SourceFolderIdentified retVal = new SourceFolderIdentified(aMessage, aFileName, aFinder);
		retVal.announce();
		return retVal;
	}

}
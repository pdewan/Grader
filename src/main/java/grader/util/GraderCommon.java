package grader.util;

public class GraderCommon {
	public static String[] docSuffixes = {".doc", ".docx", ".pdf", ".ppt", ".pptx", ".txt", ".png", ".jpg", ".jpeg", ".gif", ".tiff", ".bmp", ".lisp", ".prolog", ".sml"};
	public static String[] imageSuffixes = {".pdf", ".png", ".jpg", ".jpeg", ".gif", ".tiff", ".bmp", ".docx", ".doc"};

	
	
	public static boolean isImageDocument (String aName) {
		for (String suffix:imageSuffixes) {
			if (aName.endsWith(suffix))
				return true;
		}
		return false;
	};
	
	public static boolean isDocumentName(String aName) {
		for (String suffix:docSuffixes) {
			if (aName.endsWith(suffix))
				return true;
		}
		return false;
		
	}

}

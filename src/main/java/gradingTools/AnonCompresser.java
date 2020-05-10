package gradingTools;

import java.io.File;
import java.io.IOException;

import scala.annotation.meta.field;

public class AnonCompresser {
//	public static final String ZIP_fiLE_NAME = "C:\\Users\\dewan\\Downloads\\Assignment 1_20200125074147.zip";
//	public static final String ZIP_fiLE_NAME = "G:\\My Drive\\401 Grader Files\\Assignment 3.zip";
//	public static final String ZIP_fiLE_NAME = "D:\\sakaidownloads\\comp533\\s20\\";
//	public static final String COURSE_Folder_NAME = "G:\\My Drive\\AnonymyzedAssignmentFolders\\Comp533\\Spring18\\";
	public static final String COURSE_Folder_NAME = "G:\\My Drive\\AnonymyzedAssignmentFolders\\Comp533\\Spring19\\";

//	public static final String ZIP_Folder_NAME = "G:\\My Drive\\SakaiDownloads\\comp524\\Fall19\\";
//	public static final String ZIP_Folder_NAME = "G:\\My Drive\\SakaiDownloads\\comp533\\Spring19\\";
//	public static final String ZIP_Folder_NAME = "G:\\My Drive\\SakaiDownloads\\comp533\\Spring18\\";
//	public static final String ZIP_Folder_NAME = "G:\\My Drive\\SakaiDownloads\\comp401\\Fall17\\";
//	public static final String COURSE_Folder_NAME = "G:\\My Drive\\AnonymyzedAssignmentFolders\\Comp401\\Fall2018\\";
//	public static final String ZIP_Folder_NAME = "C:\\Users\\dewan\\Downloads\\Spring2020\\";

//	public static final String ZIP_Folder_NAME = "E:\\anon\\";
//	public static final String FOLDER_TO_DELETE = "E:\\anon\\Assignment 2";

//	static String[] ignoreFileSuffixes = AnonDriver.ignoreFileSuffixes;

	public static final String[] FOLDER_NAMES = {
			"Assignment 0"
//			"Assignment 1",
//			"Assignment 1.zip",
//			"Assignment 2",
//			"Assignment 3",
//			"Assignment 4",
//			"Assignment 5",
//			"Assignment 6",
//			"Assignment 7",
//			"Assignment 8",
//			"Assignment 9",
//			"Assignment 10",
//			"Assignment 11",
////
//			"Assignment 12",
			
	};


	public static void main(String[] args) {
		Anon.setIgnoreFileSuffixes(AnonDriver.ignoreFileSuffixes);
		for (String aFolderName:FOLDER_NAMES ) {
			String aFullZipFileName = COURSE_Folder_NAME + aFolderName;
	
		try {
			String[] deleteIgnoredFilesArgs = {aFullZipFileName, "false", "0"};

			Anon.main(deleteIgnoredFilesArgs);
			String[] compressFolderArgs = {aFullZipFileName, "false", "2"};
			Anon.main(compressFolderArgs);


//			
//			myArgs = new String[] {"G:\\My Drive\\401 Grader Files\\Assignment 2.zip", "false"};
//			Anon.main(myArgs);
			
//			myArgs = new String[] {"G:\\My Drive\\401 Grader Files\\Assignment 3.zip", "false"};
//			Anon.main(myArgs);		
			
		} catch (IOException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		}
	
	}
}

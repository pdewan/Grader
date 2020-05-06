package gradingTools;

import java.io.File;
import java.io.IOException;

import scala.annotation.meta.field;

public class AnonDriver {
//	public static final String ZIP_fiLE_NAME = "C:\\Users\\dewan\\Downloads\\Assignment 1_20200125074147.zip";
//	public static final String ZIP_fiLE_NAME = "G:\\My Drive\\401 Grader Files\\Assignment 3.zip";
//	public static final String ZIP_fiLE_NAME = "D:\\sakaidownloads\\comp533\\s20\\";
//	public static final String ZIP_Folder_NAME = "G:\\My Drive\\SakaiDownloads\\comp533\\s20\\";
//	public static final String ZIP_Folder_NAME = "G:\\My Drive\\SakaiDownloads\\comp524\\Fall19\\";
//	public static final String ZIP_Folder_NAME = "G:\\My Drive\\SakaiDownloads\\comp533\\Spring19\\";
//	public static final String ZIP_Folder_NAME = "G:\\My Drive\\SakaiDownloads\\comp533\\Spring18\\";
//	public static final String ZIP_Folder_NAME = "G:\\My Drive\\SakaiDownloads\\comp401\\Fall17\\";
//	public static final String ZIP_Folder_NAME = "C:\\Users\\dewan\\Downloads\\Fall2016\\";

	public static final String ZIP_Folder_NAME = "E:\\anon\\";
	public static final String FOLDER_TO_DELETE = "E:\\anon\\Assignment 2";

	static String[] ignoreFileSuffixes = {".png", ".class", ".gif", ".jpg"};

	public static final String[] ZIP_fiLE_NAMES = {
//			"Assignment 0.zip",
			"Assignment 1.zip",
			"Assignment 2.zip",
			"Assignment 3.zip",
//			"Assignment 4.zip",
//			"Assignment 5.zip",
//			"Assignment 6.zip",
//			"Assignment 7.zip",
//			"Assignment 8.zip",
//			"Assignment 9.zip",
//			"Assignment 10.zip",
//			"Assignment 11.zip",
//
//			"Assignment 12.zip",
			
	};



	public static void main(String[] args) {
		Anon.setIgnoreFileSuffixes(ignoreFileSuffixes);
		for (String aZipFileName:ZIP_fiLE_NAMES ) {
			String aFullZipFileName = ZIP_Folder_NAME + aZipFileName;
	
		try {
			String[] myArgs = {aFullZipFileName};
//			String[] myArgs = {FOLDER_TO_DELETE, "false", "0"};
//			String[] myArgs = {FOLDER_TO_DELETE, "false", "1"};
			Anon.main(myArgs);
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

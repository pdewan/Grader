package gradingTools;

import java.io.IOException;

public class AnonDriver {
//	public static final String ZIP_fiLE_NAME = "C:\\Users\\dewan\\Downloads\\Assignment 1_20200125074147.zip";
	public static final String ZIP_fiLE_NAME = "G:\\My Drive\\401 Grader Files\\Assignment 3.zip";

	public static void main(String[] args) {
		
		try {
			String[] myArgs = {ZIP_fiLE_NAME, "false"};
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

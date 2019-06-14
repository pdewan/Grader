package grader.checkStyle;

import framework.execution.ProcessRunner;
import grader.basics.config.BasicStaticConfigurationUtils;
import grader.basics.execution.BasicProjectExecution;
import grader.basics.execution.Runner;
import grader.basics.execution.RunningProject;
import grader.sakai.project.ASakaiProjectDatabase;

import java.io.File;
import java.io.IOException;

public class AJavaCheckStyleInvoker  implements CheckStyleInvoker{
	
	
	
	
	

	
	public RunningProject checkStyle(String aSourceFileFlder) {
		String aConfigurationFileName = ASakaiProjectDatabase.getCurrentSakaiProjectDatabase().getAssignmentDataFolder().getCheckStyleConfigurationFileName();

			String windowsName = aSourceFileFlder;
        
        String[] command = {"java", "com.puppycrawl.tools.checkstyle.Main", "-c",  aConfigurationFileName ,   windowsName };

        String[] args = {};
        Runner processRunner = new ProcessRunner(new File("."));
//        RunningProject aReturnValue = processRunner.run(null, command, "", args, 2000);
        RunningProject aReturnValue = processRunner.run(null, command, "", args, BasicStaticConfigurationUtils.DEFAULT_PROCESS_TIME_OUT*10);

        return aReturnValue;


    }
	
	
	public static void main (String[] args) {
        
		AJavaCheckStyleInvoker checkStyleInvoker = new AJavaCheckStyleInvoker();
//		compiler.compileFile("Test Data/Test C/Assignment1/All, Correct (acorrect)/Submission attachment(s)/program1/Program1/src/Simple.c",
//				"Test Data/Test C/Assignment1/All, Correct (acorrect)/Submission attachment(s)/program1/Program1/bin");
//		compiler.compileFile("src/Simple.c",
//				"Test Data/Test C/Assignment1/All, Correct (acorrect)/Submission attachment(s)/program1/Program1/bin");
		File aFile = new File("D:/dewan_backup/Java/Comp110-Grader/Test Data/Correct 110 F13 Results/Assignment3/All, Correct (acorrect)/Submission attachment(s)/acorrect_program3/Assignment3/src");
		if (!aFile.exists()) {
			System.err.println("No file");
			
		} else {
			RunningProject aProject;
			try {
				aProject = checkStyleInvoker.checkStyle (aFile.getCanonicalPath());
			
			String anOutput = aProject.getOutput();
			System.out.println("Check sty;e output:" + anOutput);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		System.out.println("Main ends");
//		checkStyleInvoker.checkStyle ("Test Data/Test 110 F13 Assignments/Assignment3/All, Correct (acorrect)/Submission attachment(s)/program1/Program1/src/");
//		checkStyleInvoker.checkStyle ("src/src");

//		compiler.compileFile("src/Simple.c",
//				"Test Data/Test C/Assignment1/All, Correct (acorrect)/Submission attachment(s)/program1/Program1");
    }

	
	

}

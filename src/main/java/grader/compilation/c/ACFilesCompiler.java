package grader.compilation.c;

import framework.execution.ProcessRunner;
import grader.basics.BasicLanguageDependencyManager;
import grader.basics.execution.Runner;
import grader.basics.execution.RunningProject;
import grader.basics.project.Project;
import grader.compilation.ClassFilesCompiler;
import grader.config.ExecutionSpecificationSelector;
import grader.settings.GraderSettingsManagerSelector;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import util.misc.Common;

public class ACFilesCompiler implements ClassFilesCompiler {
	
	public static final String OBJECT_SUFFIX = ".obj";
	public static final String EXECUTABLE_SUFFIX = ".exe";
	public static final String OBJECT_OPTION = "-o";
	public static final String EXECUTABLE_OPTION = "/link/out:";

	
	String compilerPath;
	public ACFilesCompiler() {
		setCompilerPath();
		
	}
	
	void setCompilerPath() {
            try {
            	compilerPath =  	ExecutionSpecificationSelector.getExecutionSpecification().getCCompiler();
//		 compilerPath = GraderSettingsManagerSelector.getGraderSettingsManager().getCCompilerPath();
//		compilerPath = COMPILER_COMMAND;
            } catch (ExceptionInInitializerError | NoSuchMethodError e) {
                e.printStackTrace();
            }
	}
	
	public RunningProject compileFile(String aFileName, String workingDirectory) {
        String windowsName = Common.toWindowsFileName(aFileName);
        int extensionIndex = aFileName.indexOf(BasicLanguageDependencyManager.getSourceFileSuffix());
        if (extensionIndex < 1)
        	return null;
        String baseName = aFileName.substring(0, extensionIndex);
        String shortBaseName = Common.absoluteNameToLocalName(baseName);
        String shortObjName = shortBaseName + OBJECT_SUFFIX;
        String shortExecName = shortBaseName + EXECUTABLE_SUFFIX;
//        String fullObjName = workingDirectory + "/" + shortObjName;
//        String fullExecName = workingDirectory + "/" + shortExecName;
        String fullObjName = "bin" + "\\" + shortObjName;
        String fullExecName = "bin" + "\\" + shortExecName;

        String[] args = {};
//        String[] command = {compilerPath, windowsName, "-o",  fullObjName , EXECUTABLE_OPTION  + fullExecName};
        String[] command = {compilerPath, windowsName, "-o",  shortObjName , EXECUTABLE_OPTION  + shortExecName};

//        String[] command = {compilerPath, windowsName};

//        String[] command = {"Test Data/Test C/Assignment1/All, Correct (acorrect)/Submission attachment(s)/program1/Program1/src/Simple.exe"};
        Runner processRunner = new ProcessRunner(new File(workingDirectory));
        return processRunner.run(null, command, "", args, 3000);
//		Common.exec(command);

//        Common.exec(command);
//       WordOpenedFile.newCase(aFileName, this);

    }
	
	String quote(String s) {
		return "\"" + s + "\"" ;
	}
	
	public static void main (String[] args) {
        
		ACFilesCompiler compiler = new ACFilesCompiler();
//		compiler.compileFile("Test Data/Test C/Assignment1/All, Correct (acorrect)/Submission attachment(s)/program1/Program1/src/Simple.c",
//				"Test Data/Test C/Assignment1/All, Correct (acorrect)/Submission attachment(s)/program1/Program1/bin");
//		compiler.compileFile("src/Simple.c",
//				"Test Data/Test C/Assignment1/All, Correct (acorrect)/Submission attachment(s)/program1/Program1/bin");
		compiler.compileFile("..\\src\\Simple.c",
		"Test Data/Test C/Assignment1/All, Correct (acorrect)/Submission attachment(s)/program1/Program1/bin");
//		compiler.compileFile("src/Simple.c",
//				"Test Data/Test C/Assignment1/All, Correct (acorrect)/Submission attachment(s)/program1/Program1");
    }

	@Override
	public RunningProject compile(Project aBasicProject, File sourceFolder, File buildFolder, File anObjectFolder, List<File> sourceFiles)
			throws IOException, IllegalStateException {
		List<String> commandList= new ArrayList(sourceFiles.size() + 1);
		commandList.add(compilerPath);
		boolean separateSrcBin = !sourceFolder.equals(buildFolder);
		for (File sourceFile:sourceFiles) {
			String shortName = sourceFile.getName();
			if (separateSrcBin) {
				commandList.add("../src/" + shortName);
			} else {
				commandList.add(shortName);
			}			
		}
//		 String[] command = {compilerPath, windowsName, "-o",  shortObjName , EXECUTABLE_OPTION  + shortExecName};

//       String[] command = {compilerPath, windowsName};

//       String[] command = {"Test Data/Test C/Assignment1/All, Correct (acorrect)/Submission attachment(s)/program1/Program1/src/Simple.exe"};
       Runner processRunner = new ProcessRunner(buildFolder);
       String[] args = {};
       String[] command =  commandList.toArray(args);
       return processRunner.run(null, command, "", args, 3000);
		

		
	}
	

}

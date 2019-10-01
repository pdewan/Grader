package grader.compilation;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;

import util.trace.javac.CompilerNotFound;
import framework.execution.ARunningProject;
import grader.basics.project.Project;
import grader.basics.settings.BasicGradingEnvironment;
import grader.config.StaticConfigurationUtils;
import grader.trace.compilation.SourceFileCompiled;

public class AJavaClassFilesCompiler implements ClassFilesCompiler{
	/**
	 * Given an ArrayList of .javaFiles, returns whether the .java file needs to
	 * be compiled or recompiled
	 * 
	 * @param javaFiles
	 *            ArrayList of .java files
	 * @throws IOException
	 */
	public ARunningProject compile(Project aBasicProject, File sourceFolder, File buildFolder, File anObjectFolder, List<File> sourceFiles) throws IOException, IllegalStateException {

		JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
	
		if (compiler != null) {
			StandardJavaFileManager fileManager = compiler.getStandardFileManager(null, null, null);

			List<String> optionList = new ArrayList<String>();
			// set the output directory for the compiler
			String buildFolderPath = buildFolder.getCanonicalPath();

//			String graderClassPath = BasicGradingEnvironment
//					.get().getCanonicalClasspath();
			String myClassPath = StaticConfigurationUtils.getExecutionCommandRawClassPath(); // no quotes on PC!
//			String myClassPath = graderClassPath; // classpath can inclide spaces and should not be in quotes
//			myClassPath = myClassPath.replaceAll("\"", "");
			String runtimeClasspath = System.getProperty("java.class.path");
			String aCompleteClasspath = myClassPath + BasicGradingEnvironment.get().getClassPathSeparator() + runtimeClasspath;
			if (myClassPath != null) {
//				optionList.addAll(Arrays.asList("-d", buildFolderPath, "-cp", myClassPath, "-Xlint:unchecked"));
				optionList.addAll(Arrays.asList("-d", buildFolderPath, "-cp", aCompleteClasspath, "-Xlint:unchecked"));
				System.out.println("Buildfolder:" + buildFolderPath + " classpath: " + aCompleteClasspath);

			} else {
				optionList.addAll(Arrays.asList("-d", buildFolderPath, "-Xlint:unchecked"));
				System.out.println("Buildfolder:" + buildFolderPath + " classpath: " + myClassPath);

			}

			Iterable<? extends JavaFileObject> compilationUnits = fileManager
					.getJavaFileObjectsFromFiles(sourceFiles);
			StringWriter outputWriter = new StringWriter();
			compiler.getTask(new BufferedWriter(outputWriter), fileManager, null, optionList, null, compilationUnits).call();
			String output = outputWriter.toString();
			if (!output.isEmpty()) {
				System.err.println(output);
			}
			for (File javaFile:sourceFiles) {
				SourceFileCompiled.newCase(javaFile.getAbsolutePath(), this);
				
			}
			fileManager.close();
		} else {
			String home = System.getProperty("java.home");
			System.out.println("Java Home =" + home);
			throw CompilerNotFound.newCase(this);
		}
		return null;
	}


}

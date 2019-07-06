package grader.compilation;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import framework.project.ProjectClassesManager;
import grader.basics.config.BasicProjectExecution;
import grader.basics.execution.RunningProject;
import grader.basics.project.BasicProjectClassesManager;
import grader.basics.project.Project;
import grader.basics.settings.BasicGradingEnvironment;
import grader.config.ExecutionSpecificationSelector;
import grader.language.LanguageDependencyManager;
import grader.navigation.NavigationKind;
import grader.settings.GraderSettingsModelSelector;
import util.trace.Tracer;

public class AFilesToCompileFinder implements FilesToCompileFinder {
	/**
	 * Given a .java and .class file, returns whether the .java file needs to be
	 * compiled or recompiled
	 *
	 * @param aSourceFile
	 *            The Java file
	 * @param anObjectFile
	 *            The class file
	 * @return boolean true if should compile/recompile false otherwise
	 */
	public static boolean shouldCompile(File aSourceFile, File anObjectFile) {
		// System.out.println("Class time:" + classFile.lastModified() + "
		// source time:" + javaFile.lastModified());
		if (anObjectFile == null)
			return true;
		String javaName = aSourceFile.getName();
		String className = anObjectFile.getName();
		return 
			// we will check this in caller of isCompileSourceFiles
			// !project.hasBeenCompiled() 
		 !anObjectFile.getName().startsWith("_") &&
			!aSourceFile.getName().startsWith("._") &&
			(BasicGradingEnvironment.get().isForceCompile()
				|| !anObjectFile.exists()
				|| anObjectFile.lastModified() < aSourceFile.lastModified());
		// (classFile.lastModified() - javaFile.lastModified()) < 1000;

	}
	
	@Override
	public List<File> filesToCompile(Project aProject, File aBuildFolder, Set<File> sourceFiles) throws IOException {
//		if (ExecutionSpecificationSelector.getExecutionSpecification().isCompileMissingClasses()
//				|| ExecutionSpecificationSelector.getExecutionSpecification().isForceCompile()
//				|| ExecutionSpecificationSelector.getExecutionSpecification().isPreCompileMissingClasses()) {
		
//		if (BasicGradingEnvironment.get().isCompileMissingObjectCode()
//			|| BasicGradingEnvironment.get().isForceCompile()
//			|| BasicGradingEnvironment.get().isPreCompileMissingObjectCode()) {

			// Check if any files need to be compiled
			ArrayList<File> aFilesToCompile = new ArrayList<File>();
			for (File file : sourceFiles) {
				String className = BasicProjectClassesManager.getClassName(file);
				File classFile = ProjectClassesManager.getClassFile(aBuildFolder, className);
				if (shouldCompile(file, classFile)) {
					aFilesToCompile.add(file);
				}
			}
			return aFilesToCompile;
			
//			// should be called in caller
//			if (aFilesToCompile.size() > 0) {
//				if (GraderSettingsModelSelector.getGraderSettingsModel() != null
//					&& GraderSettingsModelSelector.getGraderSettingsModel().getNavigationSetter()
//						.getNavigationKind() != NavigationKind.AUTOMATIC
//					&& !ExecutionSpecificationSelector.getExecutionSpecification().isPreCompileMissingClasses()) {
//
////					&& !BasicGradingEnvironment.get().isPreCompileMissingObjectCode()) {
//					return;
//				}
//				try {
//					// ideally should check if automatic compilation has occurred
//					System.out.println("Attempting to compile files.");
//					// project.setHasBeenCompiled(true);
//					maybeSetHasBeenCompiled(true);
//					aFilesToCompile = new ArrayList<>(sourceFiles); // compile
//																	 // all if
//																	 // we have
//																	 // to
//																	 // compile
//																	 // one
//																	 // because
//																	 // the
//																	 // previpusly
//																	 // comppiled
//																	 // files
//																	 // may be
//																	 // different
//																	 // version
//																	 // from
//																	 // ours
//
//					// compile(aFilesToCompile);
//					// JavaClassFilesCompilerSelector.getClassFilesCompiler().compile(buildFolder,
//					// aFilesToCompile);
//					boolean aSavedValue = BasicProjectExecution.isWaitForMethodConstructorsAndProcesses();
//					BasicProjectExecution.setWaitForMethodConstructorsAndProcesses(true);
//					RunningProject runningProject = LanguageDependencyManager.getSourceFilesCompiler()
//						.compile(basicProject, sourceFolder, buildFolder, basicProject.getObjectFolder(), aFilesToCompile);
//					if (runningProject != null) {
//						String anErrors = runningProject.getErrorOutput();
//						 String outputAndErrors =
//						 runningProject.getOutputAndErrors();
////						 runningProject.appendOutputAndErrorsToTranscriptFile(project);
//						appendOutputAndErrorsToTranscriptFile(runningProject);
//
//					} else {
//						Tracer.info (this, "No output from compiler"); 
//					}
//					System.out.println("Compilation attempt finished.");
//					maybeSetCanBeCompiled(true);
//					BasicProjectExecution.setWaitForMethodConstructorsAndProcesses(aSavedValue);
//
//					// project.setCanBeCompiled(true);
//					// reuse the same loader as its binary folder name has
//					// changed
//					// project.setNewClassLoader();
//					// proxyClassLoader = project.getClassLoader();
//					// project.getClassLoader().setBinaryFileSystemFolderName(buildFolder.getAbsolutePath());
//					setBinaryFileSystemFolderName();
//
//				} catch (Exception e) {
//					System.out.println("Compilation failed: " + e.toString());
//					e.printStackTrace();
//					// project.setCanBeCompiled(false);
//					maybeSetCanBeCompiled(false);
//				}
//			}
//		}
		
	}

}

package framework.project;

import framework.execution.ARunningProject;
import grader.basics.BasicLanguageDependencyManager;
import grader.basics.execution.BasicProjectExecution;
import grader.basics.execution.RunningProject;
import grader.basics.project.BasicClassDescription;
import grader.basics.project.BasicProjectClassesManager;
import grader.basics.project.ClassDescription;
import grader.basics.project.ClassesManager;
import grader.basics.project.CurrentProjectHolder;
import grader.basics.settings.BasicGradingEnvironment;
import grader.compilation.FilesToCompileFinder;
import grader.compilation.FilesToCompileFinderSelector;
import grader.config.ExecutionSpecificationSelector;
import grader.config.StaticConfigurationUtils;
import grader.execution.ABuildFolderClassLoader;
import grader.execution.ProxyClassLoader;
import grader.language.LanguageDependencyManager;
import grader.navigation.NavigationKind;
import grader.sakai.project.SakaiProject;
import grader.settings.GraderSettingsModelSelector;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import util.trace.Tracer;

/**
 * @see ClassesManager
 */
public class ProjectClassesManager extends BasicProjectClassesManager implements ClassesManager {

	// private final File buildFolder;
	// private final File sourceFolder;
	// private ClassLoader classLoader;
	protected ProxyClassLoader	proxyClassLoader;
	// private final Set<ClassDescription> classDescriptions;
	// List<String> classNamesToCompile = new ArrayList();

	SakaiProject				project;
//	boolean hasCompileErrors = false;
//	FilesToCompileFinder aFilesToCompileFinder = FilesToCompileFinderSelector.getOrCreateFilesToCompileFinder();

//	FilesToCompileFinder aFilesToCompileFinder = LanguageDependencyManager.getFilesToCompileFinder();


	protected void setProject(Object aProject) {
		project = (SakaiProject) aProject;
	}

	public ProjectClassesManager(FatProject aFatProject, SakaiProject aProject, File buildFolder, File sourceFolder, String aSourceFilePattern)
		throws IOException,
		ClassNotFoundException {
		super(aFatProject, aProject, buildFolder, sourceFolder, aSourceFilePattern);
		Tracer.info(this, "ProjectClassesManager created");
		
	}

	protected void initializeClassLoaders() throws IOException {
		// Create the Class Loader and load the classes
		if (BasicGradingEnvironment.get().isLoadClasses()) {
			// classLoader = project.getClassLoader();
			// if (classLoader == null)
			setOtherLoaders();
			// if (project != null) {
			// proxyClassLoader = project.getClassLoader();
			// }
			classLoader = new URLClassLoader(new URL[] { buildFolder.toURI().toURL() });
			// classLoader = new ABuildFolderClassLoader(buildFolder);
		}
	}

	protected Class loadClass(String className) throws ClassNotFoundException {
    	Tracer.info(this, "Loading Class:" + className);

		if (BasicGradingEnvironment.get().isLoadClasses() &&
			proxyClassLoader != null) // if we are precompiling or cleaning up,
										 // this will be null
		{
			// c = classLoader.loadClass(className);
			return proxyClassLoader.loadClass(className);
		}
		if (BasicGradingEnvironment.get().isLoadClasses() && proxyClassLoader == null) {
			return classLoader.loadClass(className);
		}
		return null;
	}

	protected ClassDescription createClassDescription(Class<?> javaClass, File source) {
		return new AParsableClassDescription(javaClass, source);
	}

	protected void setOtherLoaders() {
		if (project != null) {
			proxyClassLoader = project.getClassLoader();
		}
	}

	protected void maybeCheckStyle() {
		checkStyle(project, sourceFolder);

	}

	protected void maybeSetHasBeenCompiled(boolean newValue) {
		project.setHasBeenCompiled(true);

	}

	protected void maybeSetCanBeCompiled(boolean newValue) {
		project.setHasBeenCompiled(newValue);

	}

	protected void setBinaryFileSystemFolderName() {
		ProxyClassLoader aClassLoader = project.getClassLoader();
		if (aClassLoader == null)
			return;
		aClassLoader.setBinaryFileSystemFolderName(buildFolder.getAbsolutePath());
//		project.getClassLoader().setBinaryFileSystemFolderName(buildFolder.getAbsolutePath());
	}

	protected void setCanBeLoaded(boolean newValue) {
		project.setCanBeLoaded(true);
		// project.setCanBeLoaded(true);

	}

	protected void setHasBeenLoaded(boolean newValue) {
		project.setHasBeenLoaded(true);

	}

	protected boolean hasBeenCompiled() {
		return project.hasBeenCompiled();
	}

	protected void appendOutputAndErrorsToTranscriptFile(ARunningProject aRunningProject) {
		aRunningProject.appendOutputAndErrorsToTranscriptFile(project);

	}

	protected void manageClassLoader() {
		// may have to unload class so am doing this reset
		project.setNewClassLoader();
		proxyClassLoader = project.getClassLoader();
		project.getClassLoader().setBinaryFileSystemFolderName(buildFolder.getAbsolutePath());
	}

	public static final String	INFO_PREFIX			= "info:";
	public static final String	WARNING_PREFIX		= "warning:";
	public static final String	SRC_STRING			= "src";
	public static final String	ATTACHMENT_STRING	= "Submission attachment(s)";

	public static String beautify(String aLine) {
		String aRetVal = aLine;
		int aTruncateIndex = aLine.indexOf(SRC_STRING);
		if (aTruncateIndex != -1) {
			// aRetVal = aLine.substring(aTruncateIndex + SRC_STRING.length() +
			// 1);
			aRetVal = aLine.substring(aTruncateIndex);

			return aRetVal;
		}
		aTruncateIndex = aLine.indexOf(SRC_STRING);
		if (aTruncateIndex != -1) {
			aRetVal = aLine.substring(aTruncateIndex + ATTACHMENT_STRING.length() + 1);
			return aRetVal;
		}
		return aLine;

	}

	/*
	 * Pending checks have different source file names
	 */
	public static final String	FILE_NAME_SUFFIX_0	= "java:0:";
	public static final String	FILE_NAME_SUFFIX_1	= "java:1:";

	public static String removeFileName(String aLine) {
		String aRetVal = aLine;
		int aTruncateIndex = aLine.indexOf(FILE_NAME_SUFFIX_0);
		if (aTruncateIndex != -1) {
			aRetVal = aLine.substring(aTruncateIndex + FILE_NAME_SUFFIX_0.length());
			return aRetVal;
		}
		aTruncateIndex = aLine.indexOf(FILE_NAME_SUFFIX_1);
		if (aTruncateIndex != -1) {
			aRetVal = aLine.substring(aTruncateIndex + FILE_NAME_SUFFIX_1.length());
			return aRetVal;
		}

		return aLine;

	}

	protected void checkStyle(SakaiProject aProject, File aSourceFolder) {
//		if (!BasicGradingEnvironment.get().isCheckStyle())
//			return;
		if (!ExecutionSpecificationSelector.getExecutionSpecification().isCheckStyle())
			return;
		File aFile = new File(aProject.getCheckStyleFileName());
		if (aFile.exists()) { // have already run it, should we add a method to
								 // project to record?
			return;
		}
		boolean aSavedValue = BasicProjectExecution.isWaitForMethodConstructorsAndProcesses();
		BasicProjectExecution.setWaitForMethodConstructorsAndProcesses(true);
		RunningProject aRunner =
			LanguageDependencyManager.getCheckStyleInvoker().checkStyle(aSourceFolder.getAbsolutePath());
		String aCheckStyleOutputFile = aProject.getCheckStyleFileName();
		String aCheckStyleOutput = aRunner.getOutput();
		if (aCheckStyleOutput == null) {
			System.err.println("Not checking style" );
			return;
		}
		String[] aLines = aCheckStyleOutput.split("\n");
		Set<String> anOutputLines = new HashSet<String>();
		BasicProjectExecution.setWaitForMethodConstructorsAndProcesses(aSavedValue);
		try {
			PrintWriter pw = new PrintWriter(new FileWriter(aCheckStyleOutputFile));

			for (int i = 0; i < aLines.length; i++) {
				String aLine = aLines[i];
				String aLineWithoutFileName = removeFileName(aLine);
				if (anOutputLines.contains(aLineWithoutFileName))
					continue;
				String aBeautifiedLine = beautify(aLine);
				pw.println(aBeautifiedLine);
				anOutputLines.add(aLineWithoutFileName);
			}

			pw.close();
			// Common.writeText(aCheckStyleOutputFile, aCheckStyleOutput);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	// /**
	// * This loads all the classes based on the source code files.
	// *
	// * @param sourceFolder The folder containing the source code
	// * @throws ClassNotFoundException
	// * @throws IOException
	// */
	protected void maybeCompileSourceFiles(Set<File> sourceFiles) throws IOException {
		if (hasBeenCompiled())
			return;
		if (!ExecutionSpecificationSelector.getExecutionSpecification().isCompileMissingClasses()
				&& !ExecutionSpecificationSelector.getExecutionSpecification().isForceCompile()
				&& !ExecutionSpecificationSelector.getExecutionSpecification().isPreCompileMissingClasses()) {
			return;
		}
		if (GraderSettingsModelSelector.getGraderSettingsModel() != null
				&& GraderSettingsModelSelector.getGraderSettingsModel().getNavigationSetter()
					.getNavigationKind() != NavigationKind.AUTOMATIC
				&& !ExecutionSpecificationSelector.getExecutionSpecification().isPreCompileMissingClasses()) {

//				&& !BasicGradingEnvironment.get().isPreCompileMissingObjectCode()) {
				return;
		}
//		List<File> aFilesToCompile = FilesToCompileFinderSelector.getOrCreateFilesToCompileFinder().filesToCompile(project.getWrapper(), buildFolder, sourceFiles);
		List<File> aFilesToCompile = LanguageDependencyManager.getFilesToCompileFinder().filesToCompile(CurrentProjectHolder.getCurrentProject(), buildFolder, sourceFiles);

			
		
////		if (BasicGradingEnvironment.get().isCompileMissingObjectCode()
////			|| BasicGradingEnvironment.get().isForceCompile()
////			|| BasicGradingEnvironment.get().isPreCompileMissingObjectCode()) {
//
//			// Check if any files need to be compiled
//			ArrayList<File> aFilesToCompile = new ArrayList<File>();
//			for (File file : sourceFiles) {
//				String className = getClassName(file);
//				File classFile = getClassFile(className);
//				if (shouldCompile(file, classFile)) {
//					classNamesToCompile.add(className);
//					aFilesToCompile.add(file);
//				}
//			}
			if (aFilesToCompile.size() > 0) {
//				if (GraderSettingsModelSelector.getGraderSettingsModel() != null
//					&& GraderSettingsModelSelector.getGraderSettingsModel().getNavigationSetter()
//						.getNavigationKind() != NavigationKind.AUTOMATIC
//					&& !ExecutionSpecificationSelector.getExecutionSpecification().isPreCompileMissingClasses()) {
//
////					&& !BasicGradingEnvironment.get().isPreCompileMissingObjectCode()) {
//					return;
//				}
				try {
					// ideally should check if automatic compilation has occurred
//					System.out.println("Attempting to compile files.");
					// project.setHasBeenCompiled(true);
					maybeSetHasBeenCompiled(true);
					aFilesToCompile = new ArrayList<>(sourceFiles); // compile
																	 // all if
																	 // we have
																	 // to
																	 // compile
																	 // one
																	 // because
																	 // the
																	 // previpusly
																	 // comppiled
																	 // files
																	 // may be
																	 // different
																	 // version
																	 // from
																	 // ours

					// compile(aFilesToCompile);
					// JavaClassFilesCompilerSelector.getClassFilesCompiler().compile(buildFolder,
					// aFilesToCompile);
					boolean aSavedValue = BasicProjectExecution.isWaitForMethodConstructorsAndProcesses();
					BasicProjectExecution.setWaitForMethodConstructorsAndProcesses(true);
//					System.out.println("Attempting to compile files:" + aFilesToCompile);
					Tracer.info(this, "Attempting to compile files:" + aFilesToCompile);

					RunningProject runningProject = LanguageDependencyManager.getSourceFilesCompiler()
						.compile(basicProject, sourceFolder, buildFolder, basicProject.getObjectFolder(), aFilesToCompile);
					if (runningProject != null) {
						String anErrors = runningProject.getErrorOutput();
						 String outputAndErrors =
						 runningProject.getOutputAndErrors();
//						 runningProject.appendOutputAndErrorsToTranscriptFile(project);
						appendOutputAndErrorsToTranscriptFile(runningProject);

					} else {
						Tracer.info (this, "No output from compiler"); 
					}
//					System.out.println("Compilation attempt finished.");
					Tracer.info(this, "Compilation attempt finished.");

					
					maybeSetCanBeCompiled(true);
					BasicProjectExecution.setWaitForMethodConstructorsAndProcesses(aSavedValue);

					// project.setCanBeCompiled(true);
					// reuse the same loader as its binary folder name has
					// changed
					// project.setNewClassLoader();
					// proxyClassLoader = project.getClassLoader();
					// project.getClassLoader().setBinaryFileSystemFolderName(buildFolder.getAbsolutePath());
					setBinaryFileSystemFolderName();

				} catch (Exception e) {
					System.out.println("Compilation failed: " + e.toString());
					e.printStackTrace();
					// project.setCanBeCompiled(false);
					maybeSetCanBeCompiled(false);
				}
			}
		
		
	}
	// /**
		// * This loads all the classes based on the source code files.
		// *
		// * @param sourceFolder The folder containing the source code
		// * @throws ClassNotFoundException
		// * @throws IOException
		// */
		protected void maybeCompileSourceFilesMonolithic(Set<File> sourceFiles) throws IOException {
			if (ExecutionSpecificationSelector.getExecutionSpecification().isCompileMissingClasses()
					|| ExecutionSpecificationSelector.getExecutionSpecification().isForceCompile()
					|| ExecutionSpecificationSelector.getExecutionSpecification().isPreCompileMissingClasses()) {
			
//			if (BasicGradingEnvironment.get().isCompileMissingObjectCode()
//				|| BasicGradingEnvironment.get().isForceCompile()
//				|| BasicGradingEnvironment.get().isPreCompileMissingObjectCode()) {

				// Check if any files need to be compiled
				ArrayList<File> aFilesToCompile = new ArrayList<File>();
				for (File file : sourceFiles) {
					String className = getClassName(file);
					File classFile = getClassFile(className);
					if (shouldCompile(file, classFile)) {
						classNamesToCompile.add(className);
						aFilesToCompile.add(file);
					}
				}
				if (aFilesToCompile.size() > 0) {
					if (GraderSettingsModelSelector.getGraderSettingsModel() != null
						&& GraderSettingsModelSelector.getGraderSettingsModel().getNavigationSetter()
							.getNavigationKind() != NavigationKind.AUTOMATIC
						&& !ExecutionSpecificationSelector.getExecutionSpecification().isPreCompileMissingClasses()) {

//						&& !BasicGradingEnvironment.get().isPreCompileMissingObjectCode()) {
						return;
					}
					try {
						// ideally should check if automatic compilation has occurred
						System.out.println("Attempting to compile files.");
						// project.setHasBeenCompiled(true);
						maybeSetHasBeenCompiled(true);
						aFilesToCompile = new ArrayList<>(sourceFiles); // compile
																		 // all if
																		 // we have
																		 // to
																		 // compile
																		 // one
																		 // because
																		 // the
																		 // previpusly
																		 // comppiled
																		 // files
																		 // may be
																		 // different
																		 // version
																		 // from
																		 // ours

						// compile(aFilesToCompile);
						// JavaClassFilesCompilerSelector.getClassFilesCompiler().compile(buildFolder,
						// aFilesToCompile);
						boolean aSavedValue = BasicProjectExecution.isWaitForMethodConstructorsAndProcesses();
						BasicProjectExecution.setWaitForMethodConstructorsAndProcesses(true);
						RunningProject runningProject = LanguageDependencyManager.getSourceFilesCompiler()
							.compile(basicProject, sourceFolder, buildFolder, basicProject.getObjectFolder(), aFilesToCompile);
						if (runningProject != null) {
							String anErrors = runningProject.getErrorOutput();
							 String outputAndErrors =
							 runningProject.getOutputAndErrors();
//							 runningProject.appendOutputAndErrorsToTranscriptFile(project);
							appendOutputAndErrorsToTranscriptFile(runningProject);

						} else {
							Tracer.info (this, "No output from compiler"); 
						}
						System.out.println("Compilation attempt finished.");
						maybeSetCanBeCompiled(true);
						BasicProjectExecution.setWaitForMethodConstructorsAndProcesses(aSavedValue);

						// project.setCanBeCompiled(true);
						// reuse the same loader as its binary folder name has
						// changed
						// project.setNewClassLoader();
						// proxyClassLoader = project.getClassLoader();
						// project.getClassLoader().setBinaryFileSystemFolderName(buildFolder.getAbsolutePath());
						setBinaryFileSystemFolderName();

					} catch (Exception e) {
						System.out.println("Compilation failed: " + e.toString());
						e.printStackTrace();
						// project.setCanBeCompiled(false);
						maybeSetCanBeCompiled(false);
					}
				}
			}
			
		}

	protected void maybeCompileWrongVersion(String className, File file, Set<File> sourceFiles) {
		try {
			System.out
				.println(
					"Class files are the incorrect version for the current Java version. Attempting to recompile files.");
			// List<File> recompiledFileList = new ArrayList<>();
			// recompiledFileList.add(file);
			// if (project.hasBeenCompiled() )
			if (hasBeenCompiled())
				return;
			// project.setHasBeenCompiled(true);
			maybeSetHasBeenCompiled(true);
			List<File> recompiledFileList = new ArrayList<>(sourceFiles);
			// recompiledFileList.add(file);
			System.out.println("Recompiling files:" + recompiledFileList);
			RunningProject runningProject = LanguageDependencyManager
				.getSourceFilesCompiler().compile(basicProject, sourceFolder,
					buildFolder, basicProject.getObjectFolder(), recompiledFileList);
			// project.setCanBeCompiled(true);
			maybeSetCanBeCompiled(true);
			// may have to unload class so am doing this reset
			manageClassLoader();
			// project.setNewClassLoader();
			// proxyClassLoader = project.getClassLoader();
			// project.getClassLoader().setBinaryFileSystemFolderName(buildFolder.getAbsolutePath());

			classLoader = new URLClassLoader(new URL[] { buildFolder.toURI().toURL() });


			if (runningProject != null) {
				appendOutputAndErrorsToTranscriptFile(runningProject);
				// runningProject
				// .appendOutputAndErrorsToTranscriptFile(project);

			}
			System.out.println("Compilation attempt finished.");

			Class c = null;
			if (BasicGradingEnvironment.get().isLoadClasses()) {
				// c = classLoader.loadClass(className);
				c = proxyClassLoader.loadClass(className);

			}

			if (c != null) {
				classDescriptions
					.add(new BasicClassDescription(c, file));
			}
		} catch (Exception ex) {
			// project.setCanBeCompiled(false);
			maybeSetCanBeCompiled(false);

			System.out.println("Compilation failed: " + ex.toString());
		} catch (Error e) {
			Tracer.error("Recompilation had problems");
			e.printStackTrace();
		}
	}

	// /**
	// * This loads all the classes based on the source code files.
	// *
	// * @param sourceFolder The folder containing the source code
	// * @throws ClassNotFoundException
	// * @throws IOException
	// */
	// protected void loadClasses(File sourceFolder) throws
	// ClassNotFoundException, IOException {
	//
	// Set<File> sourceFiles = DirectoryUtils.getSourceFiles(sourceFolder,
	// sourceFilePattern);
	//// Set<File> javaFiles = DirectoryUtils.getFiles(sourceFolder, new
	// FileFilter() {
	//// @Override
	//// public boolean accept(File pathname) {
	//// return pathname.getName().endsWith(".java");
	//// }
	//// });
	// if (BasicGradingEnvironment.get().isCompileMissingObjectCode()
	// || BasicGradingEnvironment.get().isForceCompile()
	// || BasicGradingEnvironment.get().isPreCompileMissingObjectCode()) {
	//
	// // Check if any files need to be compiled
	// ArrayList<File> aFilesToCompile = new ArrayList<File>();
	// for (File file : sourceFiles) {
	// String className = getClassName(file);
	// File classFile = getClassFile(className);
	// if (shouldCompile(file, classFile)) {
	// classNamesToCompile.add(className);
	// aFilesToCompile.add(file);
	// }
	// }
	// if (aFilesToCompile.size() > 0) {
	// if (GraderSettingsModelSelector.getGraderSettingsModel() != null
	// &&
	// GraderSettingsModelSelector.getGraderSettingsModel().getNavigationSetter().getNavigationKind()
	// != NavigationKind.AUTOMATIC
	// && !BasicGradingEnvironment.get().isPreCompileMissingObjectCode()) {
	// return;
	// }
	// try {
	// System.out.println("Attempting to compile files.");
	//// project.setHasBeenCompiled(true);
	// maybeSetHasBeenCompiled(true);
	// aFilesToCompile = new ArrayList<>(sourceFiles); // compile all if we have
	// to compile one because the previpusly comppiled files may be different
	// version from ours
	//
	// // compile(aFilesToCompile);
	// //
	// JavaClassFilesCompilerSelector.getClassFilesCompiler().compile(buildFolder,
	// aFilesToCompile);
	// RunningProject runningProject = LanguageDependencyManager.
	// getSourceFilesCompiler().
	// compile(sourceFolder, buildFolder, aFilesToCompile);
	// if (runningProject != null) {
	// // String outputAndErrors = runningProject.getOutputAndErrors();
	//// runningProject.appendOutputAndErrorsToTranscriptFile(project);
	// appendOutputAndErrorsToTranscriptFile(runningProject);
	//
	// }
	// System.out.println("Compilation attempt finished.");
	// maybeSetCanBeCompiled(true);
	//// project.setCanBeCompiled(true);
	// // reuse the same loader as its binary folder name has changed
	//// project.setNewClassLoader();
	//// proxyClassLoader = project.getClassLoader();
	//// project.getClassLoader().setBinaryFileSystemFolderName(buildFolder.getAbsolutePath());
	// setBinaryFileSystemFolderName();
	//
	// } catch (Exception e) {
	// System.out.println("Compilation failed: " + e.toString());
	// e.printStackTrace();
	//// project.setCanBeCompiled(false);
	// maybeSetCanBeCompiled(false);
	// }
	// }
	// }
	//// if (project != null) {
	//// project.setHasBeenLoaded(true);
	//// project.setCanBeLoaded(true);
	//// }
	// setHasBeenLoaded(true);
	// setCanBeLoaded(true);
	//
	// for (File file : sourceFiles) {
	// String className = getClassName(file);
	// // System.out.println(className);
	// try {
	// Class c = null;
	// if (BasicGradingEnvironment.get().isLoadClasses() &&
	// proxyClassLoader != null) // if we are precompiling or cleaning up, this
	// will be null
	// {
	//// c = classLoader.loadClass(className);
	// c = proxyClassLoader.loadClass(className);
	// }
	// if (BasicGradingEnvironment.get().isLoadClasses() && proxyClassLoader ==
	// null) {
	// c = classLoader.loadClass(className);
	// }
	// if (c != null) {
	//// classDescriptions.add(new BasicClassDescription(c, file));
	// classDescriptions.add(createClassDescription(c, file));
	//
	// }
	//// else if (AProject.isLoadClasses()) {
	//// c = classLoader.loadClass(className);
	//// }
	// } catch (IncompatibleClassChangeError e) {
	// System.out.println("IncompatibleClassChangeError :" + file + " "+
	// e.getMessage());
	// } catch (UnsupportedClassVersionError e) {
	////
	//// } catch (UnsupportedClassVersionError |
	//// IncompatibleClassChangeError e) {
	// try {
	// System.out
	// .println("Class files are the incorrect version for the current Java
	// version. Attempting to recompile files.");
	//// List<File> recompiledFileList = new ArrayList<>();
	//// recompiledFileList.add(file);
	//// if (project.hasBeenCompiled() )
	// if (hasBeenCompiled())
	// break;
	//// project.setHasBeenCompiled(true);
	// maybeSetHasBeenCompiled(true);
	// List<File> recompiledFileList = new ArrayList<>(sourceFiles);
	//// recompiledFileList.add(file);
	// System.out.println("Recompiling files:" + recompiledFileList);
	// RunningProject runningProject = LanguageDependencyManager
	// .getSourceFilesCompiler().compile(sourceFolder,
	// buildFolder, recompiledFileList);
	//// project.setCanBeCompiled(true);
	// maybeSetCanBeCompiled(true);
	// // may have to unload class so am doing this reset
	// manageClassLoader();
	//// project.setNewClassLoader();
	//// proxyClassLoader = project.getClassLoader();
	//// project.getClassLoader().setBinaryFileSystemFolderName(buildFolder.getAbsolutePath());
	//
	// classLoader = new URLClassLoader(new URL[]{buildFolder.toURI().toURL()});
	//
	//
	// if (runningProject != null) {
	// appendOutputAndErrorsToTranscriptFile( runningProject);
	//// runningProject
	//// .appendOutputAndErrorsToTranscriptFile(project);
	//
	// }
	// System.out.println("Compilation attempt finished.");
	//
	// Class c = null;
	// if (BasicGradingEnvironment.get().isLoadClasses()) {
	//// c = classLoader.loadClass(className);
	// c = proxyClassLoader.loadClass(className);
	//
	// }
	//
	// if (c != null) {
	// classDescriptions
	// .add(new BasicClassDescription(c, file));
	// }
	// } catch (Exception ex) {
	//// project.setCanBeCompiled(false);
	// maybeSetCanBeCompiled(false);
	//
	// System.out.println("Compilation failed: " + ex.toString());
	// }
	// } catch (Exception e) {
	//// project.setCanBeCompiled(false);
	//
	// System.out.println("Could not load class:" + file + " " +
	// e.getClass().getSimpleName() + " "+ e.getMessage());
	//// e.printStackTrace();
	// } catch (Error e) {
	//// project.setCanBeCompiled(false);
	//
	// System.out.println("Could not load class:" + file + " " +
	// e.getClass().getSimpleName() + " " + e.getMessage());
	//
	//// e.printStackTrace();
	//// throw new IOException(e.getMessage());
	// }/*
	// * catch (Exception e) { throw new IOException(e.getMessage()); }
	// */
	//
	// }
	// }
	//
	//
	// /**
	// * Given a file, this finds the canonical class name.
	// *
	// * @param file The Java file
	// * @return The canonical class name.
	// * @throws IOException
	// */
	// protected String getClassName(File file) throws IOException {
	//
	// // Figure out the package
	// List<String> lines = FileUtils.readLines(file, null);
	// String packageName = "";
	// for (String line : lines) {
	// if (line.startsWith("package ")) {
	// packageName = line.replace("package", "").replace(";", "").trim() + ".";
	// }
	// }
	//
	// // Figure out the class name and combine it with the package
	//// String className = file.getName().replace(".java", "");
	// String className =
	// file.getName().replace(BasicLanguageDependencyManager.getSourceFileSuffix(),
	// "");
	//
	// return packageName + className;
	// }
	//
	/**
	 * Given a Java class name, this finds associated .class file.
	 *
	 * @param className
	 *            The canonical name of the Java class
	 * @return The .class File.
	 */
	private File getClassFile(String className) {
		return getClassFile(buildFolder, className);

//		File classFolder = buildFolder;
//		String[] splitClassName = className.split("\\.");
//		for (int i = 0; i < splitClassName.length - 1; i++) {
//			String packagePart = splitClassName[i];
//			classFolder = new File(classFolder, packagePart);
//		}
//
//		String classFileName;
//		if (splitClassName.length > 0) {
//			// classFileName = splitClassName[splitClassName.length - 1] +
//			// ".class";
//			classFileName =
//				splitClassName[splitClassName.length - 1] + BasicLanguageDependencyManager.getBinaryFileSuffix();
//
//		} else {
//			classFileName = className + ".class";
//		}
//
//		return new File(classFolder, classFileName);
	}
	
	public static File getClassFile(File buildFolder, String className) {

		File classFolder = buildFolder;
		String[] splitClassName = className.split("\\.");
		for (int i = 0; i < splitClassName.length - 1; i++) {
			String packagePart = splitClassName[i];
			classFolder = new File(classFolder, packagePart);
		}

		String classFileName;
		if (splitClassName.length > 0) {
			// classFileName = splitClassName[splitClassName.length - 1] +
			// ".class";
			classFileName =
				splitClassName[splitClassName.length - 1] + BasicLanguageDependencyManager.getBinaryFileSuffix();

		} else {
			classFileName = className + ".class";
		}

		return new File(classFolder, classFileName);
	}

	/**
	 * Given a .java and .class file, returns whether the .java file needs to be
	 * compiled or recompiled
	 *
	 * @param javaFile
	 *            The Java file
	 * @param classFile
	 *            The class file
	 * @return boolean true if should compile/recompile false otherwise
	 */
	private boolean shouldCompile(File javaFile, File classFile) {
		// System.out.println("Class time:" + classFile.lastModified() + "
		// source time:" + javaFile.lastModified());
		String javaName = javaFile.getName();
		String className = classFile.getName();
		return !hasBeenCompiled()
			// !project.hasBeenCompiled()
			&& !classFile.getName().startsWith("_") &&
			!javaFile.getName().startsWith("._") &&
			(BasicGradingEnvironment.get().isForceCompile()
				|| !classFile.exists()
				|| classFile.lastModified() < javaFile.lastModified());
		// (classFile.lastModified() - javaFile.lastModified()) < 1000;

	}

	
	// not clear this is ever used
	// @Override
	public List<String> getClassNamesToCompile() {
		return classNamesToCompile;
	}
	// not reallt used
	// @Override
	public void setClassNamesToCompile(List<String> classNamesToCompile) {
		this.classNamesToCompile = classNamesToCompile;
	}



}

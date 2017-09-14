package framework.project;

import framework.execution.ARunningProject;
import grader.basics.BasicLanguageDependencyManager;
import grader.basics.execution.BasicProjectExecution;
import grader.basics.execution.RunningProject;
import grader.basics.project.BasicClassDescription;
import grader.basics.project.BasicProjectClassesManager;
import grader.basics.project.ClassDescription;
import grader.basics.project.ClassesManager;
import grader.basics.settings.BasicGradingEnvironment;
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

	

	protected void setProject(Object aProject) {
		project = (SakaiProject) aProject;
	}

	public ProjectClassesManager(SakaiProject aProject, File buildFolder, File sourceFolder, String aSourceFilePattern)
		throws IOException,
		ClassNotFoundException {
		super(aProject, buildFolder, sourceFolder, aSourceFilePattern);
		// project = aProject;
		// Set the build and source folders for the project
		// this.buildFolder = buildFolder;
		// this.sourceFolder = sourceFolder;
		//
		// // Create the Class Loader and load the classes
		// if (AProject.isLoadClasses() ) {
		//// classLoader = project.getClassLoader();
		//// if (classLoader == null)
		// if (project != null) {
		// proxyClassLoader = project.getClassLoader();
		// }
		// classLoader = new URLClassLoader(new
		// URL[]{buildFolder.toURI().toURL()});
		// }
		// classDescriptions = new HashSet<ClassDescription>();
		//
		// loadClasses(sourceFolder);
		// checkStyle(project, sourceFolder);
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
		project.getClassLoader().setBinaryFileSystemFolderName(buildFolder.getAbsolutePath());
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
		if (!BasicGradingEnvironment.get().isCheckStyle())
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
		if (BasicGradingEnvironment.get().isCompileMissingObjectCode()
			|| BasicGradingEnvironment.get().isForceCompile()
			|| BasicGradingEnvironment.get().isPreCompileMissingObjectCode()) {

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
					&& !BasicGradingEnvironment.get().isPreCompileMissingObjectCode()) {
					return;
				}
				try {
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
						.compile(sourceFolder, buildFolder, aFilesToCompile);
					if (runningProject != null) {
						String anErrors = runningProject.getErrorOutput();
						 String outputAndErrors =
						 runningProject.getOutputAndErrors();
//						 runningProject.appendOutputAndErrorsToTranscriptFile(project);
						appendOutputAndErrorsToTranscriptFile(runningProject);

					} else {
						System.err.println ("Compiler could not be started");
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
		// if (project != null) {
		// project.setHasBeenLoaded(true);
		// project.setCanBeLoaded(true);
		// }
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
				.getSourceFilesCompiler().compile(sourceFolder,
					buildFolder, recompiledFileList);
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

	// /**
	// * Given an ArrayList of .javaFiles, returns whether the .java file needs
	// to
	// * be compiled or recompiled
	// *
	// * @param javaFiles
	// * ArrayList of .java files
	// * @throws IOException
	// */
	//// private void compile(ArrayList<File> javaFiles) throws IOException,
	// IllegalStateException {
	////
	//// JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
	//// if (compiler != null) {
	//// StandardJavaFileManager fileManager =
	// compiler.getStandardFileManager(null, null, null);
	////
	//// List<String> optionList = new ArrayList<String>();
	//// // set the output directory for the compiler
	//// String buildFolderPath = buildFolder.getCanonicalPath();
	//// optionList.addAll(Arrays.asList("-d", buildFolderPath));
	//// System.out.println(buildFolderPath);
	////
	//// Iterable<? extends JavaFileObject> compilationUnits = fileManager
	//// .getJavaFileObjectsFromFiles(javaFiles);
	//// compiler.getTask(null, fileManager, null, optionList, null,
	// compilationUnits).call();
	//// for (File javaFile:javaFiles) {
	//// SourceFileCompiled.newCase(javaFile.getAbsolutePath(), this);
	////
	//// }
	//// } else {
	////// throw new RuntimeException("Compiler not accessible");
	//// throw CompilerNotFound.newCase(this);
	//// }
	//// }
	// @Override
	// public ClassLoader getClassLoader() {
	// return classLoader;
	// }
	//
	// /**
	// * Looks for a class description given a class name (simple or canonical)
	// *
	// * @param className The name of the class to find
	// * @return The class description wrapped in a {@link Option} in case none
	// * was found.
	// */
	// @Override
	// public List<ClassDescription> findByClassName(String className) {
	// List<ClassDescription> classes = new ArrayList<>();
	//
	// // First search the simple names
	// for (ClassDescription description : classDescriptions) {
	// if
	// (description.getJavaClass().getSimpleName().equalsIgnoreCase(className))
	// {
	// classes.add(description);
	// }
	// }
	//
	// // Next search the canonical names
	// for (ClassDescription description : classDescriptions) {
	// if
	// (description.getJavaClass().getCanonicalName().equalsIgnoreCase(className))
	// {
	// classes.add(description);
	// }
	// }
	// return classes;
	// }
	//
	// @Override
	// public List<ClassDescription> findByClassNameMatch(String className) {
	// List<ClassDescription> classes = new ArrayList<>();
	// if (className == null) return classes;
	//
	//// // First search the simple names
	//// for (ClassDescription description : classDescriptions) {
	//// if (description.getJavaClass().getSimpleName().matches(className)) {
	//// classes.add(description);
	//// }
	//// }
	//// if (!classes.isEmpty())
	//// return classes;
	// // search the canonical names
	// for (ClassDescription description : classDescriptions) {
	// if (description.getJavaClass().getCanonicalName().matches(className)) {
	// classes.add(description);
	// }
	// }
	// return classes;
	// }
	// @Override
	// public List<ClassDescription> findClass (String aName, String aTag,
	// String aNameMatch, String aTagMatch) {
	// List<ClassDescription> result = new ArrayList();
	// if (aTag != null)
	// result = findClassByTag(aTag);
	// if (aTag != null && result.isEmpty())
	// result = findClassByPattern(aTag);
	// if (!result.isEmpty())
	// return result;
	// if (aName != null)
	// result = findByClassName(aName);
	// if (!result.isEmpty())
	// return result;
	//
	//// if (aTag != null)
	//// result = findClassByTag(aTag);
	//// if (aTag != null && result.isEmpty())
	//// result = findClassByPattern(aTag);
	//// if (!result.isEmpty())
	//// return result;
	// if (aNameMatch != null) {
	// result = findByClassNameMatch(aNameMatch);
	// }
	// if (!result.isEmpty())
	// return result;
	// if (aTagMatch != null) {
	// result = findByTagMatch(aTagMatch);
	// }
	// return result;
	// }
	//
	//
	// /**
	// * Looks for all class descriptions with a particular tag
	// *
	// * @param tag The tag to search for
	// * @return The set of matching class descriptions
	// */
	// @Override
	// public List<ClassDescription> findClassByTag(String tag) {
	// String normalizedTag = tag.replaceAll("\\s","");
	// List<ClassDescription> classes = new ArrayList<>();
	// for (ClassDescription description : classDescriptions) {
	//// if (description.getJavaClass().isInterface())
	//// continue;
	// for (String t : description.getTags()) {
	// String aNormalizedActualTag = t.replaceAll("\\s","");
	//// if (t.equalsIgnoreCase(tag)) {
	//
	// if (aNormalizedActualTag.equalsIgnoreCase(normalizedTag)) {
	// classes.add(description);
	// }
	// }
	// }
	// return classes;
	// }
	//
	// @Override
	// public List<ClassDescription> findClassByPattern(String tag) {
	// List<ClassDescription> classes = new ArrayList<>();
	// for (ClassDescription description : classDescriptions) {
	//// if (description.getJavaClass().isInterface())
	//// continue;
	// String aPattern = description.getStructurePatternName();
	//
	// if (tag.equalsIgnoreCase(aPattern)) {
	// classes.add(description);
	// }
	//
	// }
	// return classes;
	// }
	//
	// /**
	// * Looks for all class descriptions with a particular tag
	// *
	// * @param tag The tag to search for
	// * @return The set of matching class descriptions
	// */
	// @Override
	// public List<ClassDescription> findByTagMatch(String regex) {
	// List<ClassDescription> classes = new ArrayList<>();
	// for (ClassDescription description : classDescriptions) {
	// for (String t : description.getTags()) {
	// if (t.matches(regex)) {
	// classes.add(description);
	// }
	// }
	// }
	// return classes;
	// }
	//
	// /**
	// * @return All class descriptions
	// */
	// @Override
	// public Set<ClassDescription> getClassDescriptions() {
	// return classDescriptions;
	// }
	//
	// @Override
	public List<String> getClassNamesToCompile() {
		return classNamesToCompile;
	}

	// @Override
	public void setClassNamesToCompile(List<String> classNamesToCompile) {
		this.classNamesToCompile = classNamesToCompile;
	}
//	@Override
//	public boolean isHasCompileErrors() {
//		return hasCompileErrors;
//	}
//	@Override
//	public void setHasCompileErrors(boolean hasCompileErrors) {
//		this.hasCompileErrors = hasCompileErrors;
//	}


}

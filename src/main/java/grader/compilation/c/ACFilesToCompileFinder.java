package grader.compilation.c;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import framework.project.ProjectClassesManager;
import grader.basics.execution.BasicProcessRunner;
import grader.basics.execution.BasicProjectExecution;
import grader.basics.execution.RunningProject;
import grader.basics.project.BasicProjectClassesManager;
import grader.basics.project.Project;
import grader.basics.settings.BasicGradingEnvironment;
import grader.compilation.AFilesToCompileFinder;
import grader.compilation.FilesToCompileFinder;
import grader.config.ExecutionSpecificationSelector;
import grader.language.LanguageDependencyManager;
import grader.navigation.NavigationKind;
import grader.settings.GraderSettingsModelSelector;
import util.trace.Tracer;

public class ACFilesToCompileFinder extends AFilesToCompileFinder implements FilesToCompileFinder {
	
	
	/**
	 * Given a .java and .class file, returns whether the .java file needs to be
	 * compiled or recompiled
	 * @param aproject TODO
	 * @param javaFile
	 *            The Java file
	 * @param classFile
	 *            The class file
	 *
	 * @return boolean true if should compile/recompile false otherwise
	 */
	
	@Override
	public List<File> filesToCompile(Project aProject, File aBuildFolder, Set<File> sourceFiles) throws IOException {
			// Check if files need to be compiled		

			ArrayList<File> aFilesToCompile = new ArrayList<File>();
			String anExecutable = 	BasicProcessRunner.getMainEntryPoint(aProject, null);
			// It will never be main, it will be: BasicProcessRunner.MAIN_ENTRY_POINT
			File anExecutableFile = anExecutable == null?null:new File(aBuildFolder.getAbsoluteFile()+"//" + anExecutable);
			for (File file : sourceFiles) {
				String className = BasicProjectClassesManager.getClassName(file);
//				File classFile = ProjectClassesManager.getClassFile(aBuildFolder, className);
				if (shouldCompile(file, anExecutableFile)) {
					aFilesToCompile.add(file);
				}
			}
			return aFilesToCompile;
	
	}

}

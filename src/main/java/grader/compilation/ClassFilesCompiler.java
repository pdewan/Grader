package grader.compilation;

import grader.basics.execution.RunningProject;
import grader.basics.project.Project;

import java.io.File;
import java.io.IOException;
import java.util.List;

public interface ClassFilesCompiler {
	public RunningProject compile(Project aBasicProject, File sourceFolder, File buildFolder, File anObjectFolder, List<File> sourceFiles) throws IOException, IllegalStateException;


}

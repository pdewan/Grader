package grader.compilation;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Set;

import grader.basics.project.Project;

public interface FilesToCompileFinder {

	List<File> filesToCompile(Project aProject, File aBuildFolder, Set<File> sourceFiles) throws IOException;

}

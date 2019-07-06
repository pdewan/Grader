package grader.compilation.c;

import grader.compilation.AFilesToCompileFinder;
import grader.compilation.FilesToCompileFinder;

public class CFilesToCompileFinderSelector {
	static FilesToCompileFinder filesToCompileFinder ;

	public static FilesToCompileFinder getOrCreateFilesToCompileFinder() {
		if (filesToCompileFinder == null) {
			filesToCompileFinder = new ACFilesToCompileFinder();
		}
		return filesToCompileFinder;
	}

	public static void setFilesToCompileFinder(FilesToCompileFinder filedToCompileFinder) {
		CFilesToCompileFinderSelector.filesToCompileFinder = filedToCompileFinder;
	}
	

}

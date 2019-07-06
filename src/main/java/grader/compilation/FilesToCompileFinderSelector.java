package grader.compilation;

public class FilesToCompileFinderSelector {
	static FilesToCompileFinder filesToCompileFinder ;

	public static FilesToCompileFinder getOrCreateFilesToCompileFinder() {
		if (filesToCompileFinder == null) {
			filesToCompileFinder = new AFilesToCompileFinder();
		}
		return filesToCompileFinder;
	}

	public static void setFilesToCompileFinder(FilesToCompileFinder filedToCompileFinder) {
		FilesToCompileFinderSelector.filesToCompileFinder = filedToCompileFinder;
	}
	

}

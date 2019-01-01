package grader.file;

public interface RootFolderCreator {
	public  RootFolderProxy createRootFolder(String aFolder, String[] aLazilyFetchSubFoldersOf, String[] anIgnoreFiles);
}
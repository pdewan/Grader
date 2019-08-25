package grader.project.folder;

import java.util.List;
import java.util.Set;

import grader.basics.file.FileProxy;
import grader.basics.file.RootFolderProxy;

public interface RootCodeFolder {
    FileProxy sourceFile(String aClassName);

    FileProxy binaryFile(String aClassName);

    String getAbsoluteName();
    
    String getMixedCaseAbsoluteName();

    String getLocalName();

    List<FileProxy> getFileEntries();

    FileProxy getFileEntry(String name);

    public String getSourceProjectFolderName();

    public String getMixedCaseSourceProjectFolderName();

    public String getBinaryProjectFolderName();

    Set<String> getEntryNames();

    RootFolderProxy getRootFolder();

    String getProjectFolderName();

    public boolean hasSource();

    public boolean hasBinary();

    public boolean hasSeparateSourceBinary();

    public boolean hasValidBinaryFolder();

	RootFolderProxy getSourceFolder();

	RootFolderProxy getBinaryFolder();

	boolean isZippedFolder();


}

package grader.file;

import java.util.List;
import java.util.Set;

public interface RootFolderProxy {
    String getAbsoluteName();

    String getLocalName();

    public String getMixedCaseAbsoluteName();

    public String getMixedCaseLocalName();

    List<FileProxy> getFileEntries();

    FileProxy getFileEntry(String name);

    Set<String> getEntryNames();

    public Set<String> getChildrenNames();

    Set<String> getDescendentEntryNames(FileProxy aParent);

    boolean isDirectory();

    public FileProxy getFileEntryFromLocalName(String name);

    boolean exists();

    public List<FileProxy> getChildrenOf(String aParentName);

	void clear();
   



}

package grader.file;

import java.util.HashSet;
import java.util.Set;

import util.misc.Common;

public abstract class AnAbstractProxy implements RootFolderProxy{
    Set<String> descendentNames;
    Set<String> childrenNames = new HashSet();

    public Set<String> getChildrenNames() {
        return childrenNames;
    }
   

}

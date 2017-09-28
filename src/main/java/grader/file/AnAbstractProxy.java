package grader.file;

import java.util.HashSet;
import java.util.Set;

public abstract class AnAbstractProxy implements RootFolderProxy{
    Set<String> descendentNames;
    Set<String> childrenNames = new HashSet();

    public Set<String> getChildrenNames() {
        return childrenNames;
    }
    @Override
    public void clear() {
    	if (descendentNames != null)
    		descendentNames.clear();
    	childrenNames.clear();
    	
    }

}

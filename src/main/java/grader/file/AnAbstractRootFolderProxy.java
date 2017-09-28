package grader.file;

import grader.trace.file.load.RootFolderProxyLoaded;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import util.misc.Common;

public abstract class AnAbstractRootFolderProxy extends AnAbstractProxy implements RootFolderProxy {
    protected Map<String, FileProxy> nameToFileProxy = new HashMap();
    protected List<FileProxy> entries = new ArrayList();
    
    protected  String subFolderName; // only children of this folder will be viisted and put in nameToFileProxy;
    protected  String subFolderNameLowerCase;
    
    public AnAbstractRootFolderProxy(String aSubFolderName) {
    	subFolderName = aSubFolderName;
    	if (subFolderName != null)
    		subFolderNameLowerCase = subFolderName.toLowerCase();
    	
    }
    @Override
    public void clear() {
    	entries.clear();
    	nameToFileProxy.clear();
    }
    
    protected boolean inTreeOfSubFolder(String anEntryName) {
    	return subFolderNameLowerCase == null || 
    			anEntryName.toLowerCase().contains(subFolderNameLowerCase);
    	
    }

    protected void add(FileProxy aFileProxy) {
        entries.add(aFileProxy);
        nameToFileProxy.put(aFileProxy.getAbsoluteName().toLowerCase(), aFileProxy);
        nameToFileProxy.put(aFileProxy.getAbsoluteName(), aFileProxy); // added this for Unix systems

    }

    @Override
    public List<FileProxy> getFileEntries() {
        return entries;
    }

    @Override
    public Set<String> getEntryNames() {
        return nameToFileProxy.keySet();
    }

    public List<FileProxy> getChildrenOf(String aParentName) {
        String myName = aParentName.toLowerCase();
        int parentDepth = Common.numMiddleOccurences(myName, '/');

        List<FileProxy> retVal = new ArrayList();
        for (FileProxy entry : entries) {
            String childName = entry.getAbsoluteName();
            if (!childName.startsWith(myName)) continue;
            int childDepth = Common.numMiddleOccurences(childName, '/');

            if (childDepth == parentDepth + 1) {
                retVal.add(entry);
            }
        }
        return retVal;

    }

    @Override
    public Set<String> getDescendentEntryNames(FileProxy aParent) {
        String parentName = aParent.getAbsoluteName();
        Set<String> allChildren = getEntryNames();
        Set<String> retVal = new HashSet();
        for (String name : allChildren) {
            if (name.startsWith(parentName))
                retVal.add(name);
        }
        return retVal;
    }

    public FileProxy getFileEntryFromLocalName(String name) {
        return getFileEntry(getAbsoluteName() + "/" + name);
    }

    public boolean isDirectory() {
        return true;
    }

    protected void initChildrenRootData() {
        for (FileProxy entry : entries) {
            entry.initRootData();
            String entryName = entry.getLocalName();
            if (entryName == null) continue;
            int index1 = entryName.indexOf('/');
            int index2 = entryName.indexOf('\\');// use file separator
            int index3 = entryName.indexOf(File.separator);
            if (index1 == -1 && index2 == -1 && index3 == -1)
                childrenNames.add(entry.getAbsoluteName());
        }
        RootFolderProxyLoaded.newCase(getAbsoluteName(), this);
    }
    boolean debug;


    public FileProxy getFileEntryFromArg(String aName) {
    	if (aName == null) {
    		System.out.println("Null file entry returning null proxy");
    		return null;
    	}
//    	String aCanonicalName = aName.toLowerCase(); // sometimes this gives an exception
    	String aCanonicalName = aName;

//        return nameToFileProxy.get(name.toLowerCase());
    	FileProxy retVal = nameToFileProxy.get(aCanonicalName);
    	return retVal;
//    	if (retVal == null && debug) {
//    		Set<String> keys = nameToFileProxy.keySet();
//    		for (String key:keys) {
//    			System.out.println("comparing" + key + " and\n" + aCanonicalName);
//    			if (key.equals(aCanonicalName)) return nameToFileProxy.get(key);
//    		}
//    	}
//    	return retVal;
    }
    public FileProxy getFileEntry(String aName) {
    	FileProxy retVal = getFileEntryFromArg(aName);
    	if (retVal != null) 
    	
    		return retVal;
    	if (aName == null) {
    		System.err.println ("Null aName!!");
    		return null;
    	}
    	String aCanonicalName = aName.toLowerCase();
    	retVal = getFileEntryFromArg(aCanonicalName);
    	
//        return nameToFileProxy.get(name.toLowerCase());
    	if (retVal == null && debug) {
    		Set<String> keys = nameToFileProxy.keySet();
    		for (String key:keys) {
    			System.out.println("comparing" + key + " and\n" + aCanonicalName);
    			if (key.equals(aCanonicalName)) return nameToFileProxy.get(key);
    		}
    	}
    	return retVal;
    }
    
//    public FileProxy getFileEntry(String aName) {
//    	if (aName == null) {
//    		System.out.println("Null file entry returning null proxy");
//    		return null;
//    	}
//    	String aCanonicalName = aName.toLowerCase(); // sometimes this gives an exception
////        return nameToFileProxy.get(name.toLowerCase());
//    	FileProxy retVal = nameToFileProxy.get(aCanonicalName);
//    	if (retVal == null && debug) {
//    		Set<String> keys = nameToFileProxy.keySet();
//    		for (String key:keys) {
//    			System.out.println("comparing" + key + " and\n" + aCanonicalName);
//    			if (key.equals(aCanonicalName)) return nameToFileProxy.get(key);
//    		}
//    	}
//    	return retVal;
//    }

}

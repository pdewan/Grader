package wrappers.grader.file;

import grader.basics.util.DirectoryUtils;
import grader.file.FileProxy;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import util.misc.Common;

/**
 * Created with IntelliJ IDEA.
 * User: josh
 * Date: 11/9/13
 * Time: 5:30 PM
 * To change this template use File | Settings | File Templates.
 */
public class SimplifiedFileProxy implements FileProxy {

    private File file;

    public SimplifiedFileProxy(File file) {
        this.file = file;
    }

    @Override
    public String getAbsoluteName() {
//        return file.getAbsolutePath();
        return Common.toCanonicalFileName(file.getAbsolutePath());
    }

    @Override
    public String getLocalName() {
        return file.getName();
    }

    @Override
    public String getMixedCaseAbsoluteName() {
//        return file.getAbsolutePath();
        return Common.toCanonicalFileName(file.getAbsolutePath());
    }

    @Override
    public String getMixedCaseLocalName() {
        return file.getName();
    }

    @Override
    public List<FileProxy> getFileEntries() {
        Set<File> entries = DirectoryUtils.getFiles(file, new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                return true;
            }
        });
        List<FileProxy> entryProxies = new ArrayList<FileProxy>();
        for (File entry : entries) {
            entryProxies.add(new SimplifiedFileProxy(entry));
        }
        return entryProxies;
    }

    @Override
    public FileProxy getFileEntry(String name) {
        return new SimplifiedFileProxy(new File(name));
    }

    @Override
    public long getTime() {
        return file.lastModified();
    }

    @Override
    public long getSize() {
        return file.length();
    }

    @Override
    public InputStream getInputStream() {
        try {
            return new FileInputStream(file);
        } catch (FileNotFoundException e) {
            return null;
        }
    }

    @Override
    public OutputStream getOutputStream() {
        try {
            return new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            return null;
        }
    }

    @Override
    public Set<String> getEntryNames() {
        Set<File> files = DirectoryUtils.getFiles(file, new FileFilter() {
            @Override
            public boolean accept(File f) {
                return true;
            }
        });
        Set<String> names = new HashSet<String>();
        for (File f : files) {
            names.add(Common.toCanonicalFileName(f.getAbsolutePath()));
        }
        return names;
    }

    @Override
    public void initRootData() {
        //To change body of implemented methods use File | Settings | File Templates.
        System.out.println("**** TODO: initRootData");
    }

    @Override
    public Set<String> getChildrenNames() {
        Set<String> names = new HashSet<String>();
        for (File f : file.listFiles()) {
            names.add(Common.toCanonicalFileName(f.getAbsolutePath()));
        }
        return names;
    }

    @Override
    public Set<String> getDescendentEntryNames(FileProxy aParent) {
        Set<File> files = DirectoryUtils.getFiles(file, new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                return true;
            }
        });
        Set<String> names = new HashSet<String>();
        for (File f : files) {
            names.add(Common.toCanonicalFileName(f.getAbsolutePath()));
        }
        return names;
    }

    @Override
    public boolean isDirectory() {
        return file.isDirectory();
    }

    @Override
    public FileProxy getFileEntryFromLocalName(String name) {
        return new SimplifiedFileProxy(new File(file, name));
    }

    @Override
    public boolean exists() {
        return file.exists();
    }

    @Override
    public List<FileProxy> getChildrenOf(String aParentName) {
        System.out.println("**** TODO: getChildrenOf");
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public FileProxy getParentFolder() {
        return new SimplifiedFileProxy(file.getParentFile());
    }

    public File getFile() {
        return file;
    }

	@Override
	public String getParentRelativeName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String displayTree() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<FileProxy> getChildren() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getParentFolderName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getParentRelativeMixedCaseName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void clear() {
		// TODO Auto-generated method stub
		
	}
}

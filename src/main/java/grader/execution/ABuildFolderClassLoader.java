package grader.execution;

import grader.basics.file.FileProxy;
import grader.basics.file.filesystem.AFileSystemFileProxy;
import grader.project.flexible.FlexibleProject;
import grader.project.folder.RootCodeFolder;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ABuildFolderClassLoader extends ClassLoader  {
	File buildFolder;
    
    List<Class> classesLoaded = new ArrayList();

    

    public ABuildFolderClassLoader(File aBuildFolder) {
    	buildFolder = aBuildFolder;
    }

   
   
    protected InputStream getInputStreamOfClass(String aFileName) {
    	// refetch the file as we may have recompiled classes
		File classFile = new File(buildFolder, aFileName);
		if (classFile.exists()) {
			try {
				return  new FileInputStream(classFile);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
				return null;
			}
		} else {
	    	return null;
		}
    	
    }

    public Class findClass(String aClassName) throws ClassNotFoundException {
        try {
        	
            byte classBytes[];
            String aFileName = aClassName.replaceAll("\\.", "/") + ".class";
//            String aFullFileName = binaryFolderName + "/" + aFileName;
//            FileProxy classFile = rootCodeFolder.getFileEntry(aFullFileName);
//            InputStream inputStream = classFile.getInputStream();
            InputStream inputStream = getInputStreamOfClass(aFileName);
            classBytes = new byte[inputStream.available()];
            inputStream.read(classBytes);
            Class classObject = defineClass(aClassName, classBytes, 0, classBytes.length);
            return classObject;
        } catch (Exception e) {
            return null;
        }
    }

    public Class loadClass(String aClassName) throws ClassNotFoundException {
    	try {
        return super.loadClass(aClassName);
    	} catch (Exception e) {
    		return null;
    	}
    }

    
}

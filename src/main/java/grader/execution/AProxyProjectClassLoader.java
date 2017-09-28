package grader.execution;

import grader.file.FileProxy;
import grader.file.filesystem.AFileSystemFileProxy;
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

public class AProxyProjectClassLoader extends ClassLoader implements ProxyClassLoader {
    public static final String CLASS_MAIN = "main.finalAssignment";

    RootCodeFolder rootCodeFolder;
    String projectFolderName, binaryFolderName, binaryFileSystemFolderName;
    

	String sourceFolderMixedCaseName;
    List<Class> classesLoaded = new ArrayList();
    
    Map<String, Class> dynamicallyCompiledClass = new HashMap();

    public AProxyProjectClassLoader(RootCodeFolder aRootCodeFolder) {
        init(aRootCodeFolder);
    }

    void init(RootCodeFolder aRootCodeFolder) {
        rootCodeFolder = aRootCodeFolder;
        binaryFolderName = rootCodeFolder.getBinaryProjectFolderName();
        if (rootCodeFolder.getBinaryFolder() != null && rootCodeFolder.getBinaryFolder() instanceof AFileSystemFileProxy) {
        binaryFileSystemFolderName = rootCodeFolder.getBinaryFolder().getMixedCaseAbsoluteName();
        } 
//        else if (rootCodeFolder.getSourceFolder() != null){
//        	binaryFolderMixedCaseName = rootCodeFolder.getSourceFolder().
//        			getMixedCaseAbsoluteName().replaceAll(
//        					"/" + ARootCodeFolder.SOURCE + "/", 
//        					"/" + ARootCodeFolder.BINARY + "/");
//        }
        projectFolderName = rootCodeFolder.getProjectFolderName();
//        if (projectFolderName == null)
//        	projectFolderName = binaryFolderName; // bin folder was missing so bin folder became project folder and hence this confusion
    }

    public InputStream getResourceAsStream(String name) {
        InputStream retVal = super.getResourceAsStream(name);
        if (retVal != null)
            return retVal;
        String aFullFileName = projectFolderName + "/" + name;
        FileProxy fileProxy = rootCodeFolder.getRootFolder().getFileEntry(aFullFileName);
        InputStream inputStream = fileProxy.getInputStream();
        return inputStream;
    }
    @Override
    public Collection<Class> getDynamicallyCompiledClasses() {
    	return dynamicallyCompiledClass.values();
    }
    @Override
    public Class defineDynamicallyCompiledClass (String aClassName, byte[] aBytes) {
    		Class aClass = dynamicallyCompiledClass.get(aClassName); 
    		if (aClass == null)
    	
    			aClass = super.defineClass(aClassName, aBytes, 0, aBytes.length);
            if (aClass != null)
            	dynamicallyCompiledClass.put(aClassName, aClass);
            return aClass;
        
    }
    
    InputStream getInputStreamOfClass(String aFileName) {
    	// refetch the file as we may have recompiled classes
		File classFile = new File(binaryFileSystemFolderName, aFileName);
		if (classFile.exists()) {
			try {
				return  new FileInputStream(classFile);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
				return null;
			}
		} else {
    	
             String aFullFileName = binaryFolderName + "/" + aFileName;
             FileProxy classFileProxy = rootCodeFolder.getFileEntry(aFullFileName);
            return classFileProxy.getInputStream();
		}
    	
    }

    public Class findClass(String aClassName) throws ClassNotFoundException {
        try {
        	Class retVal = dynamicallyCompiledClass.get(aClassName);
        	if (retVal != null)
        		return retVal;
            byte classBytes[];
            String aFileName = aClassName.replaceAll("\\.", "/") + ".class";
//            String aFullFileName = binaryFolderName + "/" + aFileName;
//            FileProxy classFile = rootCodeFolder.getFileEntry(aFullFileName);
//            InputStream inputStream = classFile.getInputStream();
            InputStream inputStream = getInputStreamOfClass(aFileName);
            classBytes = new byte[inputStream.available()];
            inputStream.read(classBytes);
            Class classObject = defineClass(aClassName, classBytes, 0, classBytes.length);
            classesLoaded.add(classObject);
            return classObject;
        } catch (Exception e) {
            return null;
        }
    }
    public void clear() {
    	classesLoaded.clear();
    }
    public Class loadClass(String aClassName) throws ClassNotFoundException {
    	try {
        return super.loadClass(aClassName);
    	} catch (Exception e) {
    		return null;
    	}
    }

    public static void run(FlexibleProject aProject, String mainClassName) {
        try {
            ClassLoader classLoader = new AProxyProjectClassLoader(aProject.getRootCodeFolder());
            Class mainClass = classLoader.loadClass(mainClassName);
            Method mainMethod = mainClass.getMethod("main", String[].class);
            String[] strings = {"move 10 100"};
            Object[] myArgs = {strings};
            mainMethod.invoke(mainClass, myArgs);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    public String getBinaryFileSystemFolderName() {
		return binaryFileSystemFolderName;
	}
    @Override
	public void setBinaryFileSystemFolderName(String binaryFileSystemFolderName) {
		this.binaryFileSystemFolderName = binaryFileSystemFolderName;
	}

    public List<Class> getClassesLoaded() {
        return classesLoaded;
    }
}

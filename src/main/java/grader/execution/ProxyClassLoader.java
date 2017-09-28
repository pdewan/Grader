package grader.execution;

import java.util.Collection;
import java.util.List;

public interface ProxyClassLoader {
    public Class findClass(String aClassName) throws ClassNotFoundException;

    public Class loadClass(String aClassName) throws ClassNotFoundException;

    public List<Class> getClassesLoaded();

	Collection<Class> getDynamicallyCompiledClasses();

	Class defineDynamicallyCompiledClass(String aClassName, byte[] aBytes);

	String getBinaryFileSystemFolderName();

	void setBinaryFileSystemFolderName(String binaryFileSystemFolderName);
	void clear();


}

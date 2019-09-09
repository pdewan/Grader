package framework.execution.reflectionObjects;

import grader.basics.project.ClassDescription;
import grader.basics.project.ClassesManager;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: josh
 * Date: 10/7/13
 * Time: 11:54 AM
 * To change this template use File | Settings | File Templates.
 */
public class ManualClassesManager implements ClassesManager {

    private Set<ClassDescription> classDescriptions;

    public ManualClassesManager(Class<?> ... classes) {
        classDescriptions = new HashSet<ClassDescription>();
        for (Class<?> _class : classes)
            classDescriptions.add(new ManualClassDescription(_class));
    }

    @Override
    public ClassLoader getClassLoader() {
        return ClassLoader.getSystemClassLoader();
    }


    @Override
    public Set<ClassDescription> findByTag(String tag) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Set<ClassDescription> getClassDescriptions() {
        return classDescriptions;
    }

//	@Override
	public List<String> getClassNamesToCompile() {
		// TODO Auto-generated method stub
		return null;
	}

//	@Override
	public void setClassNamesToCompile(List<String> classNamesToCompile) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Set<ClassDescription> findByClassOrInterfaceName(String className) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<ClassDescription> findByTagMatch(String regex) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<ClassDescription> findByClassOrInterfaceNameMatch(String className) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<ClassDescription> findClassesAndInterfaces(String aName, String aTag,
			String aNameMatch, String aTagMatch) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<ClassDescription> findByPattern(String tag) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<ClassDescription> findByTag(String[] aTags) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<ClassDescription> findClassAndInterfaces(String aName, String[] aTag,
			String aNameMatch, String aTagMatch) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<ClassDescription> findBySupertypes(Class[] anInterfaces) {
		// TODO Auto-generated method stub
		return null;
	}

	
}

package grader.project.flexible;

import grader.basics.util.GraderFileUtils;
import grader.language.LanguageDependencyManager;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import util.misc.Common;

// this makes the class descriptions providing the text to each class
// It does not seem to use the proxy
public class AFlexibleClassesManager implements FlexibleClassesManager {
    Map<String, FlexibleClassDescription> classNameToDescription = new HashMap();
    Map<String, Set<FlexibleClassDescription>> tagToDescription = new HashMap();

    List<FlexibleClassDescription> classDescriptions = new ArrayList();
    final int ESTIMATED_SOURCES_LENGTH = 20;
//    final String SOURCE_FILE_SUFFIX = ".java";

    /* (non-Javadoc)
     * @see grader.project.ClassesManager#getClassNameToDescription()
     */
    @Override
    public Map<String, FlexibleClassDescription> getClassNameToDescription() {
        return classNameToDescription;
    }

    /* (non-Javadoc)
     * @see grader.project.ClassesManager#setClassNameToDescription(java.util.Map)
     */
    @Override
    public void setClassNameToDescription(
            Map<String, FlexibleClassDescription> classNameToDescription) {
        this.classNameToDescription = classNameToDescription;
    }

    /* (non-Javadoc)
     * @see grader.project.ClassesManager#getClassDescriptions()
     */
    @Override
    public List<FlexibleClassDescription> getClassDescriptions() {
        return classDescriptions;
    }

    /* (non-Javadoc)
     * @see grader.project.ClassesManager#setClassDescriptions(java.util.List)
     */
    @Override
    public void setClassDescriptions(
            List<FlexibleClassDescription> classDescriptions) {
        this.classDescriptions = classDescriptions;
    }

    /* (non-Javadoc)
     * @see grader.project.ClassesManager#put(java.lang.String, grader.project.ClassDescription)
     */
    @Override
    public void put(String aClassName, FlexibleClassDescription aClass) {
        classNameToDescription.put(aClassName, aClass);
        classDescriptions.add(aClass);
    }
    @Override
	public void clear() {
		classNameToDescription.clear();
		classDescriptions.clear();
		
	}

    /* (non-Javadoc)
     * @see grader.project.ClassesManager#put(java.lang.String[], grader.project.ClassDescription)
     */
    @Override
    public void put(String[] aTags, FlexibleClassDescription aClass) {
        for (String tag : aTags) {
            putTag(tag, aClass);
        }
    }

    /* (non-Javadoc)
     * @see grader.project.ClassesManager#tagToClassDescriptions(java.lang.String)
     */
    @Override
    public Set<FlexibleClassDescription> tagToClassDescriptions(String aTag) {
        return tagToDescription.get(aTag);
    }
    
    @Override
    public FlexibleClassDescription tagToUniqueClassDescription(String aTag) {
        Set<FlexibleClassDescription>  aClassDescriptions = tagToDescription.get(aTag);
        if (aClassDescriptions == null || aClassDescriptions.size() == 0) {
			throw NoClassWithTag.newCase(this, aTag);				 
		} else if (aClassDescriptions.size() > 1) {
			throw MultipleClassesWithTag.newCase(this, aTag);
		}
		for (FlexibleClassDescription aClassDescription:aClassDescriptions) {
			return aClassDescription;
			
		}
		
		return null;
        
    }
    @Override
    public FlexibleClassDescription tagsToUniqueClassDescription(List<String> aTags) {
		if (aTags == null)
			return null;		
		
			Set<FlexibleClassDescription> aClassDescriptions = new HashSet<>();
			aClassDescriptions.addAll(getClassDescriptions());
			for (String aTag:aTags) {
				Set<FlexibleClassDescription> aCurrentSet = tagToClassDescriptions(aTag);
				if (aCurrentSet == null)
					aClassDescriptions.clear();
				else
				    aClassDescriptions.retainAll(aCurrentSet);
			}			
		
			if (aClassDescriptions == null || aClassDescriptions.size() == 0) {
				throw NoClassWithTag.newCase(this, aTags);				 
			} else if (aClassDescriptions.size() > 1) {
				 MultipleClassesWithTag.newCase(this, aTags); // do not give up
			}
			
			int minTags = Integer.MAX_VALUE;
			FlexibleClassDescription retVal = null;
			// find the class with fewest tags that matches aTags
			for (FlexibleClassDescription aClassDescription:aClassDescriptions) {
				String[] aCurrentTags = aClassDescription.getTags();
				if (aCurrentTags.length < minTags) {
				 retVal = aClassDescription;
				 minTags = aCurrentTags.length;
				}				
			}
			
			return retVal;
	}
    


    /* (non-Javadoc)
     * @see grader.project.ClassesManager#tagsToClassDescriptions(java.lang.String[])
     */
    @Override
    public Set<FlexibleClassDescription> tagsToClassDescriptions(String[] aTagList) {
        Set<FlexibleClassDescription> retVal = new HashSet<>();
        for (String tag : aTagList) {
            retVal.addAll(tagToClassDescriptions(tag));
        }
        return retVal;
    }

    /* (non-Javadoc)
     * @see grader.project.ClassesManager#putTag(java.lang.String, grader.project.ClassDescription)
     */
    @Override
    public void putTag(String aTag, FlexibleClassDescription aClass) {
        Set<FlexibleClassDescription> classes = tagToDescription.get(aTag);
        if (classes == null) {
            classes = new HashSet<>();

            // Added by Josh. The classes object is never added to the tagToDescription map
            tagToDescription.put(aTag, classes);
        }
        classes.add(aClass);


    }

    /* (non-Javadoc)
     * @see grader.project.ClassesManager#classNameToClassDescription(java.lang.String)
     */
    @Override
    public FlexibleClassDescription classNameToClassDescription(String aClassName) {
        return classNameToDescription.get(aClassName);
    }

    /* (non-Javadoc)
     * @see grader.project.ClassesManager#getTags(grader.project.ClassDescription)
     */
    @Override
    public String[] getTags(FlexibleClassDescription aClassDescription) {
        return aClassDescription.getTags();
    }

    /* (non-Javadoc)
     * @see grader.project.ClassesManager#makeSourceCodeDescriptions(java.lang.String, boolean)
     */
    // this is fle stuff, should not be her
    @Override
    public void makeClassDescriptions(String aProjectDirectory, boolean aSeparateSrcBin) {
        String anActualProjectDirectory = aProjectDirectory;
        if (aSeparateSrcBin)
            anActualProjectDirectory += "/src";
        List<FlexibleClassDescription> sources = new ArrayList<>(ESTIMATED_SOURCES_LENGTH);
        File projectFoider = new File(anActualProjectDirectory);
        makeClassDescriptions(projectFoider, projectFoider);
    }

    /* (non-Javadoc)
     * @see grader.project.ClassesManager#makeClassDescriptions(java.io.File, java.io.File)
     */
    void makeClassDescriptions(File aFolder, File aProjectFolder) {
        String[] fileNames = aFolder.list();
        File[] files = aFolder.listFiles();
        for (File aFile : files) {
            if (aFile.getName().endsWith(LanguageDependencyManager.getSourceFileSuffix())) {

//            if (aFile.getName().endsWith(SOURCE_FILE_SUFFIX)) {
        //System.out.println(this.getClass().getName());
                String relativeName = GraderFileUtils.toRelativeName(aProjectFolder.getAbsolutePath(), aFile.getAbsolutePath());
                String className = Common.projectRelativeNameToClassName(relativeName);
                StringBuffer text = Common.toText(aFile.getAbsolutePath());
                FlexibleClassDescription classDescription = new AClassDescription(className, text, aFile.lastModified(), null, null, null);
                put(className, classDescription);

                // Added by Josh: The tag to class description map is never added to. This is doing just that.
                put(classDescription.getTags(), classDescription);
            }
        }

        for (File aFile : files) {
            if (aFile.isDirectory()) {
                makeClassDescriptions(aFile, aProjectFolder);
            }
        }
    }

	
}

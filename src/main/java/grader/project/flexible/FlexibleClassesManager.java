package grader.project.flexible;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface FlexibleClassesManager {

    public Map<String, FlexibleClassDescription> getClassNameToDescription();

    public void setClassNameToDescription(
            Map<String, FlexibleClassDescription> classNameToDescription);

    public List<FlexibleClassDescription> getClassDescriptions();

    public void setClassDescriptions(List<FlexibleClassDescription> classDescriptions);

    public void put(String aClassName, FlexibleClassDescription aClass);

    public void put(String[] aTags, FlexibleClassDescription aClass);

    public Set<FlexibleClassDescription> tagToClassDescriptions(String aTag);

    public Set<FlexibleClassDescription> tagsToClassDescriptions(String[] aTagList);

    public void putTag(String aTag, FlexibleClassDescription aClass);

    public FlexibleClassDescription classNameToClassDescription(String aClassName);

    public String[] getTags(FlexibleClassDescription aClassDescription);

    public void makeClassDescriptions(String aProjectDirectory, boolean aSeparateSrcBin);

	FlexibleClassDescription tagToUniqueClassDescription(String aTag);

	FlexibleClassDescription tagsToUniqueClassDescription(List<String> aTags);
	void clear();

}
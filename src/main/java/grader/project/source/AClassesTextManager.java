package grader.project.source;


import grader.basics.util.GraderFileUtils;
import grader.project.view.ClassViewManager;
import grader.project.view.ViewableClassDescription;
import grader.sakai.project.SakaiProject;
import grader.trace.source.SourceFileComputed;
import grader.trace.source.SourceFileLoaded;
import grader.trace.source.SourceFileSaved;

import java.io.File;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import util.misc.Common;


public class AClassesTextManager implements ClassesTextManager {
    public static final String SOURCE_SUFFIX = "//END OF FILE\n";
    public static final String SOURCE_PREFIX = "//START OF FILE: ";
    public static final int MAX_FILE_NAME_LENGTH = 100;
    Map<String, StringBuffer> views = new HashMap();
    StringBuffer allSourcesText;
    ClassViewManager classesManager;
//    String sourceFileNamePrefix = SOURCE_PREFIX;
//    String sourceFileNameSuffix = SOURCE_SUFFIX;
//    String sourceFileName = sourceFileNamePrefix + sourceFileNameSuffix; 
//    Project project;


    public AClassesTextManager(ClassViewManager aClassesManager) {
        classesManager = aClassesManager;
//        sourceFileNameSuffix = aProject.getSourceSuffix();
    }

    /* (non-Javadoc)
     * @see grader.project.ClassesSourceManager#writeAllSourcesText()
     */
    @Override
    public void writeAllSourcesText(String aFileName) {
        try {
        	File sourceFile = new File(aFileName);
        	if (sourceFile.exists()) return;
            PrintWriter out = new PrintWriter(aFileName);
            String allText = getAllSourcesText().toString();
            out.print(allText);
            out.close();
            SourceFileComputed.newCase(aFileName, allText, this);
        } catch (Exception e) {
//            e.printStackTrace(); // Commented out by Josh
        }
    }
    
    @Override
    public void setEditedAllSourcesText(String aFileName, String newValue) {
        try {
        	
            PrintWriter out = new PrintWriter(aFileName);
            out.print(newValue);
            out.close();
            SourceFileSaved.newCase(aFileName, newValue, this);

        } catch (Exception e) {
//            e.printStackTrace(); // Commented out by Josh
        }
    }
    @Override
    public String getEditedAllSourcesText(String aFileName) {
    	File sourceFile = new File(aFileName);
    	if (!sourceFile.exists()) writeAllSourcesText(aFileName);
//    	return Common.toText(aFileName).toString();
    	
    	String retVal = Common.toText(aFileName).toString();
        SourceFileLoaded.newCase(aFileName, retVal, this);
        return retVal;

    }

//    @Override
//    public void writeAllSourcesText() {
////        writeAllSourcesText(DEFAULT_SOURCES_FILE_NAME);
//        writeAllSourcesText(sourceFileName);
//
//    }

    /* (non-Javadoc)
     * @see grader.project.ClassesSourceManager#initializeAllSourcesText()
     */
    @Override
    public void initializeAllSourcesText() {
        Collection<ViewableClassDescription> filteredClasses = classesManager.getViewableClassDescriptions();
        allSourcesText = toStringBuffer(filteredClasses);
    }

    @Override
    public StringBuffer toStringBuffer(Collection<ViewableClassDescription> sourceClasses) {
        int totalTextSize = totalTextSize(sourceClasses) + sourceClasses.size() * (SOURCE_SUFFIX.length() + SOURCE_PREFIX.length() + MAX_FILE_NAME_LENGTH);
        StringBuffer retVal = new StringBuffer(totalTextSize);
        for (ViewableClassDescription viewable : sourceClasses) {
        	SakaiProject aProject =  (SakaiProject) viewable.getClassDescription().getProject();
        	String aProjectFolderName = aProject.getProjectFolderName();
        //System.out.println(this.getClass().getName());
        	String aLocalName = GraderFileUtils.toRelativeName(aProjectFolderName, viewable.getClassDescription().getSourceFile().getAbsoluteName());
//        	String fileName = viewable.getClassDescription().getSourceFile().getParentRelativeName();
//        	String prefix = SOURCE_PREFIX + fileName + "\n";
        	String prefix = SOURCE_PREFIX + aLocalName + "\n";
        	retVal.append(prefix);
            retVal.append(viewable.getClassDescription().getText());
            retVal.append(SOURCE_SUFFIX);
        }
        return retVal;
    }

    /* (non-Javadoc)
     * @see grader.project.ClassesSourceManager#totalTextSize(java.util.Collection)
     */
    public int totalTextSize(Collection<ViewableClassDescription> aSourceClasses) {
        int retVal = 0;
        for (ViewableClassDescription viewable : aSourceClasses) {
            retVal += viewable.getClassDescription().getText().length();
        }
        return retVal;
    }

    /* (non-Javadoc)
     * @see grader.project.ClassesSourceManager#getAllSourcesText()
     */
    @Override
    public StringBuffer getAllSourcesText() {
        if (allSourcesText == null)
            initializeAllSourcesText();
        return allSourcesText;
    }

    /* (non-Javadoc)
     * @see grader.project.ClassesSourceManager#setAllSourcesText(java.lang.StringBuffer)
     */
    @Override
    public void setAllSourcesText(StringBuffer anAllSourcesText) {
        allSourcesText = allSourcesText;
    }

}

package grader.project.flexible;

import grader.basics.file.FileProxy;
import grader.basics.settings.BasicGradingEnvironment;
import grader.basics.util.GraderFileUtils;
import grader.execution.ProxyBasedClassesManager;
import grader.execution.ProxyClassLoader;
import grader.language.LanguageDependencyManager;
import grader.project.folder.RootCodeFolder;

import java.util.List;

import util.misc.Common;

// this makes the class descriptions providing the text to each class
// It uses the file proxy
public class AProxyBasedClassesManager extends AFlexibleClassesManager implements ProxyBasedClassesManager {

    /* (non-Javadoc)
     * @see grader.project.ClassesManager#makeSourceCodeDescriptions(java.lang.String, boolean)
     */
    // ignoring second parameter
    @Override
    public void makeClassDescriptions(String aProjectDirectory, boolean aSeparateSrcBin) {
        FlexibleProject project = new AFlexibleProject(aProjectDirectory);
        makeClassDescriptions(project);
    }

    public void makeClassDescriptions(FlexibleProject aProject) {
//    	RootCodeFolder aRootCodeFolder = aProject.getRootCodeFolder();
    	
        List<FileProxy> entries = aProject.getRootCodeFolder().getFileEntries();
        String projectPath = aProject.getRootCodeFolder().getAbsoluteName();
        ProxyClassLoader classLoader = null;
        // we no longer need this check as we are allowing classes to be loaded before running
//        if (aProject.canBeRun() && aProject.hasBeenRun()) {
        if (BasicGradingEnvironment.get().isLoadClasses())
            classLoader = aProject.getClassLoader();
//        }
        makeClassDescriptions(aProject.getSourceProjectFolderName(), entries, classLoader, aProject);
    }

    /* (non-Javadoc)
     * @see grader.project.ClassesManager#makeClassDescriptions(java.io.File, java.io.File)
     */
    void makeClassDescriptions(String srcFolderName, List<FileProxy> aFiles, ProxyClassLoader aClassLoder, FlexibleProject aProject) {
        for (FileProxy aFile : aFiles) {
            String locaName = aFile.getMixedCaseLocalName();

            if (locaName != null && locaName.endsWith(LanguageDependencyManager.getSourceFileSuffix())) {
//                if (locaName != null && locaName.endsWith(SOURCE_FILE_SUFFIX)) {

        //System.out.println(this.getClass().getName());
                String relativeName = GraderFileUtils.toRelativeName(srcFolderName, aFile.getMixedCaseAbsoluteName());
                String className = Common.projectRelativeNameToClassName(relativeName);
                StringBuffer text = Common.toText(aFile.getInputStream());
                FlexibleClassDescription classDescription = new AClassDescription(className, text, aFile.getTime(), aClassLoder, aProject, aFile);
                put(className, classDescription);

                // Added by Josh: The tag to class description map is never added to. This is doing just that.
                put(classDescription.getTags(), classDescription);
            }
        }
    }
}

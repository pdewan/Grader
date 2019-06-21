package framework.utils;

import grader.basics.settings.BasicGradingEnvironment;
import grader.config.StaticConfigurationUtils;

/**
 * A singleton that investigates the machine for certain things. It looks for:
 * <ul>
 *     <li>Operating System</li>
 *     <li>Text Editor (OS specific)</li>
 *     <li>File Browser (OS specific)</li>
 *     <li>Classpath (OS specific because Windows delimits with ';' rather than ':'</li>
 * </ul>
 */
public class GradingEnvironment extends BasicGradingEnvironment {

//    private static final String[] macEditors = new String[]{
//        "/Applications/Sublime Text 2.app/Contents/SharedSupport/bin/subl",
//        "/usr/local/bin/edit"
//    };
//
//    private static final String[] linuxEditors = new String[]{
//        "/usr/local/bin/gvim",
//        "/usr/local/bin/emacs",
//        "/usr/local/bin/gedit",
//    };
//
//    private static final String[] windowsEditors = new String[]{
//        "C:\\Program Files\\Sublime Text 2\\sublime_text.exe",
//        "C:\\Program Files (x86)\\Sublime Text 2\\sublime_text.exe",
//        "C:\\Program Files\\Notepad++\\notepad++.exe",
//        "C:\\Program Files (x86)\\Notepad++\\notepad++.exe",
//        "notepad"
//    };
//    String userName;
//   
//
//	private String osName;
//    private String editor;
//    protected String diff;
//    private String browser;
//    private String classpath, canonicalClassPath, oeClassPath, canonicalOEClassPath;
//    private String assignmentName;
//    private String defaulAssignmentsDataFolderName;
////    ConfigurationManager configurationManager;  // maybe it does not belong here
//
//	
//
//	public String getDefaultAssignmentsDataFolderName() {
//		return defaulAssignmentsDataFolderName;
//	}
//
//	public void setDefaultAssignmentsDataFolderName(
//			String newVal) {
//		this.defaulAssignmentsDataFolderName = newVal;
//	}
//	public boolean isNotWindows() {
//		return !osName.equals("Windows");
//	}
//
//	private GradingEnvironment() {
//        osName = System.getProperty("os.name");
//        userName = System.getProperty("user.name");
//        if (osName.equals("Mac OS X")) {
//            osName = "Mac";
//            browser = "open";
//            editor = findEditor(macEditors);
//            classpath = findClasspath(":");
//        } else if (osName.equals("Linux")) {
//            browser = "nautilus";
//            editor = findEditor(linuxEditors);
//            classpath = findClasspath(":");
//        } else {
//            osName = "Windows";
//            browser = "explorer";
//            editor = findEditor(windowsEditors);
//            classpath = findClasspath(";");
//        }
//    }
//
//    private static String findEditor(String[] editors) {
//        for (String editor : editors) {
//            if (new File(editor).exists())
//                return editor;
//        }
//        return "";
//    }
//
//    @Override
//	protected  String findClasspath(String separator) {
//    	String systemClassPath = System.getenv("CLASSPATH");
//    	String myClassPath = System.getProperty("java.class.path");
//    	String originalClassPath = systemClassPath;
//        File oe = new File("oeall-22.jar");
////        String[] paths = new String[] { ".", "..", oe.getAbsolutePath()};
//        String[] paths = new String[] { ".", "..", myClassPath};
//
//        String classpath = "";
//        for (String path : paths)
//            classpath += (classpath.isEmpty() ? "" : separator) + path;
//        if (osName.equals("Windows"))
//        	classpath = "\""+ classpath + "\"";
//        else
//        	classpath = classpath.replaceAll(" ", "\\ ");
//        return classpath;
//    }
//    public  String toOSClassPath(String aCanoicalClassPath) {
//    	if (osName.equals("Windows"))
//        	return "\""+ aCanoicalClassPath + "\"";
//        else
//        	return aCanoicalClassPath.replaceAll(" ", "\\ ");
////        return aCanoicalClassPath;
//    }
//    protected String findOEClassPath(String separator) {
//    	String myClassPath = System.getProperty("java.class.path");
//    	String[] paths = myClassPath.split(separator);
//    	for (String aPath:paths) {
//    		if (aPath.contains("oeall")) {
//    			return aPath;
//    		}
//    	}
//    	return null;
//    }
	public GradingEnvironment() {
		super();
		nativeExecution = false; // invoked through Driver
	}
	@Override
	// class paths will be set by Driver
	protected void maybeSetClasspaths() {
//		setClasspaths();
	}
	// this one uses static configuration utils
    // this is overriding the superclass
	// why does superclass have this method, do we not rely on the programming environment
	// for the class path?
	// does forked process need some of this?
	@Override
    protected  String findSystemClasspathAndSetAssociatedClasspaths(String separator) {
    	String systemClassPath = System.getenv("CLASSPATH");
    	if(systemClassPath==null) {
    		systemClassPath="";
    	}

    	
    	
    	//should we not call the parent class methods here?
    	oeClassPath = findOEClassPath(separator); 
  	    junitClassPath = findJUnitClassPath(separator);
  	    localGraderClassPath = findLocalGraderClassPath(separator);


// added this stuff below to replace the commented code below
    	String[] paths = null;
    	/*
    	 * This makes sense only for Java
    	 */
    	if (StaticConfigurationUtils.hasClassPath() ) {
    		canonicalClassPath = systemClassPath;    		
    	}
    	// getting rid of this from here, execution command will convert to osPath
//    	String anOSClassPath = toOSClassPath(systemClassPath);
//    	classpath = anOSClassPath;
    	classpath = systemClassPath;
    	return systemClassPath;
    			
//    	return anOSClassPath; // at some pt get rid if this return value
    	//the stuff below is a mess but may need to be uncommented

//    	if (StaticConfigurationUtils.hasClassPath() ) {
//    		paths = new String[] { ".", "..", systemClassPath};
//    	} 
    	// will add oe classpath separately
//    	else if (StaticConfigurationUtils.hasOEClassPath()) {
//    		paths = new String[] { ".", "..", canonicalOEClassPath};
//    	} else if (StaticConfigurationUtils.hasOEOrClassPath()) {
//    		paths = new String[] { ".", "..", canonicalOEClassPath, systemClassPath};
//    	}
//    	
//
//
//
//        String classpath = "";
//        
//        for (String path : paths)
//            classpath += (classpath.isEmpty() ? "" : separator) + path;
//        canonicalClassPath = classpath;
//        classpath = toOSClassPath(canonicalClassPath);
////        if (osName.equals("Windows"))
////        	classpath = "\""+ classpath + "\"";
////        else
////        	classpath = classpath.replaceAll(" ", "\\ ");
//        return classpath;
    }
//    
//
//    public String getClasspath() {
//        return classpath;
//    }
//    public String getCanonicalClasspath() {
//        return canonicalClassPath;
//    }
//
//    public void setEditor(String editor) {
//        this.editor = editor;
//    }
//
//    public String getEditor() {
//        return editor;
//    }
//    
//    public String getDiff() {
//        return diff;
//    }
//    public void setDiff(String diff) {
//        this.diff = diff;
//    }
//
//    public String getOsName() {
//        return osName;
//    }
//    public String getUserName() {
//		return userName;
//	}
//
//	public void setUserName(String userName) {
//		this.userName = userName;
//	}
//
//    /**
//     * Opens a directory in the file browser
//     * @param file The directory
//     */
//    public void open(File file) {
//        try {
//            new ProcessBuilder(browser, file.getAbsolutePath()).start();
//        } catch (IOException e) {
//            System.out.println("Can't open file");
//            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
//        }
//    }
//    
//    private void editSubFiles(File folder) {
//    	File[] subFiles = folder.listFiles();
//    	for (File subFile: subFiles) {
//    		if (subFile.isDirectory()) {
//    			editSubFiles(subFile);
//    		} else {
//    			edit(subFile);
//    		}
//    	}
//    }
//
//    /**
//     * Edits a directory or file in the text editor
//     * @param file The directory or file
//     */
//    public void edit(File file) {
//
//    	if (file.isDirectory() && osName.equals("Linux")) {
//    		editSubFiles(file);
//    		return;
//    	}
//    	
//        try {
//            new ProcessBuilder(editor, file.getAbsolutePath()).start();
//        } catch (IOException e) {
//            System.out.println("Can't edit file/folder");
//            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
//        }
//    }
//
//    public String getAssignmentName() {
//        return assignmentName;
//    }
//
//    public void setAssignmentName(String assignmentName) {
//        this.assignmentName = assignmentName;
//    }
//
//    // Singleton methods
//    private static GradingEnvironment singleton = null;
//
//    public static GradingEnvironment get() {
//        if (singleton == null)
//            singleton = new GradingEnvironment();
//        return singleton;
//    }
////    public ConfigurationManager getConfigurationManager() {
////		return configurationManager;
////	}
////
////	public void setConfigurationManager(ConfigurationManager configurationManager) {
////		this.configurationManager = configurationManager;
////	}

}

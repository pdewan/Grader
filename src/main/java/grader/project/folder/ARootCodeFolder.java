package grader.project.folder;

import grader.basics.file.FileProxy;
import grader.basics.file.RootFolderProxy;
import grader.basics.file.zipfile.AZippedRootFolderProxy;
import grader.basics.project.Project;
import grader.basics.trace.BinaryFolderNotFound;
import grader.basics.trace.ProjectFolderNotFound;
import grader.basics.trace.SourceFolderAssumed;
import grader.basics.trace.SourceFolderNotFound;
import grader.language.LanguageDependencyManager;
import grader.trace.project.BinaryFolderIdentified;
import grader.trace.project.SourceFolderIdentified;

import java.io.File;
import java.nio.file.Paths;
//a root folder containing source and binary directories
import java.util.List;
import java.util.Set;

import util.misc.Common;

public class ARootCodeFolder implements RootCodeFolder {

//    public static final String SOURCE = "src";
//    public static final String BINARY = "bin";
//    public static final String BINARY_2 = "out";
//    public static final String BINARY_3 = "build"; // net beans
//	 static  String sourceFileSuffix = ".java";
//	 static Map<String, String> languageToSourceFileSuffix = new HashMap<>();
//	 static Map<String, String> languageToBinaryFileSuffix = new HashMap<>();
//	 static Map<String, MainClassFinder> languageToMainClassFinder = new HashMap();
//	 static Map<String, ClassFilesCompiler> languageToCompiler = new HashMap();
//
//	 static String language;
//		
//		public static String JAVA_LANGUAGE = "Java";
//		public static String C_LANGUAGE = "C";
//
//	
//	public static  String binaryFileSuffix = ".class";

    String sourceFolderName = Project.SOURCE;
    String binaryFolderName = Project.BINARY;
    RootFolderProxy root;
    String projectFolder;
    // changing sourceFolder to rootfolderproxy from FileProxy as we do not need the more general type
    RootFolderProxy sourceFolder, binaryFolder;
//	FileProxy sourceFolder, binaryFolder;

    boolean separateSourceBinary = true;
    boolean hasSource;
    boolean hasBinary;
    boolean hasSourceFile;
    boolean hasBinaryFile;

    public ARootCodeFolder(RootFolderProxy aRoot, String aSourceFolder, String aBinaryFolder) {
        root = aRoot;
        sourceFolderName = aSourceFolder;
        binaryFolderName = aBinaryFolder;
        SourceFolderIdentified.newCase(sourceFolderName, this);
        BinaryFolderIdentified.newCase(binaryFolderName, this);
        
//        System.out.println("&&&& " + sourceFolderName);
//        System.out.println("&&&& " + binaryFolderName);

    }

    String findParentOfSomeSourceFile(RootFolderProxy aRoot) { // will  not work with packages
        List<FileProxy> entries = aRoot.getFileEntries();
        for (FileProxy aFile : entries) {
//			if (aFile.getAbsoluteName().endsWith(sourceFileSuffix)) {
            if (aFile.getAbsoluteName().endsWith(LanguageDependencyManager.getSourceFileSuffix())) {

                return aFile.getParentFolderName();
            }
        }
        return null;

    }

    public ARootCodeFolder(RootFolderProxy aRoot) {
//		if (aRoot.getAbsoluteName().indexOf("erichman") != -1) {
//			System.out.println (" found erichman");
//		}
        // this code is a bit of a mess, need to clean this up
        root = aRoot;
        setSeparateSourceBinary();
        // should we not be doing this only if there is a separate source and binary?
        sourceFolderName = getFolderWithName(aRoot, Project.SOURCE);
//        sourceFolderName = getEntryWithSuffixAndrew(aRoot, SOURCE);
        for (String aBinary:Project.BINARIES) {
        	binaryFolderName = getFolderWithName(aRoot, aBinary);
        	if (binaryFolderName != null) {
        		break;
        	}
        }
//        binaryFolderName = getFolderWithName(aRoot, Project.BINARY);
////        binaryFolderName = getEntryWithSuffixAndrew(aRoot, BINARY);
//        // allow a set here
//        if (binaryFolderName == null) {
//            binaryFolderName = getFolderWithName(aRoot, Project.BINARY_2);
////            binaryFolderName = getEntryWithSuffixAndrew(aRoot, BINARY_2);
//        }
//        if (binaryFolderName == null) {
//            binaryFolderName = getFolderWithName(aRoot, Project.BINARY_3);
////            binaryFolderName = getEntryWithSuffixAndrew(aRoot, BINARY_3);
//        }
//        if (binaryFolderName == null) {
//            binaryFolderName = getFolderWithName(aRoot, Project.BINARY_4);
////            binaryFolderName = getEntryWithSuffixAndrew(aRoot, BINARY_3);
//        }
//		if (sourceFolderName == null || binaryFolderName == null) {
        if (sourceFolderName == null && binaryFolderName == null) {
            System.out.println(SourceFolderNotFound.newCase(root.getLocalName(), this).getMessage());
            sourceFolderName = findParentOfSomeSourceFile(aRoot);
            if (sourceFolderName != null) {
                SourceFolderAssumed.newCase(sourceFolderName, this);

//			sourceFolderName = sourceFolder.getAbsoluteName();
                binaryFolderName = sourceFolderName;
            } else {
                throw ProjectFolderNotFound.newCase(aRoot.getLocalName(), this);

//		sourceFolderName = aRoot.getAbsoluteName();
//		binaryFolderName = sourceFolderName;
//		// this should go
//		if (separateSourceBinary) {
//			sourceFolderName +=  "/" + SOURCE;
//			binaryFolderName += "/" + BINARY;
//			
//		}
            }
        }

        if (separateSourceBinary) {
        	
            if (hasValidBinaryFolder()) {
                projectFolder = Common.getParentFileName(binaryFolderName);
                sourceFolder = root.getFileEntry(sourceFolderName);
//			if (sourceFolder ==null ) {
//				System.out.println("All children" + root.getEntryNames());
//				System.out.println("not found:" + sourceFolderName);
//			}
                binaryFolder = root.getFileEntry(binaryFolderName);
            } else {
            	if (sourceFolderName != null && binaryFolderName == null) {// this will always be true?
            		 sourceFolder = root.getFileEntry(sourceFolderName);
                     projectFolder = Common.getParentFileName(sourceFolderName);

            		binaryFolder = null; 
            		// Assuming that binary folder will be made later by dynamic compilation
//            		binaryFolderName = sourceFolderName.replace("/src/", "/" + BINARY + "/");
            		binaryFolderName = null;
            		// make binary folder
            		
            	} else if (binaryFolderName == null) { // ever reach this?
                binaryFolder = root; // will this cause problems?
                binaryFolderName = root.getAbsoluteName();
                projectFolder = binaryFolderName;
            	}
            }
        } else if (sourceFolderName != null){
            projectFolder = binaryFolderName;
            //added this.
            sourceFolder = root.getFileEntry(sourceFolderName + "/"); //no idea whey I need sometimes ending backslash, need to debu
            if (sourceFolder == null) {
            	sourceFolder = root.getFileEntry(sourceFolderName);
//            	binaryFolder = sourceFolder;
            }
            binaryFolder = sourceFolder;
//			sourceFolder = root;
        } else {
        	System.err.println("No Source folder found in:" + aRoot);
        }

        SourceFolderIdentified.newCase(sourceFolderName, this);
        if (hasValidBinaryFolder()) {
//        if (hasBinaryFile) {
            BinaryFolderIdentified.newCase(binaryFolderName, this);
        } else {
            BinaryFolderNotFound.newCase(root.getAbsoluteName(), this);
        }

//        System.out.println("&&& " + sourceFolderName);
//        System.out.println("&&& " + binaryFolderName);
    }

    public boolean hasValidBinaryFolder() {
    	return binaryFolderName != null;
//        return hasBinaryFile;
//        return true; // we are not recursing to file level now
    }
    /**
     * Assumes that language is constant for all projects
     */
    void setSeparateSourceBinary() {
        Set<String> names = root.getEntryNames();
//        String srcPattern = Project.SOURCE + "/";
        String srcPattern = "/" + Project.SOURCE;

        String binPattern = "/" + Project.BINARY;
//        String binPattern = Project.BINARY + "/";

        for (String name : names) {
//            if (!hasSource && name.indexOf(srcPattern) != -1) {
            if (!hasSource && name.endsWith(srcPattern)) {
	
                hasSource = true;
            }
//            if (!hasBinary && name.indexOf(binPattern) != -1) {
            if (!hasBinary && name.endsWith(binPattern)) {

                hasBinary = true;
            }
//			if (!hasSourceFile && name.indexOf(SOURCE_FILE_SUFFIX) != -1) {
            if (!hasSourceFile && name.endsWith(LanguageDependencyManager.getSourceFileSuffix())) {

                hasSourceFile = true;
            }
//			if (!hasBinaryFile && name.indexOf(BINARY_FILE_SUFFIX) != -1) {// .classpath will fool this
            if (!hasBinaryFile && name.endsWith(LanguageDependencyManager.getBinaryFileSuffix())) {

                hasBinaryFile = true;
            }
            if (hasSource && hasBinary && hasSourceFile && hasBinaryFile) {
                break;
            }
        }
        separateSourceBinary = hasSource || hasBinary;

    }

    public String toString() {
        return root.getLocalName();
    }
//	public static String getEntryWithSuffix (RootFolderProxy aRoot, String suffix) {
//		Set<String> nameSet = aRoot.getEntryNames();
//		for (String name:nameSet) {
//			int index = name.indexOf(suffix);
//			if (index < 0)
//				continue;
//			
//			// if name ends with suffix we should proceed, or if suffix/ is an intermediate directory in zip file path we should proceed
//			if (!name.endsWith(suffix) && name.indexOf(suffix + "/") < 0)
////			if (!(name.endsWith(suffix)|| name.indexOf(suffix + "/") >= 0))
//
//				continue; // in case src and bin are not followed by / and are in intermediate directories
////			if (name.charAt(0) == '_')
////				continue;
////			if (name.indexOf("_macos") != -1)
////				continue;
//			FileProxy proxy = aRoot.getFileEntry(name);
//			String mixedCaseProxy = proxy.getMixedCaseAbsoluteName();
//			if (name.endsWith(suffix))
////			return name.substring(0, index + suffix.length());
//			return mixedCaseProxy.substring(0, index + suffix.length());
//			else {
//				return mixedCaseProxy.substring(0, index + suffix.length());
//
////			if (name.endsWith(suffix))
////				return name;
//		}
//		return null;
//	}
    
    public static String getEntryWithSuffixAndrew(RootFolderProxy aRoot, String suffix) {
        Set<String> nameSet = aRoot.getEntryNames();
        String zipPath = "";
        for (String path : nameSet) {
            if (path.contains(suffix + System.getProperty("file.separator"))) {
                System.out.println(path);
                String folderPath = path.substring(0, path.lastIndexOf(suffix) + suffix.length());
                if (path.contains(".zip")) {
                    zipPath = folderPath;
                } else {
                    return folderPath;
                }
            } else if (path.endsWith(suffix) || path.endsWith(suffix + System.getProperty("file.separator"))) {
                System.out.println(path);
                return path;
            }
        }
        if (!zipPath.isEmpty()) {
            return zipPath;
        }
//        Set<String> nameSet = aRoot.getEntryNames();
//        String zipSuffix = "";
//        for (String name : nameSet) {
//            String filename = Paths.get(name).getFileName().toString();
//            if (filename.endsWith(suffix)
//                    || filename.endsWith(suffix + System.getProperty("file.separator"))) {
//                if (name.contains(".zip")) {
//                    zipSuffix = name;
//                } else {
//                    return name;
//                }
//            }
//        }
//        if (!zipSuffix.isEmpty()) {
//            return zipSuffix;
//        }
        return null;
    }
		/*
     * We get names of files, not directories, for zips
     * for file system, we get both
     * So a bin must be extracted from file names.
     * Two cases: b
     */

    public static String getFolderWithName(RootFolderProxy aRoot, String aName) {
//    public static String getEntryWithSuffix(RootFolderProxy aRoot, String suffix) {
    	if (aRoot == null) {
    		return null;
    	}
    	String rootName = Common.toCanonicalFileName(aRoot.getAbsoluteName().toLowerCase());
        Set<String> nameSet = aRoot.getEntryNames();
        String separator = "/";
        for (String name : nameSet) {
//        	if (name.indexOf ("__MACOSX") != -1)
//        		continue;
            
            int index = name.indexOf(aName);
            if (index < 0) {
                continue;
            }
            // Jacob's solution to the intermediate node problem
            while(!name.equals(rootName)) {
                String filename = Paths.get(name).getFileName().toString();
                if (filename.endsWith(aName)
                        || filename.endsWith(aName + System.getProperty("path.separator"))) {
//                    if (name.contains(".zip")) {
//                        zipSuffix = name;
//                    } else {
                        return name;
//                    }
                }
                File file = new File(name);
                name = Common.toCanonicalFileName(file.getParent());
            }
            // pd's solution to the intermediate node problem - if his fails try this
//            int intermediateIndex = name.indexOf(aName + "/");
            int intermediateIndex = name.indexOf(aName + separator);
            boolean nameEndsWithSuffix = name.length() == index + aName.length();
            // if name ends with suffix we should proceed, or if suffix/ is an intermediate directory in zip file path we should proceed
//            if (!name.endsWith(suffix) && name.indexOf(suffix + "/") < 0)
//            if (!(name.endsWith(suffix)|| name.indexOf(suffix + "/") >= 0))
            if (intermediateIndex < 0 // not an intermediate directory)
                    && !nameEndsWithSuffix) { // not an end name
                continue; // in case src and bin are not followed by / and are in intermediate directories
            }
//            if (name.charAt(0) == '_') {
//                continue;
//            }
//            if (name.indexOf("_macos") != -1) {
//		  continue;
//            }        
            FileProxy proxy = aRoot.getFileEntry(name);
            String mixedCaseProxy = proxy.getMixedCaseAbsoluteName();
            if (nameEndsWithSuffix) //				return name.substring(0, index + suffix.length());
            {
                return mixedCaseProxy.substring(0, index + aName.length());
            } else {
                return mixedCaseProxy.substring(0, intermediateIndex + aName.length());
            }

//	  if (name.endsWith(suffix))
//	  return name;
        }
        return null;
    }

    @Override
    public FileProxy sourceFile(String aClassName) {
        String sourceFileName = Common.classNameToSourceFileName(aClassName);
        if (separateSourceBinary) {
            sourceFileName = Project.SOURCE + "/" + sourceFileName;
        }
        return root.getFileEntry(sourceFileName);
    }

    @Override
    public RootFolderProxy getRootFolder() {
        return root;
    }

    @Override
    public String getProjectFolderName() {
        return projectFolder;
    }

    @Override
    public FileProxy binaryFile(String aClassName) {
        String binaryFileName = Common.classNameToBinaryFileName(aClassName);
        if (separateSourceBinary) {
            binaryFileName += Project.BINARY + "/" + binaryFileName;
        }
        return root.getFileEntry(binaryFileName);
    }

    @Override
    public String getAbsoluteName() {
        // TODO Auto-generated method stub
        return root.getAbsoluteName();
    }
    
    @Override
    public String getMixedCaseAbsoluteName() {
        // TODO Auto-generated method stub
        return root.getMixedCaseAbsoluteName();
    }
    

    @Override
    public String getLocalName() {
        // TODO Auto-generated method stub
        return root.getAbsoluteName();
    }
    
    protected boolean maybeInitializeDescendents(FileProxy aFileProxy) {
//		System.out.println("maybe initializing children of " + aFileProxy);

    	if (!aFileProxy.getFile().isDirectory()) {
    		return false;
    	}
		if (!aFileProxy.isDescendentsInitialized()) {
//			System.out.println(" initializing children of " + aFileProxy);

			RootFolderProxy aRoot = aFileProxy.getRootFolderProxy();
			aRoot.initEntries(aFileProxy.getFile());
			aRoot.initChildrenRootData(aFileProxy);
			return true;
		} else {
	    	boolean retVal = false;

			for (FileProxy aChildProxy: aFileProxy.getChildren()) {
				retVal = retVal || maybeInitializeDescendents(aChildProxy);
			}
			return retVal;
		}
    }
    protected boolean maybeInitializeDescendents(List<FileProxy> aFileProxies) {
		
	    	boolean retVal = false;

			for (FileProxy aChildProxy: aFileProxies) {
				if (!aChildProxy.isDescendentsInitialized()) {
//					System.out.println(" initializing children of " + aChildProxy);

					RootFolderProxy aRoot = aChildProxy.getRootFolderProxy();
					aRoot.initEntries(aChildProxy.getFile());
					aRoot.initChildrenRootData(aChildProxy);
					retVal = true;
				}
			}
			return retVal;
		}
    
    protected boolean initializedDescendents = false;

    @Override
    public List<FileProxy> getFileEntries() {
    	List<FileProxy> aFileEntries = root.getFileEntries();
    	List<FileProxy> retVal = aFileEntries;
    	if (!initializedDescendents && root instanceof FileProxy) {	    	
    			if (maybeInitializeDescendents(aFileEntries)) {
    				FileProxy aFileProxy = (FileProxy) root;    			    
    				aFileProxy.getRootFolderProxy().initChildrenRootData(aFileProxy); 
    				retVal = root.getFileEntries();
    			};
    		
    	}
    	initializedDescendents = true;
        // TODO Auto-generated method stub
//        return root.getFileEntries();
        return retVal;

    }

    @Override
    public FileProxy getFileEntry(String name) {
        // TODO Auto-generated method stub
        return root.getFileEntry(name);
    }

    @Override
    public String getSourceProjectFolderName() {
        return sourceFolderName;
    }

    @Override
    public String getBinaryProjectFolderName() {
        return binaryFolderName;
    }

    @Override
    public Set<String> getEntryNames() {
        return root.getEntryNames();
    }

    public boolean hasSource() {
        return hasSource;
    }

    public boolean hasBinary() {
        return hasBinary;
    }

    public boolean hasSeparateSourceBinary() {
        return separateSourceBinary;
    }

    @Override
    public String getMixedCaseSourceProjectFolderName() {
        // TODO Auto-generated method stub
        return sourceFolder.getMixedCaseAbsoluteName();
    }
//	public static String getSourceFileSuffix() {
//		return LanguageDependencyManager.getSourceFileSuffix();
//	}
//
//	public static void setSourceFileSuffix(String sourceFileSuffix) {
//		LanguageDependencyManager.setSourceFileSuffix(sourceFileSuffix);
//	}
//	public static String getBinaryFileSuffix() {
//		return LanguageDependencyManager.getBinaryFileSuffix();
//	}

    @Override
    public RootFolderProxy getSourceFolder() {
        return sourceFolder;
    }

    @Override
    public RootFolderProxy getBinaryFolder() {
        return binaryFolder;
    }
    @Override
    public boolean isZippedFolder() {
    	return root instanceof AZippedRootFolderProxy;
    }

//	public static void setBinaryFileSuffix(String binaryFileSuffix) {
//		LanguageDependencyManager.setBinaryFileSuffix(binaryFileSuffix);
//	}
//	static void setComputedSuffixes() {
//		
//		
//	}
//	public static String getLanguage() {
//		return LanguageDependencyManager.getLanguage();
//	}
//	public static void setLanguage(String language) {
////		ARootCodeFolder.language = language;
////		sourceFileSuffix = languageToSourceFileSuffix.get(getLanguage());
////		binaryFileSuffix = languageToBinaryFileSuffix.get(getLanguage());
//		LanguageDependencyManager.setLanguage(language);
//		
//	}
//	public static MainClassFinder getMainClassFinder() {
////		return languageToMainClassFinder.get(getLanguage());
//		return LanguageDependencyManager.getMainClassFinder();
//	}
//	public static ClassFilesCompiler getSourceFilesCompiler() {
////		return languageToCompiler.get(getLanguage());
//		return LanguageDependencyManager.getSourceFilesCompiler();
//	}
//	public static boolean isJava() {
////		return getLanguage() == JAVA_LANGUAGE;
//		return LanguageDependencyManager.isJava();
//	}
//	static {
//		languageToSourceFileSuffix.put(JAVA_LANGUAGE, ".java");
//		languageToBinaryFileSuffix.put(JAVA_LANGUAGE, ".class");
//		languageToSourceFileSuffix.put(C_LANGUAGE, ".c");
//		languageToBinaryFileSuffix.put(C_LANGUAGE, ".obj");
//		
//		languageToMainClassFinder.put(JAVA_LANGUAGE, JavaMainClassFinderSelector.getMainClassFinder());
//		languageToMainClassFinder.put(C_LANGUAGE, ExecutableFinderSelector.getMainClassFinder());
//		
//		languageToCompiler.put(JAVA_LANGUAGE, JavaClassFilesCompilerSelector.getClassFilesCompiler() );
//		languageToCompiler.put(C_LANGUAGE, CFilesCompilerSelector.getClassFilesCompiler());
//
//		
//	}
}

package grader.modules;

import grader.basics.file.RootFolderProxy;
import grader.basics.file.filesystem.AFileSystemRootFolderProxy;
import grader.basics.file.zipfile.AZippedRootFolderProxy;
import grader.config.StaticConfigurationUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.configuration.PropertiesConfiguration;

public class ARequirementsToCourseInfoTranslator {
    
    public static final String COURSE_PREFIX = "comp";
    public static final String MODULE_ROOT_PACKAGE = "gradingTools";

    public static RootFolderProxy findAssignmentsDirectory(PropertiesConfiguration aConfiguration) {
        String anImplicitRoot = StaticConfigurationUtils.getImplicitRequirementsRoot(aConfiguration);
        if (anImplicitRoot == null || anImplicitRoot.isEmpty()) {
            return null;
        }
//		InputStream retVal = this.getClass().getClassLoader().getResourceAsStream("bus/uigen/uiFrame.class");
//		InputStream retVal = this.getClass().getClassLoader().getResourceAsStream("bus");
//		URL url = getClass().getResource("bus/uigen/uiFrame.class");
//		URL url2 = getClass().getResource("/bus/uigen/uiFrame.class");
//		URL url3 = getClass().getResource("/oeall-22.jar");
//		URL url4 = getClass().getResource("oeall-22.jar");
        String[] aPathElements = System.getProperty("java.class.path").split(System.getProperty("path.separator"));

//        System.out.println("Path elements:" + aPathElements);
        String aGraderPath = getGraderPath(aPathElements);
        if (aGraderPath == null) {
            return null;
        }
        RootFolderProxy aGraderFileProxy;
        File aGraderFile = new File(aGraderPath);
        if (aGraderFile.isDirectory()) {
            aGraderFileProxy = new AFileSystemRootFolderProxy(aGraderFile.getAbsolutePath(), MODULE_ROOT_PACKAGE);
        } else {
            aGraderFileProxy = new AZippedRootFolderProxy(aGraderFile.getAbsolutePath());
        }
//        System.out.println("Grader file proxy:" + aGraderFileProxy);
        return aGraderFileProxy;
    }

    public static String getGraderPath(String[] aPathElements) {
        for (String aPathElement : aPathElements) {
            if (aPathElement.contains("rader")) {
                return aPathElement;
            }
        }
        return null;
    }

    public static String[] getRequirementModules(PropertiesConfiguration config) throws IOException {
        RootFolderProxy assignments = findAssignmentsDirectory(config);
        if (assignments == null) {
            return new String[]{};
        } else {
            final Path root = Paths.get(assignments.getAbsoluteName());
            final Set<String> modules = new HashSet<>();
            Files.walkFileTree(root, new FileVisitor<Path>() {

                @Override
                public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    if (file.getFileName().toString().contains("Requirements")) {
                        Path relative = root.relativize(file);
                        modules.add(packageToModule(relative.toString()));
                        return FileVisitResult.SKIP_SIBLINGS;
                    } else {
                        return FileVisitResult.CONTINUE;
                    }
                }

                @Override
                public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
                    return FileVisitResult.CONTINUE;
                }
                
            });
            return modules.toArray(new String[modules.size()]);
        }
    }
    
    /**
     * Expects assignments to be in one of these formats:
     * <ul><li>root.course.term.section.[lab].assignment</li>
     * <li>root. course.year.season.section.[lab].assignment</li></ul>
     * 
     * Formats to CourseTerm
     * @param pName string of assignment path (relative to root)
     * @return module name
     */
    private static String packageToModule(String pName) {
        String[] parts = pName.split("[/\\\\]");
        StringBuilder modName = new StringBuilder(10);
        
        if (parts[0].startsWith(COURSE_PREFIX)) {
            modName.append(parts[0]);
            if (Character.isAlphabetic(parts[1].charAt(0))) {
                modName.append(parts[1]);
            } else {
                modName.append(seasonToAbbr(parts[2]));
                modName.append(parts[1].substring(parts[1].length() - 2));
            }
            if (parts[parts.length - 1].equals("lab")) {
                modName.append("-").append(parts[parts.length - 2]);
                modName.append("-lab");
            } else {
                modName.append("-").append(parts[parts.length - 1]);
            }
        }
        
        return modName.toString();
    }
    
    private static String seasonToAbbr(String season) {
        switch(season) {
            case "fall":
                return "f";
            case "spring":
                return "sp";
            case "maymester":
                return "mm";
            case "summer1":
                return "s1-";
            case "summer2":
                return "s2-";
            default:
                return "";
        }
    }
    
    public ARequirementsToCourseInfoTranslator() {

    }
}

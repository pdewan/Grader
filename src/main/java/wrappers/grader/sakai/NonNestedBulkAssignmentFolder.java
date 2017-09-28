package wrappers.grader.sakai;

import framework.utils.GraderSettings;
import grader.basics.settings.BasicGradingEnvironment;
import grader.file.FileProxy;
import grader.file.RootFolderProxy;
import grader.sakai.BulkAssignmentFolder;

import java.io.File;
import java.io.FileFilter;
import java.util.HashSet;
import java.util.Set;

import wrappers.grader.file.SimplifiedFileProxy;

/**
 * Similar to {@link grader.sakai.ASakaiBulkAssignmentFolder} but it doesn't assume it is nested within another folder
 */
public class NonNestedBulkAssignmentFolder implements BulkAssignmentFolder {

    private SimplifiedFileProxy rootProxy;
    private FileProxy spreadsheet;

    public NonNestedBulkAssignmentFolder() {
        rootProxy = new SimplifiedFileProxy(new File(GraderSettings.get().get("path")));
        spreadsheet = new SimplifiedFileProxy(new File(rootProxy.getFile(), "grades.csv"));
    }

    @Override
    public RootFolderProxy getAssignmentFolder() {
        return rootProxy;
    }

    @Override
    public Set<String> getStudentFolderNames() {
        File[] folders = rootProxy.getFile().listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                return pathname.isDirectory();
            }
        });
        Set<String> names = new HashSet<String>();
        for (File file : folders)
            names.add(file.getAbsolutePath());
        return names;
    }

    @Override
    public FileProxy getStudentFolder(String aName) {
        return new SimplifiedFileProxy(new File(aName));
    }

    @Override
    public FileProxy getSpreadsheet() {
        return spreadsheet;
    }

    @Override
    public String getAssignmentName() {
        return BasicGradingEnvironment.get().getAssignmentName().toLowerCase();
    }

    @Override
    public String getMixedCaseAssignmentName() {
        return BasicGradingEnvironment.get().getAssignmentName();
    }

	@Override
	public void clear() {
		rootProxy.clear();
		
	}
}

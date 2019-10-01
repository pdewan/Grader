package grader.navigation.sorter;

import java.io.File;
import java.util.Comparator;

public class AFileObjectSorter implements Comparator<File>{
	Comparator<String> fileNameSorter;
	public AFileObjectSorter(Comparator<String> aFileNameSorter) {
		fileNameSorter = aFileNameSorter;
	}

	@Override
	public int compare(File o1, File o2) {
//		if (!(o1 instanceof File && o2 instanceof File)) {
//			throw new RuntimeException("Invalid Type.  Must be of type File.");
//		}
		File f1 = (File) o1;
		File f2 = (File) o2;
		if (f1.isDirectory() && f2.isDirectory()) {
			return fileNameSorter.compare(f1.getName(), f2.getName());
		} else if (f1.isDirectory()) {
			return -1;
		} else if (f2.isDirectory()) {
			return 1;
		} else {
			return f1.getName().compareTo(f2.getName());
		}
		
	}
	

}

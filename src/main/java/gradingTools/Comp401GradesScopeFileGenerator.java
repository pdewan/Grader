package gradingTools;

public class Comp401GradesScopeFileGenerator {
//	static String[] onyensToEmail = new String[] {
//		"whglaser:whglaser@ad.unc.edu",
//		"ellenecs:ellenecs@email.unc.edu"
//	};
	static String[] onyensToEmail = new String[] {
		"whglaser:whglaser@icloud.com",
		"brian263:brian263@email.unc.edu",
		"gowrisha:preethi_gowrishankar@med.unc.edu",
		"zni:zni@email.unc.edu",
		"jordancn:jordancn@kenan-flagler.unc.edu",
		"ritan:ritan@email.unc.edu",
		"gmaddock:gmaddock@email.unc.edu"
		
	};
	public static void main (String[] args) {
		GradeScopeFileGenerator.createAndWriteGeneratedFile("grades.csv", 
				onyensToEmail);
		
	 
	}

}

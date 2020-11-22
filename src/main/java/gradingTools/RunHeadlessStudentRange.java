package gradingTools;

public class RunHeadlessStudentRange {

	public static void main(String[] args) {
		runHeadlessStudentRange(args[0], args[1], args[2], args[3]);
	}

	public static void runHeadlessStudentRange (String aCourse, String aProjectFolder, String aStudentStart, String aStudentEnd ) {
		String[] myArgs = {
				 "--project-requirements",
				 "--project-name",
				 " Assignment1",
				  "--grader-controller",
				  	"AHeadlessGradingManager",
				  	"--headless-path",
				  	aProjectFolder,
				  	"--headless-start",
				  	aStudentStart,
				  	"--headless-end",
				  	aStudentEnd,
				  	"--course-name",
				  	aCourse,
				  	"--logger",
				  	"feedback-txt+feedback-json+local-txt+local-json",
				  	"--no-framework-gui",
				  	"--clean-slate",
				  	"*"
		};
		

		Driver.main(myArgs);

		System.exit(0);
		}
//		Driver.main(myArgs);
//		java -cp .;D:\autograder\source\Comp524GraderAll.jar;D:\dewan_backup\Java\Gradescope-Autograder\GradescopeAssignmentSetup\target\classes;C:\Users\dewan\.m2\repository\org\json\json\20171018\json-20171018.jar gradingTools.Comp524Driver --project-requirements  --project-name  Assignment1 --grader-controller AHeadlessGradingManager --headless-path D:\autograder\source\Assignment1 --headless-start student --headless-end student --course-name Comp524F20 --logger feedback-txt+feedback-json+local-txt+local-json --no-framework-gui --clean-slate student

}

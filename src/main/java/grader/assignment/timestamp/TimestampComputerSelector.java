package grader.assignment.timestamp;

public class TimestampComputerSelector {
static TimestampComputer timestampComputer = new SakaiTimestampComputer();

public static TimestampComputer getTimestampComputer() {
	return timestampComputer;
}

public static void setTimestampComputer(TimestampComputer timestampComputer) {
	TimestampComputerSelector.timestampComputer = timestampComputer;
}

}

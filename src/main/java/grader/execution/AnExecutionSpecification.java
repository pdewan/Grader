package grader.execution;

import grader.basics.execution.ABasicExecutionSpecification;
import grader.config.StaticConfigurationUtils;
import util.trace.Tracer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AnExecutionSpecification extends ABasicExecutionSpecification implements ExecutionSpecification {
//	protected List<String> processTeams = new ArrayList<>();
//	protected Map<String, List<String>> processTeamToProcesses = new HashMap<>();
//	protected Map<String, List<String>> processTeamToTerminatingProcesses = new HashMap<>();
//	protected Map<String, Integer> processToSleepTime = new HashMap<>();
//	protected Map<String, String> processToEntryTag = new HashMap<>();
//	protected Map<String, List<String>> processToEntryTags = new HashMap<>();
//	protected Map<String, String> processToEntryPoint = new HashMap<>();
//	protected Map<String, List<String>> processToArgs = new HashMap<>();
//	protected Map<String, List<String>> processToStartTags = new HashMap<>();
	
	public AnExecutionSpecification() {
		
	}
	
	protected void loadEntryTag(String aProcess) {
		String entryTag = StaticConfigurationUtils.getEntryTag(aProcess);
		if (entryTag != null)
			processToEntryTag.put(aProcess, StaticConfigurationUtils.getEntryTag(aProcess));
	}
	
	protected void loadStartTags(String aProcess) {
		List<String> startTags = StaticConfigurationUtils.getProcessStartTags(aProcess);
		if (startTags != null)
			processToStartTags.put(aProcess, startTags);
	}
	
	protected void loadEntryTags(String aProcess) {
		List<String> entryTags = StaticConfigurationUtils.getEntryTags(aProcess);
		if (entryTags  != null)
			processToEntryTags.put(aProcess, entryTags);
	}
	protected void loadEntryPoint(String aProcess) {
		String anEntryPoint = StaticConfigurationUtils.getEntryPoint(aProcess);
		if (anEntryPoint  != null)
			processToEntryPoint.put(aProcess, anEntryPoint);
	}
	protected void loadSleepTime(String aProcess) {
		Integer sleepTime = StaticConfigurationUtils.getSleepTime(aProcess);
		if (sleepTime != null)
			processToSleepTime.put(aProcess, sleepTime);
	}
	
	protected void loadArgs(String aProcess) {
		List<String> args = StaticConfigurationUtils.getProcessArgs(aProcess);
		if (args != null)
			processToArgs.put(aProcess, args);
	}
	
	/* (non-Javadoc)
	 * @see grader.execution.ExecutionSpecification#loadFromConfiguration()
	 */
	@Override
	public void loadFromConfiguration() {
		processTeams = StaticConfigurationUtils.getProcessTeams();

		for (String aProcessTeam:processTeams) {
			List<String> aProcesses =  StaticConfigurationUtils.getProcesses(aProcessTeam);
			processTeamToProcesses.put(aProcessTeam, aProcesses);
			List<String> aTerminatingProcesses =  StaticConfigurationUtils.getTerminatingProcesses(aProcessTeam);
			processTeamToTerminatingProcesses.put(aProcessTeam, aTerminatingProcesses);

			for (String aProcess:aProcesses) {
				loadSleepTime(aProcess);
				loadEntryTag(aProcess);
				loadEntryTags(aProcess);
				loadEntryPoint(aProcessTeam);
				loadStartTags(aProcess);
				loadArgs(aProcess);
//				Integer sleepTime = StaticConfigurationUtils.getSleepTime(aProcess);
//				if (sleepTime != null)
//					processToSleepTime.put(aProcess, sleepTime);
				
				
//				
//				String entryTag = StaticConfigurationUtils.getEntryTag(aProcess);
//				if (entryTag != null)
//					processToEntryTag.put(aProcess, StaticConfigurationUtils.getEntryTag(aProcess));
				
//				List<String> args = StaticConfigurationUtils.getProcessArgs(aProcess);
//				if (args != null)
//					processToArgs.put(aProcess, args);
//				List<String> startTags = StaticConfigurationUtils.getProcessStartTags(aProcess);
//				if (startTags != null)
//					processToStartTags.put(aProcess, args);
				
			}
		}
		
	}
	
//	/* (non-Javadoc)
//	 * @see grader.execution.ExecutionSpecification#getProcessTeams()
//	 */
//	@Override
//	public List<String> getProcessTeams() {
//		return processTeams;
//	}
//	
//	/* (non-Javadoc)
//	 * @see grader.execution.ExecutionSpecification#setProcessTeams(java.util.List)
//	 */
//	@Override
//	public void setProcessTeams(List<String> aProcessTeamNames) {
//		Tracer.info(this, "Setting process team: " + aProcessTeamNames);
//		processTeams = aProcessTeamNames;
//	}
//	
//	/* (non-Javadoc)
//	 * @see grader.execution.ExecutionSpecification#getProcesses(java.lang.String)
//	 */
//	@Override
//	public List<String> getProcesses(String aProcessTeam) {
//		return processTeamToProcesses.get(aProcessTeam);
//	}
//	
//	/* (non-Javadoc)
//	 * @see grader.execution.ExecutionSpecification#setProcesses(java.lang.String, java.util.List)
//	 */
//	@Override
//	public void setProcesses(String aProcessTeam, List<String> aProcesses) {
//		Tracer.info(this, "Setting processes: " + aProcessTeam + " = " + aProcesses);
//		processTeamToProcesses.put(aProcessTeam, aProcesses);
//	}
//	
//	@Override
//	public List<String> getTerminatingProcesses(String aProcessTeam) {
//		return processTeamToTerminatingProcesses.get(aProcessTeam);
//	}
//	
//	/* (non-Javadoc)
//	 * @see grader.execution.ExecutionSpecification#setProcesses(java.lang.String, java.util.List)
//	 */
//	@Override
//	public void setTerminatingProcesses(String aProcessTeam, List<String> aProcesses) {
//		Tracer.info(this, "Setting terminating processes: " + aProcessTeam + " - " + aProcesses);
//		processTeamToTerminatingProcesses.put(aProcessTeam, aProcesses);
//	}
//	/* (non-Javadoc)
//	 * @see grader.execution.ExecutionSpecification#getSleepTime(java.lang.String)
//	 */
//	@Override
//	public Integer getSleepTime(String aProcess) {
//		return processToSleepTime.get(aProcess);
//	}
//	
//	/* (non-Javadoc)
//	 * @see grader.execution.ExecutionSpecification#setSleepTime(java.lang.String, int)
//	 */
//	@Override
//	public void setSleepTime(String aProcess, int aSleepTime) {
//		Tracer.info(this, "Setting sleep time: " + aSleepTime);
//		 processToSleepTime.put(aProcess, aSleepTime);
//	}
//	
//	/* (non-Javadoc)
//	 * @see grader.execution.ExecutionSpecification#getEntrytag(java.lang.String)
//	 */
//	@Override
//	public String getEntryTag(String aProcess) {
//		return processToEntryTag.get(aProcess);
//	}
//	/* (non-Javadoc)
//	 * @see grader.execution.ExecutionSpecification#setEntryTag(java.lang.String, java.lang.String)
//	 */
//	@Override
//	public void setEntryTag(String aProcess, String anEntryTag) {
//		Tracer.info(this, "Setting entry tag processes: " + aProcess + " - " + anEntryTag);
//		 processToEntryTag.put(aProcess, anEntryTag);
//	}
//	/* (non-Javadoc)
//	 * @see grader.execution.ExecutionSpecification#getArgs(java.lang.String)
//	 */
//	@Override
//	public List<String> getArgs(String aProcess) {
//		return processToArgs.get(aProcess);
//	}
//	/* (non-Javadoc)
//	 * @see grader.execution.ExecutionSpecification#setArgs(java.lang.String, java.util.List)
//	 */
//	@Override
//	public void setArgs(String aProcess, List<String> anEntryArgs) {
//		Tracer.info(this, "Setting processes args: " + aProcess + " - " + anEntryArgs);
//		 processToArgs.put(aProcess, anEntryArgs);
//	}
//	
//	@Override
//	public List<String> getStartTags(String aProcess) {
//		return processToStartTags.get(aProcess);
//	}
//	/* (non-Javadoc)
//	 * @see grader.execution.ExecutionSpecification#setArgs(java.lang.String, java.util.List)
//	 */
//	@Override
//	public void setStartTags(String aProcess, List<String> aStartTags) {
//		Tracer.info(this, "Setting start tag processes: " + aProcess + " - " + aStartTags);
//		processToStartTags.put(aProcess, aStartTags);
//	}
//
//	@Override
//	public String getEntryPoint(String aProcess) {
//		return processToEntryPoint.get(aProcess);
//	}
//
//	@Override
//	public void setEntryPoint(String aProcess, String anEntryPoint) {
//		Tracer.info(this, "Setting entry point processes: " + aProcess + " - " + anEntryPoint);
//		processToEntryPoint.put(aProcess, anEntryPoint);
//	}
//
//	@Override
//	public List<String> getEntryTags(String aProcess) {
//		return processToEntryTags.get(aProcess);
//	}
//
//	@Override
//	public void setEntryTags(String aProcess, List<String> anEntryTags) {
//		Tracer.info(this, "Setting entry tags processes: " + aProcess + " - " + anEntryTags);
//		processToEntryTags.put(aProcess, anEntryTags);
//		
//	}

}

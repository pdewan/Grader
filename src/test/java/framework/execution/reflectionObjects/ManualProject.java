package framework.execution.reflectionObjects;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Map;

import util.trace.TraceableLog;
import framework.execution.ARunningProject;
import framework.project.FatProject;
import grader.basics.execution.NotRunnableException;
import grader.basics.project.ClassesManager;
import grader.basics.project.source.BasicTextManager;
import grader.basics.util.Option;
//import scala.Option;
import util.pipe.InputGenerator;

/**
 * Created with IntelliJ IDEA.
 * User: josh
 * Date: 10/7/13
 * Time: 11:50 AM
 * To change this template use File | Settings | File Templates.
 */
public class ManualProject implements FatProject {

    private ClassesManager classesManager;

    public ManualProject() {
        classesManager = new ManualClassesManager(Program.class);
    }

    @Override
    public ARunningProject start(String input) {
        //To change body of implemented methods use File | Settings | File Templates.
        return null;
    }

    @Override
    public ARunningProject launch(String input) {
        //To change body of implemented methods use File | Settings | File Templates.
        return null;
    }

    @Override
    public ARunningProject start(String input, int timeout) throws NotRunnableException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public ARunningProject launch(String input, int timeout) throws NotRunnableException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public ARunningProject launchInteractive() throws NotRunnableException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Option<ClassesManager> getClassesManager() {
        return Option.apply(classesManager);
    }

    @Override
    public File getSourceFolder() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public File getBuildFolder(String preferredClass) throws FileNotFoundException {
        return new File("./");
    }

    @Override
    public TraceableLog getTraceableLog() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

	@Override
	public ARunningProject launch(
			InputGenerator anOutputBasedInputGenerator,
			String input, int timeout) throws NotRunnableException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ARunningProject launch(
			InputGenerator anOutputBasedInputGenerator,
			Map<String, String> aProcessToInput, int timeout)
			throws NotRunnableException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ARunningProject launch(String input, String[] anArgs, int timeout)
			throws NotRunnableException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ARunningProject launchInteractive(String[] args)
			throws NotRunnableException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isInfinite() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setInfinite(boolean newVal) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public File getProjectFolder() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public File getBuildFolder() throws FileNotFoundException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public File getObjectFolder() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BasicTextManager getTextManager() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getSource() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getCurrentInput() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setCurrentInput(String currentInput) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public StringBuffer getCurrentOutput() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void clearOutput() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setCurrentOutput(StringBuffer currentOutput) {
		// TODO Auto-generated method stub
		
	}
}

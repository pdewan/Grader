package framework.execution;

import java.io.InputStream;
import java.util.Scanner;
import java.util.concurrent.Semaphore;

public interface RunnerStreamProcessor {
	

	public BasicRunningProject getRunner() ;


	public Semaphore getSemaphore() ;


	public String getProcessName() ;

	public Boolean getOnlyProcess() ;

}

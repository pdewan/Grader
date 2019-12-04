package grader.navigation;

import grader.sakai.project.SakaiProjectDatabase;

import java.util.List;

public interface NavigationListManager {
	public List<String> getOnyenNavigationList(SakaiProjectDatabase aSakaiProjectDatabase);

	List<String> getRawOnyenNavigationList();

	List<String> getOnyenNavigationList(SakaiProjectDatabase aSakaiProjectDatabase, boolean selectedOnly);

}

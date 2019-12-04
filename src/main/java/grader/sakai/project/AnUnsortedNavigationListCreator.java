package grader.sakai.project;

import grader.navigation.NavigationListManager;

import java.util.ArrayList;
import java.util.List;
// Not sure we need this class any longer
public class AnUnsortedNavigationListCreator implements NavigationListManager {

	@Override
	public List<String> getOnyenNavigationList(
			SakaiProjectDatabase aSakaiProjectDatabase) {
		 return new ArrayList(aSakaiProjectDatabase.getOnyens());
	}

	@Override
	public List<String> getRawOnyenNavigationList() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> getOnyenNavigationList(SakaiProjectDatabase aSakaiProjectDatabase, boolean selectedOnly) {
		// TODO Auto-generated method stub
		return null;
	}

}

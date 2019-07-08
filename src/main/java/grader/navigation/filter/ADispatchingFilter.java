package grader.navigation.filter;

import grader.sakai.project.ProjectStepper;
import grader.sakai.project.SakaiProjectDatabase;
import grader.settings.navigation.NavigationFilterRepository;
import grader.settings.navigation.NavigationFilterSetter;
import util.models.DynamicEnum;

public class ADispatchingFilter implements BasicNavigationFilter{
	NavigationFilterSetter navigationFilterSetter;
	DynamicEnum navigationFilterType;
	public ADispatchingFilter(NavigationFilterSetter aNavigationFilterSetter) {
		navigationFilterSetter = aNavigationFilterSetter;
		navigationFilterType = aNavigationFilterSetter.getNavigationFilterType();
	}
	@Override
	public boolean includeProject(ProjectStepper aProjectState,
			SakaiProjectDatabase aDatabase) {
		if (aProjectState.getProject() == null)
			return false;
		String filterName = (String) navigationFilterType.getValue();
		BasicNavigationFilter currentFilter = NavigationFilterRepository.getFilter(filterName);
		return currentFilter.includeProject(aProjectState, aDatabase);	
	}

}

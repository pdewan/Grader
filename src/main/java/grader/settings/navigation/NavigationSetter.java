package grader.settings.navigation;

import grader.navigation.NavigationKind;

import java.beans.PropertyChangeListener;

import util.models.PropertyListenerRegisterer;

public interface NavigationSetter extends PropertyListenerRegisterer {

	AutomaticNavigationSetter getAutomaticNavigationSetter();

	void setAutomaticNavigationSetter(
			AutomaticNavigationSetter automaticNavigationSetter);

	NavigationFilterSetter getNavigationFilterSetter();

	void setNavigationFilterSetter(NavigationFilterSetter navigationFilterSetter);

	NavigationKind getNavigationKind();

	void setNavigationKind(NavigationKind navigationKind);

}

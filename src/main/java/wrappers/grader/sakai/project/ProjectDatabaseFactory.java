package wrappers.grader.sakai.project;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.List;

public class ProjectDatabaseFactory {
	static ProjectDatabaseWrapper oldProjectDatabaseWrapper, projectDatabaseWrapper;
	static List<PropertyChangeListener> observers = new ArrayList();
	public static String PROPERTY_NAME = "DatabaseCreated";
	public static ProjectDatabaseWrapper  createProjectDatabase() {
//		projectDatabaseWrapper = new ProjectDatabaseWrapper();

		ProjectDatabaseWrapper oldProjectDatabaseWrapper  = projectDatabaseWrapper;
		projectDatabaseWrapper = new ProjectDatabaseWrapper();
		PropertyChangeEvent anEvent = new PropertyChangeEvent(projectDatabaseWrapper, PROPERTY_NAME, oldProjectDatabaseWrapper, projectDatabaseWrapper);
		notifyPropertyChangeListeners(anEvent);
		return projectDatabaseWrapper;
	}

	public static ProjectDatabaseWrapper getOrCreateProjectDatabase() {
		if (projectDatabaseWrapper == null) {
			ProjectDatabaseWrapper oldProjectDatabaseWrapper  = projectDatabaseWrapper;
			projectDatabaseWrapper = new ProjectDatabaseWrapper();
			PropertyChangeEvent anEvent = new PropertyChangeEvent(null, PROPERTY_NAME, oldProjectDatabaseWrapper, projectDatabaseWrapper);

		}
		return projectDatabaseWrapper;
	}
	
	
	public static void addPropertyChangeListener(PropertyChangeListener aListener) {
		observers.add(aListener);		
	}
	
	protected static void notifyPropertyChangeListeners(PropertyChangeEvent anEvent) {
		for (PropertyChangeListener aListener:observers) {
			aListener.propertyChange(anEvent);
		}
		
	}

}

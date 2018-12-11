package grader.settings.navigation;

import grader.navigation.NavigationKind;
import grader.sakai.project.ASakaiProjectDatabase;
import grader.settings.GraderSettingsModel;
import grader.trace.settings.NavigationKindChange;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import javax.swing.JRadioButton;

import util.annotations.Explanation;
import util.annotations.Label;
import util.annotations.PreferredWidgetClass;
import util.annotations.Row;
import util.annotations.StructurePattern;
import util.annotations.StructurePatternNames;
import bus.uigen.OEFrame;
import bus.uigen.ObjectEditor;
@StructurePattern(StructurePatternNames.BEAN_PATTERN)
public class ANavigationSetter implements NavigationSetter {
	NavigationKind navigationKind = NavigationKind.HYBRID;
	AutomaticNavigationSetter automaticNavigationSetter ;
	NavigationFilterSetter navigationFilterSetter; 
	PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);
//	DynamicEnum<String> navigationFilterEnum;
//	NavigationFilter currentNavigationFilter;
//	String currentNavigationFilterName;
//	Object navigationParameters;
//	PropertyChangeSupport propertyChangeSupport =  new PropertyChangeSupport(this);
//	
	GraderSettingsModel graderSettings;
	
	public ANavigationSetter(GraderSettingsModel aGraderSettings) {
		graderSettings = aGraderSettings;
		navigationFilterSetter = new ANavigationFilterSetter(aGraderSettings);
		automaticNavigationSetter = new AnAutomaticNavigationSetter(aGraderSettings);
	}
	@Row(0)
	@PreferredWidgetClass(JRadioButton.class)
	@Override
	@Explanation("Automatic runs auto-grader without review.  Manual autogrades incremently before each student.  Hybrid does automatic first and then manual")
	public NavigationKind getNavigationKind() {
		return navigationKind;
	}
	public boolean preSetNavigationKind() {
		return graderSettings == null || !graderSettings.isGraderStarted();
	}
	@Override
	public void setNavigationKind(NavigationKind newVal) {
		if (navigationKind == newVal) return;
		NavigationKind oldVal = navigationKind;
		this.navigationKind = newVal;
		if (graderSettings.isSettingsLoaded()) {
			ASakaiProjectDatabase.executor().submit((() -> {
				propertyChangeSupport.firePropertyChange("navigationKind", oldVal, newVal);	
			    return null;
			}));
//			propertyChangeSupport.firePropertyChange("navigationKind", oldVal, newVal);	

			NavigationKindChange.newCase(newVal, graderSettings, this);
		}
	}
	
	@Row(1)
	@Label("Automatic Navigation Options")
	@Override
	@Explanation("Determines if and how fast automatically navigated records are displayed")
	public AutomaticNavigationSetter getAutomaticNavigationSetter() {
		return automaticNavigationSetter;
	}
	@Override
	public void setAutomaticNavigationSetter(
			AutomaticNavigationSetter automaticNavigationSetter) {
		this.automaticNavigationSetter = automaticNavigationSetter;
	}
	@Row(2)
	@Label("Manual Navigation Filter") // theoretically this could be used also for automatic navigation
	@Override
	@Explanation("Provides a way to manually browse through only selected records in the range based on filter type")
	public NavigationFilterSetter getNavigationFilterSetter() {
		return navigationFilterSetter;
	}
	@Override
	public void setNavigationFilterSetter(
			NavigationFilterSetter navigationFilterSetter) {
		this.navigationFilterSetter = navigationFilterSetter;
	}
	
//	void constructNavigationFilterTypes () {
//		Set<String> filterNames = NavigationFilterRepository.filterTypes();
//		List<String> filterNamesList = new ArrayList(filterNames);
//		Collections.sort(filterNamesList);
//		navigationFilterEnum = new ADynamicEnum(filterNamesList);
//		navigationFilterEnum.addPropertyChangeListener(this);
//		if (filterNamesList.size() > 0) {
//		  currentNavigationFilterName =  filterNamesList.get(0);
//		  currentNavigationFilter = NavigationFilterRepository.getFilterer(currentNavigationFilterName);
//		}
//	}
//	
////	public boolean preGetNavigationFilterTypes() {
////		return  navigationFilterEnum.choicesSize() > 0;
////	}
////	public boolean preGetParameters() {
////		return preGetNavigationFilterTypes() && currentNavigationFilter != null && currentNavigationFilter.getParameters() != null;
////	}
//	@Row(2)	
//	public DynamicEnum getNavigationFilterType() {
//		return navigationFilterEnum;
//	}
//	
//	
//	@Row(3)
//	@PreferredWidgetClass(JRadioButton.class)
//	@Label("Navigation Filter Options")
//	public Object getFilterOptions() {
//		return currentNavigationFilter.getParameters();
//	}
//
//	@Override
//	public void propertyChange(PropertyChangeEvent anEvent) {
//		if (anEvent.getSource() == navigationFilterEnum && anEvent.getPropertyName().equals("value")) {
//			currentNavigationFilterName = (String) anEvent.getNewValue();
//			Object oldParameters = currentNavigationFilter.getParameters();
//			currentNavigationFilter = NavigationFilterRepository.getFilterer(currentNavigationFilterName);
//			Object newParameters = currentNavigationFilter.getParameters();
//			propertyChangeSupport.firePropertyChange("FilterOptions", oldParameters, newParameters);
//		}
//		
//		
//	}
//	@Override
//	public void addPropertyChangeListener(PropertyChangeListener aListener) {
//		propertyChangeSupport.addPropertyChangeListener(aListener);
//		
//	}
	
	@Override
	public void addPropertyChangeListener(PropertyChangeListener aListener) {
		propertyChangeSupport.addPropertyChangeListener(aListener);
		
	}
	
	public static void main (String[] args) {
//		NavigationFilter gradingStatusFilter = new AGradingStatusFilter();
//     	NavigationFilterRepository.register(gradingStatusFilter);
//     	NavigationFilter notesStatusFilter = new ANotesStatusFilter();
//     	NavigationFilterRepository.register(notesStatusFilter);
//     	NavigationFilter letterStatusFilter = new ALetterGradeBasedFilter();
//     	NavigationFilterRepository.register(letterStatusFilter);
		NavigationSetter navigationSetter = new ANavigationSetter(null);		
		OEFrame frame = ObjectEditor.edit(navigationSetter);
		frame.setSize(600, 300);
	}
	
	
}

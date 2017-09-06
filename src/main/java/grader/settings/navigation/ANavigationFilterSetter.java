package grader.settings.navigation;

import grader.navigation.filter.NavigationFilter;
import grader.settings.GraderSettingsModel;
import grader.trace.settings.NavigationFilterUserChange;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.List;

import javax.swing.JRadioButton;

import util.annotations.Explanation;
import util.annotations.Label;
import util.annotations.PreferredWidgetClass;
import util.annotations.Row;
import util.annotations.StructurePattern;
import util.annotations.StructurePatternNames;
import util.models.ADynamicEnum;
import util.models.DynamicEnum;
import bus.uigen.OEFrame;
import bus.uigen.ObjectEditor;
import bus.uigen.attributes.AttributeNames;
import bus.uigen.introspect.Attribute;
@StructurePattern(StructurePatternNames.BEAN_PATTERN)
public class ANavigationFilterSetter implements NavigationFilterSetter {
//	NavigationKind navigationKind = NavigationKind.AUTOMATIC_THEN_MANUAL;
//	AutomaticNavigationSetter automaticNavigationSetter = new AnAutomaticNavigationSetter();
	DynamicEnum<String> navigationFilterEnum;
	NavigationFilter<Object> currentNavigationFilter;
	String currentNavigationFilterName;
	Object navigationParameters;
	PropertyChangeSupport propertyChangeSupport =  new PropertyChangeSupport(this);
	GraderSettingsModel graderSettings;
	
	String currentFilterExplanation = "";
	
	
	public ANavigationFilterSetter(GraderSettingsModel aGraderSettings) {
		graderSettings = aGraderSettings;
		constructNavigationFilterTypes();
	}
	
	
	void constructNavigationFilterTypes () {
//		Set<String> filterNames = NavigationFilterRepository.filterTypes();
//		List<String> filterNamesList = new ArrayList(filterNames);
//		Collections.sort(filterNamesList);
		List<String> filterNamesList = NavigationFilterRepository.filterTypes();

		navigationFilterEnum = new ADynamicEnum<String>(filterNamesList);
		navigationFilterEnum.addPropertyChangeListener(this);
		if (filterNamesList.size() > 0) {
		  currentNavigationFilterName =  filterNamesList.get(0);
		  currentNavigationFilter = NavigationFilterRepository.getFilter(currentNavigationFilterName);
		  currentNavigationFilter.addPropertyChangeListener(this);
		}
//		announceExplanation();
	}
	
//	public boolean preGetNavigationFilterTypes() {
//		return  navigationFilterEnum.choicesSize() > 0;
//	}
//	public boolean preGetParameters() {
//		return preGetNavigationFilterTypes() && currentNavigationFilter != null && currentNavigationFilter.getParameters() != null;
//	}
	@Override
	@Row(2)	
	@Label("Filter Type")
	@Explanation("A list of filters to select which student records are displayed in manual navigation.")
	public DynamicEnum<String> getNavigationFilterType() {
		return navigationFilterEnum;
	}
	public boolean preSetNavigationFilterType() {
		return graderSettings == null || !graderSettings.isGraderStarted();
//		return false;

	}
	public void setNavigationFilterType(DynamicEnum<String> newVal) {
		navigationFilterEnum = newVal;
	}
	
	@Override
	@Row(3)
	@PreferredWidgetClass(JRadioButton.class)
	@Label("Filter Options")
	public Object getParameter() {
		return currentNavigationFilter.getParameter();
	}
	public boolean preSetParameter() {
		return graderSettings == null || !graderSettings.isGraderStarted();
	}
	@Override
	public void setParameter(Object newVal) {
		Object oldVal = currentNavigationFilter.getParameter();
		currentNavigationFilter.setParameter(newVal);
		propertyChangeSupport.firePropertyChange("Parameter", oldVal, newVal);

	}
	
	public static String getExplanation(Object object) {
		if (object == "") return null;
		return getExplanation(object.getClass());
		
	}
	
	public static String getExplanation(Class<? extends Object> aClass) {
		Explanation explanation = aClass.getAnnotation(Explanation.class);
		if (explanation == null) return "";
		return explanation.value();
		
	}
	
	void announceExplanation() {
//		String oldExplanation = currentFilterExplanation;
		currentFilterExplanation = getExplanation(currentNavigationFilter);
		propertyChangeSupport.firePropertyChange("Parameter", null, new Attribute(AttributeNames.EXPLANATION, currentFilterExplanation ));

	}

	@Override
	public void propertyChange(PropertyChangeEvent anEvent) {
		if (anEvent.getSource() == navigationFilterEnum && anEvent.getPropertyName().equals("value")) {
			currentNavigationFilterName = (String) anEvent.getNewValue();
			Object oldParameters = currentNavigationFilter.getParameter();
			currentNavigationFilter = NavigationFilterRepository.getFilter(currentNavigationFilterName);
			Object newParameters = currentNavigationFilter.getParameter();
			propertyChangeSupport.firePropertyChange("Parameter", oldParameters, newParameters);
			announceExplanation();
			NavigationFilterUserChange.newCase(currentNavigationFilter, graderSettings, this);
			
		} else if (anEvent.getSource() == currentNavigationFilter) {
			// assuming the property names are the same in both objects, maybe bad idea
			propertyChangeSupport.firePropertyChange(anEvent.getPropertyName(), anEvent.getOldValue(), anEvent.getNewValue());
		}
		
		
	}
	@Override
	public void addPropertyChangeListener(PropertyChangeListener aListener) {
		propertyChangeSupport.addPropertyChangeListener(aListener);
//		announceExplanation();
		
	}
	
	
	public static void main (String[] args) {
//		NavigationFilter gradingStatusFilter = new AGradingStatusFilter();
//     	NavigationFilterRepository.register(gradingStatusFilter);
//     	NavigationFilter notesStatusFilter = new ANotesStatusFilter();
//     	NavigationFilterRepository.register(notesStatusFilter);
		NavigationFilterSetter navigationFilterSetter = new ANavigationFilterSetter(null);
		OEFrame frame = ObjectEditor.edit(navigationFilterSetter);
		frame.setSize(600, 300);
	}
	
}

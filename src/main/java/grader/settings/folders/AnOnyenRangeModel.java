package grader.settings.folders;

import grader.settings.GraderSettingsModel;
import grader.trace.settings.EndOnyenUserChange;
import grader.trace.settings.StartOnyenUserChange;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import org.objectweb.asm.tree.JumpInsnNode;

import util.annotations.Explanation;
import util.annotations.Label;
import util.annotations.Row;
import util.annotations.StructurePattern;
import util.annotations.StructurePatternNames;
import util.annotations.Visible;
import bus.uigen.ObjectEditor;
@StructurePattern(StructurePatternNames.BEAN_PATTERN)
public class AnOnyenRangeModel implements OnyenRangeModel{	
	public static final String ANONYMOUS = "*******";
	String startingOnyen = "^", endingOnyen = "$", onyenList = "", goToOnyen = "";
	GraderSettingsModel graderSettings;
	PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);
	public AnOnyenRangeModel(GraderSettingsModel aGraderSettings) {
		graderSettings = aGraderSettings;
	}
	@Visible(false)
	public String getStartingOnyen() {
		return startingOnyen;
	}
    @Row(0)
    @Label("Onyen Range Start:")
    public String getDisplayedStartingOnyen() {
    	if (graderSettings.isPrivacyMode())
    	return ANONYMOUS;
    	else return getStartingOnyen();
//		return startingOnyen;
	}

	public void setDisplayedStartingOnyen(String newValue) {
		String oldValue = startingOnyen;
		this.startingOnyen = newValue;
		propertyChangeSupport.firePropertyChange("displayedStartingOnyen", oldValue, newValue);
		if (graderSettings.isGraderStarted() && !oldValue.equals(newValue))
		StartOnyenUserChange.newCase(newValue, graderSettings, this);
	}
	@Visible(false)
	public String getEndingOnyen() {
		return endingOnyen;
	}
	@Row(1)
    @Label("Onyen Range End:")
	public String getDisplayedEndingOnyen() {
		if (graderSettings.isPrivacyMode())
	    	return ANONYMOUS;
	    	else return getEndingOnyen();
	}
	// this makes no sense, IT should mean setEndgingOnyen
	public void setDisplayedEndingOnyen(String newValue) {
		String oldValue = endingOnyen;
		this.endingOnyen = newValue;
		propertyChangeSupport.firePropertyChange("displayedEndingOnyen", oldValue, newValue);
		if (graderSettings.isGraderStarted() &&  !oldValue.equals(newValue))
			EndOnyenUserChange.newCase(newValue, graderSettings, this);

	}
	@Row(2)
	@Override
//	@Explanation("The onyen you will start at when you review grades")
	@Explanation("A comma separated list of onyens, which overrides the start..end range and is not saved")
	public String getOnyenList() {
		return onyenList;
	}
	public static final String SELECTED_ONYENS_PROPERTY = "SelectedOnyens";
    @Override
	public void setOnyenList(String newVal) {
    	String anOldOnyens = onyenList;
		this.onyenList = newVal;
		propertyChangeSupport.firePropertyChange(SELECTED_ONYENS_PROPERTY, anOldOnyens, newVal);
	}

    @Row(3)
	@Override
	@Explanation("The onyen you will start at when you review grades")
    @Label("Manual Navigation Start Onyen:")
    @Visible(false)
	public String getGoToOnyen() {
		return goToOnyen;
	}
	public static final String GO_TO_ONYEN_PROPERTY = "GoToOnyen";
    @Override
	public void setGoToOnyen(String newVal) {
    	String anOldOnyen = goToOnyen;
		this.goToOnyen = newVal;
		propertyChangeSupport.firePropertyChange(GO_TO_ONYEN_PROPERTY, anOldOnyen, newVal);
	}
	
	@Override
	public void addPropertyChangeListener(PropertyChangeListener aListener) {
		propertyChangeSupport.addPropertyChangeListener(aListener);
		
	}
	public static void main (String[] args) {
		AnOnyenRangeModel onyenRangeModel = new AnOnyenRangeModel(null);
		ObjectEditor.edit(onyenRangeModel);
	}
	
	

}

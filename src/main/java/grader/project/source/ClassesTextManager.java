package grader.project.source;

import grader.basics.project.source.BasicTextManager;
import grader.project.view.ViewableClassDescription;

import java.util.Collection;

public interface ClassesTextManager extends BasicTextManager {
    public static final String PROJECT_DIRECTORY = ".";

//    public void writeAllSourcesText();

//    public void initializeAllSourcesText();

    public StringBuffer toStringBuffer(
            Collection<ViewableClassDescription> sourceClasses);

    public int totalTextSize(Collection<ViewableClassDescription> aSourceClasses);

//    public StringBuffer getAllSourcesText();
//
//    public void setAllSourcesText(StringBuffer anAllSourcesText);
//
//    void writeAllSourcesText(String aFileName);

	void setEditedAllSourcesText(String aFileName, String newValue);

	String getEditedAllSourcesText(String aFileName);

}
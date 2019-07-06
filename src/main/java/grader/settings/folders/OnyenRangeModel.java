package grader.settings.folders;

import util.models.PropertyListenerRegisterer;

public interface OnyenRangeModel extends PropertyListenerRegisterer{
	public String getStartingOnyen() ;
	public void setDisplayedStartingOnyen(String startingOnyen) ;
	public String getEndingOnyen();

	public void setDisplayedEndingOnyen(String endingOnyen) ;
	String getOnyenList();
	void setOnyenList(String goToOnyen);
	String getGoToOnyen();
	void setGoToOnyen(String newVal);

}

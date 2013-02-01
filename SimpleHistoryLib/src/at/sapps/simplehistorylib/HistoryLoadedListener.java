package at.sapps.simplehistorylib;

import java.util.List;

public interface HistoryLoadedListener {

	/**
	 * the HistoryLoadedListener is only called when a method returns multiple
	 * entries
	 * 
	 * @param entries
	 *            may be null!
	 */
	public void onHistoryLoaded(List<Entry> entries);

}

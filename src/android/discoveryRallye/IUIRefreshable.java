package android.discoveryRallye;

/**
 *  \brief Callback for a refreshable user interface
 *  
 *  A user interface have to refresh by another instance 
 *  should implement this interface, as a callback method.
 *  
 * */
public interface IUIRefreshable {
	/**
	 * Callback Method to refresh the user interface
	 * 
	 * Do in this method everything what is necessary
	 * to refresh the view.
	 * 
	 */
	public void uiRefresh();
}

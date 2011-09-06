/**
 * Acdroid Apps for Android
 * 
 * @author Carlos Diaz Canovas
 * @author Marcos Trujillo Seoane
 * 
 * Project Gesture Call
 * 
 */
package ac.gestureCallPro.util.contactos;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.Contacts.People;



/**
 * @author mtrujillo
 * @version $Revision: 1.0 $
 */
@SuppressWarnings("deprecation")
public class ContactCursorInf {
	public static final Cursor getCursor(Context context){
		
		
		Uri uri =  People.CONTENT_URI;
		String[] projection = new String[] { 
				People._ID,
				People.NAME,
				People.Phones.NUMBER}; 
			
			
		String selection = People.NAME + " IS NOT NULL AND " + People.Phones.NUMBER + " IS NOT NULL";
		
		String[] selectionArgs = null;
		String sortOrder = People.NAME +  " COLLATE LOCALIZED ASC";
        return ((Activity)context).managedQuery(uri, projection, selection, selectionArgs, sortOrder);
	}
}

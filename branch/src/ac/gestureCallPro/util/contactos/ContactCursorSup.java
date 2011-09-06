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
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.Data;

public class ContactCursorSup {
	public static final Cursor getCursor(Context context){
		Uri uri =  Data.CONTENT_URI;
		String[] projection = new String []{
				Data._ID,
				Data.DISPLAY_NAME,
				Phone.NUMBER,
				Phone.TYPE 
		};
		String selection = Data.MIMETYPE + "='" + Phone.CONTENT_ITEM_TYPE + "' AND "
        		+ Phone.NUMBER + " IS NOT NULL";
		
		String[] selectionArgs = null;
		String sortOrder = Data.DISPLAY_NAME + " COLLATE LOCALIZED ASC";
        return ((Activity)context).managedQuery(uri, projection, selection, selectionArgs, sortOrder);
	}
}

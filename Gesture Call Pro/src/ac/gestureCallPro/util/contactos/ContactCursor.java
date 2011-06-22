package ac.gestureCallPro.util.contactos;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.os.Build;
import android.provider.Contacts.People;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.Data;

/**
 * Con esta clase se podra acceder a la lista de contactos
 * del telefono movil independientemente de si se
 * posee un telefono con version superior a 1.6
 * o inferior.
 * 
 * @author mtrujillo y cdiaz
 *
 */
public class ContactCursor {
	
	Cursor mCursor=null;
	Context mContext;
	
	
	public ContactCursor(Context context){
		
		mContext = context;
		
		//Comprobamos la version del sistema y actuamos en consecuencia
		if (Build.VERSION.SDK_INT > 4){
			mCursor = ContactCursorSup.getCursor(mContext);
		}			
		else{
			mCursor = ContactCursorInf.getCursor(mContext);
		}
			
		
		((Activity) mContext).startManagingCursor(mCursor);
		
	}
	
	/**
	 * Devuelve el cursor con la lista de contactos
	 * 
	 * @return Cursor con la lista de contactos.
	 */
	public Cursor getCursor(){
		return mCursor;
	}
	
	
	
	
	

	/***************************************************************/
	/* Clases para obtener las constantes de los nombres de las    */
	/* columnas del cursor                                         */
	/***************************************************************/
	
	
	public String getIdNameColum(){
		if (Build.VERSION.SDK_INT > 4){
			return Data._ID;
		}			
		else{
			return  People._ID;
		}
	}
	
	
	public String getNameNameColum(){
		if (Build.VERSION.SDK_INT > 4){
			return Data.DISPLAY_NAME;
		}			
		else{
			return  People.DISPLAY_NAME;
		}
	}
	
	public String getPhoneNameColum(){
		if (Build.VERSION.SDK_INT > 4){
			return Phone.NUMBER;
		}			
		else{
			return  People.Phones.NUMBER;
		}
	}

}

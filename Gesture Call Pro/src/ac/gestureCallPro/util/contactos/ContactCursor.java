package ac.gestureCallPro.util.contactos;

import android.content.Context;
import android.database.Cursor;
import android.os.Build;

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
			
		
		
	}
	
	/**
	 * Devuelve el cursor con la lista de contactos
	 * 
	 * @return Cursor con la lista de contactos.
	 */
	public Cursor getCursor(){
		return mCursor;
	}

}

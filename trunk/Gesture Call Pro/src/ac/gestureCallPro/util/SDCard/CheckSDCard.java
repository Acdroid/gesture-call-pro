package ac.gestureCallPro.util.SDCard;

import android.content.Context;
import android.os.Environment;
import android.widget.Toast;

/**
 * @author mtrujillo
 *
 */
public class CheckSDCard {
	
	/**
	 * <b> comprobarSDCard</b><br><br>
	 *    public static Boolean comprobarSDCard()<br>
	 * <ul>Esta clase se ha implementado para facilitar la
	 * comprobacion del estado de la tarjeta SDCard.
	 * Si el estado es correcto se devolver� true.
	 * Si el estado es incorrecto se obtendra un mensaje por
	 * pantalla informando del error y se devolvera false.
	 * </ul><br><br>
	 * @return True si la SDCard esta correctametne o false e.o.c.
	 */
	public static Boolean comprobarSDCard(Context mContext){
		
		String auxSDCardStatus = Environment.getExternalStorageState();
		
		if (auxSDCardStatus.equals(Environment.MEDIA_MOUNTED))
			return true;
		else if (auxSDCardStatus.equals(Environment.MEDIA_MOUNTED_READ_ONLY)){
			Toast.makeText(mContext, "Error, la tarjeta SDCard solo se puede leer.\nEsto no impedira el buen funcionamiento" +
					"de la aplicacion de lectura", Toast.LENGTH_LONG).show();
			return false;
		}
		else if(auxSDCardStatus.equals(Environment.MEDIA_NOFS)){
			Toast.makeText(mContext, "Error, la tarjeta SDCard no se puede usar, no tiene un formato aceptado o no ests " +
					"correctamente formateada.", Toast.LENGTH_LONG).show();
			return false;
		}
			
		else if(auxSDCardStatus.equals(Environment.MEDIA_REMOVED)){
			Toast.makeText(mContext, "Error, la tarjeta SDCard no se encuentra, para utilizar el lector necesita" +
					"insertar una tarjeta SD Card en el movil.", Toast.LENGTH_LONG).show();
			return false;
		}
		else if(auxSDCardStatus.equals(Environment.MEDIA_SHARED)){
			Toast.makeText(mContext, "Error, la tarjeta SDCard no esta montada porque se esta utilizando" +
					"conectada mediante USB. Desconectela y vuelva a intentarlo.", Toast.LENGTH_LONG).show();
			return false;
		}
		else if (auxSDCardStatus.equals(Environment.MEDIA_UNMOUNTABLE)){
			Toast.makeText(mContext, "Error, la tarjeta SDCard no puede montarse.\nEsto puede deberse a que se encuentra corrupta" +
					"o dagnada.", Toast.LENGTH_LONG).show();
			return false;
		}
		else if (auxSDCardStatus.equals(Environment.MEDIA_UNMOUNTED)){
			Toast.makeText(mContext, "Error, la tarjeta SDCard esta presente en el movil pero no esta montada." +
					"Montela antes de usar el programa.", Toast.LENGTH_LONG).show();
			return false;
		}
		
		
		return true;
	}

}

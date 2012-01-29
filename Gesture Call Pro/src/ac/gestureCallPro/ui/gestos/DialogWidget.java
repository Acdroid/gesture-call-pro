package ac.gestureCallPro.ui.gestos;

import ac.gestureCallPro.R;
import ac.gestureCallPro.util.gestures.GesturesRecognizer;
import ac.gestureCallPro.util.mToast.mToast;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.gesture.GestureOverlayView;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

public class DialogWidget extends Dialog{

	private final String dir = Environment.getExternalStorageDirectory() + "/GestureCall";
	private final String fich = "gestures";
	private GestureOverlayView overlay;
	private GesturesRecognizer gr;
	private Context mContext;
	private String prediccionActual="";
	private Dialog dialog = this;


	//HAndler encargado de recibir las predicciones del
	public Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {

			String prediccion = (String)msg.obj;
			//Comprobamos si se ha detectado gesto
			if (prediccion.equals("")){
				mToast.Make(mContext, mContext.getResources().getString(R.string.no_gesto), 0);
				dialog.dismiss();
				return;
			}

			//Guardamos la prediccion actual
			prediccionActual = prediccion;
			//Ejecutamos segun la accion que este elegida
			call(prediccion);
		}

	};

	public DialogWidget(Context context) {
		super(context);
		setContentView(R.layout.widget_dialog);
		
		mContext= context;

		overlay = (GestureOverlayView)dialog.findViewById(R.id.gestures);
		try {
			gr = new GesturesRecognizer(dir,fich, overlay, handler, GesturesRecognizer.RECONOCEDOR_BASICO);
		} catch (Exception e) {
			Toast.makeText(mContext, e.getMessage() + "\nERROR while begin the Gesture Recognize!!",Toast.LENGTH_SHORT).show();
			dialog.dismiss();
			return;
		} //Reconocedor, lo cargamos con la base de datos de accesos directos


	}

	/**
	 * <p>Este metodo se encarga de realizar una llamada al
	 * numero de telefono que se le pasa como prediccion
	 * tras realizar un gesto. Se comprueba en las
	 * preferencias si se llama directamente o se muestra un
	 * dialogo antes de llamar.
	 * 
	 * @param prediccion numero de telefono al que queremos llamar.
	 */
	public void call(String prediccion){
		String url = "tel:" + prediccion;
		Intent i = new Intent(Intent.ACTION_CALL, Uri.parse(url));
		mContext.startActivity(i);

		dialog.dismiss();
	}



}

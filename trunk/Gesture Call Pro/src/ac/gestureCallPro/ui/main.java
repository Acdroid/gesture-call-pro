/**
 * Acdroid Apps for Android
 * 
 * @author Carlos Diaz Canovas
 * @author Marcos Trujillo Seoane
 * 
 * Project Gesture Call
 * 
 */
package ac.gestureCallPro.ui;


import ac.gestureCallPro.ui.cabecera.Cabecera;
import ac.gestureCallPro.ui.donate.Donate;
import ac.gestureCallPro.R;
import ac.gestureCallPro.exceptions.NoPreferenceException; 
import ac.gestureCallPro.preferences.Preferences;
import ac.gestureCallPro.ui.contactos.ListContact;
import ac.gestureCallPro.ui.gestos.GestureBuilderActivity;
import ac.gestureCallPro.util.config.AppConfig;
import ac.gestureCallPro.util.config.AppConfig.Themes;
import ac.gestureCallPro.util.gestures.GesturesRecognizer;
import ac.gestureCallPro.util.mToast.mToast;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.database.Cursor;
import android.gesture.GestureLibrary;
import android.gesture.GestureOverlayView;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.Data;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class main extends Activity { 

	public static final String NO_PREDICCION = "Sin_Resultado";
//	public static final int RESULT_OK = 0;
	public static final int RESULT_ERROR = 1;
	public static final int RESULT_SALIR = 2;
	public static final int RESULT_REALOAD_GESTURES = 3;
	public static final int RESULT_GESTO_ADD_OK = 4;
	public static final int RESULT_PREF_SAVED = 5;
	public static final int RESULT_PREF_NOT_SAVED = 6;
	public static final int ID = 0;
	public static final int DIALOG_SALIR = 0;
	public static final int DIALOG_CALL = 1;
	public static final String MY_AD_UNIT_ID = "a14daeadcc3acb6";
	public static final int ACCION_LLAMAR=0;
	public static final int ACCION_SMS=1;
	public static final int ACCION_PERDIDA=2;
	public static final int HELLO_ID = 62929;
	
	public GestureOverlayView overlay;
	public static GesturesRecognizer gr;
	public LinearLayout lay_main; 
	public  AppConfig ap;
	public Dialog dialogCall;
	public String prediccionActual="";
	public Context mContext;
	public int tipoAccion=ACCION_LLAMAR; //Accion por defecto llamar si no se pulsa ningun boton
	public CallCountDown countdown;
	
	public Cabecera cabecera;
	
	public boolean smsOn=false;
	public boolean isOnCallingSms=false;

	private final String dir = Environment.getExternalStorageDirectory() + "/GestureCall";
	private final String fich = "gestures";

	//HAndler encargado de recibir las predicciones del
	public Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {

			String prediccion = (String)msg.obj;
			//Comprobamos si se ha detectado gesto
			if (prediccion.equals("")){
				mToast.Make(mContext, getResources().getString(R.string.no_gesto), 0);
				return;
			}

			//Guardamos la prediccion actual
			prediccionActual = prediccion;
			//Ejecutamos segun la accion que este elegida
			ejecutaAccion(prediccion);
		}

	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		init();
	}

	/**
	 * <p>Metodo encargado de iniciar toda la configuracion tanto
	 * de los elementos graficos como de preparar los atributos
	 * tales como AppCondig ap o el mContext.
	 */
	private void init(){
		mContext = this;
		isOnCallingSms=false;

		//Cargamos las opciones
		ap = new AppConfig(this, AppConfig.NAME);

		overlay = (GestureOverlayView)findViewById(R.id.gestures);
		try {
			gr = new GesturesRecognizer(dir,fich, overlay, handler, GesturesRecognizer.RECONOCEDOR_BASICO);
		} catch (Exception e) {
			Toast.makeText(this, e.getMessage() + "\nERROR while begin the Gesture Recognize!!",Toast.LENGTH_SHORT).show();
		} //Reconocedor, lo cargamos con la base de datos de accesos directos
		
		//notificacion
		setStatusBarNotification();
		
		//La cuenta atras para las llamadas
		long intervalo; 
		try {
			intervalo = ap.getLong(AppConfig.S_AFTER_CALL);
		} catch (NoPreferenceException e) {
			Log.i("Gesture Call","No Secs After Call preference. Apply Default 3000");
			intervalo=3000; //Ponemos intervalo por defecto si ocurre un error
			ap.put(3000,AppConfig.S_AFTER_CALL);
		}
		
		//Configuramos la cabecera
		cabecera = (Cabecera)findViewById(R.id.main_cabecera);
		cabecera.setVisibleAccion();
		cabecera.setOnActionClick(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				clickSms(arg0);
				
			}
		});
		cabecera.setOnOptionClickWitReturn(ID);
		
		
		
		
		countdown = new CallCountDown(intervalo, 1000);
		//Iniciamos el dialog
		//TODO cambiar esto , hay que sacarlo fuera de aqui
		createDialog();
		
		//accion por defecto
		setDefaultAction();
		
		//Cargamos tema
		lay_main = (LinearLayout)findViewById(R.id.lay_main);
		setTheme();
		
		
		//Aviso por pantalla //TODO donde poner esto? En appconfig o aqui
		mToast.Make(this, getResources().getString(R.string.makeGesture), 0);
		
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		@SuppressWarnings("unused")
		Dialog dialog;
		switch(id) {
		case DIALOG_SALIR:
			AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
			builder.setMessage(mContext.getResources().getString(R.string.di_salir))
			.setCancelable(false)
			.setPositiveButton(mContext.getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					//Comunicador.this.finish();
					getStore().save();
					((Activity) mContext).setResult(RESULT_OK);
					((Activity) mContext).finish();
				}
			})
			.setNegativeButton(mContext.getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					dialog.cancel();
				}
			});
			AlertDialog alert = builder.create();
			return alert;

		case DIALOG_CALL:

			return dialogCall;
		default:
			return dialog = null;
		}
	}

	public AppConfig getAp() {
		return ap;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu_escritorio, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
		switch (item.getItemId()) {
		case R.id.me_salir:
			showDialog(DIALOG_SALIR);
			return true;
		case R.id.me_gestures:
			clickEdit(null);
			return true;
		case R.id.me_opciones:
			clickOpciones(null);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == ID){
			switch (resultCode){
			case RESULT_OK:
				break;
			case RESULT_CANCELED:
				boolean aux;
				try {
					aux = ap.getBool(AppConfig.RETURN_AFTER_CALL);
				} catch (NoPreferenceException e) {
					Log.i("Gesture Call","No Return After Call preference. Apply Default false");
					isOnCallingSms=false;//Si falla no salimos
					ap.put(false,AppConfig.RETURN_AFTER_CALL);
					return;
				}
				//Si venimos de una llamada y se quiere salir despues de llamar
				if ((aux) && (isOnCallingSms)){
					isOnCallingSms=false;
					main.this.finish();
				}
				else{
					//sino volvemos a poner isOnCallingSms a fale
					isOnCallingSms=false;
				}
				
				break;
			case RESULT_ERROR:
				break;
			case RESULT_SALIR:
				main.this.finish();
				break;
			case RESULT_REALOAD_GESTURES:
				getStore().load();
				break;
			case RESULT_PREF_SAVED:
				setStatusBarNotification();
				
				//Cargamos el contador
				//La cuenta atras para las llamadas
				long intervalo;
				try {
					intervalo = ap.getLong(AppConfig.S_AFTER_CALL);
				} catch (NoPreferenceException e) {
					Log.i("Gesture Call","No Secs After Call preference. Apply Default 3000");
					intervalo=3000; //Ponemos intervalo por defecto si ocurre un error
					ap.put(3000,AppConfig.S_AFTER_CALL);
				}
				countdown = new CallCountDown(intervalo, 1000);
				setTheme();
				setDefaultAction();
				break;
			default:
			}

			getStore().load();
		}
	}
	
	
	
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	
	
	/**
	 * Crea una notificacion en la barra de status
	 * para acceder directamente a la aplicación
	 * */
	public void setStatusBarNotification(){
		
		String ns = Context.NOTIFICATION_SERVICE;
		NotificationManager mNotificationManager = (NotificationManager) getSystemService(ns);
		
		//Comprobamos si el usuario desea usar notificaciones
		boolean notif;
		try {
			notif = ap.getBool(AppConfig.NOTIFICATION);
		} catch (NoPreferenceException e) {
			Log.i("Gesture Call","No Notification Icon Preference. Apply Default: True");
			notif = true; //en caso de error SI notificaciones
			ap.put(true,AppConfig.NOTIFICATION);			
		}
		
		if (!notif){
			mNotificationManager.cancel(HELLO_ID);
			return;
		}
		
		
		//Creamos la notificacion
		int icon = R.drawable.icon;
		CharSequence tickerText = getResources().getString(R.string.notification_hello);
		long when = System.currentTimeMillis();

		Notification notification = new Notification(icon, tickerText, when);
		
		notification.flags |= Notification.FLAG_NO_CLEAR | Notification.FLAG_ONGOING_EVENT;
				
		Context context = getApplicationContext();
		CharSequence contentTitle = getString(R.string.app_name);
		CharSequence contentText  = getString(R.string.click_gesture);
		Intent notificationIntent = new Intent(this, main.class);
		PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);

		notification.setLatestEventInfo(context, contentTitle, contentText, contentIntent);
		
		
		

		mNotificationManager.notify(HELLO_ID, notification);
	}
	
	/**
	 * Establece la accion por defecto deseada por el usuario
	 */
	public void setDefaultAction(){
		
		int  actDef;
		try {
			actDef = ap.getInt(AppConfig.ACCION_POR_DEFECTO);
		} catch (NoPreferenceException e) {
			Log.i("Gesture Call","No Def Action Preference. Apply Default: Call");
			actDef = ACCION_LLAMAR;
			ap.put(ACCION_LLAMAR,AppConfig.ACCION_POR_DEFECTO);		
			
		}
	
		switch (actDef) {
		case ACCION_LLAMAR:
			tipoAccion = ACCION_LLAMAR;
			smsOn = false;
			cabecera.setAccionImageResource(R.drawable.phone);
			break;
		case ACCION_SMS:
			tipoAccion = ACCION_SMS;
			smsOn = true;
			cabecera.setAccionImageResource(R.drawable.sms);
			break;
		case ACCION_PERDIDA:
			tipoAccion = ACCION_PERDIDA;
			smsOn = false;
			cabecera.setAccionImageResource(R.drawable.phone);
			break;
		default:
			break;
		}
	}
	
	
	/**
	 * Carga el tema segun las preferencias del usuarios
	 * 
	 */
	public void setTheme(){
		int  theme;
		try {
			theme = ap.getInt(AppConfig.THEME);
		} catch (NoPreferenceException e) {
			Log.i("Gesture Call","No Theme Preference. Apply Default: Grey");
			theme = Themes.GREY;
			ap.put(Themes.GREY,AppConfig.THEME);			
		}
		
		
		switch (theme) {
		case Themes.GREY:
			lay_main.setBackgroundResource(R.drawable.background_grey);
			//overlay.setGestureColor(R.color.overlay_grey);
			//overlay.setUncertainGestureColor(R.color.overlay_grey_uncertain);
			
			break;
		case Themes.BLUE:
			lay_main.setBackgroundResource(R.drawable.background_blue_gradient);
			//overlay.setGestureColor(R.color.overlay_blue);
			//overlay.setUncertainGestureColor(R.color.overlay_blue_uncertain);
			
			break;
		case Themes.GREEN:
			lay_main.setBackgroundResource(R.drawable.background_green_gradient);
			//overlay.setGestureColor(R.color.overlay_green);
			//overlay.setUncertainGestureColor(R.color.overlay_green_uncertain);
			
			break;
		case Themes.BLACK:
			lay_main.setBackgroundResource(R.drawable.background_black_gradient);
			//overlay.setGestureColor(R.color.overlay_green);
			//overlay.setUncertainGestureColor(R.color.overlay_green_uncertain);
			
			break;
		case Themes.WHITE:
			lay_main.setBackgroundResource(R.drawable.background_white_gradient);
			//overlay.setGestureColor(R.color.overlay_green);
			//overlay.setUncertainGestureColor(R.color.overlay_green_uncertain);
			
			break;

		default:
			lay_main.setBackgroundResource(R.drawable.background_grey);
			//overlay.setGestureColor(R.color.overlay_grey);
			//overlay.setUncertainGestureColor(R.color.overlay_grey_uncertain);
			
			break;
		}
	}
	
	
	

	/**
	 * <p>Metodo que llama a la accion determinada (llamar
	 * enviar sms, hacer una llamada perdida y nuevas que
	 * se vayan inventando) segun el tipo de accion que esté 
	 * seleccionada.
	 * 
	 * @param prediccion numero de telefono al que queremos llamar.
	 */
	public void ejecutaAccion(String prediccion){
		int accionActual = getTipoAccion();

		
		switch (accionActual) {
		case ACCION_LLAMAR:			
			call(prediccion);			
			break;
		case ACCION_SMS:
			sms(prediccion);
			break;
		case ACCION_PERDIDA:
			//isOnCallingSms=true;
			missedCall(prediccion);
			break;
		default:
			break;
		}


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

		//Comprobamos si se quiere avisar antes de llamar o no
		try {
			if (ap.getBool(AppConfig.AVISO_AL_LLAMAR)){ 

				//Obtenemos el textView para cambiar el nombre de la persona a la que queremos llamar
				dialogCall.setTitle(mContext.getResources().getString(R.string.calling));
				TextView t = (TextView)dialogCall.findViewById(R.id.dialog_text_contact);

				TextView tquieres = (TextView)dialogCall.findViewById(R.id.qieres);
				tquieres.setText(R.string.dialog_bc_seriusly);
				
				Uri uri =  Data.CONTENT_URI;
				String[] projection = new String []{
						Data.DISPLAY_NAME
				};
				String[] selectionArgs = null;
				String sortOrder = Data.DISPLAY_NAME + " COLLATE LOCALIZED ASC";
				String selection = Phone.NUMBER +"='" + getPrediccionActual() + "'";
				Cursor c =  managedQuery(uri, projection, selection, selectionArgs, sortOrder);
				startManagingCursor(c);
				if(c.moveToFirst()){ 
					t.setText(c.getString(c.getColumnIndex(Data.DISPLAY_NAME))+  "?");       			
				}
			else{
					t.setText(getPrediccionActual());
				}
				showDialog(DIALOG_CALL);
								
				countdown.setButton((Button) dialogCall.findViewById(R.id.dialog_button_yes));
				countdown.start();
				

			}
			else{//Si no, se llama directamente

				String url = "tel:" + prediccion;
				Intent i = new Intent(Intent.ACTION_CALL, Uri.parse(url));
				isOnCallingSms=true;
				startActivityForResult(i,ID);
				
			}
		} catch (NoPreferenceException e) { //En caso de no existir la opcion guardada correctamente se llama
			Log.i("Gesture Call","No Before Call preference. Calling");
			String url = "tel:" + prediccion;
			Intent i = new Intent(Intent.ACTION_CALL, Uri.parse(url));
			isOnCallingSms=true;
			startActivityForResult(i,ID);
			ap.put(true, AppConfig.AVISO_AL_LLAMAR);
		}

	}

	/**
	 * <p>Este metodo se encarga de iniciar el envio de
	 * un sms al numero de telefono que se le pasa como
	 * prediccion tras realizar un gesto. Se comprueba en las
	 * preferencias si se llama directamente o se muestra un
	 * dialogo antes de llamar.
	 * 
	 * @param prediccion numero de telefono al que queremos
	 * enviar un sms.
	 */
	public void sms(String prediccion){

		//Comprobamos si se quiere avisar antes de enviar el sms
		try {
			if (ap.getBool(AppConfig.AVISO_AL_LLAMAR)){

				//Obtenemos el textView para cambiar el nombre de la persona a la que queremos llamar
				dialogCall.setTitle(mContext.getResources().getString(R.string.sms));
				TextView tquieres = (TextView)dialogCall.findViewById(R.id.qieres);
				tquieres.setText(R.string.dialog_bc_seriusly_sms);
				
				
				TextView t = (TextView)dialogCall.findViewById(R.id.dialog_text_contact);

				Uri uri =  Data.CONTENT_URI;
				String[] projection = new String []{
						Data.DISPLAY_NAME
				};
				String[] selectionArgs = null;
				String sortOrder = Data.DISPLAY_NAME + " COLLATE LOCALIZED ASC";
				String selection = Phone.NUMBER +"='" + getPrediccionActual() + "'";
				Cursor c =  managedQuery(uri, projection, selection, selectionArgs, sortOrder);
				startManagingCursor(c);
				if(c.moveToFirst()){ 
					t.setText(c.getString(c.getColumnIndex(Data.DISPLAY_NAME)) +  "?");       			
				}
				else{
					t.setText(getPrediccionActual());
				}
				showDialog(DIALOG_CALL);
				
				countdown.setButton((Button) dialogCall.findViewById(R.id.dialog_button_yes));
				countdown.start();

			}
			else{//En caso de false se envia directamente
				String url = "sms:" + prediccion;
				Intent iSMS = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
				isOnCallingSms=true;
				startActivityForResult(iSMS,ID);
			}
		} catch (NoPreferenceException e) { //En caso de no existir la opcion se envia directamente
			Log.i("Gesture Call","No Before Call preference. Sending sms");
			String url = "sms:" + prediccion;
			Intent iSMS = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
			isOnCallingSms=true;
			startActivityForResult(iSMS,ID);
			ap.put(true, AppConfig.AVISO_AL_LLAMAR);
		}

	}

	/**
	 * <p>Este metodo se encarga de realizar una llamada
	 * perdida al numero de telefono que se le pasa como
	 * prediccion tras realizar un gesto. Se comprueba en las
	 * preferencias si se llama directamente o se muestra un
	 * dialogo antes de llamar.
	 * 
	 * @param prediccion numero de telefono al que queremos llamar.
	 */
	public void missedCall(String prediccion){

		//TODO hay que hacerlo to, investigar y esas cosas .
	}

	/**
	 * Metodo para la creacion del dialgo, para que este mas accesible
	 * y separado del resto del codigo
	 */
	private void createDialog(){
		dialogCall = new Dialog(mContext);
		dialogCall.setContentView(R.layout.dialog_b4_call);
		dialogCall.setTitle(mContext.getResources().getString(R.string.calling));
		
		//Cuando el usuario pulsa atras, cancelamos la cuenta atras
		dialogCall.setOnCancelListener(new OnCancelListener() {			
			@Override
			public void onCancel(DialogInterface dialog) {
				countdown.setIsPressedButtonSi(false);
				countdown.cancel();				
			}
		});
		
		Button buttonDialog = (Button) dialogCall.findViewById(R.id.dialog_button_yes);
		
		
		buttonDialog.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				countdown.setIsPressedButtonSi(true);
				callActions();
				dialogCall.dismiss();
			}
		});
		
		buttonDialog = (Button) dialogCall.findViewById(R.id.dialog_button_no);
		
		
		buttonDialog.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				countdown.setIsPressedButtonSi(false);
				countdown.cancel();	
				dialogCall.dismiss();
			}
		});

	}
	
	
	
	/**
	 * Este metodo gestiona las acciones a tomar en las llamadas
	 * */
	private void callActions() {
		
		CheckBox c = (CheckBox)dialogCall.findViewById(R.id.dialog_check);
		if(c.isChecked()){
			getAp().put(false, AppConfig.AVISO_AL_LLAMAR);
		}

		//Marcamos como que se esta llamando o enviando un smsm para poder salir
		//directamente al volver si es necesario
		isOnCallingSms=true;
		
		//Dependiendo del tipo de accion realizamos una u otra cosa
		String url = "";
		switch (tipoAccion) {
		case ACCION_LLAMAR:
			url=  "tel:" + getPrediccionActual();
			Intent i = new Intent(Intent.ACTION_CALL, Uri.parse(url));
			startActivityForResult(i,ID);
			break;
		case ACCION_SMS:
			url = "sms:" + getPrediccionActual();
			Intent iSMS = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
			startActivityForResult(iSMS,ID);
			break;
		case ACCION_PERDIDA:
			mToast.Make(mContext, "Action is not yet available", 0);
			break;
		default:
			break;
		}
		
	}
	
	
	/**
	 * Clase que gestiona la cuenta atras antes de una "llamada"
	 * 1.Crear el objeto con new
	 * 2.Llamar a setButton
	 * 3.Llamar a start
	 * @author mtrujillo
	 **/
    class CallCountDown extends CountDownTimer{

    	/**
    	 * Field button.
    	 */
    	Button button;
    	/**
    	 * Field isPressedButtonSi.
    	 */
    	boolean isPressedButtonSi=false;
    	
    	/**
    	 * Constructor for CallCountDown.
    	 * @param millisInFuture long
    	 * @param countDownInterval long
    	 */
    	public CallCountDown(long millisInFuture, long countDownInterval) {
    		super(millisInFuture, countDownInterval);
    	}

    	/**
    	 * Method setButton.
    	 * @param b Button
    	 */
    	public void setButton(Button b) {    		
    		this.button = b;
    	}
    	
    	/**
    	 * Method setIsPressedButtonSi.
    	 * @param pressed boolean
    	 */
    	public void setIsPressedButtonSi(boolean pressed){
    		isPressedButtonSi = pressed;
    	}
    	
    	/**
    	 * Method onFinish.
    	 */
    	@Override
    	public void onFinish() {
    		if(isPressedButtonSi){
    			isPressedButtonSi=false;
    			return;
    		}
    		callActions();
    		dialogCall.dismiss();
    	}
    	

    	/**
    	 * Method onTick.
    	 * @param millisUntilFinished long
    	 */
    	@Override
    	public void onTick(long millisUntilFinished) {
    		button.setText(getString(R.string.yes)+" ("+millisUntilFinished/1000+")");
    	}

    }
	
	@Override
	protected void onRestart() {
		
		super.onRestart();
		if(smsOn){
			tipoAccion=ACCION_SMS;
			//OJO esto se cambiara segun los temas
			cabecera.setAccionImageResource(R.drawable.sms);
		}
		else{
			tipoAccion=ACCION_LLAMAR;
			cabecera.setAccionImageResource(R.drawable.phone);
			//OJO desactivar otros elementos como llamada perdida
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		
		if(smsOn){
			tipoAccion=ACCION_SMS;
			//OJO esto se cambiara segun los temas
			cabecera.setAccionImageResource(R.drawable.sms);
		}
		else{
			tipoAccion=ACCION_LLAMAR;
			cabecera.setAccionImageResource(R.drawable.phone);
			//OJO desactivar otros elementos como llamada perdida
		}
	}

    /**
     * Method clickDonate.
     * @param v View
     */
    public void clickDonate(View v){
    	Intent i = new Intent(this,Donate.class);
    	//Intent i = new Intent(this,DonateAmazon.class); //Para amazon
    	startActivity(i);
    }
	
	
	
	/* **************** Funciones auxiliares o menores ****************** */

	/**
	 * Method clickAdd.
	 * @param v View
	 */
	public void clickAdd(View v){
		Intent i = new Intent(main.this,ListContact.class);
		startActivityForResult(i, ID);
	}
	
	/**
	 * Method clickSms.
	 * @param v View
	 */
	public void clickSms(View v){
		
		if(smsOn){
			smsOn=false;
			tipoAccion=ACCION_LLAMAR;
			//OJO esto se cambiara segun los temas
			cabecera.setAccionImageResource(R.drawable.phone);
			mToast.Make(this, getResources().getString(R.string.smsoff), 0);
		}
		else{
			smsOn=true;
			tipoAccion=ACCION_SMS;
			cabecera.setAccionImageResource(R.drawable.sms);
			
			mToast.Make(this, getResources().getString(R.string.smson), 0);
			
			//OJO desactivar otros elementos como llamada perdida
		}
		
	}

	/**
	 * Method clickEdit.
	 * @param v View
	 */
	public void clickEdit(View v){
		Intent i = new Intent(main.this,GestureBuilderActivity.class);
		startActivityForResult(i,ID);
	}

	/**
	 * Method clickOpciones.
	 * @param v View
	 */
	public void clickOpciones(View v){
		Intent i = new Intent(this,Preferences.class);
		startActivityForResult(i, ID);

	}

	/**
	 * Method getStore.
	
	 * @return GestureLibrary */
	public static GestureLibrary getStore(){
		return gr.getStore();    	
	}


	/**
	 * Method getPrediccionActual.
	
	 * @return String */
	public String getPrediccionActual() {
		return prediccionActual;
	}

	/**
	 * Method getTipoAccion.
	
	 * @return int */
	public int getTipoAccion(){
		return tipoAccion;
	}
	

	/**
	 * Method onSaveInstanceState.
	 * @param outState Bundle
	 */
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		outState.putBoolean("ISCALLSMS", isOnCallingSms);
		super.onSaveInstanceState(outState);
	}





	/**
	 * Method onRestoreInstanceState.
	 * @param savedInstanceState Bundle
	 */
	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		if ( (savedInstanceState != null) && (savedInstanceState.containsKey("ISCALLSMS")) ){
			isOnCallingSms = savedInstanceState.getBoolean("ISCALLSMS");
		}
		super.onRestoreInstanceState(savedInstanceState);
	}





	/**
	 * Method onStop.
	 */
	@Override
	protected void onStop() {
		super.onStop();
		countdown.cancel();
	}

	
	
	
	
	
	
	
}
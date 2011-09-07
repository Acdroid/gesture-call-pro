/**
s * Acdroid Apps for Android
 * 
 * @author Carlos Diaz Canovas
 * @author Marcos Trujillo Seoane
 * 
 * Project Gesture Call
 * 
 */
package ac.gestureCallPro.util.config;

import java.io.File;
import java.io.IOException;

import ac.gestureCallPro.R;
import ac.gestureCallPro.exceptions.NoPreferenceException;
import ac.gestureCallPro.ui.main;
import ac.gestureCallPro.util.mToast.mToast;
import ac.gestureCallPro.util.shortcut.CreateShortcut;
import android.app.Dialog;
import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

/**
 * AppConfig.java 30/03/2011
 * 
 * Esta clase, que extiende de la clase MSharedPreferences,
 * se encarga de interceder entre esta y la aplicacion
 * facilitando su uso. Contiene una serie de Static con
 * todas las posibles preferencias que se pueden guardar.
 * Durante el primer uso guarda la configuracion inicial.
 * @author mtrujillo & cdiaz
 * @version $Revision: 1.0 $
 */
public class AppConfig extends MSharedPreferences{
	public static final String VERSION = "version"; //version
	@Deprecated
	public static final String FIRST_TIME = "first_time"; //deprecated
	public static final String NAME = "GestureCall"; //Nombre del fichero de configuracion
	public static final String AVISO_AL_LLAMAR = "ask_before_call"; //Se avisara antes de lamar o no
	public static final String DEVELOPERS = "developers"; //Solo para desarrollar
	public static final String NOTIFICATION = "notification"; //Poner o no notificacion
	public static final String RETURN_AFTER_CALL = "returnAfterCall"; //Salir de la app al terminar de llamar
	public static final String ACCION_POR_DEFECTO = "defAction"; //accion por defecto llamar, sms o perdida?
	public static final String THEME = "theme"; //Theme elegido
	public static final String S_AFTER_CALL = "secondsAfterCall"; //tiempo en segundos antes de llamar, sms o perdida
	
    public static final class Themes {
        public static final int GREY = 0;
        public static final int GREEN = 1;
        public static final int BLUE = 2;
        public static final int BLACK = 3;
        public static final int WHITE = 4;
    }
   
	private static final String dir = Environment.getExternalStorageDirectory() + "/GestureCall";
	private final String fich = dir + "/gestures";



	/**
	 * Constructor for AppConfig.
	 * @param mContext Context
	 * @param name String
	 */
	public AppConfig(Context mContext, String name){
		super(mContext,name);
		
		//para el desarrollo por si se quiere hacer cosas especiales
//		put(true,DEVELOPERS);
		try {
			if( pref.contains(DEVELOPERS) && getBool(DEVELOPERS) ){
				makeDevelopers();
				return;
			}
		} catch (NoPreferenceException e) {
			Log.i("Gesture Call","Normal mode");
		}
		

		if(!pref.contains(VERSION)){
			makeAll();
		}
		else{
			checkVersion();
		}
		

	}
	
	/**
	 * Crea la estructura hasta el archivo deseado
	 * 
	 * @param dirPath El directorio completo donde esta el archivo sin el nombre del archivo
	 * @param fullPath
	
	 * 
	 * * @throws NoContactFileException  */
	public static void createStructure(String dirPath, String fullPath){

		File file = new File(fullPath);


		File directory = new File(dirPath);		    	

		//Intentamos crear los directorios si no existen ya
		if (!directory.exists()) {
			boolean okDir  = directory.mkdirs();

			if (!okDir) {
				Log.e("GestureCall_AC", "Unable to create directory: "+dirPath);
				//throw new NoFileException("Unable to create directory: "+dirPath);
			}
		}

		//Intentamos pues crear el archivo
		boolean okFile;
		try {
			okFile = file.createNewFile();


			if (!okFile) {
				Log.e("GestureCall_AC", "Unable to create file: "+fullPath);
				//throw new NoFileException("Unable to create file: "+fullPath);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}



		Log.i("GestureCall_AC", "Config file created");



	}
	
	/**
	 * Llama a todas las versiones
	 */
	private void makeAll(){
		makeV1();
		makeV2();
		makeV3();
		makeV4();
	}
	
	/**
	 * Comprueba la version de la configuracion y dependiendo
	 * de esto llama a las funciones necesarias
	 */
	private void checkVersion(){
		int ver;
		try {
			ver = getInt(VERSION);
		} catch (NoPreferenceException e) {
			Log.i("Gesture Call","Preference Version not found. Save all the default preferences");
			ver = 0; //Si falla ponemos que es version 0 y que sobreescriba todo
		}
		
		if (ver < 1){
			makeAll();
			return;
		}
		if (ver < 2){
			makeV2();
		}
		
		if (ver < 3){
			makeV3();
		}
		
		if (ver < 4){
			makeV4();
		}
		
		
	}
	
	/**
	 * Primera version de las opciones con la opcion de
	 * aviso al llamar e inclusion de la version
	 */
	private void makeV1(){		
		//Para los antiguos si no contine aviso al llamar lo guardamos
		//Esto lo hacemos apra que se mantenga la opcion de los que ya
		//tienen la version antigua de GC
		if(!pref.contains(AVISO_AL_LLAMAR))
			put(true,AVISO_AL_LLAMAR);
		
		put(1,VERSION);
		createStructure(dir, fich);
	}
	
	/**
	 * Primera version de las opciones con la opcion de
	 * aviso al llamar e inclusion de la version
	 * 1.2.2
	 */
	private void makeV2(){		
		//put(true,DEVELOPERS); //SOLO PARA DESARROLLADORES, PONER A FALSE!
		put(2,VERSION);
	}

	/**
	 * Primera version de las opciones con la opcion de
	 * aviso al llamar e inclusion de la version
	 * 
	 * 2.1
	 * 
	 */
	private void makeV3(){		
		//put(true,DEVELOPERS); //SOLO PARA DESARROLLADORES, PONER A FALSE!
		
		put(true,NOTIFICATION);
		put(false,RETURN_AFTER_CALL);
		put(main.ACCION_LLAMAR,ACCION_POR_DEFECTO);
		put(Themes.BLACK,THEME);
		put(new Long(3000),S_AFTER_CALL);
		put(3,VERSION); //Imprescindible siempre poner
		
		Dialog dialog = new Dialog(mContext);
		dialog.setContentView(R.layout.whats_new_firsttime);
		Button b;
		b = (Button)dialog.findViewById(R.id.whats_new_button);
		b.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				CreateShortcut.create(mContext);
				
			}
		});
		
		dialog.setTitle("Whats new");
		
		dialog.show();
	}

	/**
	 * Cuarta version de las opciones con la opcion de
	 * aviso al llamar e inclusion de la version
	 * 
	 * 2.1.1
	 * 
	 */
	private void makeV4(){
		//Falseamos las notificaciones
		//para que no haya quejas
		put(false,NOTIFICATION);
		mToast.Make(mContext, mContext.getResources().getString(R.string.aviso_notificacion),1);
		put(4,VERSION); //Imprescindible siempre poner
	}
	
	
	
	/**
	 * Para el desarrollo por si se quieren probar cosas
	 * Por ejemplo para a�adir nuevas opciones
	 * durante el desarrollo
	 */
	private void makeDevelopers(){
//		Log.d("DEBUG","pasa por developers");
//		//Poner flags que se quieran probar
// 		put(main.ACCION_SMS,ACCION_POR_DEFECTO);
//		put(true,NOTIFICATION);
//		put(Themes.GREEN,THEME);
//		put(new Long(4000),S_AFTER_CALL);
//		logOptions(); //Muestra todas las opciones menos FIRST_TIME

	}
	
	/**
	 * Muestra por log todas las opciones:
	 */
	@SuppressWarnings("unused")
	private void logOptions(){
		
		try {
			Log.d("DEBUG",ACCION_POR_DEFECTO + " " +  getInt(ACCION_POR_DEFECTO) + " (0 llamar,1 sms, 2 perdida)" ) ;
					
			Log.d("DEBUG", AVISO_AL_LLAMAR + " " + getBool(AVISO_AL_LLAMAR)  );
			Log.d("DEBUG",DEVELOPERS + " " + getBool(DEVELOPERS));
			Log.d("DEBUG",NOTIFICATION + " " + getBool(NOTIFICATION));
			Log.d("DEBUG",RETURN_AFTER_CALL + " " + getBool(RETURN_AFTER_CALL));
			Log.d("DEBUG",VERSION + " " + getInt(VERSION));
			Log.d("DEBUG",THEME + " " + getInt(THEME) + "  (0 gris,1verde,2azul)");
			Log.d("DEBUG",S_AFTER_CALL + " " + getLong(S_AFTER_CALL));
			
			
		} catch (NoPreferenceException e) {
			Log.d("Gesture Call","Error al debuguear las opciones. Alguna no se encuentra.");
		}
	}
	
}

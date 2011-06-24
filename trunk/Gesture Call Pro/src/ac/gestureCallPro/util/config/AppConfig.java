
package ac.gestureCallPro.util.config;

import java.io.File;
import java.io.IOException;
import java.util.GregorianCalendar;

import ac.gestureCallPro.exceptions.NoPreferenceException;
import ac.gestureCallPro.ui.main;
import android.content.Context;
import android.os.Environment;
import android.util.Log;

/**
 * AppConfig.java 30/03/2011
 * 
 * Esta clase, que extiende de la clase MSharedPreferences,
 * se encarga de interceder entre esta y la aplicacion
 * facilitando su uso. Contiene una serie de Static con
 * todas las posibles preferencias que se pueden guardar.
 * Durante el primer uso guarda la configuraciï¿½n inicial.
 * @author mtrujillo & cdiaz
 */
public class AppConfig extends MSharedPreferences{
	public static final String VERSION = "version";
	public static final String FIRST_TIME = "first_time";
	public static final String NAME = "GestureCall";
	public static final String AVISO_AL_LLAMAR = "ask_before_call";
	public static final String DEBUG = "debug";
	public static final String NOTIFICATION = "notification";
	public static final String RETURN_AFTER_CALL = "returnAfterCall";
	public static final String ACCION_POR_DEFECTO = "defAction";
	public static final String THEME = "theme";
	
    public static final class Themes {
        public static final int GREY = 0;
        public static final int GREEN = 1;
        public static final int BLUE = 2;
    }
	

	private static final String dir = Environment.getExternalStorageDirectory() + "/GestureCall";
	private final String fich = dir + "/gestures";


	public AppConfig(Context mContext, String name){
		super(mContext,name);
		
		//para el desarrollo por si se quiere hacer cosas especiales
		try {
			if( (pref.contains(DEBUG)) && (getBool(DEBUG) == true)){
				makeDEBUG();
				return;
			}
		} catch (NoPreferenceException e) {
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
	 * @throws NoContactFileException 
	 * 
	 * */
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
			// TODO Auto-generated catch block
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
			//TODO revisar
			ver = 0; //Si falla ponemos que es version 0 y que sobreescriba todo
		}
		
		if (ver < 1){
			makeAll();
			return;
		}
		if (ver < 2){
			makeV2();
			return;
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
	 */
	private void makeV2(){		
		//put(true,DEBUG); //SOLO PARA DESARROLLADORES, PONER A FALSE!
		
		put(true,NOTIFICATION);
		put(false,RETURN_AFTER_CALL);
		put(main.ACCION_LLAMAR,ACCION_POR_DEFECTO);
		put(Themes.GREY,THEME);
		put(2,VERSION); //Imprescindible siempre poner
	}

	
	
	
	/**
	 * Para el desarrollo por si se quieren probar cosas
	 * Por ejemplo para añadir nuevas opciones
	 * durante el desarrollo
	 */
	private void makeDEBUG(){
		//Poner flags que se quieran probar
//		put(main.ACCION_LLAMAR,ACCION_POR_DEFECTO);
//		put(true,NOTIFICATION);
		put(Themes.BLUE,THEME);
		logOptions(); //Muestra todas las opciones menos FIRST_TIME
		return;
	}
	
	/**
	 * Muestra por log todas las opciones:
	 */
	private void logOptions(){
		
		try {
			Log.d("DEBUG",ACCION_POR_DEFECTO + " " +  getInt(ACCION_POR_DEFECTO)) ;
					
			Log.d("DEBUG", AVISO_AL_LLAMAR + " " + getBool(AVISO_AL_LLAMAR)  );
			Log.d("DEBUG",DEBUG + " " + getBool(DEBUG));
			Log.d("DEBUG",NOTIFICATION + " " + getBool(NOTIFICATION));
			Log.d("DEBUG",RETURN_AFTER_CALL + " " + getBool(RETURN_AFTER_CALL));
			Log.d("DEBUG",VERSION + " " + getInt(VERSION));
			Log.d("DEBUG",THEME + " " + getInt(THEME) + "  (0 gris,1verde,2azul)");
			
			
		} catch (NoPreferenceException e) {
			Log.d("GestureCall_AC","Falla al pintar en el log en la funcion debug de AppConfig. Te falta algun registro.");
		}
	}
	
}

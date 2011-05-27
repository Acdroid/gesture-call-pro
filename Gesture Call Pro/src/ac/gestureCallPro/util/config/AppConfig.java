
package ac.gestureCallPro.util.config;

import java.io.File;
import java.io.IOException;

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
 * Durante el primer uso guarda la configuraci�n inicial.
 * @author mtrujillo & cdiaz
 */
public class AppConfig extends MSharedPreferences{
	public static final String FIRST_TIME = "first_time";
	public static final String NAME = "GestureCall";
	public static final String AVISO_AL_LLAMAR = "ask_before_call";

	private static final String dir = Environment.getExternalStorageDirectory() + "/GestureCall";
	private final String fich = dir + "/gestures";


	public AppConfig(Context mContext, String name){
		super(mContext,name);

		//Comprobamos si es la primera vez que se llama alconstructor, en tal caso
		//se guardan los valores iniciales
		if(!pref.contains(FIRST_TIME)){
			//mToast.Make(mContext,mContext.getResources().getString(R.string.first_time_toast), 1);

			//Procedemos a cargar los valores por primera vez en las preferencias.
			//Estos valores son por defecto

			//Valores por defectos
			put(false,FIRST_TIME); //Flag para indicar que no es la primera vez que se usa
			put(true,AVISO_AL_LLAMAR);
			

			createStructure(dir, fich);


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

}

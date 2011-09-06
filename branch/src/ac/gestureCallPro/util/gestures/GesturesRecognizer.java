/**
 * Acdroid Apps for Android
 * 
 * @author Carlos Diaz Canovas
 * @author Marcos Trujillo Seoane
 * 
 * Project Gesture Call
 * 
 */
package ac.gestureCallPro.util.gestures;

import java.io.File;
import java.util.ArrayList;


import android.content.Context;
import android.gesture.Gesture;
import android.gesture.GestureLibraries;
import android.gesture.GestureLibrary;
import android.gesture.GestureOverlayView;
import android.gesture.GestureOverlayView.OnGesturePerformedListener;
import android.gesture.Prediction;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

/**
 * Gestures.java 02/03/2011
 * @author mtrujillo
 */
public class GesturesRecognizer implements OnGesturePerformedListener{

	public static final int RECONOCEDOR_BASICO = 1;

	public static GestureLibrary Store; //Libreria de gestos
	private File mStoreFile; //path de la libreria de gestos
	private GestureOverlayView overlay;
	private Handler handler;
	private int tipoReconocedor = 0;



	/**
	 * <b>GesturesRecognizer</b><br><br>
	 *    private GesturesRecognizer()<br>
	 * <ul>Constructor de la clase GesturesRecognizer</ul><br><br>
	 * @param id Id de la clase R.raw con el identificador de la 
	 * base de datos de gestos a usar (recomendado para accesos
	 * directos gestuales)
	 * @param Context context desde el que se llama a la clase
	 * (normalmente se debera llamar con this )
	 * @param GestureOverLayView donde se pinta la imagen
	 * @param Handler para recoger las predicciones cuando se produzcan
	 * @param Tipo de reconocedor a usar. Usar constantes de la clase 
	 *  como por ejemplo RECONOCEDOR_BASICO
	 */
	public GesturesRecognizer (int id, Context context, GestureOverlayView ol, Handler han,int tipo) throws Exception{
		
		if (Store == null) {
			Store = GestureLibraries.fromRawResource(context, id);
		}
		if (!Store.load()){
			if ( Store.getGestureEntries() == null)
				throw new Exception("No se ha podido cargar ningun gesto en la libreria", new Throwable("La libreria esta vacia o ha fallado al cargar."));
		}
		overlay = ol;
		overlay.addOnGesturePerformedListener(this);
		handler = han;
		tipoReconocedor = tipo;

//		DEBUG
//		Set<String> entradas = Store.getGestureEntries();
//		int i = 1;
//		for (String name :entradas){
//			Log.d("DEBUG", i + " " + name);
//			i++;
//		}
	}

	/**
	 * <b>GesturesRecognizer</b><br><br>
	 *    public GesturesRecognizer()<br>
	 * <ul>Constructor de la clase GesturesRecognizer</ul><br><br>
	 * @param Libreria de gestos ya creada. (El constructor se encarga de hacer load() )
	 * @param GestureOverLayView donde se pinta la imagen
	 * @param Handler para recoger las predicciones cuando se produzcan
	 * @param Tipo de reconocedor a usar. Usar constantes de la clase 
	 *  como por ejemplo RECONOCEDOR_BASICO
	 */
	public GesturesRecognizer(GestureLibrary lib, GestureOverlayView ol, Handler han,int tipo) throws Exception{
		Store = lib;
		if ( !Store.load()){
			Log.d("DEBUG","Store.load == null");
			if ( Store.getGestureEntries() == null)
				throw new Exception("No se ha podido cargar ningun gesto en la libreria", new Throwable("La libreria esta vacia o ha fallado al cargar."));
		}
		overlay = ol;
		overlay.addOnGesturePerformedListener(this);
		handler = han;
		tipoReconocedor = tipo;
	}


	/**
	 * <b>GesturesRecognizer</b><br><br>
	 *    private GesturesRecognizer()<br>
	 * <ul>Constructor de la clase GesturesRecognizer</ul><br><br>
	 * @param String pathGestos con el path de la libreria
	 * de gestos. Recomendado para librer�as autogeneradas
	 * en la SDCard. El path no debe incluir el nombre del 
	 * fichero, solo la ruta de acceso hasta el direcctorio
	 * donde se encuentra.
	 * @param String nameLibreria con el nombre del fichero donde
	 * se encuentra la base de datos de gestos.
	 * @param GestureOverLayView donde se pinta la imagen
	 * @param Handler para recoger las predicciones cuando se produzcan
	 * @param Tipo de reconocedor a usar. Usar constantes de la clase 
	 *  como por ejemplo RECONOCEDOR_BASICO
	 */
	public GesturesRecognizer(String pathGestos, String nameLibreria, GestureOverlayView ol,Handler han,int tipo) throws Exception{
		mStoreFile = new File(pathGestos, nameLibreria);

		if (Store == null) {
			Store = GestureLibraries.fromFile(mStoreFile);
		}
		if (!Store.load()){
			Log.d("DEBUG","Store.load == null");
			if ( Store.getGestureEntries() == null)
				throw new Exception("No se ha podido cargar ningun gesto en la libreria", new Throwable("La libreria esta vacia o ha fallado al cargar."));
		}

		overlay = ol;
		overlay.addOnGesturePerformedListener(this);
		handler = han;
		tipoReconocedor = tipo;

		//DEBUG
		//		Set<String> entradas = Store.getGestureEntries();
		//		int i = 1;
		//		for (String name :entradas){
		//			Log.d("recognizer", i + " " + name);
		//			i++;
		//		}

	}
	
	
	/**
	 * <b>GesturesRecognizer</b><br><br>
	 *    private GesturesRecognizer()<br>
	 * <ul>Constructor de la clase GesturesRecognizer</ul><br><br>
	 * @param File storeFile fichero con la libreria de gestos.
	 * @param GestureOverLayView donde se pinta la imagen
	 * @param Handler para recoger las predicciones cuando se produzcan
	 * @param Tipo de reconocedor a usar. Usar constantes de la clase 
	 *  como por ejemplo RECONOCEDOR_BASICO
	 */
	public GesturesRecognizer(File storeFile, GestureOverlayView ol,Handler han,int tipo) throws Exception{
		mStoreFile = storeFile;
	
		if (Store == null) {
			Store = GestureLibraries.fromFile(mStoreFile);
		}
		if (!Store.load()){
			Log.d("DEBUG","Store.load == null");
			if ( Store.getGestureEntries() == null)
				throw new Exception("No se ha podido cargar ningun gesto en la libreria", new Throwable("La libreria esta vacia o ha fallado al cargar."));
		}

		overlay = ol;
		overlay.addOnGesturePerformedListener(this);
		handler = han;
		tipoReconocedor = tipo;
		//DEBUG
		//		Set<String> entradas = Store.getGestureEntries();
		//		int i = 1;
		//		for (String name :entradas){
		//			Log.d("recognizer", i + " " + name);
		//			i++;
		//		}

	}





	/**
	 * <b>gestureRecognizerBasic</b><br><br>
	 *   private String gestureRecognizerBasic()<br>
	 * <ul>Metodo que devuelve el nombre del gesto
	 * de la base de datos de gestos mas parecido
	 * al gesto introducido.
	 * <b>El gesto elegido sera el primero de las predicciones
	 * sin utilizar ningun algoritmo.</b></ul><br><br>
	 * @param Gesture con el gesto que queremos reconocer
	 * @return Nombre del gesto reconocido o String vacio
	 * e.o.c.(No hay Store, gesto null o no reconocido nada)
	 */
	public void gestureRecognizerBasic(Gesture gesture){
		if ( (Store == null) || (gesture == null) ){
			Message msg = new Message();
			msg.obj = "";
			handler.sendMessage(msg);
		}

		Log.d("DEBUG","Gestures basic ");

		//Obtenemos el array de gestos reconocidos
		ArrayList<Prediction> predictions = Store.recognize(gesture);
		if (predictions.size() > 0){
			if (predictions.get(0).score > 1.0) {

//				//				DEBUG <----
//				Iterator<Prediction> h = predictions.iterator();
//				int j=1;
//				Log.d("GESTOS " + j , "Nuevo gesto!!" );
//				while (h.hasNext()){
//					Prediction p = h.next();
//					Log.d("GESTOS " + j , p.name + " " + p.score );
//					j++;
//				}
				
//				for (String g : Store.getGestureEntries()){
//					
//					if (g.equals(predictions.get(0).name)){
//						Log.d("GESTOS ", g + " " + predictions.get(0) );
//						Message msg = new Message();
//						msg.obj = predictions.get(0).name;
//						handler.sendMessage(msg);
//						return;
//					}
//				}
				int i = 0;
				while( (i < predictions.size()) && (predictions.get(i).score > 1.0) ){
					for (String g : Store.getGestureEntries()){
						if (g.equals(predictions.get(i).name)){
							Message msg = new Message();
							msg.obj = predictions.get(i).name;
							handler.sendMessage(msg);
							return;
						}
					}
					
					
					i++;
				}


				
			}
		}

		Message msg = new Message();
		msg.obj = "";
		handler.sendMessage(msg);


	}

	/**
	 * <b>getStore</b><br><br>
	 *   public GestureLibrary getStore()<br>
	 * <ul>Devuelve la libreria de Gestos</ul><br><br>
	 * @return Libreria de gestos.
	 */
	public GestureLibrary getStore() {
		return Store;
	}


	static class NamedGesture {
		String name;
		Gesture gesture;
	}

	@Override
	public void onGesturePerformed(GestureOverlayView overlay, Gesture gesture) {

		switch (tipoReconocedor){
		case RECONOCEDOR_BASICO:
			gestureRecognizerBasic(gesture);
			break;
		default:

		}
		return;
	}

}

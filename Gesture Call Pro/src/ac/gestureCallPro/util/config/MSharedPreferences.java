package ac.gestureCallPro.util.config;

import java.util.ArrayList;
import java.util.Iterator;

import ac.gestureCallPro.exceptions.NoPreferenceException;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;


/**
 * MSharedPreferences.java 30/03/2011
 * Clase creada para facilitar el uso de SharedPreferences.
 */
public class MSharedPreferences {
	public static final int PREFERENCE_MODE = Context.MODE_PRIVATE;
	public SharedPreferences pref;
	public SharedPreferences.Editor prefEditor;
	public String namePreferences = "";
	
	
	/*
  	 * <ul>Constructor de la clase MSharedPreferences
  	 * Selecciona por defecto las preferencias TacTIC_Movil</ul><br><br>
  	 * @param mContext contexto desde el que se llama al constructor
	 */
//	public MSharedPreferences (Context mContext){
//		pref = mContext.getSharedPreferences(PREFERENCE_NAME_DEF, PREFERENCE_MODE);
//		prefEditor = pref.edit();
//		namePreferences = PREFERENCE_NAME_DEF;
//	}
	
	/**
  	 * <ul>Constructor de la clase MSharedPreferences</ul><br><br>
  	 * @param mContext contexto desde el que se llama al constructor
  	 * @param nombre nombre de las preferencias que deseamos obtener.
	 */
	public MSharedPreferences (Context mContext,String nombre){
		pref = mContext.getSharedPreferences(nombre, PREFERENCE_MODE);
		prefEditor = pref.edit();
		namePreferences = nombre;
	}
	
	

	/**
	 * <ul>Devuelve las preferencias de la aplicacion
	 * TacTIC Movil</ul><br><br>
	 * @return preferences con la configuracion de la aplicacion
	 */
	public SharedPreferences getPreferences() {
		return pref;
	}
	
	
	/**
	 * <ul>Obtiene la preferencia con clave key y la guarda en destino.
	 * Si la preferencia no existe lanza un throw y devolvera default en
	 * su lugar.</ul><br><br>
	 * @param key String con la clave de la prefernecia.
	 * @return String obtenido de la preferencia.
	 * @throws NoPreferenceException
	 */
	public final String getString(String key)throws NoPreferenceException{
		
		String res= pref.getString(key, "default");
		if (res.equals("default")) throw new NoPreferenceException("No existe la preferencia con key " + key );
		return res;
	}
	
	/**
	 * <ul>Obtiene la preferencia con clave key y la guarda en destino.
	 * Si la preferencia no existe lanza un throw y devolvera -333333 en
	 * su lugar.</ul><br><br>
	 * @param key String con la clave de la prefernecia.
	 * @return int obtenido de la preferencia.
	 * @throws NoPreferenceException
	 */
	public final int getInt(String key)throws NoPreferenceException{
	
		int res =  -333333;
		try{
			res = pref.getInt(key, -333333);
		} catch ( ClassCastException e){
			Log.e("MSharedPreferences","Error al llamar a la funcion get. No es de tipo Int");
			throw new NoPreferenceException("La key " + key + " existe pero no es tipo Int");
		}
		if(res == -333333) throw new NoPreferenceException("No existe la preferencia con key " + key);
		return res;
	}
	
	/**
	 * <ul>Obtiene la preferencia con clave key y la guarda en destino.
	 * Si la preferencia no existe lanza un throw y -333333 default en
	 * su lugar.</ul><br><br>
	 * @param key String con la clave de la prefernecia.
	 * @return float obtenido de la preferencia.
	 * @throws NoPreferenceException
	 */
	public final float getFloat(String key)throws NoPreferenceException{
		
		float res = -333333;
		try{
			res = pref.getFloat(key, -333333);
		} catch ( ClassCastException e){
			Log.e("MSharedPreferences","Error al llamar a la funcion get. No es de tipo Float");
			throw new NoPreferenceException("La key " + key + " existe pero no es tipo Float");
		}
		if(res == -333333) throw new NoPreferenceException("No existe la preferencia con key " + key);
		return res;
	}
	
	/**
	 * <ul>Obtiene la preferencia con clave key y la guarda en destino.
	 * Si la preferencia no existe lanza un throw y -333333 default en
	 * su lugar.</ul><br><br>
	 * @param key String con la clave de la prefernecia.
	 * @return Long obtenido de la preferencia.
	 * @throws NoPreferenceException
	 */
	public final Long getLong( String key)throws NoPreferenceException{
		Long res = (long) -333333;
		try{
			res = pref.getLong(key, -333333);
		} catch ( ClassCastException e){
			Log.e("MSharedPreferences","Error al llamar a la funcion get. No es de tipo Long");
			throw new NoPreferenceException("La key " + key + " existe pero no es tipo Long");
		}
		if(res == -333333) throw new NoPreferenceException("No existe la preferencia con key " + key);
		return res;
		
	}
	
	/**
	 * <ul>Obtiene la preferencia con clave key y la guarda en destino.
	 * Si la preferencia no existe lanza un throw y true default en
	 * su lugar.</ul><br><br>
	 * @param key String con la clave de la prefernecia.
	 * @return boolean obtenido de la preferencia.
     * @throws NoPreferenceException
	 */
	public final boolean getBool( String key)throws NoPreferenceException{
		if (!pref.contains(key)){
			throw new NoPreferenceException("No existe la preferencia con key " + key);
		}
		
		return  pref.getBoolean(key, true);
	}
	
	
	/**
	 * <ul>Crea una nueva preferencia.</ul><br><br>
	 * @param valor String que queremos guardar.
	 * @param key que identificara a la preferencia.
	 */
	public final void put(String valor, String key){
		prefEditor.putString(key, valor);
		prefEditor.commit();
	}
	
	/**
	 * <ul>Crea una nueva preferencia.</ul><br><br>
	 * @param valor int que queremos guardar.
	 * @param key que identificara a la preferencia.
	 */
	public final void put(int valor, String key){
		prefEditor.putInt(key, valor);
		prefEditor.commit();
	}
	
	/**
	 * <ul>Crea una nueva preferencia.</ul><br><br>
	 * @param valor float que queremos guardar.
	 * @param key que identificara a la preferencia.
	 */
	public final void put(float valor, String key){
		prefEditor.putFloat(key, valor);
		prefEditor.commit();
	}
	
	/**
	 * <ul>Crea una nueva preferencia.</ul><br><br>
	 * @param valor Long que queremos guardar.
	 * @param key que identificara a la preferencia.
	 */
	public final void put(Long valor, String key){
		prefEditor.putLong(key, valor);
		prefEditor.commit();
	}
	
	/**
	 * <ul>Crea una nueva preferencia.</ul><br><br>
	 * @param valor boolean que queremos guardar.
	 * @param key que identificara a la preferencia.
	 */
	public final void put(boolean valor, String key){
		prefEditor.putBoolean(key, valor);
		prefEditor.commit();
	}
	
	/**
	 * <ul>Crea un nuevo grupo de preferencias. Esta limitado a que sean Strings</ul><br><br>
	 * @param ArrayList de valores de preferencias Strings.
	 * @param ArrayList de keys que identificaran a las preferencias.
	 */
	public final void putStrings(ArrayList<String> valores, ArrayList<String>keys)throws Exception{
		if (valores.size() != keys.size())  throw new Exception("Diferente tama�o de las listas de preferencias y claves");
		
		Iterator<String> i = keys.iterator();
		Iterator<String> j = valores.iterator();
		while(i.hasNext()){
			String auxKey = i.next();
			String auxValor = j.next();
			put(auxValor, auxKey);
		}
		prefEditor.commit();
	}
	
	
	/**
	 * <ul>Crea un nuevo grupo de preferencias. Esta limitado a que sean Strings</ul><br><br>
	 * @param ArrayList de valores de preferencias Strings.
	 * @param ArrayList de keys que identificaran a las preferencias.
	 */
	public final void putInts(ArrayList<Integer> valores, ArrayList<String>keys)throws Exception{
		if (valores.size() != keys.size())  throw new Exception("Diferente tama�o de las listas de preferencias y claves");
		
		Iterator<String> i = keys.iterator();
		Iterator<Integer> j = valores.iterator();
		while(i.hasNext()){
			String auxKey = i.next();
			Integer auxValor = j.next();
			put(auxValor, auxKey);
		}
		prefEditor.commit();
	}
	
	
	/**
	 * <ul>Borra el registro de una preferencia</ul><br><br>
	 * @param key que identificara a la preferencia que queremos borrar.
	 */
	public final void remove(String key){
		prefEditor.remove(key);
		prefEditor.commit();
	}
	
	

}

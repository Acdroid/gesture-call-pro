/**
 * Acdroid Apps for Android
 * 
 * @author Carlos Diaz Canovas
 * @author Marcos Trujillo Seoane
 * 
 * Project Gesture Call
 * 
 */
package ac.gestureCallPro.ui.cabecera;

import ac.gestureCallPro.R; 
import ac.gestureCallPro.preferences.Preferences;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class Cabecera extends LinearLayout {
	
	private Context context; 
	
	private ImageView opciones;
	private ImageView accion;
	private View separator;

	
	
	public Cabecera(Context c) {
		super(c);
		context = c;
		init();
	}
	
	public Cabecera(Context c, AttributeSet attrs) {
		super(c, attrs);
		context = c;
		init();
	}
	
	
	/**
	 * Inicializa el View
	 */
	private void init (){ 
		String infService = Context.LAYOUT_INFLATER_SERVICE;
	    LayoutInflater li = (LayoutInflater)getContext().getSystemService(infService);
	    li.inflate(R.layout.cabecera, this, true);
	    
	    
		opciones = (ImageView)findViewById(R.id.cabecera_options);
		opciones.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent i = new Intent(context,Preferences.class);
				((Activity)context).startActivity(i);
				
			}
		});
		
		accion = (ImageView)findViewById(R.id.cabecera_sms);
		separator = (View)findViewById(R.id.separator2);
	    
	}
	
	
	public void setAccionImageResource(int id){
		accion.setImageResource(id);		
	}
	
	public void setOptionsImageResource(int id){
		opciones.setImageResource(id);
	}
	
	public void setVisibleAccion(){
		accion.setVisibility(View.VISIBLE);
		separator.setVisibility(View.VISIBLE);
	}
	
	public void setNoVisibleAccion(){
		accion.setVisibility(View.GONE);
		separator.setVisibility(View.GONE); 
	}
	/**
	 * 
	 * Asigna al boton accion un onclicklistener
	 * 
	 * @param ocl OnClickListener a asinar
	 */
	public void setOnActionClick(OnClickListener ocl){
		accion.setOnClickListener(ocl);
	}


	/**
	 * 
	 * Asigna al boton opciones un onclicklistener
	 * 
	 * @param ocl OnClickListener a asinar
	 */
	public void setOnOptionClick(OnClickListener ocl){
		opciones.setOnClickListener(ocl);
	}
	
	public void setOnOptionClickWitReturn(final int id){
		opciones.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent i = new Intent(context,Preferences.class);
				((Activity)context).startActivityForResult(i, id);
				
			}
		});
		
		
	}
	
	
	

	public ImageView getOpciones() {
		return opciones;
	}


	public void setOpciones(ImageView opciones) {
		this.opciones = opciones;
	}


	public ImageView getAccion() {
		return accion;
	}


	public void setAccion(ImageView accion) {
		this.accion = accion;
	}

}

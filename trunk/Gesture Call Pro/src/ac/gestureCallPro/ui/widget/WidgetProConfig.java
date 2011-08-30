/**
 * Acdroid Apps for Android
 * 
 * @author Carlos Diaz Canovas
 * @author Marcos Trujillo Seoane
 * 
 * Project Gesture Call
 * 
 */
package ac.gestureCallPro.ui.widget;


import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.os.Bundle;

public class WidgetProConfig extends Activity {
	 
    private int widgetId = 0;
 
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
 
        //Obtenemos el Intent que ha lanzado esta ventana
        //y recuperamos sus parámetros
        Intent intentOrigen = getIntent();
        Bundle params = intentOrigen.getExtras();
 
        //Obtenemos el ID del widget que se está configurando
        widgetId = params.getInt(
                AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID);
 
        //Actualizamos el widget tras la configuración
        AppWidgetManager appWidgetManager =
            AppWidgetManager.getInstance(WidgetProConfig.this);
        	WidgetSimple.actualizarWidget(WidgetProConfig.this, appWidgetManager, widgetId);

        
      //Devolvemos como resultado: ACEPTAR (RESULT_OK)
        Intent resultado = new Intent();
        resultado.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId);
        setResult(RESULT_OK, resultado);
        finish();
 

    }
}
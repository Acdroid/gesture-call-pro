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

import ac.gestureCallPro.R;
import ac.gestureCallPro.ui.main;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.RemoteViews;

/**
 * @author marcos y carlos
 *
 * @version $Revision: 1.0 $
 */
public class WidgetPro extends AppWidgetProvider {

	private static RemoteViews controles;
	public static final String ACCION_BOTON_WIDGET_PRO = "ac.gestureCallPro.proPressButton";
	public static final String ACCION_BOTON_WIDGET_PRO_DIRECT_CALL = "ac.gestureCallPro.proDirectCall";

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {
		Log.d("widget","Entra en onupdate");
		super.onUpdate(context, appWidgetManager, appWidgetIds);
		//Iteramos la lista de widgets en ejecución
	    for (int i = 0; i < appWidgetIds.length; i++)
	    {
	        //ID del widget actual
	        int widgetId = appWidgetIds[i];
	 
	        //Actualizamos el widget actual
	        actualizarWidget(context, appWidgetManager, widgetId);
	    }

	}


	public void callGestureCall(Context c,AppWidgetManager widgetManager,int widgetId){

		//Creamos el intent, anadimos flag nueva actividad y la lanzamos
		Intent i = new Intent(c,main.class);
		i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		c.startActivity(i);
	}

	public static void actualizarWidget(Context context,
			AppWidgetManager appWidgetManager, int widgetId)
	{
		//Obtenemos la lista de controles del widget actual
		controles = new RemoteViews(context.getPackageName(), R.layout.widget_pro);

		//Asociamos los 'eventos' al widget
		Intent intent = new Intent(ACCION_BOTON_WIDGET_PRO);
		intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId);

		PendingIntent pendingIntent = PendingIntent.getBroadcast(context, widgetId, intent,
				PendingIntent.FLAG_UPDATE_CURRENT);

		controles.setOnClickPendingIntent(R.id.widgetpro_imagebutton, pendingIntent);
		
		
		//Asociamos los 'eventos' al widget
		Intent intent2 = new Intent(ACCION_BOTON_WIDGET_PRO_DIRECT_CALL);
	
		intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId);

		PendingIntent pendingIntent2 = PendingIntent.getBroadcast(context, widgetId, intent2,
				PendingIntent.FLAG_UPDATE_CURRENT);

		controles.setOnClickPendingIntent(R.id.widgetpro_directcall, pendingIntent2);


		//Notificamos al manager de la actualización del widget actual
		appWidgetManager.updateAppWidget(widgetId, controles);
	}


	@Override
	public void onReceive(Context context, Intent intent) {
		if (intent.getAction().equals(ACCION_BOTON_WIDGET_PRO)) {
			//Obtenemos el ID del widget a actualizar
			int widgetId = intent.getIntExtra(
					AppWidgetManager.EXTRA_APPWIDGET_ID,
					AppWidgetManager.INVALID_APPWIDGET_ID);

			//Obtenemos el widget manager de nuestro contexto
			AppWidgetManager widgetManager =
				AppWidgetManager.getInstance(context);

			//Actualizamos el widget
			if (widgetId != AppWidgetManager.INVALID_APPWIDGET_ID) {
				callGestureCall(context, widgetManager, widgetId);
			}
		}
		if (intent.getAction().equals(ACCION_BOTON_WIDGET_PRO_DIRECT_CALL)) {
			Log.d("widget","Entra a llamar");
			String url = "tel:" + "913152117";
			Intent i = new Intent(Intent.ACTION_CALL, Uri.parse(url));
			context.startActivity(i);
			
		}
	}




}

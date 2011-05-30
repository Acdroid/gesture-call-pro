/**
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
import android.util.Log;
import android.widget.RemoteViews;

/**
 * @author marcos y carlos
 *
 */
public class WidgetSimple extends AppWidgetProvider {

	private static RemoteViews controles;
	public static final String ACCION_BOTON_WIDGET_SIMPLE = "ac.gestureCallPro.PressButton";


	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {
		Log.d("widget","Entra en onupdate");
		super.onUpdate(context, appWidgetManager, appWidgetIds);
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
		controles = new RemoteViews(context.getPackageName(), R.layout.widget_simple);

		//Asociamos los 'eventos' al widget
		Intent intent = new Intent(ACCION_BOTON_WIDGET_SIMPLE);
		intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId);

		PendingIntent pendingIntent = PendingIntent.getBroadcast(context, widgetId, intent,
				PendingIntent.FLAG_UPDATE_CURRENT);

		controles.setOnClickPendingIntent(R.id.widget_imagebutton, pendingIntent);

		//Notificamos al manager de la actualización del widget actual
		appWidgetManager.updateAppWidget(widgetId, controles);
	}


	@Override
	public void onReceive(Context context, Intent intent) {
		if (intent.getAction().equals(ACCION_BOTON_WIDGET_SIMPLE)) {
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
	}




}

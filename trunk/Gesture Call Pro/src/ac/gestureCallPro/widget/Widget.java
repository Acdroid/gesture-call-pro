package ac.gestureCallPro.widget;

import ac.gestureCallPro.R;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

public class Widget extends AppWidgetProvider {

	private static final int ID_WIDGET = 69692;

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

		Log.d("Gesture Call", "paso por el on update");

		//Obtenemos la lista de controles del widget actual
		RemoteViews controles =
			new RemoteViews(context.getPackageName(), R.layout.widget);

		//Asociamos los 'eventos' al widget
		Intent intent = new Intent("ac.gestureCallPro.CLICK");
		intent.putExtra(
				AppWidgetManager.EXTRA_APPWIDGET_ID, ID_WIDGET);

		PendingIntent pendingIntent =
			PendingIntent.getBroadcast(context, ID_WIDGET,
					intent, PendingIntent.FLAG_UPDATE_CURRENT);

		controles.setOnClickPendingIntent(R.id.widget_img, pendingIntent);

		super.onUpdate(context, appWidgetManager, appWidgetIds);
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		Log.d("Gesture Call","Paso por el onReceive con intent " + intent.getAction()
		+ "  " + intent.getPackage() + "  " + context.getPackageName()
		
		);

		if (intent.getAction().equals("ac.gestureCallPro.CLICK")) {
			//Obtenemos el ID del widget a actualizar
			int widgetId = intent.getIntExtra(
					AppWidgetManager.EXTRA_APPWIDGET_ID,
					AppWidgetManager.INVALID_APPWIDGET_ID);

			//Obtenemos el widget manager de nuestro contexto
			AppWidgetManager widgetManager =
				AppWidgetManager.getInstance(context);

			//Actualizamos el widget
			if (widgetId != AppWidgetManager.INVALID_APPWIDGET_ID) {
				Toast.makeText(context, "CLICK", Toast.LENGTH_LONG).show();
			}
		}
		else if (intent.getAction().equals("android.appwidget.action.APPWIDGET_UPDATE")){
			
			//Obtenemos la lista de controles del widget actual
			RemoteViews controles =
				new RemoteViews(context.getPackageName(), R.layout.widget);
			
			Log.d("Gesture Call", "controles " + controles.toString());

			//Asociamos los 'eventos' al widget
			Intent intent2 = new Intent("ac.gestureCallPro.CLICK");
			intent.putExtra(
					AppWidgetManager.EXTRA_APPWIDGET_ID, ID_WIDGET);

			PendingIntent pendingIntent =
				PendingIntent.getBroadcast(context, ID_WIDGET,
						intent2, PendingIntent.FLAG_UPDATE_CURRENT);

			Log.d("Gesture Call", "pending " + pendingIntent.toString());
			controles.setOnClickPendingIntent(R.id.widget_img, pendingIntent);
		}
	}
	public static void actualizarWidget(Context context,
			AppWidgetManager appWidgetManager, int widgetId)
	{
		//Obtenemos la lista de controles del widget actual
		RemoteViews controles =
			new RemoteViews(context.getPackageName(), R.layout.widget);

		//Asociamos los 'eventos' al widget
		Intent intent = new Intent("ac.gestureCallPro.CLICK");
		intent.putExtra(
				AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId);

		PendingIntent pendingIntent =
			PendingIntent.getBroadcast(context, widgetId,
					intent, PendingIntent.FLAG_UPDATE_CURRENT);

		controles.setOnClickPendingIntent(R.id.widget_img, pendingIntent);

		//Notificamos al manager de la actualización del widget actual
		appWidgetManager.updateAppWidget(widgetId, controles);
	}
}

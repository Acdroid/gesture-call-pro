package ac.gestureCallPro.util.bootReceiver;

import ac.gestureCallPro.R;
import ac.gestureCallPro.exceptions.NoPreferenceException;
import ac.gestureCallPro.ui.main;
import ac.gestureCallPro.util.config.AppConfig;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BootUpReceiver extends BroadcastReceiver{
	AppConfig ap;
    @Override
    public void onReceive(Context context, Intent intent) {
    	//Cargamos las opciones
		ap = new AppConfig(context, AppConfig.NAME);
		try {
			if (ap.getBool(AppConfig.OPEN_START) && ap.getBool(AppConfig.NOTIFICATION)){
				String ns = Context.NOTIFICATION_SERVICE;
				NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(ns);
				
				//Creamos la notificacion
				int icon = R.drawable.icon;
				CharSequence tickerText = context.getResources().getString(R.string.notification_hello);
				long when = System.currentTimeMillis();

				Notification notification = new Notification(icon, tickerText, when);
				
				notification.flags |= Notification.FLAG_NO_CLEAR | Notification.FLAG_ONGOING_EVENT;
						
				CharSequence contentTitle = context.getString(R.string.app_name);
				CharSequence contentText  = context.getString(R.string.click_gesture);
				Intent notificationIntent = new Intent(context, main.class);
				PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notificationIntent, 0);

				notification.setLatestEventInfo(context, contentTitle, contentText, contentIntent);

				mNotificationManager.notify(62929, notification);
				
			}
		} catch (NoPreferenceException e) {
			return;
		}
		
    }

}
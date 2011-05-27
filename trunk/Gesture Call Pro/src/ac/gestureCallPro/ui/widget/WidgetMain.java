package ac.gestureCallPro.ui.widget;

import ac.gestureCallPro.R;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.util.Log;
import android.widget.RemoteViews;

public class WidgetMain  extends AppWidgetProvider{
    
    //Componente que identifica esta clase
    static final ComponentName THIS_APPWIDGET =
        new ComponentName("ac.gestureCallPro.ui.widget","ac.gestureCallPro.ui.widget.WidgetMain");
         
    //instancia de la clase
    private static WidgetMain sInstance;
    
    /**metodo que devuelve una instancia de la clase para que luego se puedan hacer
     * llamadas sobre ella, lo utiliza el servicio
     * @return
     */    
    static synchronized WidgetMain getInstance() {
        if (sInstance == null) {
            sInstance = new WidgetMain();
        }
        return sInstance;
    }
    
    /**
     * M�todo llamado en la subida del widget y que contendr� la llamada a todas
     * las inicializaciones necesarias
     */
    public void onUpdate(Context context, AppWidgetManager appWidgetManager,
            int[] appWidgetIds) {
        
        //llamada al m�todo de inicializaciones
        defaultAppWidget(context, appWidgetIds);    
        
        //llamada al registro del servicio (antes se ha tenido que hacer todo el
        //defaultAppWidget
        Intent updateIntent = new Intent(context, WidgetService.class);
        updateIntent.addFlags(Intent.FLAG_RECEIVER_REGISTERED_ONLY);
        context.sendBroadcast(updateIntent);
    }

    /**
     * M�todo en el que hay que inicializar las vistas, exceptuando los botones 
     * que se le llamar� desde dentro a otro m�todo
     * 
     * @param context
     * @param appWidgetIds
     */
    private void defaultAppWidget(Context context, int[] appWidgetIds) {
        final Resources res = context.getResources();
        final RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget);
        
        //en los Widgets se trabaja con RemoteViews y no Views directamente por lo que
        //recogeremos las RemoteViews siempre e inicializaremos los valores de nuestras
        //vistas, por ejemplo:
        //views.setTextViewText(R.id.text, "Inicializando campo de texto");

        linkButtons(context, views);
        pushUpdate(context, appWidgetIds, views);
    }

    /**
     * M�todo que actualiza la parte gr�fica. Todo el tiempo se juega con RemoteViews y
     * se van pasando de m�todo en m�todo hasta llegar aqu� que es donde se cierran y se
     * muestran. No se mete en otros m�todos ya que siempre hay que acabar llamando a este m�todo
     * y a veces algunos m�todos son llamados al principio y otros cada cierto tiempo por lo
     * que tendriamos que repetir c�digo 
     * 
     * @param context
     * @param appWidgetIds
     * @param views
     */
    private void pushUpdate(Context context, int[] appWidgetIds, RemoteViews views) {
        final AppWidgetManager gm = AppWidgetManager.getInstance(context);
        if (appWidgetIds != null) {
            gm.updateAppWidget(appWidgetIds, views);
        } else {
            gm.updateAppWidget(THIS_APPWIDGET, views);
        }
    }
    
    /**
     * M�todo llamado cuando se ha realizado una acci�n, para ello nos podemos valer
     * de valores recibidos desde el servicio o m�s f�cil si hemos creado m�todos
     * en el Servicio para recoger las variables podremos recogerlas directamente
     * 
     * @param service
     * @param appWidgetIds
     */
    void performUpdate(WidgetService service, int[] appWidgetIds) {
        final Resources res = service.getResources();
        final RemoteViews views = new RemoteViews(service.getPackageName(), R.layout.main);
        
        //ejemplo de lo que har�amos, cambiar�amos el texto de un TextView y a�adir�amos
        //el nuevo valor evaluado por el servicio tras el pulsamiento de un bot�n
        //views.setTextViewText("El nuevo valor es:" + service.getValue());
        
   
        
        //llama al m�todo que actualiza las vistas
        pushUpdate(service, appWidgetIds, views);
    }

    /**
     * Links a todos los botones que va a tener el widget, es bueno tenerlo
     * separado ya que se har� s�lo la primera vez
     * 
     * @param context
     * @param views
     */
    private void linkButtons(Context context, RemoteViews views) {
        //intents y el servico que necesitaremos para crear la interconexi�n bot�n-servicio
        Intent intent;
        PendingIntent pendingIntent;

        final ComponentName serviceName = new ComponentName(context, WidgetService.class);
        
        //Ejemplo de como se puede linkar un bot�n y como ves hay que darle una acci�n
        //que luego en el servicio recogeremos y cambiaremos el desarrollo en funci�n 
        //de eso, para ello necesitamos utilizar pending intent, no hay OnClickListener
        intent = new Intent(WidgetService.UNA_ACCION);
        intent.setComponent(serviceName);
        pendingIntent = PendingIntent.getService(context, 0, intent, 0);
        views.setOnClickPendingIntent(R.id.widget_imagebutton, pendingIntent);
    }
        
    /**
     * M�todo de notificacion del Widget, es el m�todo que comunica con el otro m�todo
     * de notificaci�n pero del servicio, de esta manera tenemmos todo m�s ordenado.
     * Lo primero que hace es comprobar la instancia del servicio y despu�s lo pasa a
     * producci�n
     * 
     * @param service
     * @param what
     */
    void notifyChange(WidgetService service, String what) {
        if (hasInstances(service)) {
            performUpdate(service, null);
        }
    }
    
    /**
     * Comprueba que est� instanciado correctamente la variable que se recibe como 
     * servicio
     * @param context
     * @return
     */
    private boolean hasInstances(Context context) {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(THIS_APPWIDGET);
        return (appWidgetIds.length > 0);
    }
   
}
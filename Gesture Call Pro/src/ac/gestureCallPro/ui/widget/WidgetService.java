package ac.gestureCallPro.ui.widget;

import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.util.Log;

public class WidgetService extends Service{

    //accion creada para la comprensi�n entre llamadas y para identificar
    //lo que debe hacer
    public static final String UNA_ACCION = "ac.gestureCallPro.ui.main";
    
    //clase del Widget principal con la que se har� la comunicaci�n
    //ya que esto es un servicio y necesita contactar con algo
    private WidgetMain mAppWidgetProvider = WidgetMain.getInstance();
    
    //variable para el uso del programa, pudes usar lo que quieras, eso depende de ti
    //esto es un ejemplo
    private int value = 5;
    
    /**
     * Recibe mensajes externos y los evalua llamando a la funci�n de actualizaci�n
     * del widget || En principio no es necesario, pero lo ponemos porque puede
     * ser �til para llamadas externas
     */
    private BroadcastReceiver mIntentReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            
            String action = intent.getAction();
            
            int[] appWidgetIds = intent.getIntArrayExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS);
            mAppWidgetProvider.performUpdate(WidgetService.this, appWidgetIds);
        }
    };
    
    /**
     * M�todo llamado en la creaci�n con el que registraremos el servicio 
     * y las posibles acciones que pueda tener
     */
    @Override
     public void onCreate() {
            super.onCreate();
            
            IntentFilter commandFilter = new IntentFilter();
            //a�adimos la accion, si ponemos m�s que es lo normal las a�adiremos
            //aqu� seguidas una detr�s de otra
            commandFilter.addAction(UNA_ACCION);            
            registerReceiver(mIntentReceiver, commandFilter);
     }
    
    /**
     * simplemente se pone 
     */
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    
    /**
     * Hay que acordarse de que los servicios hay que cerrarlos y cuando se cierran
     * hay que desregistrarlos, con este m�todo lo hacemos
     */
    @Override
    public void onDestroy(){
         unregisterReceiver(mIntentReceiver);
         super.onDestroy();
    }
     
    /**
     * M�todo que es llamado siempre que se recibe un aviso, por lo que es donde
     * evaluamos las acciones
     */
    @Override
    public void onStart(Intent intent, int startId) {
    	Log.d("DEBUG","una acci�n y tal pascual1");
        //recogemos la accion
        String action = intent.getAction();
        Log.d("DEBUG",action + " una acci�n y tal pascual");
        
        if(UNA_ACCION.equals(action)){
            //hacemos lo que tengamos que hacer, por ejemplo poner una variable a 0
        	Log.d("DEBUG","una acci�n y tal pascual2");
            value = 0;
        }
        Log.d("DEBUG","una acci�n y tal pascual3");
        //llamamos al m�todo que hace la notificaci�n (se podr�a hacer desde aqu�
        //directamente pero es bueno trabajar por capas, adem�s este m�todo lo podremos
        //utilizar si decidimos tambi�n utilizar el BroadcastReceiver
        notifyChange(action);        
    }
    
    /**
     * M�todo de notificaci�n. Contacta con el widget y le envia la notificaci�n 
     * seg�n el campo what que le llega, que tambi�n se puede cambiar, eso depende
     * de como quieras gestionar la informaci�n entre los m�todos
     * 
     * @param what
     */
     private void notifyChange(String what) {
        Intent i = new Intent(what);
        sendBroadcast(i);
        Log.d("DEBUG","una acci�n y tal pascual55");
        mAppWidgetProvider.notifyChange(this, what);
     }

     /**
      * M�todo que sirve para devolver el valor de value, no es necesario, pero si 
      * guardamos variables en el servicio con m�todos podemos devolver los datos,
      * o enviarlos dentro del intent desde el notifyChange
      * 
      * @return
      */
     public int getValue(){
         return value;
     }
}
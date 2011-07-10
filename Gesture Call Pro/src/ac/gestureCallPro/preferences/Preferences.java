/**
 * 
 */
package ac.gestureCallPro.preferences;

import ac.gestureCallPro.R;
import ac.gestureCallPro.exceptions.NoPreferenceException;
import ac.gestureCallPro.ui.main;
import ac.gestureCallPro.util.config.AppConfig;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract.CommonDataKinds.Email;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * @author marcos
 *
 */
public class Preferences extends Activity{
	public static final int DIALOG_SEGUNDOS = 1;
	public static final int DIALOG_THEMES = 2;
	public static final int DIALOG_ACCION = 3;
	
	public Context mContext;
	final CharSequence[] items = {"1 second", "2 seconds", "3 seconds", "4 seconds","5 seconds", "6 seconds", "7 seconds", "8 seconds"
			, "9 seconds", "10 seconds"};
	final CharSequence[] themes = {"Grey","Green","Blue","Black","White"}; //coinciden con appConfig.GREY GREEN y BLUE
	final CharSequence[] acciones = {"Llamar","SMS"};  //coinciden con appConfig.GREY GREEN y BLUE
	
	private AppConfig ap;
	private boolean askBefore=true;
	private boolean exitAC=false;
	private boolean notification=true;
	private Long numSecs;
	private int theme;
	private int accion;
	
	
	
	

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        numSecs = new Long(3000); //Valor por defecto por si falla la recogida de la configuracion
        setContentView(R.layout.pref);
        getValues();
        initValues();

		
        
    }	
    
	
	private void getValues(){
		ap = new AppConfig(this, AppConfig.NAME);
		try {
			askBefore = ap.getBool(AppConfig.AVISO_AL_LLAMAR);
			exitAC = ap.getBool(AppConfig.RETURN_AFTER_CALL);
			notification = ap.getBool(AppConfig.NOTIFICATION);
			numSecs = ap.getLong(AppConfig.S_AFTER_CALL);
			theme = ap.getInt(AppConfig.THEME);
			accion = ap.getInt(AppConfig.ACCION_POR_DEFECTO);
			
		} catch (NoPreferenceException e) {
			Log.e("GestureCall_pro","Error al cargar preferencias en preferences.java." +
					e.getMessage());
		}
		
		
	}
	private void initValues(){
		CheckBox c = (CheckBox) findViewById(R.id.pref_check);
		c.setChecked(askBefore);
		
		if (!askBefore){
			enabledLayoutSecsAftercall(false);
		}
		
		TextView t = (TextView)findViewById(R.id.pref_text_seconds);
		t.setText(Long.toString(numSecs/1000));
		
		
		c = (CheckBox)findViewById(R.id.pref_check_exit_after_call);
		c.setChecked(exitAC);
		
		c = (CheckBox)findViewById(R.id.pref_check_notification);
		c.setChecked(notification);
        
		putThemeInLayout();
		
		changeDefActionColor();
			
		
	}
	
	private void saveValues(){
		CheckBox c = (CheckBox) findViewById(R.id.pref_check);
		ap.put(c.isChecked(), AppConfig.AVISO_AL_LLAMAR);
		
		ap.put(numSecs, AppConfig.S_AFTER_CALL);
				
		c = (CheckBox) findViewById(R.id.pref_check_exit_after_call);
		ap.put(c.isChecked(), AppConfig.RETURN_AFTER_CALL);
		
		c = (CheckBox) findViewById(R.id.pref_check_notification);

		ap.put(c.isChecked(), AppConfig.NOTIFICATION);
		
		ap.put(theme, AppConfig.THEME);
		
		ap.put(accion, AppConfig.ACCION_POR_DEFECTO);
	}
	
	
	
	//Menu para los creditos
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu_creditos, menu);
		return true;
	}
    
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		// Handle item selection
		switch (item.getItemId()) {
		
		
			case R.id.me_creditos:
				Dialog dialog = new Dialog(this);
				dialog.setContentView(R.layout.credits_layout);
				dialog.setTitle("Credits:");
				
				dialog.show();
				return true;
		
				
			default:
				return super.onOptionsItemSelected(item);
		}
	}
	
	
	@Override
	protected Dialog onCreateDialog(int id) {
		@SuppressWarnings("unused")
		Dialog dialog;
		switch(id) {
		case DIALOG_SEGUNDOS:
			AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
			builder.setTitle(mContext.getResources().getString(R.string.select_secs));
			builder.setItems(items, new DialogInterface.OnClickListener() {
			    public void onClick(DialogInterface dialog, int item) {
			        numSecs = new Long((item +1) * 1000);
			        TextView t = (TextView)((Activity)mContext).findViewById(R.id.pref_text_seconds);
					t.setText(Long.toString(numSecs/1000));
			    }
			});
			AlertDialog alert = builder.create();
			return alert;
		case DIALOG_THEMES:
			AlertDialog.Builder builder2 = new AlertDialog.Builder(mContext);
			builder2.setTitle(mContext.getResources().getString(R.string.select_themes));
			builder2.setItems(themes, new DialogInterface.OnClickListener() {
			    public void onClick(DialogInterface dialog, int item) {
			        theme = item;
			        putThemeInLayout();
			    }
			});
			AlertDialog alert2 = builder2.create();
			return alert2;
		case DIALOG_ACCION:
			AlertDialog.Builder builder3 = new AlertDialog.Builder(mContext);
			builder3.setTitle(mContext.getResources().getString(R.string.select_dialog_def_action));
			builder3.setItems(acciones, new DialogInterface.OnClickListener() {
			    public void onClick(DialogInterface dialog, int item) {
			        accion = item;
			        changeDefActionColor();
			        	
			    }
			});
			AlertDialog alert3 = builder3.create();
			return alert3;
			
		default:
			return dialog = null;
		}
	}

	
	//**************************** Funciones menores o de Clicks en vires ********************************//
	
	public void clickReturn(View v){
		setResult(main.RESULT_PREF_NOT_SAVED);
		Preferences.this.finish();
	}
	
	public void clickSave(View v){
		saveValues();
		setResult(main.RESULT_PREF_SAVED);
		Preferences.this.finish();
	}
	
	public void clickAccionDefecto(View v){
		//showDialog(DIALOG_ACCION);
		if (accion == 0)
			accion = 1;
		else
			accion = 0;
		changeDefActionColor();
	}
	
	public void clickSecsAfterCall(View v){
		showDialog(DIALOG_SEGUNDOS);
	}
	
	public void clickThemes(View v){
		showDialog(DIALOG_THEMES);
	}
	
	public void clickCheckBoxAfterCall(View v){
		CheckBox c = (CheckBox)findViewById(R.id.pref_check);
		//Si esta ok permitimos cambiar el tiempo, e.o.c lo inhabilitamos
		if(c.isChecked()){
			enabledLayoutSecsAftercall(true);
			
		}
		else{
			enabledLayoutSecsAftercall(false);
		}
	}
	
	public void clickNovedades(View v){
		Dialog dialog = new Dialog(this);
		dialog.setContentView(R.layout.whats_new);
		dialog.setTitle("Whats new");
		
		dialog.show();
	}
	
	
	public void clickAbout(View v){
		Dialog dialog = new Dialog(this);
		dialog.setContentView(R.layout.credits_layout);
		dialog.setTitle("Credits:");
		
		dialog.show();
	}
	
	public void clickContact(View v){
		Intent i = new Intent(Intent.ACTION_SEND);
		i.setType("plain/text");
		i.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{"aracemcskyline@gmail.com"});
		startActivity(Intent.createChooser(i, "Send mail..."));
		
		
	}
	
	public void changeDefActionColor(){
		TextView textCall = (TextView) findViewById(R.id.pref_accion_def1);
        TextView textSMS = (TextView) findViewById(R.id.pref_accion_def2);
        if(accion == 0 ){
        	textCall.setTextColor(getResources().getColor(R.color.verde_android));
        	textSMS.setTextColor(getResources().getColor(R.color.gris));
        	
        }
        else{
        	textCall.setTextColor(getResources().getColor(R.color.gris));
        	textSMS.setTextColor(getResources().getColor(R.color.verde_android));
        }
	}
	
	public void putThemeInLayout(){
		LinearLayout l = (LinearLayout)findViewById(R.id.pref_lay_theme_withcolor);
		switch (theme){
		case AppConfig.Themes.GREY:
			l.setBackgroundResource(R.color.gris);
			break;
		case AppConfig.Themes.GREEN:
			l.setBackgroundResource(R.drawable.background_green_gradient);
			break;
		case AppConfig.Themes.BLUE:
			l.setBackgroundResource(R.drawable.background_blue_gradient);
			break;
		case AppConfig.Themes.BLACK:
			l.setBackgroundResource(R.drawable.background_black_gradient_border);
			break;
		case AppConfig.Themes.WHITE:
			l.setBackgroundResource(R.drawable.background_white_gradient);
			break;
		default:
			return;
		}
			
	}
	
	/**
	 * Metodo auxiliar para activar o desactivar el layout 
	 * de secs after call.
	 * @param b true si queremos activarlo false e.o.c.
	 */
	public void enabledLayoutSecsAftercall(boolean b){
		LinearLayout l = (LinearLayout)findViewById(R.id.pref_lay_secs_after_call);
		TextView t = (TextView)findViewById(R.id.pref_text_seconds);
		TextView t2 = (TextView)findViewById(R.id.pref_text_seconds_large);
		TextView t3 = (TextView)findViewById(R.id.pref_text_secs);
		TextView t4 = (TextView)findViewById(R.id.pref_text_seconds_large2);
		if(b){
			l.setEnabled(true);
			t.setTextColor(getResources().getColor(R.color.white));
			t2.setTextColor(getResources().getColor(R.color.white));
			t3.setTextColor(getResources().getColor(R.color.azul_claro));
			t4.setTextColor(getResources().getColor(android.R.color.secondary_text_dark));
		}
		else{			
			l.setEnabled(false);
			t.setTextColor(getResources().getColor(R.color.gris));
			t2.setTextColor(getResources().getColor(R.color.gris));
			t3.setTextColor(getResources().getColor(R.color.gris));
			t4.setTextColor(getResources().getColor(R.color.gris));
		}
	}
	
	
}

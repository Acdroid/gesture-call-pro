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
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

/**
 * @author marcos
 *
 */
public class Preferences extends Activity{
	public static final int DIALOG_SEGUNDOS = 1;
	private AppConfig ap;
	private CheckBox c;
	
	private boolean askBefore;
	private boolean exitAC;
	private boolean notification;
	private long numSecs;
	
	public Context mContext;
	final CharSequence[] items = {"1 second", "2 seconds", "3 seconds", "4 seconds","5 seconds"};
	
	

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.pref);
        getValues();
        initValues();

		
        
    }	
    
	
	public void clickReturn(View v){
		setResult(main.RESULT_PREF_NOT_SAVED);
		Preferences.this.finish();
	}
	
	public void clickSave(View v){
		saveValues();
		setResult(main.RESULT_PREF_SAVED);
		Preferences.this.finish();
	}
	
	public void clickSecsAfterCall(View v){
		showDialog(DIALOG_SEGUNDOS);
	}
	
	private void getValues(){
		ap = new AppConfig(this, AppConfig.NAME);
		try {
			askBefore = ap.getBool(AppConfig.AVISO_AL_LLAMAR);
			exitAC = ap.getBool(AppConfig.RETURN_AFTER_CALL);
			notification = ap.getBool(AppConfig.NOTIFICATION);
			numSecs = ap.getLong(AppConfig.S_AFTER_CALL);
			Log.d("DEBUG","1" + notification + "");
			
		} catch (NoPreferenceException e) {
			Log.d("DEBUG",e.getMessage());
		}
		
		
	}
	private void initValues(){
		CheckBox c = (CheckBox) findViewById(R.id.pref_check);
		c.setChecked(askBefore);
		
		TextView t = (TextView)findViewById(R.id.pref_text_seconds);
		t.setText(numSecs+ "\nsecs");
		
		
		c = (CheckBox)findViewById(R.id.pref_check_exit_after_call);
		c.setChecked(exitAC);
		
		c = (CheckBox)findViewById(R.id.pref_check_notification);
		c.setChecked(notification);
        
			
		
	}
	
	private void saveValues(){
		CheckBox c = (CheckBox) findViewById(R.id.pref_check);
		ap.put(c.isChecked(), AppConfig.AVISO_AL_LLAMAR);
		
		ap.put(numSecs, AppConfig.S_AFTER_CALL);
		
		c = (CheckBox) findViewById(R.id.pref_check_exit_after_call);
		ap.put(c.isChecked(), AppConfig.RETURN_AFTER_CALL);
		
		c = (CheckBox) findViewById(R.id.pref_check_notification);
		Log.d("DEBUG","2" + c.isChecked() + "");
		ap.put(c.isChecked(), AppConfig.NOTIFICATION);
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
			    }
			});
			AlertDialog alert = builder.create();
			return alert;
		default:
			return dialog = null;
		}
	}

	
	
	
	
	
}

/**
 * 
 */
package ac.gestureCallPro.preferences;

import ac.gestureCallPro.R;
import ac.gestureCallPro.exceptions.NoPreferenceException;
import ac.gestureCallPro.ui.main;
import ac.gestureCallPro.util.config.AppConfig;
import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;

/**
 * @author marcos
 *
 */
public class Preferences extends Activity{
	
	private AppConfig ap;
	private CheckBox c;
	
	private boolean askBefore;
	private boolean exitAC;
	private boolean notification;
	

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
	
	private void getValues(){
		ap = new AppConfig(this, AppConfig.NAME);
		try {
			askBefore = ap.getBool(AppConfig.AVISO_AL_LLAMAR);
			exitAC = ap.getBool(AppConfig.RETURN_AFTER_CALL);
			notification = ap.getBool(AppConfig.NOTIFICATION);
			Log.d("DEBUG","1" + notification + "");
			
		} catch (NoPreferenceException e) {
			Log.d("DEBUG",e.getMessage());
			Log.d("DEBUG","1" + notification + "");
		}
		
		
	}
	private void initValues(){
		CheckBox c = (CheckBox) findViewById(R.id.pref_check);
		c.setChecked(askBefore);
		
		c = (CheckBox)findViewById(R.id.pref_check_exit_after_call);
		c.setChecked(exitAC);
		
		c = (CheckBox)findViewById(R.id.pref_check_notification);
		c.setChecked(notification);
        
			
		
	}
	
	private void saveValues(){
		CheckBox c = (CheckBox) findViewById(R.id.pref_check);
		ap.put(c.isChecked(), AppConfig.AVISO_AL_LLAMAR);
		
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
	
	
	
	
	
	
	
	
}

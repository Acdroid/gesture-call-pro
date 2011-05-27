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

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pref);
        ap = new AppConfig(this, AppConfig.NAME);
        c = (CheckBox) findViewById(R.id.pref_check);
        try {
			c.setChecked(ap.getBool(AppConfig.AVISO_AL_LLAMAR));
		} catch (NoPreferenceException e) {

		}
        
    }	
    
	
	public void clickReturn(View v){
		setResult(main.RESULT_OK);
		Preferences.this.finish();
	}
	
	public void clickSave(View v){
		ap.put(c.isChecked(), AppConfig.AVISO_AL_LLAMAR);
		setResult(main.RESULT_OK);
		Preferences.this.finish();
	}
	
	
	
	
	//Menu para los créditos
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

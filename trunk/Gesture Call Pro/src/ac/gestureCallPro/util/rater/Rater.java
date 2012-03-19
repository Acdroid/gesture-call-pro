package ac.gestureCallPro.util.rater;

import ac.gestureCallPro.R;
import ac.gestureCallPro.exceptions.NoPreferenceException;
import ac.gestureCallPro.util.config.AppConfig;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Rater {

	public static int APP_USES = 20;
	public static int APP_DAYS_USES = 13;


	/**
	 * This method check the app uses and the days after install
	 * and show a dialog to motivate the user rate the app
	 * @param context context of the application
	 */
	public static void checkRater (Context context){

		AppConfig ap = new AppConfig(context, AppConfig.NAME);

		try {
			//Check if the user select not show this again
			int count = ap.getInt(AppConfig.RATER_PRO_LAUCH_COUNTER);
			Log.d("DEBUG","Number of uses: " + count);
			if (!ap.getBool(AppConfig.RATER_PRO)){
				//protect the case when the uses will bigger than MAX_INT xDDDDDDDD
				if (count > 65000)
					count = APP_USES + 1;
				else
					count ++;
				ap.put( count, AppConfig.RATER_PRO_LAUCH_COUNTER);
				return;
			}


			int counter = ap.getInt(AppConfig.RATER_PRO_LAUCH_COUNTER) + 1;
			ap.put(counter, AppConfig.RATER_PRO_LAUCH_COUNTER);
			if (counter >= APP_USES * ap.getInt(AppConfig.RATER_PRO_REMIND_LATER)){
				showRaterDialog(context);
				return;
			}

			Long dateFirstLauch = ap.getLong(AppConfig.RATER_PRO_FIRST_USE);

			if (System.currentTimeMillis() >= dateFirstLauch + ( APP_DAYS_USES * ap.getInt(AppConfig.RATER_PRO_REMIND_LATER) * 24 * 60 * 60 * 1000)){
				showRaterDialog(context);
				return;
			}		


		} catch (NoPreferenceException e) {
			ap.put(true, AppConfig.RATER_PRO);
			ap.put(0, AppConfig.RATER_PRO_LAUCH_COUNTER);
			ap.put(System.currentTimeMillis(), AppConfig.RATER_PRO_FIRST_USE);
			ap.put(1, AppConfig.RATER_PRO_REMIND_LATER);

		}



	}

	/**
	 * This method check the app uses and the days after install
	 * and show a dialog to motivate the user rate the app
	 * @param context context of the application
	 * @param appUses When the app uses is bigger than this number
	 * show the dialog
	 * @param daysUses When the number of days after installation 
	 * is bigger than this number show the dialog.
	 */
	public static void checkRater (Context context ,int appUses,int daysUses){
		AppConfig ap = new AppConfig(context, AppConfig.NAME);

		try {
			//Check if the user select not show this again
			if (!ap.getBool(AppConfig.RATER_PRO)){
				int count = ap.getInt(AppConfig.RATER_PRO_LAUCH_COUNTER);

				//protect the case when the uses will bigger than MAX_INT xDDDDDDDD
				if (count > 65000)
					count = APP_USES + 1;
				else
					count ++;
				ap.put( count, AppConfig.RATER_PRO_LAUCH_COUNTER);
				return;
			}

			int counter = ap.getInt(AppConfig.RATER_PRO_LAUCH_COUNTER) + 1;
			Log.d("DEBUG","Counter " + counter + "  remind " + ap.getInt(AppConfig.RATER_PRO_REMIND_LATER));
			ap.put(counter, AppConfig.RATER_PRO_LAUCH_COUNTER );
			if (counter >= appUses * ap.getInt(AppConfig.RATER_PRO_REMIND_LATER)){
				showRaterDialog(context);
				return;
			}

			Long dateFirstLauch = ap.getLong(AppConfig.RATER_PRO_FIRST_USE);
			if (System.currentTimeMillis() >= dateFirstLauch + ( daysUses * ap.getInt(AppConfig.RATER_PRO_REMIND_LATER) * 24 * 60 * 60 * 1000)){
				showRaterDialog(context);
				return;
			}		


		} catch (NoPreferenceException e) {
			ap.put(true, AppConfig.RATER_PRO);
			ap.put(0, AppConfig.RATER_PRO_LAUCH_COUNTER);
			ap.put(System.currentTimeMillis(), AppConfig.RATER_PRO_FIRST_USE);
		}

	}

	public static void showRaterDialog (final Context context){

		//Create the dialog 
		final Dialog dialog;
		if (Build.VERSION.SDK_INT > 13)
			dialog = new Dialog(context, android.R.style.Theme_DeviceDefault_Dialog);
		else if (Build.VERSION.SDK_INT > 10)
			dialog = new Dialog(context, android.R.style.Theme_Holo_Dialog);
		else
			dialog = new Dialog(context, android.R.style.Theme_Dialog);


		//Get the app name and put the title
		String app_name = context.getResources().getString(R.string.app_name);
		final String app_package = context.getApplicationInfo().packageName;
		String Title = context.getResources().getString(R.string.rate) + " " + app_name;
		dialog.setTitle(Title);


		//Create the layout of the dialog
		LinearLayout ll = new LinearLayout(context);
		ll.setOrientation(LinearLayout.VERTICAL);
		ll.setGravity(Gravity.CENTER);


		ImageView im = new ImageView(context);		
		im.setImageDrawable(context.getResources().getDrawable(R.drawable.icon));
		//Calculate the size of the banner in pixels

		float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 70, context.getResources().getDisplayMetrics()); 
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams((int)px,(int)px);
		//		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
		//				LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

		px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, context.getResources().getDisplayMetrics()); 
		im.setLayoutParams(params);	
		im.setPadding(0, (int)px, 0, 0);
		ll.addView(im);

		TextView tv = new TextView(context);
		tv.setTextColor(Color.WHITE);
		tv.setText(context.getResources().getString(R.string.rate_text1) + app_name + context.getResources().getString(R.string.rate_text2) );
		px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, context.getResources().getDisplayMetrics()); 
		tv.setPadding((int)px, 0, (int)px, 10);
		ll.addView(tv);

		Button b1 = new Button(context);
		b1.setText(Title); //The same text than the title
		b1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + app_package)));
				//Change the value of rate to dont show more times the dialog rate
				AppConfig ap = new AppConfig(context, AppConfig.NAME);
				ap.put(false, AppConfig.RATER_PRO);
				dialog.dismiss();

			}
		});

		ll.addView(b1);

		Button b2 = new Button(context);
		b2.setText(context.getResources().getString(R.string.rate_later));
		b2.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				AppConfig ap = new AppConfig(context, AppConfig.NAME);
				try {
					ap.put(ap.getInt(AppConfig.RATER_PRO_REMIND_LATER) + 1, AppConfig.RATER_PRO_REMIND_LATER);
				} catch (NoPreferenceException e) {
					ap.put(2, AppConfig.RATER_PRO_REMIND_LATER);
				}
				dialog.dismiss();
			}
		});
		ll.addView(b2);

		Button b3 = new Button(context);
		b3.setText(context.getResources().getString(R.string.rate_no_thanks));
		b3.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				AppConfig ap = new AppConfig(context, AppConfig.NAME);
				ap.put(false, AppConfig.RATER_PRO);
				dialog.dismiss();
			}
		});
		ll.addView(b3);

		dialog.setContentView(ll);        
		dialog.show();        
	}



}

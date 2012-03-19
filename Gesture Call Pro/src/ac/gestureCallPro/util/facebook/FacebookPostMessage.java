package ac.gestureCallPro.util.facebook;


import java.io.IOException;
import java.net.MalformedURLException;

import ac.gestureCallPro.util.facebook.SessionEvents.AuthListener;
import ac.gestureCallPro.util.facebook.SessionEvents.LogoutListener;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.facebook.android.AsyncFacebookRunner;
import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.Facebook.DialogListener;
import com.facebook.android.FacebookError;


public class FacebookPostMessage implements DialogListener, AuthListener, LogoutListener{
	private static final String FACEBOOK_ID ="145501312222682"; 


	private Facebook facebookClient;
	private AsyncFacebookRunner mAsyncFBRunner;
	private Activity activity;
	private String post="";
	private String link="";
	private String image="";
	private String nameApp ="";
	private Boolean posteado = false;
	private String [] permisos = new String[] {"publish_stream"};
	private Handler handler = new Handler();



	public FacebookPostMessage (Activity act,String name, String mes, String l,String i){
		activity = act;
		nameApp = name;
		post = mes;
		link = l;
		image = i;

		facebookClient = new Facebook("145501312222682");
		SessionStore.restore(facebookClient, activity);
		SessionEvents.addAuthListener(this);
		SessionEvents.addLogoutListener(this);
		postMessageOnWall();



		//
		//		mAsyncFBRunner = new AsyncFacebookRunner(facebookClient);
		//
		//		facebookClient.authorize(activity, permisos, this);
		//
		//		Log.d("Gesture Call","Terminada petición autorizacion");

		//		//Try to post the message. if cant, when autorize the app we will try other time
		//		try
		//		{
		//			if (!posteado){
		//				posteado = true;
		//				Bundle parameters = new Bundle();
		//				parameters.putString("message", post);// the message to post to the wall
		//
		//				if (!link.equals("") && (link != null))
		//					parameters.putString("link",link);
		//
		//				if (!image.equals("") && (image != null))
		//					parameters.putString("picture", image);
		//
		//				if (!nameApp.equals("") && (nameApp!= null))
		//					parameters.putString("name", nameApp);    
		//
		//				facebookClient.dialog(activity, "stream.publish", parameters, this);
		//			}
		//		}
		//		catch (Exception e)
		//		{
		//			Log.e("Gesture Call",e.getMessage());
		//		}
		//
		//		Log.d("Gesture Call","Escrito comentario");
	}


	public void postMessageOnWall() {
		if (facebookClient.isSessionValid()) {
			Bundle parameters = new Bundle();
			parameters.putString("message", post);

			if (!link.equals("") && (link != null))
				parameters.putString("link",link);

			if (!image.equals("") && (image != null))
				parameters.putString("picture", image);

			if (!nameApp.equals("") && (nameApp!= null))
				parameters.putString("name", nameApp);   

			try {
				String response = facebookClient.request("me/feed", parameters,"POST");
				Log.d("Gesture Call" , " Response: " + response);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			login();
		}
	}



	public void login() {
		if (!facebookClient.isSessionValid()) {
			facebookClient.authorize(this.activity, this.permisos,Facebook.FORCE_DIALOG_AUTH,this);
		}
	}



	private void postMessageInThread() {
		Thread t = new Thread() {
			public void run() {

				try {
					postMessageOnWall();
					handler.post(mUpdateFacebookNotification);
				} catch (Exception ex) {
					Log.e("Gesture Call", "Error sending msg",ex);
				}
			}
		};
		t.start();
	}

	private void clearCredentials() {
		try {
			facebookClient.logout(activity);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}




	final Runnable mUpdateFacebookNotification = new Runnable() {
		public void run() {
			Toast.makeText(activity, "Facebook updated !", Toast.LENGTH_LONG).show();
			clearCredentials();
		}
	};


	@Override
	public void onCancel() {
		Log.i("Gesture Call","Autorizacion de facebook cancelada.\n");

	}

	@Override
	public void onComplete(Bundle values) {
		Log.d("Gesture Call","Autorización de facebook completada.");
		postMessageInThread();
	}

	@Override
	public void onError(DialogError arg0) {
		Log.e("Gesture Call","Error al intentar autorizar facebook.\n" + arg0.toString());

	}

	@Override
	public void onFacebookError(FacebookError arg0) {
		Log.e("Gesture Call","Error al intentar autorizar facebook, ERORR EN FACEBOOK.\n" + arg0.toString() + " " + arg0.getMessage());

	}



	//^^^^^^^  AuthListener ^^^^^^^^^^^^ //
	@Override
	public void onAuthSucceed() {
		Log.d("Gesture Call", "login ok onAuth");
		postMessageInThread();

	}

	@Override
	public void onAuthFail(String error) {
		Log.d("Gesture Call", "login mal onAuth");

	}


	//^^^^^^^  LogoutListener ^^^^^^^^^^^^ //

	@Override
	public void onLogoutBegin() {
		Log.d("Gesture Call", "logout ok onAuth");

	}

	@Override
	public void onLogoutFinish() {
		Log.d("Gesture Call", "logout mal onAuth");

	}


}

/**
 * Acdroid Apps for Android
 * 
 * @author Carlos Diaz Canovas
 * @author Marcos Trujillo Seoane
 * 
 * Project Gesture Call
 * 
 */
package ac.gestureCallPro.util.shortcut;

import ac.gestureCallPro.R;
import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;

public class CreateShortcut {
	public static final void create (Context context){
		Intent shortcutIntent = new Intent(Intent.ACTION_MAIN); 
		shortcutIntent.setClassName(context, context.getApplicationContext().getPackageName() + ".ui.main");

		Intent i = new Intent();
		i.setAction("com.android.launcher.action.INSTALL_SHORTCUT");
		i.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);
		i.putExtra(Intent.EXTRA_SHORTCUT_NAME, context.getResources().getString(R.string.app_name));
		Parcelable iconResource = Intent.ShortcutIconResource.fromContext(
				context,R.drawable.icon); 
		i.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE,iconResource);

		context.sendBroadcast(i);
	}
}

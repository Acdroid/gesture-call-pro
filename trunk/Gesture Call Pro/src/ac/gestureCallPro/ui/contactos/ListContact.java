/**
 * Acdroid Apps for Android
 * 
 * @author Carlos Diaz Canovas
 * @author Marcos Trujillo Seoane
 * 
 * Project Gesture Call
 * 
 */
package ac.gestureCallPro.ui.contactos;


import java.io.File;
import java.util.Set;

import ac.gestureCallPro.R;
import ac.gestureCallPro.ui.main;
import ac.gestureCallPro.ui.creadorGestos.CreadorGestos;
import ac.gestureCallPro.util.contactos.ContactCursor;
import ac.gestureCallPro.util.mToast.mToast;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.gesture.GestureLibraries;
import android.gesture.GestureLibrary;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.AlphabetIndexer;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SectionIndexer;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;



public final class ListContact extends ListActivity
{
	public static final int ID = 2;
	public static final String KEY_NAME ="NAME";
	public static final String KEY_PHONE ="PHONE";
	public GestureLibrary store;
	public ContactCursor manegadorCursor;

	public Cursor cursor;
	
	/**
	 * Called when the activity is first created. Responsible for initializing the UI.
	 * @param savedInstanceState Bundle
	 */
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.lista_contactos);     
	
		obtenLibreria();
		
		manegadorCursor = new ContactCursor(this);
		cursor =  manegadorCursor.getCursor();
		//startManagingCursor(cursor);
		String[] fields = new String[] {
				manegadorCursor.getNameNameColum(),
				manegadorCursor.getPhoneNameColum()
		};

        int[] to = new int[] { R.id.item_lista_nombre,R.id.item_lista_numero};

		SimpleCursorAdapter adapter = new mySimpleCursorAdapter(this, R.layout.item_lista_contactos, cursor,
				fields, to);

		setListAdapter(adapter);
	}


	/**
	 * Obtiene la store con los gestos
	 */
	private void obtenLibreria(){
		File mStoreFile = new File(Environment.getExternalStorageDirectory() + "/GestureCall", "gestures");
		store = GestureLibraries.fromFile(mStoreFile);
		if (!store.load()){
			Log.d("DEBUG","Store.load == null");
			if ( store.getGestureEntries() == null)
				mToast.Make(this, "Error while obtain the Gestures Library. Try to close and open Gesture Call", 0);
		}
	}

	/**
	 * Obtains the contact list for the currently selected account.
	 *
	
	 * @return A cursor for for accessing the contact list. */
	@SuppressWarnings("unused")
	private Cursor getContacts(){
		return new ContactCursor(this).getCursor();
    }



	/**
	 * Method onListItemClick.
	 * @param l ListView
	 * @param v View
	 * @param position int
	 * @param id long
	 */
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		Intent i = new Intent(ListContact.this,CreadorGestos.class);
		String nombre = cursor.getString(cursor.getColumnIndex(manegadorCursor.getNameNameColum()));
		String phone= cursor.getString(cursor.getColumnIndex(manegadorCursor.getPhoneNameColum()));
		
		
		i.putExtra(KEY_NAME, nombre);
		i.putExtra(KEY_PHONE, phone);
		startActivityForResult(i, ID);
	}
	
    /**
     * Method onActivityResult.
     * @param requestCode int
     * @param resultCode int
     * @param data Intent
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        
        
        if (requestCode == ID){
			switch (resultCode){
			case main.RESULT_OK:
				break;
			case main.RESULT_ERROR:
				break;
			case main.RESULT_SALIR:
				setResult(main.RESULT_SALIR);
				ListContact.this.finish();
				break;
			case main.RESULT_GESTO_ADD_OK:
				setResult(main.RESULT_OK);
				ListContact.this.finish();
				break;
			default:
				super.onActivityResult(requestCode, resultCode, data);
			}
			
			
		}
	}
        
	
	

	private class mySimpleCursorAdapter extends SimpleCursorAdapter implements SectionIndexer{

		/**
		 * Field mContext.
		 */
		private Context mContext;
		/**
		 * Field gestosNoNull.
		 */
		Set<String> gestosNoNull;
		/**
		 * Field alphaIndexer.
		 */
		AlphabetIndexer alphaIndexer;

		/**
		 * Constructor for mySimpleCursorAdapter.
		 * @param context Context
		 * @param layout int
		 * @param c Cursor
		 * @param from String[]
		 * @param to int[]
		 */
		public mySimpleCursorAdapter(Context context, int layout, Cursor c,
				String[] from, int[] to) {
			super(context, layout, c, from, to);

			this.mContext = context;
			gestosNoNull = store.getGestureEntries();	
			alphaIndexer=new AlphabetIndexer(c,c.getColumnIndex(manegadorCursor.getNameNameColum()), " ABCDEFGHIJKLMNOPQRSTUVWXYZ");
		}


		@Override
		public int getPositionForSection(int section) {
			return alphaIndexer.getPositionForSection(section);
		}


		@Override
		public int getSectionForPosition(int position) {
			return alphaIndexer.getSectionForPosition(position);
		}


		@Override
		public Object[] getSections() {
			return alphaIndexer.getSections();
		}

		
		@Override
		public void bindView(View view, Context context, Cursor cursor) {
			super.bindView(view, context, cursor);	
			
			TextView t2 = (TextView) view.findViewById(R.id.item_lista_numero);
			String phone =t2.getText().toString();
			ImageView i = (ImageView)view.findViewById(R.id.item_list_image);
			if ( gestosNoNull.contains(phone) ){
				i.setImageResource(R.drawable.btn_check_on);
			}
			else{
				i.setImageResource(R.drawable.btn_check_off);
			}
		}
	}
	

}

/**
 *  Poner licencia
 * @author Carlos Diaz Canovas y Marcos Trujillo Seoane
 * 
 */
package ac.gestureCallPro.ui.contactos;


import java.util.Set;

import ac.gestureCallPro.R;
import ac.gestureCallPro.ui.main;
import ac.gestureCallPro.ui.creadorGestos.CreadorGestos;
import ac.gestureCallPro.util.contactos.ContactCursor;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.gesture.GestureLibrary;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.Data;
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

	public Cursor cursor;
	/**
	 * Called when the activity is first created. Responsible for initializing the UI.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.lista_contactos);     
		
		store = main.getStore();
		
		cursor = getContacts();
		startManagingCursor(cursor);
		String[] fields = new String[] {
				Data.DISPLAY_NAME,
				Phone.NUMBER
		};

        int[] to = new int[] { R.id.item_lista_nombre,R.id.item_lista_numero};

		SimpleCursorAdapter adapter = new mySimpleCursorAdapter(this, R.layout.item_lista_contactos, cursor,
				fields, to);

		setListAdapter(adapter);
	}



	/**
	 * Obtains the contact list for the currently selected account.
	 *
	 * @return A cursor for for accessing the contact list.
	 */
	private Cursor getContacts()
	{
        // Run query
//        Uri uri = ContactsContract.Contacts.CONTENT_URI;
//        String[] projection = new String[] {
//                ContactsContract.Contacts._ID,
//                ContactsContract.Contacts.DISPLAY_NAME
//        };
//        String selection = ContactsContract.Contacts.IN_VISIBLE_GROUP + " = '1'";
//        
//        String[] selectionArgs = null;
//        String sortOrder = ContactsContract.Contacts.DISPLAY_NAME + " COLLATE LOCALIZED ASC";

		
//		Uri uri =  Data.CONTENT_URI;
//		String[] projection = new String []{
//				Data._ID,
//				Data.DISPLAY_NAME,
//				Phone.NUMBER,
//				Phone.TYPE 
//		};
//		String selection = Data.MIMETYPE + "='" + Phone.CONTENT_ITEM_TYPE + "' AND "
//        		+ Phone.NUMBER + " IS NOT NULL";
//		
//		String[] selectionArgs = null;
//		String sortOrder = Data.DISPLAY_NAME + " COLLATE LOCALIZED ASC";
//        return managedQuery(uri, projection, selection, selectionArgs, sortOrder);
		return new ContactCursor(this).getCursor();
    }



	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		Intent i = new Intent(ListContact.this,CreadorGestos.class);
		String nombre = cursor.getString(cursor.getColumnIndex(Data.DISPLAY_NAME));
		String phone= cursor.getString(cursor.getColumnIndex(Phone.NUMBER));
		
		
		i.putExtra(KEY_NAME, nombre);
		i.putExtra(KEY_PHONE, phone);
		startActivityForResult(i, ID);
	}
	
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

		private Context mContext;
		Set<String> gestosNoNull;
		AlphabetIndexer alphaIndexer;

		public mySimpleCursorAdapter(Context context, int layout, Cursor c,
				String[] from, int[] to) {
			super(context, layout, c, from, to);

			this.mContext = context;
			gestosNoNull = store.getGestureEntries();	
			alphaIndexer=new AlphabetIndexer(c,c.getColumnIndex(Data.DISPLAY_NAME), " ABCDEFGHIJKLMNOPQRSTUVWXYZ");
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

			//Cambiamos el color de fondo
			//LinearLayout l = (LinearLayout) view.findViewById(R.id.lay_item);
			TextView t = (TextView) view.findViewById(R.id.item_lista_nombre);
			t.setTextColor(mContext.getResources().getColor(android.R.color.white));
//			if (cursor.getPosition() % 2 == 0){
//				l.setBackgroundResource(R.drawable.linearlayout_color_gradient_down);
//				
//			}
//			else{
//				l.setBackgroundResource(R.drawable.linearlayout_color_gradient_up);
//			}
			super.bindView(view, context, cursor);
			
			//Comprobamos si hay gesto o no
			
			
			TextView t2 = (TextView) view.findViewById(R.id.item_lista_numero);
			String phone =t2.getText().toString();
			ImageView i = (ImageView)view.findViewById(R.id.item_list_image);
			if (gestosNoNull.contains(phone) == true){
				i.setImageResource(R.drawable.btn_check_on);
			}
			else{
				i.setImageResource(R.drawable.btn_check_off);
			}
		}
	}
	

}

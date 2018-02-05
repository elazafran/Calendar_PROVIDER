package com.example.contentprovider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

public class MiContentProvider extends ContentProvider {

	private MiBaseDatos MBD;
	SQLiteDatabase SQLDB;
	private static final String NOMBRE_CP = "mi.content.provider.contactos";
	
	private static final int CONTACTOS = 1;
	private static final int CONTACTOS_ID = 2;
	private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
	static{
		uriMatcher.addURI(NOMBRE_CP, "contactos", CONTACTOS);
		uriMatcher.addURI(NOMBRE_CP, "contactos/#", CONTACTOS_ID);
	}
	
	
	
	@Override
	public String getType(Uri uri) {
		return null;
	}
	
	
	
	@Override
	public boolean onCreate() {
		MBD = new MiBaseDatos(getContext());
		return true;
	}
	
	
	
	@Override
	public Uri insert(Uri uri, ContentValues values) {
		long registro = 0;
		try {
			if (uriMatcher.match(uri) == CONTACTOS) {
				SQLDB = MBD.getWritableDatabase();
				registro = SQLDB.insert("contactos", null, values);
		    }
		} catch (IllegalArgumentException e) {
			Log.e("ERROR", "Argumento no admitido: " + e.toString());
		}
		
		// Comprobar si se inserto bien el registro
		if (registro > 0) {
			Log.e("INSERT", "Registro creado correctamente");
		} else {
			Log.e("Error", "Al insertar registro: " + registro);
		}
		
		return Uri.parse("contactos/" + registro);
	}
	
	
	
	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		String id = "";
		try {
			if (uriMatcher.match(uri) == CONTACTOS_ID) {
				id = uri.getLastPathSegment();
				SQLDB = MBD.getWritableDatabase();
				SQLDB.update("contactos", values, "_id=" + id, selectionArgs);
		    }
		} catch (IllegalArgumentException e) {
			Log.e("ERROR", "Argumento no admitido: " + e.toString());
		}
		
		return Integer.parseInt(id);
	}
	
	
	
	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		int registro = 0;
		try {
			if (uriMatcher.match(uri) == CONTACTOS_ID) {
				String id = "_id=" + uri.getLastPathSegment();
				SQLDB = MBD.getWritableDatabase();
				registro = SQLDB.delete("contactos", id, null);
		    }
		} catch (IllegalArgumentException e) {
			Log.e("ERROR", "Argumento no admitido: " + e.toString());
		}
		
	    return registro;
	}
	
	
	
	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		Cursor c = null;
		try {
			switch (uriMatcher.match(uri)) {
			case CONTACTOS_ID:
				String id = "_id=" + uri.getLastPathSegment();
				SQLDB = MBD.getReadableDatabase();
				c = SQLDB.query("contactos", projection, id, selectionArgs, 
						null, null, null, sortOrder);
				break;
			case CONTACTOS:
				SQLDB = MBD.getReadableDatabase();
				c = SQLDB.query("contactos", projection, null, selectionArgs, 
						null, null, null, sortOrder);
				break;
			}
		} catch (IllegalArgumentException e) {
			Log.e("ERROR", "Argumento no admitido: " + e.toString());
		}
		
		return c;
	}
	
	
	
}
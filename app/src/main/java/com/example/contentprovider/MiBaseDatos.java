package com.example.contentprovider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MiBaseDatos extends SQLiteOpenHelper {

	private static final String TABLA_CONTACTOS = "CREATE TABLE contactos " +
			"(_id INT PRIMARY KEY, nombre TEXT, telefono INT, email TEXT)";
	
	
	public MiBaseDatos(Context context) {
		super(context, "mibasedatos.db", null, 1);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(TABLA_CONTACTOS);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS" + TABLA_CONTACTOS);
        onCreate(db);
	}
}
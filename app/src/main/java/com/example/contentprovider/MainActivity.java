package com.example.contentprovider;

import com.example.contentprovider.R;

import android.Manifest;
import android.content.ContentUris;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.util.Log;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.view.View;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

public class MainActivity extends Activity {
	
	private static final Uri URI_CP = Uri.parse("content://mi.content.provider.contactos/contactos");
	private Uri uri;
	private Cursor c;
	
	private int id;
	private String nombre;
	private int telefono;
	private String email;

    private static final String DEBUG_TAG = "MyActivity";
    public static final String[] INSTANCE_PROJECTION = new String[] {
            CalendarContract.Instances.EVENT_ID,      // 0
            CalendarContract.Instances.BEGIN,         // 1
            CalendarContract.Instances.TITLE          // 2
    };

    // The indices for the projection array above.
    private static final int PROJECTION_ID_INDEX = 0;
    private static final int PROJECTION_BEGIN_INDEX = 1;
    private static final int PROJECTION_TITLE_INDEX = 2;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        
//        ContentResolver CR = getContentResolver();
        
        
        // Insertamos 4 registros
//        CR.insert(URI_CP, setVALORES(1, "Pedro", 111111111, "pedro@DB.es"));
//        CR.insert(URI_CP, setVALORES(2, "Sandra", 222222222, "sandra@DB.es"));
//        uri = CR.insert(URI_CP, setVALORES(3, "Maria", 333333333, "maria@DB.es"));
//        Log.d("REGISTRO INSERTADO", uri.toString());
//        uri = CR.insert(URI_CP, setVALORES(4, "Dani", 444444444, "dani@DB.es"));
//        Log.d("REGISTRO INSERTADO", uri.toString());
//
        
        // Recuperamos todos los registros de la tabla
//        String[] valores_recuperar = {"_id", "nombre", "telefono", "email"};
//        c = CR.query(URI_CP, valores_recuperar, null, null, null);
//        c.moveToFirst();
//        do {
//        	id = c.getInt(0);
//        	nombre = c.getString(1);
//            telefono = c.getInt(2);
//            email = c.getString(3);
//            Log.d("QUERY", "" +id+ ", " +nombre+ ", " +telefono+ ", " +email);
//		} while (c.moveToNext());
//
//
//        // Actualizamos un registro de la tabla
//        uri = Uri.parse("content://mi.content.provider.contactos/contactos/3");
//        CR.update(uri, setVALORES(3, "PPPPP", 121212121, "xxxx@xxxx.es"),
//        		null, null);
//        // Y lo mostramos en el log
//        c = CR.query(uri, valores_recuperar, null, null, null);
//        c.moveToFirst();
//        id = c.getInt(0);
//        nombre = c.getString(1);
//        telefono = c.getInt(2);
//        email = c.getString(3);
//        Log.d("QUERY", "" +id+ ", " +nombre+ ", " +telefono+ ", " +email);
//
//
//        // Borramos un registro
//        uri = Uri.parse("content://mi.content.provider.contactos/contactos/4");
//        CR.delete(uri, null, null);

        //getDataFromCalendarTable();


        getCalendarEvents();
    }


    private ContentValues setVALORES(int id, String nom, int tlf, String email) {
    	ContentValues valores = new ContentValues();
    	valores.put("_id", id);
		valores.put("nombre", nom);
		valores.put("telefono", tlf);
		valores.put("email", email);
		return valores;
	}

    public void getDataFromCalendarTable() {
        Cursor cur = null;
        ContentResolver cr = getContentResolver();

        String[] mProjection =
                {
                        CalendarContract.Calendars.ALLOWED_ATTENDEE_TYPES,
                        CalendarContract.Calendars.ACCOUNT_NAME,
                        CalendarContract.Calendars.CALENDAR_DISPLAY_NAME,
                        CalendarContract.Calendars.CALENDAR_LOCATION,
                        CalendarContract.Calendars.CALENDAR_TIME_ZONE
                };

        Uri uri = CalendarContract.Calendars.CONTENT_URI;
        String selection = "((" + CalendarContract.Calendars.ACCOUNT_NAME + " = ?) AND ("
                + CalendarContract.Calendars.ACCOUNT_TYPE + " = ?) AND ("
                + CalendarContract.Calendars.OWNER_ACCOUNT + " = ?))";
        String[] selectionArgs = new String[]{"rmenmar264@gmail.com", "com.google",
                "rmenmar264@gmail.com"};

//        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
//            ContextCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CALENDAR}, MY_CAL_REQ);
//        }
        cur = cr.query(uri, mProjection, selection, selectionArgs, null);

        while (cur.moveToNext()) {
            String displayName = cur.getString(cur.getColumnIndex(CalendarContract.Calendars.CALENDAR_DISPLAY_NAME));
            String accountName = cur.getString(cur.getColumnIndex(CalendarContract.Calendars.ACCOUNT_NAME));

            Log.i("INFO:",displayName);

        }

    }

    private void getCalendarEvents(){
        // Specify the date range you want to search for recurring
// event instances
        Calendar beginTime = Calendar.getInstance();
        beginTime.set(2018, 1, 1, 8, 0);
        long startMillis = beginTime.getTimeInMillis();
        Calendar endTime = Calendar.getInstance();
        endTime.set(2018, 1, 31, 8, 0);
        long endMillis = endTime.getTimeInMillis();

        Cursor cur = null;
        ContentResolver cr = getContentResolver();

// The ID of the recurring event whose instances you are searching
// for in the Instances table
        String selection = CalendarContract.Instances.EVENT_ID + " = ?";
        String[] selectionArgs = new String[] {"207"};

// Construct the query with the desired date range.
        Uri.Builder builder = CalendarContract.Instances.CONTENT_URI.buildUpon();
        ContentUris.appendId(builder, startMillis);
        ContentUris.appendId(builder, endMillis);

// Submit the query
        cur =  cr.query(builder.build(),
                INSTANCE_PROJECTION,
                null,
                null,
                null);

        while (cur.moveToNext()) {
            String title = null;
            long eventID = 0;
            long beginVal = 0;

            // Get the field values
            eventID = cur.getLong(PROJECTION_ID_INDEX);
            beginVal = cur.getLong(PROJECTION_BEGIN_INDEX);
            title = cur.getString(PROJECTION_TITLE_INDEX);

            // Do something with the values.
            Log.i(DEBUG_TAG, "Event:  " + title);
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(beginVal);
            DateFormat formatter = new SimpleDateFormat("MM/dd/yyyy hh:mm");
            Log.i(DEBUG_TAG, "Date: " + formatter.format(calendar.getTime()));
        }
    }


}
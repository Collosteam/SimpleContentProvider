package com.collosteam.SimpleContentProvider;

import android.app.Service;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.IBinder;
import android.util.Log;

/**
 * Created by Collos on 16.01.14.
 */
public class ProviderService extends Service {

    final String LOG_TAG = "{ProviderService}";

    public static String ACTION_INSERT = "action.insert";
    public static String ACTION_UPDATE = "action.update";
    public static String ACTION_DELETE = "action.delete";


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        ContentValues cv;
        if (intent != null) {

            String action = intent.getAction();


            if (ACTION_INSERT.equals(action)) {

                cv = new ContentValues();
                cv.put(MyActivity.CONTACT_NAME, intent.getStringExtra("name"));
                cv.put(MyActivity.CONTACT_EMAIL, intent.getStringExtra("email"));
                Uri newUri = getContentResolver().insert(MyActivity.CONTACT_URI, cv);
                Log.d(LOG_TAG, "insert, result Uri : " + newUri.toString());
                this.stopSelf();

            }

            if (ACTION_UPDATE.equals(action)) {

                cv = new ContentValues();
                cv.put(MyActivity.CONTACT_NAME, intent.getStringExtra("name"));
                cv.put(MyActivity.CONTACT_EMAIL, intent.getStringExtra("email"));
                Uri uri = ContentUris.withAppendedId(MyActivity.CONTACT_URI, intent.getIntExtra("id", -1));
                int cnt = getContentResolver().update(uri, cv, null, null);
                Log.d(LOG_TAG, "update, count = " + cnt);


            }


            if (ACTION_DELETE.equals(action)) {


                Uri uri = ContentUris.withAppendedId(MyActivity.CONTACT_URI, intent.getIntExtra("id", -1));
                int cnt = getContentResolver().delete(uri, null, null);
                Log.d(LOG_TAG, "delete, count = " + cnt);


            }


        }

        return super.onStartCommand(intent, flags, startId);
    }

    public IBinder onBind(Intent intent) {


        return null;
    }


}

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

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        ContentValues cv;
        if (intent != null) {


            cv = new ContentValues();
            cv.put(MyActivity.CONTACT_NAME, intent.getStringExtra("name"));
            cv.put(MyActivity.CONTACT_EMAIL, intent.getStringExtra("email"));
            Uri newUri = getContentResolver().insert(MyActivity.CONTACT_URI, cv);
            Log.d(LOG_TAG, "insert, result Uri : " + newUri.toString());

            this.stopSelf();

        }

        return super.onStartCommand(intent, flags, startId);
    }

    public IBinder onBind(Intent intent) {


            return null;
        }



}

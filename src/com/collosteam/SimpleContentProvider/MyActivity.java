package com.collosteam.SimpleContentProvider;


import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class MyActivity extends Activity {

    final String LOG_TAG = "myLogs";

    public static final Uri CONTACT_URI = Uri
            .parse("content://com.collosteam.provider.AdressBook/contacts");

    public static final String CONTACT_NAME = "name";
    public static final String CONTACT_EMAIL = "email";

    EditText etName;
    EditText etEmail;
    int selectedID = -1;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        Cursor cursor = getContentResolver().query(CONTACT_URI, null, null,
                null, null);

        startManagingCursor(cursor);

        String from[] = {"name", "email"};
        int to[] = {android.R.id.text1, android.R.id.text2};
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this,
                android.R.layout.simple_list_item_2, cursor, from, to);

        ListView lvContact = (ListView) findViewById(R.id.lvContact);
        lvContact.setAdapter(adapter);

        lvContact.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                selectedID = (int) id;

            }
        });

        etName = (EditText) findViewById(R.id.editText_name);
        etEmail = (EditText) findViewById(R.id.editText_email);

    }

    /**
     * Вставляем данные через сервис
     */

    public void onClickInsert(View v) {

        String name = etName.getText().toString();
        String email = etEmail.getText().toString();

        if (name.trim().length() > 0 && email.trim().length() > 0) {
            Intent intent = new Intent(this, ProviderService.class);
            intent.setAction(ProviderService.ACTION_INSERT);
            intent.putExtra("name", name);
            intent.putExtra("email", email);
            startService(intent);

       /*     ContentValues cv = new ContentValues();
        cv.put(CONTACT_NAME, name);
        cv.put(CONTACT_EMAIL, email);

        Uri newUri = getContentResolver().insert(CONTACT_URI, cv);
        Log.d(LOG_TAG, "insert, result Uri : " + newUri.toString());*/

        }
    }

    public void onClickUpdate(View v) {

        String name = etName.getText().toString();
        String email = etEmail.getText().toString();

        if (name.trim().length() > 0 && email.trim().length() > 0 && selectedID > 0) {
            Intent intent = new Intent(this, ProviderService.class);
            intent.setAction(ProviderService.ACTION_UPDATE);
            intent.putExtra("id", selectedID);
            intent.putExtra("name", name);
            intent.putExtra("email", email);
            startService(intent);
            selectedID = -1;
        }




       /* ContentValues cv = new ContentValues();
        cv.put(CONTACT_NAME, "name 5");
        cv.put(CONTACT_EMAIL, "email 5");
        Uri uri = ContentUris.withAppendedId(CONTACT_URI, 2);
        int cnt = getContentResolver().update(uri, cv, null, null);
        Log.d(LOG_TAG, "update, count = " + cnt);*/
    }

    public void onClickDelete(View v) {

        if (selectedID > 0) {

            Intent intent = new Intent(this, ProviderService.class);
            intent.setAction(ProviderService.ACTION_DELETE);
            intent.putExtra("id", selectedID);
            startService(intent);
            selectedID = -1;
        }


    }

    public void onClickError(View v) {
        Uri uri = Uri.parse("content://ru.startandroid.providers.AdressBook/phones");
        try {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        } catch (Exception ex) {
            Log.d(LOG_TAG, "Error: " + ex.getClass() + ", " + ex.getMessage());
        }

    }
}

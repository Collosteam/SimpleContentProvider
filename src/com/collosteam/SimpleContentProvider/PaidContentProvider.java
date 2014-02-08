package com.collosteam.SimpleContentProvider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

/**
 * Created by Droid on 05.02.14.
 */
public class PaidContentProvider extends ContentProvider {


    final String LOG_TAG = "PaidContentProvider";

    // // Константы для БД
    // БД
    static final String DB_NAME = "collosDB";
    static final int DB_VERSION = 1;

    // Таблица
    static final String PAID_TABLE = "paid";

    // Поля
    static final String ID = "_id";
    static final String DATE = "date";
    static final String PRODUCT_NAME = "product_name";
    static final String PRICE = "price";
    static final String COUNT = "count";
    static final String SHOP = "shop";
    static final String STATUS = "status";

    // Запрос создания таблицы
    static final String DB_CREATE = "create table if not exist" + PAID_TABLE + "("
            + ID + " INTEGER primary key autoincrement, "
            + DATE + " INTEGER, "
            + PRODUCT_NAME + " TEXT, "
            + PRICE + " REAL, "
            + COUNT + " REAL, "
            + SHOP + " TEXT, "
            + STATUS + " INTEGER)";   //, UNIQUE(" + PRODUCT_NAME + ") ON CONFLICT REPLACE)

    // // Uri

    // authority
    static final String AUTHORITY = "com.collosteam.provider.PaidContentProvider";

    // path
    static final String PAID_PATH = PAID_TABLE;


    // Общий Uri
    public static final Uri CONTACT_CONTENT_URI = Uri.parse("content://"
            + AUTHORITY + "/" + PAID_PATH);


    // Типы данных
    // набор строк
    static final String PAID_CONTENT_TYPE = "vnd.android.cursor.dir/vnd."
            + AUTHORITY + "." + PAID_PATH;

    // одна строка
    static final String PAID_CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd."
            + AUTHORITY + "." + PAID_PATH;


    //// UriMatcher
    // общий Uri
    static final int URI_PAID = 1;

    // Uri с указанным ID
    static final int URI_PAID_ID = 2;

    // Uri с указанным названием магазина SHOP
    static final int URI_PAID_SHOP = 3;

    // Uri с указаной датой DATE
    static final int URI_PAID_DATE = 4;

    // Uri с указанным STATUS
    static final int URI_PAID_STATUS = 5;

    // описание и создание UriMatcher
    private static final UriMatcher uriMatcher;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHORITY, PAID_PATH, URI_PAID);
        uriMatcher.addURI(AUTHORITY, PAID_PATH + "/#", URI_PAID_ID);
        uriMatcher.addURI(AUTHORITY, PAID_PATH + "/#", URI_PAID_SHOP);
        uriMatcher.addURI(AUTHORITY, PAID_PATH + "/#", URI_PAID_DATE);
        uriMatcher.addURI(AUTHORITY, PAID_PATH + "/#", URI_PAID_STATUS);
    }


    DBHelper dbHelper;
    SQLiteDatabase db;


    @Override
    public boolean onCreate() {
        Log.d(LOG_TAG, "onCreate");
        dbHelper = new DBHelper(getContext());
        return true;
    }

    //Reading
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Log.d(LOG_TAG, "query, " + uri.toString());
        // проверяем Uri
        switch (uriMatcher.match(uri)) {
            case URI_PAID: // общий Uri
                Log.d(LOG_TAG, "URI_PAID");
                // если сортировка не указана, ставим свою - по имени
                if (TextUtils.isEmpty(sortOrder)) {
                    sortOrder = ID;
                }
                break;
            case URI_PAID_ID: // Uri с ID
                String id = uri.getLastPathSegment();
                Log.d(LOG_TAG, "URI_PAID_ID, " + id);
                // добавляем ID к условию выборки
                if (TextUtils.isEmpty(selection)) {
                    selection = ID + " = " + id;
                } else {
                    selection = selection + " AND " + ID + " = " + id;
                }
                break;

            case URI_PAID_DATE: // Uri с DATE
                String date = uri.getLastPathSegment();
                Log.d(LOG_TAG, "URI_PAID_DATE, " + date);
                // добавляем ID к условию выборки
                if (TextUtils.isEmpty(selection)) {
                    selection = DATE + " = " + date;
                } else {
                    selection = selection + " AND " + DATE + " = " + date;
                }
                break;

            default:
                throw new IllegalArgumentException("Wrong URI: " + uri);
        }

        try {
            db.beginTransaction();
            db = dbHelper.getWritableDatabase();
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }

        Cursor cursor = db.query(PAID_TABLE, projection, selection,
                selectionArgs, null, null, sortOrder);
        // просим ContentResolver уведомлять этот курсор
        // об изменениях данных в CONTACT_CONTENT_URI
        cursor.setNotificationUri(getContext().getContentResolver(),
                CONTACT_CONTENT_URI);
        return cursor;
    }

    @Override
    public String getType(Uri uri) {
        Log.d(LOG_TAG, "getType, " + uri.toString());
        switch (uriMatcher.match(uri)) {
            case URI_PAID:
            case URI_PAID_DATE:
            case URI_PAID_STATUS:
            case URI_PAID_SHOP:
                return PAID_CONTENT_TYPE;


            case URI_PAID_ID:

                return PAID_CONTENT_ITEM_TYPE;
        }
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }


    private class DBHelper extends SQLiteOpenHelper {

        public DBHelper(Context context) {
            super(context, DB_NAME, null, DB_VERSION);
        }

        public void onCreate(SQLiteDatabase db) {
            db.execSQL(DB_CREATE);
        }


        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

            //TODO Добавить скрипт для обновления БД
        }
    }


}

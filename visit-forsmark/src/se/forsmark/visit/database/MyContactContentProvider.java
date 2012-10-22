package se.forsmark.visit.database;

import java.util.Arrays;
import java.util.HashSet;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;


//public class MyContactContentProvider extends ContentProvider {
/*
  // database
  private ContactDatabaseHelper database;

  // Used for the UriMacher
  private static final int CONTACTS = 10;
  private static final int CONTACT_ID = 20;

  private static final String AUTHORITY = "se.forsmark.visit.database.contentprovider";

  private static final String BASE_PATH = "contacts";
  public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY
      + "/" + BASE_PATH);

  public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE
      + "/contacts";
  public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE
      + "/contact";

  private static final UriMatcher sURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);
  static {
    sURIMatcher.addURI(AUTHORITY, BASE_PATH, CONTACTS);
    sURIMatcher.addURI(AUTHORITY, BASE_PATH + "/#", CONTACT_ID);
  }

  @Override
  public boolean onCreate() {
    database = new ContactDatabaseHelper(getContext());
    return false;
  }

  @Override
  public Cursor query(Uri uri, String[] projection, String selection,
      String[] selectionArgs, String sortOrder) {

    // Uisng SQLiteQueryBuilder instead of query() method
    SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();

    // Check if the caller has requested a column which does not exists
    checkColumns(projection);

    // Set the table
    queryBuilder.setTables(ContactTable.TABLE_CONTACT);

    int uriType = sURIMatcher.match(uri);
    switch (uriType) {
    case CONTACTS:
      break;
    case CONTACT_ID:
      // Adding the ID to the original query
      queryBuilder.appendWhere(ContactTable.COLUMN_ID + "="
          + uri.getLastPathSegment());
      break;
    default:
      throw new IllegalArgumentException("Unknown URI: " + uri);
    }

    SQLiteDatabase db = database.getWritableDatabase();
    Cursor cursor = queryBuilder.query(db, projection, selection,
        selectionArgs, null, null, sortOrder);
    // Make sure that potential listeners are getting notified
    cursor.setNotificationUri(getContext().getContentResolver(), uri);

    return cursor;
  }

  @Override
  public String getType(Uri uri) {
    return null;
  }

  @Override
  public Uri insert(Uri uri, ContentValues values) {
    int uriType = sURIMatcher.match(uri);
    SQLiteDatabase sqlDB = database.getWritableDatabase();
    int rowsDeleted = 0;
    long id = 0;
    switch (uriType) {
    case CONTACTS:
      id = sqlDB.insert(ContactTable.TABLE_CONTACT, null, values);
      break;
    default:
      throw new IllegalArgumentException("Unknown URI: " + uri);
    }
    getContext().getContentResolver().notifyChange(uri, null);
    return Uri.parse(BASE_PATH + "/" + id);
  }

  @Override
  public int delete(Uri uri, String selection, String[] selectionArgs) {
    int uriType = sURIMatcher.match(uri);
    SQLiteDatabase sqlDB = database.getWritableDatabase();
    int rowsDeleted = 0;
    switch (uriType) {
    case CONTACTS:
      rowsDeleted = sqlDB.delete(ContactTable.TABLE_CONTACT, selection,
          selectionArgs);
      break;
    case CONTACT_ID:
      String id = uri.getLastPathSegment();
      if (TextUtils.isEmpty(selection)) {
        rowsDeleted = sqlDB.delete(ContactTable.TABLE_CONTACT,
            ContactTable.COLUMN_ID + "=" + id, 
            null);
      } else {
        rowsDeleted = sqlDB.delete(ContactTable.TABLE_CONTACT,
            ContactTable.COLUMN_ID + "=" + id 
            + " and " + selection,
            selectionArgs);
      }
      break;
    default:
      throw new IllegalArgumentException("Unknown URI: " + uri);
    }
    getContext().getContentResolver().notifyChange(uri, null);
    return rowsDeleted;
  }

  @Override
  public int update(Uri uri, ContentValues values, String selection,
      String[] selectionArgs) {

    int uriType = sURIMatcher.match(uri);
    SQLiteDatabase sqlDB = database.getWritableDatabase();
    int rowsUpdated = 0;
    switch (uriType) {
    case CONTACTS:
      rowsUpdated = sqlDB.update(ContactTable.TABLE_CONTACT, 
          values, 
          selection,
          selectionArgs);
      break;
    case CONTACT_ID:
      String id = uri.getLastPathSegment();
      if (TextUtils.isEmpty(selection)) {
        rowsUpdated = sqlDB.update(ContactTable.TABLE_CONTACT, 
            values,
            ContactTable.COLUMN_ID + "=" + id, 
            null);
      } else {
        rowsUpdated = sqlDB.update(ContactTable.TABLE_CONTACT, 
            values,
            ContactTable.COLUMN_ID + "=" + id 
            + " and " 
            + selection,
            selectionArgs);
      }
      break;
    default:
      throw new IllegalArgumentException("Unknown URI: " + uri);
    }
    getContext().getContentResolver().notifyChange(uri, null);
    return rowsUpdated;
  }

  
  
  private void checkColumns(String[] projection) {
    String[] available = { ContactTable.COLUMN_FIRSTNAME,
        ContactTable.COLUMN_LASTNAME, ContactTable.COLUMN_PNMBR,
        ContactTable.COLUMN_ID, ContactTable.COLUMN_SEX, ContactTable.COLUMN_ADRESS, 
        ContactTable.COLUMN_ADRESS, ContactTable.COLUMN_POSTNMBR, ContactTable.COLUMN_POSTADRESS,
        ContactTable.COLUMN_COUNTRY, ContactTable.COLUMN_CELLPHONE, ContactTable.COLUMN_EMAIL };
    if (projection != null) {
      HashSet<String> requestedColumns = new HashSet<String>(Arrays.asList(projection));
      HashSet<String> availableColumns = new HashSet<String>(Arrays.asList(available));
      // Check if all columns which are requested are available
      if (!availableColumns.containsAll(requestedColumns)) {
        throw new IllegalArgumentException("Unknown columns in projection");
      }
    }
    
  }
  
*/

//} 

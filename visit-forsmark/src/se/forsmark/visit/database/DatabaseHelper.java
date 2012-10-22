package se.forsmark.visit.database;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;




	public class DatabaseHelper extends SQLiteOpenHelper {
		// Database table
		  public static final String TABLE_CONTACT = "contact";
		  public static final String COLUMN_ID = "_id";
		  public static final String COLUMN_FIRSTNAME = "firstname";
		  public static final String COLUMN_LASTNAME = "lastname";
		  public static final String COLUMN_PNMBR = "pnmbr";
		  public static final String COLUMN_SEX = "sex";
		  public static final String COLUMN_ADRESS = "adress";
		  public static final String COLUMN_POSTNMBR = "postnmbr";
		  public static final String COLUMN_POSTADRESS = "postadress";
		  public static final String COLUMN_COUNTRY = "country";
		  public static final String COLUMN_CELLPHONE = "cellphone";
		  public static final String COLUMN_EMAIL = "email";
		  
		  
		  //db2
		 // public static final String TABLE_CONTACTS = "contacts";
		 
		  
		  private static final String DATABASE_NAME = "contacttable.db";
		  public static final int DATABASE_VERSION = 2;
		  
		  
		  public DatabaseHelper(Context context) {
				super(context, DATABASE_NAME, null, DATABASE_VERSION);
			}
	  
		  private static final String TABLE_CONTACT_CREATE = "create contact table " 
			      + TABLE_CONTACT
			      + "(" 
			      + COLUMN_ID + " integer primary key autoincrement, " 
			      + COLUMN_FIRSTNAME + " text not null, " 
			      + COLUMN_LASTNAME + " text not null," 
			      + COLUMN_PNMBR + " text not null,"
			      + COLUMN_SEX + " text not null,"
			      + COLUMN_ADRESS + " text not null,"
			      + COLUMN_POSTNMBR + " text not null,"
			      + COLUMN_POSTADRESS + " text not null, "
			      + COLUMN_COUNTRY + " text not null,"
			      + COLUMN_CELLPHONE + " text not null,"
			      + COLUMN_EMAIL + " text not null"
			      +   "); ";

		 
		  
		  @Override
		  public void onCreate(SQLiteDatabase database) {
			    database.execSQL(TABLE_CONTACT_CREATE);
			  }
		  @Override
		  public void onUpgrade(SQLiteDatabase database, int oldVersion,
			      int newVersion) {
			    Log.w(DatabaseSQLite.class.getName(), "Upgrading database from version "
			        + oldVersion + " to " + newVersion
			        + ", which will destroy all old data");
			    database.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACT);
			    onCreate(database);
			  }
		
	/*	
	private static final String DATABASE_NAME = "contacttable.db";
	  private static final int DATABASE_VERSION = 1;

	  public ContactDatabaseHelper(Context context) {
	    super(context, DATABASE_NAME, null, DATABASE_VERSION);
	  }

	  // Method is called during creation of the database
	  @Override
	  public void onCreate(SQLiteDatabase database) {
	    ContactTable.onCreate(database);
	  }

	  // Method is called during an upgrade of the database,
	  // e.g. if you increase the database version
	  @Override
	  public void onUpgrade(SQLiteDatabase database, int oldVersion,
	      int newVersion) {
	    ContactTable.onUpgrade(database, oldVersion, newVersion);
	  }
	  */
	}


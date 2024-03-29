package se.forsmark.visit.database;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;




	public class DatabaseHelper extends SQLiteOpenHelper {
		// Database contact table
		  public static final String TABLE_CONTACT = "contact";
		  public static final String COLUMN_CONTACT_ID = "_id";
		  public static final String COLUMN_CONTACT_FIRSTNAME = "firstname";
		  public static final String COLUMN_CONTACT_LASTNAME = "lastname";
		  public static final String COLUMN_CONTACT_PNMBR = "pnmbr";
		  public static final String COLUMN_CONTACT_SEX = "sex";
		  public static final String COLUMN_CONTACT_ADRESS = "adress";
		  public static final String COLUMN_CONTACT_POSTNMBR = "postnmbr";
		  public static final String COLUMN_CONTACT_POSTADRESS = "postadress";
		  public static final String COLUMN_CONTACT_COUNTRY = "country";
		  public static final String COLUMN_CONTACT_CELLPHONE = "cellphone";
		  public static final String COLUMN_CONTACT_EMAIL = "email";
		  public static final String COLUMN_CONTACT_NOSFR = "nosfr";
		  
		  
		// Database attendants table
		  public static final String TABLE_ATTENDANTS = "attendants";
		  public static final String COLUMN_ATTENDANTS_ID = "_id";
		  public static final String COLUMN_ATTENDANTS_FIRSTNAME = "firstname";
		  public static final String COLUMN_ATTENDANTS_LASTNAME = "lastname";
		  public static final String COLUMN_ATTENDANTS_PNMBR = "pnmbr";
		  public static final String COLUMN_ATTENDANTS_SEX = "sex";
		  public static final String COLUMN_ATTENDANTS_NOSFR = "nosfr";
		  public static final String COLUMN_ATTENDANTS_BOOKINGID = "bookingId";
		
		// Database booking table
		  public static final String TABLE_BOOKING = "booking";
		  public static final String COLUMN_BOOKING_ID = "_id";
		  public static final String COLUMN_BOOKING_DATE = "date";
		  public static final String COLUMN_BOOKING_CONTACT_ID = "bookingContactId";
		  public static final String COLUMN_BOOKING_BOOKED= "booked";  // 1 if booking has pulled through. Else 0 
		  
		  //db2
		 // public static final String TABLE_CONTACTS = "contacts";
		 
		  
		  private static final String DATABASE_NAME = "forsmarktable.db";
		  public static final int DATABASE_VERSION = 12;
		  
		  
		  public DatabaseHelper(Context context) {
				super(context, DATABASE_NAME, null, DATABASE_VERSION);
			}
	  
		  private static final String TABLE_CONTACT_CREATE = "create table " 
			      + TABLE_CONTACT
			      + "(" 
			      + COLUMN_CONTACT_ID + " integer primary key autoincrement, " 
			      + COLUMN_CONTACT_FIRSTNAME + " text not null, " 
			      + COLUMN_CONTACT_LASTNAME + " text not null," 
			      + COLUMN_CONTACT_PNMBR + " text not null,"
			      + COLUMN_CONTACT_SEX + " text not null,"
			      + COLUMN_CONTACT_ADRESS + " text not null,"
			      + COLUMN_CONTACT_POSTNMBR + " text not null,"
			      + COLUMN_CONTACT_POSTADRESS + " text not null, "
			      + COLUMN_CONTACT_COUNTRY + " text not null,"
			      + COLUMN_CONTACT_CELLPHONE + " text not null,"
			      + COLUMN_CONTACT_EMAIL + " text not null,"
			      + COLUMN_CONTACT_NOSFR + " Integer not null"
			      +   "); ";

		  private static final String TABLE_ATTENDANTS_CREATE = "create table " 
			      + TABLE_ATTENDANTS
			      + "(" 
			      + COLUMN_ATTENDANTS_ID + " integer primary key autoincrement, " 
			      + COLUMN_ATTENDANTS_FIRSTNAME + " text, " 
			      + COLUMN_CONTACT_LASTNAME + " text, " 
			      + COLUMN_CONTACT_PNMBR + " text, "
			      + COLUMN_CONTACT_SEX + " text, "
			      + COLUMN_ATTENDANTS_NOSFR + " integer, "
			      + COLUMN_ATTENDANTS_BOOKINGID + " text not null"
			      +   "); ";
		  
		  private static final String TABLE_BOOKING_CREATE = "create table " 
			      + TABLE_BOOKING
			      + "(" 
			      + COLUMN_BOOKING_ID + " text not null, " 
			      + COLUMN_BOOKING_DATE + " text not null, "
			      + COLUMN_BOOKING_CONTACT_ID + " integer, "
			      + COLUMN_BOOKING_BOOKED + " integer" // set as 0 as long as the booking has not been confirmed.
			      +   "); ";
		  
		  @Override
		  public void onCreate(SQLiteDatabase database) {
			    database.execSQL(TABLE_CONTACT_CREATE);
			    database.execSQL(TABLE_ATTENDANTS_CREATE);
			    database.execSQL(TABLE_BOOKING_CREATE);
			  }
		  @Override
		  public void onUpgrade(SQLiteDatabase database, int oldVersion,
			      int newVersion) {
			    Log.w(DatabaseSQLite.class.getName(), "Upgrading database from version "
			        + oldVersion + " to " + newVersion
			        + ", which will destroy all old data");
			    database.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACT);
			    database.execSQL("DROP TABLE IF EXISTS " + TABLE_ATTENDANTS);
			    database.execSQL("DROP TABLE IF EXISTS " + TABLE_BOOKING);
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


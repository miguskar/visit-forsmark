package se.forsmark.visit.database;

import android.database.Cursor;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.SQLException;

public class DatabaseSQLite {

	private SQLiteDatabase database;
	private DatabaseHelper dbHelper;

	// ---------------------------------------------------------------------------------

	public DatabaseSQLite(Context context) {
		dbHelper = new DatabaseHelper(context);
	}

	// ---------------------------------------------------------------------------------

	public void open() throws SQLException { // not sure about the exception?
		database = dbHelper.getWritableDatabase();
	}

	// ---------------------------------------------------------------------------------

	public void close() {
		dbHelper.close();
	}

	// ---------------------------------------------------------------------------------

	/*
	 * public boolean checkIfUpgraded(){ return dbHelper.checkIfUpgraded(); }
	 */
	// ---------------------------------------------------------------------------------

	public int getLatestContactId() {
		try {
			Cursor c = database.rawQuery("SELECT MAX("
					+ DatabaseHelper.COLUMN_CONTACT_ID + ") FROM "
					+ DatabaseHelper.TABLE_CONTACT, null);
			if (c.moveToFirst()) {
				return c.getInt(c.getColumnIndex("_id"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	public Cursor getLatestContactInfo() {
		Cursor c = null;
		try {
			c = database.rawQuery("SELECT * FROM " + DatabaseHelper.TABLE_CONTACT + " ORDER BY _id DESC LIMIT 1;", null);
			return c;
		}catch (SQLException ex) {
			//TODO Handle exception
			throw ex;
		}
	}

	public void addContact(String fname, String lname, String pnbr,
			String sex, String adress, String postnmbr, String padress,
			String country, String cphone, String email) {
		try {
			ContentValues values = new ContentValues();
			values.put(DatabaseHelper.COLUMN_CONTACT_FIRSTNAME, fname);
			values.put(DatabaseHelper.COLUMN_CONTACT_LASTNAME, lname);
			values.put(DatabaseHelper.COLUMN_CONTACT_PNMBR, pnbr);
			values.put(DatabaseHelper.COLUMN_CONTACT_SEX, sex);
			values.put(DatabaseHelper.COLUMN_CONTACT_ADRESS, adress);
			values.put(DatabaseHelper.COLUMN_CONTACT_POSTNMBR, postnmbr);
			values.put(DatabaseHelper.COLUMN_CONTACT_POSTADRESS, padress);
			values.put(DatabaseHelper.COLUMN_CONTACT_COUNTRY, country);
			values.put(DatabaseHelper.COLUMN_CONTACT_CELLPHONE, cphone);
			values.put(DatabaseHelper.COLUMN_CONTACT_EMAIL, email);

			database.insert(DatabaseHelper.TABLE_CONTACT, null, values);

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void updateContact(int id, String fname, String lname, String pnbr,
			String sex, String adress, String postnmbr, String padress,
			String country, String cphone, String email) {
		try {
			ContentValues values = new ContentValues();
			values.put(DatabaseHelper.COLUMN_CONTACT_FIRSTNAME, fname);
			values.put(DatabaseHelper.COLUMN_CONTACT_LASTNAME, lname);
			values.put(DatabaseHelper.COLUMN_CONTACT_PNMBR, pnbr);
			values.put(DatabaseHelper.COLUMN_CONTACT_SEX, sex);
			values.put(DatabaseHelper.COLUMN_CONTACT_ADRESS, adress);
			values.put(DatabaseHelper.COLUMN_CONTACT_POSTNMBR, postnmbr);
			values.put(DatabaseHelper.COLUMN_CONTACT_POSTADRESS, padress);
			values.put(DatabaseHelper.COLUMN_CONTACT_COUNTRY, country);
			values.put(DatabaseHelper.COLUMN_CONTACT_CELLPHONE, cphone);
			values.put(DatabaseHelper.COLUMN_CONTACT_EMAIL, email);
			String[] ids = {String.valueOf(id)};
			
			database.update(DatabaseHelper.TABLE_CONTACT, values, "WHERE _id = ?", ids);

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 * @param fname first name
	 * @param lname last name
	 * @param pnbr SSN
	 * @param sex
	 * @param sfr 
	 * @param bookingId
	 * @return ROWID or -1 if unsuccessful 
	 */
	public int addAttendant(String fname, String lname, String pnbr, String sex, int sfr, String bookingId) {
		int id = -1;
		try {
			ContentValues v = new ContentValues();
			v.put(DatabaseHelper.COLUMN_ATTENDANTS_FIRSTNAME, fname);
			v.put(DatabaseHelper.COLUMN_ATTENDANTS_LASTNAME, lname);
			v.put(DatabaseHelper.COLUMN_ATTENDANTS_PNMBR, pnbr);
			v.put(DatabaseHelper.COLUMN_ATTENDANTS_SEX, sex);
			v.put(DatabaseHelper.COLUMN_ATTENDANTS_SFR, sfr);
			v.put(DatabaseHelper.COLUMN_ATTENDANTS_BOOKINGID, bookingId);
			
			id = (int) database.insert(DatabaseHelper.TABLE_ATTENDANTS, null, v);
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
		return id;
	}
	
	/**
	 * @param id row id
	 * @param fname first name
	 * @param lname last name
	 * @param pnbr SSN
	 * @param sex
	 * @param sfr 
	 * @param bookingId
	 * 
	 */
	public void updateAttendant(int id, String fname, String lname, String pnbr, String sex, int sfr, String bookingId) {
		try {
			ContentValues v = new ContentValues();
			v.put(DatabaseHelper.COLUMN_ATTENDANTS_FIRSTNAME, fname);
			v.put(DatabaseHelper.COLUMN_ATTENDANTS_LASTNAME, lname);
			v.put(DatabaseHelper.COLUMN_ATTENDANTS_PNMBR, pnbr);
			v.put(DatabaseHelper.COLUMN_ATTENDANTS_SEX, sex);
			v.put(DatabaseHelper.COLUMN_ATTENDANTS_SFR, sfr);
			v.put(DatabaseHelper.COLUMN_ATTENDANTS_BOOKINGID, bookingId);
			String[] ids = {String.valueOf(id)};
			
			database.update(DatabaseHelper.TABLE_ATTENDANTS, v, "_id = ?", ids);
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
	}

	// ---------------------------------------------------------------------------------

	public String getFirstname() {
		try {
			Cursor cursor = database.rawQuery("SELECT "
					+ DatabaseHelper.COLUMN_CONTACT_FIRSTNAME + " FROM "
					+ DatabaseHelper.TABLE_CONTACT, null);

			String fn = null;

			if (cursor.moveToFirst()) {
				fn = cursor.getString(cursor
						.getColumnIndex(DatabaseHelper.COLUMN_CONTACT_FIRSTNAME));
			}
			return fn;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	// ---------------------------------------------------------------------------------

	public String getLastname() {
		try {
			Cursor cursor = database.rawQuery("SELECT "
					+ DatabaseHelper.COLUMN_CONTACT_LASTNAME + " FROM "
					+ DatabaseHelper.TABLE_CONTACT, null);

			String ln = null;

			if (cursor.moveToFirst()) {
				ln = cursor.getString(cursor
						.getColumnIndex(DatabaseHelper.COLUMN_CONTACT_LASTNAME));
			}
			return ln;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	// ---------------------------------------------------------------------------------

	public String getPnmbr() {
		try {
			Cursor cursor = database.rawQuery("SELECT "
					+ DatabaseHelper.COLUMN_CONTACT_PNMBR + " FROM "
					+ DatabaseHelper.TABLE_CONTACT, null);

			String ln = null;

			if (cursor.moveToFirst()) {
				ln = cursor.getString(cursor
						.getColumnIndex(DatabaseHelper.COLUMN_CONTACT_PNMBR));
			}
			return ln;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	// ---------------------------------------------------------------------------------

	public String getSex() {
		try {
			Cursor cursor = database.rawQuery("SELECT "
					+ DatabaseHelper.COLUMN_CONTACT_SEX + " FROM "
					+ DatabaseHelper.TABLE_CONTACT, null);

			String ln = null;

			if (cursor.moveToFirst()) {
				ln = cursor.getString(cursor
						.getColumnIndex(DatabaseHelper.COLUMN_CONTACT_SEX));
			}
			return ln;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	// ---------------------------------------------------------------------------------

	public String getAdress() {
		try {
			Cursor cursor = database.rawQuery("SELECT "
					+ DatabaseHelper.COLUMN_CONTACT_ADRESS + " FROM "
					+ DatabaseHelper.TABLE_CONTACT, null);

			String ln = null;

			if (cursor.moveToFirst()) {
				ln = cursor.getString(cursor
						.getColumnIndex(DatabaseHelper.COLUMN_CONTACT_ADRESS));
			}
			return ln;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	// ---------------------------------------------------------------------------------

	public String getPostnr() {
		try {
			Cursor cursor = database.rawQuery("SELECT "
					+ DatabaseHelper.COLUMN_CONTACT_POSTNMBR + " FROM "
					+ DatabaseHelper.TABLE_CONTACT, null);

			String ln = null;

			if (cursor.moveToFirst()) {
				ln = cursor.getString(cursor
						.getColumnIndex(DatabaseHelper.COLUMN_CONTACT_POSTNMBR));
			}
			return ln;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	// ---------------------------------------------------------------------------------

	public String getPostAdress() {
		try {
			Cursor cursor = database.rawQuery("SELECT "
					+ DatabaseHelper.COLUMN_CONTACT_POSTADRESS + " FROM "
					+ DatabaseHelper.TABLE_CONTACT, null);

			String ln = null;

			if (cursor.moveToFirst()) {
				ln = cursor.getString(cursor
						.getColumnIndex(DatabaseHelper.COLUMN_CONTACT_POSTADRESS));
			}
			return ln;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	// ---------------------------------------------------------------------------------

	public String getCountry() {
		try {
			Cursor cursor = database.rawQuery("SELECT "
					+ DatabaseHelper.COLUMN_CONTACT_COUNTRY + " FROM "
					+ DatabaseHelper.TABLE_CONTACT, null);

			String ln = null;

			if (cursor.moveToFirst()) {
				ln = cursor.getString(cursor
						.getColumnIndex(DatabaseHelper.COLUMN_CONTACT_COUNTRY));
			}
			return ln;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	// ---------------------------------------------------------------------------------

	public String getCellphone() {
		try {
			Cursor cursor = database.rawQuery("SELECT "
					+ DatabaseHelper.COLUMN_CONTACT_CELLPHONE + " FROM "
					+ DatabaseHelper.TABLE_CONTACT, null);

			String ln = null;

			if (cursor.moveToFirst()) {
				ln = cursor.getString(cursor
						.getColumnIndex(DatabaseHelper.COLUMN_CONTACT_CELLPHONE));
			}
			return ln;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	// ---------------------------------------------------------------------------------
	/*
	 * 
	 * // Database table public static final String TABLE_CONTACT = "contact";
	 * public static final String COLUMN_ID = "_id"; public static final String
	 * COLUMN_FIRSTNAME = "firstname"; public static final String
	 * COLUMN_LASTNAME = "lastname"; public static final String COLUMN_PNMBR =
	 * "pnmbr"; public static final String COLUMN_SEX = "sex"; public static
	 * final String COLUMN_ADRESS = "adress"; public static final String
	 * COLUMN_POSTNMBR = "postnmbr"; public static final String
	 * COLUMN_POSTADRESS = "postadress"; public static final String
	 * COLUMN_COUNTRY = "country"; public static final String COLUMN_CELLPHONE =
	 * "cellphone"; public static final String COLUMN_EMAIL = "email";
	 * 
	 * 
	 * // Database creation SQL statement //TODO private static final String
	 * DATABASE_CREATE = "create table " + TABLE_CONTACT + "(" + COLUMN_ID +
	 * " integer primary key autoincrement, " + COLUMN_FIRSTNAME +
	 * " text not null, " + COLUMN_LASTNAME + " text not null," + COLUMN_PNMBR +
	 * "text not null," + COLUMN_SEX + "text not null," + COLUMN_ADRESS +
	 * "text not null," + COLUMN_POSTNMBR + "text not null," + COLUMN_POSTADRESS
	 * + "next not null, " + COLUMN_COUNTRY + "next not null," +
	 * COLUMN_CELLPHONE + "next not null," + COLUMN_EMAIL + "next not null,"
	 * 
	 * + " text not null" + "); ";
	 * 
	 * public static void onCreate(SQLiteDatabase database) {
	 * database.execSQL(DATABASE_CREATE); }
	 * 
	 * public static void onUpgrade(SQLiteDatabase database, int oldVersion, int
	 * newVersion) { Log.w(ContactTable.class.getName(),
	 * "Upgrading database from version " + oldVersion + " to " + newVersion +
	 * ", which will destroy all old data");
	 * database.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACT);
	 * onCreate(database); }
	 */
}
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

	public int getLatestContact() {
		try {
			Cursor c = database.rawQuery("SELECT MAX("
					+ DatabaseHelper.COLUMN_ID + ") FROM "
					+ DatabaseHelper.TABLE_CONTACT, null);
			if (c.moveToFirst()) {
				return c.getInt(c.getColumnIndex("_id"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}

	public void addContact(String fname, String lname, String pnmbr,
			String sex, String adress, String postnmbr, String padress,
			String country, String cphone, String email) {
		try {
			ContentValues values = new ContentValues();
			values.put(DatabaseHelper.COLUMN_FIRSTNAME, fname);
			values.put(DatabaseHelper.COLUMN_LASTNAME, lname);
			values.put(DatabaseHelper.COLUMN_PNMBR, pnmbr);
			values.put(DatabaseHelper.COLUMN_SEX, sex);
			values.put(DatabaseHelper.COLUMN_ADRESS, adress);
			values.put(DatabaseHelper.COLUMN_POSTNMBR, postnmbr);
			values.put(DatabaseHelper.COLUMN_POSTADRESS, padress);
			values.put(DatabaseHelper.COLUMN_COUNTRY, country);
			values.put(DatabaseHelper.COLUMN_CELLPHONE, cphone);
			values.put(DatabaseHelper.COLUMN_EMAIL, email);

			database.insert(DatabaseHelper.TABLE_CONTACT, null, values);

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	// ---------------------------------------------------------------------------------

	public String getFirstname() {
		try {
			Cursor cursor = database.rawQuery("SELECT "
					+ DatabaseHelper.COLUMN_FIRSTNAME + " FROM "
					+ DatabaseHelper.TABLE_CONTACT, null);

			String fn = null;

			if (cursor.moveToFirst()) {
				fn = cursor.getString(cursor
						.getColumnIndex(DatabaseHelper.COLUMN_FIRSTNAME));
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
					+ DatabaseHelper.COLUMN_LASTNAME + " FROM "
					+ DatabaseHelper.TABLE_CONTACT, null);

			String ln = null;

			if (cursor.moveToFirst()) {
				ln = cursor.getString(cursor
						.getColumnIndex(DatabaseHelper.COLUMN_LASTNAME));
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
					+ DatabaseHelper.COLUMN_PNMBR + " FROM "
					+ DatabaseHelper.TABLE_CONTACT, null);

			String ln = null;

			if (cursor.moveToFirst()) {
				ln = cursor.getString(cursor
						.getColumnIndex(DatabaseHelper.COLUMN_PNMBR));
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
					+ DatabaseHelper.COLUMN_SEX + " FROM "
					+ DatabaseHelper.TABLE_CONTACT, null);

			String ln = null;

			if (cursor.moveToFirst()) {
				ln = cursor.getString(cursor
						.getColumnIndex(DatabaseHelper.COLUMN_SEX));
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
					+ DatabaseHelper.COLUMN_ADRESS + " FROM "
					+ DatabaseHelper.TABLE_CONTACT, null);

			String ln = null;

			if (cursor.moveToFirst()) {
				ln = cursor.getString(cursor
						.getColumnIndex(DatabaseHelper.COLUMN_ADRESS));
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
					+ DatabaseHelper.COLUMN_POSTNMBR + " FROM "
					+ DatabaseHelper.TABLE_CONTACT, null);

			String ln = null;

			if (cursor.moveToFirst()) {
				ln = cursor.getString(cursor
						.getColumnIndex(DatabaseHelper.COLUMN_POSTNMBR));
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
					+ DatabaseHelper.COLUMN_POSTADRESS + " FROM "
					+ DatabaseHelper.TABLE_CONTACT, null);

			String ln = null;

			if (cursor.moveToFirst()) {
				ln = cursor.getString(cursor
						.getColumnIndex(DatabaseHelper.COLUMN_POSTADRESS));
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
					+ DatabaseHelper.COLUMN_COUNTRY + " FROM "
					+ DatabaseHelper.TABLE_CONTACT, null);

			String ln = null;

			if (cursor.moveToFirst()) {
				ln = cursor.getString(cursor
						.getColumnIndex(DatabaseHelper.COLUMN_COUNTRY));
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
					+ DatabaseHelper.COLUMN_CELLPHONE + " FROM "
					+ DatabaseHelper.TABLE_CONTACT, null);

			String ln = null;

			if (cursor.moveToFirst()) {
				ln = cursor.getString(cursor
						.getColumnIndex(DatabaseHelper.COLUMN_CELLPHONE));
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

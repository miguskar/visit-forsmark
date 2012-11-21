package se.forsmark.visit.database;

import java.util.ArrayList;

import android.database.Cursor;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.SQLException;
import android.util.Log;

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
			Cursor c = database.rawQuery("SELECT MAX(" + DatabaseHelper.COLUMN_CONTACT_ID + ") AS id FROM "
					+ DatabaseHelper.TABLE_CONTACT, null);
			if (c.moveToFirst()) {
				int id = c.getInt(c.getColumnIndex("id"));
				c.close();
				return id;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}

	public String getContactName(int id) {
		Cursor c = null;
		String[] ids = { String.valueOf(id) };
		try {
			c = database.rawQuery("SELECT " + DatabaseHelper.COLUMN_CONTACT_FIRSTNAME + ", "
					+ DatabaseHelper.COLUMN_CONTACT_LASTNAME + " FROM " + DatabaseHelper.TABLE_CONTACT
					+ " WHERE _id = ?", ids);
			c.moveToFirst();
			String s = String.format("%s %s", c.getString(c.getColumnIndex(DatabaseHelper.COLUMN_CONTACT_FIRSTNAME)),
					c.getString(c.getColumnIndex(DatabaseHelper.COLUMN_CONTACT_LASTNAME)));
			c.close();
			return s;
		} catch (SQLException ex) {
			throw ex;
		}
	}

	public Cursor getLatestContactInfo() {
		Cursor c = null;
		try {
			c = database
					.rawQuery("SELECT * FROM " + DatabaseHelper.TABLE_CONTACT + " ORDER BY _id DESC LIMIT 1;", null);
			return c;
		} catch (SQLException ex) {
			throw ex;
		}
	}

	// TODO change this method to something else derp
	public Cursor getContactInfo(String bookingId) {
		Cursor c = null;
		
		try {
			Log.v("derp", bookingId);
			c = database.rawQuery("SELECT " + DatabaseHelper.COLUMN_BOOKING_CONTACT_ID 
					+ " FROM " + DatabaseHelper.TABLE_BOOKING + " WHERE "
					+ DatabaseHelper.COLUMN_BOOKING_ID + " = '" + bookingId + "'", null);
			
			c.moveToFirst();
			String id = String.valueOf(c.getInt(c.getColumnIndex(DatabaseHelper.COLUMN_BOOKING_CONTACT_ID)));
			
			c = database.rawQuery("SELECT * FROM " + DatabaseHelper.TABLE_CONTACT + " WHERE "
					+ DatabaseHelper.COLUMN_CONTACT_ID + " = '" + id + "';", null);
			return c;
		} catch (SQLException ex) {
			throw ex;
		}
	}

	public void addBooking(String id, String date) {
		try {
			ContentValues values = new ContentValues();
			values.put(DatabaseHelper.COLUMN_BOOKING_ID, id);
			values.put(DatabaseHelper.COLUMN_BOOKING_DATE, date);
			values.put(DatabaseHelper.COLUMN_BOOKING_CONTACT_ID, 0);
			values.put(DatabaseHelper.COLUMN_BOOKING_BOOKED, 0);
			
			database.insert(DatabaseHelper.TABLE_BOOKING, null, values);

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void setBookingBooked(String bookingId){
		try {
			ContentValues values = new ContentValues();
			values.put(DatabaseHelper.COLUMN_BOOKING_BOOKED, 1);
			String[] id = {bookingId};

			database.update(DatabaseHelper.TABLE_BOOKING, values, DatabaseHelper.COLUMN_BOOKING_ID + " = ?", id);

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void updateBookingContactId(String bookingId,int conId) { // FINT NAMN
		try {
			ContentValues values = new ContentValues();
			values.put(DatabaseHelper.COLUMN_BOOKING_CONTACT_ID, conId);
			String[] ids = {bookingId};

			database.update(DatabaseHelper.TABLE_BOOKING, values, DatabaseHelper.COLUMN_BOOKING_ID + " = ?", ids);

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	// TODO fixa så att vi inte får duplicates i databasen
	// http://stackoverflow.com/questions/3634984/insert-if-not-exists-else-update
	public void addContact(String fname, String lname, String pnbr, String sex, String adress, String postnmbr,
			String padress, String country, String cphone, String email, int nosfr) {
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
			values.put(DatabaseHelper.COLUMN_CONTACT_NOSFR, nosfr);

			database.insert(DatabaseHelper.TABLE_CONTACT, null, values);

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void updateContact(int id, String fname, String lname, String pnbr, String sex, String adress,
			String postnmbr, String padress, String country, String cphone, String email, int nosfr) {
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
			values.put(DatabaseHelper.COLUMN_CONTACT_NOSFR, nosfr);
			String[] ids = { String.valueOf(id) };

			database.update(DatabaseHelper.TABLE_CONTACT, values, DatabaseHelper.COLUMN_CONTACT_ID + " = ?", ids);

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @param fname
	 *            first name
	 * @param lname
	 *            last name
	 * @param pnbr
	 *            SSN
	 * @param sex
	 * @param sfr
	 * @param bookingId
	 * @return ROWID or -1 if unsuccessful
	 */
	public int addAttendant(String fname, String lname, String pnbr, String sex, int nosfr, String bookingId) {
		int id = -1;
		try {
			ContentValues v = new ContentValues();
			v.put(DatabaseHelper.COLUMN_ATTENDANTS_FIRSTNAME, fname);
			v.put(DatabaseHelper.COLUMN_ATTENDANTS_LASTNAME, lname);
			v.put(DatabaseHelper.COLUMN_ATTENDANTS_PNMBR, pnbr);
			v.put(DatabaseHelper.COLUMN_ATTENDANTS_SEX, sex);
			v.put(DatabaseHelper.COLUMN_ATTENDANTS_NOSFR, nosfr);
			v.put(DatabaseHelper.COLUMN_ATTENDANTS_BOOKINGID, bookingId);

			id = (int) database.insert(DatabaseHelper.TABLE_ATTENDANTS, null, v);
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
		return id;
	}

	public void deleteAttendant(int id) {
		String[] ids = { String.valueOf(id) };
		try {
			database.delete(DatabaseHelper.TABLE_ATTENDANTS, DatabaseHelper.COLUMN_ATTENDANTS_ID + " = ?", ids);
		} catch (SQLException ex) {
			ex.printStackTrace();
		}

	}

	public void deleteBooking(String bookingId, int contactId) {

		String[] idb = { bookingId };
		String[] idc = { String.valueOf(contactId) };
		try {
			database.delete(DatabaseHelper.TABLE_ATTENDANTS, DatabaseHelper.COLUMN_ATTENDANTS_BOOKINGID + " = ?", idb);
			database.delete(DatabaseHelper.TABLE_BOOKING, DatabaseHelper.COLUMN_CONTACT_ID + " = ?", idc);
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	/**
	 * @param id
	 *            row id
	 * @param fname
	 *            first name
	 * @param lname
	 *            last name
	 * @param pnbr
	 *            SSN
	 * @param sex
	 * @param sfr
	 * @param bookingId
	 * 
	 */
	public void updateAttendant(int id, String fname, String lname, String pnbr, String sex, int nosfr/*
																									 * ,
																									 * String
																									 * bookingId
																									 */) {
		try {
			ContentValues v = new ContentValues();
			v.put(DatabaseHelper.COLUMN_ATTENDANTS_FIRSTNAME, fname);
			v.put(DatabaseHelper.COLUMN_ATTENDANTS_LASTNAME, lname);
			v.put(DatabaseHelper.COLUMN_ATTENDANTS_PNMBR, pnbr);
			v.put(DatabaseHelper.COLUMN_ATTENDANTS_SEX, sex);
			v.put(DatabaseHelper.COLUMN_ATTENDANTS_NOSFR, nosfr);
			// v.put(DatabaseHelper.COLUMN_ATTENDANTS_BOOKINGID, bookingId);
			String[] ids = { String.valueOf(id) };

			database.update(DatabaseHelper.TABLE_ATTENDANTS, v, DatabaseHelper.COLUMN_ATTENDANTS_ID + " = ?", ids);
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
	}

	public String getBookingDate(String bookingId) {
		String s = "";
		String[] ids = { bookingId };
		try {
			Cursor c = database.rawQuery("SELECT " + DatabaseHelper.COLUMN_BOOKING_DATE + " FROM "
					+ DatabaseHelper.TABLE_BOOKING + " WHERE " + DatabaseHelper.COLUMN_BOOKING_ID + " = ?", ids);
			if (c.moveToFirst()) {
				s = c.getString(c.getColumnIndex(DatabaseHelper.COLUMN_BOOKING_DATE));
			}
			c.close();
			return s;
		} catch (SQLException ex) {
			throw ex;
		}
	}
	
	public Cursor getAllMyBookings(){
		Cursor c = null;
		String[] book = { "1" };
		try{
			c=database.query(DatabaseHelper.TABLE_BOOKING, null, "booked=?", book, null, null, DatabaseHelper.COLUMN_BOOKING_DATE);
			return c;
		}
		catch(SQLException ex) {
			throw ex;
		}
		
		
	}

	public Cursor getAttendantContactInfo(int id) {
		Cursor c = null;
		String[] ids = { String.valueOf(id) };
		try {
			c = database.rawQuery("SELECT * FROM " + DatabaseHelper.TABLE_ATTENDANTS + " WHERE "
					+ DatabaseHelper.COLUMN_ATTENDANTS_ID + " = ?", ids);
			return c;
		} catch (SQLException ex) {
			throw ex;
		}
	}

	public String getAttendantName(int id) {
		Cursor c = null;
		String[] ids = { String.valueOf(id) };
		try {
			c = database.rawQuery("SELECT " + DatabaseHelper.COLUMN_ATTENDANTS_FIRSTNAME + ", "
					+ DatabaseHelper.COLUMN_ATTENDANTS_LASTNAME + " FROM " + DatabaseHelper.TABLE_ATTENDANTS
					+ " WHERE _ID = ?", ids);
			c.moveToFirst();
			String s = String.format("%s %s", c.getString(c.getColumnIndex(DatabaseHelper.COLUMN_ATTENDANTS_FIRSTNAME)),
					c.getString(c.getColumnIndex(DatabaseHelper.COLUMN_ATTENDANTS_LASTNAME)));
			c.close();
			return s;
		} catch (SQLException ex) {
			throw ex;
		}
	}

	public ArrayList<Integer> getAttendantIdsFromBookingId(String bookingId) {
		String[] ids = { bookingId };
		ArrayList<Integer> r = new ArrayList<Integer>();
		Cursor c = null;
		try {
			c = database.rawQuery("SELECT _id FROM " + DatabaseHelper.TABLE_ATTENDANTS + " WHERE "
					+ DatabaseHelper.COLUMN_ATTENDANTS_BOOKINGID + " = ?", ids);
			c.moveToFirst();
			while (!c.isAfterLast()) {
				r.add(c.getInt(c.getColumnIndex("_id")));
				c.moveToNext();
			}
			c.close();
		} catch (SQLException ex) {
			throw ex;
		}
		return r;
	}
}

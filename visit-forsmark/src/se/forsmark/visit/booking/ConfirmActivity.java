package se.forsmark.visit.booking;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import se.forsmark.visit.R;
import se.forsmark.visit.database.DatabaseHelper;
import se.forsmark.visit.database.DatabaseSQLite;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ConfirmActivity extends Activity {
	private String bookingId;
	private int contactId;
	private int progressBar;
	private boolean disable = false;
	private final int EDIT_CONTACT = 1, EDIT_ATTENDANT = 2;
	private int seatsLeft, eventId;
	private OnClickListener ocl;
	private AttendantHandler ah;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.confirmview);

		initialize();

	}

	private void initialize() {

		// Set Title
		TextView tv = (TextView) findViewById(R.id.border_title);
		tv.setText(R.string.ConfirmationTitle);

		// Create ONE click listener
		ocl = new OnClickListener() {
			public void onClick(View v) {
				if(!disable){
					editButton(v);
				}
			}
		};

		Bundle extras = getIntent().getExtras();
		bookingId = extras.getString("bookingId");
		contactId = extras.getInt("contactId");
		eventId = extras.getInt("eventId");
		progressBar = extras.getInt("progressBar");
		Log.v("eventId", eventId + "");

		// Unhide progress and set background
		View v = findViewById(R.id.border_progress);
		if (progressBar == 3)
			v.setBackgroundResource(R.drawable.border_step_three);
		else
			v.setBackgroundResource(R.drawable.border_step_two_two);
		v.setVisibility(View.VISIBLE);

		// GET SEATS
		ah = new AttendantHandler();
		ah.execute(AttendantHandler.GET_SEATS);

		DatabaseSQLite db = new DatabaseSQLite(getApplicationContext());
		db.open();
		// Get contact name
		String contactName = db.getContactName(contactId);

		Button b = (Button) findViewById(R.id.ButtonConfirmContactPerson);
		b.setText(contactName);

		ArrayList<Integer> al = db.getAttendantIdsFromBookingId(bookingId);
		LinearLayout l = (LinearLayout) findViewById(R.id.confirmformLayout);

		// Create buttons
		for (int id : al) {
			b = new Button(this);
			b.setId(id);
			b.setGravity(Gravity.LEFT);
			b.setTextAppearance(getApplicationContext(), R.style.CodeFont);
			b.setTextColor(Color.WHITE);
			String name = db.getAttendantName(id);
			if (!name.equals(" ") || name == null) {
				b.setText(db.getAttendantName(id));
			} else {
				b.setText(getString(R.string.attendantButtonText));
			}
			b.setBackgroundResource(R.drawable.editbutton);
			b.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.iconpencil1, 0);
			b.setOnClickListener(ocl);
			l.addView(b, l.getChildCount() - 3);

		}

		// Date and time
		TextView dateTv = (TextView) findViewById(R.id.TextViewconfirmDate);
		TextView timeTv = (TextView) findViewById(R.id.TextViewconfirmTime);
		String date = db.getBookingDate(bookingId);
		dateTv.setText(date.subSequence(0, 10));
		timeTv.setText(String.format("%s - %s", date.substring(10, 16),
				date.substring(17, date.length())));
		db.close();

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		disable(false);
		if (resultCode == RESULT_OK) {
			Bundle extras = data.getExtras();
			String text = extras.getString("displayName");
			if (requestCode == EDIT_CONTACT) {
				Button b = (Button) findViewById(R.id.ButtonConfirmContactPerson);
				b.setText(text);
			} else if (requestCode == EDIT_ATTENDANT) {
				int id = extras.getInt("attendantId");
				if (extras.getBoolean("edit") || extras.getBoolean("new")) {
					Button b = (Button) findViewById(id);
					b.setText(text);
				} else {
					ah = new AttendantHandler();
					ah.execute(AttendantHandler.DELETE_ATTENDANT, id);
				}

			}
		} else if (resultCode == RESULT_CANCELED && data != null) {
			Bundle extras = data.getExtras();
			if (extras.getBoolean("new")) {
				ah = new AttendantHandler();
				ah.execute(AttendantHandler.DELETE_ATTENDANT,
						extras.getInt("attendantId"));
			}
		}
	}

	public void editButton(View v) {
		disable(true);
		Intent dialog = new Intent(getApplicationContext(),
				EditAttendantDialogActivity.class);
		dialog.putExtra("attendantId", v.getId());
		startActivityForResult(dialog, EDIT_ATTENDANT);

	}

	public void editContact(View v) {
		disable(true);
		Intent i = new Intent(getApplicationContext(),
				EditContactActivity.class);
		i.putExtra("contactId", contactId);
		startActivityForResult(i, EDIT_CONTACT);
	}

	public void addAttendantButton(View v) {
		disable(true);
		ah = new AttendantHandler();
		ah.execute(AttendantHandler.ADD_ATTENDANT);
	}

	public void bottomBackClick(View v) {
		setResult(RESULT_OK);
		finish();
	}

	@Override
	public void onBackPressed() {
		setResult(RESULT_OK);
		finish();
	}

	public void disable(boolean dis) {
		if (dis) {
			disable = dis;
			findViewById(R.id.ButtonConfirmContactPerson).setEnabled(false);
			findViewById(R.id.AddAttendantButton).setEnabled(false);
			findViewById(R.id.bottomBackButton).setEnabled(false);
			findViewById(R.id.bottomCancelButton).setEnabled(false);
			findViewById(R.id.bottomNextButton).setEnabled(false);
		} else {
			disable = dis;
			findViewById(R.id.ButtonConfirmContactPerson).setEnabled(true);
			findViewById(R.id.AddAttendantButton).setEnabled(true);
			findViewById(R.id.bottomBackButton).setEnabled(true);
			findViewById(R.id.bottomCancelButton).setEnabled(true);
			findViewById(R.id.bottomNextButton).setEnabled(true);
		}
	}

	public void bottomCancelClick(View v) {
		disable(true);
		// Dialogruta AVBRYT BOKNING
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.create();
		builder.setTitle(R.string.cancelBookingTitle);
		builder.setMessage(R.string.cancelBookingMessage);
		builder.setPositiveButton(R.string.dialogYes,
				new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {
						setResult(RESULT_CANCELED);
						finish();
					}
				});
		builder.setNegativeButton(R.string.dialogNo,
				new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {
						disable(false);
					}
				});
		builder.show(); // b
	}

	public void bottomNextClick(View v) {
		disable(true);
		// Dialogruta AVBRYT BOKNING
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.create();
		builder.setTitle(R.string.Confrim);
		builder.setMessage(R.string.confirmBookingMessage);
		builder.setPositiveButton(R.string.dialogYes,
				new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {
						ah = new AttendantHandler();
						ah.execute(AttendantHandler.CREATE_BOOKING);
					}
				});
		builder.setNegativeButton(R.string.dialogNo,
				new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {
						disable(false);
					}
				});
		builder.show();
	}

	private boolean isNetworkConnected() {
		getApplicationContext();
		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo ni = cm.getActiveNetworkInfo();
		if (ni == null) {
			// There are no active networks.
			return false;
		} else
			return true;
	}

	private class AttendantHandler extends AsyncTask<Integer, Void, String> {
		private int mode;
		public static final int GET_SEATS = 0;
		public static final int ADD_ATTENDANT = 1;
		public static final int DELETE_ATTENDANT = 2;
		public static final int CREATE_BOOKING = 3;

		@Override
		protected String doInBackground(Integer... params) {
			mode = params[0];
			String s = "-1";

			if (isNetworkConnected()) {
				switch (mode) {
				case GET_SEATS:
					s = getSeatsLeft().toString();
					break;
				case ADD_ATTENDANT:
					getSeatsLeft();
					if (seatsLeft > 0) {
						s = addAttendantToBooking(bookingId);
					} else
						s = "false";
					getSeatsLeft();
					break;
				case DELETE_ATTENDANT:
					if (deleteAttendantFromBooking(bookingId).equals("1")) {
						getSeatsLeft();
						s = params[1].toString(); // ATTENDANT ID
					} else
						s = "FAIL";
					break;
				case CREATE_BOOKING:
					if (validateAttendants()) {
						if (createBooking().equals("true")) {
							sendEmailConfirmation();
							return "true";
						}
						return "false";
					} else {
						Toast.makeText(getApplicationContext(),
								R.string.invalidAttendantsInfo,
								Toast.LENGTH_LONG).show();
						return null;
					}
				}
			}
			return s;
		}

		@Override
		protected void onPostExecute(String result) {

			switch (mode) {
			case GET_SEATS:
				updateSeats();
				break;
			case ADD_ATTENDANT:
				updateSeats();
				attendantAdded(result);
				break;
			case DELETE_ATTENDANT:
				updateSeats();
				attendantDeleted(result);
				break;
			case CREATE_BOOKING:
				bookingCreated(result);
				break;
			}
		}

		private Integer getSeatsLeft() {
			String result = "";
			InputStream is = null;
			try {
				HttpParams httpParams = new BasicHttpParams();
				HttpConnectionParams.setConnectionTimeout(httpParams, 10000);
				HttpConnectionParams.setSoTimeout(httpParams, 10000);
				HttpClient httpclient = new DefaultHttpClient(httpParams);
				HttpPost httppost = new HttpPost(
						getString(R.string.httpRequestUrl));

				List<NameValuePair> pairs = new ArrayList<NameValuePair>();

				pairs.add(new BasicNameValuePair("case", "getSeats"));
				pairs.add(new BasicNameValuePair("id", "" + bookingId));

				httppost.setEntity(new UrlEncodedFormEntity(pairs));

				if (isCancelled())
					return null;
				HttpResponse response = httpclient.execute(httppost);
				HttpEntity entity = response.getEntity();
				is = entity.getContent();
			} catch (Exception e) {
				Log.e("log_tag", "Error in http connection " + e.toString());
			}
			try {
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(is, "iso-8859-1"), 8);
				StringBuilder sb = new StringBuilder();
				String line = null;
				while ((line = reader.readLine()) != null) {
					if (isCancelled())
						return null;
					sb.append(line + "\n");
				}
				is.close();

				result = sb.toString();
				result = result.substring(0, result.length() - 1);
			} catch (StringIndexOutOfBoundsException e) {

			} catch (Exception e) {
				Log.e("log_tag", "Error converting result " + e.toString());
			}
			if (isCancelled())
				return null;
			seatsLeft = Integer.parseInt(result);
			return 0;
		}

		private void updateSeats() {
			TextView tv = (TextView) findViewById(R.id.SeatsLeft);
			if (seatsLeft > 0) {
				findViewById(R.id.AddAttendantButton).setVisibility(
						View.VISIBLE);
				tv.setText(String.format(getString(R.string.ConfirmSeatsLeft),
						seatsLeft));
			} else {
				findViewById(R.id.AddAttendantButton).setVisibility(View.GONE);
				tv.setVisibility(View.GONE);
			}
			Log.e("SEATS UPDATED", "B...");
		}

		private void attendantAdded(String result) {
			if (result.equals("true")) { // add button and add to local db

				DatabaseSQLite db = new DatabaseSQLite(getApplicationContext());
				db.open();
				int id = db.addAttendant("", "", "", "", 0, bookingId);
				db.close();

				Button b = new Button(getApplicationContext());

				Log.v("buttonID", Integer.toString(id));
				b.setId(id);
				b.setGravity(Gravity.LEFT);
				b.setTextAppearance(getApplicationContext(), R.style.CodeFont);
				b.setTextColor(Color.WHITE);
				b.setBackgroundResource(R.drawable.editbutton);
				b.setText(getString(R.string.attendantButtonText));
				b.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.iconpencil1, 0);
				b.setOnClickListener(ocl);
				LinearLayout l = (LinearLayout) findViewById(R.id.confirmformLayout);
				l.addView(b, l.getChildCount() - 3);
				seatsLeft = getSeatsLeft();
				if (seatsLeft > 0) {
					TextView tv2 = (TextView) findViewById(R.id.SeatsLeft);
					tv2.setText(String.format(
							getString(R.string.ConfirmSeatsLeft), seatsLeft));
				}
				Intent i = new Intent(getApplicationContext(),
						EditAttendantActivity.class);
				i.putExtra("attendantId", id);
				i.putExtra("new", true);
				startActivityForResult(i, EDIT_ATTENDANT);
			} else {
				Toast.makeText(getApplicationContext(), "Försök att lägga till deltagare misslyckades", Toast.LENGTH_LONG).show(); 
			}
		}

		private String addAttendantToBooking(String reservationKey) {
			String result = "";
			InputStream is = null;
			// http post
			try {
				HttpParams httpParams = new BasicHttpParams();
				HttpConnectionParams.setConnectionTimeout(httpParams, 10000);
				HttpConnectionParams.setSoTimeout(httpParams, 10000);
				HttpClient httpclient = new DefaultHttpClient(httpParams);
				HttpPost httppost = new HttpPost(
						getString(R.string.httpRequestUrl));

				List<NameValuePair> pairs = new ArrayList<NameValuePair>();

				pairs.add(new BasicNameValuePair("case", "addAttendant"));
				pairs.add(new BasicNameValuePair("resKey", "" + reservationKey));
				pairs.add(new BasicNameValuePair("eventId", "" + eventId));

				httppost.setEntity(new UrlEncodedFormEntity(pairs));

				HttpResponse response = httpclient.execute(httppost);
				HttpEntity entity = response.getEntity();
				is = entity.getContent();
			} catch (Exception e) {
				Log.e("log_tag", "Error in http connection " + e.toString());
			}
			try {
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(is, "iso-8859-1"), 8);
				StringBuilder sb = new StringBuilder();
				String line = null;
				while ((line = reader.readLine()) != null) {
					sb.append(line);
				}
				is.close();

				result = sb.toString().trim();

			} catch (Exception e) {
				Log.e("log_tag", "Error converting result " + e.toString());
			}

			result = result.substring(1, result.length() - 1);
			Log.v("addAttResult", result);
			return result;
		}

		private String deleteAttendantFromBooking(String reservationKey) {
			String result = "";
			InputStream is = null;
			try {
				HttpParams httpParams = new BasicHttpParams();
				HttpConnectionParams.setConnectionTimeout(httpParams, 10000);
				HttpConnectionParams.setSoTimeout(httpParams, 10000);
				HttpClient httpclient = new DefaultHttpClient(httpParams);
				HttpPost httppost = new HttpPost(
						getString(R.string.httpRequestUrl));

				List<NameValuePair> pairs = new ArrayList<NameValuePair>();

				pairs.add(new BasicNameValuePair("case", "deleteAttendant"));
				pairs.add(new BasicNameValuePair("resKey", "" + reservationKey));

				httppost.setEntity(new UrlEncodedFormEntity(pairs));

				HttpResponse response = httpclient.execute(httppost);
				HttpEntity entity = response.getEntity();
				is = entity.getContent();
			} catch (Exception e) {
				Log.e("log_tag", "Error in http connection " + e.toString());
			}
			try {
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(is, "iso-8859-1"), 8);
				StringBuilder sb = new StringBuilder();
				String line = null;
				while ((line = reader.readLine()) != null) {
					sb.append(line);
				}
				is.close();
				result = sb.toString();
			} catch (Exception e) {
				Log.e("log_tag", "Error converting result " + e.toString());
			}
			Log.v("deleteAttResult", result);
			return result;
		}

		private void attendantDeleted(String attendantId) {
			if (!attendantId.equals("FAIL")) {
				int id = Integer.parseInt(attendantId);

				DatabaseSQLite db = new DatabaseSQLite(getApplicationContext());
				db.open();
				db.deleteAttendant(id);
				db.close();
				LinearLayout l = (LinearLayout) findViewById(R.id.confirmformLayout);
				l.removeView(findViewById(id));

			} else {
				Toast.makeText(getApplicationContext(), "Försök att ta bort deltagare misslyckades", Toast.LENGTH_LONG).show();
			}

		}

		private boolean validateAttendants() {
			DatabaseSQLite db = new DatabaseSQLite(getApplicationContext());
			db.open();
			ArrayList<Integer> ids = db.getAttendantIdsFromBookingId(bookingId);
			String name;
			for (int id : ids) {
				name = db.getAttendantName(id);
				if (name.equals(" ")) {
					db.close();
					return false;
				}
			}
			db.close();
			return true;
		}

		private String createBooking() {
			// Create array to contain visitors
			JSONArray visitors = new JSONArray();

			final String FIRST_NAME = "first_name";
			final String LAST_NAME = "last_name";
			final String ADDRESS = "address";
			final String ZIP = "zip";
			final String POSTAREA = "postarea";
			final String COUNTRY = "country";
			final String PHONE = "phone";
			final String MAIL = "mail";
			final String SEX = "sex";
			final String PNR = "pnr";
			final String NO_SFR = "nossr";

			// Get contact info
			DatabaseSQLite db = new DatabaseSQLite(getApplicationContext());
			db.open();
			Cursor c = db.getLatestContactInfo();

			// Create object to contain contact info
			JSONObject visitor = new JSONObject();
			if (c.moveToFirst()) {
				try {
					visitor.put(FIRST_NAME,c.getString(c.getColumnIndex(DatabaseHelper.COLUMN_CONTACT_FIRSTNAME)));
					visitor.put(LAST_NAME,c.getString(c.getColumnIndex(DatabaseHelper.COLUMN_CONTACT_LASTNAME)));
					visitor.put(ADDRESS,c.getString(c.getColumnIndex(DatabaseHelper.COLUMN_CONTACT_ADRESS)));
					visitor.put(ZIP,c.getString(c.getColumnIndex(DatabaseHelper.COLUMN_CONTACT_PNMBR)));
					visitor.put(POSTAREA,c.getString(c.getColumnIndex(DatabaseHelper.COLUMN_CONTACT_POSTADRESS)));
					visitor.put(COUNTRY,c.getString(c.getColumnIndex(DatabaseHelper.COLUMN_CONTACT_COUNTRY)));
					visitor.put(PHONE,c.getString(c.getColumnIndex(DatabaseHelper.COLUMN_CONTACT_CELLPHONE)));
					visitor.put(MAIL,c.getString(c.getColumnIndex(DatabaseHelper.COLUMN_CONTACT_EMAIL)));
					visitor.put(SEX, c.getString(c.getColumnIndex(DatabaseHelper.COLUMN_CONTACT_SEX)));
					visitor.put(PNR,c.getString(c.getColumnIndex(DatabaseHelper.COLUMN_CONTACT_PNMBR)));
					visitor.put(NO_SFR,c.getString(c.getColumnIndex(DatabaseHelper.COLUMN_CONTACT_NOSFR)));
				} catch (JSONException ex) {
					ex.printStackTrace();
				}
			} else {
				throw new CursorIndexOutOfBoundsException(
						"Could not move to first index");
			}
			c.close();
			visitors.put(visitor);
			// Create json object for all relevant attendants and add them
			// to
			// the
			// array
			ArrayList<Integer> attendantIds = db
					.getAttendantIdsFromBookingId(bookingId);
			for (int id : attendantIds) {
				c = db.getAttendantContactInfo(id);
				if (c.moveToFirst()) {
					try {
						visitor = new JSONObject();
						visitor.put(FIRST_NAME,c.getString(c.getColumnIndex(DatabaseHelper.COLUMN_ATTENDANTS_FIRSTNAME)));
						visitor.put(LAST_NAME,c.getString(c.getColumnIndex(DatabaseHelper.COLUMN_ATTENDANTS_LASTNAME)));
						visitor.put(PNR,c.getString(c.getColumnIndex(DatabaseHelper.COLUMN_ATTENDANTS_PNMBR)));
						visitor.put(SEX,c.getString(c.getColumnIndex(DatabaseHelper.COLUMN_ATTENDANTS_SEX)));
						visitor.put(NO_SFR,c.getString(c.getColumnIndex(DatabaseHelper.COLUMN_ATTENDANTS_NOSFR)));
					} catch (JSONException ex) {
						ex.printStackTrace();
					}
				} else {
					throw new CursorIndexOutOfBoundsException(
							"Could not move to first index");
				}
				visitors.put(visitor);
				c.close();
			}
			db.close();
			JSONObject json = new JSONObject();
			try {
				json.put("visitors", visitors);
				json.put("reservation_key", bookingId);
			} catch (JSONException ex) {
				ex.printStackTrace();
			}

			// http post
			HttpParams httpParams = new BasicHttpParams();
			HttpConnectionParams.setConnectionTimeout(httpParams, 10000); // Timeout
			HttpConnectionParams.setSoTimeout(httpParams, 10000); // Limits
			HttpClient client = new DefaultHttpClient(httpParams);

			HttpResponse response = null;
			InputStream is = null;
			try {
				// Create request
				HttpPost request = new HttpPost(
						getString(R.string.httpRequestUrl));
				Log.v("req:", json.toString());
				// add json & case
				List<NameValuePair> pairs = new ArrayList<NameValuePair>(2);
				pairs.add(new BasicNameValuePair("json", json.toString()));
				pairs.add(new BasicNameValuePair("case", "confirm"));
				UrlEncodedFormEntity u = new UrlEncodedFormEntity(pairs);
				u.setContentEncoding("UTF-8");
				request.setEntity(u);

				response = client.execute(request); // execute
				is = response.getEntity().getContent(); // get data

			} catch (Exception e) {
				Log.e("Http Exception: ", e.toString());
			}

			String result;
			// convert response to string
			try {

				BufferedReader reader = new BufferedReader(
						new InputStreamReader(is, "iso-8859-1"), 8);
				StringBuilder sb = new StringBuilder();
				String line = null;
				while ((line = reader.readLine()) != null) {
					sb.append(line + "\n");
				}
				is.close();

				result = sb.toString();
				Log.e("res", result);
				result = result.substring(0, 1);
				// if affected rows is correct
				if (Integer.parseInt(result) == attendantIds.size() + 1) {
					return "true";
				}
			} catch (Exception e) {
				Log.e("log_tag", "Error converting result " + e.toString());
			}
			// fail if this point is reached
			return "false";
		}

		private void bookingCreated(String result) {
			if (result.equals("true")) {
				// save in local database to enable my bookings
				DatabaseSQLite db = new DatabaseSQLite(getApplicationContext());
				db.open();
				db.setBookingBooked(bookingId);
				db.close();
				Intent i = new Intent(getApplicationContext(),
						BookConfirmationActivity.class);
				i.putExtra("bookingId", bookingId);
				i.putExtra("state", BookConfirmationActivity.STATE_CONFIRM);
				startActivity(i);
			} else {
				Toast.makeText(getApplicationContext(),
						R.string.couldNotCompleteBooking, Toast.LENGTH_LONG)
						.show();
			}
		}

		private void sendEmailConfirmation() {
			HttpURLConnection urlConnection = null;
			try {
				URL url = new URL(getString(R.string.httpEmailRequestUrl)
						+ bookingId);
				urlConnection = (HttpURLConnection) url.openConnection();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				urlConnection.disconnect();
			}
		}

	}

}

package se.forsmark.visit;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpException;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import se.forsmark.visit.database.DatabaseHelper;
import se.forsmark.visit.database.DatabaseSQLite;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.sqlite.SQLiteException;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
	private final int EDIT_CONTACT = 1, EDIT_ATTENDANT = 2;
	private int seatsLeft;
	private int preAttendants;  //Attendents that have been added but whose forms are still to be edited

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.confirmview);

		initialize();
	
		
	}

	private void initialize() {
		// Set Title
		preAttendants=0;
	
		TextView tv = (TextView) findViewById(R.id.border_title);
		tv.setText(R.string.ConfirmationTitle);

		// Unhide progress and set background
		View v = findViewById(R.id.border_progress);
		v.setBackgroundResource(R.drawable.border_step_three);
		v.setVisibility(View.VISIBLE);
		Bundle extras = getIntent().getExtras();
		bookingId = extras.getString("bookingId");
		contactId = extras.getInt("contactId");
		
		
		seatsLeft=getSeatsLeft();
		Log.v("SeatsLeft", Integer.toString(seatsLeft));
		Log.v("bid", bookingId);
		
		DatabaseSQLite db = new DatabaseSQLite(getApplicationContext());
		db.open();
		// Get contact name
		String contactName = db.getContactName(contactId);

		Button b = (Button) findViewById(R.id.ButtonConfirmContactPerson);
		b.setText(contactName);
		
		

		ArrayList<Integer> al = db.getAttendantIdsFromBookingId(bookingId);
		LinearLayout l = (LinearLayout) findViewById(R.id.confirmformLayout);
		
		if (seatsLeft > 0) {
		
		}else{
			b = (Button) findViewById(R.id.AddAttendantButton);  //lägg till addAttendantknapp
			b.setVisibility(View.GONE);
		}
		TextView tv2=(TextView)findViewById(R.id.SeatsLeft);
		tv2.setText("Det finns " + seatsLeft + " platser kvar.");
		for (int id : al) {
			b = new Button(this);
			b.setId(id);
			b.setGravity(Gravity.LEFT);
			b.setTextAppearance(getApplicationContext(), R.style.CodeFont);
			b.setTextColor(Color.WHITE);
			b.setText(db.getAttendantName(id));
			b.setBackgroundResource(R.drawable.editbutton);
			b.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.editbutton_arr, 0);
			b.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					editButton(v);
				}
			});
			l.addView(b, l.getChildCount() - 2);
		
		}
		db.close();
	
   
	}

	public void deleteAttendant(int id) {
		DatabaseSQLite db = new DatabaseSQLite(getApplicationContext());
		db.open();
		db.deleteAttendant(id);
		db.close();
		LinearLayout l = (LinearLayout) findViewById(R.id.confirmformLayout);
		l.removeView(findViewById(id));
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			Bundle extras = data.getExtras();
			String text = extras.getString("displayName");
			if (requestCode == EDIT_CONTACT) {
				Button b = (Button) findViewById(R.id.ButtonConfirmContactPerson);
				b.setText(text);
			} else if (requestCode == EDIT_ATTENDANT) {
				int id = extras.getInt("attendantId");
				if (extras.getBoolean("edit")) {
					Button b = (Button) findViewById(id);
					b.setText(text);
				} else {
					deleteAttendant(id);
				}

			}
		}
	}

	public void editButton(View v) {
		Intent dialog = new Intent(v.getContext(), EditAttendantDialogActivity.class);
		dialog.putExtra("attendantId", v.getId());
		startActivityForResult(dialog, EDIT_ATTENDANT);

	}

	public void editContact(View v) {
		Intent i = new Intent(getApplicationContext(), EditContactActivity.class);
		i.putExtra("contactId", contactId);
		startActivityForResult(i, EDIT_CONTACT);
	}

	public void addAttendantButton(View v) {
		// Kolla -finns det platser kvar
		// update seatsleft
		seatsLeft=getSeatsLeft();
		
		if (seatsLeft > 0) {
			Log.v("Att", "ny");
			
			preAttendants++;
			
			DatabaseSQLite db = new DatabaseSQLite(getApplicationContext());
			db.open();
			int id=db.addAttendant("", "", "", "", 0, bookingId);
			db.close();
			
			preBook(1, bookingId); //lägg till i prebook
			
			Button b = new Button(this);
		
			Log.v("buttonID", Integer.toString(id));
			b.setGravity(Gravity.LEFT);
			b.setTextAppearance(getApplicationContext(), R.style.CodeFont);
			b.setTextColor(Color.WHITE);
			b.setText("Fyll i information eller ta bort");
			b.setBackgroundResource(R.drawable.editbutton);
			b.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.editbutton_arr, 0);
			b.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					editButton(v);
				}
			});
			LinearLayout l = (LinearLayout) findViewById(R.id.confirmformLayout);
			l.addView(b, l.getChildCount() - 2);
			seatsLeft=getSeatsLeft();
			if(seatsLeft>0){
				TextView tv2=(TextView)findViewById(R.id.SeatsLeft);
				tv2.setText("Det finns " + seatsLeft + " platser kvar.");
			}
			
			
			// Lägg till deltagare i databas och ta oss till den aktiviteten
		} else {
			Button b = (Button) findViewById(R.id.AddAttendantButton);
			b.setVisibility(View.GONE);
			// TOAST NO SEATS
			String text = getResources().getString(R.string.ConfirmActivityNoSeatsToast);
			Toast t = Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT);
			t.setGravity(Gravity.TOP, 0, 0); // Position
			t.show();
		}
		

	}
	
	public String preBook(int seats, String id) { //id är bookingId
		String result = "";
		InputStream is = null;
		// http post
		try {
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(
					this.getString(R.string.httpRequestUrl));

			List<NameValuePair> pairs = new ArrayList<NameValuePair>();
			
			pairs.add(new BasicNameValuePair("case", "preBook"));
			pairs.add(new BasicNameValuePair("id", "" + id));
			pairs.add(new BasicNameValuePair("seats", "" + seats));
			
			httppost.setEntity(new UrlEncodedFormEntity(pairs));

			HttpResponse response = httpclient.execute(httppost);
			HttpEntity entity = response.getEntity();
			is = entity.getContent();
		} catch (Exception e) {
			Log.e("log_tag", "Error in http connection " + e.toString());
		}
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					is, "iso-8859-1"), 8);
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
			is.close();

			result = sb.toString();

		} catch (Exception e) {
			Log.e("log_tag", "Error converting result " + e.toString());
		}
		result = result.substring(1, result.length()-2);
		return result;
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
	
	public void bottomCancelClick(View v) {
		// Dialogruta AVBRYT BOKNING
		// TODO hitta snyggare lösning och lägg in strängarna i strings xmlen
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.create();
		builder.setTitle("Avbryt bokning");
		builder.setMessage("Är du säker på att du vill avbryta din bokning?");
		builder.setPositiveButton("Ja", new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {
				setResult(RESULT_CANCELED);
				finish();
			}
		});
		builder.setNegativeButton("Nej", new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {
				// Do nothing
			}
		});
		builder.show(); // b
	}
	
	
	
	public void bottomNextClick(View v) {
		//TODO SKAPA BOKNINGEN OCH SKICKA MAIL
		
//		Intent i = new Intent(getApplicationContext(), BookConfirmationActivity.class);
//		i.putExtra("bookingId", bookingId);
//		i.putExtra("state", 1);
//		startActivity(i);
		
		createBooking();
	}
	
	public int getSeatsLeft(){
		String result = "";
		InputStream is = null;
		String[] tmp = { "NOCONNECTION" };
		int tp=0;
		try {
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(
					this.getString(R.string.httpRequestUrl));  

			List<NameValuePair> pairs = new ArrayList<NameValuePair>();
			
			pairs.add(new BasicNameValuePair("case", "getSeats"));
			pairs.add(new BasicNameValuePair("id", "" + bookingId));
			
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
				sb.append(line + "\n");
			}
			is.close();
			
			//sb.deleteCharAt(0);
			//sb.deleteCharAt(sb.indexOf("'"));
			result = sb.toString();
			result = result.substring(0, result.length()-1);
		} catch (StringIndexOutOfBoundsException e) {
			tmp[0] = "NORESULT";
			
		} catch (Exception e) {
			Log.e("log_tag", "Error converting result " + e.toString());
		}
		
		return Integer.parseInt(result);
		
	}
	

	private void createBooking() {
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
				visitor.put(FIRST_NAME, c.getString(c.getColumnIndex(DatabaseHelper.COLUMN_CONTACT_FIRSTNAME)));
				visitor.put(LAST_NAME, c.getString(c.getColumnIndex(DatabaseHelper.COLUMN_CONTACT_LASTNAME)));
				visitor.put(ADDRESS, c.getString(c.getColumnIndex(DatabaseHelper.COLUMN_CONTACT_ADRESS)));
				visitor.put(ZIP, c.getString(c.getColumnIndex(DatabaseHelper.COLUMN_CONTACT_PNMBR)));
				visitor.put(POSTAREA, c.getString(c.getColumnIndex(DatabaseHelper.COLUMN_CONTACT_POSTADRESS)));
				visitor.put(COUNTRY, c.getString(c.getColumnIndex(DatabaseHelper.COLUMN_CONTACT_COUNTRY)));
				visitor.put(PHONE, c.getString(c.getColumnIndex(DatabaseHelper.COLUMN_CONTACT_CELLPHONE)));
				visitor.put(MAIL, c.getString(c.getColumnIndex(DatabaseHelper.COLUMN_CONTACT_EMAIL)));
				visitor.put(SEX, c.getString(c.getColumnIndex(DatabaseHelper.COLUMN_CONTACT_SEX)));
				visitor.put(PNR, c.getString(c.getColumnIndex(DatabaseHelper.COLUMN_CONTACT_PNMBR)));
				visitor.put(NO_SFR, c.getString(c.getColumnIndex(DatabaseHelper.COLUMN_CONTACT_NOSFR)));
				// // FIXME Lägg till i kontakt-aktiviteten & db
			} catch (JSONException ex) {
				ex.printStackTrace();
			}
		} else {
			throw new CursorIndexOutOfBoundsException("Could not move to first index");
		}

		visitors.put(visitor);
		// Create json object for all relevant attendants and add them to the
		// array
		ArrayList<Integer> attendantIds = db.getAttendantIdsFromBookingId(bookingId);
		for (int id : attendantIds) {
			c = db.getAttendantContactInfo(id);
			if (c.moveToFirst()) {
				try {
					visitor = new JSONObject();
					visitor.put(FIRST_NAME, c.getString(c.getColumnIndex(DatabaseHelper.COLUMN_ATTENDANTS_FIRSTNAME)));
					visitor.put(LAST_NAME, c.getString(c.getColumnIndex(DatabaseHelper.COLUMN_ATTENDANTS_LASTNAME)));
					visitor.put(PNR, c.getString(c.getColumnIndex(DatabaseHelper.COLUMN_ATTENDANTS_PNMBR)));
					visitor.put(SEX, c.getString(c.getColumnIndex(DatabaseHelper.COLUMN_ATTENDANTS_SEX)));
					visitor.put(NO_SFR, c.getString(c.getColumnIndex(DatabaseHelper.COLUMN_ATTENDANTS_NOSFR)));
				} catch (JSONException ex) {
					ex.printStackTrace();
				}
			} else {
				throw new CursorIndexOutOfBoundsException("Could not move to first index");
			}
			visitors.put(visitor);
		}
		db.close();
		JSONObject postParameter = new JSONObject();
		try {
			postParameter.put("visitors", visitors);
			postParameter.put("reservation_key", bookingId);
		} catch (JSONException ex) {
			ex.printStackTrace();
		}

		if (isNetworkConnected()) {
			// http post

			HttpParams httpParams = new BasicHttpParams();
			HttpConnectionParams.setConnectionTimeout(httpParams, 10000); // Timeout
			HttpConnectionParams.setSoTimeout(httpParams, 10000);	// Limit
			HttpClient client = new DefaultHttpClient(httpParams);
			
			HttpResponse response = null;
			InputStream is = null;
			try {
				// HttpPost post = new
				// HttpPost(getResources().getString(R.string.httpRequestUrl));
				HttpPost request = new HttpPost("http://83.249.138.5/backlog/test.php");
				Log.v("req:", postParameter.toString());
				//se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
				request.setEntity(new ByteArrayEntity(postParameter.toString().getBytes("UTF8")));
				response = client.execute(request); // execute
				is = response.getEntity().getContent(); // get data

			} catch (Exception e) {
				Log.e("Http Exception: ", e.toString());
			}

			String result;
			// convert response to string
			try {

				BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8);
				StringBuilder sb = new StringBuilder();
				String line = null;
				while ((line = reader.readLine()) != null) {
					sb.append(line + "\n");
				}
				is.close();

				result = sb.toString();
				Log.i("result", result);
			} catch (Exception e) {
				Log.e("log_tag", "Error converting result " + e.toString());
			}

		} else {
			Toast.makeText(getApplicationContext(), R.string.noInternet, Toast.LENGTH_SHORT).show();
		}
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

}

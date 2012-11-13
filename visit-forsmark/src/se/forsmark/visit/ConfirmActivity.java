package se.forsmark.visit;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
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

import se.forsmark.visit.database.DatabaseSQLite;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
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
		
		String result = "";
		InputStream is = null;
	/*	
		try {
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(
					this.getString(R.string.httpRequestUrl));

			List<NameValuePair> pairs = new ArrayList<NameValuePair>();
			
			pairs.add(new BasicNameValuePair("case", "getSeats"));
			pairs.add(new BasicNameValuePair("id", "" + contactId));
			
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

			result = sb.toString();
			result = result.substring(1, result.length() - 2);
			result = result.replace("\"", "");

			tmp = result.split(",");
		} catch (StringIndexOutOfBoundsException e) {
			tmp[0] = "NORESULT";
		} catch (Exception e) {
			Log.e("log_tag", "Error converting result " + e.toString());
		}
	}
		
		*/
		TextView tv = (TextView) findViewById(R.id.border_title);
		tv.setText(R.string.ConfirmationTitle);

		// Unhide progress and set background
		View v = findViewById(R.id.border_progress);
		v.setBackgroundResource(R.drawable.border_step_three);
		v.setVisibility(View.VISIBLE);
		Bundle extras = getIntent().getExtras();
		bookingId = extras.getString("bookingId");
		contactId = extras.getInt("contactId");

		DatabaseSQLite db = new DatabaseSQLite(getApplicationContext());
		db.open();
		// Get contact name
		String contactName = db.getContactName(contactId);

		Button b = (Button) findViewById(R.id.ButtonConfirmContactPerson);
		b.setText(contactName);

		ArrayList<Integer> al = db.getAttendantIdsFromBookingId(bookingId);
		LinearLayout l = (LinearLayout) findViewById(R.id.confirmformLayout);
		
		if (seatsLeft > 0) {
			b = (Button) findViewById(R.id.AddAttendantButton);

		}
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
		if (seatsLeft > 0) {
			Log.v("Att", "ny");
			
			preAttendants++;
			
			DatabaseSQLite db = new DatabaseSQLite(getApplicationContext());
			db.open();
			int id=db.addAttendant("", "", "", "", 0, bookingId);
			db.close();
			
			preBook(1, bookingId); //lägg till i prebook
			Button b = new Button(this);
			b.setId(id);
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
		Intent i = new Intent(getApplicationContext(), BookConfirmationActivity.class);
		i.putExtra("bookingId", bookingId);
		i.putExtra("state", 1);
		startActivity(i);
	}
}

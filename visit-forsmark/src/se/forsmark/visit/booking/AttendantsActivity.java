package se.forsmark.visit.booking;

import java.util.ArrayList;

import se.forsmark.visit.R;
import se.forsmark.visit.R.drawable;
import se.forsmark.visit.R.id;
import se.forsmark.visit.R.layout;
import se.forsmark.visit.R.string;
import se.forsmark.visit.database.DatabaseHelper;
import se.forsmark.visit.database.DatabaseSQLite;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class AttendantsActivity extends Activity {
	private String deltagare = "Deltagare ";
	private int counter = 1;
	private int nbrAttendants;
	private int eventId;
	private String bookingId;
	private ArrayList<Integer> attendantIds;
	private final int BOOKING = 10;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.attendantsview);
		Bundle extras = getIntent().getExtras();
		nbrAttendants = extras.getInt("attendantsCount");
		bookingId = extras.getString("bookingId");
		eventId = extras.getInt("eventId");
		initialize();
	}

	private void initialize() {
		// Get Info from shared preferences & intent
		String text;
		// Set Title
		TextView tv = (TextView) findViewById(R.id.border_title);
		text = String.format(getString(R.string.attendantsTitle), (counter + 1), nbrAttendants);
		tv.setText(text);

		// Unhide next button
		Button b;
		if (nbrAttendants > 2) {
			b = (Button) findViewById(R.id.button_next_top);
			b.setVisibility(View.VISIBLE);
			b = (Button) findViewById(R.id.bottomNextButton);
			b.setVisibility(View.GONE);
		}

		// Unhide progress and set background
		View v = findViewById(R.id.border_progress);
		v.setBackgroundResource(R.drawable.border_step_two);
		v.setVisibility(View.VISIBLE);

		// EJ SFR TOAST
		b = (Button) findViewById(R.id.button_hint_SFR);
		b.setOnClickListener(new OnClickListener() { // Create Toast hint

			public void onClick(View v) {
				// Get message from resources
				String text = getResources().getString(R.string.NoSFRToast);
				Toast t = Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG); // Create
																							// toast
				t.setGravity(Gravity.TOP, 0, 0); // Position
				t.show();
			}
		});
		// Get Ids from db;
		DatabaseSQLite db = new DatabaseSQLite(getApplicationContext());
		db.open();
		attendantIds = db.getAttendantIdsFromBookingId(bookingId);
		db.close();
		if (attendantIds.size() > 0) {
			fillForm();
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_CANCELED) {
			setResult(RESULT_CANCELED);
			finish();
		}
	}

	private boolean validate() {
		// Kolla att alla fält är fyllda:
		LinearLayout l = (LinearLayout) findViewById(R.id.attendantformLayout);
		for (int p = 0; p < l.getChildCount(); p++) {
			View vv = l.getChildAt(p);
			if (vv instanceof EditText) {
				EditText ed = (EditText) vv;
				if (ed.getText().toString().equals("")) {
					// TODO I MÅN AV TID - fixa ordentlig validering!
					String text = getResources().getString(R.string.errorMessageFieldEmpty); // Get
																								// message
																								// from
																								// resources
					Toast t2 = Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG); // Creat
																									// toast
					t2.setGravity(Gravity.BOTTOM, 0, 0); // Position
					t2.show();
					return false;
				}
			}
			// Validates radiobuttons: man & woman
		}
		RadioButton rbMan = (RadioButton) findViewById(R.id.attendantradioButtonMan);
		RadioButton rbWoman = (RadioButton) findViewById(R.id.attendantradioButtonWoman);

		if (!rbMan.isChecked() && !rbWoman.isChecked()) {
			String text = getResources().getString(R.string.errorMessageGender); // Get
																					// message
																					// from
																					// resources
			Toast t3 = Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG); // Create
																							// toast
			t3.setGravity(Gravity.BOTTOM, 0, 0); // Position
			t3.show();
			return false;
		}
		return true;
	}

	private void addOrUpdateAttendant() {
		EditText firstname = (EditText) findViewById(R.id.attendantPersonFirstName);
		EditText lastname = (EditText) findViewById(R.id.attendantPersonLastName);
		EditText pnmbr = (EditText) findViewById(R.id.attendantPersonNbr);
		RadioButton rbMan = (RadioButton) findViewById(R.id.attendantradioButtonMan);
		CheckBox cb = (CheckBox) findViewById(R.id.attendantcheckboxSFR);

		int aid;
		if (counter - 1 < attendantIds.size()) {
			aid = attendantIds.get(counter - 1);
		} else {
			aid = 0;
		}
		DatabaseSQLite db = new DatabaseSQLite(getApplicationContext());
		db.open();
		if (aid == 0) { // If attendant hasn't been added to the db
			int id = db.addAttendant(firstname.getText().toString(), lastname.getText().toString(), pnmbr.getText()
					.toString(), rbMan.isChecked() ? "male" : "female", cb.isChecked() ? 1 : 0, bookingId);
			if (id != -1)
				attendantIds.add(id);

		} else { // Update attendant if already exists in db
			db.updateAttendant(aid, firstname.getText().toString(), lastname.getText().toString(), pnmbr.getText()
					.toString(), rbMan.isChecked() ? "male" : "female", cb.isChecked() ? 1 : 0);
		}
		db.close();
	}

	@Override
	public void onBackPressed() {
		addOrUpdateAttendant();
		setResult(RESULT_OK);
		finish();
	}

	public void bottomCancelClick(View v) {

		// Dialogruta AVBRYT BOKNING
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.create();
		builder.setTitle(R.string.cancelBookingTitle);
		builder.setMessage(R.string.cancelBookingMessage);
		builder.setPositiveButton(R.string.dialogYes, new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {
				setResult(RESULT_CANCELED);
				finish();
			}
		});
		builder.setNegativeButton(R.string.dialogNo, new DialogInterface.OnClickListener() {
			
			public void onClick(DialogInterface dialog, int which) {
				// Do nothing
			}
		});
		builder.show(); // b
	}

	public void bottomBackClick(View v) {
		onBackPressed();

	}

	public void bottomNextClick(View v) {

		if (validate()) {
			addOrUpdateAttendant();
		} else {
			return;
		}

		DatabaseSQLite db = new DatabaseSQLite(getApplicationContext());
		db.open();
		int id = db.getLatestContactId();
		db.close();
		Intent ip = new Intent(v.getContext(), ConfirmActivity.class);
		ip.putExtra("eventId", eventId);
		ip.putExtra("contactId", id);
		ip.putExtra("bookingId", bookingId);
		ip.putExtra("progressBar", 3);
		startActivityForResult(ip, BOOKING);
	}

	public void topNextButton(View v) {
		if (validate()) {
			addOrUpdateAttendant();
		} else {
			return;
		}

		/*
		 * Om det är första deltagaren visa back-knappen. Om det är sista
		 * deltagaren, dölj next-knappen.
		 */
		++counter;
		// Modified stuff:
		if (counter == 2) {
			Button b = (Button) findViewById(R.id.button_back_top);
			b.setVisibility(View.VISIBLE);
		}
		if (counter >= nbrAttendants - 1) {
			Button b = (Button) findViewById(R.id.button_next_top);
			b.setVisibility(View.GONE);
			b = (Button) findViewById(R.id.bottomNextButton);
			b.setVisibility(View.VISIBLE);
		}
		/*
		 * RESET FIELDS
		 */
		EditText firstname = (EditText) findViewById(R.id.attendantPersonFirstName);
		EditText lastname = (EditText) findViewById(R.id.attendantPersonLastName);
		EditText pnmbr = (EditText) findViewById(R.id.attendantPersonNbr);
		RadioGroup rg = (RadioGroup) findViewById(R.id.attendantRadioGroup);
		CheckBox cb = (CheckBox) findViewById(R.id.attendantcheckboxSFR);

		// TESTTOAST
		/*
		 * String text="Counter: "+ counter + " attendantIdsSize: " +
		 * attendantIds.size() + " nbr Attendants: " +nbrAttendants; Toast t2 =
		 * Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG); //
		 * Create toast t2.show();
		 */

		// Check if this attendant already exists in the attendantArrays. If so
		// the correct information is gathered from the database and distributed
		// over the fields accordingly.
		if (attendantIds.size() > counter) {
			fillForm();
		}
		// Else clear all fields
		else {
			firstname.setText("");
			lastname.setText("");
			pnmbr.setText("");
			rg.clearCheck();
			cb.setChecked(false);
		}
		// Set Title
		TextView tv = (TextView) findViewById(R.id.border_title);
		tv.setText(String.format(getString(R.string.attendantsTitle), (counter + 1), nbrAttendants));
	}

	public void topBackButton(View v) {
		// sparar/uppdataerar utan validering
		addOrUpdateAttendant();

		// hämta info från föregående och fyll i fields.
		--counter;
		fillForm();

		// dölj / visa navigation
		if (counter == nbrAttendants - 2) {
			Button b = (Button) findViewById(R.id.button_next_top);
			b.setVisibility(View.VISIBLE);
			b = (Button) findViewById(R.id.bottomNextButton);
			b.setVisibility(View.GONE);
		}
		if (counter <= 1) {
			Button backb = (Button) findViewById(R.id.button_back_top);
			backb.setVisibility(View.GONE);
		}
		// Set Title
		TextView tv2 = (TextView) findViewById(R.id.border_title);
		tv2.setText(String.format(getString(R.string.attendantsTitle), (counter + 1), nbrAttendants));
	}

	private void fillForm() {
		EditText firstname = (EditText) findViewById(R.id.attendantPersonFirstName);
		EditText lastname = (EditText) findViewById(R.id.attendantPersonLastName);
		EditText pnmbr = (EditText) findViewById(R.id.attendantPersonNbr);
		DatabaseSQLite db = new DatabaseSQLite(getApplicationContext());
		db.open();
		Cursor c = db.getAttendantContactInfo(attendantIds.get(counter - 1));
		if (c.moveToFirst()) {
			firstname.setText(c.getString(c.getColumnIndex(DatabaseHelper.COLUMN_ATTENDANTS_FIRSTNAME)));
			lastname.setText(c.getString(c.getColumnIndex(DatabaseHelper.COLUMN_ATTENDANTS_LASTNAME)));
			pnmbr.setText(c.getString(c.getColumnIndex(DatabaseHelper.COLUMN_ATTENDANTS_PNMBR)));

			if (c.getString(c.getColumnIndex(DatabaseHelper.COLUMN_ATTENDANTS_SEX)).equals("male")) {
				RadioButton rb = (RadioButton) findViewById(R.id.attendantradioButtonMan);
				rb.setChecked(true);
			} else {
				RadioButton rb = (RadioButton) findViewById(R.id.attendantradioButtonWoman);
				rb.setChecked(true);
			}
			CheckBox cb = (CheckBox) findViewById(R.id.attendantcheckboxSFR);
			if (c.getInt(c.getColumnIndex(DatabaseHelper.COLUMN_ATTENDANTS_NOSFR)) == 1) {
				cb.setChecked(true);
			} else {
				cb.setChecked(false);
			}

		}
		c.close();
		db.close();
	}

}

package se.forsmark.visit;

import java.util.ArrayList;

import se.forsmark.visit.database.DatabaseHelper;
import se.forsmark.visit.database.DatabaseSQLite;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
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
	private int counter = 0;
	private int nbrAttendants;
	private String bookingId = "hejhej";
	private SharedPreferences prefs;
	private ArrayList<Integer> attendantIds;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
		getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.attendantsview);
		initialize();
	}

	private void initialize() {
		// Get Info from shared preferences & intent
		String text;
		Bundle extras = getIntent().getExtras();
		nbrAttendants = extras.getInt("attendantsCount");
		prefs = getSharedPreferences("forsmark", MODE_PRIVATE);
		// bookingId = prefs.getString("bookingId", null);

		// Set Title
		TextView tv = (TextView) findViewById(R.id.border_title);
		text = deltagare + (counter + 1) + " av " + nbrAttendants;
		tv.setText(text);

		// Unhide next button
		Button b;
		if (nbrAttendants > 1) {
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
				Toast t = Toast.makeText(getApplicationContext(), text,
						Toast.LENGTH_LONG); // Create toast
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

	private boolean validate() {
		// Kolla att alla f�lt �r fyllda:
		LinearLayout l = (LinearLayout) findViewById(R.id.attendantformLayout);
		for (int p = 0; p < l.getChildCount(); p++) {
			View vv = l.getChildAt(p);
			if (vv instanceof EditText) {
				EditText ed = (EditText) vv;
				if (ed.getText().toString().equals("")) {
					// TODO I M�N AV TID - fixa ordentlig validering!
					String text = getResources().getString(
							R.string.errorMessageFieldEmpty); // Get message
																// from
																// resources
					Toast t2 = Toast.makeText(getApplicationContext(), text,
							Toast.LENGTH_SHORT); // Creat toast
					t2.setGravity(Gravity.BOTTOM, 0, 0); // Position
					t2.show();
					return false;
				}
			}
			// Validates radiobuttons: man & woman
		}
		RadioButton rbMan = (RadioButton) findViewById(R.id.attendantradioButtonMan);
		RadioButton rbWoman = (RadioButton) findViewById(R.id.attendantradioButtonWoman);
		CheckBox cb = (CheckBox) findViewById(R.id.attendantcheckboxSFR);

		if (!rbMan.isChecked() && !rbWoman.isChecked()) {
			String text = getResources().getString(R.string.errorMessageGender); // Get
																					// message
																					// from
																					// resources
			Toast t3 = Toast.makeText(getApplicationContext(), text,
					Toast.LENGTH_SHORT); // Create toast
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
		if (counter < attendantIds.size()) {
			aid = attendantIds.get(counter);
		} else {
			aid = 0;
		}
		DatabaseSQLite db = new DatabaseSQLite(getApplicationContext());
		db.open();
		if (aid == 0) { // If attendant hasn't been added to the db
			int id = db.addAttendant(firstname.getText().toString(), lastname
					.getText().toString(), pnmbr.getText().toString(), rbMan
					.isChecked() ? "male" : "female", cb.isChecked() ? 1 : 0,
					bookingId);
			if (id != -1)
				attendantIds.add(id);
			// else
			// TODO ERROR

		} else { // Update attendant if already exists in db
			db.updateAttendant(aid, firstname.getText().toString(), lastname
					.getText().toString(), pnmbr.getText().toString(), rbMan
					.isChecked() ? "male" : "female", cb.isChecked() ? 1 : 0,
					bookingId);
		}
		db.close();
	}

	@Override
	public void onBackPressed() {
		addOrUpdateAttendant();
		finish();
	}

	public void bottomCancelClick(View v) {
		// TODO ta bort bokningen!
		// TODO CONFIRM popup
		onBackPressed();
	}

	public void bottomNextClick(View v) {

		if (validate()) {
			addOrUpdateAttendant();
		} else {
			return;
		}

		Intent i = new Intent();
		// TODO starta n�sta aktivitet

		/*
		 * Om det �r f�rsta deltagaren, visa back-knappen Om det �r n�st sista
		 * deltagaren, g� till bekr�fta-sidan Annars
		 */

		// Kolla om det �r sista deltagaren.
		// Om ej, g� viadare till n�sta och spara.
		if (counter == 0) {
			Button b = (Button) findViewById(R.id.button_back_top);
			b.setVisibility(View.VISIBLE);
		} else if (counter == nbrAttendants - 1) { // TODO -1 eller -2?
			Button b = (Button) findViewById(R.id.button_next_top);
			b.setVisibility(View.GONE);
		}

	}

	public void topNextButton(View v) {
		if (validate()) {
			addOrUpdateAttendant();
		} else {
			return;
		}

		/*
		 * Om det �r f�rsta deltagaren visa back-knappen. Om det �r sista
		 * deltagaren, d�lj next-knappen.
		 */
		++counter;
		// Modified stuff:
		if (counter == 1) {
			Button b = (Button) findViewById(R.id.button_back_top);
			b.setVisibility(View.VISIBLE);
			b = (Button) findViewById(R.id.bottomBackButton);
			b.setVisibility(View.GONE);
		} else if (counter >= nbrAttendants - 1) {
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
		tv.setText(deltagare + (counter + 1) + " av " + nbrAttendants);
	}

	public void topBackButton(View v) {
		// sparar/uppdataerar utan validering
		addOrUpdateAttendant();

		// h�mta info fr�n f�reg�ende och fyll i fields.
		--counter;
		fillForm();

		//d�lj / visa navigation
		if (counter == nbrAttendants - 2) {
			Button b = (Button) findViewById(R.id.button_next_top);
			b.setVisibility(View.VISIBLE);
			b = (Button) findViewById(R.id.bottomNextButton);
			b.setVisibility(View.GONE);
		} else if (counter <= 0) {
			Button backb = (Button) findViewById(R.id.button_back_top);
			backb.setVisibility(View.GONE);
			backb = (Button) findViewById(R.id.bottomBackButton);
			backb.setVisibility(View.VISIBLE);
		}
		// Set Title
		TextView tv2 = (TextView) findViewById(R.id.border_title);
		tv2.setText(deltagare + (counter + 1) + " av " + nbrAttendants);
	}
	
	private void fillForm() {
		EditText firstname = (EditText) findViewById(R.id.attendantPersonFirstName);
		EditText lastname = (EditText) findViewById(R.id.attendantPersonLastName);
		EditText pnmbr = (EditText) findViewById(R.id.attendantPersonNbr);
		DatabaseSQLite db = new DatabaseSQLite(getApplicationContext());
		db.open();
		Cursor c = db.getAttendantContactInfo(String.valueOf(attendantIds
				.get(counter)));
		if (c.moveToFirst()) {
			firstname
					.setText(c.getString(c
							.getColumnIndex(DatabaseHelper.COLUMN_ATTENDANTS_FIRSTNAME)));
			lastname.setText(c.getString(c
					.getColumnIndex(DatabaseHelper.COLUMN_ATTENDANTS_LASTNAME)));
			pnmbr.setText(c.getString(c
					.getColumnIndex(DatabaseHelper.COLUMN_ATTENDANTS_PNMBR)));

			if (c.getString(
					c.getColumnIndex(DatabaseHelper.COLUMN_ATTENDANTS_SEX))
					.equals("male")) {
				RadioButton rb = (RadioButton) findViewById(R.id.attendantradioButtonMan);
				rb.setChecked(true);
			} else {
				RadioButton rb = (RadioButton) findViewById(R.id.attendantradioButtonWoman);
				rb.setChecked(true);
			}
			CheckBox cb = (CheckBox) findViewById(R.id.attendantcheckboxSFR);
			if (c.getInt(c.getColumnIndex(DatabaseHelper.COLUMN_ATTENDANTS_SFR)) == 1) {
				cb.setChecked(true);
			}else {
				cb.setChecked(false);
			}

		}
		db.close();
	}

}

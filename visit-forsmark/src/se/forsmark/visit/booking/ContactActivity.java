package se.forsmark.visit.booking;

import se.forsmark.visit.R;
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
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

public class ContactActivity extends Activity {
	private String bookingId;
	private int attendantsCount;
	private static int BOOKING = 10, eventId;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.contactview);
		Bundle extras = getIntent().getExtras();
		bookingId = extras.getString("bookingId");
		attendantsCount = extras.getInt("seats");
		eventId = extras.getInt("eventId");
		initialize(); // Initialize views
	}

	/**
	 * Unhides elements etc..
	 */
	private void initialize() {

		// Set Title
		TextView tv = (TextView) findViewById(R.id.border_title);
		tv.setText(R.string.ContactActivityTitle);
		// Display progress
		ImageView iv = (ImageView) findViewById(R.id.border_progress);
		iv.setVisibility(View.VISIBLE); // Unhide progress
		
		if(attendantsCount > 1)
			iv.setBackgroundResource(R.drawable.border_step_one); // Display step one
		else
			iv.setBackgroundResource(R.drawable.border_step_one_two); // Display step one
		// Display hint button an
		Button b = (Button) findViewById(R.id.button_hint_top);
		
		b.setVisibility(View.VISIBLE); // Unhide button
		b.setOnClickListener(new OnClickListener() { // Create Toast hint

			public void onClick(View v) {
				// Create toast
				Toast t = Toast.makeText(getApplicationContext(), R.string.ContactActivityToast, Toast.LENGTH_LONG);
				t.setGravity(Gravity.TOP, 0, 0); // Position
				t.show();
			}
		});

		// EJ SFR TOAST
		b = (Button) findViewById(R.id.contact_button_hint_SFR);
		b.setOnClickListener(new OnClickListener() { // Create Toast hint

			public void onClick(View v) {
				// Get message from resources
				String text = getResources().getString(R.string.NoSFRToast);
				Toast t = Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG); // Create toast
				t.setGravity(Gravity.TOP, 0, 0); // Position
				t.show();
			}
		});

		// Fill form with latest contact info
		// TODO move this to dbsqllite class and return array with key=>value
		// pairs
		DatabaseSQLite db = new DatabaseSQLite(getApplicationContext());
		db.open();
		Cursor c = db.getLatestContactInfo();
		if (c.moveToFirst()) {
			EditText et = (EditText) findViewById(R.id.editTextPname);
			et.setText(c.getString(c.getColumnIndex(DatabaseHelper.COLUMN_CONTACT_FIRSTNAME)));
			et = (EditText) findViewById(R.id.editTextLName);
			et.setText(c.getString(c.getColumnIndex(DatabaseHelper.COLUMN_CONTACT_LASTNAME)));
			et = (EditText) findViewById(R.id.editTextPnmbr);
			et.setText(c.getString(c.getColumnIndex(DatabaseHelper.COLUMN_CONTACT_PNMBR)));

			if (c.getString(c.getColumnIndex(DatabaseHelper.COLUMN_CONTACT_SEX)).equals("male")) {
				RadioButton rb = (RadioButton) findViewById(R.id.radioButtonMan);
				rb.setChecked(true);
			} else {
				RadioButton rb = (RadioButton) findViewById(R.id.radioButtonWoman);
				rb.setChecked(true);
			}

			et = (EditText) findViewById(R.id.editTextAdress);
			et.setText(c.getString(c.getColumnIndex(DatabaseHelper.COLUMN_CONTACT_ADRESS)));
			et = (EditText) findViewById(R.id.editTextPostNmbr);
			et.setText(c.getString(c.getColumnIndex(DatabaseHelper.COLUMN_CONTACT_POSTNMBR)));
			et = (EditText) findViewById(R.id.editTextPostAdress);
			et.setText(c.getString(c.getColumnIndex(DatabaseHelper.COLUMN_CONTACT_POSTADRESS)));
			et = (EditText) findViewById(R.id.editTextCountry);
			et.setText(c.getString(c.getColumnIndex(DatabaseHelper.COLUMN_CONTACT_COUNTRY)));
			et = (EditText) findViewById(R.id.editTextCell);
			et.setText(c.getString(c.getColumnIndex(DatabaseHelper.COLUMN_CONTACT_CELLPHONE)));
			et = (EditText) findViewById(R.id.editTextEmail);
			et.setText(c.getString(c.getColumnIndex(DatabaseHelper.COLUMN_CONTACT_EMAIL)));
			CheckBox cb = (CheckBox) findViewById(R.id.contactcheckboxSFR);
			if (c.getInt(c.getColumnIndex(DatabaseHelper.COLUMN_CONTACT_NOSFR)) == 1) {
				cb.setChecked(true);
			} else {
				cb.setChecked(false);
			}
		}
		c.close();
		db.close();

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_CANCELED) {
			setResult(RESULT_CANCELED);
			finish();
		}
	}

	@Override
	public void onBackPressed() {
		bottomCancelClick(null); // FULING
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

	public void bottomNextClick(View v) {
		// Iterates through user input for simple VALIDATION
		LinearLayout l = (LinearLayout) findViewById(R.id.formLayout);
		for (int p = 0; p < l.getChildCount(); p++) {

			View vv = l.getChildAt(p);
			if (vv instanceof EditText) {
				EditText ed = (EditText) vv;
				if (ed.getText().toString().equals("")) {
					// TODO I MÅN AV TID - fixa ordentlig validering!
					Toast t2 = Toast.makeText(getApplicationContext(), getString(R.string.errorMessageFieldEmpty),
							Toast.LENGTH_LONG); // Create toast
					t2.setGravity(Gravity.BOTTOM, 0, 0); // Position
					t2.show();
					return;
				}
			}

			// Validates radiobuttons: man & woman
		}
		RadioButton rbMan = (RadioButton) findViewById(R.id.radioButtonMan);
		RadioButton rbWoman = (RadioButton) findViewById(R.id.radioButtonWoman);
		if (!rbMan.isChecked() && !rbWoman.isChecked()) {
			Toast t3 = Toast.makeText(getApplicationContext(), getString(R.string.errorMessageGender),
					Toast.LENGTH_LONG); // Create toast
			t3.setGravity(Gravity.BOTTOM, 0, 0); // Position
			t3.show();
			return;
		}

		// save to local database:

		EditText firstname = (EditText) findViewById(R.id.editTextPname);
		EditText lastname = (EditText) findViewById(R.id.editTextLName);
		EditText pnmbr = (EditText) findViewById(R.id.editTextPnmbr);
		EditText adress = (EditText) findViewById(R.id.editTextAdress);
		EditText postNmbr = (EditText) findViewById(R.id.editTextPostNmbr);
		EditText postadress = (EditText) findViewById(R.id.editTextPostAdress);
		EditText country = (EditText) findViewById(R.id.editTextCountry);
		EditText cellphone = (EditText) findViewById(R.id.editTextCell);
		EditText email = (EditText) findViewById(R.id.editTextEmail);
		CheckBox cb = (CheckBox) findViewById(R.id.contactcheckboxSFR);

		DatabaseSQLite db = new DatabaseSQLite(getApplicationContext());
		db.open();
		db.addContact(firstname.getText().toString(), lastname.getText().toString(), pnmbr.getText().toString(), rbMan
				.isChecked() ? "male" : "female", adress.getText().toString(), postNmbr.getText().toString(),
				postadress.getText().toString(), country.getText().toString(), cellphone.getText().toString(), email
						.getText().toString(), cb.isChecked() ? 1 : 0);
		int id = db.getLatestContactId();
		db.updateBookingContactId(bookingId, id);
		db.close();
			// Go to confirmation directly if there are no more attendants
			if (attendantsCount == 1) {
				Intent i = new Intent(getApplicationContext(), ConfirmActivity.class);
				i.putExtra("eventId", eventId);
				i.putExtra("contactId", id);
				i.putExtra("bookingId", bookingId);
				i.putExtra("progressBar", 2);
				startActivityForResult(i, BOOKING);
			} else {
				Intent i = new Intent(getApplicationContext(), AttendantsActivity.class);
				i.putExtra("attendantsCount", attendantsCount);
				i.putExtra("eventId", eventId);
				i.putExtra("bookingId", bookingId);
				startActivityForResult(i, BOOKING);
			}
	}
}

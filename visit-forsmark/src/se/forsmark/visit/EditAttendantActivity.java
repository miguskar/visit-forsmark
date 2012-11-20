package se.forsmark.visit;

import se.forsmark.visit.database.DatabaseHelper;
import se.forsmark.visit.database.DatabaseSQLite;
import android.app.Activity;
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
import android.widget.TextView;
import android.widget.Toast;

public class EditAttendantActivity extends Activity {
	private int attendantId;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.attendantsview);
		Bundle extras = getIntent().getExtras();
		attendantId = extras.getInt("attendantId");

		initialize();
	}

	private void initialize() {

		// Unhide next button
		Button b;
		b = (Button) findViewById(R.id.bottomBackButton);
		b.setVisibility(View.GONE);

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
		
		//Change name of the button "next" to "save"
		Button next = (Button) findViewById(R.id.bottomNextButton);
		next.setText(getString(R.string.Save));
		// Fill form with attendant Information & set title
		fillForm(attendantId);
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
					Toast t2 = Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT); // Creat
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
			Toast t3 = Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT); // Create
																							// toast
			t3.setGravity(Gravity.BOTTOM, 0, 0); // Position
			t3.show();
			return false;
		}
		return true;
	}

	private void updateAttendant() {
		EditText firstname = (EditText) findViewById(R.id.attendantPersonFirstName);
		EditText lastname = (EditText) findViewById(R.id.attendantPersonLastName);
		EditText pnmbr = (EditText) findViewById(R.id.attendantPersonNbr);
		RadioButton rbMan = (RadioButton) findViewById(R.id.attendantradioButtonMan);
		CheckBox cb = (CheckBox) findViewById(R.id.attendantcheckboxSFR);

		DatabaseSQLite db = new DatabaseSQLite(getApplicationContext());
		db.open();
		// Update attendant in db
		db.updateAttendant(attendantId, firstname.getText().toString(), lastname.getText().toString(), pnmbr.getText()
				.toString(), rbMan.isChecked() ? "male" : "female", cb.isChecked() ? 1 : 0);
		db.close();
	}

	@Override
	public void onBackPressed() {
		setResult(RESULT_CANCELED);
		finish();
	}

	public void bottomCancelClick(View v) {
		onBackPressed();
	}

	public void bottomNextClick(View v) {

		if (validate()) {
			updateAttendant();
		} else {
			return;
		}

		EditText firstname = (EditText) findViewById(R.id.attendantPersonFirstName);
		EditText lastname = (EditText) findViewById(R.id.attendantPersonLastName);
		Intent i = new Intent(getApplicationContext(), ConfirmActivity.class);
		i.putExtra("attendantId", attendantId);
		i.putExtra("displayName", firstname.getText().toString() + " " + lastname.getText().toString());
		setResult(RESULT_OK, i);
		finish();
	}

	private void fillForm(int id) {
		// OBS SÄTTER ÄVEN TITEL I DENNA AKTIVITET
		TextView tv = (TextView) findViewById(R.id.border_title);
		EditText firstname = (EditText) findViewById(R.id.attendantPersonFirstName);
		EditText lastname = (EditText) findViewById(R.id.attendantPersonLastName);
		EditText pnmbr = (EditText) findViewById(R.id.attendantPersonNbr);
		DatabaseSQLite db = new DatabaseSQLite(getApplicationContext());
		db.open();
		Cursor c = db.getAttendantContactInfo(id);
		if (c.moveToFirst()) {
			String fName, lName;
			fName = c.getString(c.getColumnIndex(DatabaseHelper.COLUMN_ATTENDANTS_FIRSTNAME));
			lName = c.getString(c.getColumnIndex(DatabaseHelper.COLUMN_ATTENDANTS_LASTNAME));
			tv.setText(fName + " " + lName);
			firstname.setText(fName);
			lastname.setText(lName);
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
		db.close();
	}

}

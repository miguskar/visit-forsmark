package se.forsmark.visit.Booking;

import se.forsmark.visit.R;
import se.forsmark.visit.R.drawable;
import se.forsmark.visit.R.id;
import se.forsmark.visit.R.layout;
import se.forsmark.visit.R.string;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

public class EditContactActivity extends Activity {
	private int contactId;


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.contactview);
		Bundle extras = getIntent().getExtras();
		contactId = extras.getInt("contactId");
		
		initialize();
	}
	
	private void initialize() {
		// Set Title
				TextView tv = (TextView) findViewById(R.id.border_title);
				tv.setText(R.string.ContactActivityTitle);
				// Display progress
				ImageView iv = (ImageView) findViewById(R.id.border_progress);
				iv.setVisibility(View.VISIBLE); // Unhide progress
				iv.setBackgroundResource(R.drawable.border_step_one); // Display step
																		// one
				// Display hint button an
				Button b = (Button) findViewById(R.id.button_hint_top);

				b.setVisibility(View.VISIBLE); // Unhide button

				b.setOnClickListener(new OnClickListener() { // Create Toast hint

					public void onClick(View v) {
						String text = getResources().getString(
								R.string.ContactActivityToast); // Get message from
																// resources
						Toast t = Toast.makeText(getApplicationContext(), text,
								Toast.LENGTH_LONG); // Creat toast
						t.setGravity(Gravity.TOP, 0, 0); // Position
						t.show();
					}
				});
				
				//Change name of the button "next" to "save"
				Button next = (Button) findViewById(R.id.bottomNextButton);
				next.setText(getString(R.string.Save));
				
				//Fill form with latest contact info
				//TODO move this to dbsqllite class and return array with key=>value pairs
				DatabaseSQLite db = new DatabaseSQLite(getApplicationContext());
				db.open();
				Cursor c = db.getLatestContactInfo(); //FIXME ÄNDRA LOGIK, KANSKE INTE ALLTID SISTA? ID LIGGER I INTENT ELLER ATTRIBUT
				if (c.moveToFirst()) {
					EditText et = (EditText) findViewById(R.id.editTextPname);
					et.setText(c.getString(c.getColumnIndex(DatabaseHelper.COLUMN_CONTACT_FIRSTNAME)));
					et = (EditText) findViewById(R.id.editTextLName);
					et.setText(c.getString(c.getColumnIndex(DatabaseHelper.COLUMN_CONTACT_LASTNAME)));
					et = (EditText) findViewById(R.id.editTextPnmbr);
					et.setText(c.getString(c.getColumnIndex(DatabaseHelper.COLUMN_CONTACT_PNMBR)));
			
					if(c.getString(c.getColumnIndex(DatabaseHelper.COLUMN_CONTACT_SEX)).equals("male")){
						RadioButton rb=(RadioButton) findViewById(R.id.radioButtonMan);
						rb.setChecked(true);
					}
					else{
						RadioButton rb=(RadioButton) findViewById(R.id.radioButtonWoman);
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
				db.close();
	}
	
	public void bottomCancelClick(View v) {
		finish();
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
					String text = getResources().getString(
							R.string.errorMessageFieldEmpty); // Get message from
															// resources
					Toast t2 = Toast.makeText(getApplicationContext(), text,
							Toast.LENGTH_LONG); // Creat toast
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
			String text = getResources().getString(
					R.string.errorMessageGender); // Get message from
													// resources
			Toast t3 = Toast.makeText(getApplicationContext(), text,
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
		db.updateContact(contactId, firstname.getText().toString(), lastname.getText()
				.toString(), pnmbr.getText().toString(),
				rbMan.isChecked() ? "male" : "female", adress.getText()
						.toString(), postNmbr.getText().toString(), postadress
						.getText().toString(), country.getText().toString(),
				cellphone.getText().toString(), email.getText().toString(), cb.isChecked() ? 1 : 0);
		db.close();
		
		Intent i = new Intent(getApplicationContext(), AttendantsActivity.class);
		i.putExtra("displayName", firstname.getText().toString() + " " + lastname.getText().toString());
		
		setResult(RESULT_OK, i);
		finish();
		
	}
}

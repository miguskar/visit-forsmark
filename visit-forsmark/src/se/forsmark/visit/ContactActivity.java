package se.forsmark.visit;

import se.forsmark.visit.database.DatabaseSQLite;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

public class ContactActivity extends Activity {
	private int contactId;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
		getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.contactview);
		contactId = 0;

		initialize(); // Initialize views
	}
	
	@Override
	public void onPause() {
		super.onPause();
		//SharedPreferences.Editor ed;
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
		iv.setBackgroundResource(R.drawable.border_step_one); // Display step
																// one
		// Display hint button
		Button b = (Button) findViewById(R.id.button_hint_top);
		Button bnext = (Button) findViewById(R.id.bottomNextButton);

		b.setVisibility(View.VISIBLE); // Unhide button
		bnext.setVisibility(View.VISIBLE);

		b.setOnClickListener(new OnClickListener() { // Create Toast hint

			public void onClick(View v) {
				String text = getResources().getString(
						R.string.ContactActivityToast); // Get message from
														// resources
				Toast t = Toast.makeText(getApplicationContext(), text,
						Toast.LENGTH_SHORT); // Creat toast
				t.setGravity(Gravity.TOP, 0, 0); // Position
				t.show();
			}
		});
	}

	public void bottomBackClick(View v) {

	}

	public void bottomCancelClick(View v) {

	}

	public void bottomNextClick(View v) {
		Intent i = new Intent(getApplicationContext(), AttendantsActivity.class);
		i.putExtra("personCount", 5); // TODO ALLTID 5 ATM hårdkodat som fan
		startActivity(i);

		// Iterates through user input for simple VALIDATION
		LinearLayout l = (LinearLayout) findViewById(R.id.formLayout);
		for (int p = 0; p < l.getChildCount(); p++) {

			View vv = l.getChildAt(p);
			if (vv instanceof EditText) {
				EditText ed = (EditText) vv;
				if (ed.getText().toString().equals("")) {
					// TODO obs fixa ny toast!
					String text2 = getResources().getString(
							R.string.ContactActivityToast); // Get message from
															// resources
					Toast t2 = Toast.makeText(getApplicationContext(), text2,
							Toast.LENGTH_SHORT); // Creat toast
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
			// TODO fixa ny toast!!
			String text3 = getResources().getString(
					R.string.ContactActivityToast); // Get message from
													// resources
			Toast t3 = Toast.makeText(getApplicationContext(), text3,
					Toast.LENGTH_SHORT); // Creat toast
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

		DatabaseSQLite db = new DatabaseSQLite(getApplicationContext());
		db.open();
		db.addContact(firstname.getText().toString(), lastname.getText()
				.toString(), pnmbr.getText().toString(),
				rbMan.isChecked() ? "Man" : "Kvinna", adress.getText()
						.toString(), postNmbr.getText().toString(), postadress
						.getText().toString(), country.getText().toString(),
				cellphone.getText().toString(), email.getText().toString());
		int id = db.getLatestContact();
		this.contactId = id;
		// db.addContactToBooking(id); LÄGG TILL KONTAKTPERSON TILL BOKNINGEN..
		db.close();

	}

}

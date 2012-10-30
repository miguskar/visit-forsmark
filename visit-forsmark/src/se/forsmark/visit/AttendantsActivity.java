package se.forsmark.visit;

import java.util.ArrayList;
import se.forsmark.visit.database.DatabaseSQLite;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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
import android.widget.TextView;
import android.widget.Toast;

public class AttendantsActivity extends Activity {
	private String deltagare = "Deltagare ";
	private int counter = 0;
	private int nbrAttendants;
	private String bookingId = "hejhej";
	private SharedPreferences prefs;
	private ArrayList<Integer> attendantIds = new ArrayList<Integer>();

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
		bookingId = prefs.getString("bookingId", null);

		// Set Title
		TextView tv = (TextView) findViewById(R.id.border_title);
		text = deltagare + (counter + 1) + " av " + nbrAttendants;
		tv.setText(text);

		// Unhide next button
		Button b;
		if (nbrAttendants > 1) {
			b = (Button) findViewById(R.id.button_next_top);
			b.setVisibility(View.VISIBLE);
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
		CheckBox cb = (CheckBox) findViewById(R.id.checkBox1);

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
		CheckBox cb = (CheckBox) findViewById(R.id.checkBox1);

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
		// TODO get back one step
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
		// TODO starta nästa aktivitet

		/*
		 * Om det är första deltagaren, visa back-knappen Om det är näst sista
		 * deltagaren, gå till bekräfta-sidan Annars
		 */

		// Kolla om det är sista deltagaren.
		// Om ej, gå viadare till nästa och spara.
		if (counter == 0) {
			Button b = (Button) findViewById(R.id.button_back_top);
			b.setVisibility(View.VISIBLE);
		} else if (counter == nbrAttendants - 1) {
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
		 * Om det är första deltagaren visa back-knappen. Om det är sista
		 * deltagaren, dölj next-knappen.
		 */
		if (counter == 0) {
			Button b = (Button) findViewById(R.id.button_back_top);
			b.setVisibility(View.VISIBLE);
		} else if (counter == nbrAttendants - 1) {
			Button b = (Button) findViewById(R.id.button_next_top);
			b.setVisibility(View.GONE);
		}

		/*
		 * RESET FIELDS
		 */
		EditText firstname = (EditText) findViewById(R.id.attendantPersonFirstName);
		EditText lastname = (EditText) findViewById(R.id.attendantPersonLastName);
		EditText pnmbr = (EditText) findViewById(R.id.attendantPersonNbr);
		RadioButton rbMan = (RadioButton) findViewById(R.id.attendantradioButtonMan);
		RadioButton rbWoman = (RadioButton) findViewById(R.id.attendantradioButtonWoman);
		CheckBox cb = (CheckBox) findViewById(R.id.checkBox1);

		firstname.setText("");
		lastname.setText("");
		pnmbr.setText("");
		rbMan.setChecked(false);
		rbWoman.setChecked(false);
		cb.setChecked(false);
		++counter;

		// Set Title
		TextView tv = (TextView) findViewById(R.id.border_title);
		tv.setText(deltagare + (counter + 1) + " av " + nbrAttendants);
	}
	
	public void topBackButton(View v){
		
		//Validera? om man inte fyllt i allt ska vi spara iallafall eller toast? Varning.. du har inte fyllt i allt. Vill du gå tillbaka måste alla fält fyllas i för att de ska sparas....
		if(validate()){
			addOrUpdateAttendant();
		}
		else{
			new AlertDialog.Builder(this)
		    .setTitle("Rensa information")
		    .setMessage("För att aktuell deltagare ska sparas krävs att alla fält är ifyllda. Är du säker på att du vill gå vidare?")
		    .setPositiveButton("Ja", new DialogInterface.OnClickListener() {
		        public void onClick(DialogInterface dialog, int which) { 
		            // continue with moving back
		        }
		     })
		    .setNegativeButton("Nej", new DialogInterface.OnClickListener() {
		        public void onClick(DialogInterface dialog, int which) { 
		            // do nothing
		        	return;
		        }
		     })
		     .show();
		}
		
		//spara
		//spara i array.
		//hämta info från föregående och fyll i fields.
		//räkna ned counter
		//kolla vilka knappar som ska visas.
	}
}

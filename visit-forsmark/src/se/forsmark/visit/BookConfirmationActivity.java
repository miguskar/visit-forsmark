package se.forsmark.visit;

import java.util.ArrayList;
import se.forsmark.visit.database.DatabaseHelper;
import se.forsmark.visit.database.DatabaseSQLite;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class BookConfirmationActivity extends Activity {
	
	private int state;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.calenderview);

		Initialize(savedInstanceState);
	}

	public void Initialize(Bundle savedInstanceState) {
		//TODO CHANGE BUTTONS FUNCTIONS
		state = getIntent().getExtras().getInt("state");
		Button b = (Button) findViewById(R.id.backButton);
		if(state == 1){
			b.setText(getString(R.string.FrotnPage));
		}else if(state == 2){
			b.setText(getString(R.string.Back));
		}
		String bookingId = getIntent().getExtras().getString("bookingId");
		DatabaseSQLite db = new DatabaseSQLite(getApplicationContext());

		TextView dateTv = (TextView) findViewById(R.id.nameTextView);
		TextView timeTv = (TextView) findViewById(R.id.addressTextView);
		TextView orderNbrTv = (TextView) findViewById(R.id.postAddressTextView);
		TextView nameTv = (TextView) findViewById(R.id.nameTextView);
		TextView addTv = (TextView) findViewById(R.id.addressTextView);
		TextView postAddTv = (TextView) findViewById(R.id.postAddressTextView);
		TextView countryTv = (TextView) findViewById(R.id.countryTextView);
		TextView phoneTv = (TextView) findViewById(R.id.phoneTextView);
		TextView mailTv = (TextView) findViewById(R.id.emailTextView);
		TextView attNameTv = (TextView) findViewById(R.id.attendantTextView);

		db.open();
		String Date = db.getBookingDate(bookingId);
		dateTv.setText(Date);
		timeTv.setText(Date);
		orderNbrTv.setText(bookingId.toUpperCase());

		Cursor c = db.getContactInfo(bookingId);
		nameTv.setText(String.format("%s %s", 
				c.getString(c.getColumnIndex(DatabaseHelper.COLUMN_CONTACT_FIRSTNAME)),
				c.getString(c.getColumnIndex(DatabaseHelper.COLUMN_CONTACT_LASTNAME))));

		addTv.setText(c.getString(c.getColumnIndex(DatabaseHelper.COLUMN_CONTACT_ADRESS)));

		postAddTv.setText(String.format("%s %s",
				c.getString(c.getColumnIndex(DatabaseHelper.COLUMN_CONTACT_POSTADRESS)),
				c.getString(c.getColumnIndex(DatabaseHelper.COLUMN_CONTACT_POSTNMBR))));

		countryTv.setText(c.getString(c.getColumnIndex(DatabaseHelper.COLUMN_CONTACT_COUNTRY)));
		phoneTv.setText(c.getString(c.getColumnIndex(DatabaseHelper.COLUMN_CONTACT_CELLPHONE)));
		mailTv.setText(c.getString(c.getColumnIndex(DatabaseHelper.COLUMN_CONTACT_EMAIL)));
		ArrayList<Integer> att = db.getAttendantIdsFromBookingId(bookingId);
		if (att.size() <= 0) {
			LinearLayout li = (LinearLayout) findViewById(R.id.attendantsLayout);
			li.setVisibility(View.GONE);
		} else {
			for (Integer id : att) {
				attNameTv.append(db.getAttendantName(id) + "\n");
			}
		}
		db.close();
	}
	
	public void backButtonClick(View v){
		if(state == 1){
			Intent it = new Intent(getApplicationContext(), MainActivity.class);
			it.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(it);
		}else{
			finish();
		}
	}
}

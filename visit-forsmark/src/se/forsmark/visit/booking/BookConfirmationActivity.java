package se.forsmark.visit.booking;

import java.util.ArrayList;

import se.forsmark.visit.MainActivity;
import se.forsmark.visit.R;
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
	public final static int STATE_CONFIRM = 2;
	public final static int STATE_VIEW = 1;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.bookconfirmationview);

		Initialize(savedInstanceState);
	}

	public void Initialize(Bundle savedInstanceState) {
		state = getIntent().getExtras().getInt("state");
		Button b = (Button) findViewById(R.id.backButton);
		if (state == STATE_CONFIRM) {
			b.setText(getString(R.string.FrotnPage));
		} else if (state == STATE_VIEW) {
			b.setText(getString(R.string.Back));
			findViewById(R.id.alertMail).setVisibility(View.GONE);
			findViewById(R.id.alertFindThisBooking).setVisibility(View.GONE);
		}
		String bookingId = getIntent().getExtras().getString("bookingId");
		DatabaseSQLite db = new DatabaseSQLite(getApplicationContext());
		TextView titleTv = (TextView) findViewById(R.id.border_title);
		TextView dateTv = (TextView) findViewById(R.id.dateTextView);
		TextView orderNbrTv = (TextView) findViewById(R.id.bookingNumberTextView);
		TextView nameTv = (TextView) findViewById(R.id.nameTextView);
		TextView addTv = (TextView) findViewById(R.id.addressTextView);
		TextView postAddTv = (TextView) findViewById(R.id.postAddressTextView);
		TextView countryTv = (TextView) findViewById(R.id.countryTextView);
		TextView phoneTv = (TextView) findViewById(R.id.phoneTextView);
		TextView mailTv = (TextView) findViewById(R.id.emailTextView);
		TextView attNameTv = (TextView) findViewById(R.id.attendantTextView);

		titleTv.setText(getString(R.string.ConfirmedTitle));

		db.open();
		String date = db.getBookingDate(bookingId);
		dateTv.setText(String.format("%s %s - %s",date.subSequence(0, 10), date.substring(10, 16), date.substring(17, date.length())));
		orderNbrTv.setText(bookingId.toUpperCase());

		Cursor c = db.getContactInfo(bookingId);
		c.moveToFirst();
		nameTv.setText(String.format("%s %s", c.getString(c.getColumnIndex(DatabaseHelper.COLUMN_CONTACT_FIRSTNAME)),
				c.getString(c.getColumnIndex(DatabaseHelper.COLUMN_CONTACT_LASTNAME))));

		addTv.setText(c.getString(c.getColumnIndex(DatabaseHelper.COLUMN_CONTACT_ADRESS)));

		postAddTv.setText(String.format("%s %s",
				c.getString(c.getColumnIndex(DatabaseHelper.COLUMN_CONTACT_POSTNMBR)),
				c.getString(c.getColumnIndex(DatabaseHelper.COLUMN_CONTACT_POSTADRESS))));

		countryTv.setText(c.getString(c.getColumnIndex(DatabaseHelper.COLUMN_CONTACT_COUNTRY)));
		phoneTv.setText(c.getString(c.getColumnIndex(DatabaseHelper.COLUMN_CONTACT_CELLPHONE)));
		mailTv.setText(c.getString(c.getColumnIndex(DatabaseHelper.COLUMN_CONTACT_EMAIL)));
		c.close();
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

	@Override
	public void onBackPressed() {
		backButtonClick(null);
	}

	public void backButtonClick(View v) {
		if (state == STATE_CONFIRM) {
			Intent it = new Intent(getApplicationContext(), MainActivity.class);
			it.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(it);
		} else {
			finish();
		}
	}
}

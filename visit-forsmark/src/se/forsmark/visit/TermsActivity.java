package se.forsmark.visit;

import se.forsmark.visit.database.DatabaseSQLite;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

public class TermsActivity extends Activity{
	
	private static int BOOKING = 10;
	private String bookingId;
	private int seats;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.termsview);
		TextView title = (TextView) findViewById(R.id.border_title);
		title.setText(getString(R.string.termsTitle));
		Bundle extras = getIntent().getExtras();
		bookingId =  extras.getString("bookingId");
		seats = extras.getInt("seats");
		
	}
	
	protected void onActivityResult(int requestCode, int resultCode,
            Intent data) {
        if (requestCode == BOOKING) {
            if (resultCode == RESULT_CANCELED) {
            	deleteBooking();
            	setResult(RESULT_CANCELED);
                finish();
            }
        }
    }
	
	public void bottomNextClick(View v){
		Intent terms = new Intent(getApplicationContext(), ContactActivity.class);
		terms.putExtra("bookingId", bookingId);
		terms.putExtra("seats", seats);
		startActivityForResult(terms, BOOKING);
	}
	
	public void bottomCancelClick(View v){
		deleteBooking();
		finish();
	}
	
	private void deleteBooking() {
		// TODO Ta bort bokningen
		DatabaseSQLite db = new DatabaseSQLite(getApplicationContext());
		db.open();
		int cId = db.getLatestContactId();
		db.deleteBooking(bookingId, cId);
		db.close();
	}
	
	
}

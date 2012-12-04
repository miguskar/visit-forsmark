package se.forsmark.visit;

import java.util.Date;

import se.forsmark.visit.booking.BookConfirmationActivity;
import se.forsmark.visit.database.DatabaseHelper;
import se.forsmark.visit.database.DatabaseSQLite;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MyBookingsActivity extends Activity {
	Button b;
	private Button mbf; // button "Framtida Bokningar" in the list
	private Button hiddenmbf; // button used to get the right percentage when
								// "moving" mbf
	private Button mbo; // button "Gamla Bokningar" in the list
	private Button hiddenmbo; // button used to get the right percentage when
								// "moving" mbo
	int year;
	int month;
	int day;
	int hour;
	private OnClickListener ocl;
	LinearLayout l; // Layout with list of future bookings
	LinearLayout l2; // Layout with list of old bookings

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.mybookingview);
		
		//Create yellow Menu-buttons "Framtida bokningar/Gamla bokningar" add the arrow image to button"  
		((Button) findViewById(R.id.Futurebookingsbutton)).setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.arrow_show_all_events, 0);
		((Button) findViewById(R.id.Oldbookingsbutton)).setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.arrow_show_all_events, 0);
		//-------------------------------------------------------------
		initialize(); // Initialize views
	}

	public void initialize() {

		// onClickListener for each booking. -> Get to bookingConfirmation 
		ocl = new OnClickListener() {
			public void onClick(View v) {
				Intent i = new Intent(getApplicationContext(), BookConfirmationActivity.class);
				i.putExtra("bookingId", v.getTag().toString());
				i.putExtra("state", BookConfirmationActivity.STATE_VIEW);
				startActivity(i);
			}
		};
		//---------------------------------------------------------------
		
		// Set title
		TextView t = (TextView) findViewById(R.id.border_title);
		t.setText(getString(R.string.MyBookingsTitle));
		//---------------------------------------------------------------
		
		// Set List title buttons
		mbf = (Button) findViewById(R.id.Futurebookingsbutton);
		mbf.setOnClickListener(buttonHandler);

		mbo = (Button) findViewById(R.id.Oldbookingsbutton);
		mbo.setOnClickListener(buttonHandler);
		//---------------------------------------------------------------
		
		// Set Bottom Buttons
		b = (Button) findViewById(R.id.bottomBackButton);
		b.setVisibility(View.VISIBLE);
		//----------------------------------------------------------------
		
		// Get current date;
		Date date = new Date();
		year = date.getYear() + 1900;
		month = date.getMonth() + 1;
		day = date.getDate();
		hour = date.getHours();
		String datestring;
	
		if(month<10){
			datestring = "" + year + "-0" + month;
		}
		else{
			datestring = "" + year + "-" + month;
		}
		
		if(day<10){
			datestring+="-0" + day;
		}
		else{
			datestring+="-" + day;
		}
		//
		//----------------------------------------------------------------
		
		// Get a cursor of all my bookings that are finished:
		DatabaseSQLite db = new DatabaseSQLite(getApplicationContext());
		db.open();
		Cursor c = db.getAllMyBookings();
		
		l = (LinearLayout) findViewById(R.id.rlayout1);
		l2 = (LinearLayout) findViewById(R.id.rlayout2);
		String[] days = getResources().getStringArray(R.array.myBookingsDaysStringsSwe);
		String[] months = getResources().getStringArray(R.array.calMonthStringsSwe);
		
		//create all bookings buttons
		while (c.moveToNext()) {
			Log.v("date", c.getString(c.getColumnIndex(DatabaseHelper.COLUMN_BOOKING_DATE)));
			b = new Button(this);
			b.setTag(c.getString(c.getColumnIndex(DatabaseHelper.COLUMN_BOOKING_ID)));
			b.setGravity(Gravity.LEFT);
			b.setTextAppearance(getApplicationContext(), R.style.CodeFont);
			b.setTextColor(Color.BLACK);
			b.setBackgroundResource(R.drawable.daylistitem_normal);
			
			b.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.dayview_arrow, 0);
			b.setGravity(Gravity.CENTER);
			String[] datetimestr = c.getString(c.getColumnIndex(DatabaseHelper.COLUMN_BOOKING_DATE)).split(" ");
			String[] datestr = datetimestr[0].split("-");
			Date weekDay = new Date(Integer.parseInt(datestr[0]), Integer.parseInt(datestr[1]),Integer.parseInt(datestr[2]));
			Log.v("sadas", datestr[2]);
			b.setText(days[weekDay.getDay()+2] + " " + datestr[2] + " " + months[Integer.parseInt(datestr[1])-1] + "\n" + datetimestr[1] + "-" + datetimestr[2]);
			b.setOnClickListener(ocl);
			b.setVisibility(View.VISIBLE);
			// If booking is in future:
			if ((datestring.compareTo(c.getString(c.getColumnIndex(DatabaseHelper.COLUMN_BOOKING_DATE))) < 0)) {
				l.addView(b, l.getChildCount());
				// If its an old booking:
			} else {
				l2.addView(b, l2.getChildCount());
				Log.v("col_bo_date",c.getString(c.getColumnIndex(DatabaseHelper.COLUMN_BOOKING_DATE)) );
				Log.v("datestring", datestring);
			}

		}
		Log.v("child count", l2.getChildCount()+"");
		if(l2.getChildCount() == 0)
			findViewById(R.id.rlay2).setVisibility(View.GONE);
		db.close();
	}

	// Hides/unhides lists of future bookings or old bookings
	View.OnClickListener buttonHandler = new View.OnClickListener() {
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.Futurebookingsbutton:

				if (l.getVisibility() == View.VISIBLE) {
					l.setVisibility(View.GONE);
					((Button) findViewById(R.id.Futurebookingsbutton)).setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.arrow_show_no_events, 0);
				} else {
					l.setVisibility(View.VISIBLE);
					((Button) findViewById(R.id.Futurebookingsbutton)).setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.arrow_show_all_events, 0);
				}
				break;
			case R.id.Oldbookingsbutton:
				if (l2.getVisibility() == View.VISIBLE) {
					l2.setVisibility(View.GONE);
					((Button) findViewById(R.id.Oldbookingsbutton)).setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.arrow_show_no_events, 0);
				} else {
					l2.setVisibility(View.VISIBLE);
					((Button) findViewById(R.id.Oldbookingsbutton)).setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.arrow_show_all_events, 0);
				}
				break;
			}
		}
	};

	public void bottomBackClick(View v) {
		this.finish();
	}

}

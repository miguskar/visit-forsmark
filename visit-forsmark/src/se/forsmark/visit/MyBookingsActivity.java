package se.forsmark.visit;



import java.util.ArrayList;
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
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MyBookingsActivity extends Activity {
	Button b;
	private Button mbf; // button "Framtida Bokningar" in the list
	private Button hiddenmbf; //button used to get the right percentage when "moving" mbf
	private Button mbo; // button "Gamla Bokningar" in the list
	private Button hiddenmbo; //button used to get the right percentage when "moving" mbo
	int year;
	int month;
	int day;
	int hour;
	private OnClickListener ocl;
	LinearLayout l; //Layout with list of future bookings
	LinearLayout l2; // Layout with list of old bookings
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.mybookingview);
	
             	initialize(); // Initialize views
	}
	
	public void initialize(){

		ocl = new OnClickListener() {
			public void onClick(View v) {
				Intent i =new Intent(getApplicationContext(), BookConfirmationActivity.class);
				
				i.putExtra("bookingId", v.getTag().toString() ); 
				i.putExtra("state", BookConfirmationActivity.STATE_VIEW);
				startActivity(i);

			}
		};
		
		//Set title
		TextView t = (TextView) findViewById(R.id.border_title);
		t.setText(getString(R.string.MyBookingsTitle));
		//Set List title buttons
		
		mbf = (Button) findViewById(R.id.Futurebookingsbutton);
		mbf.setOnClickListener(buttonHandler);
		
		mbo = (Button) findViewById(R.id.Oldbookingsbutton);
		mbo.setOnClickListener(buttonHandler);
		
		hiddenmbf = (Button) findViewById(R.id.FuturebookingsbuttonHidden);
		hiddenmbf.setVisibility(View.INVISIBLE);
		
		hiddenmbo = (Button) findViewById(R.id.OldbookingsbuttonHidden);
		hiddenmbo.setVisibility(View.INVISIBLE);
		
		
		//Set Bottom Buttons
		b = (Button) findViewById(R.id.bottomCancelButton);
		b.setVisibility(View.GONE);
		
		b = (Button) findViewById(R.id.bottomNextButton);
		b.setVisibility(View.GONE);
		
		b = (Button) findViewById(R.id.bottomBackButton);
		b.setVisibility(View.VISIBLE);
		
		//Get current date;
		Date date=new Date();
		year=date.getYear()+1900;
		month=date.getMonth()+1;
		day=date.getDate();
		hour=date.getHours();
		String datestring =""+year+"-"+month+"-"+day;
	
		
	
		//Get a cursor of all my bookings that are finished:
		DatabaseSQLite db = new DatabaseSQLite(getApplicationContext());
		db.open();
		Cursor c=db.getAllMyBookings();
	
		l = (LinearLayout) findViewById(R.id.rlayout1);
		l2 = (LinearLayout) findViewById(R.id.rlayout2);
			
		while(c.moveToNext()){
			Log.v("date", c.getString(c.getColumnIndex(DatabaseHelper.COLUMN_BOOKING_DATE)));
			b = new Button(this);
			b.setTag(c.getString(c.getColumnIndex(DatabaseHelper.COLUMN_BOOKING_ID)));
			b.setGravity(Gravity.LEFT);
			b.setTextAppearance(getApplicationContext(), R.style.CodeFont);
			b.setTextColor(Color.WHITE);
			b.setBackgroundResource(R.drawable.editbutton);
			b.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.editbutton_arr, 0);
			b.setText(c.getString(c.getColumnIndex(DatabaseHelper.COLUMN_BOOKING_DATE)));
			b.setOnClickListener(ocl);
			b.setVisibility(View.VISIBLE);
			//If booking is in future:
			if((datestring.compareTo(c.getString(c.getColumnIndex(DatabaseHelper.COLUMN_BOOKING_DATE)))<0)){
				l.addView(b, l.getChildCount());
			//If its an old booking:
			}else{
				l2.addView(b,l2.getChildCount());
			}
		
		}
		db.close();	
	}
	
	
	//Hides/unhides lists of future bookings or old bookings
	View.OnClickListener buttonHandler = new View.OnClickListener() {
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.Futurebookingsbutton:
			
			
				if(l.getVisibility()==View.VISIBLE){
					LayoutParams params=new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.FILL_PARENT, 1.0f);
					LayoutParams params2=new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.FILL_PARENT, 2.0f);
					l.setVisibility(View.GONE);
					mbf.setLayoutParams(params);
					hiddenmbf.setLayoutParams(params2);
				}else{
					LayoutParams params=new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.FILL_PARENT, 2.0f);
					LayoutParams params2=new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.FILL_PARENT, 1.0f);
					l.setVisibility(View.VISIBLE);
					mbf.setLayoutParams(params);
					hiddenmbf.setLayoutParams(params2);
					
				}
				break;
			case R.id.Oldbookingsbutton:
				if(l2.getVisibility()==View.VISIBLE){
					l2.setVisibility(View.GONE);
				
				}else{
					l2.setVisibility(View.VISIBLE);
				}
				break;
				}
			}
	};
	
	
	
	public void bottomBackClick(View v) {
		this.finish();
	}
	


}

package se.forsmark.visit;



import java.util.Date;

import se.forsmark.visit.database.DatabaseHelper;
import se.forsmark.visit.database.DatabaseSQLite;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
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
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.mybookingview);
	
             	initialize(); // Initialize views
	}
	
	public void initialize(){
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
		
		
		//Get a cursor of all my bookings that are finished:
		DatabaseSQLite db = new DatabaseSQLite(getApplicationContext());
		db.open();
		Cursor c=db.getAllMyBookings();
		db.close();
		
		//Get current date;
		Date date=new Date();
		year=date.getYear();
		month=date.getMonth();
		day=date.getDay();
		hour=date.getHours();
		
		if(c.moveToNext()){
			Log.v("date", c.getString(c.getColumnIndex(DatabaseHelper.COLUMN_BOOKING_DATE)));
			//Check if booking is in the future
	//c.getString(c.getColumnIndex(DatabaseHelper.COLUMN_BOOKING_DATE
		
			
		
		//	if(Integer.getInteger(c.getString(c.getColumnIndex(DatabaseHelper.COLUMN_BOOKING_DATE)))>=day){
		//		b = new Button(this);
		//		b.setText(c.getString(c.getColumnIndex(DatabaseHelper)));
		//	}
			
			
			
	//	}
		}
		
	}
	
	View.OnClickListener buttonHandler = new View.OnClickListener() {
		public void onClick(View v) {
			Intent myIntent;
			switch (v.getId()) {
			case R.id.Futurebookingsbutton:
				//TODO 
				
				
				break;
			case R.id.Oldbookingsbutton:
				//TODO
				break;
				}
			
		}
	};
	
	
	
	public void bottomBackClick(View v) {
		this.finish();
	}
	


}

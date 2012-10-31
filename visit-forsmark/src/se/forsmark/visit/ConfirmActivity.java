package se.forsmark.visit;

import java.util.ArrayList;

import se.forsmark.visit.database.DatabaseHelper;
import se.forsmark.visit.database.DatabaseSQLite;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ConfirmActivity extends Activity{
	private int nbrAttendants;
	private String bookingId;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
		getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.confirmview); 
		
		
		initialize();
	}
	
	
	private void initialize(){
		// Set Title
		TextView tv = (TextView) findViewById(R.id.border_title);
		tv.setText(R.string.ConfirmationTitle);
		
		// Unhide progress and set background
		View v = findViewById(R.id.border_progress);
		v.setBackgroundResource(R.drawable.border_step_three);
		v.setVisibility(View.VISIBLE);
		Bundle extras = getIntent().getExtras();
		nbrAttendants = extras.getInt("attendantsCount");
		bookingId = extras.getString("bookingId");
		
		DatabaseSQLite db = new DatabaseSQLite(getApplicationContext());
		db.open();
		ArrayList<Integer> al = db.getAttendantIdsFromBookingId(bookingId);
		LinearLayout l = (LinearLayout)	findViewById(R.id.confirmformLayout);
		Button b;
		for(int id: al) {
			b = new Button(this);
			b.setGravity(Gravity.LEFT);
			b.setTextAppearance(getApplicationContext(), R.style.CodeFont);
			b.setTextColor(Color.WHITE);
			b.setTag(id);
			b.setText(db.getAttendantName(id));
			b.setBackgroundResource(R.drawable.editbutton);
			b.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.editbutton_arr, 0);
			b.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					editButton(v);
				}
			});
			l.addView(b, l.getChildCount()-1);
		}
		db.close();
	}
	
	public void editButton(View v) {
		//TODO ändra täxt på fergen
	}
}

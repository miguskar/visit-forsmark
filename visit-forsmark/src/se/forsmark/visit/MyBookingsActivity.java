package se.forsmark.visit;

import se.forsmark.visit.database.DatabaseHelper;
import se.forsmark.visit.database.DatabaseSQLite;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
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
		hiddenmbf.setVisibility(View.GONE);
		
		hiddenmbo = (Button) findViewById(R.id.OldbookingsbuttonHidden);
		hiddenmbo.setVisibility(View.GONE);
		
		
		//Set Bottom Buttons
		b = (Button) findViewById(R.id.bottomCancelButton);
		b.setVisibility(View.GONE);
		
		b = (Button) findViewById(R.id.bottomNextButton);
		b.setVisibility(View.GONE);
		
		b = (Button) findViewById(R.id.bottomBackButton);
		b.setVisibility(View.VISIBLE);
		
		
		//Kolla vad jag har för bokningar:
		DatabaseSQLite db = new DatabaseSQLite(getApplicationContext());
		
		
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

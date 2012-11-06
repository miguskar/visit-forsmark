package se.forsmark.visit;

import java.util.ArrayList;

import se.forsmark.visit.database.DatabaseSQLite;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ConfirmActivity extends Activity{
	private int nbrAttendants;
	private String bookingId;
	private int contactId;
	private final int EDIT_CONTACT = 1, EDIT_ATTENDANT = 2;
	private int seatsLeft;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
		getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.confirmview); 
		
		
		initialize();
		seatsLeft=10; //TODO ta bort mej
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
		contactId = extras.getInt("contactId");
		
		DatabaseSQLite db = new DatabaseSQLite(getApplicationContext());
		db.open();
		// Get contact name
		String contactName = db.getContactName(contactId);
		
		Button b = (Button) findViewById(R.id.ButtonConfirmContactPerson);
		b.setText(contactName);
		
		ArrayList<Integer> al = db.getAttendantIdsFromBookingId(bookingId);
		LinearLayout l = (LinearLayout)	findViewById(R.id.confirmformLayout);
		if(seatsLeft>0){
			b=(Button) findViewById(R.id.AddAttendantButton);
			
			}
		for(int id: al) {
			b = new Button(this);
			b.setId(id);
			b.setGravity(Gravity.LEFT);
			b.setTextAppearance(getApplicationContext(), R.style.CodeFont);
			b.setTextColor(Color.WHITE);
			b.setText(db.getAttendantName(id));
			b.setBackgroundResource(R.drawable.editbutton);
			b.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.editbutton_arr, 0);
			b.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					editButton(v);
				}
			});
			l.addView(b, l.getChildCount()-2);
		}
		db.close();
	
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			Bundle extras = data.getExtras();
			String text = extras.getString("displayName");
			if (requestCode == EDIT_CONTACT) {
				Button b = (Button) findViewById(R.id.ButtonConfirmContactPerson);
				b.setText(text);
			}else if(requestCode == EDIT_ATTENDANT) {
				int id = extras.getInt("attendantId");
				Button b = (Button) findViewById(id);
				b.setText(text);
			}
		}
	}
	
	public void editButton(View v) {
		Intent dialog = new Intent(v.getContext(), EditAttendantDialogActivity.class);
		startActivity(dialog);
		
	/*	int id = (Integer) v.getId();
		Intent i = new Intent(getApplicationContext(), EditAttendantActivity.class);
		i.putExtra("attendantId", id);
		startActivityForResult(i, EDIT_ATTENDANT);*/
	}
	
	public void editContact(View v) {
		Intent i = new Intent(getApplicationContext(), EditContactActivity.class);
		i.putExtra("contactId", contactId);
		startActivityForResult(i, EDIT_CONTACT);
	}
	
	public void bottomBackClick(View v) {
		
	}
	
	public void bottomCancelClick(View v) {
		
	}
	
	public void bottomNextClick(View v) {
		
	}
}

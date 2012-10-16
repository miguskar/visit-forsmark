package se.forsmark.visit;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ContactActivity extends Activity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
		getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.contactview);

		initialize(); // Initialize views
	}

	/**
	 * Unhides elements etc..
	 */
	private void initialize() {
		//Set Title
		TextView tv = (TextView) findViewById(R.id.border_title);
		tv.setText(R.string.ContactActivityTitle);
		//Display progress
		ImageView iv = (ImageView) findViewById(R.id.border_progress);
		iv.setVisibility(View.VISIBLE); //Unhide progress
		iv.setBackgroundResource(R.drawable.border_step_one); //Display step one
		//Display hint button
		Button b = (Button) findViewById(R.id.button_hint_top);
		b.setVisibility(View.VISIBLE); //Unhide button
		
		b.setOnClickListener(new OnClickListener() { //Create Toast hint
			
			public void onClick(View v) {
				String text = getResources().getString(R.string.ContactActivityToast); //Get message from resources
				Toast t = Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT); //Creat toast
				t.setGravity(Gravity.TOP, 0, 0); //Position
				t.show();
			}
		});
		
	}

	public void bottomBackClick(View v) {
		
	}

	public void bottomCancelClick(View v) {

	}

	public void bottomNextClick(View v) {
		Intent i = new Intent(getApplicationContext(), AttendantsActivity.class);
		i.putExtra("personCount", 5); //TODO ALLTID 5 ATM hårdkodat som fan
		startActivity(i);
	}
}

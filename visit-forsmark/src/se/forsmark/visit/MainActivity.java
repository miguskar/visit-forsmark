package se.forsmark.visit;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity {

	private Button calender;
	private Button contact;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		calender = (Button) findViewById(R.id.button1);	// Initialize button
		contact = (Button) findViewById(R.id.button2);	// Initialize button
		calender.setOnClickListener(buttonHandler);		// Set clickListener
		contact.setOnClickListener(buttonHandler);		// Set clickListener
	}

	View.OnClickListener buttonHandler = new View.OnClickListener() {
		public void onClick(View v) {
			Intent myIntent;
			switch (v.getId()) {
			case R.id.button1:
				myIntent = new Intent(v.getContext(), CalenderActivity.class);
				startActivityForResult(myIntent, 0);
				break;
			case R.id.button2:
				myIntent = new Intent(v.getContext(), ContactActivity.class);
				startActivityForResult(myIntent, 0);
				break;
			}
		}
	};
}

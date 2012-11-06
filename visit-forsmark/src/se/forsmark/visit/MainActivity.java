package se.forsmark.visit;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;

public class MainActivity extends Activity {

	private Button calender;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.main);

		calender = (Button) findViewById(R.id.button1); // Initialize button
		calender.setOnClickListener(buttonHandler); // Set clickListener
	}

	View.OnClickListener buttonHandler = new View.OnClickListener() {
		public void onClick(View v) {
			Intent myIntent;
			switch (v.getId()) {
			case R.id.button1:
				myIntent = new Intent(v.getContext(), CalenderActivity.class);
				startActivity(myIntent);
				break;
			}
		}
	};
}

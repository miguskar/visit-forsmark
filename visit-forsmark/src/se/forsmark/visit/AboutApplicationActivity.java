package se.forsmark.visit;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

public class AboutApplicationActivity extends Activity {
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.contactforsmarkview);

		Initialize(savedInstanceState);
	}

	private void Initialize(Bundle savedInstanceState) {
		TextView title = (TextView) findViewById(R.id.border_title);
		title.setText(getString(R.string.aboutUs));
	}
	
	public void bottomBackClick(View v) {
		this.finish();
	}

}

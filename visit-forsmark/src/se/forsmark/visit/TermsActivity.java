package se.forsmark.visit;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.widget.TextView;

public class TermsActivity extends Activity{
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.termsview);
		TextView title = (TextView) findViewById(R.id.border_title);
		title.setText(getString(R.string.termsTitle));
	}
}

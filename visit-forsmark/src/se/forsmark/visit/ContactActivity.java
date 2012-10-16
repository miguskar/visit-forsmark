package se.forsmark.visit;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;

public class ContactActivity extends Activity{
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
		getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.template);
		View v = View.inflate(this, R.layout.contactview, null);
		RelativeLayout l = (RelativeLayout) findViewById(R.id.activity_container);
		l.addView(v);
	}
}

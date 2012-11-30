package se.forsmark.visit;
//http://stackoverflow.com/questions/11064244/json-parsing-works-on-android-4-0-but-not-on-android-4-0
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

public class AboutActivity extends Activity {
	Button b;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.aboutview);
	
		initialize(); // Initialize views
	}
	
	public void initialize(){
		//Set title
		TextView t = (TextView) findViewById(R.id.border_title);
		t.setText(getString(R.string.aboutVisitForsmark));
		//Set info
		t = (TextView) findViewById(R.id.aboutVisitForsmarkTextView);
		t.setText(R.string.aboutVisitForsmarkLong);
	}
	
	public void bottomBackClick(View v) {
		this.finish();
	}


}

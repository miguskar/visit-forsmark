package se.forsmark.visit;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

public class TermsActivity extends Activity{
	
	private static int CONTACT_ACTIVITY = 123;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.termsview);
		TextView title = (TextView) findViewById(R.id.border_title);
		title.setText(getString(R.string.termsTitle));
	}
	
	protected void onActivityResult(int requestCode, int resultCode,
            Intent data) {
        if (requestCode == CONTACT_ACTIVITY) {
            if (resultCode == RESULT_CANCELED) {
            	setResult(RESULT_CANCELED);
                finish();
            }
        }
    }
	
	public void bottomNextClick(View v){
		Intent terms = new Intent(getApplicationContext(), TermsActivity.class);
		terms.putExtras(getIntent().getExtras());
		startActivityForResult(terms, CONTACT_ACTIVITY);
	}
	
	public void bottomCancelClick(View v){
		finish();
	}
}

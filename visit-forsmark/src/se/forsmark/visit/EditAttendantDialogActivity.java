package se.forsmark.visit;

import android.app.Activity;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

public class EditAttendantDialogActivity extends Activity {

	private int maxSeats;
	private EditText ed;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
		getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.editattendatdialog);

		Initialize(getIntent().getExtras());
	}

	public void Initialize(Bundle extras) {
		
		
	
		TextView tvDate = (TextView) findViewById(R.id.border_title);
	//	TextView tvTime = (TextView) findViewById(R.id.border_small);
	//	tvTime.setVisibility(View.VISIBLE);

		
		tvDate.setText("Ändra / Ta bort kontakt");
	//	tvTime.setText(R.string.EditAttendantDialogQ);
	
	}

 
		

	public void cancelButton(View v) {
		finish();
	}

	public void nextButton(View v) {

	}
}

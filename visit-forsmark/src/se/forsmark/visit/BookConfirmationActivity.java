package se.forsmark.visit;

import java.util.Calendar;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

public class BookConfirmationActivity extends Activity{
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.calenderview);
	
		Initialize(savedInstanceState);
	}
	
	public void Initialize(Bundle savedInstanceState) {
		
	}
}


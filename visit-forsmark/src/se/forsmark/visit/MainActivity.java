package se.forsmark.visit;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.ImageView;

public class MainActivity extends Activity {
	private Intent myIntent;
	private ImageView img, pressed;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.main);
		img = (ImageView) findViewById(R.id.mainButtonImage);
		img.setOnTouchListener(on);
	}

	OnTouchListener on = new OnTouchListener() {
		public boolean onTouch(View v, MotionEvent event) {
			int col = getColour((int) event.getX(), (int) event.getY());
			Log.v("color", col + "");
			switch (col) {

			case -1: // om appen
				pressed = (ImageView) findViewById(R.id.aboutButtonPressed);
				pressed.setVisibility(View.VISIBLE);
				myIntent = new Intent(v.getContext(),
						AboutApplicationActivity.class);
				startActivity(myIntent);
				break;
			case -15925504: // mina bokningar
				pressed = (ImageView) findViewById(R.id.myBookingsButtonPressed);
				pressed.setVisibility(View.VISIBLE);
				myIntent = new Intent(v.getContext(), MyBookingsActivity.class);
				startActivity(myIntent);
				break;
			case -16777216: // hitta hit
				pressed = (ImageView) findViewById(R.id.findFrosmarkButtonPressed);
				pressed.setVisibility(View.VISIBLE);
				myIntent = new Intent(v.getContext(),
						FindForsmarkActivity.class);
				startActivity(myIntent);
				break;
			case -65536: // Kontakta oss
				pressed = (ImageView) findViewById(R.id.contactButtonPressed);
				pressed.setVisibility(View.VISIBLE);
				myIntent = new Intent(v.getContext(),
						ContactForsmarkActivity.class);
				startActivity(myIntent);
				break;	
			case -16776961: // boka
				pressed = (ImageView) findViewById(R.id.bookingButtonPressed);
				pressed.setVisibility(View.VISIBLE);
				myIntent = new Intent(v.getContext(), CalenderActivity.class);
				startActivity(myIntent);
				break;
			case -196864: // om forsmark
				pressed = (ImageView) findViewById(R.id.aboutFrosmarkButtonPressed);
				pressed.setVisibility(View.VISIBLE);
				myIntent = new Intent(v.getContext(), AboutActivity.class);
				startActivity(myIntent);
				break;
			}
			return false;
		}
	};

	private int getColour(int x, int y) {
		ImageView img = (ImageView) findViewById(R.id.mainButtonColors);
		img.setDrawingCacheEnabled(true);
		Bitmap hotspots = Bitmap.createBitmap(img.getDrawingCache());
		img.setDrawingCacheEnabled(false);
		return hotspots.getPixel(x, y);
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (pressed != null)
			pressed.setVisibility(View.GONE);
	}
}
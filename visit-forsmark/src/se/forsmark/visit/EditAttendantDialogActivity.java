package se.forsmark.visit;

import se.forsmark.visit.database.DatabaseSQLite;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

import android.widget.TextView;

public class EditAttendantDialogActivity extends Activity {

	private final int EDIT_ATTENDANT = 2;
	private int id;
	private String name;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.editattendatdialog);

		Bundle extras = (Bundle) getIntent().getExtras();
		id = extras.getInt("attendantId");
		
		Initialize();
	}

	public void Initialize() {
		TextView tvDate = (TextView) findViewById(R.id.border_title);
		DatabaseSQLite db = new DatabaseSQLite(getApplicationContext());
		db.open();
		name = db.getAttendantName(id);
		db.close();
		tvDate.setText(name);
	}

	@Override
	public void onBackPressed() {
		setResult(RESULT_CANCELED);
		finish();
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			Intent i = new Intent(data);
			i.putExtra("edit", true);
			setResult(RESULT_OK, i);
			finish();
		}
	}

	public void editButton(View v) {
		Intent i = new Intent(getApplicationContext(), EditAttendantActivity.class);
		i.putExtra("attendantId", id);
		startActivityForResult(i, EDIT_ATTENDANT);
	}

	public void deleteButton(View v) {
		AlertDialog.Builder builder=new AlertDialog.Builder(this);
		builder.create();
		builder.setTitle("Varning");
		builder.setMessage("Är du säker på att du vill ta bort " + name + "?");
		builder.setPositiveButton("Ja", new DialogInterface.OnClickListener() {
			
			
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
			
				Intent i = new Intent(getApplicationContext(), ConfirmActivity.class);
				i.putExtra("attendantId", id);
				i.putExtra("edit", false);
				setResult(RESULT_OK, i);
				finish();
				
			}
		});
		builder.setNegativeButton("Nej", new DialogInterface.OnClickListener() {
			
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				finish();
				
			}
		});
		
		builder.show();    //b
		
	}
	
}

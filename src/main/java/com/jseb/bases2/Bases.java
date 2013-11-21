package com.jseb.bases2;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.jseb.bases2.exceptions.BasesException;
import com.jseb.bases2.listeners.FocusChangedListener;

public class Bases extends Activity {
	private static final String PREFS_NAME = "BASES_PREFERENCES";
	private static final String DIALOG_SHOWN = "DIALOG_SHOWN";
	private SharedPreferences prefs;
	private static int convert_focus;
	private MenuItem convertType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.convert_layout);

		prefs = getSharedPreferences(PREFS_NAME, 0);
		if (!prefs.getBoolean(DIALOG_SHOWN, false)){
			help();
			prefs.edit().putBoolean(DIALOG_SHOWN, true).commit();
		}

		convert_focus = 0;
		initializeViews();
    }

	public void initializeViews() {
		FocusChangedListener listener = new FocusChangedListener(this);
		((EditText) findViewById(R.id.binary_text)).setOnFocusChangeListener(listener);
		((EditText) findViewById(R.id.decimal_text)).setOnFocusChangeListener(listener);
		((EditText) findViewById(R.id.hexa_text)).setOnFocusChangeListener(listener);
		((EditText) findViewById(R.id.octa_text)).setOnFocusChangeListener(listener);
		((EditText) findViewById(R.id.custom_base)).setOnFocusChangeListener(listener);
		((EditText) findViewById(R.id.custom_base_base)).setOnFocusChangeListener(listener);
	}

	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.bases, menu);
		this.convertType = menu.findItem(R.id.menu_convert);

        return true;
    }

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()) {
			case R.id.menu_convert:
				convert();
				break;
			case R.id.menu_reset:
				clear();
				break;
			case R.id.menu_help:
				help();
				break;
		}

		return super.onOptionsItemSelected(item);
	}

	public void convert() {
		EditText bina = (EditText) findViewById(R.id.binary_text);
		EditText deca = (EditText) findViewById(R.id.decimal_text);
		EditText hexa = (EditText) findViewById(R.id.hexa_text);
		EditText octa = (EditText) findViewById(R.id.octa_text);
		EditText cust = (EditText) findViewById(R.id.custom_base);
		EditText cust_base = (EditText) findViewById(R.id.custom_base_base);

		try {
			int dec = Integer.parseInt(getFocusedField().getText().toString(), getApplicableRadix());

			bina.setText(Integer.toBinaryString(dec));
			octa.setText(Integer.toOctalString(dec));
			deca.setText(Integer.toString(dec));
			hexa.setText(Integer.toHexString(dec));
			if (!cust_base.getText().toString().equals("")) cust.setText(Integer.toString(dec, Integer.parseInt(cust_base.getText().toString())));
		} catch (NumberFormatException e) {
			Toast.makeText(this, R.string.conversion_error, Toast.LENGTH_LONG).show();
		} catch (BasesException e) {
			Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
		}
	}

	public EditText getFocusedField() throws BasesException {
		if (convert_focus == 0) {
			if (!((EditText) findViewById(R.id.binary_text)).getText().toString().equals("")) {
				convert_focus = 1;
				convertType.setTitle("Convert Bin.");
			} else if (!((EditText) findViewById(R.id.octa_text)).getText().toString().equals("")) {
				convert_focus = 2;
				convertType.setTitle("Convert Oct.");
			} else if (!((EditText) findViewById(R.id.decimal_text)).getText().toString().equals("")) {
				convert_focus = 3;
				convertType.setTitle("Convert Dec.");
			} else if (!((EditText) findViewById(R.id.hexa_text)).getText().toString().equals("")) {
				convert_focus = 4;
				convertType.setTitle("Convert Hex.");
			} else if (!((EditText) findViewById(R.id.custom_base)).getText().toString().equals("")) {
				convert_focus = 5;
				convertType.setTitle("Convert Cus.");
			}
		}

		switch(convert_focus) {
			case 0: throw new BasesException(getString(R.string.all_fields_blank_error));
			case 2: return (EditText) findViewById(R.id.octa_text);
			case 3: return (EditText) findViewById(R.id.decimal_text);
			case 4: return (EditText) findViewById(R.id.hexa_text);
			case 5: return (EditText) findViewById(R.id.custom_base);
			default: return (EditText) findViewById(R.id.binary_text);
		}
	}

	public int getApplicableRadix() throws BasesException,NumberFormatException {
		switch(convert_focus) {
			case 2: return 8;
			case 3: return 10;
			case 4: return 16;
			case 5:
				String base_text = ((EditText) findViewById(R.id.custom_base_base)).getText().toString();
				if (base_text.equals("")) throw new BasesException(getString(R.string.custom_base_blank_error));
				else return Integer.parseInt(base_text, 10);
			default: return 2;
		}
	}

	public void clear() {
		((EditText) findViewById(R.id.binary_text)).setText("");
		((EditText) findViewById(R.id.decimal_text)).setText("");
		((EditText) findViewById(R.id.hexa_text)).setText("");
		((EditText) findViewById(R.id.octa_text)).setText("");
		((EditText) findViewById(R.id.custom_base)).setText("");
		((EditText) findViewById(R.id.custom_base_base)).setText("");
		convertType.setTitle(R.string.menu_convert);
		convert_focus = 0;
	}

	public void help() {
		new AlertDialog.Builder(this).setTitle(R.string.dialog_title_text).setMessage(R.string.dialog_message_text).setNeutralButton(R.string.dialog_button_text, null).show();
	}
}

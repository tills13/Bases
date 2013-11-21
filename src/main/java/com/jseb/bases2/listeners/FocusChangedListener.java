package com.jseb.bases2.listeners;

import android.content.Context;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.EditText;

public class FocusChangedListener implements OnFocusChangeListener {
	private Context mContext;

	public FocusChangedListener(Context context) {
		this.mContext = context;
	}

	@Override
	public void onFocusChange(View v, boolean hasFocus) {
		if (hasFocus) ((EditText) v).setText("");
	}
}
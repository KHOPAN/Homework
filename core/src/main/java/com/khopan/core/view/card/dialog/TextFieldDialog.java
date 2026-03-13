package com.khopan.core.view.card.dialog;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;

public class TextFieldDialog extends Dialog {
	private final EditText editText;

	public TextFieldDialog(final Context context) {
		super(context);
		final FrameLayout frameLayout = new FrameLayout(this.context);
		this.editText = new EditText(this.context);
		frameLayout.addView(this.editText, new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
		this.dialog.setView(frameLayout);
	}

	@Override
	public CharSequence getSummary() {
		return null;
	}
}

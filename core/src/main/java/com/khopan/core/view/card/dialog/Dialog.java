package com.khopan.core.view.card.dialog;

import android.content.Context;

import androidx.appcompat.app.AlertDialog;

public abstract class Dialog {
	protected final Context context;
	protected final AlertDialog dialog;

	public Dialog(final Context context) {
		this.context = context;
		this.dialog = new AlertDialog.Builder(this.context)
				.setTitle("Hello, world!")
				.setNegativeButton("Cancel", (flp, f) -> {})
				.setPositiveButton("Done", (x, y) -> {})
				.create();
	}

	public AlertDialog getDialog() {
		return this.dialog;
	}

	public void show() {
		this.dialog.show();
	}
}

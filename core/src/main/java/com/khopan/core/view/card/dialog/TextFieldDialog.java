package com.khopan.core.view.card.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.util.TypedValue;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.FrameLayout;

public class TextFieldDialog extends Dialog {
	/**
	 * The {@link android.widget.EditText}.
	 */
	public final EditText editText;

	/**
	 * The {@link com.khopan.core.view.card.dialog.TextFieldDialog.TextFieldListener}.
	 */
	protected TextFieldListener listener;

	/**
	 * Constructs a new {@link com.khopan.core.view.card.dialog.TextFieldDialog} instance.
	 *
	 * @param context the {@link android.content.Context}.
	 */
	public TextFieldDialog(final Context context) {
		super(context);
		final FrameLayout frameLayout = new FrameLayout(this.context);
		final int padding = Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 12.0f, context.getResources().getDisplayMetrics()));
		frameLayout.setPadding(padding, 0, padding, 0);
		this.editText = new EditText(this.context);
		this.editText.setOnEditorActionListener((view, identifier, event) -> {
			if(identifier == EditorInfo.IME_ACTION_DONE) {
				this.buttonClicked(DialogInterface.BUTTON_POSITIVE);
				this.dialog.dismiss();
				return true;
			}

			return false;
		});

		this.editText.setSingleLine(true);
		frameLayout.addView(this.editText, new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
		this.dialog.setView(frameLayout);
	}

	@Override
	public CharSequence getSummary() {
		final CharSequence text = this.editText.getText();
		return text != null ? text.toString() : null;
	}

	/**
	 * @return the {@link com.khopan.core.view.card.dialog.TextFieldDialog.TextFieldListener}.
	 */
	public TextFieldListener getTextFieldListener() {
		return this.listener;
	}

	/**
	 * Sets the text.
	 *
	 * @param text the text.
	 */
	public void setText(final CharSequence text) {
		this.editText.setText(text);
		this.update();
	}

	/**
	 * Sets the {@link com.khopan.core.view.card.dialog.TextFieldDialog.TextFieldListener}.
	 *
	 * @param listener the {@link com.khopan.core.view.card.dialog.TextFieldDialog.TextFieldListener}.
	 */
	public void setTextFieldListener(final TextFieldListener listener) {
		this.listener = listener;
	}

	@Override
	public void show() {
		this.editText.requestFocus();
		final Window window = this.dialog.getWindow();

		if(window != null) {
			window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
		}

		super.show();
	}

	@Override
	protected boolean buttonClicked(final int button) {
		if(button != DialogInterface.BUTTON_POSITIVE) {
			return false;
		}

		this.update();
		return true;
	}

	private void update() {
		if(this.listener != null) {
			final CharSequence text = this.editText.getText();
			this.listener.textUpdated(this, text != null ? text.toString() : null);
		}

		this.updated();
	}

	/**
	 * A listener for handling when the text is updated.
	 */
	@FunctionalInterface
	public interface TextFieldListener {
		/**
		 * Handles the selected text.
		 *
		 * @param dialog the {@link com.khopan.core.view.card.dialog.TextFieldDialog}.
		 * @param text the selected text.
		 */
		void textUpdated(final TextFieldDialog dialog, final CharSequence text);
	}
}

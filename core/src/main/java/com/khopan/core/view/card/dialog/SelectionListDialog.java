package com.khopan.core.view.card.dialog;

import android.content.Context;
import android.widget.LinearLayout;

import androidx.recyclerview.widget.RecyclerView;

public class SelectionListDialog extends Dialog {
	protected final RecyclerView recyclerView;

	public SelectionListDialog(final Context context) {
		super(context);
		final LinearLayout linearLayout = new LinearLayout(this.context);
		linearLayout.setOrientation(LinearLayout.VERTICAL);
		this.recyclerView = new RecyclerView(this.context);
		this.dialog.setContentView(linearLayout);
	}

	@Override
	public CharSequence getSummary() {
		return null;
	}
}

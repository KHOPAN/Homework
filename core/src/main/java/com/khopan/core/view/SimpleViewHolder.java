package com.khopan.core.view;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class SimpleViewHolder<T extends View> extends RecyclerView.ViewHolder {
	public final T itemView;

	public SimpleViewHolder(@NonNull final T itemView) {
		super(itemView);
		this.itemView = itemView;
	}
}

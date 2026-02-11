package com.khopan.core.view;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * A concrete {@link androidx.recyclerview.widget.RecyclerView.ViewHolder}
 * implementation that uses generics.
 *
 * @param <T> the target {@link android.view.View} type.
 */
public class SimpleViewHolder<T extends View> extends RecyclerView.ViewHolder {
	/**
	 * The {@link android.view.View}.
	 */
	public final T itemView;

	/**
	 * Constructs a new {@link com.khopan.core.view.SimpleViewHolder} instance.
	 *
	 * @param itemView the {@link android.view.View}.
	 */
	public SimpleViewHolder(@NonNull final T itemView) {
		super(itemView);
		this.itemView = itemView;
	}
}

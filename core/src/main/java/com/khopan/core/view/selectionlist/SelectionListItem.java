package com.khopan.core.view.selectionlist;

import androidx.annotation.NonNull;

public class SelectionListItem {
	public final Object data;

	public boolean enabled;

	public SelectionListItem(final Object data) {
		this(data, true);
	}

	public SelectionListItem(final Object data, final boolean enabled) {
		this.data = data;
		this.enabled = enabled;
	}

	@NonNull
	@Override
	public String toString() {
		return String.valueOf(this.data);
	}
}

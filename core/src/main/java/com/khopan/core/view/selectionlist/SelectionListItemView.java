package com.khopan.core.view.selectionlist;

import android.animation.ValueAnimator;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.graphics.drawable.SeslRecoilDrawable;
import androidx.appcompat.widget.AppCompatSeslCheckedTextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.khopan.core.R;

import java.lang.reflect.Field;
import java.util.List;

import dev.oneuiproject.oneui.widget.CardItemView;

public class SelectionListItemView extends FrameLayout {
	private final LayoutInflater inflater;
	private final CardItemView itemView;
	private final RecyclerView recyclerView;
	private final View topDivider;
	private final View bottomDivider;
	private final Drawable dividerBackground;
	private final Adapter adapter;
	private final AlertDialog alertDialog;

	private Object[] data;
	private int selectedIndex;
	private CharSequence dialogTitle;
	private OnSelectListener listener;

	public SelectionListItemView(@NonNull final Context context) {
		this(context, null, 0);
	}

	public SelectionListItemView(@NonNull final Context context, @Nullable final AttributeSet attributeSet) {
		this(context, attributeSet, 0);
	}

	public SelectionListItemView(@NonNull final Context context, @Nullable final AttributeSet attributeSet, final int defaultStyleAttribute) {
		super(context, attributeSet, defaultStyleAttribute);
		this.inflater = LayoutInflater.from(context);
		this.itemView = new CardItemView(context, attributeSet);
		this.itemView.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
		this.addView(this.itemView);
		final FrameLayout dialogView = new FrameLayout(context);
		this.inflater.inflate(R.layout.view_selection_list_item, dialogView, true);
		this.recyclerView = dialogView.findViewById(R.id.recycler_view);
		this.topDivider = dialogView.findViewById(R.id.top_divider);
		this.bottomDivider = dialogView.findViewById(R.id.bottom_divider);
		this.dividerBackground = this.topDivider.getBackground();
		this.recyclerView.setAdapter(this.adapter = new Adapter());
		this.recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
		this.recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
			@Override
			public void onScrolled(@NonNull final RecyclerView recyclerView, final int dx, final int dy) {
				SelectionListItemView.this.updateDividers();
			}
		});

		this.alertDialog = new AlertDialog.Builder(context).setTitle(this.itemView.getTitle()).setView(dialogView).setNegativeButton("null", (dialog, which) -> dialog.cancel()).create();
		this.alertDialog.create();
		this.itemView.setOnClickListener(view -> {
			this.adapter.notifyItemRangeChanged(0, this.adapter.getItemCount());
			this.alertDialog.show();
		});

		this.updateDividers();
		this.updateSummary();
	}

	public CharSequence getButtonText() {
		return this.alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).getText();
	}

	public Object[] getData() {
		return this.data;
	}

	public CharSequence getDialogTitle() {
		return this.dialogTitle;
	}

	public OnSelectListener getOnSelectListener() {
		return this.listener;
	}

	public int getSelectedIndex() {
		return this.selectedIndex;
	}

	public boolean getShowTopDivider() {
		return this.itemView.getShowTopDivider();
	}

	public CharSequence getTitle() {
		return this.itemView.getTitle();
	}

	public CardItemView getItemView() {
		return this.itemView;
	}

	public void notifyDataSetUpdated() {
		this.notifyDataUpdated(0, this.adapter.getItemCount());
	}

	public void notifyDataUpdated(final int index) {
		this.recyclerView.post(() -> this.adapter.notifyItemChanged(index));
	}

	public void notifyDataUpdated(final int index, final int length) {
		this.recyclerView.post(() -> this.adapter.notifyItemRangeChanged(index, length));
	}

	public void setButtonText(final CharSequence buttonText) {
		this.alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setText(buttonText);
	}

	public void setData(final Object[] data) {
		this.data = data;
		this.notifyDataSetUpdated();
		this.updateSummary();
	}

	public void setDialogTitle(final CharSequence dialogTitle) {
		this.dialogTitle = dialogTitle;
		this.alertDialog.setTitle(dialogTitle == null ? this.itemView.getTitle() : dialogTitle);
	}

	public void setOnSelectListener(final OnSelectListener listener) {
		this.listener = listener;
	}

	public void setSelectedIndex(final int selectedIndex) {
		if(this.data == null || selectedIndex >= this.data.length) {
			return;
		}

		final int previousIndex = this.selectedIndex;
		this.selectedIndex = selectedIndex;
		this.updateSummary();

		if(this.listener != null) {
			this.listener.onSelect(this.selectedIndex);
		}

		this.recyclerView.post(() -> {
			this.adapter.notifyItemChanged(previousIndex, 0);
			this.adapter.notifyItemChanged(this.selectedIndex, 0);
		});

		this.alertDialog.cancel();
	}

	public void setShowTopDivider(final boolean showTopDivider) {
		this.itemView.setShowTopDivider(showTopDivider);
	}

	public void setTitle(final CharSequence title) {
		this.itemView.setTitle(title);
	}

	private void updateDividers() {
		this.topDivider.setBackground(this.recyclerView.canScrollVertically(-1) ? this.dividerBackground : null);
		this.bottomDivider.setBackground(this.recyclerView.canScrollVertically(1) ? this.dividerBackground : null);
	}

	private void updateSummary() {
		if(this.data != null && this.selectedIndex < this.data.length) {
			this.itemView.setSummary(String.valueOf(SelectionListItemView.this.data[this.selectedIndex]));
		}
	}

	private class Adapter extends RecyclerView.Adapter<ViewHolder> {
		private final Field field;

		private Adapter() {
			Field field;

			try {
				field = SeslRecoilDrawable.class.getDeclaredField("mAnimator");
				field.setAccessible(true);
			} catch(final Throwable ignored) {
				field = null;
			}

			this.field = field;
		}

		@Override
		public int getItemCount() {
			return SelectionListItemView.this.data == null ? 0 : SelectionListItemView.this.data.length;
		}

		@Override
		public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
			this.bind(holder, position, false);
		}

		@Override
		public void onBindViewHolder(@NonNull final ViewHolder holder, final int position, @NonNull final List<Object> payloads) {
			this.bind(holder, position, !payloads.isEmpty());
		}

		@NonNull
		@Override
		public ViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, final int viewType) {
			final View view = SelectionListItemView.this.inflater.inflate(R.layout.view_selection_list_item_entry, parent, false);
			return new ViewHolder(view, view.findViewById(R.id.checkmark), view.findViewById(R.id.text_view));
		}

		private void bind(final ViewHolder holder, final int index, final boolean animate) {
			if(SelectionListItemView.this.data == null || index >= SelectionListItemView.this.data.length) {
				return;
			}

			final boolean enabled = !(SelectionListItemView.this.data[index] instanceof SelectionListItem) || ((SelectionListItem) SelectionListItemView.this.data[index]).enabled;
			holder.itemView.setEnabled(enabled);
			holder.itemView.setOnClickListener(view -> SelectionListItemView.this.setSelectedIndex(holder.getBindingAdapterPosition()));
			holder.checkbox.setChecked(SelectionListItemView.this.selectedIndex == index);
			holder.checkbox.setEnabled(enabled);

			if(!animate) {
				holder.checkbox.jumpDrawablesToCurrentState();
			}

			holder.textView.setEnabled(enabled);
			holder.textView.setText(String.valueOf(SelectionListItemView.this.data[index]));
			final Drawable foreground = holder.itemView.getForeground();

			if(animate || !(foreground instanceof SeslRecoilDrawable)) {
				return;
			}

			final ValueAnimator animator;

			try {
				animator = (ValueAnimator) this.field.get(foreground);
			} catch(final Throwable ignored) {
				return;
			}

			if(animator != null) {
				animator.cancel();
				animator.setFloatValues(0.0f, 0.0f);
				animator.setCurrentFraction(0.0f);
			}
		}
	}

	private static class ViewHolder extends RecyclerView.ViewHolder {
		private final AppCompatSeslCheckedTextView checkbox;
		private final TextView textView;

		private ViewHolder(@NonNull final View itemView, final AppCompatSeslCheckedTextView checkbox, final TextView textView) {
			super(itemView);
			this.checkbox = checkbox;
			this.textView = textView;
		}
	}
}

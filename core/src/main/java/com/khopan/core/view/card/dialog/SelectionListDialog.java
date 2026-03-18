package com.khopan.core.view.card.dialog;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.khopan.core.CoreLayout;
import com.khopan.core.view.SimpleViewHolder;
import com.khopan.core.view.card.CheckableCardView;

public class SelectionListDialog extends Dialog {
	protected final RecyclerView recyclerView;
	protected final View bottomDividerView;
	protected final View topDividerView;

	protected Adapter adapter;

	private final int itemHeight;
	private final RecyclerViewAdapter recyclerViewAdapter;

	@SuppressLint("PrivateResource")
	public SelectionListDialog(final Context context) {
		super(context);
		final Resources resources = context.getResources();
		final DisplayMetrics metrics = resources.getDisplayMetrics();
		this.itemHeight = Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 56.0f, metrics));
		final LinearLayout linearLayout = new LinearLayout(this.context);
		linearLayout.setOrientation(LinearLayout.VERTICAL);
		linearLayout.setPadding(0, Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 12.0f, metrics)), 0, Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 16.0f, metrics)));
		this.topDividerView = new View(this.context);
		final Resources.Theme theme = context.getTheme();
		final TypedValue value = new TypedValue();
		theme.resolveAttribute(androidx.appcompat.R.attr.listDividerColor, value, true);
		final int backgroundColor = resources.getColor(value.resourceId, theme);
		this.topDividerView.setBackgroundColor(backgroundColor);
		final int dividerHeight = resources.getDimensionPixelSize(androidx.appcompat.R.dimen.sesl_list_divider_height);
		final LinearLayout.LayoutParams topDividerViewParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dividerHeight);
		final int margin = Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20.0f, metrics));
		topDividerViewParams.leftMargin = margin;
		topDividerViewParams.rightMargin = margin;
		linearLayout.addView(this.topDividerView, topDividerViewParams);
		this.recyclerView = new RecyclerView(this.context);
		this.recyclerView.setAdapter(this.recyclerViewAdapter = new RecyclerViewAdapter());
		this.recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
		this.recyclerView.setPadding(margin, 0, margin, 0);
		this.recyclerView.setScrollBarStyle(View.SCROLLBARS_OUTSIDE_OVERLAY);
		CoreLayout.forceEnableScrollbars(this.recyclerView, false, true);
		final LinearLayout.LayoutParams recyclerViewParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0);
		recyclerViewParams.weight = 1.0f;
		linearLayout.addView(this.recyclerView, recyclerViewParams);
		this.bottomDividerView = new View(this.context);
		this.bottomDividerView.setBackgroundColor(backgroundColor);
		final LinearLayout.LayoutParams bottomDividerViewParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dividerHeight);
		bottomDividerViewParams.leftMargin = margin;
		bottomDividerViewParams.rightMargin = margin;
		linearLayout.addView(this.bottomDividerView, bottomDividerViewParams);
		this.dialog.setView(linearLayout);
	}

	public Adapter getAdapter() {
		return this.adapter;
	}

	@Override
	public CharSequence getSummary() {
		return null;
	}

	@SuppressLint("NotifyDataSetChanged")
	public void setAdapter(final Adapter adapter) {
		this.adapter = adapter;
		this.recyclerViewAdapter.notifyDataSetChanged();
	}

	private class RecyclerViewAdapter extends RecyclerView.Adapter<SimpleViewHolder<CheckableCardView>> {
		@Override
		public int getItemCount() {
			return SelectionListDialog.this.adapter == null ? 0 : SelectionListDialog.this.adapter.getItemCount();
		}

		@Override
		public void onBindViewHolder(@NonNull final SimpleViewHolder<CheckableCardView> holder, final int position) {
			holder.itemView.resetForegroundState();

			if(SelectionListDialog.this.adapter == null) {
				return;
			}

			final boolean state = SelectionListDialog.this.adapter.getCheckboxState(position);
			holder.itemView.setCheckboxState(state);
			holder.itemView.setEnabled(SelectionListDialog.this.adapter.getState(position));
			holder.itemView.setOnClickListener(view -> SelectionListDialog.this.adapter.setCheckboxState(holder.getBindingAdapterPosition(), !state));
			holder.itemView.setSummary(SelectionListDialog.this.adapter.getSummary(position));
			holder.itemView.setTitle(SelectionListDialog.this.adapter.getTitle(position));
		}

		@NonNull
		@Override
		public SimpleViewHolder<CheckableCardView> onCreateViewHolder(@NonNull final ViewGroup parent, final int viewType) {
			final CheckableCardView cardView = new CheckableCardView(parent.getContext());
			cardView.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
			cardView.setCheckboxVisible(true);
			cardView.setTopDividerVisible(false);
			cardView.constraintLayout.getLayoutParams().height = SelectionListDialog.this.itemHeight;
			cardView.constraintLayout.setPadding(0, 0, 0, 0);
			return new SimpleViewHolder<>(cardView);
		}
	}

	public interface Adapter {
		boolean getCheckboxState(final int index);
		int getItemCount();
		String getTitle(final int index);
		void setCheckboxState(final int index, final boolean state);

		default boolean getState(final int index) {
			return true;
		}

		default String getSummary(final int index) {
			return null;
		}
	}
}

package com.khopan.core.view.card.dialog;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.khopan.core.CoreLayout;
import com.khopan.core.view.SimpleViewHolder;
import com.khopan.core.view.card.CardView;
import com.khopan.core.view.card.CheckableCardView;

public class SelectionListDialog extends Dialog {
	protected final Drawable dividerBackground;
	protected final RecyclerView recyclerView;
	protected final View bottomDivider;
	protected final View topDivider;

	/**
	 * Constructs a new {@link SelectionListDialog}.
	 *
	 * @param context the {@link Context}.
	 */
	public SelectionListDialog(final Context context) {
		super(context);
		final Resources.Theme theme = context.getTheme();
		final TypedValue value = new TypedValue();
		theme.resolveAttribute(androidx.appcompat.R.attr.listDividerColor, value, true);
		this.dividerBackground = AppCompatResources.getDrawable(context, value.resourceId);
		final LinearLayout linearLayout = new LinearLayout(context);
		linearLayout.setOrientation(LinearLayout.VERTICAL);
		final Resources resources = context.getResources();
		final DisplayMetrics metrics = resources.getDisplayMetrics();
		linearLayout.setPadding(0, Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 16.0f, metrics)), 0, Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 12.0f, metrics)));
		this.topDivider = new View(context);
		@SuppressLint("PrivateResource")
		final int dividerHeight = resources.getDimensionPixelSize(androidx.appcompat.R.dimen.sesl_list_divider_height);
		final LinearLayout.LayoutParams topDividerParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dividerHeight);
		final int margin = Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20.0f, metrics));
		topDividerParams.leftMargin = margin;
		topDividerParams.rightMargin = margin;
		linearLayout.addView(this.topDivider, topDividerParams);
		this.recyclerView = new RecyclerView(context);
		this.recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
			@Override
			public void onScrolled(@NonNull final RecyclerView recyclerView, final int dx, final int dy) {
				SelectionListDialog.this.updateDividers();
			}
		});

		this.recyclerView.setAdapter(new Adapter());
		this.recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
		this.recyclerView.setPadding(margin, 0, margin, 0);
		this.recyclerView.setScrollBarStyle(ViewGroup.SCROLLBARS_OUTSIDE_OVERLAY);
		CoreLayout.forceEnableScrollbars(this.recyclerView, false, true);
		final LinearLayout.LayoutParams recyclerViewParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0);
		recyclerViewParams.weight = 1.0f;
		linearLayout.addView(this.recyclerView, recyclerViewParams);
		this.bottomDivider = new View(context);
		final LinearLayout.LayoutParams bottomDividerParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dividerHeight);
		bottomDividerParams.leftMargin = margin;
		bottomDividerParams.rightMargin = margin;
		linearLayout.addView(this.bottomDivider, bottomDividerParams);
		this.dialog.setView(linearLayout);
		this.updateDividers();
	}

	@Override
	protected void buildDialog(final AlertDialog.Builder builder) {
		super.buildDialog(builder);
		builder.setPositiveButton(null, null);
	}

	private void updateDividers() {
		this.topDivider.setBackground(this.recyclerView.canScrollVertically(-1) ? this.dividerBackground : null);
		this.bottomDivider.setBackground(this.recyclerView.canScrollVertically(1) ? this.dividerBackground : null);
	}

	private class Adapter extends RecyclerView.Adapter<SimpleViewHolder<CardView>> {
		@Override
		public int getItemCount() {
			return 1000;
		}

		@Override
		public void onBindViewHolder(@NonNull final SimpleViewHolder<CardView> holder, final int position) {
			holder.itemView.resetForegroundState();
			holder.itemView.setTitle("Motor");
		}

		@NonNull
		@Override
		public SimpleViewHolder<CardView> onCreateViewHolder(@NonNull final ViewGroup parent, final int viewType) {
			final CheckableCardView view = new CheckableCardView(parent.getContext());
			view.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
			view.setCheckboxVisible(true);
			view.setTopDividerVisible(false);
			view.constraintLayout.getLayoutParams().height = Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 56.0f, SelectionListDialog.this.context.getResources().getDisplayMetrics()));
			view.constraintLayout.setPadding(0, 0, 0, 0);
			return new SimpleViewHolder<>(view);
		}
	}
}

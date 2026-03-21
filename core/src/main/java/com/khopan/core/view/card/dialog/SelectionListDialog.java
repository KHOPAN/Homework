package com.khopan.core.view.card.dialog;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.khopan.core.CoreLayout;
import com.khopan.core.R;
import com.khopan.core.view.SimpleViewHolder;
import com.khopan.core.view.card.CheckableCardView;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * A {@link com.khopan.core.view.card.dialog.Dialog}
 * that displays a selection list. It uses
 * {@link com.khopan.core.view.card.dialog.SelectionListDialog.Adapter}
 * to display the data.
 */
public class SelectionListDialog extends Dialog {
	/**
	 * The {@link androidx.recyclerview.widget.RecyclerView}.
	 */
	protected final RecyclerView recyclerView;

	/**
	 * The bottom divider view.
	 */
	protected final View bottomDividerView;

	/**
	 * The top divider view.
	 */
	protected final View topDividerView;

	/**
	 * The {@link com.khopan.core.view.card.dialog.SelectionListDialog.Adapter}
	 * to provide the data.
	 */
	protected Adapter adapter;

	/**
	 * The {@link com.khopan.core.view.card.dialog.SelectionListDialog.SelectionListListener}.
	 */
	protected SelectionListListener listener;

	private final Drawable dividerBackground;
	private final int itemHeight;
	private final RecyclerViewAdapter recyclerViewAdapter;
	private final Set<Integer> updated;

	private boolean multiple;
	private int selected;
	private int size;

	/**
	 * Constructs a new {@link com.khopan.core.view.card.dialog.SelectionListDialog} instance.
	 *
	 * @param context the {@link android.content.Context}.
	 */
	@SuppressLint("PrivateResource")
	public SelectionListDialog(final Context context) {
		super(context);
		this.updated = new HashSet<>();
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
		this.topDividerView.setBackgroundColor(resources.getColor(value.resourceId, theme));
		this.dividerBackground = this.topDividerView.getBackground();
		final int dividerHeight = resources.getDimensionPixelSize(androidx.appcompat.R.dimen.sesl_list_divider_height);
		final LinearLayout.LayoutParams topDividerViewParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dividerHeight);
		final int margin = Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20.0f, metrics));
		topDividerViewParams.leftMargin = margin;
		topDividerViewParams.rightMargin = margin;
		linearLayout.addView(this.topDividerView, topDividerViewParams);
		this.recyclerView = new RecyclerView(this.context);
		this.recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
			@Override
			public void onScrolled(@NonNull final RecyclerView recyclerView, final int dx, final int dy) {
				SelectionListDialog.this.updateDividers();
			}
		});

		this.recyclerView.setAdapter(this.recyclerViewAdapter = new RecyclerViewAdapter());
		this.recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
		this.recyclerView.setPadding(margin, 0, margin, 0);
		this.recyclerView.setScrollBarStyle(View.SCROLLBARS_OUTSIDE_OVERLAY);
		CoreLayout.forceEnableScrollbars(this.recyclerView, false, true);
		final LinearLayout.LayoutParams recyclerViewParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0);
		recyclerViewParams.weight = 1.0f;
		linearLayout.addView(this.recyclerView, recyclerViewParams);
		this.bottomDividerView = new View(this.context);
		final LinearLayout.LayoutParams bottomDividerViewParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dividerHeight);
		bottomDividerViewParams.leftMargin = margin;
		bottomDividerViewParams.rightMargin = margin;
		linearLayout.addView(this.bottomDividerView, bottomDividerViewParams);
		this.updateDividers();
		this.dialog.setView(linearLayout);
	}

	/**
	 * @return the {@link com.khopan.core.view.card.dialog.SelectionListDialog.Adapter}.
	 */
	public Adapter getAdapter() {
		return this.adapter;
	}

	/**
	 * @return the {@link com.khopan.core.view.card.dialog.SelectionListDialog.SelectionListListener}.
	 */
	public SelectionListListener getSelectionListListener() {
		return this.listener;
	}

	@Override
	public CharSequence getSummary() {
		return this.adapter == null ? null : this.adapter.getCardSummary(this.selected);
	}

	/**
	 * Sets the {@link com.khopan.core.view.card.dialog.SelectionListDialog.Adapter}.
	 *
	 * @param adapter the {@link com.khopan.core.view.card.dialog.SelectionListDialog.Adapter}.
	 */
	@SuppressLint("NotifyDataSetChanged")
	public void setAdapter(final Adapter adapter) {
		this.adapter = adapter;

		if(this.adapter == null) {
			this.recyclerViewAdapter.notifyDataSetChanged();
			return;
		}

		this.multiple = this.adapter.isMultiple();
		this.size = this.adapter.getItemCount();
		this.recyclerViewAdapter.notifyDataSetChanged();
		this.dialog.setButton(DialogInterface.BUTTON_POSITIVE, this.multiple ? this.context.getString(R.string.dialog_button_positive) : null, this.multiple ? this.dialogListener : null);
	}

	/**
	 * Sets the {@link com.khopan.core.view.card.dialog.SelectionListDialog.SelectionListListener}.
	 *
	 * @param listener the {@link com.khopan.core.view.card.dialog.SelectionListDialog.SelectionListListener}.
	 */
	public void setSelectionListListener(final SelectionListListener listener) {
		this.listener = listener;
	}

	@Override
	public void show() {
		if(this.multiple) {
			this.updated.forEach(this.recyclerViewAdapter::notifyItemChanged);
			this.updated.clear();
		}

		super.show();
	}

	@Override
	protected boolean buttonClicked(int button) {
		if(button != DialogInterface.BUTTON_POSITIVE) {
			return false;
		}

		this.updated.forEach(index -> this.adapter.setCheckboxState(index, !this.adapter.getCheckboxState(index)));
		this.updated.clear();

		if(this.listener != null) {
			this.listener.itemSelected(this);
		}

		this.updated();
		return true;
	}

	private void updateDividers() {
		this.topDividerView.setBackground(this.recyclerView.canScrollVertically(-1) ? this.dividerBackground : null);
		this.bottomDividerView.setBackground(this.recyclerView.canScrollVertically(1) ? this.dividerBackground : null);
	}

	private class RecyclerViewAdapter extends RecyclerView.Adapter<SimpleViewHolder<CheckableCardView>> {
		@Override
		public int getItemCount() {
			return SelectionListDialog.this.size;
		}

		@Override
		public int getItemViewType(final int position) {
			return SelectionListDialog.this.multiple ? CheckableCardView.CHECKBOX_TYPE_MULTIPLE : CheckableCardView.CHECKBOX_TYPE_SINGLE;
		}

		@Override
		public void onBindViewHolder(@NonNull final SimpleViewHolder<CheckableCardView> holder, final int position) {
			this.onBindViewHolder(holder, position, List.of());
		}

		@Override
		public void onBindViewHolder(@NonNull final SimpleViewHolder<CheckableCardView> holder, final int position, @NonNull final List<Object> payloads) {
			if(payloads.isEmpty()) {
				holder.itemView.resetForegroundState();
			}

			if(SelectionListDialog.this.adapter == null) {
				return;
			}

			final boolean state = SelectionListDialog.this.multiple ? SelectionListDialog.this.adapter.getCheckboxState(position) ^ SelectionListDialog.this.updated.contains(position) : SelectionListDialog.this.selected == position;
			holder.itemView.setCheckboxState(state);
			holder.itemView.setEnabled(SelectionListDialog.this.adapter.getState(position));
			holder.itemView.setOnClickListener(view -> {
				final int index = holder.getBindingAdapterPosition();

				if(SelectionListDialog.this.multiple) {
					if(SelectionListDialog.this.updated.contains(position)) {
						SelectionListDialog.this.updated.remove(position);
					} else {
						SelectionListDialog.this.updated.add(position);
					}

					this.notifyItemChanged(index, 0);
					return;
				}

				final int previousIndex = SelectionListDialog.this.selected;
				this.notifyItemChanged(SelectionListDialog.this.selected = index);
				this.notifyItemChanged(previousIndex);
				SelectionListDialog.this.adapter.setCheckboxState(previousIndex, false);
				SelectionListDialog.this.adapter.setCheckboxState(SelectionListDialog.this.selected, true);
				SelectionListDialog.this.buttonClicked(DialogInterface.BUTTON_POSITIVE);
				SelectionListDialog.this.dialog.dismiss();
			});

			holder.itemView.setSummary(SelectionListDialog.this.adapter.getSummary(position));
			holder.itemView.setTitle(SelectionListDialog.this.adapter.getTitle(position));
		}

		@NonNull
		@Override
		public SimpleViewHolder<CheckableCardView> onCreateViewHolder(@NonNull final ViewGroup parent, final int viewType) {
			final CheckableCardView cardView = new CheckableCardView(parent.getContext());
			cardView.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
			cardView.setCheckboxType(viewType);
			cardView.setTopDividerVisible(false);
			cardView.constraintLayout.getLayoutParams().height = SelectionListDialog.this.itemHeight;
			cardView.constraintLayout.setPadding(0, 0, 0, 0);
			return new SimpleViewHolder<>(cardView);
		}
	}

	/**
	 * An interface that defines the data source.
	 */
	public interface Adapter {
		/**
		 * @param selected the selected item.
		 * @return the card summary based on the selected item.
		 */
		CharSequence getCardSummary(final int selected);

		/**
		 * @param index the item index.
		 * @return the checkbox state at the specified index.
		 */
		boolean getCheckboxState(final int index);

		/**
		 * @param index the item index.
		 * @return the title at the specified index.
		 */
		CharSequence getTitle(final int index);

		/**
		 * @return the total item count.
		 */
		int getItemCount();

		/**
		 * Sets the checkbox state at the specified index.
		 *
		 * @param index the item index.
		 * @param state the checkbox state.
		 */
		void setCheckboxState(final int index, final boolean state);

		/**
		 * @param index the item index.
		 * @return true if the item at the specified index is enabled, false otherwise.
		 */
		default boolean getState(final int index) {
			return true;
		}

		/**
		 * @param index the item index.
		 * @return the summary at the specified index.
		 */
		default CharSequence getSummary(final int index) {
			return null;
		}

		/**
		 * @return true if multiple items can be selected, false otherwise.
		 */
		default boolean isMultiple() {
			return false;
		}
	}

	/**
	 * A listener for handling when items are selected.
	 */
	@FunctionalInterface
	public interface SelectionListListener {
		/**
		 * Handles the selected items.
		 *
		 * @param dialog the {@link com.khopan.core.view.card.dialog.SelectionListDialog}.
		 */
		void itemSelected(final SelectionListDialog dialog);
	}

	/**
	 * A {@link com.khopan.core.view.card.dialog.SelectionListDialog.Adapter}
	 * based on a simple, immutable list.
	 */
	public static class SimpleListAdapter implements Adapter {
		/**
		 * True if multiple items can be selected, false otherwise.
		 */
		public final boolean multiple;

		/**
		 * The array containing whether the item at a specific index
		 * is selected or not.
		 */
		public final boolean[] selected;

		/**
		 * The total size.
		 */
		public final int size;

		/**
		 * The item list.
		 */
		public final Object[] items;

		/**
		 * The text to be displayed when nothing is selected.
		 */
		public CharSequence emptyText;

		/**
		 * Constructs a new {@link com.khopan.core.view.card.dialog.SelectionListDialog.SimpleListAdapter} instance.
		 *
		 * @param multiple true if multiple items can be selected, false otherwise.
		 * @param items the item list.
		 */
		public SimpleListAdapter(final boolean multiple, final Object... items) {
			this.multiple = multiple;
			this.size = items == null ? 0 : items.length;
			this.selected = new boolean[this.size];
			this.items = items;
		}

		@Override
		public CharSequence getCardSummary(final int selected) {
			if(!this.multiple) {
				return this.getTitle(selected);
			}

			final StringBuilder builder = new StringBuilder();

			for(int i = 0; i < this.size; i++) {
				if(this.getCheckboxState(i)) {
					if(builder.length() > 0) {
						builder.append(", ");
					}

					builder.append(this.getTitle(i));
				}
			}

			return builder.length() == 0 && this.emptyText != null ? this.emptyText : builder.toString();
		}

		@Override
		public boolean getCheckboxState(final int index) {
			return this.selected[index];
		}

		@Override
		public CharSequence getTitle(final int index) {
			return this.items[index] instanceof CharSequence ? (CharSequence) this.items[index] : String.valueOf(this.items[index]);
		}

		@Override
		public int getItemCount() {
			return this.size;
		}

		@Override
		public boolean getState(final int index) {
			return !(this.items[index] instanceof StateProvider) || ((StateProvider) this.items[index]).getState();
		}

		@Override
		public CharSequence getSummary(final int index) {
			return this.items[index] instanceof SummaryProvider ? ((SummaryProvider) this.items[index]).getSummary() : null;
		}

		@Override
		public boolean isMultiple() {
			return this.multiple;
		}

		@Override
		public void setCheckboxState(final int index, final boolean state) {
			this.selected[index] = state;
		}

		/**
		 * An interface to be implemented by each item to provide its state.
		 */
		@FunctionalInterface
		public interface StateProvider {
			/**
			 * @return true if this item is enabled, false otherwise.
			 */
			boolean getState();
		}

		/**
		 * An interface to be implemented by each item to provide its summary.
		 */
		@FunctionalInterface
		public interface SummaryProvider {
			/**
			 * @return this item's summary.
			 */
			CharSequence getSummary();
		}
	}
}

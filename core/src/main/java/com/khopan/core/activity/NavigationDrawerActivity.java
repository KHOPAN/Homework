package com.khopan.core.activity;

import android.graphics.Typeface;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.slidingpanelayout.widget.SlidingPaneLayout;

import com.khopan.core.CoreLayout;
import com.khopan.core.R;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import dev.oneuiproject.oneui.layout.DrawerLayout;
import dev.oneuiproject.oneui.layout.NavDrawerLayout;
import dev.oneuiproject.oneui.layout.ToolbarLayout;
import dev.oneuiproject.oneui.utils.TypefaceUtilsKt;

public abstract class NavigationDrawerActivity extends FragmentedActivity {
	protected final List<DrawerEntry> drawerItems;

	private Adapter adapter;

	protected NavigationDrawerActivity() {
		this.drawerItems = new ArrayList<>();
		this.useDrawerLayout = true;
	}

	@Override
	public void onCreate(@Nullable final Bundle bundle) {
		super.onCreate(bundle);
		this.drawerLayout.setNavigationButtonIcon(AppCompatResources.getDrawable(this, R.drawable.icon_drawer));
		final RecyclerView recyclerView = new RecyclerView(this);
		final ToolbarLayout.ToolbarLayoutParams recyclerViewParams = new ToolbarLayout.ToolbarLayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
		recyclerViewParams.layoutLocation = DrawerLayout.DRAWER_PANEL;
		recyclerView.setLayoutParams(recyclerViewParams);
		recyclerView.setAdapter(this.adapter = new Adapter());
		recyclerView.setHasFixedSize(true);
		recyclerView.setItemAnimator(null);
		recyclerView.setLayoutManager(new LinearLayoutManager(this));
		CoreLayout.forceEnableScrollbars(recyclerView, false, true);

		try {
			final Method getContainerLayoutMethod = NavDrawerLayout.class.getDeclaredMethod("getContainerLayout$oneui_design_release");
			getContainerLayoutMethod.setAccessible(true);
			final Object containerLayout = getContainerLayoutMethod.invoke(NavigationDrawerActivity.this.drawerLayout);

			if(containerLayout instanceof SlidingPaneLayout) {
				((SlidingPaneLayout) containerLayout).addPanelSlideListener(new SlidingPaneLayout.PanelSlideListener() {
					@Override
					public void onPanelClosed(@NonNull final View view) {}

					@Override
					public void onPanelOpened(@NonNull final View view) {}

					@Override
					public void onPanelSlide(@NonNull final View view, final float time) {
						final DisplayMetrics metrics = NavigationDrawerActivity.this.getResources().getDisplayMetrics();
						final float iconSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 44.0f, metrics);
						NavigationDrawerActivity.this.adapter.notifyItemRangeChanged(0, NavigationDrawerActivity.this.drawerItems.size(), new Payload(time, (int) (time * (((SlidingPaneLayout) containerLayout).seslGetPreferredDrawerPixelSize() - TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 85.0f / 3.0f, metrics) - iconSize) + iconSize)));
					}
				});
			}
		} catch(final Throwable ignored) {}

		this.drawerLayout.addView(recyclerView);
	}

	protected void setSelectedItem(final int position) {
		this.adapter.selectItem(this.drawerItems.get(position), position);
	}

	protected void onDrawerSelected(@NonNull final DrawerEntry entry, final boolean changed) {
		if(changed && entry.fragment != null) {
			this.setFragment(entry.fragment);
		}

		this.drawerLayout.setDrawerOpen(false, true);
	}

	private int getPixelSize(final float size) {
		return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, size, this.getResources().getDisplayMetrics()));
	}

	public static class DrawerEntry {
		public @DrawableRes final int icon;
		public final String text;
		public final Fragment fragment;

		private DrawerEntry(@DrawableRes final int icon, @Nullable final String text, @Nullable final Fragment fragment) {
			this.icon = icon;
			this.text = text;
			this.fragment = fragment;
		}

		public static @NonNull DrawerEntry create(@DrawableRes final int icon, @Nullable final String text, @Nullable final Fragment fragment) {
			return new DrawerEntry(icon, text, fragment);
		}
	}

	private class Adapter extends RecyclerView.Adapter<ViewHolder> {
		private final Typeface normal;
		private final Typeface selected;

		private int selectedItem;

		private Adapter() {
			this.normal = TypefaceUtilsKt.getRegularFont();
			this.selected = TypefaceUtilsKt.getSemiBoldFont();
			this.selectedItem = -1;
			this.setHasStableIds(true);
		}

		@Override
		public int getItemCount() {
			return NavigationDrawerActivity.this.drawerItems.size();
		}

		@Override
		public long getItemId(final int position) {
			return position;
		}

		@Override
		public int getItemViewType(final int position) {
			return NavigationDrawerActivity.this.drawerItems.get(position) == null ? ViewHolder.VIEW_TYPE_SEPARATOR : ViewHolder.VIEW_TYPE_DRAWER_ITEM;
		}

		@Override
		public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
			if(holder.iconView == null || holder.textView == null) {
				return;
			}

			final DrawerEntry entry = NavigationDrawerActivity.this.drawerItems.get(position);
			final boolean selected = this.selectedItem == position;
			holder.iconView.setImageResource(entry.icon);
			holder.textView.setEllipsize(selected ? TextUtils.TruncateAt.MARQUEE : TextUtils.TruncateAt.END);
			holder.itemView.setOnClickListener(view -> this.selectItem(entry, holder.getBindingAdapterPosition()));
			holder.itemView.setSelected(selected);
			holder.textView.setText(entry.text);
			holder.textView.setTypeface(selected ? this.selected : this.normal);
			final float time = NavigationDrawerActivity.this.drawerLayout.getDrawerOffset();
			holder.textView.setAlpha(time);
			final DisplayMetrics metrics = NavigationDrawerActivity.this.getResources().getDisplayMetrics();
			final float iconSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 44.0f, metrics);

			try {
				final Method getContainerLayoutMethod = NavDrawerLayout.class.getDeclaredMethod("getContainerLayout$oneui_design_release");
				getContainerLayoutMethod.setAccessible(true);
				final Object containerLayout = getContainerLayoutMethod.invoke(NavigationDrawerActivity.this.drawerLayout);

				if(containerLayout instanceof SlidingPaneLayout) {
					final ViewGroup.LayoutParams params = holder.itemView.getLayoutParams();
					params.width = (int) (time * (((SlidingPaneLayout) containerLayout).seslGetPreferredDrawerPixelSize() - TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 85.0f / 3.0f, metrics) - iconSize) + iconSize);
					holder.itemView.setLayoutParams(params);
				}
			} catch(final Throwable ignored) {}
		}

		@Override
		public void onBindViewHolder(@NonNull final ViewHolder holder, final int position, @NonNull final List<Object> payloads) {
			if(payloads.isEmpty()) {
				this.onBindViewHolder(holder, position);
				return;
			}

			for(final Object payload : payloads) {
				if(!(payload instanceof Payload)) {
					continue;
				}

				final Payload value = (Payload) payload;
				holder.textView.setAlpha(value.time);
				final ViewGroup.LayoutParams params = holder.itemView.getLayoutParams();
				params.width = value.width;
				holder.itemView.setLayoutParams(params);
			}
		}

		@NonNull
		@Override
		public ViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, final int type) {
			return new ViewHolder(parent, type != ViewHolder.VIEW_TYPE_DRAWER_ITEM);
		}

		private void selectItem(@NonNull final DrawerEntry entry, final int position) {
			if(this.selectedItem == position) {
				NavigationDrawerActivity.this.onDrawerSelected(entry, false);
				return;
			}

			final int previousItem = this.selectedItem;
			this.selectedItem = position;
			this.notifyItemChanged(previousItem);
			this.notifyItemChanged(this.selectedItem);
			NavigationDrawerActivity.this.onDrawerSelected(entry, true);
		}
	}

	private static class Payload {
		private final float time;
		private final int width;

		private Payload(final float time, final int width) {
			this.time = time;
			this.width = width;
		}
	}

	private class ViewHolder extends RecyclerView.ViewHolder {
		private static final int VIEW_TYPE_SEPARATOR = 0;
		private static final int VIEW_TYPE_DRAWER_ITEM = 1;

		private final AppCompatImageView iconView;
		private final TextView textView;

		private ViewHolder(@NonNull final ViewGroup parent, final boolean separator) {
			super(separator ? new View(NavigationDrawerActivity.this) : LayoutInflater.from(NavigationDrawerActivity.this).inflate(R.layout.drawer_item, parent, false));

			if(!separator) {
				this.iconView = this.itemView.findViewById(R.id.drawer_item_icon);
				this.textView = this.itemView.findViewById(R.id.drawer_item_text);
				return;
			}

			this.iconView = null;
			this.textView = null;
			final ViewGroup.MarginLayoutParams parameters = new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, NavigationDrawerActivity.this.getPixelSize(3.0f));
			parameters.setMarginStart(NavigationDrawerActivity.this.getPixelSize(24.0f));
			parameters.setMarginEnd(NavigationDrawerActivity.this.getPixelSize(24.0f));
			parameters.topMargin = parameters.bottomMargin = NavigationDrawerActivity.this.getPixelSize(6.0f);
			this.itemView.setLayoutParams(parameters);
			this.itemView.setForeground(AppCompatResources.getDrawable(NavigationDrawerActivity.this, R.drawable.drawer_separator));
		}
	}
}

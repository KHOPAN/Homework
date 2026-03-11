package com.khopan.core.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.widget.ImageViewCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.slidingpanelayout.widget.SlidingPaneLayout;

import com.khopan.core.CoreLayout;
import com.khopan.core.R;
import com.khopan.core.drawable.ResizableDrawable;
import com.khopan.core.view.SimpleViewHolder;
import com.khopan.core.view.card.CardView;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import dev.oneuiproject.oneui.layout.DrawerLayout;
import dev.oneuiproject.oneui.layout.NavDrawerLayout;
import dev.oneuiproject.oneui.layout.ToolbarLayout;
import dev.oneuiproject.oneui.utils.TypefaceUtilsKt;

public abstract class NavigationDrawerActivity extends FragmentedActivity {
	private static final int VIEW_TYPE_DRAWER_ITEM = 0;
	private static final int VIEW_TYPE_DIVIDER = 1;

	protected final List<DrawerEntry> drawerItems;

	private final Typeface normalTypeface;
	private final Typeface selectedTypeface;

	private Adapter adapter;
	private boolean largeScreen;
	private ColorStateList itemTint;
	private float itemIconSize;
	private float time;
	private int dividerHeight;
	private int dividerMarginHorizontal;
	private int itemMarginHorizontal;
	private int itemPadding;
	private int marginVertical;
	private int selectedItem;

	public NavigationDrawerActivity() {
		this.drawerItems = new ArrayList<>();
		this.normalTypeface = TypefaceUtilsKt.getRegularFont();
		this.selectedTypeface = TypefaceUtilsKt.getSemiBoldFont();
		this.selectedItem = 0;
	}

	@SuppressLint("PrivateResource")
	@Override
	public void onCreate(@Nullable final Bundle bundle) {
		super.onCreate(bundle);
		this.largeScreen = ((NavDrawerLayout) this.toolbarLayout).isLargeScreenMode();
		this.itemTint = this.getColorStateList(dev.oneuiproject.oneui.design.R.color.oui_des_primary_text_color);
		this.itemIconSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 44.0f, this.metrics);
		this.dividerHeight = Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 3.0f, this.metrics));
		this.dividerMarginHorizontal = Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24.0f, this.metrics));
		this.itemMarginHorizontal = Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 14.0f, this.metrics));
		this.itemPadding = Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10.0f, this.metrics));
		this.marginVertical = Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 6.0f, this.metrics));
		this.toolbarLayout.setNavigationButtonIcon(AppCompatResources.getDrawable(this, R.drawable.icon_drawer));
		final RecyclerView recyclerView = new RecyclerView(this);
		final ToolbarLayout.ToolbarLayoutParams recyclerViewParams = new ToolbarLayout.ToolbarLayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
		recyclerViewParams.layoutLocation = DrawerLayout.DRAWER_PANEL;
		recyclerView.setLayoutParams(recyclerViewParams);
		recyclerView.setAdapter(this.adapter = new Adapter());
		recyclerView.setItemAnimator(null);
		recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
		CoreLayout.forceEnableScrollbars(recyclerView, false, true);
		this.toolbarLayout.addView(recyclerView);

		try {
			final Method getContainerLayoutMethod = NavDrawerLayout.class.getDeclaredMethod("getContainerLayout$oneui_design_release");
			getContainerLayoutMethod.setAccessible(true);
			final Object containerLayout = getContainerLayoutMethod.invoke(NavigationDrawerActivity.this.toolbarLayout);

			if(containerLayout instanceof SlidingPaneLayout) {
				((SlidingPaneLayout) containerLayout).addPanelSlideListener(this.adapter);
			}
		} catch(final Throwable ignored) {}
	}

	@Override
	protected ToolbarLayout createToolbarLayout() {
		return (ToolbarLayout) this.getLayoutInflater().inflate(R.layout.layout_navigation_drawer, this.findViewById(android.R.id.content), false);
	}

	protected void onDrawerSelected(@NonNull final DrawerEntry entry, final boolean changed) {
		if(changed) {
			this.setFragment(entry.fragment);
		}

		if(!this.largeScreen) {
			((NavDrawerLayout) this.toolbarLayout).setDrawerOpen(false, true);
		}
	}

	protected void setSelectedItem(final int position) {
		if(position < 0 || position >= this.drawerItems.size()) {
			return;
		}

		final DrawerEntry entry = this.drawerItems.get(position);

		if(entry == null) {
			return;
		}

		if(this.selectedItem == position) {
			this.onDrawerSelected(entry, false);
			return;
		}

		final int previousItem = this.selectedItem;
		this.selectedItem = position;
		final DrawerEntry previousEntry = this.drawerItems.get(previousItem);

		if(previousEntry != null) {
			previousEntry.transition = true;
		}

		entry.transition = true;
		this.adapter.notifyItemChanged(previousItem);
		this.adapter.notifyItemChanged(this.selectedItem);
		this.onDrawerSelected(entry, true);
	}

	public static class DrawerEntry {
		public @DrawableRes final int icon;
		public final String title;
		public final String summary;
		public final Fragment fragment;

		private Drawable drawable;
		private boolean transition;

		private DrawerEntry(@DrawableRes final int icon, final String title, final String summary, final Fragment fragment) {
			this.icon = icon;
			this.title = title;
			this.summary = summary;
			this.fragment = fragment;
		}

		public static DrawerEntry create(@DrawableRes final int icon, final String title, final Fragment fragment) {
			return new DrawerEntry(icon, title, null, fragment);
		}

		public static DrawerEntry create(@DrawableRes final int icon, final String title, final String summary, final Fragment fragment) {
			return new DrawerEntry(icon, title, summary, fragment);
		}
	}

	private class Adapter extends RecyclerView.Adapter<SimpleViewHolder<View>> implements SlidingPaneLayout.PanelSlideListener {
		@Override
		public int getItemCount() {
			return NavigationDrawerActivity.this.drawerItems.size();
		}

		@Override
		public int getItemViewType(final int position) {
			return NavigationDrawerActivity.this.drawerItems.get(position) == null ? NavigationDrawerActivity.VIEW_TYPE_DIVIDER : NavigationDrawerActivity.VIEW_TYPE_DRAWER_ITEM;
		}

		@Override
		public void onBindViewHolder(@NonNull final SimpleViewHolder<View> holder, final int position) {
			this.applyTime(holder, NavigationDrawerActivity.this.time);
			final DrawerEntry entry = NavigationDrawerActivity.this.drawerItems.get(position);

			if(entry == null) {
				return;
			}

			final boolean selected = NavigationDrawerActivity.this.selectedItem == position;
			holder.itemView.setOnClickListener(view -> NavigationDrawerActivity.this.setSelectedItem(holder.getBindingAdapterPosition()));
			holder.itemView.setSelected(selected);
			((CardView) holder.itemView).setIcon(entry.drawable == null ? entry.drawable = AppCompatResources.getDrawable(NavigationDrawerActivity.this, entry.icon) : entry.drawable);
			((CardView) holder.itemView).setSummary(entry.summary);
			((CardView) holder.itemView).setTitle(entry.title);
			((CardView) holder.itemView).titleView.setEllipsize(selected ? TextUtils.TruncateAt.MARQUEE : TextUtils.TruncateAt.END);
			((CardView) holder.itemView).titleView.setTypeface(selected ? NavigationDrawerActivity.this.selectedTypeface : NavigationDrawerActivity.this.normalTypeface);

			if(!entry.transition) {
				((CardView) holder.itemView).resetForegroundState();
				((ResizableDrawable) holder.itemView.getBackground()).drawable.jumpToCurrentState();
			}

			entry.transition = false;
		}

		@Override
		public void onBindViewHolder(@NonNull final SimpleViewHolder<View> holder, final int position, @NonNull final List<Object> payloads) {
			if(payloads.isEmpty()) {
				this.onBindViewHolder(holder, position);
				return;
			}

			for(final Object value : payloads) {
				if(value instanceof Float) {
					this.applyTime(holder, (float) value);
				}
			}
		}

		@NonNull
		@Override
		public SimpleViewHolder<View> onCreateViewHolder(@NonNull final ViewGroup parent, final int viewType) {
			final Context context = parent.getContext();

			if(viewType != NavigationDrawerActivity.VIEW_TYPE_DRAWER_ITEM) {
				final View view = new View(context);
				final ViewGroup.MarginLayoutParams viewParams = new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, NavigationDrawerActivity.this.dividerHeight);
				viewParams.bottomMargin = NavigationDrawerActivity.this.marginVertical;
				viewParams.leftMargin = NavigationDrawerActivity.this.dividerMarginHorizontal;
				viewParams.rightMargin = NavigationDrawerActivity.this.dividerMarginHorizontal;
				viewParams.topMargin = NavigationDrawerActivity.this.marginVertical;
				view.setLayoutParams(viewParams);
				view.setForeground(new ResizableDrawable(NavigationDrawerActivity.this, R.drawable.drawer_separator));
				return new SimpleViewHolder<>(view);
			}

			final CardView cardView = new CardView(context);
			final RecyclerView.LayoutParams cardViewParams = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
			cardViewParams.bottomMargin = NavigationDrawerActivity.this.marginVertical;
			cardViewParams.leftMargin = NavigationDrawerActivity.this.itemMarginHorizontal;
			cardViewParams.rightMargin = NavigationDrawerActivity.this.itemMarginHorizontal;
			cardViewParams.topMargin = NavigationDrawerActivity.this.marginVertical;
			cardView.setLayoutParams(cardViewParams);
			cardView.setBackground(new ResizableDrawable(NavigationDrawerActivity.this, R.drawable.drawer_selector));
			cardView.setTopDividerVisible(false);
			cardView.constraintLayout.setForeground(new ResizableDrawable(NavigationDrawerActivity.this, R.drawable.drawer_ripple));
			cardView.constraintLayout.setPadding(0, 0, 0, 0);
			final ConstraintLayout.LayoutParams iconViewParams = (ConstraintLayout.LayoutParams) cardView.iconView.getLayoutParams();
			iconViewParams.width = iconViewParams.height = Math.round(NavigationDrawerActivity.this.itemIconSize);
			cardView.iconView.setAlpha(0.75f);
			ImageViewCompat.setImageTintList(cardView.iconView, NavigationDrawerActivity.this.itemTint);
			cardView.iconView.setPadding(NavigationDrawerActivity.this.itemPadding, NavigationDrawerActivity.this.itemPadding, NavigationDrawerActivity.this.itemPadding, NavigationDrawerActivity.this.itemPadding);
			cardView.iconView.setScaleType(ImageView.ScaleType.FIT_XY);
			cardView.spacerView.getLayoutParams().height = NavigationDrawerActivity.this.itemPadding;
			((ConstraintLayout.LayoutParams) cardView.titleView.getLayoutParams()).topMargin = NavigationDrawerActivity.this.itemPadding;
			return new SimpleViewHolder<>(cardView);
		}

		@Override
		public void onPanelClosed(@NonNull final View view) {}

		@Override
		public void onPanelOpened(@NonNull final View view) {}

		@Override
		public void onPanelSlide(@NonNull final View view, final float time) {
			if(NavigationDrawerActivity.this.largeScreen) {
				this.notifyItemRangeChanged(0, NavigationDrawerActivity.this.drawerItems.size(), NavigationDrawerActivity.this.time = time);
			}
		}

		private void applyTime(final SimpleViewHolder<View> holder, final float time) {
			if(!NavigationDrawerActivity.this.largeScreen) {
				return;
			}

			final int itemWidth = holder.itemView.getWidth();
			final int direction = holder.itemView.getLayoutDirection();

			if(!(holder.itemView instanceof CardView)) {
				final float dividerWidth = NavigationDrawerActivity.this.itemIconSize - (NavigationDrawerActivity.this.dividerMarginHorizontal - NavigationDrawerActivity.this.itemMarginHorizontal) * 2.0f;
				final ResizableDrawable foreground = (ResizableDrawable) holder.itemView.getForeground();
				foreground.width = Math.round(time * (itemWidth - dividerWidth) + dividerWidth);
				foreground.x = direction * (itemWidth - foreground.width);
				foreground.update();
				return;
			}

			((CardView) holder.itemView).summaryView.setAlpha(time);
			((CardView) holder.itemView).titleView.setAlpha(time);
			final ResizableDrawable background = (ResizableDrawable) holder.itemView.getBackground();
			background.width = Math.round(time * (itemWidth - NavigationDrawerActivity.this.itemIconSize) + NavigationDrawerActivity.this.itemIconSize);
			background.x = direction * (itemWidth - background.width);
			background.update();
			final ResizableDrawable foreground = (ResizableDrawable) ((CardView) holder.itemView).constraintLayout.getForeground();
			foreground.x = background.x;
			foreground.width = background.width;
			foreground.update();
		}
	}
}

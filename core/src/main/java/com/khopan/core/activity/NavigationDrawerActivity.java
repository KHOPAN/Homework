package com.khopan.core.activity;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.graphics.drawable.SeslRecoilDrawable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.slidingpanelayout.widget.SlidingPaneLayout;

import com.khopan.core.CoreLayout;
import com.khopan.core.R;
import com.khopan.core.view.SimpleViewHolder;
import com.khopan.core.view.card.CardView;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import dev.oneuiproject.oneui.layout.DrawerLayout;
import dev.oneuiproject.oneui.layout.NavDrawerLayout;
import dev.oneuiproject.oneui.layout.ToolbarLayout;
import dev.oneuiproject.oneui.utils.TypefaceUtilsKt;

public abstract class NavigationDrawerActivity extends FragmentedActivity {
	protected final List<DrawerEntry> drawerItems;

	private final Adapter adapter;

	public NavigationDrawerActivity() {
		this.drawerItems = new ArrayList<>();
		this.adapter = new Adapter();
	}

	@Override
	public void onCreate(@Nullable final Bundle bundle) {
		super.onCreate(bundle);
		this.toolbarLayout.setNavigationButtonIcon(AppCompatResources.getDrawable(this, R.drawable.icon_drawer));
		final RecyclerView recyclerView = new RecyclerView(this);
		final ToolbarLayout.ToolbarLayoutParams recyclerViewParams = new ToolbarLayout.ToolbarLayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
		recyclerViewParams.layoutLocation = DrawerLayout.DRAWER_PANEL;
		recyclerView.setLayoutParams(recyclerViewParams);
		recyclerView.setAdapter(this.adapter);
		recyclerView.setHasFixedSize(true);
		recyclerView.setItemAnimator(null);
		recyclerView.setLayoutManager(new LinearLayoutManager(this ,LinearLayoutManager.VERTICAL, false));
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
	}

	protected void setSelectedItem(final int position) {
		//this.adapter.selectItem(this.drawerItems.get(position), position);
	}

	public static class DrawerEntry {
		public @DrawableRes final int icon;
		public final String text;
		public final Fragment fragment;

		private DrawerEntry(@DrawableRes final int icon, final String text, final Fragment fragment) {
			this.icon = icon;
			this.text = text;
			this.fragment = fragment;
		}

		public static DrawerEntry create(@DrawableRes final int icon, final String text, final Fragment fragment) {
			return new DrawerEntry(icon, text, fragment);
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

	private static class ViewHolder extends RecyclerView.ViewHolder {
		private ViewHolder(@NonNull final View itemView) {
			super(itemView);
		}
	}

	private class Adapter extends RecyclerView.Adapter<SimpleViewHolder<CardView>> implements SlidingPaneLayout.PanelSlideListener {
		private float time;

		private Adapter() {

		}

		@Override
		public int getItemCount() {
			return 100;
		}

		@Override
		public void onBindViewHolder(@NonNull final SimpleViewHolder<CardView> holder, final int position) {
			holder.itemView.setTitle("Hello, world!");
			holder.itemView.setTopDividerVisible(false);
			holder.itemView.constraintLayout.setForeground(new ResizableDrawable(R.drawable.drawer_ripple));
			holder.itemView.setBackground(new ResizableDrawable(R.drawable.drawer_selector));
			holder.itemView.setIcon(AppCompatResources.getDrawable(NavigationDrawerActivity.this, NavigationDrawerActivity.this.drawerItems.get(0).icon));
			holder.itemView.setOnClickListener(view -> {
				holder.itemView.setSelected(!holder.itemView.isSelected());
			});

			this.applyTime(holder, this.time);
		}

		@Override
		public void onBindViewHolder(@NonNull final SimpleViewHolder<CardView> holder, final int position, @NonNull final List<Object> payloads) {
			if(payloads.isEmpty()) {
				this.onBindViewHolder(holder, position);
				return;
			}

			for(final Object value : payloads) {
				if(!(value instanceof Payload)) {
					continue;
				}

				final Payload payload = (Payload) value;
				this.applyTime(holder, payload.time);
				((ResizableDrawable) holder.itemView.getBackground()).setWidth(payload.width);
				((ResizableDrawable) holder.itemView.constraintLayout.getForeground()).setWidth(payload.width);
			}
		}

		@NonNull
		@Override
		public SimpleViewHolder<CardView> onCreateViewHolder(@NonNull final ViewGroup parent, final int viewType) {
			final CardView cardView = new CardView(parent.getContext());
			final RecyclerView.LayoutParams cardViewParams = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
			final int horizontal = Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 14.0f, NavigationDrawerActivity.this.metrics));
			final int vertical = Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 6.0f, NavigationDrawerActivity.this.metrics));
			cardViewParams.bottomMargin = vertical;
			cardViewParams.leftMargin = horizontal;
			cardViewParams.rightMargin = horizontal;
			cardViewParams.topMargin = vertical;
			cardView.setLayoutParams(cardViewParams);
			return new SimpleViewHolder<>(cardView);
		}

		@Override
		public void onPanelClosed(@NonNull final View view) {}

		@Override
		public void onPanelOpened(@NonNull final View view) {}

		@Override
		public void onPanelSlide(@NonNull final View view, final float time) {
			this.time = time;
			//final float iconSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 44.0f, metrics);
			this.notifyItemRangeChanged(0, this.getItemCount(), /*new Payload(time, (int) (time * (((SlidingPaneLayout) containerLayout).seslGetPreferredDrawerPixelSize() - TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 85.0f / 3.0f, metrics) - iconSize) + iconSize))*/new Payload(time, 100));
		}

		private void applyTime(final SimpleViewHolder<CardView> holder, final float time) {
			holder.itemView.titleView.setAlpha(time);
		}
	}

	private class ResizableDrawable extends Drawable implements Drawable.Callback {
		private final Drawable drawable;

		private int width;

		private ResizableDrawable(final int drawable) {
			this.drawable = Objects.requireNonNull(AppCompatResources.getDrawable(NavigationDrawerActivity.this, drawable));
			this.drawable.setCallback(this);
			this.width = -1;
		}

		@Override
		public void draw(@NonNull final Canvas canvas) {
			this.drawable.draw(canvas);
		}

		@Override
		public int getIntrinsicHeight() {
			return this.drawable.getIntrinsicHeight();
		}

		@Override
		public int getIntrinsicWidth() {
			return this.width < 0 ? this.drawable.getIntrinsicWidth() : this.width;
		}

		@Override
		public int getOpacity() {
			return this.drawable.getOpacity();
		}

		@Override
		public void invalidateDrawable(@NonNull final Drawable drawable) {
			this.invalidateSelf();
		}

		@Override
		public boolean isStateful() {
			return this.drawable.isStateful();
		}

		@Override
		public void scheduleDrawable(@NonNull final Drawable drawable, @NonNull final Runnable runnable, final long time) {
			this.scheduleSelf(runnable, time);
		}

		@Override
		public void setAlpha(final int alpha) {
			this.drawable.setAlpha(alpha);
		}

		@Override
		public void setColorFilter(@Nullable final ColorFilter colorFilter) {
			this.drawable.setColorFilter(colorFilter);
		}

		@Override
		public void unscheduleDrawable(@NonNull final Drawable drawable, @NonNull final Runnable runnable) {
			this.unscheduleSelf(runnable);
		}

		@Override
		protected void onBoundsChange(@NonNull final Rect bounds) {
			this.drawable.setBounds(bounds.left, bounds.top, this.width < 0 ? bounds.right : (this.width - bounds.left), bounds.bottom);
		}

		@Override
		protected boolean onLevelChange(final int level) {
			return this.drawable.setLevel(level);
		}

		@Override
		protected boolean onStateChange(@NonNull final int[] state) {
			final boolean changed = this.drawable.setState(state);

			if(changed) {
				this.invalidateSelf();
			}

			return changed;
		}

		private void setWidth(final int width) {
			this.width = width;
			this.drawable.setBounds(0, 0, this.width, this.drawable.getBounds().bottom);
		}
	}

	/*private class Adapter extends RecyclerView.Adapter<ViewHolder> {
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

			if(((NavDrawerLayout) NavigationDrawerActivity.this.toolbarLayout).isLargeScreenMode()) {
				final float time = ((NavDrawerLayout) NavigationDrawerActivity.this.toolbarLayout).getDrawerOffset();
				holder.textView.setAlpha(time);
			}

			final DisplayMetrics metrics = NavigationDrawerActivity.this.getResources().getDisplayMetrics();
			final float iconSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 44.0f, metrics);

			if(!((NavDrawerLayout) NavigationDrawerActivity.this.toolbarLayout).isLargeScreenMode()) {
				return;
			}

			try {
				final Method getContainerLayoutMethod = NavDrawerLayout.class.getDeclaredMethod("getContainerLayout$oneui_design_release");
				getContainerLayoutMethod.setAccessible(true);
				final Object containerLayout = getContainerLayoutMethod.invoke(NavigationDrawerActivity.this.toolbarLayout);

				if(containerLayout instanceof SlidingPaneLayout) {
					final ViewGroup.LayoutParams params = holder.itemView.getLayoutParams();
					final float time = ((NavDrawerLayout) NavigationDrawerActivity.this.toolbarLayout).getDrawerOffset();
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
	}*/
}

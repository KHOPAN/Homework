package com.khopan.homework;

import android.graphics.Typeface;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.khopan.homework.fragment.HomeworkFragment;
import com.khopan.homework.fragment.SettingsFragment;
import com.khopan.homework.fragment.TestFragment;
import com.sec.sesl.khopan.homework.R;

import java.util.ArrayList;
import java.util.List;

import dev.oneuiproject.oneui.layout.DrawerLayout;
import dev.oneuiproject.oneui.layout.ToolbarLayout;

public class HomeworkApplication extends AppCompatActivity {
	private static final Typeface TYPEFACE_NORMAL = Typeface.create("sec-roboto-light", Typeface.NORMAL);
	private static final Typeface TYPEFACE_SELECTED = Typeface.create("sec-roboto-light", Typeface.BOLD);

	private final List<AbstractFragment> fragments;
	private final List<AbstractFragment> drawerItems;

	private DisplayMetrics metrics;
	private DrawerLayout drawerLayout;

	public HomeworkApplication() {
		final HomeworkFragment fragmentHomework = new HomeworkFragment();
		final SettingsFragment fragmentSettings = new SettingsFragment();
		final TestFragment fragmentTest = new TestFragment();
		this.fragments = new ArrayList<>();
		this.fragments.add(fragmentHomework);
		this.fragments.add(fragmentSettings);
		this.fragments.add(fragmentTest);
		this.drawerItems = new ArrayList<>();
		this.drawerItems.add(fragmentHomework);
		this.drawerItems.add(null);
		this.drawerItems.add(fragmentSettings);
		this.drawerItems.add(null);
		this.drawerItems.add(fragmentTest);

		for(int i = 0; i < 50; i++) {
			this.drawerItems.add(null);
			this.drawerItems.add(fragmentHomework);
		}
	}

	private int getPixelSize(final float size) {
		return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, size, this.metrics));
	}

	@Override
	public void onCreate(@Nullable final Bundle bundle) {
		super.onCreate(bundle);
		this.metrics = this.getResources().getDisplayMetrics();
		this.drawerLayout = new DrawerLayout(this, null);
		this.drawerLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
		this.drawerLayout.setDrawerButtonIcon(this.getDrawable(R.drawable.ic_oui_info_outline));
		this.drawerLayout.setDrawerButtonOnClickListener(null); // TODO: Starts a new about activity
		this.drawerLayout.setExpandable(false);
		this.drawerLayout.setExpanded(false);
		final FrameLayout frameLayout = new FrameLayout(this);
		frameLayout.setLayoutParams(new ToolbarLayout.ToolbarLayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 0));
		final int identifier = View.generateViewId();
		frameLayout.setId(identifier);
		this.drawerLayout.addView(frameLayout);
		final RecyclerView recyclerView = new RecyclerView(this);
		recyclerView.setLayoutParams(new ToolbarLayout.ToolbarLayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 5));
		recyclerView.setAdapter(new Adapter());
		recyclerView.setClipToPadding(false);
		recyclerView.setHasFixedSize(true);
		recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
		recyclerView.setPadding(0, 0, 0, this.getPixelSize(200.0f));
		this.drawerLayout.addView(recyclerView);
		this.setContentView(this.drawerLayout);
		final FragmentTransaction transaction = this.getSupportFragmentManager().beginTransaction();
		this.fragments.forEach(fragment -> transaction.add(identifier, fragment));
		this.selectFragment(transaction, 0).commitNow();
	}

	private FragmentTransaction selectFragment(final FragmentTransaction transaction, final int index) {
		this.fragments.forEach(transaction::hide);
		final AbstractFragment fragment = this.drawerItems.get(index);
		transaction.show(fragment);
		this.drawerLayout.setTitle(this.getString(fragment.name));
		return transaction;
	}

	private class Adapter extends RecyclerView.Adapter<ViewHolder> {
		private int selectedItem;

		private Adapter() {
			this.setHasStableIds(true);
		}

		@Override
		public int getItemCount() {
			return HomeworkApplication.this.drawerItems.size();
		}

		@Override
		public long getItemId(final int position) {
			return position;
		}

		@Override
		public int getItemViewType(final int position) {
			return HomeworkApplication.this.drawerItems.get(position) == null ? ViewHolder.VIEW_TYPE_SEPARATOR : ViewHolder.VIEW_TYPE_DRAWER_ITEM;
		}

		@Override
		public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
			if(holder.iconView == null || holder.textView == null) {
				return;
			}

			final boolean selected = this.selectedItem == position;
			final AbstractFragment fragment = HomeworkApplication.this.drawerItems.get(position);
			holder.iconView.setImageResource(fragment.icon);
			holder.textView.setEllipsize(selected ? TextUtils.TruncateAt.MARQUEE : TextUtils.TruncateAt.END);
			holder.textView.setText(HomeworkApplication.this.getString(fragment.name));
			holder.textView.setTypeface(selected ? HomeworkApplication.TYPEFACE_SELECTED : HomeworkApplication.TYPEFACE_NORMAL);
			holder.itemView.setOnClickListener(view -> {
				final int selectedItem = holder.getBindingAdapterPosition();

				if(this.selectedItem == selectedItem) {
					HomeworkApplication.this.drawerLayout.setDrawerOpen(false, true);
					return;
				}

				this.notifyItemChanged(this.selectedItem);
				this.selectedItem = selectedItem;
				this.notifyItemChanged(selectedItem);
				HomeworkApplication.this.selectFragment(HomeworkApplication.this.getSupportFragmentManager().beginTransaction(), position).commit();
				HomeworkApplication.this.drawerLayout.setDrawerOpen(false, true);
			});

			holder.itemView.setSelected(selected);
		}

		@NonNull
		@Override
		public ViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, final int type) {
			return new ViewHolder(type != ViewHolder.VIEW_TYPE_DRAWER_ITEM);
		}
	}

	private class ViewHolder extends RecyclerView.ViewHolder {
		private static final int VIEW_TYPE_SEPARATOR = 0;
		private static final int VIEW_TYPE_DRAWER_ITEM = 1;

		private final AppCompatImageView iconView;
		private final TextView textView;

		private ViewHolder(final boolean separator) {
			super(separator ? new View(HomeworkApplication.this) : new FrameLayout(HomeworkApplication.this));
			final int margin;
			final ViewGroup.MarginLayoutParams parameters;

			if(separator) {
				this.iconView = null;
				this.textView = null;
				margin = HomeworkApplication.this.getPixelSize(24.0f);
				parameters = new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, HomeworkApplication.this.getPixelSize(3.0f));
				parameters.setMarginStart(margin);
				parameters.setMarginEnd(margin);
				parameters.topMargin = parameters.bottomMargin = HomeworkApplication.this.getPixelSize(6.0f);
				this.itemView.setLayoutParams(parameters);
				this.itemView.setForeground(HomeworkApplication.this.getDrawable(R.drawable.drawer_separator));
				return;
			}

			final int padding = HomeworkApplication.this.getPixelSize(16.0f);
			final int size = HomeworkApplication.this.getPixelSize(28.0f);
			margin = HomeworkApplication.this.getPixelSize(12.0f);
			parameters = new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
			parameters.setMarginStart(margin);
			parameters.setMarginEnd(margin);
			this.itemView.setLayoutParams(parameters);
			this.itemView.setBackground(HomeworkApplication.this.getDrawable(R.drawable.drawer_selector));
			final LinearLayout linearLayout = new LinearLayout(HomeworkApplication.this);
			linearLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
			linearLayout.setBackground(HomeworkApplication.this.getDrawable(R.drawable.drawer_ripple));
			linearLayout.setBaselineAligned(false);
			linearLayout.setGravity(Gravity.CENTER_VERTICAL);
			final FrameLayout frameLayout = new FrameLayout(HomeworkApplication.this);
			final LinearLayout.LayoutParams frameLayoutParameters = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
			frameLayoutParameters.setMarginStart(HomeworkApplication.this.getPixelSize(24.0f));
			frameLayout.setLayoutParams(frameLayoutParameters);
			frameLayout.setPaddingRelative(0, 0, padding, 0);
			this.iconView = new AppCompatImageView(HomeworkApplication.this);
			final FrameLayout.LayoutParams iconViewParameters = new FrameLayout.LayoutParams(size, size);
			iconViewParameters.gravity = Gravity.CENTER;
			this.iconView.setLayoutParams(iconViewParameters);
			this.iconView.setImageTintList(HomeworkApplication.this.getColorStateList(R.color.oui_primary_text_color));
			this.iconView.setScaleType(ImageView.ScaleType.FIT_XY);
			frameLayout.addView(this.iconView);
			linearLayout.addView(frameLayout);
			this.textView = new TextView(HomeworkApplication.this);
			final LinearLayout.LayoutParams textViewParameters = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT);
			textViewParameters.weight = 1.0f;
			this.textView.setLayoutParams(textViewParameters);
			this.textView.setEllipsize(TextUtils.TruncateAt.END);
			this.textView.setPaddingRelative(0, padding, 0, padding);
			this.textView.setSingleLine(true);
			this.textView.setTextColor(HomeworkApplication.this.getColor(R.color.oui_primary_text_color));
			this.textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18.0f);
			linearLayout.addView(this.textView);
			((ViewGroup) this.itemView).addView(linearLayout);
		}
	}
}

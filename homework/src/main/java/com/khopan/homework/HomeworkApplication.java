package com.khopan.homework;

import android.content.res.Resources;
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
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.khopan.homework.fragment.TestFragment;
import com.sec.sesl.khopan.homework.R;

import java.util.ArrayList;
import java.util.List;

import dev.oneuiproject.oneui.layout.DrawerLayout;
import dev.oneuiproject.oneui.layout.ToolbarLayout;

public class HomeworkApplication extends AppCompatActivity {
	private final List<Fragment> fragments;
	private final List<Fragment> drawerItems;

	private Resources resources;
	private DisplayMetrics metrics;

	public HomeworkApplication() {
		this.fragments = new ArrayList<>();
		this.fragments.add(new TestFragment());
		this.drawerItems = new ArrayList<>();
		this.drawerItems.add(this.fragments.get(0));
		this.drawerItems.add(null);
		this.drawerItems.add(this.fragments.get(0));
	}

	@Override
	public void onCreate(@Nullable final Bundle bundle) {
		super.onCreate(bundle);
		this.resources = this.getResources();
		this.metrics = this.resources.getDisplayMetrics();
		final DrawerLayout drawerLayout = new DrawerLayout(this, null);
		drawerLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
		drawerLayout.setDrawerButtonIcon(this.getDrawable(R.drawable.ic_oui_info_outline));
		drawerLayout.setDrawerButtonOnClickListener(null);
		drawerLayout.setExpandable(false);
		drawerLayout.setExpanded(false);
		drawerLayout.setTitle("Homework");

		final FrameLayout frameLayout = new FrameLayout(this);
		frameLayout.setId(ViewGroup.generateViewId());
		drawerLayout.addView(frameLayout, -1, new ToolbarLayout.ToolbarLayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 0));

		final RecyclerView recyclerView = new RecyclerView(this);
		recyclerView.setAdapter(new Adapter());
		recyclerView.setHasFixedSize(true);
		//recyclerView.setHorizontalScrollBarEnabled(false);
		recyclerView.setLayoutManager(new LinearLayoutManager(this));
		//recyclerView.setVerticalScrollBarEnabled(true);
		drawerLayout.addView(recyclerView, -1, new ToolbarLayout.ToolbarLayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 5));

		this.setContentView(drawerLayout);

		final FragmentTransaction transaction = this.getSupportFragmentManager().beginTransaction();
		this.fragments.forEach(fragment -> transaction.add(frameLayout.getId(), fragment));
		transaction.commitNow();
	}

	private int getPixelSize(float size) {
		return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, size, this.metrics));
	}

	private class Adapter extends RecyclerView.Adapter<ViewHolder> {
		private int selectedItem;

		@Override
		public int getItemCount() {
			return HomeworkApplication.this.drawerItems.size();
		}

		@Override
		public int getItemViewType(final int position) {
			return HomeworkApplication.this.drawerItems.get(position) == null ? ViewHolder.VIEW_TYPE_SEPARATOR : ViewHolder.VIEW_TYPE_DRAWER_ITEM;
		}

		@Override
		public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
			if(holder.separator) {
				return;
			}

			Fragment fragment = HomeworkApplication.this.drawerItems.get(position);
			holder.setSelected(position == this.selectedItem);
			holder.textView.setText(fragment.getClass().getSimpleName());
			holder.iconView.setImageResource(R.drawable.ic_oui_add_filled);
			holder.itemView.setOnClickListener(view -> {
				this.selectedItem = holder.getBindingAdapterPosition();
				this.notifyItemRangeChanged(0, this.getItemCount());
			});
		}

		@NonNull
		@Override
		public ViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, final int type) {
			return new ViewHolder(type == ViewHolder.VIEW_TYPE_SEPARATOR);
		}
	}

	private class ViewHolder extends RecyclerView.ViewHolder {
		private static final int VIEW_TYPE_SEPARATOR = 0;
		private static final int VIEW_TYPE_DRAWER_ITEM = 1;

		private final boolean separator;
		private final AppCompatImageView iconView;
		private final TextView textView;

		private ViewHolder(final boolean separator) {
			super(separator ? new View(HomeworkApplication.this) : new FrameLayout(HomeworkApplication.this));
			this.separator = separator;
			final int margin;
			final ViewGroup.MarginLayoutParams parameters;

			if(this.separator) {
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

		private void setSelected(boolean selected) {
			if(this.separator) {
				return;
			}

			this.itemView.setSelected(selected);
			this.textView.setEllipsize(selected ? TextUtils.TruncateAt.MARQUEE : TextUtils.TruncateAt.END);
		}
	}
}

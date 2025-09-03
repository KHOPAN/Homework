package com.khopan.homework;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
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

	private class Adapter extends RecyclerView.Adapter<ViewHolder> {
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

		private ViewHolder(final boolean separator) {
			super(separator ? new View(HomeworkApplication.this) : new FrameLayout(HomeworkApplication.this));
			this.separator = separator;
			final DisplayMetrics metrics = HomeworkApplication.this.getResources().getDisplayMetrics();
			final int margin;
			final ViewGroup.MarginLayoutParams parameters;

			if(this.separator) {
				margin = Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24.0f, metrics));
				parameters = new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 3.0f, metrics)));
				parameters.setMarginStart(margin);
				parameters.setMarginEnd(margin);
				parameters.topMargin = parameters.bottomMargin = Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 6.0f, metrics));
				this.itemView.setLayoutParams(parameters);
				this.itemView.setForeground(HomeworkApplication.this.getDrawable(R.drawable.drawer_separator));
				return;
			}

			margin = Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 12.0f, metrics));
			parameters = new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
			parameters.setMarginStart(margin);
			parameters.setMarginEnd(margin);
			this.itemView.setLayoutParams(parameters);
			this.itemView.setBackground(HomeworkApplication.this.getDrawable(R.drawable.drawer_selector));

			final LinearLayout linearLayout = new LinearLayout(HomeworkApplication.this);
			linearLayout.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
			linearLayout.setBackground(HomeworkApplication.this.getDrawable(R.drawable.drawer_ripple));
			linearLayout.setGravity(Gravity.CENTER_VERTICAL);

			((ViewGroup) this.itemView).addView(linearLayout);
		}
	}
}

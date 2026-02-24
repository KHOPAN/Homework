package com.khopan.homework.fragment;

import android.content.Context;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.util.SeslRoundedCorner;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelKt;
import androidx.lifecycle.ViewModelProvider;
import androidx.paging.Pager;
import androidx.paging.PagingConfig;
import androidx.paging.PagingData;
import androidx.paging.PagingDataAdapter;
import androidx.paging.PagingLiveData;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.khopan.core.CoreLayout;
import com.khopan.core.view.SimpleViewHolder;
import com.khopan.core.view.card.SwitchableCardView;
import com.khopan.homework.R;
import com.khopan.homework.database.HomeworkDatabase;
import com.khopan.homework.database.dao.AssignmentDao;
import com.khopan.homework.database.entity.Assignment;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Locale;
import java.util.Objects;

public class AssignmentFragment extends Fragment {
	private AssignmentDao accessor;
	private AssignmentViewModel model;

	@Override
	public void onCreate(@Nullable final Bundle bundle) {
		super.onCreate(bundle);
		final Context context = this.requireContext();
		this.accessor = HomeworkDatabase.getInstance(context.getApplicationContext()).getAssignment();
		this.model = new ViewModelProvider(this, new AssignmentViewModelFactory(this.accessor)).get(AssignmentViewModel.class);
	}

	@Nullable
	@Override
	public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable final Bundle bundle) {
		return new RecyclerView(this.requireContext());
	}

	@Override
	public void onViewCreated(@NonNull final View view, @Nullable final Bundle bundle) {
		final RecyclerView recyclerView = (RecyclerView) view;
		final Adapter adapter = new Adapter(this.accessor, DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM).withLocale(Locale.getDefault()));
		recyclerView.setAdapter(adapter);
		final Context context = this.requireContext();
		recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
		final int padding = Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10.0f, context.getResources().getDisplayMetrics()));
		recyclerView.setPadding(padding, 0, padding, 0);
		recyclerView.setScrollBarStyle(View.SCROLLBARS_OUTSIDE_OVERLAY);
		CoreLayout.forceEnableScrollbars(recyclerView, false, true);
		this.model.data.observe(this.getViewLifecycleOwner(), data -> adapter.submitData(this.getLifecycle(), data));
	}

	private static class Adapter extends PagingDataAdapter<Assignment, SimpleViewHolder<SwitchableCardView>> {
		private final AssignmentDao accessor;
		private final DateTimeFormatter formatter;

		private Adapter(final AssignmentDao accessor, final DateTimeFormatter formatter) {
			super(new DiffUtil.ItemCallback<>() {
				@Override
				public boolean areItemsTheSame(@NonNull final Assignment oldItem, @NonNull final Assignment newItem) {
					return Objects.equals(oldItem.identifier, newItem.identifier);
				}

				@Override
				public boolean areContentsTheSame(@NonNull final Assignment oldItem, @NonNull final Assignment newItem) {
					return Objects.equals(oldItem, newItem);
				}
			});

			this.accessor = accessor;
			this.formatter = formatter;
		}

		@Override
		public void onBindViewHolder(@NonNull final SimpleViewHolder<SwitchableCardView> holder, final int position) {
			final Assignment assignment = this.getItem(position);
			final Context context = holder.itemView.getContext();
			holder.itemView.setOnClickListener(assignment == null ? null : view -> new AlertDialog.Builder(context).setTitle(assignment.title).setMessage(R.string.assignment_delete).setNegativeButton(R.string.assignment_delete_cancel, null).setPositiveButton(R.string.assignment_delete_ok, (dialog, which) -> new Thread(() -> this.accessor.delete(assignment)).start()).create().show());
			holder.itemView.setRoundedCorners((position == 0 ? SeslRoundedCorner.ROUNDED_CORNER_TOP_LEFT | SeslRoundedCorner.ROUNDED_CORNER_TOP_RIGHT : 0) | (position == this.getItemCount() - 1 ? SeslRoundedCorner.ROUNDED_CORNER_BOTTOM_LEFT | SeslRoundedCorner.ROUNDED_CORNER_BOTTOM_RIGHT : 0));
			final String text = context.getString(R.string.assignment_loading);
			holder.itemView.setSummary(assignment == null ? text : this.formatter.format(LocalDateTime.ofEpochSecond(assignment.deadline, 0, ZoneOffset.UTC)));
			holder.itemView.setSwitchStateListener(null);
			holder.itemView.setSwitchState(assignment != null && assignment.done);
			holder.itemView.setSwitchStateListener(assignment == null ? null : (view, state) -> {
				assignment.done = state;
				new Thread(() -> this.accessor.update(assignment)).start();
			});

			holder.itemView.setTitle(assignment == null ? text : assignment.title);
			holder.itemView.setTopDividerVisible(position != 0);
		}

		@NonNull
		@Override
		public SimpleViewHolder<SwitchableCardView> onCreateViewHolder(@NonNull final ViewGroup parent, final int viewType) {
			final Context context = parent.getContext();
			final SwitchableCardView view = new SwitchableCardView(context);
			view.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
			view.setBackgroundColor(context.getColor(dev.oneuiproject.oneui.design.R.color.oui_des_background_color));
			view.setSwitchDividerVisible(true);
			return new SimpleViewHolder<>(view);
		}
	}

	private static class AssignmentViewModel extends ViewModel {
		private final LiveData<PagingData<Assignment>> data;

		private AssignmentViewModel(final AssignmentDao accessor) {
			PagingLiveData.cachedIn(this.data = PagingLiveData.getLiveData(new Pager<>(new PagingConfig(20, 5, true), accessor::allPaged)), ViewModelKt.getViewModelScope(this));
		}
	}

	private static class AssignmentViewModelFactory implements ViewModelProvider.Factory {
		private final AssignmentDao accessor;

		private AssignmentViewModelFactory(final AssignmentDao accessor) {
			this.accessor = accessor;
		}

		@NonNull
		@SuppressWarnings("unchecked")
		@Override
		public <T extends ViewModel> T create(@NonNull final Class<T> modelClass) {
			return (T) new AssignmentViewModel(this.accessor);
		}
	}
}

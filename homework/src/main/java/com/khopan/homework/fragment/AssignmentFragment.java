package com.khopan.homework.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelKt;
import androidx.paging.Pager;
import androidx.paging.PagingConfig;
import androidx.paging.PagingData;
import androidx.paging.PagingDataAdapter;
import androidx.paging.PagingLiveData;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.khopan.core.fragment.ContextedFragment;
import com.khopan.core.view.card.CardView;
import com.khopan.homework.database.HomeworkDatabase;
import com.khopan.homework.database.dao.AssignmentDao;
import com.khopan.homework.database.entity.Assignment;

import java.util.Objects;

public class AssignmentFragment extends ContextedFragment {
	@Nullable
	@Override
	public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable final Bundle bundle) {
		return new RecyclerView(inflater.getContext());
	}

	@Override
	public void onViewCreated(@NonNull final View view, @Nullable final Bundle bundle) {
		final RecyclerView recyclerView = (RecyclerView) view;
		final Adapter adapter = new Adapter();
		recyclerView.setAdapter(adapter);
		final AssignmentViewModel viewModel = new AssignmentViewModel(HomeworkDatabase.getInstance(this.context.getApplicationContext()).getAssignment());
		viewModel.data.observe(this.getViewLifecycleOwner(), data -> adapter.submitData(this.getLifecycle(), data));
		recyclerView.setLayoutManager(new LinearLayoutManager(this.context, LinearLayoutManager.VERTICAL, false));
	}

	private static class Adapter extends PagingDataAdapter<Assignment, ViewHolder> {
		public Adapter() {
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
		}

		@Override
		public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
			final Assignment assignment = this.getItem(position);
			((CardView) holder.itemView).setTitle(assignment == null ? "Null" : assignment.title);
		}

		@NonNull
		@Override
		public ViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, final int viewType) {
			return new ViewHolder(new CardView(parent.getContext()));
		}
	}

	private static class ViewHolder extends RecyclerView.ViewHolder {
		public ViewHolder(@NonNull final View itemView) {
			super(itemView);
		}
	}

	private static class AssignmentViewModel extends ViewModel {
		private final LiveData<PagingData<Assignment>> data;

		private AssignmentViewModel(final AssignmentDao accessor) {
			this.data = PagingLiveData.getLiveData(new Pager<>(new PagingConfig(20, 5, true), accessor::getAllPaged));
			PagingLiveData.cachedIn(this.data, ViewModelKt.getViewModelScope(this));
		}
	}
}

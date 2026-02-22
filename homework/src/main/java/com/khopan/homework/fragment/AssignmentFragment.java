package com.khopan.homework.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.khopan.core.fragment.ContextedFragment;

public class AssignmentFragment extends ContextedFragment {
	@Nullable
	@Override
	public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable final Bundle bundle) {
		return new RecyclerView(inflater.getContext());
	}

	@Override
	public void onViewCreated(@NonNull final View view, @Nullable final Bundle bundle) {
		final RecyclerView recyclerView = (RecyclerView) view;
		recyclerView.setAdapter(new Adapter());
		recyclerView.setLayoutManager(new LinearLayoutManager(this.context, LinearLayoutManager.VERTICAL, false));
	}

	private class Adapter extends RecyclerView.Adapter<ViewHolder> {
		@Override
		public int getItemCount() {
			return 0;
		}

		@Override
		public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

		}

		@NonNull
		@Override
		public ViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, final int viewType) {
			return new ViewHolder(null);
		}
	}

	private static class ViewHolder extends RecyclerView.ViewHolder {
		public ViewHolder(@NonNull final View itemView) {
			super(itemView);
		}
	}
}

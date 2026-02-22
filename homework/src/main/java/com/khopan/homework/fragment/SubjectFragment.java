package com.khopan.homework.fragment;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.khopan.core.fragment.ContextedFragment;
import com.khopan.core.view.card.CardView;
import com.khopan.core.view.card.CheckableCardView;
import com.khopan.core.view.card.SwitchableCardView;
import com.khopan.homework.R;

import java.util.ArrayList;
import java.util.List;

import javax.security.auth.Subject;

public class SubjectFragment extends ContextedFragment {
	@Nullable
	@Override
	public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable final Bundle bundle) {
		return null;//inflater.inflate(R.layout.fragment_subject, container, false);
	}

	@Override
	public void onViewCreated(@NonNull final View view, @Nullable final Bundle bundle) {
		//final RecyclerView recyclerView = view.findViewById(R.id.recycler_view);
		//recyclerView.setAdapter(new Adapter());
		//recyclerView.setLayoutManager(new LinearLayoutManager(this.context, LinearLayoutManager.VERTICAL, false));
	}

	private class Adapter extends RecyclerView.Adapter<ViewHolder> {
		private final LayoutInflater inflater;
		private final List<Pair<String, String>> list;

		private Adapter() {
			this.inflater = LayoutInflater.from(SubjectFragment.this.context);
			this.list = new ArrayList<>();
			this.list.add(new Pair<>("ภาษาไทย", "นางบุญชอบ สายทอง"));
			this.list.add(new Pair<>("ชีววิทยา", "นางสาวบุปผา สันตวา"));
			this.list.add(new Pair<>("อังกฤษ", "นางสาวจุฑามาส ริยะขัน"));
			this.list.add(new Pair<>("สังคมศึกษา", "นายพิชิต ไพอุปลี"));
			this.list.add(new Pair<>("เคมี", "นางสาวกนกภรณ์ ทรวดทรง"));
			this.list.add(new Pair<>("ฟิสิกส์", "นางสาวนงนุช จากปั้น"));
			this.list.add(new Pair<>("USB tethering", "Transferring images"));
		}

		@Override
		public int getItemCount() {
			return 1000;//this.list.size();
		}

		@Override
		public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
			//holder.layout.setRoundedCorners((position == 0 ? (SeslRoundedCorner.ROUNDED_CORNER_TOP_LEFT | SeslRoundedCorner.ROUNDED_CORNER_TOP_RIGHT) : 0) | (position == this.getItemCount() - 1 ? (SeslRoundedCorner.ROUNDED_CORNER_BOTTOM_LEFT | SeslRoundedCorner.ROUNDED_CORNER_BOTTOM_RIGHT) : 0));
			/*holder.itemView.setShowTopDivider(position != 0);
			holder.itemView.setSummary(this.list.get(position).second);
			holder.itemView.setTitle(this.list.get(position).first);*/
			final Drawable drawable = SubjectFragment.this.context.getDrawable(dev.oneuiproject.oneui.R.drawable.ic_oui_android);
			//holder.itemView.setOnClickListener(view -> {
				//((SwitchableCardView) holder.itemView).setSwitchVisible(!((SwitchableCardView) holder.itemView).isSwitchVisible());
				//holder.itemView.setIcon(holder.itemView.getIcon() == null ? drawable : null);
			//});

			((CheckableCardView) holder.itemView).setCheckboxVisible(true);
			((CheckableCardView) holder.itemView).setCheckboxStateListener((checkbox, state) -> {
				if(state) {
					holder.itemView.setIcon(holder.itemView.getIcon() == null ? drawable : null);
				}
			});
			holder.itemView.setCardLocation(position, this.getItemCount());
			holder.itemView.resetForegroundState();
			holder.itemView.setTitle(this.list.get(position % this.list.size()).first);
			holder.itemView.setSummary(this.list.get(position % this.list.size()).second);
		}

		@NonNull
		@Override
		public ViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, final int viewType) {
			return null;//new ViewHolder(this.inflater.inflate(R.layout.fragment_subject_item, parent, false).findViewById(R.id.item_view));
		}
	}

	private static class ViewHolder extends RecyclerView.ViewHolder {
		private final CardView itemView;

		public ViewHolder(@NonNull final CardView itemView) {
			super(itemView);
			this.itemView = itemView;
		}
	}
}

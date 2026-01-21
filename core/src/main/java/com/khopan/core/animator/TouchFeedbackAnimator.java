package com.khopan.core.animator;

import android.content.Context;
import android.os.Build;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.RequiresApi;
import androidx.appcompat.animation.SeslRecoilAnimator;
import androidx.appcompat.widget.SeslLinearLayoutCompat;

@RequiresApi(Build.VERSION_CODES.Q)
public class TouchFeedbackAnimator {
	private static final int ACTION_PEN_DOWN = 211;
	private static final int ACTION_PEN_UP = 212;

	private final SeslRecoilAnimator.Holder recoil;
	private final SeslLinearLayoutCompat.ItemBackgroundHolder holder;
	private final View recoilView;
	private final View holderView;

	public TouchFeedbackAnimator(final View view) {
		this(new SeslRecoilAnimator.Holder(view.getContext()), new ItemForegroundHolder(), view, view);
	}

	public TouchFeedbackAnimator(final Context context, final View recoilView, final View holderView) {
		this(new SeslRecoilAnimator.Holder(context), new ItemForegroundHolder(), recoilView, holderView);
	}

	public TouchFeedbackAnimator(final SeslRecoilAnimator.Holder recoil, final SeslLinearLayoutCompat.ItemBackgroundHolder holder, final View recoilView, final View holderView) {
		this.recoil = recoil;
		this.holder = holder;
		this.recoilView = recoilView;
		this.holderView = holderView;
	}

	public void animate(final MotionEvent event) {
		switch(event.getAction()) {
		case MotionEvent.ACTION_CANCEL:
			this.recoil.setRelease();
			this.holder.setCancel();
			break;
		case MotionEvent.ACTION_DOWN:
		case TouchFeedbackAnimator.ACTION_PEN_DOWN:
			this.recoil.setPress(this.recoilView);
			this.holder.setPress(this.holderView);
			break;
		case TouchFeedbackAnimator.ACTION_PEN_UP:
		case MotionEvent.ACTION_UP:
			this.recoil.setRelease();
			this.holder.setRelease();
			break;
		}
	}
}

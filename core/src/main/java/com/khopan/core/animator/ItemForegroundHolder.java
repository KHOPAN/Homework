package com.khopan.core.animator;

import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.graphics.drawable.SeslRecoilDrawable;
import androidx.appcompat.widget.SeslLinearLayoutCompat;

@RequiresApi(Build.VERSION_CODES.Q)
public class ItemForegroundHolder extends SeslLinearLayoutCompat.ItemBackgroundHolder {
	private Drawable foreground;

	@Override
	public void setCancel() {
		if(this.foreground != null) {
			if(this.foreground instanceof SeslRecoilDrawable) {
				((SeslRecoilDrawable) this.foreground).setCancel();
			} else {
				this.foreground.setState(new int[0]);
			}

			this.foreground = null;
		}
	}

	@Override
	public void setPress(@NonNull final View view) {
		this.setRelease();

		if((this.foreground = view.getForeground()) != null) {
			this.foreground.setState(new int[] {android.R.attr.state_pressed});
		}
	}

	@Override
	public void setRelease() {
		if(this.foreground != null) {
			this.foreground.setState(new int[0]);
			this.foreground = null;
		}
	}
}

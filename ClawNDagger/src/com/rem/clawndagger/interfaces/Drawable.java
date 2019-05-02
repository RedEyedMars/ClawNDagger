package com.rem.clawndagger.interfaces;

import com.rem.clawndagger.game.events.Events;

public interface Drawable {

	public static interface Focusable {
		public Boolean on(Events.Draw.Focus focus);
		public Boolean on(Events.Draw.Unfocus focus);
	}
	public Events.Draw on(Events.Draw draw);
}

package com.rem.clawndagger.interfaces;

public interface Nameable {

	public String getName();
	public static interface Settable extends Nameable {
		public void setName(String name);
	}
}

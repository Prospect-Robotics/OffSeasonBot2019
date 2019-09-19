package com.team2813.lib.sparkMax.options;

public enum InvertType {
	NORMAL(false), FOLLOW_LEADER(false), OPPOSE_LEADER(true), INVERTED(true);

	public boolean inverted;

	InvertType(boolean inverted) {
		this.inverted = inverted;
	}
}
package fr.wixonic.market;

import java.util.Properties;

public final class Database extends Properties {
	public int getInt(String propertyName) {
		return Integer.parseInt(this.getProperty(propertyName, "0"));
	}

	public String count(int buying, int selling, String type) {
		if (buying > 0 && selling > 0) {
			return (buying + selling) + " " + type + "s - " + count(buying, 0, "buying " + type) + ", " + count(0, selling, "selling " + type);
		} else if (buying > 0) {
			return buying > 1 ? buying + type + "s" : "One " + type;
		} else if (selling > 0) {
			return selling > 1 ? selling + type + "s" : "One " + type;
		} else {
			return "No " + type;
		}
	}
}
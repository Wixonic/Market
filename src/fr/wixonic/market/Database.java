package fr.wixonic.market;

import java.util.Properties;

public final class Database extends Properties {
	public int getInt(String propertyName) {
		return Integer.parseInt(this.getProperty(propertyName, "0"));
	}
}
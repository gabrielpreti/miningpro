package com.ml.miningpro.tools;

import java.util.Calendar;

public class DateTools {

	public static Calendar clone(Calendar source) {
		Calendar newCalendar = Calendar.getInstance();
		newCalendar.setTime(source.getTime());

		return newCalendar;
	}

	public static Calendar trunk(Calendar source, int timeUnit) {
		if (timeUnit != Calendar.SECOND && timeUnit != Calendar.MINUTE && timeUnit != Calendar.HOUR) {
			throw new IllegalArgumentException(String.format("Unsupported time unit: %d", timeUnit));
		}

		source.set(Calendar.MILLISECOND, 0);
		if (timeUnit == Calendar.SECOND) {
			return source;
		}
		
		source.set(Calendar.SECOND, 0);
		if (timeUnit == Calendar.MINUTE) {
			return source;
		}

		source.set(Calendar.MINUTE, 0);
		if (timeUnit == Calendar.HOUR) {
			return source;
		}

		return source;
	}

	public static Calendar cloneAndTrunk(Calendar source, int timeUnit) {
		Calendar newCalendar = clone(source);
		return trunk(newCalendar, timeUnit);
	}

	public static Calendar cloneAndAddTime(Calendar source, int field, int amount) {
		Calendar newCalendar = Calendar.getInstance();
		newCalendar.setTime(source.getTime());
		newCalendar.add(field, amount);

		return newCalendar;
	}
}

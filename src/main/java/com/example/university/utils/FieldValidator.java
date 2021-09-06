package com.example.university.utils;

/**
 * Class with utility methods for checking input values send from the user.
 */
public class FieldValidator {

	private static final String POSITIVE_DECIMAL_NUMBER_REGEX = "\\d+";
	private static final String FILLED_REGEX = "^\\p{L}[\\p{L}\\s]*\\p{L}$";
	private static final String IS_LATIN_WORD = "[a-zA-Z ]+";
	private static final String IS_CYRILLIC_WORD = "[а-яА-Я ]+";

	private static <T> boolean checkNull(
			@SuppressWarnings("unchecked") T... values) {
		if (values == null) {
			return true;
		} else {
			for (T value : values) {
				if (value == null) {
					return true;
				}
			}
			return false;
		}
	}

	public static boolean isFilled(String... values) {
		if (checkNull(values)) {
			return false;
		}
		for (String value : values) {
			if (!value.matches(FILLED_REGEX)) {
				return false;
			}
		}
		return true;
	}

	public static boolean isCyrillicWord(String... values) {
		if (checkNull(values)) {
			return false;
		}
		for (String value : values) {
			if (!value.matches(IS_CYRILLIC_WORD)) {
				return false;
			}
		}
		return true;
	}

	public static boolean isLatinWord(String... values) {
		if (checkNull(values)) {
			return false;
		}
		for (String value : values) {
			if (!value.matches(IS_LATIN_WORD)) {
				return false;
			}
		}
		return true;
	}

	public static boolean isPositiveDecimalNumber(String... values) {
		if (checkNull(values)) {
			return false;
		}
		for (String value : values) {
			if (!value.matches(POSITIVE_DECIMAL_NUMBER_REGEX)) {
				return false;
			}
		}
		return true;
	}

	public static boolean isPositiveByte(Number... values) {
		if (checkNull(values)) {
			return false;
		}
		for (Number value : values) {
			Long longValue = value.longValue();
			if (longValue.compareTo((long) 0) < 0
					|| longValue.compareTo((long) Byte.MAX_VALUE) > 0) {
				return false;
			}
		}
		return true;
	}

	public static boolean checkBudgetLowerTotal(int budget, int total) {
		return budget < total;
	}
}

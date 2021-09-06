package com.example.university.utils;

/**
 * Class with utility methods for checking input values send from the user.
 */
public class FieldValidator {

	private static final String POSITIVE_DECIMAL_NUMBER_REGEX = "\\d+";
	private static final String FILLED_REGEX = "^\\p{L}[\\p{L}\\s]*\\p{L}$";
	private static final String IS_LATIN_WORD = "[a-zA-Z ]+";
	private static final String IS_CYRILLIC_WORD = "[а-яА-Я ]+";

	private FieldValidator() {}

	private static boolean checkNull(Object... values) {
		if (values == null) {
			return true;
		} else {
			for (Object value : values) {
				if (value == null) {
					return true;
				}
			}
			return false;
		}
	}

	public static boolean isFilled(String... stringValues) {
		if (checkNull((Object) stringValues)) {
			return false;
		}
		for (String value : stringValues) {
			if (!value.matches(FILLED_REGEX)) {
				return false;
			}
		}
		return true;
	}

	public static boolean isCyrillicWord(String... values) {
		if (checkNull((Object) values)) {
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
		if (checkNull((Object) values)) {
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
		if (checkNull((Object) values)) {
			return false;
		}
		for (String value : values) {
			if (!value.matches(POSITIVE_DECIMAL_NUMBER_REGEX)) {
				return false;
			}
		}
		return true;
	}

	public static boolean isPositiveNumbers(Number... values) {
		if (checkNull((Object) values)) {
			return false;
		}
		for (Number value : values) {
			if (value.intValue() < 0) {
				return false;
			}
		}
		return true;
	}

	public static boolean checkBudgetLowerTotal(int budget, int total) {
		return budget < total;
	}
}

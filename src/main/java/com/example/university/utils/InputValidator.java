package com.example.university.utils;

public class InputValidator {

    private InputValidator() {
    }

    public static boolean validateUserParameters(String firstName, String lastName,
                                                 String email, String password, String lang) {
        return FieldValidator.isFilled(firstName, lastName, lang)
                && email.contains("@")
                && (password.length() >= 4);
    }

    public static boolean validateApplicantParameters(String city,
                                                      String district,
                                                      String school) {
        return FieldValidator.isFilled(city, district) && (!school.isEmpty());
    }

    public static boolean validateFacultyParameters(String facultyNameRu,
                                                    String facultyNameEn,
                                                    String facultyBudgetPlaces,
                                                    String facultyTotalPlaces) {
        if (!FieldValidator.isCyrillicWord(facultyNameRu)
                || !FieldValidator.isLatinWord(facultyNameEn)) {
            return false;
        }
        if (!FieldValidator.isPositiveDecimalNumber(facultyBudgetPlaces,
                facultyTotalPlaces)) {
            return false;
        }
        if (!FieldValidator.isPositiveNumbers(Integer.valueOf(facultyBudgetPlaces),
                Integer.valueOf(facultyTotalPlaces))) {
            return false;
        }
        int budget = Integer.parseInt(facultyBudgetPlaces);
        int total = Integer.parseInt(facultyTotalPlaces);
        return FieldValidator.checkBudgetLowerTotal(budget, total);
    }

    public static boolean validateSubjectParameters(String subjectNameRu,
                                                    String subjectNameEn) {
        if (!FieldValidator.isCyrillicWord(subjectNameRu)) {
            return false;
        }
        return FieldValidator.isLatinWord(subjectNameEn);
    }


}

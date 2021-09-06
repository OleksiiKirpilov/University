package com.example.university.utils;

public class InputValidator {

    private InputValidator() {}

    public static boolean validateUserParameters(String firstName, String lastName,
                                                 String email, String password, String lang) {
        return FieldValidator.isFilled(firstName, lastName, lang)
                && email.contains("@")
                && (password.length() >= 4);
    }

    public static boolean validateApplicantParameters(String city,
                                                      String district, String school) {
        return FieldValidator.isFilled(city, district) && (!school.isEmpty());
    }

    public static boolean validateFacultyParameters(String facultyNameRu,
                                             String facultyNameEng, String facultyBudgetSeats,
                                             String facultyTotalSeats) {
        if (!FieldValidator.isCyrillicWord(facultyNameRu)
                || !FieldValidator.isLatinWord(facultyNameEng)) {
            return false;
        }
        if (!FieldValidator.isPositiveDecimalNumber(facultyBudgetSeats,
                facultyTotalSeats)) {
            return false;
        }
        if (!FieldValidator.isPositiveByte(Long.valueOf(facultyBudgetSeats),
                Long.valueOf(facultyTotalSeats))) {
            return false;
        }
        int budget = Integer.parseInt(facultyBudgetSeats);
        int total = Integer.parseInt(facultyTotalSeats);
        return FieldValidator.checkBudgetLowerTotal(budget, total);
    }

    public static boolean validateSubjectParameters(String subjectNameRu,
                                             String subjectNameEng) {
        if (!FieldValidator.isCyrillicWord(subjectNameRu)) {
            return false;
        }
        return FieldValidator.isLatinWord(subjectNameEng);
    }


}

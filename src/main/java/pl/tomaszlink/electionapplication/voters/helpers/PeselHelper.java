package pl.tomaszlink.electionapplication.voters.helpers;

import java.time.LocalDate;

public class PeselHelper {
    private PeselHelper(){}

    public static LocalDate extractBirthDateFromPesel(String pesel) {
        int year = Integer.parseInt(pesel.substring(0, 2));
        int month = Integer.parseInt(pesel.substring(2, 4));
        int day = Integer.parseInt(pesel.substring(4, 6));

        if (month < 1 || month > 12) {
            throw new IllegalArgumentException("Invalid PESEL: invalid month");
        }
        if (day < 1 || day > 31) {
            throw new IllegalArgumentException("Invalid PESEL: invalid day");
        }

        int fullYear = year +
                (year <= LocalDate.now().getYear() % 2000 ? 2000 : 1900);

        return LocalDate.of(fullYear, month, day);
    }
}

package helpers;

import java.time.LocalDate;

public class Validation {
    public static final String STRING = "[a-zA-Zа-яА-ЯёЁ-]*";
    public static final String STRINGWSPACE = "[a-zA-Zа-яА-ЯёЁ-[ ]]*";
    public static final String INTEGER = "\\d*";
    public static final String FLOAT = "([\\d]*[.])?[\\d]*";
    public static final String ALPHANUMERIC = "\\w*";
    public static final LocalDate DEFAULT_DATE = LocalDate.of(2000, 1, 1);
}
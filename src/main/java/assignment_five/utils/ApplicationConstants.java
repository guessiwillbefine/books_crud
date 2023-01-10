package assignment_five.utils;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ApplicationConstants {
    @UtilityClass
    public class Validation {
        public static final int MAX_AUTHOR_NAME_SIZE = 20;
        public static final int MIN_AUTHOR_NAME_SIZE = 1;
        public static final int MAX_AUTHOR_SURNAME_SIZE = 20;
        public static final int MIN_AUTHOR_SURNAME_SIZE = 1;
        public static final int MIN_AGE = 18;
        public static final int MAX_AGE = 100;
        public static final int MAX_YEAR = 2023;
        public static final int DESCRIPTION_SIZE = 500;
        public static final int MAX_BOOK_NAME_SIZE = 30;
        public static final int MIN_BOOK_NAME_SIZE = 1;
        public static final int MIN_BOOK_PAGES = 1;
    }
}

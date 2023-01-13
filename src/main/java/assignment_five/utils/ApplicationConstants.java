package assignment_five.utils;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ApplicationConstants {
    @UtilityClass
    public class Validation {
        public static final int MAX_AUTHOR_NAME_SIZE = 20;
        public static final int MIN_AUTHOR_NAME_SIZE = 1;
        public static final String AUTHOR_NAME_MSG = "author's name must be between " +
                MIN_AUTHOR_NAME_SIZE + " and " + MAX_AUTHOR_NAME_SIZE;
        public static final int MAX_AUTHOR_SURNAME_SIZE = 20;
        public static final int MIN_AUTHOR_SURNAME_SIZE = 1;
        public static final String AUTHOR_SURNAME_MSG = "author's surname must be between " +
                MIN_AUTHOR_SURNAME_SIZE + " and " + MAX_AUTHOR_SURNAME_SIZE;
        public static final int MIN_AGE = 18;
        public static final int MAX_AGE = 100;
        public static final String AUTHOR_AGE_MSG = "author's age must be between " +
                MIN_AGE + " and " + MAX_AGE;
        public static final int MAX_YEAR = 2023;
        public static final String BOOK_YEAR_MSG = "year of publishing can't be greater than " + MAX_AGE;
        public static final int DESCRIPTION_SIZE = 500;
        public static final String BOOK_DESCRIPTION_MSG = "max size of description = " + DESCRIPTION_SIZE;
        public static final int MAX_BOOK_NAME_SIZE = 30;
        public static final int MIN_BOOK_NAME_SIZE = 1;
        public static final String BOOK_NAME_MSG = "book's name must be between " +
                MIN_BOOK_NAME_SIZE + " and " + MAX_BOOK_NAME_SIZE;
        public static final int MIN_BOOK_PAGES = 1;
        public static final String BOOK_PAGE_MSG = "book's must have at least " + MIN_BOOK_PAGES + " pages";
    }
}

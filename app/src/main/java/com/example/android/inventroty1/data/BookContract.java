package com.example.android.inventroty1.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

public final class BookContract {

    // give it an empty constructor to prevent anyone from instantiating contract class
    private BookContract() {}

    public static final String CONTENT_AUTHORITY = "com.example.android.books" +"";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_BOOKS = "books";
    public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_BOOKS);




    //Inner class to define constant values for each book
    public static final class BookEntry implements BaseColumns {
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_BOOKS);
        /**
         * The MIME type of the {@link #CONTENT_URI} for a list of pets.
         */
        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_BOOKS;

        /**
         * The MIME type of the {@link #CONTENT_URI} for a single Book.
         */
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_BOOKS;

        public final static String TABLE_NAME = "books";
        public final static String _ID = BaseColumns._ID;
        public final static String COLUMN_Name = "name";
        public final static String COLUMN_Price = "price";
        public final static String COLUMN_Quantity = "quantity";
        public final static String COLUMN_Supplier_Name = "supplier_name";
        public final static String COLUMN_Supplier_Phone_Number = "phone";

        /**
         * Possible values for the suppliers.
         */
        public static final int SUPPLIER_UNKNOWN = 0;
        public static final int SUPPLIER_NAHDA = 1;
        public static final int SUPPLIER_AHRAM = 2;
        public static final int SUPPLIER_SHROUQ = 3;
        public static final int SUPPLIER_LEBNAN = 4;
        public static final int SUPPLIER_SALAM = 5;
    }
}
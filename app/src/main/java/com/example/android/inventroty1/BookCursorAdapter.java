package com.example.android.inventroty1;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.inventroty1.data.BookContract;

/**
 * {@link BookCursorAdapter} is an adapter for a list or grid view
 * that uses a {@link Cursor} of Book data as its data source. This adapter knows
 * how to create list items for each row of Book data in the {@link Cursor}.
 */
public class BookCursorAdapter extends CursorAdapter {

    /**
     * Constructs a new {@link BookCursorAdapter}.
     *
     * @param context The context
     * @param c       The cursor from which to get the data.
     */
    public BookCursorAdapter(Context context, Cursor c) {
        super(context, c, 0 /* flags */);
    }

    /**
     * Makes a new blank list item view. No data is set (or bound) to the views yet.
     *
     * @param context app context
     * @param cursor  The cursor from which to get the data. The cursor is already
     *                moved to the correct position.
     * @param parent  The parent to which the new view is attached to
     * @return the newly created list item view.
     */
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        // Inflate a list item view using the layout specified in list_item.xml
        return LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
    }

    /**
     * This method binds the Book data (in the current row pointed to by cursor) to the given
     * list item layout. For example, the name for the current Book can be set on the name TextView
     * in the list item layout.
     *
     * @param view    Existing view, returned earlier by newView() method
     * @param context app context
     * @param cursor  The cursor from which to get the data. The cursor is already moved to the
     *                correct row.
     */
    @Override
    public void bindView(View view, final Context context, Cursor cursor) {
        // Find individual views that we want to modify in the list item layout
        TextView nameTextView = (TextView) view.findViewById(R.id.name);
        TextView priceTextView = (TextView) view.findViewById(R.id.price);
        TextView quantityTextView = (TextView) view.findViewById(R.id.quantity);

        // Get the sale button view
        Button saleButton = (Button) view.findViewById(R.id.sale);

        // Find the columns of Book attributes that we're interested in
        final int idColumnIndex = cursor.getInt(cursor.getColumnIndex(BookContract.BookEntry._ID));
        int nameColumnIndex = cursor.getColumnIndex(BookContract.BookEntry.COLUMN_Name);
        int priceColumnIndex = cursor.getColumnIndex(BookContract.BookEntry.COLUMN_Price);
        int quantityColumnIndex = cursor.getColumnIndex(BookContract.BookEntry.COLUMN_Quantity);

        // Read the Book attributes from the Cursor for the current Book
        String bookName = cursor.getString(nameColumnIndex);
        String bookPrice = cursor.getString(priceColumnIndex);
        final int bookQuantity = cursor.getInt(quantityColumnIndex);

        // Logic for hiding the sale button when the quantity of the current
        // book is 0 (we are out of stock), so you cannot buy the book anymore
        if (bookQuantity > 0) {
            // Button visible if the quantity > 0
            saleButton.setVisibility(View.VISIBLE);
        }

        if (bookQuantity <= 0) {
            // Button gone if quantity = 0
            saleButton.setVisibility(View.GONE);
        }


        if (TextUtils.isEmpty(bookPrice)) {
            bookPrice = context.getString(R.string.unknown_price);
        }

        // Update the TextViews with the attributes for the current Book
        nameTextView.setText(bookName);
        priceTextView.setText("Price: " + bookPrice + " $" );
        quantityTextView.setText("Quantity: " + String.valueOf(bookQuantity));

        // Decrease the quantity pf the book
        // by 1 on each click on the sale button
        saleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Display toast message for every click on the sale button
                Toast.makeText(context, "Next Sale is SOON", Toast.LENGTH_SHORT).show();

                Uri quantityUri = ContentUris.withAppendedId(BookContract.BookEntry.CONTENT_URI, idColumnIndex);
                ContentValues values = new ContentValues();
                values.put(BookContract.BookEntry.COLUMN_Quantity, bookQuantity - 1);
                context.getContentResolver().update(quantityUri, values, null, null);
            }
        });
    }
}

package com.example.android.inventroty1;

import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.inventroty1.data.BookContract;

/**
 * Allows user to create a new Book or edit an existing one.
 */
public class EditorActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    /**
     * EditText field to enter the books title
     */
    private EditText mNameEditText;

    /**
     * EditText field to enter the books price
     */
    private EditText mPriceEditText;

    /**
     * EditText field to enter the books quantity;
     */
    private TextView mQuantityView;

    private int mQuantity;


    /**
     * EditText field to enter the suppliers phone number;
     */
    private EditText mSuppliersNumberEditText;

    /**
     * EditText field to enter the books supplierr
     */
    private Spinner mSupplierSpinner;

    private int mSupplier = BookContract.BookEntry.SUPPLIER_UNKNOWN;

    private Uri mCurrentBookUri;
    private static final int EXISTING_BOOK_LOADER = 0;

    /** Boolean flag that keeps track of whether the pet has been edited (true) or not (false) */
    private boolean mBookHasChanged = false;

    /**
     * OnTouchListener that listens for any user touches on a View, implying that they are modifying
     * the view, and we change the mPetHasChanged boolean to true.
     */
    private View.OnTouchListener mTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            mBookHasChanged = true;
            return false;
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        // Examine the intent that was used to launch this activity,
        // in order to figure out if we're creating a new Book or editing an existing one.
        Intent intent = getIntent();
        mCurrentBookUri = intent.getData();

        // If the intent DOES NOT contain a Book content URI, then we know that we are
        // creating a new Book.
        if (mCurrentBookUri == null) {
            // This is a new Book, so change the app bar to say "Add a Book"
            setTitle(getString(R.string.editor_activity_title_new_book));

            // Invalidate the options menu, so the "Delete" menu option can be hidden.
            // (It doesn't make sense to delete a Book that hasn't been created yet.)
            invalidateOptionsMenu();
        } else {
            // Otherwise this is an existing Book, so change app bar to say "Edit Book"
            setTitle(getString(R.string.editor_activity_title_edit_book));

            // Initialize a loader to read the Book data from the database
            // and display the current values in the editor
            getLoaderManager().initLoader(EXISTING_BOOK_LOADER, null, this);
        }

        // Find all relevant views that we will need to read user input from
        mNameEditText = findViewById(R.id.edit_product_name);
        mPriceEditText = findViewById(R.id.edit_price);
        mQuantityView = findViewById(R.id.quantity);
        mSupplierSpinner = findViewById(R.id.spinner_supplier);
        mSuppliersNumberEditText = findViewById(R.id.edit_supplier_phone);

        // Setup OnTouchListeners on all the input fields, so we can determine if the user
        // has touched or modified them. This will let us know if there are unsaved changes
        // or not, if the user tries to leave the editor without saving.
        mNameEditText.setOnTouchListener(mTouchListener);
        mPriceEditText.setOnTouchListener(mTouchListener);
        mQuantityView.setOnTouchListener(mTouchListener);
        mSupplierSpinner.setOnTouchListener(mTouchListener);
        mSuppliersNumberEditText.setOnTouchListener(mTouchListener);



        // Button that increases the quantity
        final Button incrementButton = findViewById(R.id.increment);
        incrementButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // On click quantity increases
                if (mCurrentBookUri == null) {
                    mQuantity++;
                } else {
                    // read quantity from TextView
                    // parse it to Integer
                    // increment it
                    // set the new value to TextView
                }
                // display quantity
                displayQuantity();
            }
        });

        // Button that decreases the quantity
        final Button decrementButton =  findViewById(R.id.decrement);
        decrementButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // on click quantity decreases
                decrementButton(v);
                // display quantity
                displayQuantity();
            }
        });


        // Button for ordering the products
        final Button orderNowButton = findViewById(R.id.order_button);
        orderNowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Check that fields are not empty
                if (verifyFields()) {
                    // Proceed with order
                    orderNow();
                }
            }
        });
    }

    private boolean verifyFields() {
        // Read from input fields
        // Use trim to eliminate leading or trailing white space
        String nameString = mNameEditText.getText().toString().trim();
        String priceString = mPriceEditText.getText().toString().trim();
        String phoneNumberString = mSuppliersNumberEditText.getText().toString().trim();

        // Check that all fields in the EditText view are completed
        // No need to check for negative values of price and quantity because
        // only positive inputs are possible as specified in activity_editor.xml inputType (is NOT signed)
        if (TextUtils.isEmpty(nameString) ||
                TextUtils.isEmpty(priceString) ||
                TextUtils.isEmpty(phoneNumberString)) {
            Toast.makeText(this, "Some fields are empty! Check again", Toast.LENGTH_SHORT).show();
            return false;
        } else if (Double.parseDouble(priceString) == 0) {
            Toast.makeText(this, "Price can't be 0", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }

    public void decrementButton(View view) {
        // If quantity = 0 show toast message
        if (mQuantity == 0) {
            Toast.makeText(this, "Can't decrease quantity", Toast.LENGTH_SHORT).show();
        } else {
            // If quantity is not 0, decrease quantity
            mQuantity--;
            displayQuantity();
        }
    }

    // This method displays the quantity of products
    public void displayQuantity() {
        TextView quantityView =  findViewById(R.id.quantity);
        quantityView.setText(String.valueOf(mQuantity));
    }

    // This method display a toast message when the order button is clicked
    public void orderNow() {
        // If quantity id 0, display toast message
        if (mQuantity == 0) {
            Toast.makeText(this, "Quantity is required", Toast.LENGTH_SHORT).show();

        } else {
            // Else display toast message
            Toast.makeText(this, "Products have been successfully ordered", Toast.LENGTH_SHORT).show();

            // Set an intent that makes the user go to the Phone Call
            // when the order is done.
            String phoneNumber = mSuppliersNumberEditText.getText().toString().trim();
            Intent callIntent = new Intent(Intent.ACTION_DIAL);
            callIntent.setData(Uri.parse("tel:" + phoneNumber));
            if (callIntent.resolveActivity(getPackageManager()) != null) {
                startActivity(callIntent);
            }
        }

        setupSpinner();
    }

    /**
     * Setup the dropdown spinner that allows the user to select the gender of the Book.
     */
    private void setupSpinner() {
        // Create adapter for spinner. The list options are from the String array it will use
        // the spinner will use the default layout
        ArrayAdapter genderSpinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.array_supplier_options, android.R.layout.simple_spinner_item);

        // Specify dropdown layout style - simple list view with 1 item per line
        genderSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

        // Apply the adapter to the spinner
        mSupplierSpinner.setAdapter(genderSpinnerAdapter);

        // Set the integer mSelected to the constant values
        mSupplierSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selection = (String) parent.getItemAtPosition(position);
                if (!TextUtils.isEmpty(selection)) {
                    if (selection.equals(getString(R.string.Nahda))) {
                        mSupplier = BookContract.BookEntry.SUPPLIER_NAHDA;
                    } else if (selection.equals(getString(R.string.Lebnan))) {
                        mSupplier = BookContract.BookEntry.SUPPLIER_LEBNAN;
                    } else if (selection.equals(getString(R.string.Salam))) {
                        mSupplier = BookContract.BookEntry.SUPPLIER_SALAM;
                    } else if (selection.equals(getString(R.string.Ahram))) {
                        mSupplier = BookContract.BookEntry.SUPPLIER_AHRAM;
                    } else if (selection.equals(getString(R.string.Shorouq))) {
                        mSupplier = BookContract.BookEntry.SUPPLIER_SHROUQ;
                    } else {
                        mSupplier = BookContract.BookEntry.SUPPLIER_UNKNOWN;
                    }
                }
            }

            // Because AdapterView is an abstract class, onNothingSelected must be defined
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mSupplier = BookContract.BookEntry.SUPPLIER_UNKNOWN;
            }
        });
    }

    /**
     * Get user input from editor and save new product into database.
     */
    private void saveBook() {
        // Read from input fields
        // Use trim to eliminate leading or trailing white space
        String nameString = mNameEditText.getText().toString().trim();
        String PriceString = mPriceEditText.getText().toString().trim();
        String QuantityString = mQuantityView.getText().toString().trim();
        String supplierNumberString = mSuppliersNumberEditText.getText().toString().trim();

        // Check if this is supposed to be a new book
        // and check if all the fields in the editor are blank
        if (mCurrentBookUri == null && TextUtils.isEmpty(nameString) && TextUtils.isEmpty(PriceString) &&
                TextUtils.isEmpty(supplierNumberString) ) {
            // Since no fields were modified, we can return early without creating a new book.
            // No need to create ContentValues and no need to do any ContentProvider operations.
            return;
        }

        // Create a ContentValues object where column names are the keys,
        // and books attributes from the editor are the values.
        ContentValues values = new ContentValues();
        values.put(BookContract.BookEntry.COLUMN_Name, nameString);
        values.put(BookContract.BookEntry.COLUMN_Price, PriceString);
        values.put(BookContract.BookEntry.COLUMN_Supplier_Name, mSupplier);
        values.put(BookContract.BookEntry.COLUMN_Supplier_Phone_Number, supplierNumberString);
        // If the quantity is not provided by the user, don't try to parse the string into an
        // integer value. Use 0 by default.
        int quantity = 0;
        if (!TextUtils.isEmpty(QuantityString)) {
            quantity = Integer.parseInt(QuantityString);
        }
        values.put(BookContract.BookEntry.COLUMN_Quantity, quantity);

        // Determine if this is a new or existing book by checking if mCurrentBookUri is null or not
        if (mCurrentBookUri == null) {
            // This is a NEW book, so insert a new book into the provider,
            // returning the content URI for the new book.
            Uri newUri = getContentResolver().insert(BookContract.BookEntry.CONTENT_URI, values);

            // Show a toast message depending on whether or not the insertion was successful.
            if (newUri == null) {
                // If the new content URI is null, then there was an error with insertion.
                Toast.makeText(this, getString(R.string.editor_insert_book_failed),
                        Toast.LENGTH_SHORT).show();
            } else {
                // Otherwise, the insertion was successful and we can display a toast.
                Toast.makeText(this, getString(R.string.editor_insert_book_successful),
                        Toast.LENGTH_SHORT).show();
            }

        } else {
            // Otherwise this is an EXISTING book, so update the book with content URI: mCurrentBookUri
            // and pass in the new ContentValues. Pass in null for the selection and selection args
            // because mCurrentBookUri will already identify the correct row in the database that
            // we want to modify.
            int rowsAffected = getContentResolver().update(mCurrentBookUri, values, null, null);

            // Show a toast message depending on whether or not the update was successful.
            if (rowsAffected == 0) {
                // If no rows were affected, then there was an error with the update.
                Toast.makeText(this, getString(R.string.editor_update_book_failed),
                        Toast.LENGTH_SHORT).show();

            } else {
                // Otherwise, the update was successful and we can display a toast.
                Toast.makeText(this, getString(R.string.editor_update_book_successful),
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_editor.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_editor, menu);
        return true;
    }
    /**
     * This method is called after invalidateOptionsMenu(), so that the
     * menu can be updated (some menu items can be hidden or made visible).
     */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        // If this is a new pet, hide the "Delete" menu item.
        if (mCurrentBookUri == null) {
            MenuItem menuItem = menu.findItem(R.id.action_delete);
            menuItem.setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Save" menu option
            case R.id.action_save:
                // Save product to database
                saveBook();
                // Exit activity
                finish();
                return true;
            // Respond to a click on the "Delete" menu option
            case R.id.action_delete:
                showDeleteConfirmationDialog();
                return true;
            // Respond to a click on the "Up" arrow button in the app bar
            case android.R.id.home:
                // If the pet hasn't changed, continue with navigating up to parent activity
                // which is the {@link CatalogActivity}.
                if (!mBookHasChanged) {
                    NavUtils.navigateUpFromSameTask(EditorActivity.this);
                    return true;
                }

                // Otherwise if there are unsaved changes, setup a dialog to warn the user.
                // Create a click listener to handle the user confirming that
                // changes should be discarded.
                DialogInterface.OnClickListener discardButtonClickListener =
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                // User clicked "Discard" button, navigate to parent activity.
                                NavUtils.navigateUpFromSameTask(EditorActivity.this);
                            }
                        };

                // Show a dialog that notifies the user they have unsaved changes
                showUnsavedChangesDialog(discardButtonClickListener);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * This method is called when the back button is pressed.
     */
    @Override
    public void onBackPressed() {
        // If the pet hasn't changed, continue with handling back button press
        if (!mBookHasChanged) {
            super.onBackPressed();
            return;
        }

        // Otherwise if there are unsaved changes, setup a dialog to warn the user.
        // Create a click listener to handle the user confirming that changes should be discarded.
        DialogInterface.OnClickListener discardButtonClickListener =
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // User clicked "Discard" button, close the current activity.
                        finish();
                    }
                };

        // Show dialog that there are unsaved changes
        showUnsavedChangesDialog(discardButtonClickListener);
    }

    @Override
    public android.content.Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        // Since the editor shows all book attributes, define a projection that contains
        // all columns from the book table
        String[] projection = {
                BookContract.BookEntry._ID,
                BookContract.BookEntry.COLUMN_Name,
                BookContract.BookEntry.COLUMN_Price,
                BookContract.BookEntry.COLUMN_Quantity,
                BookContract.BookEntry.COLUMN_Supplier_Phone_Number };

        // This loader will execute the ContentProvider's query method on a background thread
        return new CursorLoader(this,   // Parent activity context
                mCurrentBookUri,         // Query the content URI for the current book
                projection,             // Columns to include in the resulting Cursor
                null,                   // No selection clause
                null,                   // No selection arguments
                null);                  // Default sort order
    }


    @Override
    public void onLoadFinished(android.content.Loader<Cursor> loader, Cursor cursor) {
        // Bail early if the cursor is null or there is less than 1 row in the cursor
        if (cursor == null || cursor.getCount() < 1) {
            return;
        }

        // Proceed with moving to the first row of the cursor and reading data from it
        // (This should be the only row in the cursor)
        if (cursor.moveToFirst()) {
            // Find the columns of book attributes that we're interested in
            int nameColumnIndex = cursor.getColumnIndex(BookContract.BookEntry.COLUMN_Name);
            int priceColumnIndex = cursor.getColumnIndex(BookContract.BookEntry.COLUMN_Price);
            int quantityColumnIndex = cursor.getColumnIndex(BookContract.BookEntry.COLUMN_Quantity);
            int phColumnIndex = cursor.getColumnIndex(BookContract.BookEntry.COLUMN_Supplier_Phone_Number);

            // Extract out the value from the Cursor for the given column index
            String name = cursor.getString(nameColumnIndex);
            String price = cursor.getString(priceColumnIndex);
            String quantity = cursor.getString(quantityColumnIndex);
            String ph = cursor.getString(phColumnIndex);

            // Update the views on the screen with the values from the database
            mNameEditText.setText(name);
            mPriceEditText.setText(price);
            mQuantityView.setText(quantity);
            mSuppliersNumberEditText.setText(ph);
        }
    }

    @Override
    public void onLoaderReset(android.content.Loader<Cursor> loader) {
        // If the loader is invalidated, clear out all the data from the input fields.
        mNameEditText.setText("");
        mPriceEditText.setText("");
        mQuantityView.setText("");
        mSuppliersNumberEditText.setText("");
    }
    private void showUnsavedChangesDialog(
            DialogInterface.OnClickListener discardButtonClickListener) {
        // Create an AlertDialog.Builder and set the message, and click listeners
        // for the positive and negative buttons on the dialog.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.unsaved_changes_dialog_msg);
        builder.setPositiveButton(R.string.discard, discardButtonClickListener);
        builder.setNegativeButton(R.string.keep_editing, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Keep editing" button, so dismiss the dialog
                // and continue editing the pet.
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        // Create and show the AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    /**
     * Prompt the user to confirm that they want to delete this pet.
     */
    private void showDeleteConfirmationDialog() {
        // Create an AlertDialog.Builder and set the message, and click listeners
        // for the postivie and negative buttons on the dialog.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.delete_dialog_msg);
        builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Delete" button, so delete the pet.
                deleteBook();
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Cancel" button, so dismiss the dialog
                // and continue editing the pet.
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        // Create and show the AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    /**
     * Perform the deletion of the book in the database.
     */
    private void deleteBook() {
        // Only perform the delete if this is an existing pet.
        if (mCurrentBookUri != null) {
            // Call the ContentResolver to delete the book at the given content URI.
            // Pass in null for the selection and selection args because the mCurrentBookUri
            // content URI already identifies the pet that we want.
            int rowsDeleted = getContentResolver().delete(mCurrentBookUri, null, null);

            // Show a toast message depending on whether or not the delete was successful.
            if (rowsDeleted == 0) {
                // If no rows were deleted, then there was an error with the delete.
                Toast.makeText(this, getString(R.string.editor_delete_book_failed),
                        Toast.LENGTH_SHORT).show();
            } else {
                // Otherwise, the delete was successful and we can display a toast.
                Toast.makeText(this, getString(R.string.editor_delete_book_successful),
                        Toast.LENGTH_SHORT).show();
            }
        }

        // Close the activity
        finish();
    }
}


package com.uyuni.fastlearner;

import com.uyuni.fastlearner.contentprovider.MyFastLearnerContentProvider;
import com.uyuni.fastlearner.db.BaseTable;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class BaseDetailActivity extends Activity {

    private Spinner mCategory;

    private EditText mTitleText;

    private EditText mBodyText;

    private Uri baseUri;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.base_edit);

        mCategory = (Spinner)findViewById(R.id.category);
        mTitleText = (EditText)findViewById(R.id.todo_edit_summary);
        mBodyText = (EditText)findViewById(R.id.todo_edit_description);
        Button confirmButton = (Button)findViewById(R.id.todo_edit_button);

        Bundle extras = getIntent().getExtras();

        // check from the saved Instance
        baseUri = (bundle == null) ? null : (Uri)bundle
                .getParcelable(MyFastLearnerContentProvider.CONTENT_ITEM_TYPE);

        // Or passed from the other activity
        if (extras != null) {
            baseUri = extras.getParcelable(MyFastLearnerContentProvider.CONTENT_ITEM_TYPE);

            fillData(baseUri);
        }

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(mTitleText.getText().toString())) {
                    makeToast();
                } else {
                    setResult(RESULT_OK);
                    finish();
                }
            }

        });
    }

    private void fillData(Uri uri) {
        String[] projection = {
                BaseTable.COLUMN_WORD, BaseTable.COLUMN_DEFINITION, BaseTable.COLUMN_CATEGORY
        };
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            String category = cursor.getString(cursor
                    .getColumnIndexOrThrow(BaseTable.COLUMN_CATEGORY));

            for (int i = 0; i < mCategory.getCount(); i++) {

                String s = (String)mCategory.getItemAtPosition(i);
                if (s.equalsIgnoreCase(category)) {
                    mCategory.setSelection(i);
                }
            }

            mTitleText
                    .setText(cursor.getString(cursor.getColumnIndexOrThrow(BaseTable.COLUMN_WORD)));
            mBodyText.setText(cursor.getString(cursor
                    .getColumnIndexOrThrow(BaseTable.COLUMN_DEFINITION)));

            // always close the cursor
            cursor.close();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        saveState();
        outState.putParcelable(MyFastLearnerContentProvider.CONTENT_ITEM_TYPE, baseUri);
    }

    @Override
    protected void onPause() {
        super.onPause();
        saveState();
    }

    private void saveState() {
        String category = (String)mCategory.getSelectedItem();
        String word = mTitleText.getText().toString();
        String definition = mBodyText.getText().toString();

        // only save if either summary or description
        // is available

        if (definition.length() == 0 && word.length() == 0) {
            return;
        }

        ContentValues values = new ContentValues();
        values.put(BaseTable.COLUMN_CATEGORY, category);
        values.put(BaseTable.COLUMN_WORD, word);
        values.put(BaseTable.COLUMN_DEFINITION, definition);

        if (baseUri == null) {
            // New todo
            baseUri = getContentResolver().insert(MyFastLearnerContentProvider.CONTENT_URI, values);
        } else {
            // Update todo
            getContentResolver().update(baseUri, values, null, null);
        }
    }

    private void makeToast() {
        Toast.makeText(BaseDetailActivity.this, "Please maintain a summary", Toast.LENGTH_LONG)
                .show();
    }

}

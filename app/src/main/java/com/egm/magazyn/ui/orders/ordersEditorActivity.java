package com.egm.magazyn.ui.orders;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;

import com.egm.magazyn.R;
import com.egm.magazyn.data.dbClasses.dbProvider;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import static com.egm.magazyn.data.dbClasses.dbContract.*;;
//import android.content.CursorLoader;

public class ordersEditorActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{

    private TextView customersName, customersID;

    private static final String[] PROJECTION=new String[]{
            ordersEntry._ID,
            ordersEntry.COL_CLIENT_ID,
            ordersEntry.COL_PRODUCTS_IDS,
            ordersEntry.COL_PRODUCTS_LOADED,
            ordersEntry.COL_IS_DELIVERED
    };

    private static final String[] CUSTOMERS_PROJECTION=new String[]{
            customersEntry._ID,
            customersEntry.COL_NAMES,
            customersEntry.COL_SURNAME,
    };

    private Intent intent;

    private static final int EXISTING_LOADER =0;
    private boolean hasChanged=false, operationSuccessful=false;

    private FloatingActionButton save, delete, back;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.orders_editor_layout);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        intent=getIntent();
        if(intent.getData()!=null){
            setTitle(R.string.edit_position);
            LoaderManager.getInstance(this).initLoader(EXISTING_LOADER, null, this);
        }else{
            setTitle(R.string.add_position);
        }
        customersName = (TextView) findViewById(R.id.textView_order_choose_customer);
        customersID = (TextView) findViewById(R.id.textView_order_choose_customer_id);
        save = findViewById(R.id.fab_order_save);
        delete = findViewById(R.id.fab_order_delete);
        back = findViewById(R.id.fab_order_back);
        save.animate().translationY(-getResources().getDimension(R.dimen.fab_spacing));
        delete.animate().translationX(-getResources().getDimension(R.dimen.fab_spacing));
        delete.show();
        save.show();
        back.show();
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getContentResolver().delete(intent.getData(),ordersEntry.TABLE_NAME, PROJECTION);
                finish();
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view   ) {
                saveData(view);
                if(operationSuccessful)
                    finish();
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void saveData(View view){
        String customerString = customersID.getText().toString().trim();
        if(customerString.isEmpty()){
            Snackbar.make(view,
                    getString(R.string.empty_field)+"\n"+getString(R.string.order_choose_customer),
                    Snackbar.LENGTH_LONG).show();
            return;
        }

        dbProvider rp=new dbProvider();

        ContentValues cvs=new ContentValues();
        cvs.put(ordersEntry.COL_CLIENT_ID, customerString);

        if(intent.getData()==null){
            Uri newRowID = getContentResolver().insert(ordersEntry.CONTENT_URI, cvs);
            if(newRowID==null){
                Snackbar.make(view,
                        getString(R.string.row_saved_false),
                        Snackbar.LENGTH_LONG).show();
            }else{
                Snackbar.make(view,
                        getString(R.string.row_saved_true),
                        Snackbar.LENGTH_LONG).show();
                operationSuccessful=true;
            }
        }else{
            int affectedRows=getContentResolver().update(intent.getData(), cvs,
                    ordersEntry.TABLE_NAME, PROJECTION);
            Snackbar.make(view,
                        getString(R.string.rows_updated+affectedRows),
                        Snackbar.LENGTH_LONG).show();
            operationSuccessful=true;
        }
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(this,
                intent.getData(),
                PROJECTION,
                null,null,null);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        if(data.moveToFirst()){
            customersID.setText(data.getString(data.getColumnIndexOrThrow(ordersEntry.COL_CLIENT_ID)));

        }
    }
    View.OnTouchListener onTouchListener=new View.OnTouchListener(){
        @Override
        public boolean onTouch(View v, MotionEvent event){
            hasChanged=true;
            return false;
        }
    };

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        customersName.setText("");
    }
}

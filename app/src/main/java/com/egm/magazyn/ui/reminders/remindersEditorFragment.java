package com.egm.magazyn.ui.reminders;

import android.app.DatePickerDialog;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;

import com.egm.magazyn.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.Calendar;

import static com.egm.magazyn.data.dbproviders.reminders.remindersContract.remindersEntry.*;

public class remindersEditorFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{

    private EditText equipmentName;
    private TextView nextInspectionDate;

    private String equipmentNameText;
    private String nextInspectionDateString;

    private static final String[] PROJECTION=new String[]{
            _ID,
            COLUMN_EQUIPMENT_NAME,
            COLUMN_NEXT_INSPECTION_DATE
    };
    private static final int EXISTING_EQUIPMENT_LOADER=0;
    private boolean hasChanged=false;

    private static final String ARG_PARAM1="param1";
    private static final String ARG_PARAM2="param2";

    private String mParam1;
    private String mParam2;

    private FloatingActionButton menuFAB, close, save, delete, cancel;
    private DatePickerDialog picker;

    public remindersEditorFragment(){

    }

    public static remindersEditorFragment newInstance(String prm1, String prm2){
        remindersEditorFragment frg=new remindersEditorFragment();
        Bundle args=new Bundle();
        args.putString(ARG_PARAM1,prm1);
        args.putString(ARG_PARAM2,prm2);
        frg.setArguments(args);
        return frg;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        final View rootView=inflater.inflate(R.layout.reminders_editor,container,false);
        equipmentName = (EditText) rootView.findViewById(R.id.input_eq_name);
        nextInspectionDate = (TextView) rootView.findViewById(R.id.input_next_inspection_date);
        menuFAB = (FloatingActionButton) rootView.findViewById(R.id.reminders_edit_menu_fab);
        close=(FloatingActionButton) rootView.findViewById(R.id.reminders_edit_close_fab);
        save=(FloatingActionButton) rootView.findViewById(R.id.reminders_edit_save_fab);
        delete=(FloatingActionButton) rootView.findViewById(R.id.reminders_edit_delete_fab);
        cancel=(FloatingActionButton) rootView.findViewById(R.id.reminders_edit_cancel_fab);
        close.hide();
        delete.hide();
        save.hide();
        cancel.hide();
        menuFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showMenu();
            }
        });
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeMenu();
            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Delete button pressed", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                closeMenu();
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Save button pressed", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                closeMenu();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Cancel button pressed", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                replaceFrag(new remindersFragment());
            }
        });
        nextInspectionDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                picker = new DatePickerDialog(rootView.getContext(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                nextInspectionDate.setText(year + " - " + (monthOfYear + 1) + " - " + dayOfMonth);
                            }
                        }, year, month, day);
                picker.show();
            }
        });
        return rootView;
    }

    private void showMenu(){
        menuFAB.hide();
        close.show();
        delete.show();
        save.show();
        cancel.show();
        delete.animate().translationY(-3*getResources().getDimension(R.dimen.fab_spacing));
        save.animate().translationY(-2*getResources().getDimension(R.dimen.fab_spacing));
        cancel.animate().translationY(-getResources().getDimension(R.dimen.fab_spacing));
    }

    private void closeMenu(){
        delete.animate().translationY(0);
        save.animate().translationY(0);
        cancel.animate().translationY(0);
        close.hide();
        delete.hide();
        save.hide();
        cancel.hide();
        menuFAB.show();
    }

    private void replaceFrag(Fragment frag){
        FragmentTransaction ft= getParentFragmentManager().beginTransaction();
        ft.replace(R.id.nav_host_fragment,frag);
        ft.addToBackStack(null);
        ft.commit();
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        return null;
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {

    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {

    }
}

package com.egm.magazyn.ui.clients;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.egm.magazyn.R;

import static com.egm.magazyn.data.dbClasses.dbContract.clientsEntry.*;

public class clientsCursorAdapter extends CursorAdapter{

    public clientsCursorAdapter(Context context, Cursor c){super(context, c, 0);}

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        return LayoutInflater.from(context).inflate(R.layout.clients_list_item, viewGroup,false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView names=(TextView) view.findViewById(R.id.textView_clients_li_names);
        TextView surname=(TextView) view.findViewById(R.id.textView_clients_li_surnames);
        TextView phoneNumber = (TextView) view.findViewById(R.id.textView_clients_li_phone);
        TextView adress = (TextView) view.findViewById(R.id.textView_clients_li_adress);

        String namesText= cursor.getString(cursor.getColumnIndexOrThrow(COL_NAMES));
        String surnameText=cursor.getString(cursor.getColumnIndexOrThrow(COL_SURNAME));
        String phoneNumberString = cursor.getString(cursor.getColumnIndexOrThrow(COL_PHONE_NUMBER));
        String customerAdress = cursor.getString(cursor.getColumnIndexOrThrow(COL_ADRESS));

        names.setText(namesText);
        surname.setText(surnameText);
        phoneNumber.setText(phoneNumberString);
        adress.setText(customerAdress);
    }
}

package com.egm.magazyn.ui.warehouse;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.egm.magazyn.R;

import static com.egm.magazyn.data.dbClasses.dbContract.warehouseEntry;

public class warehouseCursorAdapter extends CursorAdapter{

    public warehouseCursorAdapter(Context context, Cursor c){super(context, c, 0);}

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        return LayoutInflater.from(context).inflate(R.layout.warehouse_list_item, viewGroup,false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView productName=(TextView) view.findViewById(R.id.product_name);
        TextView quantityInfo=(TextView) view.findViewById(R.id.product_quantity_info);

        double isQuantity=cursor.getDouble(cursor.getColumnIndexOrThrow(warehouseEntry.COL_QUANTITY));
        double shouldBe=cursor.getDouble(cursor.getColumnIndexOrThrow(warehouseEntry.COL_QUANTITY_AFTER_LAST_DELIVERY));

        productName.setText(cursor.getString(cursor.getColumnIndexOrThrow(warehouseEntry.COL_PRODUCT_NAME)));
        quantityInfo.setText(Double.toString(isQuantity)+"/"+Double.toString(shouldBe) +"  "+ (int)(isQuantity/shouldBe)*100);
        ProgressBar prgsBar = (ProgressBar) view.findViewById(R.id.product_used);
        prgsBar.setMax(100);
        prgsBar.setMin(0);
        prgsBar.setProgress((int)((isQuantity/shouldBe)*100),true);
    }
}

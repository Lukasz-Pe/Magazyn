package com.egm.magazyn.ui.reminders;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.egm.magazyn.R;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;

import static com.egm.magazyn.data.dbClasses.dbContract.*;

public class remindersCursorAdapter extends CursorAdapter{

    public remindersCursorAdapter(Context context, Cursor c){super(context, c, 0);}

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        return LayoutInflater.from(context).inflate(R.layout.reminders_list_item, viewGroup,false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView equipmentName=(TextView) view.findViewById(R.id.equipment_name);
        TextView nextInspectionDate=(TextView) view.findViewById(R.id.inspection_date);
        TextView serialNumber=(TextView) view.findViewById(R.id.equipment_serial_number);
        TextView timeTillNextInspectionDate=(TextView) view.findViewById(R.id.time_till_inspection);

        String equipmentNameText= cursor.getString(cursor.getColumnIndexOrThrow(remindersEntry.COLUMN_EQUIPMENT_NAME));
        String equipmentSerialNumberText=cursor.getString(cursor.getColumnIndexOrThrow(remindersEntry.COLUMN_SERIAL_NUMBER));
        String nextInspectionDateText= cursor.getString(cursor.getColumnIndexOrThrow(remindersEntry.COLUMN_NEXT_INSPECTION_DATE));
        DateTimeFormatter formatter=DateTimeFormatter.ofPattern("uuuu-M-d");
        LocalDate NextInspectionDateDate = LocalDate.parse(nextInspectionDateText, formatter);
        Period diff = Period.between(LocalDate.now(),NextInspectionDateDate);
        String timeTillNextInspectionText = diff.getYears()+"-"+diff.getMonths()+"-"+diff.getDays();

        equipmentName.setText(equipmentNameText);
        serialNumber.setText(equipmentSerialNumberText);
        nextInspectionDate.setText(nextInspectionDateText);
        timeTillNextInspectionDate.setText(timeTillNextInspectionText);

    }
}

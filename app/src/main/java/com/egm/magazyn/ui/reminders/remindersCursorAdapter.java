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

import static com.egm.magazyn.data.dbproviders.reminders.remindersContract.*;

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
        TextView timeTillNextInspectionDate=(TextView) view.findViewById(R.id.time_till_inspection);

        String equipmentNameText= context.getString(cursor.getColumnIndexOrThrow(remindersEntry.COLUMN_EQUIPMENT_NAME));
        String nextInspectionDateText= context.getString(cursor.getColumnIndexOrThrow(remindersEntry.COLUMN_NEXT_INSPECTION_DATE));
        LocalDate NextInspectionDateDate = LocalDate.parse(nextInspectionDateText);
        Period diff = Period.between(LocalDate.now(),NextInspectionDateDate);
        String timeTillNextInspectionText = diff.getYears()+"-"+diff.getMonths()+"-"+diff.getDays();

        equipmentName.setText(equipmentNameText);
        nextInspectionDate.setText(nextInspectionDateText);
        timeTillNextInspectionDate.setText(timeTillNextInspectionText);
    }
}


/*
From String to Date

String dtStart = "2010-10-15T09:27:37Z";
SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
try {
    Date date = format.parse(dtStart);
    System.out.println(date);
} catch (ParseException e) {
    e.printStackTrace();
}
From Date to String

SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
try {
    Date date = new Date();
    String dateTime = dateFormat.format(date);
    System.out.println("Current Date Time : " + dateTime);
} catch (ParseException e) {
    e.printStackTrace();
}
 */
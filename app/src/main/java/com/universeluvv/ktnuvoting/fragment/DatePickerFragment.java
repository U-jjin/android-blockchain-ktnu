package com.universeluvv.ktnuvoting;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.widget.DatePicker;

import androidx.fragment.app.DialogFragment;

import com.universeluvv.ktnuvoting.VoteAddActivity;

import java.util.Calendar;

public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

        int year,month, day;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        Calendar c =Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);              // MONTH : 0~11
        day = c.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog datePickerDialog =new DatePickerDialog(getActivity(),this, year, month, day);

        datePickerDialog.getDatePicker().setMinDate(c.getTimeInMillis());
        // Create a new instance of DatePickerDialog and return it
        return datePickerDialog;
    }


    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

        VoteAddActivity activity =(VoteAddActivity)getActivity();

        String date = year+"."+(month+1)+"."+dayOfMonth;
        activity.date_tv.setTextColor(this.getResources().getColor(R.color.colorPoint));
        activity.date_tv.setText(date);
    }
}

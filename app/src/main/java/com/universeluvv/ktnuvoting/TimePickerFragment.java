package com.universeluvv.ktnuvoting;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {

    String h,m;
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        Calendar c =Calendar.getInstance();



        TimePickerDialog timePickerDialog =new TimePickerDialog(getActivity(),this,0,0,true);

        return timePickerDialog;
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

        VoteAddActivity activity =(VoteAddActivity)getActivity();

        if(hourOfDay<10) h="0"+hourOfDay;
        else h=hourOfDay+"";
        if(minute<10) m ="0"+minute;
        else m=minute+"";

        String time = h+":"+m;
        activity.time_tv.setTextColor(this.getResources().getColor(R.color.colorPoint));
        activity.time_tv.setText(time);

    }

}

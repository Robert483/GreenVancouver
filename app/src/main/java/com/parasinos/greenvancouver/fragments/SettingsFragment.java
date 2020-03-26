package com.parasinos.greenvancouver.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.NumberPicker;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.parasinos.greenvancouver.R;

import java.util.Objects;

public class SettingsFragment extends Fragment implements NumberPicker.OnValueChangeListener{


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_settings, container, false);
        NumberPicker numberPicker = v.findViewById(R.id.numberPicker);
        int minNum = 1;
        numberPicker.setMinValue(minNum);
        int maxNum = 20;
        numberPicker.setMaxValue(maxNum);
        numberPicker.setValue(10);
        numberPicker.setOnValueChangedListener(this);

        return v;
    }

    @Override
    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {

        SharedPreferences sharedPreferences = Objects.requireNonNull(this.getActivity()).getSharedPreferences("pref", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("maxResult", newVal);
        editor.apply();

    }
}
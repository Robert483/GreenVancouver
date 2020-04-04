package com.parasinos.greenvancouver.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.parasinos.greenvancouver.R;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

public class SettingsFragment extends Fragment implements NumberPicker.OnValueChangeListener {
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

        TextView textView = v.findViewById(R.id.termsOfService);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("https://firebasestorage.googleapis.com/v0/b/greenvancouver.appspot.com/o/terms.txt?alt=media&token=b4cbeab6-d2ee-4dcf-b564-2834d5dfac80"));
                startActivity(intent);
            }
        });

        textView = v.findViewById(R.id.txtv_aboutus);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("https://firebasestorage.googleapis.com/v0/b/greenvancouver.appspot.com/o/about_us.txt?alt=media&token=222f78a1-392f-4c1d-be3d-f8e02e0b24ef"));
                startActivity(intent);
            }
        });

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

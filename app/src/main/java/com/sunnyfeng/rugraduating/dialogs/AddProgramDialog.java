package com.sunnyfeng.rugraduating.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.sunnyfeng.rugraduating.R;

public class AddProgramDialog extends Dialog implements android.view.View.OnClickListener, AdapterView.OnItemSelectedListener  {

    private Activity activity;
    private Button yes, no;

    public AddProgramDialog(Activity activity) {
        super(activity);
        this.activity = activity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_add_program);
        yes = findViewById(R.id.dialog_ok_add_program);
        no = findViewById(R.id.dialog_cancel_add_program);
        yes.setOnClickListener(this);
        no.setOnClickListener(this);

        // Drop down menu for majors
        Spinner spinner = findViewById(R.id.dialog_spin_course_add_program);
        //TODO: get majors from database instead of using placeholder "R.array.major_array"
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(activity,
                R.array.major_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.dialog_ok_add_program:
                // TODO: include functionality to add the selected program to the DB
                dismiss();
                break;
            case R.id.dialog_cancel_add_program:
                dismiss();
                break;
            default:
                break;
        }
        dismiss();
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        //TODO: save the chosen program when it's selected
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) { }
}

package com.sunnyfeng.rugraduating.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;

import com.sunnyfeng.rugraduating.R;

public class AddCourseDialog extends Dialog implements android.view.View.OnClickListener, AdapterView.OnItemSelectedListener  {

    private Activity activity;
    private Button yes, no;

    public AddCourseDialog(Activity activity) {
        super(activity);
        this.activity = activity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_add_class);
        yes = findViewById(R.id.dialog_ok_add_course);
        no = findViewById(R.id.dialog_cancel_add_course);
        yes.setOnClickListener(this);
        no.setOnClickListener(this);

        // Drop down menu for majors
        EditText editText = findViewById(R.id.course_code_editText);
        String courseCode = editText.getText().toString();
        //TODO: add class into database for student

    }

    @Override
    public void onClick(View v) {
        // TODO: add functionality
        switch (v.getId()) {
            case R.id.dialog_ok_add_course:
                dismiss();
                break;
            case R.id.dialog_cancel_add_course:
                dismiss();
                break;
            default:
                break;
        }
        dismiss();
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}

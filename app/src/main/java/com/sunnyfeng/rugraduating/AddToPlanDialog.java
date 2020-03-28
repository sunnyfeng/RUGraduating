package com.sunnyfeng.rugraduating;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;

public class AddToPlanDialog extends Dialog implements android.view.View.OnClickListener {

    private Activity activity;
    private Button yes, no;

    public AddToPlanDialog(Activity activity) {
        super(activity);
        // TODO Auto-generated constructor stub
        this.activity = activity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_add_to_plan);
        yes = findViewById(R.id.dialog_ok_add_course);
        no = findViewById(R.id.dialog_cancel_add_course);
        yes.setOnClickListener(this);
        no.setOnClickListener(this);

        //TODO: add spinners
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
}

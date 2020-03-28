package com.sunnyfeng.rugraduating;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;

public class AddProgramDialog extends Dialog implements android.view.View.OnClickListener {

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

        //TODO: add spinner
    }

    @Override
    public void onClick(View v) {
        // TODO: add functionality
        switch (v.getId()) {
            case R.id.dialog_ok_add_program:
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
}

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
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.sunnyfeng.rugraduating.R;
import com.sunnyfeng.rugraduating.objects.User;

import java.util.ArrayList;

public class AddProgramDialog extends Dialog implements android.view.View.OnClickListener, AdapterView.OnItemSelectedListener  {

    private Activity activity;
    private Button yes, no;
    private String selectedProgram = "";

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
        ArrayList<String> untakenPrograms = new ArrayList<String>();
        ArrayAdapter<String> programAdapter = new ArrayAdapter<String>(activity, android.R.layout.simple_spinner_item, untakenPrograms);
        programAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(programAdapter);
        spinner.setOnItemSelectedListener(this);

        RequestQueue requests = Volley.newRequestQueue(activity);

        User mUser = ((User) activity.getApplicationContext());
        String netID = mUser.getNetID();

        String url = "https://webhooks.mongodb-stitch.com/api/client/v2.0/app/degreenav-uuidd/service/webhookTest/incoming_webhook/getUnenrolledPrograms?netID=" + netID;
        JsonObjectRequest getUntakenPrograms = new JsonObjectRequest(Request.Method.POST, url, null, response -> {
            try{
                int count = 0;
                while (count < response.length()) {
                    untakenPrograms.add(response.getJSONObject(Integer.toString(count)).getString("name"));
                    count++;
                }
                programAdapter.notifyDataSetChanged();
            } catch (Exception e) {
                System.out.println(e.toString());
            }

        }, error -> {
            //TODO: Handle error gracefully
            System.out.println(error);
        });
        requests.add(getUntakenPrograms);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.dialog_ok_add_program:
                // TODO: include functionality to add the selected program to the DB
                User mUser = ((User) activity.getApplicationContext());
                String netID = mUser.getNetID();
                RequestQueue requests = Volley.newRequestQueue(activity);
                String url = "https://webhooks.mongodb-stitch.com/api/client/v2.0/app/degreenav-uuidd/service/webhookTest/incoming_webhook/addProgram?netID=" + netID + "&program=" + selectedProgram;
                JsonObjectRequest registerStudent = new JsonObjectRequest(Request.Method.POST, url, null, response -> {
                    try{
                        boolean isSuccessful = response.getBoolean("0");
                        if (isSuccessful) {
                            TextView dialogText = (TextView)findViewById(R.id._dialog_add_program_string);
                            dialogText.setText("Program added!");
                            wait(2500);
                            dismiss();
                        } else {
                            TextView dialogText = (TextView)findViewById(R.id._dialog_add_program_string);
                            dialogText.setText("Could not add program. Try again later.");
                            wait(2500);
                            dismiss();
                        }

                    } catch (Exception e) {
                        System.out.println(e.toString());
                    }

                }, error -> {
                    //TODO: Handle error gracefully
                    System.out.println(error);
                });
                requests.add(registerStudent);

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
    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        //TODO: save the chosen program when it's selected
        String itemSelected = parent.getItemAtPosition(pos).toString();
        selectedProgram = itemSelected;
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) { }
}

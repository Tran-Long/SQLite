package com.example.sqliteexamples;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.Nullable;

public class StudentFormActivity extends Activity {
    String ten, mssv, ntns, diachi, mail;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);

        Intent intent = getIntent();
        EditText name = findViewById(R.id.name_form);
        EditText id = findViewById(R.id.id_form);
        EditText dob = findViewById(R.id.dob_form);
        EditText address = findViewById(R.id.address_form);
        EditText email = findViewById(R.id.email_form);

        findViewById(R.id.but_confirm_form).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                ten = name.getText().toString();
                mssv = id.getText().toString();
                ntns = dob.getText().toString();
                diachi = address.getText().toString();
                mail = email.getText().toString();
                intent.putExtra("name", ten);
                intent.putExtra("id", mssv);
                intent.putExtra("dob", ntns);
                intent.putExtra("address", diachi);
                intent.putExtra("email", mail);
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        });
    }
}

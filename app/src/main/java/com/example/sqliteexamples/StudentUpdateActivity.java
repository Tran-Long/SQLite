package com.example.sqliteexamples;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class StudentUpdateActivity extends Activity {
    TextView mssv;
    EditText hoten, ngaysinh, diachi, email;
    String s_mssv, s_hoten, s_ngaysinh, s_diachi, s_email;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_form);
        mssv = findViewById(R.id.tv_mssv);
        mssv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(StudentUpdateActivity.this, "This field cannot be modified" +
                        "\nPlease add new student if you want to change this field", Toast.LENGTH_LONG).show();
            }
        });
        hoten = findViewById(R.id.edt_hoten);
        ngaysinh = findViewById(R.id.edt_dob);
        diachi = findViewById(R.id.edt_address);
        email = findViewById(R.id.edt_email);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        s_mssv = bundle.getString("mssv");
        s_hoten = bundle.getString("ten");
        s_ngaysinh = bundle.getString("ngaysinh");
        s_diachi = bundle.getString("diachi");
        s_email = bundle.getString("email");

        mssv.setText(s_mssv);
        hoten.setText(s_hoten);
        ngaysinh.setText(s_ngaysinh);
        diachi.setText(s_diachi);
        email.setText(s_email);

        findViewById(R.id.btn_back_update).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!hoten.getText().toString().equals(s_hoten) ||
                        !ngaysinh.getText().toString().equals(s_ngaysinh) ||
                        !diachi.getText().toString().equals(s_diachi) ||
                        !email.getText().toString().equals(s_email)){

                    Dialog dialog = new Dialog(StudentUpdateActivity.this);
                    dialog.setContentView(R.layout.dialog_alert_back);

                    dialog.findViewById(R.id.btn_save_alert).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            bundle.putString("r_mssv", mssv.getText().toString());
                            bundle.putString("r_ten", hoten.getText().toString());
                            bundle.putString("r_ngaysinh", ngaysinh.getText().toString());
                            bundle.putString("r_diachi", diachi.getText().toString());
                            bundle.putString("r_email", email.getText().toString());
                            intent.putExtras(bundle);
                            setResult(Activity.RESULT_OK, intent);
                            finish();
                        }
                    });

                    dialog.findViewById(R.id.btn_dont_alert).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            bundle.putString("r_mssv", s_mssv);
                            bundle.putString("r_ten", s_hoten);
                            bundle.putString("r_ngaysinh", s_ngaysinh);
                            bundle.putString("r_diachi", s_diachi);
                            bundle.putString("r_email", s_email);
                            intent.putExtras(bundle);
                            setResult(Activity.RESULT_OK, intent);
                            finish();
                        }
                    });
                    dialog.setCanceledOnTouchOutside(true);
                    dialog.show();
                }else{
                    bundle.putString("r_mssv", s_mssv);
                    bundle.putString("r_ten", s_hoten);
                    bundle.putString("r_ngaysinh", s_ngaysinh);
                    bundle.putString("r_diachi", s_diachi);
                    bundle.putString("r_email", s_email);
                    intent.putExtras(bundle);
                    setResult(Activity.RESULT_OK, intent);
                    finish();
                }

            }
        });

        findViewById(R.id.btn_confirm_update).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                s_mssv = mssv.getText().toString();
                s_hoten = hoten.getText().toString();
                s_ngaysinh = ngaysinh.getText().toString();
                s_diachi = diachi.getText().toString();
                s_email = email.getText().toString();
            }
        });
    }
}

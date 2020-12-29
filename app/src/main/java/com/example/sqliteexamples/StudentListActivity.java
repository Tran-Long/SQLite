package com.example.sqliteexamples;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import io.bloco.faker.Faker;

public class StudentListActivity extends AppCompatActivity {

    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_list);

        String dataPath = getFilesDir() + "/student_data";
        db = SQLiteDatabase.openDatabase(dataPath, null, SQLiteDatabase.CREATE_IF_NECESSARY);

//        createRandomData();
        testDB();
        ListView listView = findViewById(R.id.list_students);
        StudentAdapter adapter = new StudentAdapter(db);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(StudentListActivity.this, StudentInfoActivity.class);
                Bundle bundle = new Bundle();
                Cursor cursor = (Cursor) adapter.getItem(position);

                bundle.putString("mssv", cursor.getString(cursor.getColumnIndex("mssv")));
                bundle.putString("name", cursor.getString(cursor.getColumnIndex("hoten")));
                bundle.putString("dob", cursor.getString(cursor.getColumnIndex("ngaysinh")));
                bundle.putString("email", cursor.getString(cursor.getColumnIndex("email")));
                bundle.putString("address", cursor.getString(cursor.getColumnIndex("diachi")));

                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        // context menu
        registerForContextMenu(listView);
        listView.setLongClickable(true);

        findViewById(R.id.but_add).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StudentListActivity.this, StudentFormActivity.class);
                intent.putExtra("none", "none");
                startActivityForResult(intent, 177);
                adapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 177){
            if(resultCode == RESULT_OK){
                String mssv = data.getStringExtra("id");
                String ten = data.getStringExtra("name");
                String ngaysinh = data.getStringExtra("dob");
                String diachi = data.getStringExtra("address");
                String email = data.getStringExtra("email");

            }
        }
    }

    private void createRandomData() {
        db.beginTransaction();
        try {
            db.execSQL("create table sinhvien(" +
                    "mssv char(8) primary key," +
                    "hoten text," +
                    "ngaysinh date," +
                    "email text," +
                    "diachi text);");

            Faker faker = new Faker();
            for (int i = 0; i < 20; i++) {
                String mssv = "2017" + faker.number.number(4);
                String hoten = faker.name.name();
                String ngaysinh = faker.date.birthday(18, 22).toString();
                String email = faker.internet.email();
                String diachi = faker.address.city() + ", " + faker.address.country();
                String sql = String.format("insert into sinhvien(mssv, hoten, ngaysinh, email, diachi) " +
                        "values('%s', '%s', '%s', '%s', '%s')", mssv, hoten, ngaysinh, email, diachi);

                db.execSQL(sql);
            }

            db.setTransactionSuccessful();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            db.endTransaction();
        }

    }

    private void appendData(String ten, String mssv, String ngaysinh, String diachi, String email){
        String dataPath = getFilesDir() + "/student_data";
        db = SQLiteDatabase.openDatabase(dataPath, null, SQLiteDatabase.CREATE_IF_NECESSARY);
        db.beginTransaction();
        try {
            String sql = String.format("insert into sinhvien(mssv, hoten, ngaysinh, email, diachi) " +
                    "values('%s', '%s', '%s', '%s', '%s')", mssv, ten, ngaysinh, email, diachi);
            db.execSQL(sql);
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
        finally{
            db.endTransaction();
            db.close();
        }
    }

    @Override
    protected void onStop() {
        db.close();
        super.onStop();
    }

    public void testDB() {
        String query = "select *from sinhvien where number < 5";
        Cursor c1 = db.rawQuery(query, null);
        c1.moveToPosition(-1);
        Log.v("TAG", "records " + c1.getCount());
        while (c1.moveToNext()) {
            int num = c1.getInt(0);
            String id = c1.getString(1);
            String name = c1.getString(2);
            String dob = c1.getString(3);
            String mail = c1.getString(4);
            String address = c1.getString(5);
            Log.v("TAG", String.valueOf(num) + " - " + id + " - " + name + " - " + dob
                    + " - " + mail + " - " + address);
        }
    }
}
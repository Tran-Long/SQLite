package com.example.sqliteexamples;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import io.bloco.faker.Faker;

public class StudentListActivity extends AppCompatActivity {

    SQLiteDatabase db;
    StudentAdapter adapter;
    SearchView searchView;
    ListView listView;
    String dataPath;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_list);
        dataPath = getFilesDir() + "/student_data";
        db = SQLiteDatabase.openDatabase(dataPath, null, SQLiteDatabase.CREATE_IF_NECESSARY);

//        createRandomData();
        listView = findViewById(R.id.list_students);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(StudentListActivity.this, StudentInfoActivity.class);
                Cursor cs = (Cursor) adapter.getItem(position);
                Bundle bundle = new Bundle();
                bundle.putString("mssv", cs.getString(cs.getColumnIndex("mssv")));
                bundle.putString("ten", cs.getString(cs.getColumnIndex("hoten")));
                bundle.putString("ngaysinh", cs.getString(cs.getColumnIndex("ngaysinh")));
                bundle.putString("diachi", cs.getString(cs.getColumnIndex("diachi")));
                bundle.putString("email", cs.getString(cs.getColumnIndex("email")));

                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        adapter = new StudentAdapter(db);
        listView.setAdapter(adapter);

        // context menu
        registerForContextMenu(listView);
        listView.setLongClickable(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_option, menu);
        searchView = (SearchView)menu.findItem(R.id.action_search).getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                String sql = "";
                if(query.equals("")){
                    sql = "select * from sinhvien";
                }else{
                    sql = "select * from sinhvien where mssv like '%" + query + "%' or hoten like '%" + query +"%'";
                }

                Cursor cs = db.rawQuery(sql, null);
                adapter.setCursor(cs);
                adapter.notifyDataSetChanged();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                String sql = "";
                if(newText.equals("")){
                    sql = "select * from sinhvien";
                }else{
                    sql = "select * from sinhvien where mssv like '%" + newText + "%' or hoten like '%" + newText +"%'";
                }

                Cursor cs = db.rawQuery(sql, null);
                adapter.setCursor(cs);
                adapter.notifyDataSetChanged();
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id  = item.getItemId();
        if(id == R.id.action_add){
            Intent intent = new Intent(StudentListActivity.this, StudentFormActivity.class);
            intent.putExtra("none", "none");
            startActivityForResult(intent, 177);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.menu_context, menu);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        Cursor cs = (Cursor) adapter.getItem(info.position);
        Log.v("TAG", "item at pos " + info.position);
        int id  = item.getItemId();
        if(id == R.id.action_update){
            Intent intent = new Intent(StudentListActivity.this, StudentUpdateActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("mssv", cs.getString(cs.getColumnIndex("mssv")));
            bundle.putString("ten", cs.getString(cs.getColumnIndex("hoten")));
            bundle.putString("ngaysinh", cs.getString(cs.getColumnIndex("ngaysinh")));
            bundle.putString("diachi", cs.getString(cs.getColumnIndex("diachi")));
            bundle.putString("email", cs.getString(cs.getColumnIndex("email")));

            intent.putExtras(bundle);
            startActivityForResult(intent, 221);
        }else if (id == R.id.action_select){

        }else if (id == R.id.action_delete){
            AlertDialog dialog = new AlertDialog.Builder(StudentListActivity.this)
                    .setMessage("Are you sure to remove this student ?")
                    .setTitle("Remove student")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String mssv = cs.getString(cs.getColumnIndex("mssv"));
                            db.beginTransaction();
                            try{
                                int r = db.delete("sinhvien", "mssv = '" +mssv+ "'", null);
                                if(r > 0){
                                    Toast.makeText(StudentListActivity.this, "Successfully removed", Toast.LENGTH_SHORT).show();
                                }
                                db.setTransactionSuccessful();
                            }catch(Exception exception){
                                exception.printStackTrace();
                            }finally{
                                db.endTransaction();
                                adapter.resetView();
                                adapter.notifyDataSetChanged();
                            }
                        }
                    })
                    .setNegativeButton("No", null)
                    .create();
            dialog.setCanceledOnTouchOutside(true);
            dialog.show();
        }
        return super.onContextItemSelected(item);
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
                db.beginTransaction();
                try {
                    String sql = String.format("insert into sinhvien(mssv, hoten, ngaysinh, email, diachi) " +
                            "values('%s', '%s', '%s', '%s', '%s')", mssv, ten, ngaysinh, email, diachi);
                    db.execSQL(sql);
                    db.setTransactionSuccessful();
                }
                catch (Exception ex){
                    ex.printStackTrace();
                }
                finally{
                    db.endTransaction();
                    adapter.resetView();
                    adapter.notifyDataSetChanged();
                }
            }
        }else if (requestCode == 221){
            if(resultCode == RESULT_OK) {
                Bundle bundle = data.getExtras();
                String mssv, ten, ngaysinh, diachi, email;
                mssv = bundle.getString("r_mssv");
                ten = bundle.getString("r_ten");
                ngaysinh = bundle.getString("r_ngaysinh");
                diachi = bundle.getString("r_diachi");
                email = bundle.getString("r_email");
                Log.v("TAG", "name : " + ten
                                        + "\n ngaysinh: " + ngaysinh
                                        + "\n email: " + email);
                db.beginTransaction();
                try {
                    ContentValues cv = new ContentValues();
                    cv.put("hoten", ten);
                    cv.put("ngaysinh", ngaysinh);
                    cv.put("diachi", diachi);
                    cv.put("email", email);
                    int r = db.update("sinhvien", cv, "mssv = '" + mssv + "'", null);
                    Log.v("TAG", "row " + r);
                    db.setTransactionSuccessful();
                } catch (Exception exception) {
                    exception.printStackTrace();
                } finally {
                    db.endTransaction();
                    adapter.resetView();
                    adapter.notifyDataSetChanged();
                }
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
            for (int i = 0; i < 10; i++) {
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


    @Override
    protected void onDestroy() {
        db.close();
        super.onDestroy();
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
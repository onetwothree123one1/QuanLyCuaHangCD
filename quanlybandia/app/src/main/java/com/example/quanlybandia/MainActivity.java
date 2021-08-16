package com.example.quanlybandia;

import androidx.appcompat.app.AppCompatActivity;
import android.database.Cursor;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import java.util.ArrayList;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {
    final String DATABASE_NAME = "dulieu.sqlite";
    SQLiteDatabase database;
    ListView listView;
    ArrayList<CD> list;
    AdapterCD adapter;
    Button btnAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        database = Database.initDatabase(this, DATABASE_NAME);
        Cursor cursor = database.rawQuery("SELECT * FROM CD", null);
        cursor.moveToFirst();

        addControls();
        readData();
    }

    private void addControls() {
        btnAdd = (Button) findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(new View.OnClickListener(){
                                      @Override
                                      public void onClick(View v) {
                                          Intent intent = new Intent(MainActivity.this, AddActivity.class);
                                          startActivity(intent);
                                      }
                                  });
        listView = findViewById(R.id.listView);
        list = new ArrayList<>();
        adapter = new AdapterCD(this, list);
        listView.setAdapter(adapter);
    }

    private void readData() {
        database = Database.initDatabase(this, DATABASE_NAME);
        Cursor cursor = database.rawQuery("SELECT * FROM CD", null);
        list.clear();
        for (int i = 0; i < cursor.getCount(); i++) {
            cursor.moveToPosition(i);

            int MaCD = cursor.getInt(0);
            String TuaCD = cursor.getString(1);
            String DonGia = cursor.getString(2);
            String SoLuong = cursor.getString(3);
            byte[] Anh = cursor.getBlob(4);
            list.add(new CD(MaCD, TuaCD, DonGia, SoLuong, Anh));

        }
        adapter.notifyDataSetChanged();

    }
}
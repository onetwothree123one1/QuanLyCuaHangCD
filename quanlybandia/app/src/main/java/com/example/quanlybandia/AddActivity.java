package com.example.quanlybandia;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class AddActivity extends AppCompatActivity {
    final String DATABASE_NAME = "dulieu.sqlite";
    final int RESQUEST_TAKE_PHOTO = 123;
    final int REQUEST_CHOOSE_PHOTO = 321;
    int MaCD = -1;
    Button btnThemAnh, btnChupAnh, btnsua, btnhuy;
    EditText edtTuaCD, edtDonGia, edtSoLuong;
    ImageView imgdaidienanh;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        addControls();
        addEvents();
    }
    private void addControls() {
        btnThemAnh = (Button) findViewById(R.id.btnThemAnh);
        btnChupAnh = (Button) findViewById(R.id.btnChupAnh);
        btnsua = (Button) findViewById(R.id.btnsua);
        btnhuy = (Button) findViewById(R.id.btnhuy);
        edtTuaCD = (EditText) findViewById(R.id.edtTuaCD);
        edtDonGia = (EditText) findViewById(R.id.edtDonGia);
        edtSoLuong = (EditText) findViewById(R.id.edtSoLuong);
        imgdaidienanh = (ImageView) findViewById(R.id.imgdaidienanh);
    }

    private void addEvents(){
        btnThemAnh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choosePhoto();
            }
        });
        btnChupAnh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePicture();
            }
        });

        btnsua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insert();
            }
        });
        btnhuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancel();
            }
        });
    }

    private void takePicture(){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, RESQUEST_TAKE_PHOTO);
    }

    private void choosePhoto(){
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_CHOOSE_PHOTO);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CHOOSE_PHOTO) {
                try {
                    Uri imageUri = data.getData();
                    InputStream is = getContentResolver().openInputStream(imageUri);
                    Bitmap bitmap = BitmapFactory.decodeStream(is);
                    imgdaidienanh.setImageBitmap(bitmap);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            } else if (requestCode == RESQUEST_TAKE_PHOTO) {
                Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                imgdaidienanh.setImageBitmap(bitmap);
            }
        }
    }

    private void insert(){
        String TuaCD = edtTuaCD.getText().toString();
        String DonGia = edtDonGia.getText().toString();
        String SoLuong = edtSoLuong.getText().toString();
        byte[] Anh = getByteArrayFromImageView(imgdaidienanh);
        ContentValues contentValues = new ContentValues();
        contentValues.put("TuaCD", TuaCD);
        contentValues.put("SoLuong", SoLuong);
        contentValues.put("DonGia", DonGia);
        contentValues.put("Anh", Anh);


        SQLiteDatabase database = Database.initDatabase(this, "dulieu.sqlite");
        database.insert("CD",null, contentValues);
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);

    }

    private void cancel(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private byte[] getByteArrayFromImageView(ImageView imgv){

        BitmapDrawable drawable = (BitmapDrawable) imgv.getDrawable();
        Bitmap bmp = drawable.getBitmap();

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
    }
}
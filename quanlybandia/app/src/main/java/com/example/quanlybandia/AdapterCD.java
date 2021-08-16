package com.example.quanlybandia;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;


public class AdapterCD extends BaseAdapter {
    Activity context;
    ArrayList<CD> list;

    public AdapterCD(Activity context, ArrayList<CD> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, final View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.listviewrow, null);
        ImageView imgdaidien = (ImageView) row.findViewById(R.id.imgdaidienanh);
        TextView txtmacd = (TextView) row.findViewById(R.id.txtmacd);
        TextView txttuacd = (TextView) row.findViewById(R.id.txttuacd);
        TextView txtdongia = (TextView) row.findViewById(R.id.txtdongia);
        TextView txtsoluong = (TextView) row.findViewById(R.id.txtsoluong);
        Button btnxoa = (Button) row.findViewById(R.id.btnxoa);
        Button btnsua = (Button) row.findViewById(R.id.btnsua);


        final CD CD = list.get(position);
        txtmacd.setText(CD.MaCD + "");
        txttuacd.setText(CD.TuaCd);
        txtdongia.setText(CD.DonGia);
        txtsoluong.setText(CD.SoLuong);
        Bitmap bmdaidien = BitmapFactory.decodeByteArray(CD.Anh, 0, CD.Anh.length);
        imgdaidien.setImageBitmap(bmdaidien);
        btnsua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ActivityUpdate.class);
                intent.putExtra("maCD", CD.MaCD);

                context.startActivity(intent);
            }
        });
        btnxoa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setIcon(android.R.drawable.ic_delete);
                builder.setTitle("Xác nhận xóa");
                builder.setMessage("Bạn có chắc chắn muốn xóa nhân viên này?");
                builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        delete(CD.MaCD);
                    }
                });
                builder.setNegativeButton("Không", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
        return row;
    }

    private void delete(int MaCDDia) {
        SQLiteDatabase database = Database.initDatabase(context,"dulieu.sqlite");
        ((SQLiteDatabase) database).delete("CD", "MaCD = ?", new String[]{MaCDDia + ""});
        list.clear();
        Cursor cursor = database.rawQuery("SELECT * FROM CD", null);
        while (cursor.moveToNext()){
            int MaCD = cursor.getInt(0);
            String TuaCD = cursor.getString(1);
            String SoLuong = cursor.getString(2);
            String DonGia = cursor.getString(3);

            byte[] Anh = cursor.getBlob(4);

            list.add(new CD(MaCD, TuaCD, SoLuong,DonGia, Anh));
        }
        notifyDataSetChanged();
    }
}

package com.example.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.ListView;

import androidx.annotation.Nullable;

import com.example.myapplication.models.Quote;

import java.util.ArrayList;

public class FavQuotesOpenHelperDb extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Qotes.db";
    public static final String CREATE_SQL_QUOTES = String.format("CREATE TABLE %s (%s INTEGER PRIMARY KEY %s TEXT %S TEXT",
            FavQuotesContract.Infos.TABLE_NAME,
            FavQuotesContract.Infos.COLUMN_NAME_ID,
            FavQuotesContract.Infos.COLUMN_NAME_QUOTE,
            FavQuotesContract.Infos.COLUMN_NAME_AUTHOR);
    public static final String DELETE_SQL_QUOTES = String.format("DROP TABLE IF EXIST %s",
            FavQuotesContract.Infos.TABLE_NAME);


    public FavQuotesOpenHelperDb(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_SQL_QUOTES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(DELETE_SQL_QUOTES);
        onCreate(sqLiteDatabase);
    }

    private void add(int id ,String quote,String author){
        SQLiteDatabase db = FavQuotesOpenHelperDb.this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(FavQuotesContract.Infos.COLUMN_NAME_ID,id);
        values.put(FavQuotesContract.Infos.COLUMN_NAME_QUOTE,quote);
        values.put(FavQuotesContract.Infos.COLUMN_NAME_AUTHOR,author);

        db.insert(FavQuotesContract.Infos.TABLE_NAME,null,values);

    }
    public void add(Quote quote){
        add(quote.getId(), quote.getQuote(), quote.getAuthor());
    }

    public ArrayList<Quote> getAll(){
        ArrayList<Quote> quotes = new ArrayList<>();
        SQLiteDatabase db = FavQuotesOpenHelperDb.this.getReadableDatabase();
        String[] projection = {
          FavQuotesContract.Infos.COLUMN_NAME_ID,
          FavQuotesContract.Infos.COLUMN_NAME_QUOTE,
          FavQuotesContract.Infos.COLUMN_NAME_AUTHOR
        };

        Cursor cursor = db.query(
                FavQuotesContract.Infos.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                null
        );

        while(cursor.moveToNext()){
            int id = cursor.getInt(cursor.getColumnIndexOrThrow(FavQuotesContract.Infos.COLUMN_NAME_ID));
            String quote = cursor.getString(cursor.getColumnIndexOrThrow(FavQuotesContract.Infos.COLUMN_NAME_QUOTE));
            String author = cursor.getString(cursor.getColumnIndexOrThrow(FavQuotesContract.Infos.COLUMN_NAME_AUTHOR));

            quotes.add(new Quote(id,quote,author));

        }
        cursor.close();
        return quotes;
    }
    public void delete(int id){
        SQLiteDatabase db = FavQuotesOpenHelperDb.this.getWritableDatabase();

        String selection = FavQuotesContract.Infos.COLUMN_NAME_ID + "LIKE ?";

        String[] selectionArg = { Integer.toString(id)};

        db.delete(FavQuotesContract.Infos.TABLE_NAME, selection, selectionArg);
    }
}

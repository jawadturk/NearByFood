package com.example.jawad.nearbyfood.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;


import com.example.jawad.nearbyfood.application.ApplicationClass;

import java.util.ArrayList;
import java.util.List;


public class DatabaseOps {

    private static DatabaseOps currentInstance;

    private SQLiteHandler sqLiteHandler;

    private DatabaseOps() {
        sqLiteHandler = new SQLiteHandler(ApplicationClass.getInstance());
    }

    public synchronized static DatabaseOps getCurrentInstance() {
        if (currentInstance == null) {
            currentInstance = new DatabaseOps();
        }
        return currentInstance;
    }


    public boolean insertResturantId(String resturantId) {

        SQLiteDatabase writableDatabase = sqLiteHandler.getWritableDatabase();

        ContentValues contentValues = new ContentValues();


        contentValues.put(SQLiteHandler.TABLE_RESTURANT_FAVORITE.COL_RESTURANT_ID, resturantId);


        long insertedRowID = writableDatabase.insertWithOnConflict(SQLiteHandler.TABLE_RESTURANT_FAVORITE.TABLE_NAME,
                null,
                contentValues, SQLiteDatabase.CONFLICT_REPLACE);

        writableDatabase.close();

        return insertedRowID >= 0;
    }

    public boolean isFavorite(String resturantId) {


        SQLiteDatabase readableDatabase = sqLiteHandler.getReadableDatabase();


        Cursor cur = readableDatabase.rawQuery("SELECT Count(*) FROM "+SQLiteHandler.TABLE_RESTURANT_FAVORITE.TABLE_NAME+" where "+SQLiteHandler.TABLE_RESTURANT_FAVORITE.COL_RESTURANT_ID+ " = ?", new String[] { resturantId } );


        cur.moveToFirst();
        int count= cur.getInt(0);
        cur.close();
        readableDatabase.close();
        if (count>0)
        {
            return true;
        }else
        {
            return false;
        }


    }




    public boolean unFavorite(String resturantId) {
        String table = SQLiteHandler.TABLE_RESTURANT_FAVORITE.TABLE_NAME;
        String whereClause = SQLiteHandler.TABLE_RESTURANT_FAVORITE.COL_RESTURANT_ID + " =?";
        String[] whereArgs = new String[]{resturantId};
        int numberOfAffectedRows = sqLiteHandler
                .getWritableDatabase()
                .delete(table, whereClause, whereArgs);

        return numberOfAffectedRows > 0;
    }




}

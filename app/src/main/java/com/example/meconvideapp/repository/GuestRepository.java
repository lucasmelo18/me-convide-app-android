package com.example.meconvideapp.repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.example.meconvideapp.constants.DataBaseConstants;
import com.example.meconvideapp.entities.GuestEntity;

public class GuestRepository {

    private static GuestRepository INSTANCE;
    private GuestDataBaseHelper mGuestDataBaseHelper;

    private GuestRepository(Context c){
        this.mGuestDataBaseHelper = new GuestDataBaseHelper(c);
    }

    public static synchronized GuestRepository getInstance(Context c){
        if(INSTANCE == null){
            INSTANCE = new GuestRepository(c);
        }
        return INSTANCE;
    }

    public Boolean insert(GuestEntity guestEntity) {
        try{
            SQLiteDatabase sqLiteDatabase = this.mGuestDataBaseHelper.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put(DataBaseConstants.GUEST.COLUMNS.NAME, guestEntity.getName());
            contentValues.put(DataBaseConstants.GUEST.COLUMNS.PRESENCE, guestEntity.getConfirmed());
            sqLiteDatabase.insert(DataBaseConstants.GUEST.TABLE_NAME, null, contentValues);

            return true;
        } catch (Exception e){
            return false;
        }
    }
}

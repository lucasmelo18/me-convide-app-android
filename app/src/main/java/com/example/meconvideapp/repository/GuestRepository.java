package com.example.meconvideapp.repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.meconvideapp.constants.DataBaseConstants;
import com.example.meconvideapp.constants.GuestConstants;
import com.example.meconvideapp.entities.GuestCount;
import com.example.meconvideapp.entities.GuestEntity;

import java.util.ArrayList;
import java.util.List;

public class GuestRepository {

    private static GuestRepository INSTANCE;
    private GuestDataBaseHelper mGuestDataBaseHelper;

    private GuestRepository(Context c) {
        this.mGuestDataBaseHelper = new GuestDataBaseHelper(c);
    }

    public static synchronized GuestRepository getInstance(Context c) {
        if (INSTANCE == null) {
            INSTANCE = new GuestRepository(c);
        }
        return INSTANCE;
    }

    public Boolean insert(GuestEntity guestEntity) {
        try {
            SQLiteDatabase sqLiteDatabase = this.mGuestDataBaseHelper.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put(DataBaseConstants.GUEST.COLUMNS.NAME, guestEntity.getName());
            contentValues.put(DataBaseConstants.GUEST.COLUMNS.PRESENCE, guestEntity.getConfirmed());
            sqLiteDatabase.insert(DataBaseConstants.GUEST.TABLE_NAME, null, contentValues);

            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public Boolean update(GuestEntity guestEntity) {
        try {
            SQLiteDatabase sqLiteDatabase = this.mGuestDataBaseHelper.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put(DataBaseConstants.GUEST.COLUMNS.NAME, guestEntity.getName());
            contentValues.put(DataBaseConstants.GUEST.COLUMNS.PRESENCE, guestEntity.getConfirmed());

            String selection = DataBaseConstants.GUEST.COLUMNS.ID + " = ?";
            String[] selectionArgs = {String.valueOf(guestEntity.getId())};

            sqLiteDatabase.update(DataBaseConstants.GUEST.TABLE_NAME, contentValues, selection, selectionArgs);

            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public Boolean remove(int id) {
        try {
            SQLiteDatabase sqLiteDatabase = this.mGuestDataBaseHelper.getWritableDatabase();

            String selection = DataBaseConstants.GUEST.COLUMNS.ID + " = ?";
            String[] selectionArgs = {String.valueOf(id)};

            sqLiteDatabase.delete(DataBaseConstants.GUEST.TABLE_NAME, selection, selectionArgs);

            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public List<GuestEntity> getGuestsByQuery(String query) {
        List<GuestEntity> list = new ArrayList<>();

        try {
            SQLiteDatabase sqLiteDatabase = this.mGuestDataBaseHelper.getReadableDatabase();
            Cursor cursor = sqLiteDatabase.rawQuery(query, null);

            if (cursor != null && cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    GuestEntity guestEntity = new GuestEntity();
                    guestEntity.setId(cursor.getInt(cursor.getColumnIndex(DataBaseConstants.GUEST.COLUMNS.ID)));
                    guestEntity.setName(cursor.getString(cursor.getColumnIndex(DataBaseConstants.GUEST.COLUMNS.NAME)));
                    guestEntity.setConfirmed(cursor.getInt(cursor.getColumnIndex(DataBaseConstants.GUEST.COLUMNS.PRESENCE)));

                    list.add(guestEntity);
                }
            }

            if (cursor != null) {
                cursor.close();
            }

        } catch (Exception e) {
            return list;
        }

        return list;
    }

    public GuestEntity load(int id) {
        GuestEntity guestEntity = new GuestEntity();
        try {
            SQLiteDatabase sqLiteDatabase = this.mGuestDataBaseHelper.getReadableDatabase();

            String[] projection = {
                    DataBaseConstants.GUEST.COLUMNS.ID,
                    DataBaseConstants.GUEST.COLUMNS.NAME,
                    DataBaseConstants.GUEST.COLUMNS.PRESENCE,
            };

            String selection = DataBaseConstants.GUEST.COLUMNS.ID + " = ?";
            String[] selectionArg = {String.valueOf(id)};

            Cursor cursor = sqLiteDatabase.query(DataBaseConstants.GUEST.TABLE_NAME, projection, selection, selectionArg, null, null, null);
            if (cursor != null && cursor.getCount() > 0) {
                cursor.moveToFirst();
                guestEntity.setId(cursor.getInt(cursor.getColumnIndex(DataBaseConstants.GUEST.COLUMNS.ID)));
                guestEntity.setName(cursor.getString(cursor.getColumnIndex(DataBaseConstants.GUEST.COLUMNS.NAME)));
                guestEntity.setConfirmed(cursor.getInt(cursor.getColumnIndex(DataBaseConstants.GUEST.COLUMNS.PRESENCE)));

            }

            if (cursor != null) {
                cursor.close();
            }

            return guestEntity;

        } catch (Exception e) {
            return guestEntity;
        }
    }

    public GuestCount loadDashboard() {
        GuestCount guestCount = new GuestCount(0, 0, 0);
        Cursor cursor;
        try {
            SQLiteDatabase sqLiteDatabase = this.mGuestDataBaseHelper.getReadableDatabase();

            // PRESENTES
            String queryPresence = "select count(*) from " + DataBaseConstants.GUEST.TABLE_NAME + " where " +
                    DataBaseConstants.GUEST.COLUMNS.PRESENCE + " = " + GuestConstants.CONFIRMATION.PRESENT;

            cursor = sqLiteDatabase.rawQuery(queryPresence, null);

            if (cursor != null && cursor.getCount() > 0) {
                cursor.moveToFirst();
                guestCount.setPresentCount(cursor.getInt(0));
            }

            // AUSENTES
            String queryAbsent = "select count(*) from " + DataBaseConstants.GUEST.TABLE_NAME + " where " +
                    DataBaseConstants.GUEST.COLUMNS.PRESENCE + " = " + GuestConstants.CONFIRMATION.ABSENT;

            cursor = sqLiteDatabase.rawQuery(queryAbsent, null);

            if (cursor != null && cursor.getCount() > 0) {
                cursor.moveToFirst();
                guestCount.setAbsentCount(cursor.getInt(0));
            }

            // TODOS
            String queryAllInvited = "select count(*) from " + DataBaseConstants.GUEST.TABLE_NAME;

            cursor = sqLiteDatabase.rawQuery(queryAllInvited, null);

            if (cursor != null && cursor.getCount() > 0) {
                cursor.moveToFirst();
                guestCount.setAllInvitedCount(cursor.getInt(0));
            }

            return guestCount;

        } catch (Exception e) {
            return guestCount;
        }
    }
}

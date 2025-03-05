package com.MyTransportApp.cabslanka.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.MyTransportApp.cabslanka.Model.User;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static String databaseName = "CabsLankaDatabase";
    static int version = 1;
    private static String tableName = "users";
    private static String columnId = "id";
    private static String columnName = "name";
    private static String columnEmail = "email";
    private static String columnPhoneNo = "phoneNo";
    private static String columnAddress = "address";
    private static String columnPassword = "password";

    private String create_user_table = "create table " + tableName + "(" +
            columnId + " integer primary key autoincrement, " +
            columnName + " text, " +
            columnEmail + " text, " +
            columnPhoneNo + " text, " +
            columnAddress + " text," +
            columnPassword + " text " +
            ")";

    private String drop_user_table = "drop table if exists " + tableName;

    public DatabaseHelper(Context context) {
        super(context, databaseName, null, version);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(create_user_table);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(drop_user_table);
        onCreate(db);
    }
    //User registration
    public boolean addUser(User user){
        boolean result = true;
        try {
            SQLiteDatabase db = getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put(columnName,user.getName());
            contentValues.put(columnEmail,user.getEmail());
            contentValues.put(columnPhoneNo,user.getPhoneNo());
            contentValues.put(columnAddress,user.getAddress());
            contentValues.put(columnPassword,user.getPassword());
            result = db.insert(tableName,null,contentValues)>0;
        } catch (Exception e) {
            result = false;
        }
        return result;
    }
    //Forgot Password
    public boolean updatePassword(String email,String password){
        boolean result = true;
        try {
            SQLiteDatabase db = getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put(columnPassword,password);
            result = db.update(tableName,contentValues, "email = ?",new String[] {email})>0;
        } catch (Exception e) {
            result = false;
        }
        return result;
    }

    //Update Account
    public boolean updateAccount(String name,String email,String phoneNo,String address,String password){
        boolean result = true;
        try {
            SQLiteDatabase db = getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put(columnName,name);
            contentValues.put(columnPhoneNo,phoneNo);
            contentValues.put(columnAddress,address);
            contentValues.put(columnPassword,password);
            result = db.update(tableName,contentValues, "email = ?",new String[] {email})>0;
        } catch (Exception e) {
            result = false;
        }
        return result;
    }

    //Delete Account
    public boolean deleteAccount(String email,String password){
        boolean result = true;
        try {
            SQLiteDatabase db = getWritableDatabase();
            result = db.delete(tableName,"email = ? " + "and password = ?",new String[] {email,password})>0;
        } catch (Exception e) {
            result = false;
        }
        return result;
    }

    //Login
    public User checkLogin(String email, String password){
        User user = null;
        try {
            SQLiteDatabase db = getReadableDatabase();
            Cursor cursor = db.rawQuery("select * from "+tableName+" where email = ? " +
                    "and password = ?", new String[] {email,password});
            if(cursor.moveToFirst()){
                user = new User();
                user.setId(cursor.getInt(0));
                user.setName(cursor.getString(1));
                user.setEmail(cursor.getString(2));
                user.setPhoneNo(cursor.getString(3));
                user.setAddress(cursor.getString(4));
                user.setPassword(cursor.getString(5));
            }
        }catch (Exception e){
            user = null;
        }
        return user;
    }
    //Check email for reset the password
    public User checkEmail(String email){
        User user = null;
        try {
            SQLiteDatabase db = getReadableDatabase();
            Cursor cursor = db.rawQuery("select * from "+tableName+" where email = ?", new String[] {email});
            if(cursor.moveToFirst()){
                user = new User();
                user.setId(cursor.getInt(0));
                user.setName(cursor.getString(1));
                user.setEmail(cursor.getString(2));
                user.setPhoneNo(cursor.getString(3));
                user.setAddress(cursor.getString(4));
                user.setPassword(cursor.getString(5));
            }
        }catch (Exception e){
            user = null;
        }
        return user;
    }
}

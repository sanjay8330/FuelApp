package com.example.eadlab;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static String TABLE_NAME = "Employee";
    public static String EMP_ID = "e_id";
    public static String FIRSTNAME = "firstName";
    public static String LASTNAME = "lastName";
    public static String EMAIL = "email";

    public DatabaseHelper(@Nullable Context context) {
        super(context, "LabExercise", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String createTableStmt = "CREATE TABLE " + TABLE_NAME + "(" + EMP_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + FIRSTNAME + ", "+ LASTNAME + ", " + EMAIL + ")";
        sqLiteDatabase.execSQL(createTableStmt);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    //Add Data Method
    public boolean registerEmployee(EmployeeModel employeeModel){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(FIRSTNAME, employeeModel.getFirstName());
        cv.put(LASTNAME, employeeModel.getLastName());
        cv.put(EMAIL, employeeModel.getEmail());

        long result = db.insert(TABLE_NAME, null, cv);
        if(result == -1){
            return false;
        }else{
            return true;
        }
    }

    //Get Method
    public List<EmployeeModel> getAllEmployees(){
        List<EmployeeModel> returnEmployeeModelList = new ArrayList<>();

        String query = "SELECT * FROM " + TABLE_NAME;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if(cursor.moveToFirst()){
            do{
                int empID = cursor.getInt(0);
                String fName = cursor.getString(1);
                String lName = cursor.getString(2);
                String email = cursor.getString(3);

                EmployeeModel employeeModel = new EmployeeModel(empID, fName, lName, email);
                returnEmployeeModelList.add(employeeModel);
            }while (cursor.moveToLast());
        }
        return returnEmployeeModelList;
    }
}

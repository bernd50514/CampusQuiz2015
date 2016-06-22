package campusquizregandlogdesign.com.example.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.HashMap;

import trainingsspiel.database.DBHelper;

/**
 * Created by Administrator on 01.07.2015.
 */
public class SQLiteQuestionHandler extends SQLiteOpenHelper {

    private static final String TAG = SQLiteQuestionHandler.class.getSimpleName();

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;
    // Database Name
    private static final String DATABASE_NAME = "ps15_prod_Test";

    // Login table name
    private static final String TABLE_QUESTION = "question";

    // Login Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_FRAGEN_ID = "Fragen_ID";
    private static final String KEY_FRAGE = "Frage";
    private static final String KEY_ANTWORT_1 = "Antwort_1";
    private static final String KEY_ANTWORT_2 = "Antwort_2";
    private static final String KEY_ANTWORT_3 = "Antwort_3";
    private static final String KEY_ANTWORT_4 = "Antwort_4";
    private static final String KEY_RIGHTANSWER = "Antwort_Richtig";
    private static final String KEY_NAME_SUBKATEGORIEN = "Kurzname";
    private Context context;
    private SQLiteDatabase database;

    public SQLiteQuestionHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void openDB() {
        DBHelper dbHelper = new DBHelper(context);
        if (database == null || !database.isOpen()) {
            database = dbHelper.getWritableDatabase();
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_Question_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_QUESTION + "("
                + KEY_ID + " INTEGER PRIMARY KEY autoincrement," + KEY_FRAGEN_ID + " TEXT UNIQUE,"
                + KEY_FRAGE + " TEXT," + KEY_ANTWORT_1 + " TEXT,"
                + KEY_ANTWORT_2 + " TEXT," + KEY_ANTWORT_3 + " TEXT,"
                + KEY_ANTWORT_4 + " TEXT," + KEY_RIGHTANSWER + " TEXT,"
                + KEY_NAME_SUBKATEGORIEN + " TEXT" + ");";
        db.execSQL(CREATE_Question_TABLE);

        Log.d(TAG, "Database tables SQLite Question created");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_QUESTION);

        // Create tables again
        onCreate(db);

    }

    public void addQuestion(String fragen_id, String frage, String antwort_1, String antwort_2, String antwort_3, String antwort_4, String rightAnswer, String subName) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_FRAGEN_ID, fragen_id);
        values.put(KEY_FRAGE, frage);
        values.put(KEY_ANTWORT_1, antwort_1);
        values.put(KEY_ANTWORT_2, antwort_2);
        values.put(KEY_ANTWORT_3, antwort_3);
        values.put(KEY_ANTWORT_4, antwort_4);
        values.put(KEY_RIGHTANSWER, rightAnswer);
        values.put(KEY_NAME_SUBKATEGORIEN, subName);
        // Inserting Row
        long id = db.insert(TABLE_QUESTION, null, values);
        db.close(); // Closing database connection

        Log.d(TAG, "New question inserted into sqlite Question: " + id);
    }


    public HashMap<String, String> getQuestionDetails() {
        HashMap<String, String> questions = new HashMap<String, String>();
        String selectQuery = "SELECT  * FROM " + TABLE_QUESTION;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // Move to first row
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            questions.put("Fragen_ID", cursor.getString(1));
            questions.put("Frage", cursor.getString(2));
            questions.put("Antwort_1", cursor.getString(3));
            questions.put("Antwort_2", cursor.getString(4));
            questions.put("Antwort_3", cursor.getString(5));
            questions.put("Antwort_4", cursor.getString(6));
            questions.put("Antwort_Richtig", cursor.getString(7));
            questions.put("Kurzname", cursor.getString(8));

        }
        cursor.close();
        db.close();
        // return user
        Log.d(TAG, "Fetching questions from Sqlite: " + questions.toString());

        return questions;
    }

    /**
     * Getting user login status return true if rows are there in table
     */
    public int getRowCount() {
        String countQuery = "SELECT  * FROM " + TABLE_QUESTION;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int rowCount = cursor.getCount();
        db.close();
        cursor.close();

        // return row count
        return rowCount;
    }

    /**
     * Re crate database Delete all tables and create them again
     */
    public void deleteQuestions() {
        SQLiteDatabase db = this.getWritableDatabase();
        // Delete All Rows
        db.delete(TABLE_QUESTION, null, null);
        db.close();

        // db.execSQL("delete from "+ TABLE_QUESTION);
        Log.d(TAG, "Deleted all questions info from sqlite");
    }

    public String[] QuestionStringArray() {
        String selectQuery = "SELECT  * FROM " + TABLE_QUESTION;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        String[] result = new String[cursor.getCount()];
        cursor.moveToFirst();
        for (int i = 0; i < cursor.getCount(); i++) {
            String row = cursor.getString(cursor.getColumnIndex("Frage"));
            result[i] = row;
            cursor.moveToNext();
        }
        return result;
    }

    public String[] Answer1StringArray() {
        String selectQuery = "SELECT  * FROM " + TABLE_QUESTION;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        String[] result = new String[cursor.getCount()];
        cursor.moveToFirst();
        for (int i = 0; i < cursor.getCount(); i++) {
            String row = cursor.getString(cursor.getColumnIndex("Antwort_1"));
            result[i] = row;
            cursor.moveToNext();
        }
        return result;
    }

    public String[] Answer2StringArray() {
        String selectQuery = "SELECT  * FROM " + TABLE_QUESTION;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        String[] result = new String[cursor.getCount()];
        cursor.moveToFirst();
        for (int i = 0; i < cursor.getCount(); i++) {
            String row = cursor.getString(cursor.getColumnIndex("Antwort_2"));
            result[i] = row;
            cursor.moveToNext();
        }
        return result;
    }


    public String[] Answer3StringArray() {
        String selectQuery = "SELECT  * FROM " + TABLE_QUESTION;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        String[] result = new String[cursor.getCount()];
        cursor.moveToFirst();
        for (int i = 0; i < cursor.getCount(); i++) {
            String row = cursor.getString(cursor.getColumnIndex("Antwort_3"));
            result[i] = row;
            cursor.moveToNext();
        }
        return result;
    }


    public String[] Answer4StringArray() {
        String selectQuery = "SELECT  * FROM " + TABLE_QUESTION;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        String[] result = new String[cursor.getCount()];
        cursor.moveToFirst();
        for (int i = 0; i < cursor.getCount(); i++) {
            String row = cursor.getString(cursor.getColumnIndex("Antwort_4"));
            result[i] = row;
            cursor.moveToNext();
        }
        return result;
    }


    public String[] CorrectAnswerStringArray() {
        String selectQuery = "SELECT  * FROM " + TABLE_QUESTION;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        String[] result = new String[cursor.getCount()];
        cursor.moveToFirst();
        for (int i = 0; i < cursor.getCount(); i++) {
            String row = cursor.getString(cursor.getColumnIndex("Antwort_Richtig"));
            result[i] = row;
            cursor.moveToNext();
        }
        return result;
    }

    public String[] QuestionCategoryName() {
        String selectQuery = "SELECT  * FROM " + TABLE_QUESTION;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        String[] result = new String[cursor.getCount()];
        cursor.moveToFirst();
        for (int i = 0; i < cursor.getCount(); i++) {
            String row = cursor.getString(cursor.getColumnIndex("Kurzname"));
            result[i] = row;
            cursor.moveToNext();
        }
        return result;
    }

}

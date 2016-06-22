package trainingsspiel.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import trainingsspiel.bean.ErrorQuestionInfo;


public class DBManager {

    private Context context;
    private SQLiteDatabase database;

    public DBManager(Context context) {
        this.context = context;
    }


    public void openDB() {
        DBHelper dbHelper = new DBHelper(context);
        if (database == null || !database.isOpen()) {
            database = dbHelper.getWritableDatabase();
        }
    }

    public void closeDB() {
        if (database != null && database.isOpen()) {
            database.close();
        }
    }

    public long deleteLibraryAllData() {
        return database.delete(DBHelper.TABLE_NAME_TEST_LIBRARY, null,
                null);
    }

    public long insertErrorQuestion(ErrorQuestionInfo info) {
        ContentValues newValues = new ContentValues();

        newValues.put(DBHelper.MY_ERROR_QUESTION_NAME, info.getQuestionName());
        newValues.put(DBHelper.MY_ERROR_QUESTION_TYPE, info.getQuestionType());
        newValues.put(DBHelper.MY_ERROR_QUESTION_ANSWER, info.getQuestionAnswer());
        newValues.put(DBHelper.MY_ERROR_QUESTION_SELECTED, info.getQuestionSelect());
        newValues.put(DBHelper.MY_ERROR_QUESTION_ISRIGHT, info.getIsRight());
        newValues.put(DBHelper.MY_ERROR_QUESTION_ANALYSIS, info.getAnalysis());
        newValues.put(DBHelper.MY_ERROR_QUESTION_OPTION_A, info.getOptionA());
        newValues.put(DBHelper.MY_ERROR_QUESTION_OPTION_B, info.getOptionB());
        newValues.put(DBHelper.MY_ERROR_QUESTION_OPTION_C, info.getOptionC());
        newValues.put(DBHelper.MY_ERROR_QUESTION_OPTION_D, info.getOptionD());
        newValues.put(DBHelper.MY_ERROR_QUESTION_OPTION_E, info.getOptionE());
        newValues.put(DBHelper.MY_ERROR_QUESTION_OPTION_TYPE, info.getOptionType());

        return database.insert(DBHelper.TABLE_NAME_MY_ERROR_QUESTION, null,
                newValues);
    }

    public long deleteAllData() {
        return database.delete(DBHelper.TABLE_NAME_MY_ERROR_QUESTION, null,
                null);
    }

    public ErrorQuestionInfo[] queryAllData() {
        Cursor result = database.query(DBHelper.TABLE_NAME_MY_ERROR_QUESTION,
                null, null, null, null,
                null, null);
        return ConvertToQuestion(result);
    }

    private ErrorQuestionInfo[] ConvertToQuestion(Cursor cursor) {
        int resultCounts = cursor.getCount();
        if (resultCounts == 0 || !cursor.moveToFirst()) {
            return null;
        }
        ErrorQuestionInfo[] peoples = new ErrorQuestionInfo[resultCounts];
        for (int i = 0; i < resultCounts; i++) {
            peoples[i] = new ErrorQuestionInfo();
            peoples[i].questionId = cursor.getInt(0);
            peoples[i].questionName = cursor.getString(cursor
                    .getColumnIndex(DBHelper.MY_ERROR_QUESTION_NAME));
            peoples[i].questionType = cursor.getString(cursor
                    .getColumnIndex(DBHelper.MY_ERROR_QUESTION_TYPE));
            peoples[i].questionAnswer = cursor.getString(cursor
                    .getColumnIndex(DBHelper.MY_ERROR_QUESTION_ANSWER));
            peoples[i].questionSelect = cursor.getString(cursor
                    .getColumnIndex(DBHelper.MY_ERROR_QUESTION_SELECTED));
            peoples[i].isRight = cursor.getString(cursor
                    .getColumnIndex(DBHelper.MY_ERROR_QUESTION_ISRIGHT));
            peoples[i].Analysis = cursor.getString(cursor
                    .getColumnIndex(DBHelper.MY_ERROR_QUESTION_ANALYSIS));
            peoples[i].optionA = cursor.getString(cursor
                    .getColumnIndex(DBHelper.MY_ERROR_QUESTION_OPTION_A));
            peoples[i].optionB = cursor.getString(cursor
                    .getColumnIndex(DBHelper.MY_ERROR_QUESTION_OPTION_B));
            peoples[i].optionC = cursor.getString(cursor
                    .getColumnIndex(DBHelper.MY_ERROR_QUESTION_OPTION_C));
            peoples[i].optionD = cursor.getString(cursor
                    .getColumnIndex(DBHelper.MY_ERROR_QUESTION_OPTION_D));
            peoples[i].optionE = cursor.getString(cursor
                    .getColumnIndex(DBHelper.MY_ERROR_QUESTION_OPTION_E));
            peoples[i].optionType = cursor.getString(cursor
                    .getColumnIndex(DBHelper.MY_ERROR_QUESTION_OPTION_TYPE));
            cursor.moveToNext();
        }
        return peoples;
    }

}

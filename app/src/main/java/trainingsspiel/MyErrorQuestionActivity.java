package trainingsspiel;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.demo.materialdesignnavdrawer.R;
import com.demo.materialdesignnavdrawer.activities.MainActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import trainingsspiel.adapter.MyErrorQuestionListAdapter;
import trainingsspiel.bean.ErrorQuestion;
import trainingsspiel.bean.ErrorQuestionInfo;
import trainingsspiel.database.DBManager;


public class MyErrorQuestionActivity extends Activity {

    ErrorQuestion question;
    private ImageView left;
    private TextView title;
    private List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
    private ListView listView;
    private List<ErrorQuestion> list = new ArrayList<ErrorQuestion>();
    private MyErrorQuestionListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.my_error_question);

        initView();
    }


    private void initView() {
        left = (ImageView) findViewById(R.id.left);
        title = (TextView) findViewById(R.id.title);
        title.setText("Mein Spielübersicht");
        listView = (ListView) findViewById(R.id.listview);

        left.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                finish();
                Intent intent = new Intent(MyErrorQuestionActivity.this, MainActivity.class); // go back to main menu
                startActivity(intent);
            }
        });

        adapter = new MyErrorQuestionListAdapter(this, data, listView);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position,
                                    long arg3) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(MyErrorQuestionActivity.this, MyErrorQuestionDetailActivity.class);
                question = list.get(position);
                intent.putExtra("questionName", question.getQuestionName());
                intent.putExtra("questionType", question.getQuestionType());
                intent.putExtra("questionAnswer", question.getQuestionAnswer());
                intent.putExtra("questionSelect", question.getQuestionSelect());
                intent.putExtra("isRight", question.getIsRight());
                intent.putExtra("Analysis", question.getAnalysis());
                intent.putExtra("optionA", question.getOptionA());
                intent.putExtra("optionB", question.getOptionB());
                intent.putExtra("optionC", question.getOptionC());
                intent.putExtra("optionD", question.getOptionD());
                intent.putExtra("optionType", question.getOptionType());
                startActivity(intent);
            }
        });

        DBManager dbManager = new DBManager(MyErrorQuestionActivity.this);
        dbManager.openDB();

        ErrorQuestionInfo[] errorQuestionInfos = dbManager.queryAllData();
        if (errorQuestionInfos == null) {
            Toast.makeText(MyErrorQuestionActivity.this, "keine Daten",
                    Toast.LENGTH_SHORT).show();
        } else {
            Map<String, Object> map = null;
            for (int i = 0; i < errorQuestionInfos.length; i++) {
                ErrorQuestion errorQuestion = new ErrorQuestion();
                map = new HashMap<String, Object>();
                map.put("title", errorQuestionInfos[i].questionName);
                map.put("type", errorQuestionInfos[i].questionType);
                map.put("trainingsspiel", errorQuestionInfos[i].questionAnswer);
                map.put("isright", errorQuestionInfos[i].isRight);//
                map.put("selected", errorQuestionInfos[i].questionSelect);//
                map.put("analysis", errorQuestionInfos[i].Analysis);//
                data.add(map);

                errorQuestion.setQuestionName(errorQuestionInfos[i].questionName);
                errorQuestion.setQuestionType(errorQuestionInfos[i].questionType);
                errorQuestion.setQuestionAnswer(errorQuestionInfos[i].questionAnswer);
                errorQuestion.setQuestionSelect(errorQuestionInfos[i].questionSelect);
                errorQuestion.setIsRight(errorQuestionInfos[i].isRight);
                errorQuestion.setAnalysis(errorQuestionInfos[i].Analysis);
                errorQuestion.setOptionA(errorQuestionInfos[i].optionA);
                errorQuestion.setOptionB(errorQuestionInfos[i].optionB);
                errorQuestion.setOptionC(errorQuestionInfos[i].optionC);
                errorQuestion.setOptionD(errorQuestionInfos[i].optionD);
                errorQuestion.setOptionType(errorQuestionInfos[i].optionType);
                list.add(errorQuestion);
            }
        }

    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            finish();
            Toast.makeText(getApplicationContext(), "Trainingsspiel abgeschlossen", Toast.LENGTH_LONG).show();
            Intent backhomemenuIntent = new Intent(MyErrorQuestionActivity.this, MainActivity.class);
            backhomemenuIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(backhomemenuIntent);
        }
        return super.onKeyDown(keyCode, event);
    }


}

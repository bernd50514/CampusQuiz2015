package trainingsspiel;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.demo.materialdesignnavdrawer.R;
import com.demo.materialdesignnavdrawer.activities.MainActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import campusquizregandlogdesign.com.example.helper.JsonParsingFunctions;
import campusquizregandlogdesign.com.example.helper.SQLiteQuestionHandler;
import trainingsspiel.adapter.ExaminationSubmitAdapterChallenge;
import trainingsspiel.bean.AnSwerInfo;
import trainingsspiel.bean.ErrorQuestionInfo;
import trainingsspiel.bean.SaveQuestionInfo;
import trainingsspiel.database.DBManager;
import trainingsspiel.util.ConstantUtil;
import trainingsspiel.util.ViewPagerScroller;
import trainingsspiel.view.VoteSubmitViewPager;


public class ChallengeQuizActivity extends Activity {

    //  JSON url
    private static final String CHALLENGEQUESTION_URL = "http://ps15server.cloudapp.net:8080/useraccount/GamePlayService/getStageQuestions";
    // ALL JSON node names for Questions
    private static final String TAG_MESSAGES = "Questions";
    private static final String TAG_FRAGEN_ID = "Fragen_ID";

    //FK
//private Button ToggleButton;
    private static final String TAG_FRAGE = "Frage";
    private static final String TAG_ANTWORT_1 = "Antwort_1";
    //NewExaminationSubmitAdapter pagerAdapter;
    private static final String TAG_ANTWORT_2 = "Antwort_2";
    private static final String TAG_ANTWORT_3 = "Antwort_3";
    private static final String TAG_ANTWORT_4 = "Antwort_4";
    private static final String TAG_RIGHTANSWER = "Antwort_Richtig";
    //  JSON url
    private static final String SUBMIT_URL = "http://ps15server.cloudapp.net:8080/useraccount/GamePlayService/submitStageResults";
    public List<Map<String, SaveQuestionInfo>> list = new ArrayList<Map<String, SaveQuestionInfo>>();
    public Map<String, SaveQuestionInfo> map2 = new HashMap<String, SaveQuestionInfo>();
    public List<SaveQuestionInfo> questionInfos = new ArrayList<SaveQuestionInfo>();
    VoteSubmitViewPager viewPager;
    //ExaminationSubmitAdapter pagerAdapter;
    ExaminationSubmitAdapterChallenge pagerAdapter;
    List<View> viewItems = new ArrayList<View>();
    List<AnSwerInfo> dataItems = new ArrayList<AnSwerInfo>();
    Dialog builderSubmit;
    SQLiteQuestionHandler sqLiteDB;
    Timer timer;
    TimerTask timerTask;
    int minute = 2;
    int second = 0;
    boolean isPause = false;
    int isFirst;
    DBManager dbManager;
    String dateStr = "";
    String imgServerUrl = "";
    LoadInbox In = new LoadInbox();
    LoadInbox2 In2 = new LoadInbox2();
    // products JSONArray
    JSONArray inbox = null;
    int stage = 0, gameid = 0;
    String subkategoriename = null, myusername = null;
    int q1 = 0, q2 = 0, q3 = 0;
    private ImageView leftIv;
    private TextView titleTv;
    private TextView right;
    private ProgressDialog progressDialog;
    private String pageCode;
    private int pageScore;
    private int errortopicNums;
    private int errortopicNums1;
    private String isPerfectData = "1";
    private String type = "0";
    private String errorMsg = "";
    private boolean isUpload = false;
    private Handler handlerSubmit = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);

            switch (msg.what) {
                case 1:
                    //showSubmitDialog();

                    new Handler().postDelayed(new Runnable() {

                        @Override
                        public void run() {
                            //builderSubmit.dismiss();
                            finish();

                        }
                    }, 10000);

                    break;

                default:
                    break;
            }

        }
    };
    private Handler handlerStopTime = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    stopTime();
                    break;
                case 1:
                    startTime();
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    };
    Handler handlerTime = new Handler() {
        public void handleMessage(Message msg) {

            if (minute < 2) {
                right.setTextColor(Color.RED);
                //FK
                //ToggleButton.setTextColor(Color.RED);
            } else {
                right.setTextColor(Color.WHITE);
                //FK
                //ToggleButton.setTextColor(Color.WHITE);
            }
            if (minute == 0) {
                if (second == 0) {
                    isFirst += 1;
                    // times up
                    if (isFirst == 1) {
                        showTimeOutDialog(true, "0");
                    }
                    right.setText("00:00");
                    //FK
                    //ToggleButton.setText("Noch 00 Sekunden!");
                    if (timer != null) {
                        timer.cancel();
                        timer = null;
                    }
                    if (timerTask != null) {
                        timerTask = null;
                    }
                } else {
                    second--;
                    if (second >= 10) {
                        right.setText("0" + minute + ":" + second);
                        //FK
                        //ToggleButton.setText("Noch "+second+" Sekunden!");
                    } else {
                        right.setText("0" + minute + ":0" + second);
                        //FK
                        //ToggleButton.setText("Noch "+second+" Sekunden!");
                    }
                }
            } else {
                if (second == 0) {
                    second = 59;
                    minute--;
                    if (minute >= 10) {
                        right.setText(minute + ":" + second);
                        //FK
                        //ToggleButton.setText("Noch "+second+" Sekunden!");
                    } else {
                        right.setText("0" + minute + ":" + second);
                        //FK
                        // ToggleButton.setText("Noch "+second+" Sekunden!");
                    }
                } else {
                    second--;
                    if (second >= 10) {
                        if (minute >= 10) {
                            right.setText(minute + ":" + second);
                            //FK
                            // ToggleButton.setText("Noch "+second+" Sekunden!");
                        } else {
                            right.setText("0" + minute + ":" + second);
                            //FK
                            // ToggleButton.setText("Noch "+second+" Sekunden!");
                        }
                    } else {
                        if (minute >= 10) {
                            right.setText(minute + ":0" + second);
                            //FK
                            // ToggleButton.setText("Noch "+second+" Sekunden!");
                        } else {
                            right.setText("0" + minute + ":0" + second);
                            //FK
                            // ToggleButton.setText("Noch "+second+" Sekunden!");
                        }
                    }
                }
            }
        }

    };
    // Progress Dialog
    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_practice_test);

        sqLiteDB = new SQLiteQuestionHandler(getApplicationContext());

        dbManager = new DBManager(ChallengeQuizActivity.this);
        dbManager.openDB();
        initView();
        //TODO: initiate a webservice in LoadData?

        Bundle bundle = getIntent().getExtras();
        gameid = bundle.getInt("game");
        stage = bundle.getInt("stage");
        myusername = bundle.getString("username");
        subkategoriename = bundle.getString("subkategorie");
        Log.i("FLOW", gameid + " " + stage + " " + myusername + " " + subkategoriename);

        In.execute();
    }

    public void fillData() {
        loadData();
        ErrorQuestionInfo[] errorQuestionInfos = dbManager.queryAllData();
        if (errorQuestionInfos != null) {

            int colunm = (int) dbManager.deleteAllData();
        }
        if (sqLiteDB.getRowCount() > 0) {
            sqLiteDB.deleteQuestions();
        }
    }

    public void initView() {
        leftIv = (ImageView) findViewById(R.id.left);
        titleTv = (TextView) findViewById(R.id.title);
        right = (TextView) findViewById(R.id.right);

        //FK
        //ToggleButton = (Button) findViewById(R.id.ToggleButton1);

        titleTv.setText("Challenge");
        Drawable drawable1 = getBaseContext().getResources().getDrawable(
                R.drawable.ic_action_name3);
        drawable1.setBounds(0, 0, drawable1.getMinimumWidth(),
                drawable1.getMinimumHeight());
        right.setCompoundDrawables(drawable1, null, null, null);
//	right.setText("15:00");

        //ToggleButton.setText("BLABLA");
        viewPager = (VoteSubmitViewPager) findViewById(R.id.vote_submit_viewpager);
        leftIv.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {

                isPause = true;
                showTimeOutDialog(true, "1");
                Message msg = new Message();
                msg.what = 0;
                handlerStopTime.sendMessage(msg);//here the timer will stop when user wants to exit...
            }
        });

        initViewPagerScroll();

    }

    private void loadData() {
        //sqLiteDB = new SQLiteQuestionHandler(getApplicationContext());

        String getFrage[] = new String[sqLiteDB.getRowCount()];

        int numberOfFrage = getFrage.length;

        //initialize the parameters in SQLite DB
        String getQuestion[] = sqLiteDB.QuestionStringArray();
        String getAnswer_1[] = sqLiteDB.Answer1StringArray();

        String getAnswer_2[] = sqLiteDB.Answer2StringArray();

        String getAnswer_3[] = sqLiteDB.Answer3StringArray();

        String getAnswer_4[] = sqLiteDB.Answer4StringArray();

        String getRightAnswer[] = sqLiteDB.CorrectAnswerStringArray();

        String getCategorieName[] = sqLiteDB.QuestionCategoryName();


        for (int i = 0; i < getQuestion.length; i++) {
            //TODO: Check how to display random without repeat
            AnSwerInfo info = new AnSwerInfo();
            info.setQuestionId(i);
            info.setSubCategorie(getCategorieName[i]);
            info.setQuestionName(getQuestion[i]);
            info.setQuestionType("0");
            info.setQuestionFor("0");
            info.setCorrectAnswer(getRightAnswer[i]);
            info.setOptionA(getAnswer_1[i]);
            info.setOptionB(getAnswer_2[i]);
            info.setOptionC(getAnswer_3[i]);
            info.setOptionD(getAnswer_4[i]);

            info.setScore(1);// 分值
            info.setOption_type("0");
            dataItems.add(info);
        }

        for (int i = 0; i < dataItems.size(); i++) {
            viewItems.add(getLayoutInflater().inflate(
                    R.layout.vote_submit_viewpager_item, null));
        }
        pagerAdapter = new ExaminationSubmitAdapterChallenge(
                ChallengeQuizActivity.this, viewItems,
                dataItems, imgServerUrl);


        viewPager.setAdapter(pagerAdapter);
        viewPager.getParent()
                .requestDisallowInterceptTouchEvent(false);

    }

    /**
     * 设置ViewPager的滑动速度
     */
    private void initViewPagerScroll() {
        try {
            Field mScroller = null;
            mScroller = ViewPager.class.getDeclaredField("mScroller");
            mScroller.setAccessible(true);
            ViewPagerScroller scroller = new ViewPagerScroller(viewPager.getContext());
            mScroller.set(viewPager, scroller);


        } catch (NoSuchFieldException e) {

        } catch (IllegalArgumentException e) {

        } catch (IllegalAccessException e) {

        }
    }

    /**
     * @param index 根据索引值切换页面
     */
    public void setCurrentView(int index) {
        viewPager.setCurrentItem(index);
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        stopTime();
        minute = -1;
        second = -1;
        super.onDestroy();
    }

    public int[] uploadExam(int errortopicNum) {
        String myresultList = "";
        int[] a = new int[3];

        errortopicNums = errortopicNum;
        if (questionInfos.size() > 0) {
            //
            if (questionInfos.size() == dataItems.size()) {
                for (int i = 0; i < questionInfos.size(); i++) {
                    Log.i("WURST", i + " --> " + questionInfos.get(i).getIs_correct());
                    a[i] = Integer.valueOf(questionInfos.get(i).getIs_correct());
                    if (i == questionInfos.size() - 1) {

                        if (i == 0) {
                            if (questionInfos.get(i).getIs_correct().equals("0")) {
                                q1 = 0;
                            } else if (questionInfos.get(i).getIs_correct().equals("1")) {
                                q1 = 1;
                            }
                        } else if (i == 1) {
                            if (questionInfos.get(i).getIs_correct().equals("0")) {
                                q2 = 0;
                            } else if (questionInfos.get(i).getIs_correct().equals("1")) {
                                q2 = 1;
                            }
                        } else if (i == 2) {
                            if (questionInfos.get(i).getIs_correct().equals("0")) {
                                q3 = 0;
                            } else if (questionInfos.get(i).getIs_correct().equals("1")) {
                                q3 = 1;
                            }
                        }

                        myresultList += questionInfos.get(i).getIs_correct().toString() + "";
                    } else {
                        myresultList += questionInfos.get(i).getIs_correct().toString() + ",";
                    }
                    if (questionInfos.size() == 0) {
                        myresultList += "";
                    }

                }
            } else {    //2. Teil der Fragen wurden beantwortet
                for (int i = 0; i < dataItems.size(); i++) {
                    if (dataItems.get(i).getIsSelect() == null) {
                        errortopicNums1 += 1;
                        SaveQuestionInfo questionInfo = new SaveQuestionInfo();
                        questionInfo.setIs_correct(ConstantUtil.isError);
                        questionInfos.add(questionInfo);
                    }
                }

                for (int i = 0; i < dataItems.size(); i++) {

                    a[i] = Integer.valueOf(questionInfos.get(i).getIs_correct());
                    if (i == dataItems.size() - 1) {
                        // BEGIN TEST PARTIAL SCORES
                        // auch wenn nur Teil der Fragen beantwortet beim Timeup wurde, wirds beim Spiel geranked. 12.08.2015 by Liang
                        if (i == 0) {
                            if (questionInfos.get(i).getIs_correct().equals("0")) {
                                q1 = 0;
                            } else if (questionInfos.get(i).getIs_correct().equals("1")) {
                                q1 = 1;
                            }
                        } else if (i == 1) {
                            if (questionInfos.get(i).getIs_correct().equals("0")) {
                                q2 = 0;
                            } else if (questionInfos.get(i).getIs_correct().equals("1")) {
                                q2 = 1;
                            }
                        } else if (i == 2) {
                            if (questionInfos.get(i).getIs_correct().equals("0")) {
                                q3 = 0;
                            } else if (questionInfos.get(i).getIs_correct().equals("1")) {
                                q3 = 1;
                            }
                        }

                        // END TEST PARTIAL SCORES


                        myresultList += questionInfos.get(i).getIs_correct().toString() + "";
                    } else {
                        myresultList += questionInfos.get(i).getIs_correct().toString() + ",";
                    }
                    if (dataItems.size() == 0) {
                        myresultList += "";
                    }

                }
            }
        } else {
            // keine Antworten wurden gewählt.
            for (int i = 0; i < dataItems.size(); i++) {
                if (dataItems.get(i).getIsSelect() == null) {
                    errortopicNums1 += 1;

                    SaveQuestionInfo questionInfo = new SaveQuestionInfo();

                    questionInfo.setIs_correct(ConstantUtil.isError);
                    questionInfos.add(questionInfo);
                }
            }

            for (int i = 0; i < dataItems.size(); i++) {
                if (i == dataItems.size() - 1) {
                    myresultList += questionInfos.get(i).getIs_correct().toString() + "";
                } else {
                    myresultList += questionInfos.get(i).getIs_correct().toString() + ",";
                }
                if (dataItems.size() == 0) {
                    myresultList += "";
                }

            }
        }
        System.out.println("Daten schon im Background abgegeben====" + myresultList);


        return a;
    }

    // TODO: Wie reagiert Challenge wenn: 1. Nutzer will aus im Zuge des Spiels 2.Nutzer kann nicht alle Fragen beantworten beim Timeup 3. Nutzer hat keine Fragen beantwortet beim Timeup..
    public void uploadExamination(int errortopicNum) {
        // TODO Auto-generated method stub
        String resultlist = "[";
        errortopicNums = errortopicNum;

        if (questionInfos.size() > 0) { // Fragen wurdern mindesten einmal beantwortet.
            // 1. Alle Fragen wurden ausgewählt und beantwortet
            if (questionInfos.size() == dataItems.size()) {
                for (int i = 0; i < questionInfos.size(); i++) {
                    if (i == questionInfos.size() - 1) {
                        resultlist += questionInfos.get(i).toString() + "]";
                    } else {
                        resultlist += questionInfos.get(i).toString() + ",";
                    }
                    if (questionInfos.size() == 0) {
                        resultlist += "]";
                    }
                    if (questionInfos.get(i).getIs_correct()
                            .equals(ConstantUtil.isCorrect)) {
                        int score = questionInfos.get(i).getScore();
                        pageScore += score;
                    }
                }
            } else {
                //2. Teil der Fragen wurden beantwortet
                for (int i = 0; i < dataItems.size(); i++) {
                    if (dataItems.get(i).getIsSelect() == null) {
                        errortopicNums1 += 1;
                        SaveQuestionInfo questionInfo = new SaveQuestionInfo();
                        questionInfo.setQuestionId(dataItems.get(i).getQuestionId());
                        questionInfo.setQuestionType(dataItems.get(i).getQuestionType());
                        questionInfo.setRealAnswer(dataItems.get(i).getCorrectAnswer());
                        questionInfo.setScore(dataItems.get(i).getScore());
                        questionInfo.setIs_correct(ConstantUtil.isError);
                        questionInfos.add(questionInfo);
                    }
                }

                for (int i = 0; i < dataItems.size(); i++) {
                    if (i == dataItems.size() - 1) {
                        resultlist += questionInfos.get(i).toString() + "]";
                    } else {
                        resultlist += questionInfos.get(i).toString() + ",";
                    }
                    if (dataItems.size() == 0) {
                        resultlist += "]";
                    }
                    if (questionInfos.get(i).getIs_correct()
                            .equals(ConstantUtil.isCorrect)) {
                        int score = questionInfos.get(i).getScore();
                        pageScore += score;
                    }
                }
            }
        } else { // überhaupt nicht geantwortet...bis Countdown abläuft..

            for (int i = 0; i < dataItems.size(); i++) {
                if (dataItems.get(i).getIsSelect() == null) {
                    errortopicNums1 += 1;

                    SaveQuestionInfo questionInfo = new SaveQuestionInfo();
                    questionInfo.setQuestionId(dataItems.get(i).getQuestionId());
                    questionInfo.setQuestionType(dataItems.get(i).getQuestionType());
                    questionInfo.setRealAnswer(dataItems.get(i).getCorrectAnswer());
                    questionInfo.setScore(dataItems.get(i).getScore());
                    questionInfo.setIs_correct(ConstantUtil.isError);
                    questionInfos.add(questionInfo);
                }
            }

            for (int i = 0; i < dataItems.size(); i++) {
                if (i == dataItems.size() - 1) {
                    resultlist += questionInfos.get(i).toString() + "]";
                } else {
                    resultlist += questionInfos.get(i).toString() + ",";
                }
                if (dataItems.size() == 0) {
                    resultlist += "]";
                }
                if (questionInfos.get(i).getIs_correct()
                        .equals(ConstantUtil.isCorrect)) {
                    int score = questionInfos.get(i).getScore();
                    pageScore += score;
                }
            }
        }

        System.out.println("Daten schon im Background abgegeben====" + resultlist);

        Message msg = handlerSubmit.obtainMessage();
        msg.what = 1;
        handlerSubmit.sendMessage(msg);

        In2.execute();


    }

    protected void showTimeOutDialog(final boolean flag, final String backtype) {
        final Dialog builder = new Dialog(this, R.style.dialog);
        builder.setContentView(R.layout.my_dialog);
        TextView title = (TextView) builder.findViewById(R.id.dialog_title);
        TextView content = (TextView) builder.findViewById(R.id.dialog_content);
        if (backtype.equals("0")) {
            content.setText("Die Zeit ist abgelaufen!");
        } else if (backtype.equals("1")) {
            content.setText("Willst du das Spiel beenden？");
        } else {
            content.setText(errorMsg + "");
        }
        final Button confirm_btn = (Button) builder
                .findViewById(R.id.dialog_sure);
        Button cancel_btn = (Button) builder.findViewById(R.id.dialog_cancle);
        if (backtype.equals("0")) {
            confirm_btn.setText("Abgeben");
            cancel_btn.setText("aus");
        } else if (backtype.equals("1")) {
            confirm_btn.setText("ja");
            cancel_btn.setText("Weitermachen");
        } else {
            confirm_btn.setText("Ja");
            cancel_btn.setVisibility(View.GONE);
        }
        confirm_btn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (backtype.equals("0")) { // Times up -->aufhören
                    builder.dismiss();
                    finish();
                    uploadExamination(pagerAdapter.errorTopicNum());


                } else {  // exits during the game ?--yes
                    builder.dismiss();
                    finish();
                    Intent backhomemenuIntent = new Intent(ChallengeQuizActivity.this, MainActivity.class); // Go back to main menu....
                    backhomemenuIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(backhomemenuIntent);

                }

            }
        });

        cancel_btn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (backtype.equals("0")) { //times up -->aus
                    finish();
                    builder.dismiss();
                    Intent backhomemenuIntent = new Intent(ChallengeQuizActivity.this, MainActivity.class);
                    backhomemenuIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(backhomemenuIntent);
                } else { // exits during the game? -->no
                    isPause = false;
                    builder.dismiss();
                    Message msg = new Message();
                    msg.what = 1;
                    handlerStopTime.sendMessage(msg);
                }


            }
        });
        builder.setCanceledOnTouchOutside(false);//
        builder.setOnKeyListener(new OnKeyListener() {

            @Override
            public boolean onKey(DialogInterface dialog, int keyCode,
                                 KeyEvent event) {

                return flag;
            }

        });
        builder.show();


    }

    protected void showSubmitDialog() {
        builderSubmit = new Dialog(this, R.style.dialog);
        builderSubmit.setContentView(R.layout.my_dialog);
        TextView title = (TextView) builderSubmit.findViewById(R.id.dialog_title);
        TextView content = (TextView) builderSubmit.findViewById(R.id.dialog_content);
        content.setText("Spiel abgeschlossen");
        final Button confirm_btn = (Button) builderSubmit
                .findViewById(R.id.dialog_sure);

        confirm_btn.setVisibility(View.GONE);
        Button cancel_btn = (Button) builderSubmit.findViewById(R.id.dialog_cancle);
        cancel_btn.setVisibility(View.GONE);
        builderSubmit.setCanceledOnTouchOutside(false);//
        builderSubmit.setOnKeyListener(new OnKeyListener() {

            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {

                return true;
            }
        });
        builderSubmit.show();


    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            isPause = true;
            showTimeOutDialog(true, "1");
            Message msg = new Message();
            msg.what = 0;
            handlerStopTime.sendMessage(msg);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        Message msg = new Message();
        msg.what = 0;
        handlerStopTime.sendMessage(msg);
        super.onPause();
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        Message msg = new Message();
        msg.what = 1;
        handlerStopTime.sendMessage(msg);
        super.onResume();
    }

    private void startTime() {
        if (timer == null) {
            timer = new Timer();
        }
        if (timerTask == null) {
            timerTask = new TimerTask() {

                @Override
                public void run() {
                    Message msg = new Message();
                    msg.what = 0;
                    handlerTime.sendMessage(msg);
                }
            };
        }
        if (timer != null && timerTask != null) {
            timer.schedule(timerTask, 0, 1000);
        }
    }

    private void stopTime() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        if (timerTask != null) {
            timerTask.cancel();
            timerTask = null;
        }
    }

    /**
     * Background Async Task to Load all INBOX messages by making HTTP Request
     */
    class LoadInbox extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //mSwipeRefreshLayout.setRefreshing(true);
            pDialog = new ProgressDialog(ChallengeQuizActivity.this);
            pDialog.setMessage("Lade Quiz ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }


        /**
         * getting Inbox JSON
         */
        protected String doInBackground(String... args) {

            try {
                JSONObject json = JsonParsingFunctions.readJsonFromUrl(CHALLENGEQUESTION_URL + "?gameid=" + gameid + "&stage=" + stage);
                Log.i("FLOW", CHALLENGEQUESTION_URL + "?gameid=" + gameid + "&stage=" + stage);
                inbox = json.getJSONArray(TAG_MESSAGES);
                // looping through All messages
                for (int i = 0; i < inbox.length(); i++) {
                    JSONObject c = inbox.getJSONObject(i);

                    // Storing each json item in variable
                    String frage = c.getString(TAG_FRAGE);
                    String frage_id = String.valueOf(c.getInt(TAG_FRAGEN_ID));
                    String antwort_1 = c.getString(TAG_ANTWORT_1);
                    String antwort_2 = c.getString(TAG_ANTWORT_2);
                    String antwort_3 = c.getString(TAG_ANTWORT_3);
                    String antwort_4 = c.getString(TAG_ANTWORT_4);
                    String correct_answer = c.getString(TAG_RIGHTANSWER);
                    String sub__name = subkategoriename;

                    sqLiteDB.addQuestion(frage_id, frage, antwort_1, antwort_2, antwort_3, antwort_4, correct_answer, sub__name);
                }

            } catch (JSONException e) {
                e.printStackTrace();
                //Log.i("FLOW",e.toString());
            } catch (IOException e) {
                e.printStackTrace();
                // Log.i("FLOW", e.toString());
            }

            return null;
        }

        /**
         * After completing background task Dismiss the progress dialog
         * *
         */
        protected void onPostExecute(String file_url) {
            // dismiss the dialog after getting all products

            pDialog.dismiss();
            // updating UI from Background Thread
            runOnUiThread(new Runnable() {
                public void run() {

                }
            });

            fillData();
        }

    }

    /**
     * Background Async Task to Load all INBOX messages by making HTTP Request
     */
    class LoadInbox2 extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //mSwipeRefreshLayout.setRefreshing(true);
            pDialog = new ProgressDialog(ChallengeQuizActivity.this);
            pDialog.setMessage("Uebermittle Ergebnisse ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }


        /**
         * getting Inbox JSON
         */
        protected String doInBackground(String... args) {

            int[] q = uploadExam(pagerAdapter.errorTopicNum());

            try {
                q1 = q[0];
                q2 = q[1];
                q3 = q[2];

                JSONObject json = JsonParsingFunctions.readJsonFromUrl(SUBMIT_URL + "?game_id=" + gameid + "&username=" + myusername + "&stage=" + stage + "&Q1=" + q1 + "&Q2=" + q2 + "&Q3=" + q3);
                //Log.i("FLOW",SUBMIT_URL + "?game_id=" + gameid + "&username=" + myusername + "&stage=" + stage + "&Q1=" + q1+ "&Q2=" + q2+ "&Q3=" + q3);
                inbox = json.getJSONArray("");

            } catch (JSONException e) {
                e.printStackTrace();
                //Log.i("FLOW",e.toString());
            } catch (IOException e) {
                e.printStackTrace();
                // Log.i("FLOW", e.toString());
            }

            return null;
        }

        /**
         * After completing background task Dismiss the progress dialog
         * *
         */
        protected void onPostExecute(String file_url) {
            // dismiss the dialog after getting all products

            pDialog.dismiss();
            // updating UI from Background Thread
            runOnUiThread(new Runnable() {
                public void run() {

                }
            });

            Intent overView = new Intent(ChallengeQuizActivity.this, MyErrorQustionActivityChallenge.class);
            overView.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(overView);
        }

    }

}

package trainingsspiel;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.demo.materialdesignnavdrawer.R;
import com.demo.materialdesignnavdrawer.activities.MainActivity;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import campusquizregandlogdesign.com.example.helper.SQLiteQuestionHandler;
import trainingsspiel.adapter.ExaminationSubmitAdapter;
import trainingsspiel.bean.AnSwerInfo;
import trainingsspiel.bean.ErrorQuestionInfo;
import trainingsspiel.bean.SaveQuestionInfo;
import trainingsspiel.database.DBManager;
import trainingsspiel.util.ConstantUtil;
import trainingsspiel.util.ViewPagerScroller;
import trainingsspiel.view.VoteSubmitViewPager;


public class AnalogyExaminationActivity extends Activity {

    public List<Map<String, SaveQuestionInfo>> list = new ArrayList<Map<String, SaveQuestionInfo>>();
    public Map<String, SaveQuestionInfo> map2 = new HashMap<String, SaveQuestionInfo>();
    public List<SaveQuestionInfo> questionInfos = new ArrayList<SaveQuestionInfo>();

    //FK
    //private Button ToggleButton;
    VoteSubmitViewPager viewPager;
    ExaminationSubmitAdapter pagerAdapter;
    //NewExaminationSubmitAdapter pagerAdapter;

    List<View> viewItems = new ArrayList<View>();
    List<AnSwerInfo> dataItems = new ArrayList<AnSwerInfo>();
    Dialog builderSubmit;
    SQLiteQuestionHandler sqLiteDB;
    Timer timer;
    TimerTask timerTask;
    int minute = 3;
    int second = 0;
    boolean isPause = false;
    int isFirst;
    DBManager dbManager;
    String dateStr = "";
    String imgServerUrl = "";
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

            } else {
                right.setTextColor(Color.WHITE);

            }
            if (minute == 0) {
                if (second == 0) {
                    isFirst += 1;
                    // times up
                    if (isFirst == 1) {
                        showTimeOutDialog(true, "0");
                    }
                    right.setText("00:00");

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

                    } else {
                        right.setText("0" + minute + ":0" + second);

                    }
                }
            } else {
                if (second == 0) {
                    second = 59;
                    minute--;
                    if (minute >= 10) {
                        right.setText(minute + ":" + second);

                    } else {
                        right.setText("0" + minute + ":" + second);

                    }
                } else {
                    second--;
                    if (second >= 10) {
                        if (minute >= 10) {
                            right.setText(minute + ":" + second);

                        } else {
                            right.setText("0" + minute + ":" + second);

                        }
                    } else {
                        if (minute >= 10) {
                            right.setText(minute + ":0" + second);

                        } else {
                            right.setText("0" + minute + ":0" + second);

                        }
                    }
                }
            }
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_practice_test);
        dbManager = new DBManager(AnalogyExaminationActivity.this);
        dbManager.openDB();
        initView();
        //TODO: initiate a webservice in LoadData?
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


        titleTv.setText("Training");
        Drawable drawable1 = getBaseContext().getResources().getDrawable(
                R.drawable.ic_action_name3);
        drawable1.setBounds(0, 0, drawable1.getMinimumWidth(),
                drawable1.getMinimumHeight());
        right.setCompoundDrawables(drawable1, null, null, null);

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
        sqLiteDB = new SQLiteQuestionHandler(getApplicationContext());

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

            info.setScore(1);
            info.setOption_type("0");
            dataItems.add(info);
        }

        for (int i = 0; i < dataItems.size(); i++) {
            viewItems.add(getLayoutInflater().inflate(
                    R.layout.vote_submit_viewpager_item, null));
        }
        pagerAdapter = new ExaminationSubmitAdapter(
                AnalogyExaminationActivity.this, viewItems,
                dataItems, imgServerUrl);


        viewPager.setAdapter(pagerAdapter);
        viewPager.getParent()
                .requestDisallowInterceptTouchEvent(false);

    }


    /**
     *
     *
     * */
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
     * @param index
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

        Intent overView = new Intent(AnalogyExaminationActivity.this, MyErrorQuestionActivity.class);
        overView.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(overView);

    }

    protected void showTimeOutDialog(final boolean flag, final String backtype) {
        final Dialog builder = new Dialog(this, R.style.dialog);
        builder.setContentView(R.layout.my_dialog);
        TextView title = (TextView) builder.findViewById(R.id.dialog_title);
        TextView content = (TextView) builder.findViewById(R.id.dialog_content);
        if (backtype.equals("0")) {
            content.setText("Die Zeit ist abgelaufen!");
        } else if (backtype.equals("1")) {
            content.setText("Willst du das Training beenden？");
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
                    Intent backhomemenuIntent = new Intent(AnalogyExaminationActivity.this, MainActivity.class);
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
                    Intent backhomemenuIntent = new Intent(AnalogyExaminationActivity.this, MainActivity.class);
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
        content.setText("Trainingsspiel abgeschlossen");
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

}

package trainingsspiel.adapter;

import android.graphics.Color;
import android.support.v4.view.PagerAdapter;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.demo.materialdesignnavdrawer.R;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import trainingsspiel.ChallengeQuizActivity;
import trainingsspiel.bean.AnSwerInfo;
import trainingsspiel.bean.ErrorQuestionInfo;
import trainingsspiel.bean.SaveQuestionInfo;
import trainingsspiel.database.DBManager;
import trainingsspiel.util.ConstantUtil;

/**
 * Created by Administrator on 16.07.2015.
 */
public class ExaminationSubmitAdapterChallenge extends PagerAdapter {


    ChallengeQuizActivity newContext;

    List<View> viewItems;

    View convertView;

    List<AnSwerInfo> dataItems;
    Integer pressed = 0;
    String imgServerUrl = "";
    boolean isNext = false;
    StringBuffer answer = new StringBuffer();
    StringBuffer answerLast = new StringBuffer();
    StringBuffer answer1 = new StringBuffer();
    DBManager dbManager;
    String isCorrect = ConstantUtil.isCorrect;//
    int errortopicNum = 0;
    private Map<Integer, Boolean> map = new HashMap<Integer, Boolean>();
    private Map<Integer, Boolean> mapClick = new HashMap<Integer, Boolean>();
    private Map<Integer, String> mapMultiSelect = new HashMap<Integer, String>();
    private boolean isClick = false;


    public ExaminationSubmitAdapterChallenge(ChallengeQuizActivity context, List<View> viewItems, List<AnSwerInfo> dataItems, String imgServerUrl) {
        newContext = context;
        this.viewItems = viewItems;
        this.dataItems = dataItems;
        this.imgServerUrl = imgServerUrl;
        dbManager = new DBManager(context);
        dbManager.openDB();
    }


    public long getItemId(int position) {
        return position;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(viewItems.get(position));
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        final ViewHolder holder = new ViewHolder();
        convertView = viewItems.get(position);
        holder.categorieName = (TextView) convertView.findViewById(R.id.activity_prepare_test_subcategorie);// set subcateogrie for every questions
        holder.question = (TextView) convertView.findViewById(R.id.activity_prepare_test_question);


        holder.totalText = (TextView) convertView.findViewById(R.id.activity_prepare_test_totalTv);
        holder.wrongLayout = (LinearLayout) convertView.findViewById(R.id.activity_prepare_test_wrongLayout);

        holder.layoutA = (LinearLayout) convertView.findViewById(R.id.activity_prepare_test_layout_a);
        holder.layoutB = (LinearLayout) convertView.findViewById(R.id.activity_prepare_test_layout_b);
        holder.layoutC = (LinearLayout) convertView.findViewById(R.id.activity_prepare_test_layout_c);
        holder.layoutD = (LinearLayout) convertView.findViewById(R.id.activity_prepare_test_layout_d);
        holder.nextlayout = (LinearLayout) convertView.findViewById(R.id.activity_next_layout);


        holder.next_question = (TextView) convertView.findViewById(R.id.countDownTextview);
        holder.ivA = (ImageView) convertView.findViewById(R.id.vote_submit_select_image_a);
        holder.ivB = (ImageView) convertView.findViewById(R.id.vote_submit_select_image_b);
        holder.ivC = (ImageView) convertView.findViewById(R.id.vote_submit_select_image_c);
        holder.ivD = (ImageView) convertView.findViewById(R.id.vote_submit_select_image_d);

        holder.tvA = (TextView) convertView.findViewById(R.id.vote_submit_select_text_a);
        holder.tvB = (TextView) convertView.findViewById(R.id.vote_submit_select_text_b);
        holder.tvC = (TextView) convertView.findViewById(R.id.vote_submit_select_text_c);
        holder.tvD = (TextView) convertView.findViewById(R.id.vote_submit_select_text_d);

        holder.ivA_ = (ImageView) convertView.findViewById(R.id.vote_submit_select_image_a_);
        holder.ivB_ = (ImageView) convertView.findViewById(R.id.vote_submit_select_image_b_);
        holder.ivC_ = (ImageView) convertView.findViewById(R.id.vote_submit_select_image_c_);
        holder.ivD_ = (ImageView) convertView.findViewById(R.id.vote_submit_select_image_d_);

        holder.totalText.setText(position + 1 + "/" + dataItems.size());


        if (dataItems.get(position).getOptionA().equals("")) {
            holder.layoutA.setVisibility(View.GONE);
        }
        if (dataItems.get(position).getOptionB().equals("")) {
            holder.layoutB.setVisibility(View.GONE);
        }
        if (dataItems.get(position).getOptionC().equals("")) {
            holder.layoutC.setVisibility(View.GONE);
        }
        if (dataItems.get(position).getOptionD().equals("")) {
            holder.layoutD.setVisibility(View.GONE);
        }


        holder.ivA_.setVisibility(View.GONE);
        holder.ivB_.setVisibility(View.GONE);
        holder.ivC_.setVisibility(View.GONE);
        holder.ivD_.setVisibility(View.GONE);

        holder.tvA.setVisibility(View.VISIBLE);
        holder.tvB.setVisibility(View.VISIBLE);
        holder.tvC.setVisibility(View.VISIBLE);
        holder.tvD.setVisibility(View.VISIBLE);

        holder.tvA.setText(" " + dataItems.get(position).getOptionA());
        holder.tvB.setText(" " + dataItems.get(position).getOptionB());
        holder.tvC.setText(" " + dataItems.get(position).getOptionC());
        holder.tvD.setText(" " + dataItems.get(position).getOptionD());


        if (dataItems.get(position).getQuestionType().equals("0")) {


            holder.categorieName.setText("" + dataItems.get(position).getSubCategorie());// retrieve name of subcategorie
            holder.question.setText("" + dataItems.get(position).getQuestionName());

            holder.layoutA.setOnClickListener(new View.OnClickListener() {


                @Override
                public void onClick(View v) {

                    pressed++;
                    if (map.containsKey(position)) {
                        return;
                    }
                    map.put(position, true);
                    if (dataItems.get(position).getCorrectAnswer().contains("1")) {

                        newContext.setCurrentView(position + 1);
                        holder.ivA.setImageResource(R.drawable.ic_practice_test_right_new);
                        holder.tvA.setTextColor(Color.parseColor("#61bc31"));
                        isCorrect = ConstantUtil.isCorrect;
                        //Test
                        errortopicNum += 1;

                        ErrorQuestionInfo errorQuestionInfo = new ErrorQuestionInfo();
                        errorQuestionInfo.setQuestionName(dataItems.get(position).getQuestionName());
                        errorQuestionInfo.setQuestionType(dataItems.get(position).getQuestionType());
                        errorQuestionInfo.setQuestionAnswer(dataItems.get(position).getCorrectAnswer());
                        errorQuestionInfo.setIsRight(isCorrect);
                        //errorQuestionInfo.setQuestionAnswer("1");
                        errorQuestionInfo.setQuestionSelect("First_Correct");

                        errorQuestionInfo.setOptionType(dataItems.get(position).getOption_type());
                        if (dataItems.get(position).getOption_type().equals("0")) {
                            errorQuestionInfo.setOptionA(dataItems.get(position).getOptionA());
                            errorQuestionInfo.setOptionB(dataItems.get(position).getOptionB());
                            errorQuestionInfo.setOptionC(dataItems.get(position).getOptionC());
                            errorQuestionInfo.setOptionD(dataItems.get(position).getOptionD());

                        }
                        long colunm = dbManager.insertErrorQuestion(errorQuestionInfo);


                    } else {
                        isCorrect = ConstantUtil.isError;
                        errortopicNum += 1;

                        ErrorQuestionInfo errorQuestionInfo = new ErrorQuestionInfo();
                        errorQuestionInfo.setQuestionName(dataItems.get(position).getQuestionName());
                        errorQuestionInfo.setQuestionType(dataItems.get(position).getQuestionType());
                        errorQuestionInfo.setQuestionAnswer(dataItems.get(position).getCorrectAnswer());
                        errorQuestionInfo.setIsRight(isCorrect);
                        errorQuestionInfo.setQuestionSelect("1");

                        errorQuestionInfo.setOptionType(dataItems.get(position).getOption_type());
                        if (dataItems.get(position).getOption_type().equals("0")) {
                            errorQuestionInfo.setOptionA(dataItems.get(position).getOptionA());
                            errorQuestionInfo.setOptionB(dataItems.get(position).getOptionB());
                            errorQuestionInfo.setOptionC(dataItems.get(position).getOptionC());
                            errorQuestionInfo.setOptionD(dataItems.get(position).getOptionD());

                        }
                        long colunm = dbManager.insertErrorQuestion(errorQuestionInfo);

                        if (colunm == -1) {
                            Toast.makeText(newContext, "Falsche Antwort", Toast.LENGTH_SHORT).show();
                        }


                        holder.ivA.setImageResource(R.drawable.ic_practice_test_wrong_new);
                        holder.tvA.setTextColor(Color.parseColor("#d53235"));


                        if (dataItems.get(position).getCorrectAnswer().contains("1")) {
                            holder.ivA.setImageResource(R.drawable.ic_practice_test_right_new);
                            holder.tvA.setTextColor(Color.parseColor("#61bc31"));
                        } else if (dataItems.get(position).getCorrectAnswer().contains("2")) {
                            holder.ivB.setImageResource(R.drawable.ic_practice_test_right_new);
                            holder.tvB.setTextColor(Color.parseColor("#61bc31"));
                        } else if (dataItems.get(position).getCorrectAnswer().contains("3")) {
                            holder.ivC.setImageResource(R.drawable.ic_practice_test_right_new);
                            holder.tvC.setTextColor(Color.parseColor("#61bc31"));
                        } else if (dataItems.get(position).getCorrectAnswer().contains("4")) {
                            holder.ivD.setImageResource(R.drawable.ic_practice_test_right_new);
                            holder.tvD.setTextColor(Color.parseColor("#61bc31"));
                        }


                    }

                    SaveQuestionInfo questionInfo = new SaveQuestionInfo();
                    questionInfo.setQuestionId(dataItems.get(position).getQuestionId());
                    questionInfo.setQuestionType(dataItems.get(position).getQuestionType());
                    questionInfo.setRealAnswer(dataItems.get(position).getCorrectAnswer());
                    questionInfo.setScore(dataItems.get(position).getScore());
                    questionInfo.setIs_correct(isCorrect);
                    newContext.questionInfos.add(questionInfo);
                    dataItems.get(position).setIsSelect("0");
                }
            });
            holder.layoutB.setOnClickListener(new View.OnClickListener() {


                @Override
                public void onClick(View arg0) {

                    if (map.containsKey(position)) {
                        return;
                    }
                    map.put(position, true);
                    if (dataItems.get(position).getCorrectAnswer().contains("2")) {

                        newContext.setCurrentView(position + 1);
                        holder.ivB.setImageResource(R.drawable.ic_practice_test_right_new);
                        holder.tvB.setTextColor(Color.parseColor("#61bc31"));
                        isCorrect = ConstantUtil.isCorrect;
                        //Test B
                        errortopicNum += 1;

                        ErrorQuestionInfo errorQuestionInfo = new ErrorQuestionInfo();
                        errorQuestionInfo.setQuestionName(dataItems.get(position).getQuestionName());
                        errorQuestionInfo.setQuestionType(dataItems.get(position).getQuestionType());
                        errorQuestionInfo.setQuestionAnswer(dataItems.get(position).getCorrectAnswer());
                        errorQuestionInfo.setIsRight(isCorrect);

                        errorQuestionInfo.setQuestionSelect("Second_Correct");

                        errorQuestionInfo.setOptionType(dataItems.get(position).getOption_type());
                        if (dataItems.get(position).getOption_type().equals("0")) {
                            errorQuestionInfo.setOptionA(dataItems.get(position).getOptionA());
                            errorQuestionInfo.setOptionB(dataItems.get(position).getOptionB());
                            errorQuestionInfo.setOptionC(dataItems.get(position).getOptionC());
                            errorQuestionInfo.setOptionD(dataItems.get(position).getOptionD());

                        }
                        long colunm = dbManager.insertErrorQuestion(errorQuestionInfo);


                    } else {
                        isCorrect = ConstantUtil.isError;
                        errortopicNum += 1;

                        ErrorQuestionInfo errorQuestionInfo = new ErrorQuestionInfo();
                        errorQuestionInfo.setQuestionName(dataItems.get(position).getQuestionName());
                        errorQuestionInfo.setQuestionType(dataItems.get(position).getQuestionType());
                        errorQuestionInfo.setQuestionAnswer(dataItems.get(position).getCorrectAnswer());
                        errorQuestionInfo.setIsRight(isCorrect);
                        errorQuestionInfo.setQuestionSelect("2");

                        errorQuestionInfo.setOptionType(dataItems.get(position).getOption_type());
                        if (dataItems.get(position).getOption_type().equals("0")) {
                            errorQuestionInfo.setOptionA(dataItems.get(position).getOptionA());
                            errorQuestionInfo.setOptionB(dataItems.get(position).getOptionB());
                            errorQuestionInfo.setOptionC(dataItems.get(position).getOptionC());
                            errorQuestionInfo.setOptionD(dataItems.get(position).getOptionD());

                        }
                        long colunm = dbManager.insertErrorQuestion(errorQuestionInfo);

                        if (colunm == -1) {
                            Toast.makeText(newContext, "Falsche Antwort", Toast.LENGTH_SHORT).show();
                        }


                        holder.ivB.setImageResource(R.drawable.ic_practice_test_wrong_new);
                        holder.tvB.setTextColor(Color.parseColor("#d53235"));


                        if (dataItems.get(position).getCorrectAnswer().contains("1")) {
                            holder.ivA.setImageResource(R.drawable.ic_practice_test_right_new);
                            holder.tvA.setTextColor(Color.parseColor("#61bc31"));
                        } else if (dataItems.get(position).getCorrectAnswer().contains("2")) {
                            holder.ivB.setImageResource(R.drawable.ic_practice_test_right_new);
                            holder.tvB.setTextColor(Color.parseColor("#61bc31"));
                        } else if (dataItems.get(position).getCorrectAnswer().contains("3")) {
                            holder.ivC.setImageResource(R.drawable.ic_practice_test_right_new);
                            holder.tvC.setTextColor(Color.parseColor("#61bc31"));
                        } else if (dataItems.get(position).getCorrectAnswer().contains("4")) {
                            holder.ivD.setImageResource(R.drawable.ic_practice_test_right_new);
                            holder.tvD.setTextColor(Color.parseColor("#61bc31"));
                        }


                    }

                    SaveQuestionInfo questionInfo = new SaveQuestionInfo();
                    questionInfo.setQuestionId(dataItems.get(position).getQuestionId());
                    questionInfo.setQuestionType(dataItems.get(position).getQuestionType());
                    questionInfo.setRealAnswer(dataItems.get(position).getCorrectAnswer());
                    questionInfo.setScore(dataItems.get(position).getScore());
                    questionInfo.setIs_correct(isCorrect);
                    newContext.questionInfos.add(questionInfo);
                    dataItems.get(position).setIsSelect("0");
                }
            });
            holder.layoutC.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View arg0) {

                    if (map.containsKey(position)) {
                        return;
                    }
                    map.put(position, true);
                    if (dataItems.get(position).getCorrectAnswer().contains("3")) {

                        newContext.setCurrentView(position + 1);
                        holder.ivC.setImageResource(R.drawable.ic_practice_test_right_new);
                        holder.tvC.setTextColor(Color.parseColor("#61bc31"));
                        isCorrect = ConstantUtil.isCorrect;

                        errortopicNum += 1;

                        ErrorQuestionInfo errorQuestionInfo = new ErrorQuestionInfo();
                        errorQuestionInfo.setQuestionName(dataItems.get(position).getQuestionName());
                        errorQuestionInfo.setQuestionType(dataItems.get(position).getQuestionType());
                        errorQuestionInfo.setQuestionAnswer(dataItems.get(position).getCorrectAnswer());
                        errorQuestionInfo.setIsRight(isCorrect);

                        errorQuestionInfo.setQuestionSelect("_Correct");

                        errorQuestionInfo.setOptionType(dataItems.get(position).getOption_type());
                        if (dataItems.get(position).getOption_type().equals("0")) {
                            errorQuestionInfo.setOptionA(dataItems.get(position).getOptionA());
                            errorQuestionInfo.setOptionB(dataItems.get(position).getOptionB());
                            errorQuestionInfo.setOptionC(dataItems.get(position).getOptionC());
                            errorQuestionInfo.setOptionD(dataItems.get(position).getOptionD());

                        }
                        long colunm = dbManager.insertErrorQuestion(errorQuestionInfo);
                    } else {
                        isCorrect = ConstantUtil.isError;
                        errortopicNum += 1;

                        ErrorQuestionInfo errorQuestionInfo = new ErrorQuestionInfo();
                        errorQuestionInfo.setQuestionName(dataItems.get(position).getQuestionName());
                        errorQuestionInfo.setQuestionType(dataItems.get(position).getQuestionType());
                        errorQuestionInfo.setQuestionAnswer(dataItems.get(position).getCorrectAnswer());
                        errorQuestionInfo.setIsRight(isCorrect);
                        errorQuestionInfo.setQuestionSelect("3");

                        errorQuestionInfo.setOptionType(dataItems.get(position).getOption_type());
                        if (dataItems.get(position).getOption_type().equals("0")) {
                            errorQuestionInfo.setOptionA(dataItems.get(position).getOptionA());
                            errorQuestionInfo.setOptionB(dataItems.get(position).getOptionB());
                            errorQuestionInfo.setOptionC(dataItems.get(position).getOptionC());
                            errorQuestionInfo.setOptionD(dataItems.get(position).getOptionD());

                        }
                        long colunm = dbManager.insertErrorQuestion(errorQuestionInfo);

                        if (colunm == -1) {
                            Toast.makeText(newContext, "Falsche Antwort", Toast.LENGTH_SHORT).show();
                        }

                        holder.ivC.setImageResource(R.drawable.ic_practice_test_wrong_new);
                        holder.tvC.setTextColor(Color.parseColor("#d53235"));


                        if (dataItems.get(position).getCorrectAnswer().contains("1")) {
                            holder.ivA.setImageResource(R.drawable.ic_practice_test_right_new);
                            holder.tvA.setTextColor(Color.parseColor("#61bc31"));
                        } else if (dataItems.get(position).getCorrectAnswer().contains("2")) {
                            holder.ivB.setImageResource(R.drawable.ic_practice_test_right_new);
                            holder.tvB.setTextColor(Color.parseColor("#61bc31"));
                        } else if (dataItems.get(position).getCorrectAnswer().contains("3")) {
                            holder.ivC.setImageResource(R.drawable.ic_practice_test_right_new);
                            holder.tvC.setTextColor(Color.parseColor("#61bc31"));
                        } else if (dataItems.get(position).getCorrectAnswer().contains("4")) {
                            holder.ivD.setImageResource(R.drawable.ic_practice_test_right_new);
                            holder.tvD.setTextColor(Color.parseColor("#61bc31"));
                        }

                    }

                    SaveQuestionInfo questionInfo = new SaveQuestionInfo();
                    questionInfo.setQuestionId(dataItems.get(position).getQuestionId());
                    questionInfo.setQuestionType(dataItems.get(position).getQuestionType());
                    questionInfo.setRealAnswer(dataItems.get(position).getCorrectAnswer());
                    questionInfo.setScore(dataItems.get(position).getScore());
                    questionInfo.setIs_correct(isCorrect);
                    newContext.questionInfos.add(questionInfo);
                    dataItems.get(position).setIsSelect("0");
                }
            });
            holder.layoutD.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View arg0) {

                    if (map.containsKey(position)) {
                        return;
                    }
                    map.put(position, true);
                    if (dataItems.get(position).getCorrectAnswer().contains("4")) {

                        newContext.setCurrentView(position + 1); // next questions
                        holder.ivD.setImageResource(R.drawable.ic_practice_test_right_new);
                        holder.tvD.setTextColor(Color.parseColor("#61bc31"));
                        isCorrect = ConstantUtil.isCorrect;

                        errortopicNum += 1;

                        ErrorQuestionInfo errorQuestionInfo = new ErrorQuestionInfo();
                        errorQuestionInfo.setQuestionName(dataItems.get(position).getQuestionName());
                        errorQuestionInfo.setQuestionType(dataItems.get(position).getQuestionType());
                        errorQuestionInfo.setQuestionAnswer(dataItems.get(position).getCorrectAnswer());
                        errorQuestionInfo.setIsRight(isCorrect);

                        errorQuestionInfo.setQuestionSelect("First_Correct");

                        errorQuestionInfo.setOptionType(dataItems.get(position).getOption_type());
                        if (dataItems.get(position).getOption_type().equals("0")) {
                            errorQuestionInfo.setOptionA(dataItems.get(position).getOptionA());
                            errorQuestionInfo.setOptionB(dataItems.get(position).getOptionB());
                            errorQuestionInfo.setOptionC(dataItems.get(position).getOptionC());
                            errorQuestionInfo.setOptionD(dataItems.get(position).getOptionD());

                        }
                        long colunm = dbManager.insertErrorQuestion(errorQuestionInfo);

                    } else {
                        isCorrect = ConstantUtil.isError;
                        errortopicNum += 1;

                        ErrorQuestionInfo errorQuestionInfo = new ErrorQuestionInfo();
                        errorQuestionInfo.setQuestionName(dataItems.get(position).getQuestionName());
                        errorQuestionInfo.setQuestionType(dataItems.get(position).getQuestionType());
                        errorQuestionInfo.setQuestionAnswer(dataItems.get(position).getCorrectAnswer());
                        errorQuestionInfo.setIsRight(isCorrect);
                        errorQuestionInfo.setQuestionSelect("4");

                        errorQuestionInfo.setOptionType(dataItems.get(position).getOption_type());
                        if (dataItems.get(position).getOption_type().equals("0")) {
                            errorQuestionInfo.setOptionA(dataItems.get(position).getOptionA());
                            errorQuestionInfo.setOptionB(dataItems.get(position).getOptionB());
                            errorQuestionInfo.setOptionC(dataItems.get(position).getOptionC());
                            errorQuestionInfo.setOptionD(dataItems.get(position).getOptionD());

                        }
                        long colunm = dbManager.insertErrorQuestion(errorQuestionInfo);

                        if (colunm == -1) {
                            Toast.makeText(newContext, "Falsche Antwrot", Toast.LENGTH_SHORT).show();
                        }
                        holder.ivD.setImageResource(R.drawable.ic_practice_test_wrong_new);
                        holder.tvD.setTextColor(Color.parseColor("#d53235"));


                        if (dataItems.get(position).getCorrectAnswer().contains("1")) {
                            holder.ivA.setImageResource(R.drawable.ic_practice_test_right_new);
                            holder.tvA.setTextColor(Color.parseColor("#61bc31"));
                        } else if (dataItems.get(position).getCorrectAnswer().contains("2")) {
                            holder.ivB.setImageResource(R.drawable.ic_practice_test_right_new);
                            holder.tvB.setTextColor(Color.parseColor("#61bc31"));
                        } else if (dataItems.get(position).getCorrectAnswer().contains("3")) {
                            holder.ivC.setImageResource(R.drawable.ic_practice_test_right_new);
                            holder.tvC.setTextColor(Color.parseColor("#61bc31"));
                        } else if (dataItems.get(position).getCorrectAnswer().contains("4")) {
                            holder.ivD.setImageResource(R.drawable.ic_practice_test_right_new);
                            holder.tvD.setTextColor(Color.parseColor("#61bc31"));
                        }


                    }

                    SaveQuestionInfo questionInfo = new SaveQuestionInfo();
                    questionInfo.setQuestionId(dataItems.get(position).getQuestionId());
                    questionInfo.setQuestionType(dataItems.get(position).getQuestionType());
                    questionInfo.setRealAnswer(dataItems.get(position).getCorrectAnswer());
                    questionInfo.setScore(dataItems.get(position).getScore());
                    questionInfo.setIs_correct(isCorrect);
                    newContext.questionInfos.add(questionInfo);
                    dataItems.get(position).setIsSelect("0");
                }
            });


        }


        ForegroundColorSpan blackSpan = new ForegroundColorSpan(Color.parseColor("#ffffff"));// turn the first 3 digits color back to balck...

        SpannableStringBuilder builder1 = new SpannableStringBuilder(holder.question.getText().toString());
        builder1.setSpan(blackSpan, 0, 5, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        holder.question.setText(builder1);


        if (position == viewItems.size() - 1) {

            holder.next_question.setText("Abgeben");
        }


        holder.nextlayout.setOnClickListener(new LinearLayoutOnClickListener(position + 1, true, position, holder));// control next move


        container.addView(viewItems.get(position));

        return viewItems.get(position);
    }

    @Override
    public int getCount() {
        if (viewItems == null)
            return 0;
        return viewItems.size();
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == arg1;
    }

    //错题数
    public int errorTopicNum() {
        if (errortopicNum != 0) {
            return errortopicNum;
        }
        return 0;
    }

    class LinearLayoutOnClickListener implements View.OnClickListener {

        private int mPosition;
        private int mPosition1;
        private boolean mIsNext;
        private ViewHolder viewHolder;


        public LinearLayoutOnClickListener(int position, boolean mIsNext, int position1, ViewHolder viewHolder) {
            mPosition = position;
            mPosition1 = position1;
            this.viewHolder = viewHolder;
            this.mIsNext = mIsNext;

        }

        @Override
        public void onClick(View v) {

            //timerProcessing[0] = true;

            if (mPosition == viewItems.size()) {
                //single choice
                if (dataItems.get(mPosition1).getQuestionType().equals("0")) {
                    if (!map.containsKey(mPosition1)) {
                        Toast.makeText(newContext, "Hast du schon geantwortet?", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    newContext.uploadExamination(errortopicNum);


                }
            } else {

                if (dataItems.get(mPosition1).getQuestionType().equals("0")) {
                    if (mIsNext) {
                        if (!map.containsKey(mPosition1)) {
                            Toast.makeText(newContext, "Hast du schon geantwortet?", Toast.LENGTH_SHORT).show();
                            return;
                        }


                    }

                    isNext = mIsNext;

                    newContext.setCurrentView(mPosition);

                }

            }

        }

    }

    public class ViewHolder {

        TextView questionType;
        TextView question;
        TextView categorieName;
        //	LinearLayout previousBtn, nextBtn,errorBtn;

        Button nextBtn;
        TextView totalText;
        LinearLayout wrongLayout;

        LinearLayout layoutA;
        LinearLayout layoutB;
        LinearLayout layoutC;
        LinearLayout layoutD;
        LinearLayout nextlayout;

        TextView next_question;

        ImageView ivA;
        ImageView ivB;
        ImageView ivC;
        ImageView ivD;

        TextView tvA;
        TextView tvB;
        TextView tvC;
        TextView tvD;

        ImageView ivA_;
        ImageView ivB_;
        ImageView ivC_;
        ImageView ivD_;


    }


}

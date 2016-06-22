package trainingsspiel;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.demo.materialdesignnavdrawer.R;

import trainingsspiel.util.ConstantUtil;

/**
 * ´íÌâÏêÇé
 *
 * @author ½ðÖÓ»À
 */
public class MyErrorQuestionDetailActivity extends Activity {

    private ImageView left;
    private TextView title;


    private TextView questionNameTV;
    private LinearLayout layoutA;
    private LinearLayout layoutB;
    private LinearLayout layoutC;
    private LinearLayout layoutD;
    private LinearLayout layoutE;
    private ImageView ivA;
    private ImageView ivB;
    private ImageView ivC;
    private ImageView ivD;
    private ImageView ivE;
    private TextView tvA;
    private TextView tvB;
    private TextView tvC;
    private TextView tvD;

    private ImageView ivA_;
    private ImageView ivB_;
    private ImageView ivC_;
    private ImageView ivD_;

    private LinearLayout wrongLayout;


    private String questionName = "";
    private String questionType = "";
    private String questionAnswer = "";
    private String questionSelect = "";
    private String isRight = "";
    private String Analysis = "";
    private String optionA = "";
    private String optionB = "";
    private String optionC = "";
    private String optionD = "";
    private String optionType = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.my_error_question_detail);
        initView();
    }

    private void initView() {
        questionName = getIntent().getStringExtra("questionName");
        questionType = getIntent().getStringExtra("questionType");
        questionAnswer = getIntent().getStringExtra("questionAnswer");
        questionSelect = getIntent().getStringExtra("questionSelect");
        isRight = getIntent().getStringExtra("isRight");
        Analysis = getIntent().getStringExtra("Analysis");
        optionA = getIntent().getStringExtra("optionA");
        optionB = getIntent().getStringExtra("optionB");
        optionC = getIntent().getStringExtra("optionC");
        optionD = getIntent().getStringExtra("optionD");

        optionType = getIntent().getStringExtra("optionType");
        left = (ImageView) findViewById(R.id.left);
        title = (TextView) findViewById(R.id.title);
        title.setText("Meine Antwort");
        left.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                finish();
            }
        });


        questionNameTV = (TextView) findViewById(R.id.activity_prepare_test_question);
        layoutA = (LinearLayout) findViewById(R.id.activity_prepare_test_layout_a);
        layoutB = (LinearLayout) findViewById(R.id.activity_prepare_test_layout_b);
        layoutC = (LinearLayout) findViewById(R.id.activity_prepare_test_layout_c);
        layoutD = (LinearLayout) findViewById(R.id.activity_prepare_test_layout_d);

        ivA = (ImageView) findViewById(R.id.vote_submit_select_image_a);
        ivB = (ImageView) findViewById(R.id.vote_submit_select_image_b);
        ivC = (ImageView) findViewById(R.id.vote_submit_select_image_c);
        ivD = (ImageView) findViewById(R.id.vote_submit_select_image_d);

        tvA = (TextView) findViewById(R.id.vote_submit_select_text_a);
        tvB = (TextView) findViewById(R.id.vote_submit_select_text_b);
        tvC = (TextView) findViewById(R.id.vote_submit_select_text_c);
        tvD = (TextView) findViewById(R.id.vote_submit_select_text_d);

        ivA_ = (ImageView) findViewById(R.id.vote_submit_select_image_a_);
        ivB_ = (ImageView) findViewById(R.id.vote_submit_select_image_b_);
        ivC_ = (ImageView) findViewById(R.id.vote_submit_select_image_c_);
        ivD_ = (ImageView) findViewById(R.id.vote_submit_select_image_d_);

        wrongLayout = (LinearLayout) findViewById(R.id.activity_prepare_test_wrongLayout);


        questionNameTV.setText("" + questionName);

        if (optionA.equals("")) {
            layoutA.setVisibility(View.GONE);
        }
        if (optionB.equals("")) {
            layoutB.setVisibility(View.GONE);
        }
        if (optionC.equals("")) {
            layoutC.setVisibility(View.GONE);
        }
        if (optionD.equals("")) {
            layoutD.setVisibility(View.GONE);
        }

        ivA_.setVisibility(View.GONE);
        ivB_.setVisibility(View.GONE);
        ivC_.setVisibility(View.GONE);
        ivD_.setVisibility(View.GONE);

        tvA.setVisibility(View.VISIBLE);
        tvB.setVisibility(View.VISIBLE);
        tvC.setVisibility(View.VISIBLE);
        tvD.setVisibility(View.VISIBLE);

        tvA.setText("" + optionA);
        tvB.setText("" + optionB);
        tvC.setText("" + optionC);
        tvD.setText("" + optionD);


        if (questionType.equals("0")) {

            if (questionAnswer.contains("1")) {
                ivA.setImageResource(R.drawable.ic_practice_test_right_new);
                tvA.setTextColor(Color.parseColor("#61bc31"));
            } else if (questionAnswer.contains("2")) {
                ivB.setImageResource(R.drawable.ic_practice_test_right_new);
                tvB.setTextColor(Color.parseColor("#61bc31"));
            } else if (questionAnswer.contains("3")) {
                ivC.setImageResource(R.drawable.ic_practice_test_right_new);
                tvC.setTextColor(Color.parseColor("#61bc31"));
            } else if (questionAnswer.contains("4")) {
                ivD.setImageResource(R.drawable.ic_practice_test_right_new);
                tvD.setTextColor(Color.parseColor("#61bc31"));
            }


            if (questionSelect.contains("1")) {
                ivA.setImageResource(R.drawable.ic_practice_test_wrong_new);
                tvA.setTextColor(Color.parseColor("#d53235"));
            } else if (questionSelect.contains("2")) {
                ivB.setImageResource(R.drawable.ic_practice_test_wrong_new);
                tvB.setTextColor(Color.parseColor("#d53235"));
            } else if (questionSelect.contains("3")) {
                ivC.setImageResource(R.drawable.ic_practice_test_wrong_new);
                tvC.setTextColor(Color.parseColor("#d53235"));
            } else if (questionSelect.contains("4")) {
                ivD.setImageResource(R.drawable.ic_practice_test_wrong_new);
                tvD.setTextColor(Color.parseColor("#d53235"));
            }

        } else if (questionType.equals("1")) {

            if (questionAnswer.contains("1")) {
                ivA.setImageResource(R.drawable.ic_practice_test_right_new);
                tvA.setTextColor(Color.parseColor("#61bc31"));
            }
            if (questionAnswer.contains("2")) {
                ivB.setImageResource(R.drawable.ic_practice_test_right_new);
                tvB.setTextColor(Color.parseColor("#61bc31"));
            }
            if (questionAnswer.contains("3")) {
                ivC.setImageResource(R.drawable.ic_practice_test_right_new);
                tvC.setTextColor(Color.parseColor("#61bc31"));
            }
            if (questionAnswer.contains("4")) {
                ivD.setImageResource(R.drawable.ic_practice_test_right_new);
                tvD.setTextColor(Color.parseColor("#61bc31"));
            }

            if (questionSelect.contains("1")) {
                ivA.setImageResource(R.drawable.ic_practice_test_wrong_new);
                tvA.setTextColor(Color.parseColor("#d53235"));
            }
            if (questionSelect.contains("2")) {
                ivB.setImageResource(R.drawable.ic_practice_test_wrong_new);
                tvB.setTextColor(Color.parseColor("#d53235"));
            }
            if (questionSelect.contains("3")) {
                ivC.setImageResource(R.drawable.ic_practice_test_wrong_new);
                tvC.setTextColor(Color.parseColor("#d53235"));
            }
            if (questionSelect.contains("4")) {
                ivD.setImageResource(R.drawable.ic_practice_test_wrong_new);
                tvD.setTextColor(Color.parseColor("#d53235"));
            }

        } else if (questionType.equals("2")) {

            if (questionAnswer.contains("1")) {
                ivA.setImageResource(R.drawable.ic_practice_test_right_new);
                tvA.setTextColor(Color.parseColor("#61bc31"));
            } else if (questionAnswer.contains("2")) {
                ivB.setImageResource(R.drawable.ic_practice_test_right_new);
                tvB.setTextColor(Color.parseColor("#61bc31"));
            } else if (questionAnswer.contains("3")) {
                ivC.setImageResource(R.drawable.ic_practice_test_right_new);
                tvC.setTextColor(Color.parseColor("#61bc31"));
            } else if (questionAnswer.contains("4")) {
                ivD.setImageResource(R.drawable.ic_practice_test_right_new);
                tvD.setTextColor(Color.parseColor("#61bc31"));
            }

            if (questionSelect.contains("1")) {
                ivA.setImageResource(R.drawable.ic_practice_test_wrong_new);
                tvA.setTextColor(Color.parseColor("#d53235"));
            } else if (questionSelect.contains("2")) {
                ivB.setImageResource(R.drawable.ic_practice_test_wrong_new);
                tvB.setTextColor(Color.parseColor("#d53235"));
            } else if (questionSelect.contains("3")) {
                ivC.setImageResource(R.drawable.ic_practice_test_wrong_new);
                tvC.setTextColor(Color.parseColor("#d53235"));
            } else if (questionSelect.contains("4")) {
                ivD.setImageResource(R.drawable.ic_practice_test_wrong_new);
                tvD.setTextColor(Color.parseColor("#d53235"));
            }

            layoutC.setVisibility(View.GONE);
            layoutD.setVisibility(View.GONE);
        }

        if (isRight.equals(ConstantUtil.isError)) {
            wrongLayout.setVisibility(View.VISIBLE);

        } else {
            wrongLayout.setVisibility(View.GONE);
        }
    }

}

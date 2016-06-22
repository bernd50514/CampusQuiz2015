package trainingsspiel;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.demo.materialdesignnavdrawer.R;

public class StartTrainingActivity extends Activity {

    private Button startBtn;

    private ImageView left;
    private TextView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        left = (ImageView) findViewById(R.id.left);
        title = (TextView) findViewById(R.id.title);

        left.setVisibility(View.GONE);
        title.setText("Test");

        startBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent(StartTrainingActivity.this, AnalogyExaminationActivity.class);
                startActivity(intent);
            }
        });

    }


}

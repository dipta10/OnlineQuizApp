package semester2_2project.onlinequizapp2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

import semester2_2project.onlinequizapp2.Model.QuestionScore;
import semester2_2project.onlinequizapp2.common.Common;

public class Done extends AppCompatActivity {

    Button btnTryAgain;
    TextView txtResultScore, getTxtResultQuestion;
    ProgressBar progressBar;

    FirebaseDatabase database;
    DatabaseReference question_score;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_done);

        database = FirebaseDatabase.getInstance();
        question_score = database.getReference("Question_Score");

        txtResultScore = (TextView) findViewById(R.id.txtTotalScore);
        getTxtResultQuestion = findViewById(R.id.txtTotalQuestion);
        progressBar = findViewById(R.id.doneProgressBar);
        btnTryAgain = findViewById(R.id.btnTryAgain);

        btnTryAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(Done.this, Home.class);
//                startActivity(intent);
                finish();
            }
        });

        // get data from bundle and set to tview
        Bundle extra = getIntent().getExtras();
        if (extra != null) {
            int score = extra.getInt("SCORE");
            int totalQuestion = extra.getInt("TOTAL");
            int correctAnswerr = extra.getInt("CORRECT");

            txtResultScore.setText(String.format("SCORE : %d", score));
            getTxtResultQuestion.setText(String.format("PASSED: %d / %d", correctAnswerr, totalQuestion));

            progressBar.setMax(totalQuestion);
            progressBar.setProgress(correctAnswerr);

            // upload point to DB

            question_score.child(String.format("%s_%s", Common.currentUser.getUserName(), Common.categoryID))
                    .setValue(new QuestionScore(String.format("%s_%s", Common.currentUser.getUserName(),
                                Common.categoryID),
                            Common.currentUser.getUserName(),
                            String.valueOf(score)));
        }
    }
}

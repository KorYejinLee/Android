package com.iot.englishtestproject;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import static com.iot.englishtestproject.DBOpenHelper.TABLE_NAME;
import static com.iot.englishtestproject.DBOpenHelper.ENGLISHWORD;
import static com.iot.englishtestproject.DBOpenHelper.ENGLISHTRANSATION;
import static com.iot.englishtestproject.DBOpenHelper.KOREATRANSATION;
import static com.iot.englishtestproject.DBOpenHelper.PARTOFSPEECH;
import static com.iot.englishtestproject.DBOpenHelper.PRONUNCIATION;

public class TestActivity extends AppCompatActivity {
    private DBOpenHelper openHelper;
    private WordItem currentWord;

    private TextView wordTextView;
    private EditText translationEditText;
    private Button nextButton;
    private Button submitButton;
    private Button englishToKoreanButton;
    private Button koreanToEnglishButton;
    private TextView scoreTextView;

    private int score = 0;
    private boolean isEnglishToKorean = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        openHelper = new DBOpenHelper(this, "Dictionary", null, 1);

        wordTextView = findViewById(R.id.englishWordTextView);
        translationEditText = findViewById(R.id.koreanTranslationEditText);
        nextButton = findViewById(R.id.nextButton);
        submitButton = findViewById(R.id.submitButton);
        englishToKoreanButton = findViewById(R.id.englishToKoreanButton);
        koreanToEnglishButton = findViewById(R.id.koreanToEnglishButton);
        scoreTextView = findViewById(R.id.scoreTextView);

        loadRandomWord();
        updateScoreDisplay();

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer();
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadRandomWord();
            }
        });

        englishToKoreanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isEnglishToKorean = true;
                loadRandomWord();
            }
        });

        koreanToEnglishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isEnglishToKorean = false;
                loadRandomWord();
            }
        });
    }

    private void loadRandomWord() {
        SQLiteDatabase db = null;
        Cursor cursor = null;
        try {
            db = openHelper.getReadableDatabase();
            cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " ORDER BY RANDOM() LIMIT 1", null);

            int colIndexEnglishWord = cursor.getColumnIndex(ENGLISHWORD);
            int colIndexEnglishTranslation = cursor.getColumnIndex(ENGLISHTRANSATION);
            int colIndexKoreanTranslation = cursor.getColumnIndex(KOREATRANSATION);
            int colIndexPronunciation = cursor.getColumnIndex(PRONUNCIATION);
            int colIndexPartOfSpeech = cursor.getColumnIndex(PARTOFSPEECH);

            if (cursor.moveToFirst()) {
                String englishWord = cursor.getString(colIndexEnglishWord);
                String englishTranslation = cursor.getString(colIndexEnglishTranslation);
                String koreanTranslation = cursor.getString(colIndexKoreanTranslation);
                String pronunciation = cursor.getString(colIndexPronunciation);
                String partOfSpeech = cursor.getString(colIndexPartOfSpeech);

                currentWord = new WordItem(englishWord, englishTranslation, koreanTranslation, pronunciation, partOfSpeech);

                if (isEnglishToKorean) {
                    wordTextView.setText(currentWord.getEnglishWord());
                    translationEditText.setHint("Enter Korean Translation");
                } else {
                    wordTextView.setText(currentWord.getKoreanTranslation());
                    translationEditText.setHint("Enter English Translation");
                }

                translationEditText.setText("");
            } else {
                Toast.makeText(this, "No words found in the database", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Log.e("TestActivity", "Error loading random word", e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (db != null) {
                db.close();
            }
        }
    }

    private void checkAnswer() {
        String userAnswer = translationEditText.getText().toString().trim();
        String correctAnswer = isEnglishToKorean ? currentWord.getKoreanTranslation() : currentWord.getEnglishWord();

        if (userAnswer.equals(correctAnswer)) {
            score++;
            Toast.makeText(this, "Correct!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Wrong. The correct answer is " + correctAnswer, Toast.LENGTH_SHORT).show();
        }
        updateScoreDisplay();
    }

    private void updateScoreDisplay() {
        scoreTextView.setText("Score: " + score);
    }
}

package com.iot.englishtestproject;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import static com.iot.englishtestproject.DBOpenHelper.TABLE_NAME;
import static com.iot.englishtestproject.DBOpenHelper.ENGLISHWORD;
import static com.iot.englishtestproject.DBOpenHelper.ENGLISHTRANSATION;
import static com.iot.englishtestproject.DBOpenHelper.KOREATRANSATION;
import static com.iot.englishtestproject.DBOpenHelper.PARTOFSPEECH;
import static com.iot.englishtestproject.DBOpenHelper.PRONUNCIATION;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class RegistActivity extends AppCompatActivity {
    private DBOpenHelper openHelper;
    private ArrayList<WordItem> words;
    private Button addButton;
    private Button deleteButton;
    private WordAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist);

        words = new ArrayList<>();
        adapter = new WordAdapter(this, words);
        final ListView listview = (ListView) findViewById(R.id.listViewWords) ;
        listview.setAdapter(adapter);
        addButton = findViewById(R.id.addButton);
        openHelper = new DBOpenHelper(this,"Dictionary",null,1);

        loadWords();

        // array 추가
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAddWordDialog();
            }
        });

        // array 삭제
        deleteButton = (Button) findViewById(R.id.deleteButton);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteSelectedWords();
            }
        });
    }

    private void loadWords() {
        if (openHelper == null) {
            Log.e("loadWords", "DatabaseOpenHelper is null. Cannot perform database operations.");
            // Optionally, show a user-friendly message or take corrective action
            return; // Exit the method to avoid further operations
        }

        SQLiteDatabase db = null;
        Cursor cursor = null;
        try {
            db = openHelper.getReadableDatabase();
            cursor = db.query(TABLE_NAME, null, null, null, null, null, null);

            words.clear();
            if (cursor.moveToFirst()) {
                do {
                    int colIndexEnglishWord = cursor.getColumnIndex(ENGLISHWORD);
                    int colIndexEnglishTranslation = cursor.getColumnIndex(ENGLISHTRANSATION);
                    int colIndexKoreanTranslation = cursor.getColumnIndex(KOREATRANSATION);
                    int colIndexPronunciation = cursor.getColumnIndex(PRONUNCIATION);
                    int colIndexPartOfSpeech = cursor.getColumnIndex(PARTOFSPEECH);

                    if (colIndexEnglishWord != -1 && colIndexEnglishTranslation != -1 && colIndexKoreanTranslation != -1 &&
                            colIndexPronunciation != -1 && colIndexPartOfSpeech != -1) {
                        String englishWord = cursor.getString(colIndexEnglishWord);
                        String englishTranslation = cursor.getString(colIndexEnglishTranslation);
                        String koreanTranslation = cursor.getString(colIndexKoreanTranslation);
                        String pronunciation = cursor.getString(colIndexPronunciation);
                        String partOfSpeech = cursor.getString(colIndexPartOfSpeech);

                        words.add(new WordItem(englishWord, englishTranslation, koreanTranslation, pronunciation, partOfSpeech));
                        Log.i("loadWords", "Inserted data in list.");
                    } else {
                        Log.e("loadWords", "Column not found in cursor.");
                    }
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.e("loadWords", "Error loading words", e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (db != null) {
                db.close();
            }
            adapter.notifyDataSetChanged();
        }
    }

    private void showAddWordDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("새로운 단어를 입력해주세요");

        // Inflate and set the layout for the dialog
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_add_word, null);
        builder.setView(dialogView);

        final EditText editTextEnglishWord = dialogView.findViewById(R.id.editTextEnglishWord);
        final EditText editTextEnglishTranslation = dialogView.findViewById(R.id.editTextEnglishTranslation);
        final EditText editTextKoreanTranslation = dialogView.findViewById(R.id.editTextKoreanTranslation);
        final EditText editTextPronunciation = dialogView.findViewById(R.id.editTextPronunciation);
        final EditText editTextPartOfSpeech = dialogView.findViewById(R.id.editTextPartOfSpeech);

        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                addWord(editTextEnglishWord.getText().toString().trim(),
                        editTextEnglishTranslation.getText().toString().trim(),
                        editTextKoreanTranslation.getText().toString().trim(),
                        editTextPronunciation.getText().toString().trim(),
                        editTextPartOfSpeech.getText().toString().trim());
                Log.i("action","save buttion click");
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                Log.i("action","cancel buttion click");
            }
        });
        builder.show();
    }

    private void addWord(String englishWord, String englishTranslation, String koreanTranslation, String pronunciation, String partOfSpeech) {
        if (englishWord.isEmpty() || englishTranslation.isEmpty() || koreanTranslation.isEmpty() ||
                pronunciation.isEmpty() || partOfSpeech.isEmpty()) {
            Toast.makeText(this, "빈칸을 모두 채워주세요", Toast.LENGTH_SHORT).show();
            return;
        }

        SQLiteDatabase db = openHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ENGLISHWORD, englishWord);
        values.put(ENGLISHTRANSATION, englishTranslation);
        values.put(KOREATRANSATION, koreanTranslation);
        values.put(PRONUNCIATION, pronunciation);
        values.put(PARTOFSPEECH, partOfSpeech);

        db.insert(TABLE_NAME, null, values);
        Log.i("db","insert to the database");
        db.close();

        words.add(new WordItem(englishWord, englishTranslation, koreanTranslation, pronunciation, partOfSpeech));
        adapter.notifyDataSetChanged();
    }

    public void deleteSelectedWords() {
        final ArrayList<WordItem> selectedWords = adapter.getSelecctedItems();

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("단어 삭제");
        builder.setMessage("단어를 삭제하시겠습니까?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                SQLiteDatabase db = openHelper.getWritableDatabase();

                db.beginTransaction();
                try {
                    for (WordItem wordItem : selectedWords) {
                        String whereClause = ENGLISHWORD + "=?";
                        String[] whereArgs = new String[]{wordItem.getEnglishWord()};

                        int deletedRows = db.delete(TABLE_NAME, whereClause, whereArgs);
                        if (deletedRows > 0) {
                            words.remove(wordItem);
                        } else {
                            Toast.makeText(RegistActivity.this, "Failed to delete some words", Toast.LENGTH_SHORT).show();
                        }
                    }
                    db.setTransactionSuccessful();
                    Toast.makeText(RegistActivity.this, "Words deleted", Toast.LENGTH_SHORT).show();
                } finally {
                    db.endTransaction();
                    db.close();
                }

                adapter.notifyDataSetChanged(); // Refresh the ListView
            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.show();
    }
}

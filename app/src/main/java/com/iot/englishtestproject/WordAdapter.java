package com.iot.englishtestproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.ArrayList;

public class WordAdapter extends ArrayAdapter<WordItem> {
  private ArrayList<WordItem> selectedItems = new ArrayList<>();

    public WordAdapter(Context context, ArrayList<WordItem> words) {
        super(context, 0, words);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_word, parent, false);
        }

        final WordItem wordItem = getItem(position);

        CheckBox checkBox = convertView.findViewById(R.id.checkBox);
        TextView textViewWord = convertView.findViewById(R.id.textViewWord);

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(wordItem.getEnglishWord()).append("-");
        stringBuilder.append(wordItem.getEnglishTranslation()).append("-");
        stringBuilder.append(wordItem.getPronunciation()).append("-");
        stringBuilder.append(wordItem.getKoreanTranslation()).append("-");
        stringBuilder.append(wordItem.getPartOfSpeech());
        textViewWord.setText(stringBuilder.toString());

        checkBox.setChecked(selectedItems.contains(wordItem));
        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(((CheckBox) view).isChecked()) {
                    if(!selectedItems.contains(wordItem)) {
                        selectedItems.add(wordItem);
                    }
                    else {
                        selectedItems.remove(wordItem);
                    }
                }
            }
        });

        return convertView;
    }

    public ArrayList<WordItem> getSelecctedItems() {
        return selectedItems;
    }
}

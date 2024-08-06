package com.iot.englishtestproject;

public class WordItem {
    private String englishWord;
    private String englishTranslation;
    private String koreanTranslation;
    private String pronunciation;
    private String partOfSpeech;

    public WordItem(String englishWord, String englishTranslation, String koreanTranslation, String pronunciation, String partOfSpeech) {
        this.englishWord = englishWord;
        this.englishTranslation = englishTranslation;
        this.koreanTranslation = koreanTranslation;
        this.pronunciation = pronunciation;
        this.partOfSpeech = partOfSpeech;
    }

    public String getEnglishWord() {
        return englishWord;
    }

    public void setEnglishWord(String englishWord) {
        this.englishWord = englishWord;
    }

    public String getEnglishTranslation() {
        return englishTranslation;
    }

    public void setEnglishTranslation(String englishTranslation) {
        this.englishTranslation = englishTranslation;
    }

    public String getKoreanTranslation() {
        return koreanTranslation;
    }

    public void setKoreanTranslation(String koreanTranslation) {
        this.koreanTranslation = koreanTranslation;
    }

    public String getPronunciation() {
        return pronunciation;
    }

    public void setPronunciation(String pronunciation) {
        this.pronunciation = pronunciation;
    }

    public String getPartOfSpeech() {
        return partOfSpeech;
    }

    public void setPartOfSpeech(String partOfSpeech) {
        this.partOfSpeech = partOfSpeech;
    }

    @Override
    public String toString() {
        return englishWord; // ListView에서 어떤 값을 표시할지 결정
    }
}

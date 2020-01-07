package com.anizzzz.redisautocomplete.dto;

public class AutocompleteData implements Comparable<AutocompleteData> {
    private String value;
    private long score;

    public AutocompleteData(String value, long score){
        this.value = value;
        this.score = score;
    }

    @Override
    public int compareTo(AutocompleteData autocompleteData) {
        return Long.compare(autocompleteData.getScore(), this.score);
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public long getScore() {
        return score;
    }

    public void setScore(long score) {
        this.score = score;
    }
}

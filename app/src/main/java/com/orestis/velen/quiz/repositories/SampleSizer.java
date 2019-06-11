package com.orestis.velen.quiz.repositories;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class SampleSizer {

    private List<String> list1;
    private List<String> list2;
    private int sampleSize;
    private int randomStart;

    public SampleSizer(String[] array1, String[] array2, int sampleSize) {
        list1 = new ArrayList<>(Arrays.asList(array1));
        list2 = new ArrayList<>(Arrays.asList(array2));
        this.sampleSize = sampleSize;
        Random rand = new Random();
        int maxEnd = array1.length - sampleSize;
        randomStart = rand.nextInt(maxEnd + 1);
    }

    public List<String> getFirst() {
        return new ArrayList<>(list1.subList(randomStart, randomStart + sampleSize));
    }

    public List<String> getSecond() {
        return new ArrayList<>(list2.subList(randomStart, randomStart + sampleSize));
    }
}

package com.orestis.velen.quiz.geography;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GeographyItemsSampleSizer {

    private List<DataItem> list1;
    private List<DataItem> list2;
    private int sampleSize;
    private int randomStart;

    public GeographyItemsSampleSizer(List<DataItem> list1, List<DataItem> list2, int sampleSize) throws ListSizeMismatchException {
        this.list1 = list1;
        this.list2 = list2;
        this.sampleSize = sampleSize;
        Random rand = new Random();
        if(list1.size() == list2.size()) {
            int maxEnd = list1.size() - sampleSize;
            randomStart = rand.nextInt(maxEnd + 1);
        } else {
            throw new ListSizeMismatchException("Provided lists are of different size.");
        }
    }

    public GeographyItemsSampleSizer(List<DataItem> list, int sampleSize) {
        this.list1 = list;
        this.sampleSize = sampleSize;
        Random rand = new Random();
        int maxEnd = list1.size() - sampleSize;
        randomStart = rand.nextInt(maxEnd + 1);
    }

    public List<DataItem> getSingle() {
        return getFirst();
    }

    public List<DataItem> getFirst() {
        return new ArrayList<>(list1.subList(randomStart, randomStart + sampleSize));
    }

    public List<DataItem> getSecond() {
        return new ArrayList<>(list2.subList(randomStart, randomStart + sampleSize));
    }
}

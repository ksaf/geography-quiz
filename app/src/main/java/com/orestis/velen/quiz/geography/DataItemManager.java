package com.orestis.velen.quiz.geography;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.util.Pair;

import com.orestis.velen.quiz.questions.Difficulty;
import com.orestis.velen.quiz.questions.GameType;
import com.orestis.velen.quiz.questions.QuestionPoolData;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import static com.orestis.velen.quiz.geography.DataItem.AFRICA;
import static com.orestis.velen.quiz.geography.DataItem.ALL_CONTINENTS;
import static com.orestis.velen.quiz.geography.DataItem.AMERICA;
import static com.orestis.velen.quiz.geography.DataItem.AUSTRALIA;
import static com.orestis.velen.quiz.geography.DataItem.EAST_ASIA;
import static com.orestis.velen.quiz.geography.DataItem.EAST_ASIA_AND_OCEANIA;
import static com.orestis.velen.quiz.geography.DataItem.EUROPE;
import static com.orestis.velen.quiz.geography.DataItem.MESOPOTAMIA;
import static com.orestis.velen.quiz.geography.DataItem.NORTH_AMERICA;
import static com.orestis.velen.quiz.geography.DataItem.SOUTH_AMERICA;
import static com.orestis.velen.quiz.geography.DataItem.TYPE_CAPITAL;
import static com.orestis.velen.quiz.geography.DataItem.TYPE_COUNTRY;
import static com.orestis.velen.quiz.geography.DataItem.TYPE_MONUMENT;
import static com.orestis.velen.quiz.geography.DataItem.WEST_ASIA;
import static com.orestis.velen.quiz.questions.Difficulty.EASY;
import static com.orestis.velen.quiz.questions.Difficulty.HARD;
import static com.orestis.velen.quiz.questions.Difficulty.NORMAL;

public class DataItemManager {

    private List<DataItem> items = new ArrayList<>();
    private Map<Difficulty, Map<Difficulty, Integer>> difficultyMix = new HashMap<>();

    public DataItemManager() {
        setupDifficultyMix();
        setupItems();
        completeContinents();
    }

    public QuestionPoolData getSample(Difficulty difficulty, GameType gameType, int sampleSize) {
        List<DataItem> question = new ArrayList<>();
        List<DataItem> answer = new ArrayList<>();
        List<DataItem> allAnswers = new ArrayList<>();
        switch (gameType) {
            case TYPE_OUTLINES:
                question = answer = getRandomItems(TYPE_COUNTRY, difficulty, sampleSize);
                allAnswers = getAllItems(TYPE_COUNTRY, difficulty);
                break;
            case TYPE_FLAGS:
                question = answer = getRandomItems(TYPE_COUNTRY, difficulty, sampleSize);
                allAnswers = getAllItems(TYPE_COUNTRY, difficulty);
                break;
            case TYPE_CAPITALS_TEXT:
                question = getRandomItems(TYPE_COUNTRY, difficulty, sampleSize);
                answer = getCapitalsOfCountries(question);
                allAnswers = getAllItems(TYPE_CAPITAL, difficulty);
                break;
            case TYPE_CAPITALS_MAP:
                question = answer = getRandomItems(TYPE_CAPITAL, difficulty, sampleSize);
                allAnswers = getAllItems(TYPE_CAPITAL, difficulty);
                break;
            case TYPE_OUTLINE_TO_FLAG:
                question = answer = getRandomItems(TYPE_COUNTRY, difficulty, sampleSize);
                allAnswers = getAllItems(TYPE_COUNTRY, difficulty);
                break;
            case TYPE_MONUMENTS:
                question = answer = getRandomItems(TYPE_MONUMENT, difficulty, sampleSize);
                allAnswers = getAllItems(TYPE_MONUMENT, difficulty);
                break;

        }

        return new QuestionPoolData(question, answer, allAnswers);
    }

    public QuestionPoolData getSample(int continent, GameType gameType, int sampleSize) {
        List<DataItem> question = new ArrayList<>();
        List<DataItem> answer = new ArrayList<>();
        List<DataItem> allAnswers = new ArrayList<>();
        switch (gameType) {
            case TYPE_OUTLINES:
                question = answer = getRandomItems(TYPE_COUNTRY, continent, sampleSize);
                allAnswers = getAllItems(TYPE_COUNTRY, continent);
                break;
            case TYPE_FLAGS:
                question = answer = getRandomItems(TYPE_COUNTRY, continent, sampleSize);
                allAnswers = getAllItems(TYPE_COUNTRY, continent);
                break;
            case TYPE_CAPITALS_TEXT:
                question = getRandomItems(TYPE_COUNTRY, continent, sampleSize);
                answer = getCapitalsOfCountries(question);
                allAnswers = getAllItems(TYPE_CAPITAL, continent);
                break;
            case TYPE_CAPITALS_MAP:
                question = answer = getRandomItems(TYPE_CAPITAL, continent, sampleSize);
                allAnswers = getAllItems(TYPE_CAPITAL, continent);
                break;
            case TYPE_OUTLINE_TO_FLAG:
                question = answer = getRandomItems(TYPE_COUNTRY, continent, sampleSize);
                allAnswers = getAllItems(TYPE_COUNTRY, continent);
                break;
            case TYPE_MONUMENTS:
                question = answer = getRandomItems(TYPE_MONUMENT, continent, sampleSize);
                allAnswers = getAllItems(TYPE_MONUMENT, continent);
                break;

        }

        return new QuestionPoolData(question, answer, allAnswers);
    }

    private void addItem(String itemCode, @Nullable Pair<Float, Float> coordinates, int continent, Difficulty difficulty, int itemType) {
        items.add(new DataItem(itemCode, coordinates, continent, difficulty, itemType));
    }
    private void addItem(String itemCode, @Nullable Pair<Float, Float> coordinates, int continent, Difficulty difficulty, int itemType, String linkItemCode) {
        items.add(new DataItem(itemCode, coordinates, continent, difficulty, itemType, linkItemCode));
    }

    private void completeContinents() {
        Map<String, Integer> continentsMap = new HashMap<>();
        for (DataItem item : items) {
            if(item.getContinent() > 0 && item.getLinkItemCode() != null) {
                continentsMap.put(item.getLinkItemCode(), item.getContinent());
            }
        }
        for (DataItem item : items) {
            if(item.getContinent() < 0) {
                item.setContinent(continentsMap.get(item.getItemCode()));
            }
        }
    }

    private List<DataItem> getAllItems(int itemType, Difficulty difficulty) {
        List<DataItem> returnItems = new ArrayList<>();
        for(DataItem item : items) {
            if(item.getDifficulty() == difficulty && item.getItemType() == itemType) {
                returnItems.add(item);
            }
        }
        return returnItems;
    }

    private List<DataItem> getAllItems(int itemType, int continent) {
        List<DataItem> returnItems = new ArrayList<>();
        for(DataItem item : items) {
            if(isCorrectContinent(item, continent) && item.getItemType() == itemType) {
                returnItems.add(item);
            }
        }
        return returnItems;
    }

    private boolean isCorrectContinent(DataItem item, int continent) {
        boolean isCorrectContinent = false;
        switch (continent) {
            case EUROPE:
                isCorrectContinent = item.getContinent() == EUROPE;
                break;
            case AFRICA:
                isCorrectContinent = item.getContinent() == AFRICA;
                break;
            case EAST_ASIA_AND_OCEANIA:
                isCorrectContinent = item.getContinent() == EAST_ASIA || item.getContinent() == AUSTRALIA;
                break;
            case AMERICA:
                isCorrectContinent = item.getContinent() == NORTH_AMERICA || item.getContinent() == SOUTH_AMERICA;
                break;
            case MESOPOTAMIA:
                isCorrectContinent = item.getContinent() == EAST_ASIA;
                break;
            case ALL_CONTINENTS:
                isCorrectContinent = true;
                break;
        }
        return isCorrectContinent;
    }

    private List<DataItem> getAllItems(int itemType) {
        List<DataItem> returnItems = new ArrayList<>();
        for(DataItem item : items) {
            if(item.getItemType() == itemType) {
                returnItems.add(item);
            }
        }
        return returnItems;
    }

    private List<DataItem> getCapitalsOfCountries(List<DataItem> countries) {
        List<DataItem> capitals = new ArrayList<>();
        for(DataItem country : countries) {
            for(DataItem item : getAllItems(TYPE_CAPITAL)) {
                if(item.getLinkItemCode() != null && item.getLinkItemCode().equals(country.getItemCode())) {
                    capitals.add(item);
                    break;
                }
            }
        }
        return capitals;
    }

    private List<DataItem> getRandomItems(int type, int continent, int sampleSize) {
        List<DataItem> finalList = new ArrayList<>();
        List<DataItem> itemsForContinent = getAllItems(type, continent);
        List<Integer> continentIndexesSelected = new ArrayList<>();
        Random rand = new Random();
        for(int i = 0; i < sampleSize; i++) {
            int randomIndex = rand.nextInt(itemsForContinent.size());
            while (continentIndexesSelected.contains(randomIndex)) {
                randomIndex = rand.nextInt(itemsForContinent.size());
            }
            continentIndexesSelected.add(randomIndex);
            finalList.add(itemsForContinent.get(i));
        }
        return finalList;
    }

    private List<DataItem> getRandomItems(int type, Difficulty difficulty, int sampleSize) {
        List<DataItem> finalList = new ArrayList<>();
        List<Integer> easyIndexesSelected = new ArrayList<>();
        List<Integer> normalIndexesSelected = new ArrayList<>();
        List<Integer> hardIndexesSelected = new ArrayList<>();
        Random rand = new Random();
        int easySampleSize = sampleSize * difficultyMix.get(difficulty).get(EASY) / 100;
        List<DataItem> easyItemsForType = getAllItems(type, EASY);
        int normalSampleSize = sampleSize * difficultyMix.get(difficulty).get(NORMAL) / 100;
        List<DataItem> normalItemsForType = getAllItems(type, NORMAL);
        int hardSampleSize = sampleSize * difficultyMix.get(difficulty).get(HARD) / 100;
        List<DataItem> hardItemsForType = getAllItems(type, HARD);

        if(type == TYPE_MONUMENT) {
            easySampleSize = sampleSize;
            normalSampleSize = hardSampleSize = 0;
        }

        for(int e = 0; e < easySampleSize; e++) {
            int randomIndex = rand.nextInt(easyItemsForType.size());
            while (easyIndexesSelected.contains(randomIndex)) {
                randomIndex = rand.nextInt(easyItemsForType.size());
            }
            easyIndexesSelected.add(randomIndex);
            finalList.add(easyItemsForType.get(e));
        }

        for(int n = 0; n < normalSampleSize; n++) {
            int randomIndex = rand.nextInt(normalItemsForType.size());
            while (normalIndexesSelected.contains(randomIndex)) {
                randomIndex = rand.nextInt(normalItemsForType.size());
            }
            normalIndexesSelected.add(randomIndex);
            finalList.add(normalItemsForType.get(n));
        }

        for(int h = 0; h < hardSampleSize; h++) {
            int randomIndex = rand.nextInt(hardItemsForType.size());
            while (hardIndexesSelected.contains(randomIndex)) {
                randomIndex = rand.nextInt(hardItemsForType.size());
            }
            hardIndexesSelected.add(randomIndex);
            finalList.add(hardItemsForType.get(h));
        }
        Collections.shuffle(finalList);
        return finalList;
    }

    private void setupDifficultyMix() {
        Map<Difficulty, Integer> easyMix = new HashMap<>();
        easyMix.put(EASY, 80);
        easyMix.put(NORMAL, 20);
        easyMix.put(HARD, 0);
        difficultyMix.put(EASY, easyMix);
        Map<Difficulty, Integer> normalMix = new HashMap<>();
        normalMix.put(EASY, 20);
        normalMix.put(NORMAL, 70);
        normalMix.put(HARD, 10);
        difficultyMix.put(NORMAL, normalMix);
        Map<Difficulty, Integer> hardMix = new HashMap<>();
        hardMix.put(EASY, 0);
        hardMix.put(NORMAL, 20);
        hardMix.put(HARD, 80);
        difficultyMix.put(HARD, hardMix);
    }

    private void setupItems() {

        //EASY COUNTRIES

        addItem("china", null, -1, EASY, TYPE_COUNTRY);
        addItem("india", null, -1, EASY, TYPE_COUNTRY);
        addItem("united_states", null, -1, EASY, TYPE_COUNTRY);
        addItem("indonesia", null, -1, EASY, TYPE_COUNTRY);
        addItem("brazil", null, -1, EASY, TYPE_COUNTRY);
        addItem("pakistan", null, -1, EASY, TYPE_COUNTRY);
        addItem("nigeria", null, -1, EASY, TYPE_COUNTRY);
        addItem("bangladesh", null, -1, EASY, TYPE_COUNTRY);
        addItem("russia", null, -1, EASY, TYPE_COUNTRY);
        addItem("mexico", null, -1, EASY, TYPE_COUNTRY);
        addItem("japan", null, -1, EASY, TYPE_COUNTRY);
        addItem("ethiopia", null, -1, EASY, TYPE_COUNTRY);
        addItem("philippines", null, -1, EASY, TYPE_COUNTRY);
        addItem("vietnam", null, -1, EASY, TYPE_COUNTRY);
        addItem("egypt", null, -1, EASY, TYPE_COUNTRY);
        addItem("dr_congo", null, -1, EASY, TYPE_COUNTRY);
        addItem("iran", null, -1, EASY, TYPE_COUNTRY);
        addItem("germany", null, -1, EASY, TYPE_COUNTRY);
        addItem("turkey", null, -1, EASY, TYPE_COUNTRY);
        addItem("thailand", null, -1, EASY, TYPE_COUNTRY);
        addItem("united_kingdom", null, -1, EASY, TYPE_COUNTRY);
        addItem("france", null, -1, EASY, TYPE_COUNTRY);
        addItem("italy", null, -1, EASY, TYPE_COUNTRY);
        addItem("tanzania", null, -1, EASY, TYPE_COUNTRY);
        addItem("south_africa", null, -1, EASY, TYPE_COUNTRY);
        addItem("myanmar", null, -1, EASY, TYPE_COUNTRY);
        addItem("south_korea", null, -1, EASY, TYPE_COUNTRY);
        addItem("colombia", null, -1, EASY, TYPE_COUNTRY);
        addItem("kenya", null, -1, EASY, TYPE_COUNTRY);
        addItem("spain", null, -1, EASY, TYPE_COUNTRY);
        addItem("ukraine", null, -1, EASY, TYPE_COUNTRY);
        addItem("argentina", null, -1, EASY, TYPE_COUNTRY);
        addItem("sudan", null, -1, EASY, TYPE_COUNTRY);
        addItem("uganda", null, -1, EASY, TYPE_COUNTRY);
        addItem("algeria", null, -1, EASY, TYPE_COUNTRY);
        addItem("iraq", null, -1, EASY, TYPE_COUNTRY);
        addItem("poland", null, -1, EASY, TYPE_COUNTRY);
        addItem("canada", null, -1, EASY, TYPE_COUNTRY);
        addItem("morocco", null, -1, EASY, TYPE_COUNTRY);
        addItem("afghanistan", null, -1, EASY, TYPE_COUNTRY);
        addItem("saudi_arabia", null, -1, EASY, TYPE_COUNTRY);
        addItem("peru", null, -1, EASY, TYPE_COUNTRY);
        addItem("venezuela", null, -1, EASY, TYPE_COUNTRY);
        addItem("malaysia", null, -1, EASY, TYPE_COUNTRY);
        addItem("uzbekistan", null, -1, EASY, TYPE_COUNTRY);
        addItem("mozambique", null, -1, EASY, TYPE_COUNTRY);
        addItem("nepal", null, -1, EASY, TYPE_COUNTRY);
        addItem("ghana", null, -1, EASY, TYPE_COUNTRY);
        addItem("yemen", null, -1, EASY, TYPE_COUNTRY);
        addItem("angola", null, -1, EASY, TYPE_COUNTRY);
        addItem("madagascar", null, -1, EASY, TYPE_COUNTRY);
        addItem("north_korea", null, -1, EASY, TYPE_COUNTRY);
        addItem("australia", null, -1, EASY, TYPE_COUNTRY);
        addItem("cameroon", null, -1, EASY, TYPE_COUNTRY);
        addItem("ivory_coast", null, -1, EASY, TYPE_COUNTRY);
        addItem("niger", null, -1, EASY, TYPE_COUNTRY);
        addItem("sri_lanka", null, -1, EASY, TYPE_COUNTRY);
        addItem("romania", null, -1, EASY, TYPE_COUNTRY);
        addItem("burkina_faso", null, -1, EASY, TYPE_COUNTRY);
        addItem("syria", null, -1, EASY, TYPE_COUNTRY);

        //EASY CAPITALS

        addItem("beijing", new Pair<>(0.570267f, 0.309813f), EAST_ASIA, EASY, TYPE_CAPITAL, "china");
        addItem("new_delhi", new Pair<>(0.141841f, 0.461334f), EAST_ASIA, EASY, TYPE_CAPITAL, "india");
        addItem("washington", new Pair<>(0.743548f, 0.405367f), NORTH_AMERICA, EASY, TYPE_CAPITAL, "united_states");
        addItem("jakarta", new Pair<>(0.260545f, 0.425842f), AUSTRALIA, EASY, TYPE_CAPITAL, "indonesia");
        addItem("brasilia", new Pair<>(0.663048f, 0.424477f), SOUTH_AMERICA, EASY, TYPE_CAPITAL, "brazil");
        addItem("islamabad", new Pair<>(0.829506f, 0.383526f), WEST_ASIA, EASY, TYPE_CAPITAL, "pakistan");
        addItem("abuja", new Pair<>(0.358783f, 0.453143f), AFRICA, EASY, TYPE_CAPITAL, "nigeria");
        addItem("dhaka", new Pair<>(0.272825f, 0.522761f), EAST_ASIA, EASY, TYPE_CAPITAL, "bangladesh");
        addItem("moscow", new Pair<>(0.768108f, 0.393081f), EUROPE, EASY, TYPE_CAPITAL, "russia");
        addItem("mexico_city", new Pair<>(0.473394f, 0.685203f), NORTH_AMERICA, EASY, TYPE_CAPITAL, "mexico");
        addItem("tokyo", new Pair<>(0.824048f, 0.367145f), EAST_ASIA, EASY, TYPE_CAPITAL, "japan");
        addItem("addis_ababa", new Pair<>(0.676692f, 0.446318f), AFRICA, EASY, TYPE_CAPITAL, "ethiopia");
        addItem("maynila", new Pair<>(0.618022f, 0.637426f), EAST_ASIA, EASY, TYPE_CAPITAL, "philippines");
        addItem("hanoi", new Pair<>(0.448834f, 0.550062f), EAST_ASIA, EASY, TYPE_CAPITAL, "vietnam");
        addItem("cairo", new Pair<>(0.601649f, 0.210164f), AFRICA, EASY, TYPE_CAPITAL, "egypt");
        addItem("kinshasa", new Pair<>(0.444741f, 0.610125f), AFRICA, EASY, TYPE_CAPITAL, "dr_congo");
        addItem("tehran", new Pair<>(0.496589f, 0.339844f), WEST_ASIA, EASY, TYPE_CAPITAL, "iran");
        addItem("berlin", new Pair<>(0.431097f, 0.461334f), EUROPE, EASY, TYPE_CAPITAL, "germany");
        addItem("ankara", new Pair<>(0.699887f, 0.687933f), EUROPE, EASY, TYPE_CAPITAL, "turkey");
        addItem("bangkok", new Pair<>(0.190960f, 0.137816f), AUSTRALIA, EASY, TYPE_CAPITAL, "thailand");
        addItem("london", new Pair<>(0.248266f, 0.472254f), EUROPE, EASY, TYPE_CAPITAL, "united_kingdom");
        addItem("paris", new Pair<>(0.294656f, 0.521396f), EUROPE, EASY, TYPE_CAPITAL, "france");
        addItem("rome", new Pair<>(0.427004f, 0.646981f), EUROPE, EASY, TYPE_CAPITAL, "italy");
        addItem("dodoma", new Pair<>(0.650768f, 0.629235f), AFRICA, EASY, TYPE_CAPITAL, "tanzania");
        addItem("pretoria", new Pair<>(0.581183f, 0.843549f), AFRICA, EASY, TYPE_CAPITAL, "south_africa");
        addItem("naypyidaw", new Pair<>(0.342410f, 0.577363f), EAST_ASIA, EASY, TYPE_CAPITAL, "myanmar");
        addItem("seoul", new Pair<>(0.691700f, 0.346669f), EAST_ASIA, EASY, TYPE_CAPITAL, "south_korea");
        addItem("bogota", new Pair<>(0.368334f, 0.188323f), SOUTH_AMERICA, EASY, TYPE_CAPITAL, "colombia");
        addItem("nairobi", new Pair<>(0.665776f, 0.569173f), AFRICA, EASY, TYPE_CAPITAL, "kenya");
        addItem("madrid", new Pair<>(0.205969f, 0.672917f), EUROPE, EASY, TYPE_CAPITAL, "spain");
        addItem("kiev", new Pair<>(0.669870f, 0.507746f), EUROPE, EASY, TYPE_CAPITAL, "ukraine");
        addItem("buenos_aires", new Pair<>(0.542979f, 0.687933f), SOUTH_AMERICA, EASY, TYPE_CAPITAL, "argentina");
        addItem("khartoum", new Pair<>(0.613929f, 0.372605f), AFRICA, EASY, TYPE_CAPITAL, "sudan");
        addItem("kampala", new Pair<>(0.611200f, 0.540507f), AFRICA, EASY, TYPE_CAPITAL, "uganda");
        addItem("algiers", new Pair<>(0.294656f, 0.753455f), EUROPE, EASY, TYPE_CAPITAL, "algeria");
        addItem("baghdad", new Pair<>(0.395622f, 0.391716f), WEST_ASIA, EASY, TYPE_CAPITAL, "iraq");
        addItem("warsaw", new Pair<>(0.551166f, 0.468159f), EUROPE, EASY, TYPE_CAPITAL, "poland");
        addItem("ottawa", new Pair<>(0.754463f, 0.294797f), NORTH_AMERICA, EASY, TYPE_CAPITAL, "canada");
        addItem("rabat", new Pair<>(0.215520f, 0.162387f), AFRICA, EASY, TYPE_CAPITAL, "morocco");
        addItem("kabul", new Pair<>(0.766743f, 0.364415f), WEST_ASIA, EASY, TYPE_CAPITAL, "afghanistan");
        addItem("riyadh", new Pair<>(0.420182f, 0.517301f), WEST_ASIA, EASY, TYPE_CAPITAL, "saudi_arabia");
        addItem("lima", new Pair<>(0.341046f, 0.393081f), SOUTH_AMERICA, EASY, TYPE_CAPITAL, "peru");
        addItem("caracas", new Pair<>(0.451563f, 0.126896f), SOUTH_AMERICA, EASY, TYPE_CAPITAL, "venezuela");
        addItem("kuala_lumpur", new Pair<>(0.199147f, 0.296162f), AUSTRALIA, EASY, TYPE_CAPITAL, "malaysia");
        addItem("tashkent", new Pair<>(0.777658f, 0.242925f), WEST_ASIA, EASY, TYPE_CAPITAL, "uzbekistan");
        addItem("maputo", new Pair<>(0.615293f, 0.849009f), AFRICA, EASY, TYPE_CAPITAL, "mozambique");
        addItem("kathmandu", new Pair<>(0.230528f, 0.479079f), EAST_ASIA, EASY, TYPE_CAPITAL, "nepal");
        addItem("accra", new Pair<>(0.274189f, 0.487270f), AFRICA, EASY, TYPE_CAPITAL, "ghana");
        addItem("sana_a", new Pair<>(0.396987f, 0.713869f), WEST_ASIA, EASY, TYPE_CAPITAL, "yemen");
        addItem("luanda", new Pair<>(0.418817f, 0.656537f), AFRICA, EASY, TYPE_CAPITAL, "angola");
        addItem("antananarivo", new Pair<>(0.770836f, 0.753455f), AFRICA, EASY, TYPE_CAPITAL, "madagascar");
        addItem("pyongyang", new Pair<>(0.673963f, 0.326193f), EAST_ASIA, EASY, TYPE_CAPITAL, "north_korea");
        addItem("canberra", new Pair<>(0.806311f, 0.814883f), AUSTRALIA, EASY, TYPE_CAPITAL, "australia");
        addItem("yaounde", new Pair<>(0.401080f, 0.503650f), AFRICA, EASY, TYPE_CAPITAL, "cameroon");
        addItem("yamoussoukro", new Pair<>(0.226435f, 0.480445f), AFRICA, EASY, TYPE_CAPITAL, "ivory_coast");
        addItem("niamey", new Pair<>(0.300113f, 0.399906f), AFRICA, EASY, TYPE_CAPITAL, "niger");
        addItem("sri_jayawardenepura_kotte", new Pair<>(0.165036f, 0.728884f), EAST_ASIA, EASY, TYPE_CAPITAL, "sri_lanka");
        addItem("bucharest", new Pair<>(0.618022f, 0.595109f), EUROPE, EASY, TYPE_CAPITAL, "romania");
        addItem("ouagadougou", new Pair<>(0.268732f, 0.414922f), AFRICA, EASY, TYPE_CAPITAL, "burkina_faso");
        addItem("damascus", new Pair<>(0.266003f, 0.390351f), WEST_ASIA, EASY, TYPE_CAPITAL, "syria");

        //MEDIUM COUNTRIES

        addItem("mali", null, -1, NORMAL, TYPE_COUNTRY);
        addItem("chile", null, -1, NORMAL, TYPE_COUNTRY);
        addItem("malawi", null, -1, NORMAL, TYPE_COUNTRY);
        addItem("kazakhstan", null, -1, NORMAL, TYPE_COUNTRY);
        addItem("zambia", null, -1, NORMAL, TYPE_COUNTRY);
        addItem("netherlands", null, -1, NORMAL, TYPE_COUNTRY);
        addItem("guatemala", null, -1, NORMAL, TYPE_COUNTRY);
        addItem("ecuador", null, -1, NORMAL, TYPE_COUNTRY);
        addItem("zimbabwe", null, -1, NORMAL, TYPE_COUNTRY);
        addItem("cambodia", null, -1, NORMAL, TYPE_COUNTRY);
        addItem("senegal", null, -1, NORMAL, TYPE_COUNTRY);
        addItem("chad", null, -1, NORMAL, TYPE_COUNTRY);
        addItem("guinea", null, -1, NORMAL, TYPE_COUNTRY);
        addItem("south_sudan", null, -1, NORMAL, TYPE_COUNTRY);
        addItem("rwanda", null, -1, NORMAL, TYPE_COUNTRY);
        addItem("burundi", null, -1, NORMAL, TYPE_COUNTRY);
        addItem("tunisia", null, -1, NORMAL, TYPE_COUNTRY);
        addItem("benin", null, -1, NORMAL, TYPE_COUNTRY);
        addItem("belgium", null, -1, NORMAL, TYPE_COUNTRY);
        addItem("somalia", null, -1, NORMAL, TYPE_COUNTRY);
        addItem("cuba", null, -1, NORMAL, TYPE_COUNTRY);
        addItem("bolivia", null, -1, NORMAL, TYPE_COUNTRY);
        addItem("haiti", null, -1, NORMAL, TYPE_COUNTRY);
        addItem("greece", null, -1, NORMAL, TYPE_COUNTRY);
        addItem("dominican_republic", null, -1, NORMAL, TYPE_COUNTRY);
        addItem("czech_republic", null, -1, NORMAL, TYPE_COUNTRY);
        addItem("portugal", null, -1, NORMAL, TYPE_COUNTRY);
        addItem("azerbaijan", null, -1, NORMAL, TYPE_COUNTRY);
        addItem("sweden", null, -1, NORMAL, TYPE_COUNTRY);
        addItem("hungary", null, -1, NORMAL, TYPE_COUNTRY);
        addItem("belarus", null, -1, NORMAL, TYPE_COUNTRY);
        addItem("united_arab_emirates", null, -1, NORMAL, TYPE_COUNTRY);
        addItem("tajikistan", null, -1, NORMAL, TYPE_COUNTRY);
        addItem("serbia", null, -1, NORMAL, TYPE_COUNTRY);
        addItem("austria", null, -1, NORMAL, TYPE_COUNTRY);
        addItem("switzerland", null, -1, NORMAL, TYPE_COUNTRY);
        addItem("israel", null, -1, NORMAL, TYPE_COUNTRY);
        addItem("honduras", null, -1, NORMAL, TYPE_COUNTRY);
        addItem("papua_new_guinea", null, -1, NORMAL, TYPE_COUNTRY);
        addItem("jordan", null, -1, NORMAL, TYPE_COUNTRY);
        addItem("togo", null, -1, NORMAL, TYPE_COUNTRY);
        addItem("bulgaria", null, -1, NORMAL, TYPE_COUNTRY);
        addItem("laos", null, -1, NORMAL, TYPE_COUNTRY);
        addItem("paraguay", null, -1, NORMAL, TYPE_COUNTRY);
        addItem("sierra_leone", null, -1, NORMAL, TYPE_COUNTRY);
        addItem("libya", null, -1, NORMAL, TYPE_COUNTRY);
        addItem("nicaragua", null, -1, NORMAL, TYPE_COUNTRY);
        addItem("el_salvador", null, -1, NORMAL, TYPE_COUNTRY);
        addItem("kyrgyzstan", null, -1, NORMAL, TYPE_COUNTRY);
        addItem("lebanon", null, -1, NORMAL, TYPE_COUNTRY);
        addItem("singapore", null, -1, NORMAL, TYPE_COUNTRY);

        //MEDIUM CAPITALS

        addItem("bamako", new Pair<>(0.199636f, 0.401865f), AFRICA, NORMAL, TYPE_CAPITAL, "mali");
        addItem("santiago", new Pair<>(0.401906f, 0.657932f), SOUTH_AMERICA, NORMAL, TYPE_CAPITAL, "chile");
        addItem("lilongwe", new Pair<>(0.625317f, 0.706476f), AFRICA, NORMAL, TYPE_CAPITAL, "malawi");
        addItem("astana", new Pair<>(0.063645f, 0.147507f), EAST_ASIA, NORMAL, TYPE_CAPITAL, "kazakhstan");
        addItem("lusaka", new Pair<>(0.554694f, 0.717068f), AFRICA, NORMAL, TYPE_CAPITAL, "zambia");
        addItem("amsterdam", new Pair<>(0.320650f, 0.452173f), EUROPE, NORMAL, TYPE_CAPITAL, "netherlands");
        addItem("guatemala_city", new Pair<>(0.583821f, 0.747959f), NORTH_AMERICA, NORMAL, TYPE_CAPITAL, "guatemala");
        addItem("quito", new Pair<>(0.313576f, 0.242001f), SOUTH_AMERICA, NORMAL, TYPE_CAPITAL, "ecuador");
        addItem("harare", new Pair<>(0.585600f, 0.761253f), AFRICA, NORMAL, TYPE_CAPITAL, "zimbabwe");
        addItem("phnom_penh", new Pair<>(0.434590f, 0.664111f), EAST_ASIA, NORMAL, TYPE_CAPITAL, "cambodia");
        addItem("dakar", new Pair<>(0.101625f, 0.384157f), AFRICA, NORMAL, TYPE_CAPITAL, "senegal");
        addItem("n_djamena", new Pair<>(0.438976f, 0.412456f), AFRICA, NORMAL, TYPE_CAPITAL, "chad");
        addItem("conakry", new Pair<>(0.134309f, 0.437169f), AFRICA, NORMAL, TYPE_CAPITAL, "guinea");
        addItem("juba", new Pair<>(0.598839f, 0.487478f), AFRICA, NORMAL, TYPE_CAPITAL, "south_sudan");
        addItem("kigali", new Pair<>(0.585600f, 0.574912f), AFRICA, NORMAL, TYPE_CAPITAL, "rwanda");
        addItem("bujumbura", new Pair<>(0.585600f, 0.594329f), AFRICA, NORMAL, TYPE_CAPITAL, "burundi");
        addItem("tunis", new Pair<>(0.390404f, 0.739133f), EUROPE, NORMAL, TYPE_CAPITAL, "tunisia");
        addItem("porto_novo", new Pair<>(0.300337f, 0.476004f), AFRICA, NORMAL, TYPE_CAPITAL, "benin");
        addItem("brussels", new Pair<>(0.313576f, 0.484830f), EUROPE, NORMAL, TYPE_CAPITAL, "belgium");
        addItem("mogadishu", new Pair<>(0.736609f, 0.524603f), AFRICA, NORMAL, TYPE_CAPITAL, "somalia");
        addItem("havana", new Pair<>(0.686259f, 0.631399f), NORTH_AMERICA, NORMAL, TYPE_CAPITAL, "cuba");
        addItem("la_paz", new Pair<>(0.438976f, 0.445113f), SOUTH_AMERICA, NORMAL, TYPE_CAPITAL, "bolivia");
        addItem("port_au_prince", new Pair<>(0.804625f, 0.690534f), NORTH_AMERICA, NORMAL, TYPE_CAPITAL, "haiti");
        addItem("athens", new Pair<>(0.585600f, 0.717068f), EUROPE, NORMAL, TYPE_CAPITAL, "greece");
        addItem("santo_domingo", new Pair<>(0.835531f, 0.695002f), NORTH_AMERICA, NORMAL, TYPE_CAPITAL, "dominican_republic");
        addItem("prague", new Pair<>(0.458421f, 0.504248f), EUROPE, NORMAL, TYPE_CAPITAL, "czech_republic");
        addItem("lisbon", new Pair<>(0.125455f, 0.703828f), EUROPE, NORMAL, TYPE_CAPITAL, "portugal");
        addItem("baku", new Pair<>(0.467233f, 0.255240f), WEST_ASIA, NORMAL, TYPE_CAPITAL, "azerbaijan");
        addItem("stockholm", new Pair<>(0.493753f, 0.314431f), EUROPE, NORMAL, TYPE_CAPITAL, "sweden");
        addItem("budapest", new Pair<>(0.517583f, 0.550199f), EUROPE, NORMAL, TYPE_CAPITAL, "hungary");
        addItem("minsk", new Pair<>(0.627055f, 0.420344f), EUROPE, NORMAL, TYPE_CAPITAL, "belarus");
        addItem("abu_dhabi", new Pair<>(0.544062f, 0.549261f), WEST_ASIA, NORMAL, TYPE_CAPITAL, "united_arab_emirates");
        addItem("dushanbe", new Pair<>(0.770121f, 0.287015f), WEST_ASIA, NORMAL, TYPE_CAPITAL, "tajikistan");
        addItem("belgrade", new Pair<>(0.539635f, 0.602218f), EUROPE, NORMAL, TYPE_CAPITAL, "serbia");
        addItem("vienna", new Pair<>(0.467192f, 0.538669f), EUROPE, NORMAL, TYPE_CAPITAL, "austria");
        addItem("bern", new Pair<>(0.357679f, 0.556322f), EUROPE, NORMAL, TYPE_CAPITAL, "switzerland");
        addItem("jerusalem", new Pair<>(0.245518f, 0.411518f), WEST_ASIA, NORMAL, TYPE_CAPITAL, "israel");
        addItem("tegucigalpa", new Pair<>(0.625276f, 0.751434f), NORTH_AMERICA, NORMAL, TYPE_CAPITAL, "honduras");
        addItem("port_moresby", new Pair<>(0.776327f, 0.455649f), AUSTRALIA, NORMAL, TYPE_CAPITAL, "papua_new_guinea");
        addItem("amman", new Pair<>(0.261446f, 0.424757f), WEST_ASIA, NORMAL, TYPE_CAPITAL, "jordan");
        addItem("lome", new Pair<>(0.289704f, 0.477714f), AFRICA, NORMAL, TYPE_CAPITAL, "togo");
        addItem("sofia", new Pair<>(0.581131f, 0.634929f), EUROPE, NORMAL, TYPE_CAPITAL, "bulgaria");
        addItem("vientiane", new Pair<>(0.414235f, 0.588978f), EAST_ASIA, NORMAL, TYPE_CAPITAL, "laos");
        addItem("asuncion", new Pair<>(0.552874f, 0.551909f), SOUTH_AMERICA, NORMAL, TYPE_CAPITAL, "paraguay");
        addItem("freetown", new Pair<>(0.149286f, 0.461827f), AFRICA, NORMAL, TYPE_CAPITAL, "sierra_leone");
        addItem("tripoli", new Pair<>(0.427474f, 0.186342f), AFRICA, NORMAL, TYPE_CAPITAL, "libya");
        addItem("managua", new Pair<>(0.640294f, 0.784146f), NORTH_AMERICA, NORMAL, TYPE_CAPITAL, "nicaragua");
        addItem("san_salvador", new Pair<>(0.603224f, 0.766494f), NORTH_AMERICA, NORMAL, TYPE_CAPITAL, "el_salvador");
        addItem("bishkek", new Pair<>(0.863747f, 0.208407f), WEST_ASIA, NORMAL, TYPE_CAPITAL, "kyrgyzstan");
        addItem("beirut", new Pair<>(0.254372f, 0.377041f), WEST_ASIA, NORMAL, TYPE_CAPITAL, "lebanon");
        addItem("singapore_city", new Pair<>(0.232279f, 0.313493f), AUSTRALIA, NORMAL, TYPE_CAPITAL, "singapore");

        //HARD COUNTRIES

        addItem("denmark", null, -1, HARD, TYPE_COUNTRY);
        addItem("finland", null, -1, HARD, TYPE_COUNTRY);
        addItem("turkmenistan", null, -1, HARD, TYPE_COUNTRY);
        addItem("eritrea", null, -1, HARD, TYPE_COUNTRY);
        addItem("slovakia", null, -1, HARD, TYPE_COUNTRY);
        addItem("norway", null, -1, HARD, TYPE_COUNTRY);
        addItem("central_african_republic", null, -1, HARD, TYPE_COUNTRY);
        addItem("palestine", null, -1, HARD, TYPE_COUNTRY);
        addItem("costa_rica", null, -1, HARD, TYPE_COUNTRY);
        addItem("congo", null, -1, HARD, TYPE_COUNTRY);
        addItem("ireland", null, -1, HARD, TYPE_COUNTRY);
        addItem("oman", null, -1, HARD, TYPE_COUNTRY);
        addItem("liberia", null, -1, HARD, TYPE_COUNTRY);
        addItem("new_zealand", null, -1, HARD, TYPE_COUNTRY);
        addItem("mauritania", null, -1, HARD, TYPE_COUNTRY);
        addItem("croatia", null, -1, HARD, TYPE_COUNTRY);
        addItem("kuwait", null, -1, HARD, TYPE_COUNTRY);
        addItem("moldova", null, -1, HARD, TYPE_COUNTRY);
        addItem("panama", null, -1, HARD, TYPE_COUNTRY);
        addItem("georgia", null, -1, HARD, TYPE_COUNTRY);
        addItem("bosnia_herzegovina", null, -1, HARD, TYPE_COUNTRY);
        addItem("uruguay", null, -1, HARD, TYPE_COUNTRY);
        addItem("mongolia", null, -1, HARD, TYPE_COUNTRY);
        addItem("armenia", null, -1, HARD, TYPE_COUNTRY);
        addItem("albania", null, -1, HARD, TYPE_COUNTRY);
        addItem("lithuania", null, -1, HARD, TYPE_COUNTRY);
        addItem("jamaica", null, -1, HARD, TYPE_COUNTRY);
        addItem("namibia", null, -1, HARD, TYPE_COUNTRY);
        addItem("botswana", null, -1, HARD, TYPE_COUNTRY);
        addItem("qatar", null, -1, HARD, TYPE_COUNTRY);
        addItem("lesotho", null, -1, HARD, TYPE_COUNTRY);
        addItem("gambia", null, -1, HARD, TYPE_COUNTRY);
        addItem("slovenia", null, -1, HARD, TYPE_COUNTRY);
        addItem("latvia", null, -1, HARD, TYPE_COUNTRY);
        addItem("guinea_bissau", null, -1, HARD, TYPE_COUNTRY);
        addItem("gabon", null, -1, HARD, TYPE_COUNTRY);
        addItem("bahrain", null, -1, HARD, TYPE_COUNTRY);
        addItem("trinidad_and_tobago", null, -1, HARD, TYPE_COUNTRY);
        addItem("swaziland", null, -1, HARD, TYPE_COUNTRY);
        addItem("estonia", null, -1, HARD, TYPE_COUNTRY);
        addItem("mauritius", null, -1, HARD, TYPE_COUNTRY);
        addItem("cyprus", null, -1, HARD, TYPE_COUNTRY);
        addItem("djibouti", null, -1, HARD, TYPE_COUNTRY);
        addItem("fiji", null, -1, HARD, TYPE_COUNTRY);
        addItem("equatorial_guinea", null, -1, HARD, TYPE_COUNTRY);
        addItem("comoros", null, -1, HARD, TYPE_COUNTRY);
        addItem("bhutan", null, -1, HARD, TYPE_COUNTRY);
        addItem("guyana", null, -1, HARD, TYPE_COUNTRY);
        addItem("montenegro", null, -1, HARD, TYPE_COUNTRY);
        addItem("solomon_islands", null, -1, HARD, TYPE_COUNTRY);
        addItem("luxembourg", null, -1, HARD, TYPE_COUNTRY);

        //HARD CAPITALS

        addItem("copenhagen", new Pair<>(0.423047f, 0.392045f), EUROPE, HARD, TYPE_CAPITAL, "denmark");
        addItem("helsinki", new Pair<>(0.585558f, 0.302019f), EUROPE, HARD, TYPE_CAPITAL, "finland");
        addItem("ashgabat", new Pair<>(0.604962f, 0.293193f), WEST_ASIA, HARD, TYPE_CAPITAL, "turkmenistan");
        addItem("asmara", new Pair<>(0.300295f, 0.704656f), WEST_ASIA, HARD, TYPE_CAPITAL, "eritrea");
        addItem("bratislava", new Pair<>(0.493711f, 0.529843f), EUROPE, HARD, TYPE_CAPITAL, "slovakia");
        addItem("oslo", new Pair<>(0.397438f, 0.304667f), EUROPE, HARD, TYPE_CAPITAL, "norway");
        addItem("bangui", new Pair<>(0.462806f, 0.497131f), AFRICA, HARD, TYPE_CAPITAL, "central_african_republic");
        addItem("jerusalem_east", new Pair<>(0.249945f, 0.400927f), WEST_ASIA, HARD, TYPE_CAPITAL, "palestine");
        addItem("san_jose", new Pair<>(0.662387f, 0.816748f), NORTH_AMERICA, HARD, TYPE_CAPITAL, "costa_rica");
        addItem("brazzaville", new Pair<>(0.431901f, 0.597749f), AFRICA, HARD, TYPE_CAPITAL, "congo");
        addItem("dublin", new Pair<>(0.156319f, 0.439762f), EUROPE, HARD, TYPE_CAPITAL, "ireland");
        addItem("muscat", new Pair<>(0.607651f, 0.569561f), WEST_ASIA, HARD, TYPE_CAPITAL, "oman");
        addItem("monrovia", new Pair<>(0.168689f, 0.479479f), AFRICA, HARD, TYPE_CAPITAL, "liberia");
        addItem("wellington", new Pair<>(0.900858f, 0.904237f), AUSTRALIA, HARD, TYPE_CAPITAL, "new_zealand");
        addItem("nouakchott", new Pair<>(0.112175f, 0.343502f), AFRICA, HARD, TYPE_CAPITAL, "mauritania");
        addItem("zagreb", new Pair<>(0.477824f, 0.580152f), EUROPE, HARD, TYPE_CAPITAL, "croatia");
        addItem("kuwait_city", new Pair<>(0.445140f, 0.461827f), WEST_ASIA, HARD, TYPE_CAPITAL, "kuwait");
        addItem("chisinau", new Pair<>(0.647369f, 0.556322f), EUROPE, HARD, TYPE_CAPITAL, "moldova");
        addItem("panama_city", new Pair<>(0.721550f, 0.830042f), NORTH_AMERICA, HARD, TYPE_CAPITAL, "panama");
        addItem("tbilisi", new Pair<>(0.394790f, 0.223411f), WEST_ASIA, HARD, TYPE_CAPITAL, "georgia");
        addItem("sarajevo", new Pair<>(0.502524f, 0.612809f), EUROPE, HARD, TYPE_CAPITAL, "bosnia_herzegovina");
        addItem("montevideo", new Pair<>(0.567892f, 0.676412f), SOUTH_AMERICA, HARD, TYPE_CAPITAL, "uruguay");
        addItem("ulaanbaatar", new Pair<>(0.456600f, 0.195168f), EAST_ASIA, HARD, TYPE_CAPITAL, "mongolia");
        addItem("yerevan", new Pair<>(0.393011f, 0.251655f), WEST_ASIA, HARD, TYPE_CAPITAL, "armenia");
        addItem("tirana", new Pair<>(0.526396f, 0.661408f), EUROPE, HARD, TYPE_CAPITAL, "albania");
        addItem("vilnius", new Pair<>(0.594371f, 0.409753f), EUROPE, HARD, TYPE_CAPITAL, "lithuania");
        addItem("kingston", new Pair<>(0.748069f, 0.702891f), NORTH_AMERICA, HARD, TYPE_CAPITAL, "jamaica");
        addItem("windhoek", new Pair<>(0.452215f, 0.814155f), AFRICA, HARD, TYPE_CAPITAL, "namibia");
        addItem("gaborone", new Pair<>(0.541414f, 0.831807f), AFRICA, HARD, TYPE_CAPITAL, "botswana");
        addItem("doha", new Pair<>(0.495449f, 0.534256f), WEST_ASIA, HARD, TYPE_CAPITAL, "qatar");
        addItem("maseru", new Pair<>(0.567892f, 0.893645f), AFRICA, HARD, TYPE_CAPITAL, "lesotho");
        addItem("banjul", new Pair<>(0.109527f, 0.409753f), AFRICA, HARD, TYPE_CAPITAL, "gambia");
        addItem("ljubljana", new Pair<>(0.452215f, 0.571326f), EUROPE, HARD, TYPE_CAPITAL, "slovenia");
        addItem("riga", new Pair<>(0.585558f, 0.365567f), EUROPE, HARD, TYPE_CAPITAL, "latvia");
        addItem("bissau", new Pair<>(0.121028f, 0.418579f), AFRICA, HARD, TYPE_CAPITAL, "guinea_bissau");
        addItem("libreville", new Pair<>(0.375345f, 0.556322f), AFRICA, HARD, TYPE_CAPITAL, "gabon");
        addItem("manama", new Pair<>(0.484858f, 0.519252f), WEST_ASIA, HARD, TYPE_CAPITAL, "bahrain");
        addItem("port_of_spain", new Pair<>(0.925557f, 0.797385f), NORTH_AMERICA, HARD, TYPE_CAPITAL, "trinidad_and_tobago");
        addItem("mbabane", new Pair<>(0.600576f, 0.858286f), AFRICA, HARD, TYPE_CAPITAL, "swaziland");
        addItem("tallinn", new Pair<>(0.600576f, 0.324084f), EUROPE, HARD, TYPE_CAPITAL, "estonia");
        addItem("port_louis", new Pair<>(0.835489f, 0.762081f), AFRICA, HARD, TYPE_CAPITAL, "mauritius");
        addItem("nicosia", new Pair<>(0.703883f, 0.772672f), EUROPE, HARD, TYPE_CAPITAL, "cyprus");
        addItem("djibouti_city", new Pair<>(0.727755f, 0.429170f), AFRICA, HARD, TYPE_CAPITAL, "djibouti");
        addItem("suva", new Pair<>(0.890225f, 0.431818f), AUSTRALIA, HARD, TYPE_CAPITAL, "fiji");
        addItem("oyala", new Pair<>(0.385978f, 0.536904f), AFRICA, HARD, TYPE_CAPITAL, "equatorial_guinea");
        addItem("moroni", new Pair<>(0.734789f, 0.707304f), AFRICA, HARD, TYPE_CAPITAL, "comoros");
        addItem("thimphu", new Pair<>(0.272038f, 0.475066f), EAST_ASIA, HARD, TYPE_CAPITAL, "bhutan");
        addItem("georgetown", new Pair<>(0.536987f, 0.159863f), SOUTH_AMERICA, HARD, TYPE_CAPITAL, "guyana");
        addItem("podgorica", new Pair<>(0.515763f, 0.637577f), EUROPE, HARD, TYPE_CAPITAL, "montenegro");
        addItem("honiara", new Pair<>(0.884061f, 0.422992f), AUSTRALIA, HARD, TYPE_CAPITAL, "solomon_islands");
        addItem("luxembourg_city", new Pair<>(0.335627f, 0.503310f), EUROPE, HARD, TYPE_CAPITAL, "luxembourg");

//        addItem("suriname", null, -1, HARD, TYPE_COUNTRY);
//        addItem("cabo_verde", null, -1, HARD, TYPE_COUNTRY);
//        addItem("brunei", null, -1, HARD, TYPE_COUNTRY);
//        addItem("malta", null, -1, HARD, TYPE_COUNTRY);
//        addItem("bahamas", null, -1, HARD, TYPE_COUNTRY);
//        addItem("maldives", null, -1, HARD, TYPE_COUNTRY);
//        addItem("belize", null, -1, HARD, TYPE_COUNTRY);
//        addItem("iceland", null, -1, HARD, TYPE_COUNTRY);
//        addItem("barbados", null, -1, HARD, TYPE_COUNTRY);
//        addItem("vanuatu", null, -1, HARD, TYPE_COUNTRY);
//        addItem("sao_tome_and_principe", null, -1, HARD, TYPE_COUNTRY);
//        addItem("samoa", null, -1, HARD, TYPE_COUNTRY);
//        addItem("saint_lucia", null, -1, HARD, TYPE_COUNTRY);
//        addItem("kiribati", null, -1, HARD, TYPE_COUNTRY);
//        addItem("saint_vincent_and_grenadines", null, -1, HARD, TYPE_COUNTRY);
//        addItem("grenada", null, -1, HARD, TYPE_COUNTRY);
//        addItem("tonga", null, -1, HARD, TYPE_COUNTRY);
//        addItem("micronesia", null, -1, HARD, TYPE_COUNTRY);
//        addItem("seychelles", null, -1, HARD, TYPE_COUNTRY);
//        addItem("antigua_and_barbuda", null, -1, HARD, TYPE_COUNTRY);
//        addItem("dominica", null, -1, HARD, TYPE_COUNTRY);
//        addItem("andorra", null, -1, HARD, TYPE_COUNTRY);
//        addItem("saint_kitts_and_nevis", null, -1, HARD, TYPE_COUNTRY);
//        addItem("marshall_islands", null, -1, HARD, TYPE_COUNTRY);
//        addItem("liechtenstein", null, -1, HARD, TYPE_COUNTRY);
//        addItem("monaco", null, -1, HARD, TYPE_COUNTRY);
//        addItem("san_marino", null, -1, HARD, TYPE_COUNTRY);
//        addItem("palau", null, -1, HARD, TYPE_COUNTRY);
//        addItem("nauru", null, -1, HARD, TYPE_COUNTRY);
//        addItem("tuvalu", null, -1, HARD, TYPE_COUNTRY);
//        addItem("vatican", null, -1, HARD, TYPE_COUNTRY);

//        addItem("paramaribo", new Pair<>(0.572319f, 0.174868f), SOUTH_AMERICA, HARD, TYPE_CAPITAL);
//        addItem("praia", new Pair<>(0.063603f, 0.392045f), AFRICA, HARD, TYPE_CAPITAL);
//        addItem("bandar_seri_begawan", new Pair<>(0.362106f, 0.267542f), AUSTRALIA, HARD, TYPE_CAPITAL);
//        addItem("valletta", new Pair<>(0.449526f, 0.759433f), EUROPE, HARD, TYPE_CAPITAL);
//        addItem("nassau", new Pair<>(0.739216f, 0.617222f), NORTH_AMERICA, HARD, TYPE_CAPITAL);
//        addItem("male", new Pair<>(0.098935f, 0.762081f), EAST_ASIA, HARD, TYPE_CAPITAL);
//        addItem("belmopan", new Pair<>(0.607651f, 0.716130f), NORTH_AMERICA, HARD, TYPE_CAPITAL);
//        addItem("reykjavik", new Pair<>(0.004441f, 0.217233f), EUROPE, HARD, TYPE_CAPITAL);
//        addItem("bridgetown", new Pair<>(0.947650f, 0.770907f), NORTH_AMERICA, HARD, TYPE_CAPITAL);
//        addItem("port_vila", new Pair<>(0.872559f, 0.490071f), AUSTRALIA, HARD, TYPE_CAPITAL);
//        addItem("sao_tome", new Pair<>(0.366532f, 0.523665f), AFRICA, HARD, TYPE_CAPITAL);
//        addItem("apia", new Pair<>(0.995353f, 0.442410f), AUSTRALIA, HARD, TYPE_CAPITAL);
//        addItem("castries", new Pair<>(0.914097f, 0.735547f), NORTH_AMERICA, HARD, TYPE_CAPITAL);
//        addItem("tarawa", new Pair<>(0.921172f, 0.293193f), AUSTRALIA, HARD, TYPE_CAPITAL);
//        addItem("kingstown", new Pair<>(0.922909f, 0.751434f), NORTH_AMERICA, HARD, TYPE_CAPITAL);
//        addItem("saint_george_s", new Pair<>(0.607651f, 0.188107f), SOUTH_AMERICA, HARD, TYPE_CAPITAL);
//        addItem("nuku_alofa", new Pair<>(0.986499f, 0.541317f), AUSTRALIA, HARD, TYPE_CAPITAL);
//        addItem("palikir", new Pair<>(0.879634f, 0.256068f), AUSTRALIA, HARD, TYPE_CAPITAL);
//        addItem("victoria", new Pair<>(0.844302f, 0.611044f), AFRICA, HARD, TYPE_CAPITAL);
//        addItem("saint_john_s", new Pair<>(0.922909f, 0.724956f), NORTH_AMERICA, HARD, TYPE_CAPITAL);
//        addItem("roseau", new Pair<>(0.931763f, 0.735547f), NORTH_AMERICA, HARD, TYPE_CAPITAL);
        

        //MONUMENTS
        
        addItem("burj_al_arab_hotel", new Pair<>(0.559039f, 0.532491f), WEST_ASIA, EASY, TYPE_MONUMENT);
        addItem("mecca", new Pair<>(0.320609f, 0.603983f), WEST_ASIA, EASY, TYPE_MONUMENT);
        addItem("al_aqsa_mosque", new Pair<>(0.245518f, 0.409753f), WEST_ASIA, EASY, TYPE_MONUMENT);
        addItem("petra", new Pair<>(0.252593f, 0.437996f), WEST_ASIA, EASY, TYPE_MONUMENT);
        addItem("eiffel_tower", new Pair<>(0.272038f, 0.516604f), EUROPE, EASY, TYPE_MONUMENT);
        addItem("st_basil_catherdal", new Pair<>(0.745421f, 0.383164f), EUROPE, EASY, TYPE_MONUMENT);
        addItem("blue_domed_hurch_santorini", new Pair<>(0.600576f, 0.731134f), EUROPE, EASY, TYPE_MONUMENT);
        addItem("the_little_mermaid", new Pair<>(0.421268f, 0.384985f), EUROPE, EASY, TYPE_MONUMENT);
        addItem("neptune_and_the_palace_of_versailles", new Pair<>(0.279112f, 0.519252f), EUROPE, EASY, TYPE_MONUMENT);
        addItem("windmills_of_kinderdijk", new Pair<>(0.322388f, 0.461827f), EUROPE, EASY, TYPE_MONUMENT);
        addItem("big_ben", new Pair<>(0.241132f, 0.464475f), EUROPE, EASY, TYPE_MONUMENT);
        addItem("tower_of_pisa", new Pair<>(0.399217f, 0.617222f), EUROPE, EASY, TYPE_MONUMENT);
        addItem("lascaux", new Pair<>(0.267611f, 0.588978f), EUROPE, EASY, TYPE_MONUMENT);
        addItem("loch_ness", new Pair<>(0.193430f, 0.350563f), EUROPE, EASY, TYPE_MONUMENT);
        addItem("mont_st_michel", new Pair<>(0.234927f, 0.525430f), EUROPE, EASY, TYPE_MONUMENT);
        addItem("bran_castle", new Pair<>(0.598797f, 0.582800f), EUROPE, EASY, TYPE_MONUMENT);
        addItem("agia_sophia", new Pair<>(0.649148f, 0.661408f), EUROPE, EASY, TYPE_MONUMENT);
        addItem("brandenburg_gate", new Pair<>(0.431901f, 0.451236f), EUROPE, EASY, TYPE_MONUMENT);
        addItem("acropolis", new Pair<>(0.581131f, 0.716130f), EUROPE, EASY, TYPE_MONUMENT);
        addItem("sagrada_familia", new Pair<>(0.270259f, 0.656995f), EUROPE, EASY, TYPE_MONUMENT);
        addItem("neuschwanstein", new Pair<>(0.399217f, 0.541317f), EUROPE, EASY, TYPE_MONUMENT);
        addItem("stonehenge", new Pair<>(0.224335f, 0.477714f), EUROPE, EASY, TYPE_MONUMENT);
        addItem("manneken_pis", new Pair<>(0.313534f, 0.486540f), EUROPE, EASY, TYPE_MONUMENT);
        addItem("st_peter_cathedral", new Pair<>(0.431901f, 0.648169f), EUROPE, EASY, TYPE_MONUMENT);
        addItem("trevi_fountain", new Pair<>(0.425695f, 0.648169f), EUROPE, EASY, TYPE_MONUMENT);
        addItem("auschwitz", new Pair<>(0.526396f, 0.497131f), EUROPE, EASY, TYPE_MONUMENT);
        addItem("north_cape", new Pair<>(0.596150f, 0.083021f), EUROPE, EASY, TYPE_MONUMENT);
        addItem("the_great_sphynx_giza", new Pair<>(0.594371f, 0.214585f), AFRICA, EASY, TYPE_MONUMENT);
        addItem("giza_pyramids", new Pair<>(0.594371f, 0.212820f), AFRICA, EASY, TYPE_MONUMENT);
        addItem("victoria_falls", new Pair<>(0.545799f, 0.748786f), AFRICA, EASY, TYPE_MONUMENT);
        addItem("cape_of_good_hope", new Pair<>(0.468971f, 0.943954f), AFRICA, EASY, TYPE_MONUMENT);
        addItem("table_mountain", new Pair<>(0.471619f, 0.948367f), AFRICA, EASY, TYPE_MONUMENT);
        addItem("statue_of_liberty", new Pair<>(0.789566f, 0.363802f), NORTH_AMERICA, EASY, TYPE_MONUMENT);
        addItem("capitol_hill", new Pair<>(0.745421f, 0.402692f), NORTH_AMERICA, EASY, TYPE_MONUMENT);
        addItem("niagara_falls", new Pair<>(0.725976f, 0.328497f), NORTH_AMERICA, EASY, TYPE_MONUMENT);
        addItem("mount_rushmore", new Pair<>(0.452215f, 0.304667f), NORTH_AMERICA, EASY, TYPE_MONUMENT);
        addItem("grand_canyon", new Pair<>(0.309107f, 0.448588f), NORTH_AMERICA, EASY, TYPE_MONUMENT);
        addItem("chichen_itza", new Pair<>(0.604962f, 0.670234f), NORTH_AMERICA, EASY, TYPE_MONUMENT);
        addItem("inukshuk", new Pair<>(0.723329f, 0.319671f), NORTH_AMERICA, EASY, TYPE_MONUMENT);
        addItem("the_great_wall", new Pair<>(0.559039f, 0.300254f), EAST_ASIA, EASY, TYPE_MONUMENT);
        addItem("the_taj_mahal", new Pair<>(0.129841f, 0.482127f), EAST_ASIA, EASY, TYPE_MONUMENT);
        addItem("mount_fuji", new Pair<>(0.817823f, 0.363802f), EAST_ASIA, EASY, TYPE_MONUMENT);
        addItem("angkor_wat", new Pair<>(0.431901f, 0.652582f), EAST_ASIA, EASY, TYPE_MONUMENT);
        addItem("mount_everest", new Pair<>(0.236706f, 0.457414f), EAST_ASIA, EASY, TYPE_MONUMENT);
        addItem("the_great_buddha_of_kamakura", new Pair<>(0.824898f, 0.365567f), EAST_ASIA, EASY, TYPE_MONUMENT);
        addItem("machu_picchu", new Pair<>(0.385978f, 0.398279f), SOUTH_AMERICA, EASY, TYPE_MONUMENT);
        addItem("christ_the_redeemer", new Pair<>(0.690644f, 0.525430f), SOUTH_AMERICA, EASY, TYPE_MONUMENT);
        addItem("easter_island", new Pair<>(0.149286f, 0.648169f), SOUTH_AMERICA, EASY, TYPE_MONUMENT);
        addItem("nevado_mismi", new Pair<>(0.390363f, 0.422992f), SOUTH_AMERICA, EASY, TYPE_MONUMENT);
        addItem("uluru", new Pair<>(0.583779f, 0.685238f), AUSTRALIA, EASY, TYPE_MONUMENT);

    }

    public void sanityCheckAllStringsExist(Context context) {
        for(DataItem item : items) {
            item.getStringResource(context);
        }
    }

    public void sanityCheckAllImagesExist(Context context) {
        for(DataItem item : items) {
            if(item.getItemType() == TYPE_MONUMENT) {
                item.getItemDrawableId(context, false);
            }
            if(item.getItemType() == TYPE_CAPITAL) {
                item.getItemDrawableId(context, true);
            }
        }
    }

    public boolean sanityCheckAllItemsOfCountriesAndCapitalsOfSameTypeDifficultyAreEqualSize() {
        List<DataItem> easyCountries = getAllItems(TYPE_COUNTRY, EASY);
        List<DataItem> easyCapitals = getAllItems(TYPE_CAPITAL, EASY);
        List<DataItem> mediumCountries = getAllItems(TYPE_COUNTRY, NORMAL);
        List<DataItem> mediumCapitals = getAllItems(TYPE_CAPITAL, NORMAL);
        List<DataItem> hardCountries = getAllItems(TYPE_COUNTRY, HARD);
        List<DataItem> hardCapitals = getAllItems(TYPE_CAPITAL, HARD);
        return easyCapitals.size() == easyCountries.size() && mediumCapitals.size() == mediumCountries.size() && hardCapitals.size() == hardCountries.size();
    }

}

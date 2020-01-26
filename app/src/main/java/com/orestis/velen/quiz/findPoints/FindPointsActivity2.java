package com.orestis.velen.quiz.findPoints;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.orestis.velen.quiz.R;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class FindPointsActivity2 extends AppCompatActivity {
    private EditText cityInput;
    private ImageView map;
    private Drawable originalDrawable;
    private float percentageX;
    private float percentageY;
    private Map<String, String> currentOutputs = new HashMap<>();
    private Map<Integer, Pair<String, Integer>> maps = new HashMap<>();
    private int currentMap = 0;
    private Button saveBtn;
    private final static String EASY = "EASY";
    private final static String NORMAL = "NORMAL";
    private final static String HARD = "HARD";
    public static final String NORTH_AMERICA = "NORTH_AMERICA";
    public static final String SOUTH_AMERICA = "SOUTH_AMERICA";
    public static final String EUROPE = "EUROPE";
    public static final String EAST_ASIA = "EAST_ASIA";
    public static final String WEST_ASIA = "WEST_ASIA";
    public static final String AUSTRALIA = "AUSTRALIA";
    public static final String AFRICA = "AFRICA";
    public static final String ALL = "ALL";
    public static final String TYPE_CAPITAL = "TYPE_CAPITAL";
    public static final String TYPE_MONUMENT = "TYPE_MONUMENT";
    private String difficulty;
    private String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_points2);
        setupUI(findViewById(R.id.parent));

        saveBtn = findViewById(R.id.saveBtn);
        cityInput = findViewById(R.id.cityInput);
        map = findViewById(R.id.mapId);
        originalDrawable = map.getDrawable();

        map.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                map.onTouchEvent(motionEvent);
                saveBtn.setText("Save");
                saveBtn.setBackgroundResource(R.drawable.answer_button_default_legacy);
                drawTouchedSpot(motionEvent.getX(), motionEvent.getY(), R.drawable.map_pointer_red);
                return false;
            }
        });

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveBtn.setText("Saved!");
                saveBtn.setBackgroundResource(R.drawable.answer_button_correct_legacy);
                saveCityInfo();
            }
        });

        difficulty = EASY;
        type = TYPE_CAPITAL;

        initChangeMapButtons();
    }

    private void saveCityInfo() {
        String city = cityInput.getText().toString();
        String coordX = String.format(Locale.US,"%.6f", percentageX);
        String coordY = String.format(Locale.US,"%.6f", percentageY);

        String previouslySaved = currentOutputs.get(ALL);
        StringBuilder sb = new StringBuilder(previouslySaved);
        sb.append("\r\n").append("addItem(\"").append(city.toLowerCase()).append("\", ")
                .append("new Pair" + "<" + ">" + "(")
                .append(coordX.replace(",", ".")).append("f, ")
                .append(coordY.replace(",", ".")).append("f), ")
                .append(maps.get(currentMap).first).append(", ")
                .append(difficulty).append(", ")
                .append(type).append(");");


        currentOutputs.put(ALL, sb.toString());
        saveToMemory(ALL, currentOutputs.get(ALL));
        resetMap();
        cityInput.setText("");
    }

    private void saveToMemory(String name, String text) {
        SharedPreferences prefs = this.getSharedPreferences("com.example.app.points", Context.MODE_PRIVATE);
        prefs.edit().putString(name, text).apply();
    }

    private void drawTouchedSpot(float positionX, float positionY, int resourceId) {
        resetMap();
        Bitmap mapBitmap = ((BitmapDrawable)map.getDrawable()).getBitmap();
        int mapWidth = mapBitmap.getWidth();
        int mapHeight = mapBitmap.getHeight();
        int ivMapWidth = map.getWidth();
        int ivMapHeight = map.getHeight();
        float relativeX =  mapWidth / (float)ivMapWidth;
        float relativeY = mapHeight / (float)ivMapHeight;
        Bitmap resultBitmap = Bitmap.createBitmap(mapBitmap.getWidth(),mapBitmap.getHeight(), mapBitmap.getConfig());

        Canvas canvas = new Canvas(resultBitmap);

        Bitmap pointer = BitmapFactory.decodeResource(getResources(), resourceId);
        int pointerWidth = mapWidth / 12;
        int pointerHeight = (int)(pointerWidth * 1.41);
        canvas.drawBitmap(mapBitmap, new Matrix(), null);
        canvas.drawBitmap( Bitmap.createScaledBitmap(pointer, pointerWidth, pointerHeight, false),  positionX * relativeX - (pointerWidth / 2), positionY * relativeY - pointerHeight, null);

        map.setImageBitmap(resultBitmap);
        TextView x = findViewById(R.id.coordX);
        x.setText(String.format("X: %.2f", positionX));
        TextView y = findViewById(R.id.coordY);
        y.setText(String.format("Y: %.2f", positionY));
        TextView percX = findViewById(R.id.percX);
        percentageX = positionX / ivMapWidth;
        percX.setText(String.format("Percentage X: %.6f", percentageX));
        TextView percY = findViewById(R.id.percY);
        percentageY = positionY / ivMapHeight;
        percY.setText(String.format("Percentage Y: %.6f", percentageY));
    }

    private void resetMap() {
        map.setImageDrawable(originalDrawable);
    }

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        if(activity.getCurrentFocus()!= null) {
            inputMethodManager.hideSoftInputFromWindow(
                    activity.getCurrentFocus().getWindowToken(), 0);
        }
    }

    public void setupUI(View view) {

        // Set up touch listener for non-text box views to hide keyboard.
        if (!(view instanceof EditText)) {
            view.setOnTouchListener(new View.OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                    hideSoftKeyboard(FindPointsActivity2.this);
                    return false;
                }
            });
        }

        //If a layout container, iterate over children and seed recursion.
        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                View innerView = ((ViewGroup) view).getChildAt(i);
                setupUI(innerView);
            }
        }
    }

    private void initChangeMapButtons() {
        initMaps();
        Button rightBtn = findViewById(R.id.rightBtn);
        rightBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentMap++;
                if(currentMap > maps.size() - 1) {
                    currentMap = 0;
                }
                originalDrawable = getResources().getDrawable(maps.get(currentMap).second);
                resetMap();
            }
        });
        Button leftBtn = findViewById(R.id.leftBtn);
        leftBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentMap--;
                if(currentMap < 0) {
                    currentMap = maps.size() - 1;
                }
                originalDrawable = getResources().getDrawable(maps.get(currentMap).second);
                resetMap();
            }
        });
        final Button difficultyBtn = findViewById(R.id.difficultyBtn);
        difficultyBtn.setText(difficulty);
        difficultyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(difficulty.equals(EASY)) {
                    difficulty = NORMAL;
                } else if(difficulty.equals(NORMAL)) {
                    difficulty = HARD;
                } else if(difficulty.equals(HARD)) {
                    difficulty = EASY;
                }
                difficultyBtn.setText(difficulty);
            }
        });


        final Button typeButton = findViewById(R.id.typeButton);
        typeButton.setText(type);
        typeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(type.equals(TYPE_CAPITAL)) {
                    type = TYPE_MONUMENT;
                } else if(type.equals(TYPE_MONUMENT)) {
                    type = TYPE_CAPITAL;
                }
                typeButton.setText(type);
            }
        });
    }

    private void initMaps() {
        maps.put(0, new Pair<>(AFRICA, R.drawable.africa_map));
        currentOutputs.put(AFRICA, "");
        maps.put(1, new Pair<>(EUROPE, R.drawable.europe_map));
        currentOutputs.put(EUROPE, "");
        maps.put(2, new Pair<>(EAST_ASIA, R.drawable.east_asia_map));
        currentOutputs.put(EAST_ASIA, "");
        maps.put(3, new Pair<>(WEST_ASIA, R.drawable.west_asia_map));
        currentOutputs.put(WEST_ASIA, "");
        maps.put(4, new Pair<>(NORTH_AMERICA, R.drawable.north_america_map));
        currentOutputs.put(NORTH_AMERICA, "");
        maps.put(5, new Pair<>(SOUTH_AMERICA, R.drawable.south_america_map));
        currentOutputs.put(SOUTH_AMERICA, "");
        maps.put(6, new Pair<>(AUSTRALIA, R.drawable.australia_map));
        currentOutputs.put(AUSTRALIA, "");
        currentOutputs.put(ALL, "");
    }

    public static String sc(String originalUnprotectedString) {
        if (originalUnprotectedString == null) {
            return null;
        }
        boolean anyCharactersProtected = false;

        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < originalUnprotectedString.length(); i++) {
            char ch = originalUnprotectedString.charAt(i);

            boolean controlCharacter = ch < 32;
            boolean unicodeButNotAscii = ch > 126;
            boolean characterWithSpecialMeaningInXML = ch == '<' || ch == '&' || ch == '>';

            if (characterWithSpecialMeaningInXML || unicodeButNotAscii || controlCharacter) {
                stringBuffer.append("&#" + (int) ch + ";");
                anyCharactersProtected = true;
            } else {
                stringBuffer.append(ch);
            }
        }
        if (anyCharactersProtected == false) {
            return originalUnprotectedString;
        }

        return stringBuffer.toString();
    }
}

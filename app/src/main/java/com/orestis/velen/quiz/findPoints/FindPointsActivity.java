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
import java.util.Map;

public class FindPointsActivity extends AppCompatActivity {

    private EditText cityInput;
    private ImageView map;
    private Drawable originalDrawable;
    private float percentageX;
    private float percentageY;
    private Map<String, String> currentOutputs = new HashMap<>();
    private Map<Integer, Pair<String, Integer>> maps = new HashMap<>();
    private int currentMap = 0;
    private Button saveBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_points);
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

        initChangeMapButtons();
    }

    private void saveCityInfo() {
        String city = cityInput.getText().toString();
        String coordX = String.format("X:%.6f", percentageX);
        String coordY = String.format("Y: %.6f", percentageY);

        String previouslySaved = currentOutputs.get(maps.get(currentMap).first);
        StringBuilder sb = new StringBuilder(previouslySaved);
        sb.append(previouslySaved.split("-----------").length).append(") ").append(city).append(", ").append(coordX).append(", ").append(coordY).append("\r\n").append("-----------").append("\r\n");
        currentOutputs.put(maps.get(currentMap).first, sb.toString());
        saveToMemory(maps.get(currentMap).first, currentOutputs.get(maps.get(currentMap).first));
        resetMap();
        cityInput.setText("");
    }

    private void saveToMemory(String name, String text) {
        SharedPreferences prefs = this.getSharedPreferences("com.example.app", Context.MODE_PRIVATE);
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
        inputMethodManager.hideSoftInputFromWindow(
                activity.getCurrentFocus().getWindowToken(), 0);
    }

    public void setupUI(View view) {

        // Set up touch listener for non-text box views to hide keyboard.
        if (!(view instanceof EditText)) {
            view.setOnTouchListener(new View.OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                    hideSoftKeyboard(FindPointsActivity.this);
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
    }

    private void initMaps() {
        maps.put(0, new Pair<>("Africa", R.drawable.africa_map));
        currentOutputs.put("Africa", "");
        maps.put(1, new Pair<>("Europe", R.drawable.europe_map));
        currentOutputs.put("Europe", "");
        maps.put(2, new Pair<>("EastAsia", R.drawable.east_asia_map));
        currentOutputs.put("EastAsia", "");
        maps.put(3, new Pair<>("WestAsia", R.drawable.west_asia_map));
        currentOutputs.put("WestAsia", "");
        maps.put(4, new Pair<>("NorthAmerica", R.drawable.north_america_map));
        currentOutputs.put("NorthAmerica", "");
        maps.put(5, new Pair<>("SouthAmerica", R.drawable.south_america_map));
        currentOutputs.put("SouthAmerica", "");
        maps.put(6, new Pair<>("Australia", R.drawable.australia_map));
        currentOutputs.put("Australia", "");
    }
}

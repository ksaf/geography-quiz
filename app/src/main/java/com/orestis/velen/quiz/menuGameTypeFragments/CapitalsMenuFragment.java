package com.orestis.velen.quiz.menuGameTypeFragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.orestis.velen.quiz.R;
import com.orestis.velen.quiz.mainMenu.GameStartRequestListener;

import static com.orestis.velen.quiz.mainMenu.MainMenuActivity.VIEW_PAGER_SELECTION_CAPITALS;

public class CapitalsMenuFragment extends Fragment {

    private ViewPager viewPager;
    private GameStartRequestListener gameStartRequestListener;
    private Button menuImage;

    public void useInViewPager(ViewPager viewPager) {
        this.viewPager = viewPager;
    }

    public void setGameStartRequestListener(GameStartRequestListener gameStartRequestListener) {
        this.gameStartRequestListener = gameStartRequestListener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.capitals_menu_fragment, container, false);

        menuImage = rootView.findViewById(R.id.menuImage);
        menuImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gameStartRequestListener.onGameStartRequest(VIEW_PAGER_SELECTION_CAPITALS, getString(R.string.capitals));
            }
        });

        ImageView rightArrow = rootView.findViewById(R.id.rightArrow);
        rightArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewPager.arrowScroll(View.FOCUS_RIGHT);
            }
        });

        ImageView leftArrow = rootView.findViewById(R.id.leftArrow);
        leftArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewPager.arrowScroll(View.FOCUS_LEFT);
            }
        });

        return rootView;
    }
}

package xyz.rasp.laiquendi.sample.components;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.view.ViewCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import xyz.rasp.laiquendi.core.Component;
import xyz.rasp.laiquendi.core.ComponentCreateListener;
import xyz.rasp.laiquendi.core.SuperClass;

/**
 * Created by twiceYuan on 2017/3/23.
 * <p>
 * Mock Content
 */
@Component
@SuperClass(LinearLayout.class)
public class MockContent implements ComponentCreateListener<LinearLayout> {

    @Override
    public void onComponentCreate(LinearLayout rootView) {
        rootView.setOrientation(LinearLayout.VERTICAL);
        rootView.setPadding(32, 32, 32, 32);
        ViewCompat.setNestedScrollingEnabled(rootView, false);
        Context context = rootView.getContext();
        for (int i = 0; i < 10; i++) {
            View header = mockLine(context, 80, 20, 0);
            rootView.addView(header);
            for (int j = 0; j < Math.random() * 5 + 1; j++) {
                rootView.addView(mockLine(context, 0, 20, 0));
            }
            View footer = mockLine(context, 0, 40, (int) (Math.random() * 800));
            rootView.addView(footer);
        }
    }

    private View mockLine(Context context, int leftMargin, int bottomMargin, int rightMargin) {
        View view = new View(context);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 36);
        params.setMargins(leftMargin, 0, rightMargin, bottomMargin);
        view.setLayoutParams(params);
        view.setBackgroundColor(Color.parseColor("#DDDDDD"));
        return view;
    }
}

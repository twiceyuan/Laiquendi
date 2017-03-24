package xyz.rasp.laiquendi.sample.components;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import xyz.rasp.laiquendi.core.Component;
import xyz.rasp.laiquendi.core.Params;
import xyz.rasp.laiquendi.core.ParamsLoadListener;
import xyz.rasp.laiquendi.core.SuperClass;
import xyz.rasp.laiquendi.sample.R;

/**
 * Created by twiceYuan on 2017/3/21.
 * <p>
 * State Layout
 */
@SuppressWarnings("WeakerAccess")
@SuperClass(LinearLayout.class)
@Component(R.layout.component_state)
public class StateLayout implements ParamsLoadListener<LinearLayout> {

    @BindView(R.id.tv_message)   TextView     mTvMessage;
    @BindView(R.id.state_parent) LinearLayout mStateParent;
    @BindView(R.id.iv_empty)     ImageView    mEmptyImage;

    @Override
    public void onComponentCreate(LinearLayout rootView) {
        ButterKnife.bind(this, rootView);
        mStateParent.setVisibility(View.GONE);
    }

    public void showContent() {
        mStateParent.setVisibility(View.GONE);
    }

    public void showEmpty() {
        mStateParent.setVisibility(View.VISIBLE);
    }

    @Override
    public void onLoadParams(String params) {
        Params parsed = Params.parse(params);
        String status = parsed.getString("status", "");
        if (status.equals("empty")) {
            showEmpty();
        }
        if (status.equals("content")) {
            showContent();
        }
        String emptyString = parsed.getString("emptyString", "Empty");
        mTvMessage.setText(emptyString);
    }
}

package xyz.rasp.laiquendi.sample;

import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import xyz.rasp.laiquendi.core.Component;
import xyz.rasp.laiquendi.core.ComponentId;
import xyz.rasp.laiquendi.core.SuperClass;

/**
 * Created by twiceYuan on 2017/3/21.
 * <p>
 * State Layout
 */
@SuppressWarnings("WeakerAccess")
@SuperClass(LinearLayout.class)
@ComponentId(R.layout.component_state)
public class StateLayout implements Component {

    @BindView(R.id.tv_message)   TextView     mTvMessage;
    @BindView(R.id.state_parent) LinearLayout mStateParent;

    @Override
    public void initView(View rootView) {
        ButterKnife.bind(this, rootView);

        if (rootView instanceof LinearLayout) {
            ((LinearLayout) rootView).setGravity(Gravity.CENTER);
        }

        mStateParent.setVisibility(View.GONE);
    }

    public void showContent() {
        mStateParent.setVisibility(View.GONE);
    }

    public void showEmpty() {
        mStateParent.setVisibility(View.VISIBLE);
    }
}

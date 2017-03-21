package xyz.rasp.laiquendi.sample;

import android.view.View;
import android.widget.FrameLayout;
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
@SuperClass(FrameLayout.class)
@ComponentId(R.layout.component_state)
public class StateLayout implements Component {

    @BindView(R.id.tv_message) TextView mTvMessage;

    private Runnable showContent;
    private Runnable showEmpty;

    @Override
    public void initView(View rootView) {
        ButterKnife.bind(this, rootView);
        showContent = () -> rootView.setVisibility(View.GONE);
        showEmpty = () -> rootView.setVisibility(View.VISIBLE);
    }

    public void showContent() {
        showContent.run();
    }

    public void showEmpty() {
        showEmpty.run();
    }
}

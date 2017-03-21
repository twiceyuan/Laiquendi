package xyz.rasp.laiquendi.sample;

import android.app.Activity;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import xyz.rasp.laiquendi.core.Component;
import xyz.rasp.laiquendi.core.ComponentId;

/**
 * Created by twiceYuan on 2017/3/20.
 * <p>
 * he he
 */
@SuppressWarnings("WeakerAccess")
@ComponentId(R.layout.header)
public class Header implements Component {

    @BindView(R.id.tv_back)   TextView mTvBack;
    @BindView(R.id.tv_header) TextView mTvHeader;
    @BindView(R.id.tv_menu)   TextView mTvMenu;

    @Override
    public void initView(View rootView) {
        ButterKnife.bind(this, rootView);
    }

    public void attach(Activity activity) {
        mTvHeader.setText(activity.getTitle());
        mTvBack.setOnClickListener(v -> activity.onBackPressed());
        mTvMenu.setOnClickListener(v -> activity.openOptionsMenu());
    }

    public void setTitle(String title) {
        mTvHeader.setText(title);
    }
}

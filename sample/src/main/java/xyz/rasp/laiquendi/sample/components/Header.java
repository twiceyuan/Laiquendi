package xyz.rasp.laiquendi.sample.components;

import android.app.Activity;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import xyz.rasp.laiquendi.core.Component;
import xyz.rasp.laiquendi.core.ParamsLoadListener;
import xyz.rasp.laiquendi.core.Params;
import xyz.rasp.laiquendi.sample.R;

/**
 * Created by twiceYuan on 2017/3/20.
 * <p>
 * he he
 */
@SuppressWarnings("WeakerAccess")
@Component(R.layout.header)
public class Header implements ParamsLoadListener {

    @BindView(R.id.tv_back)          TextView mTvBack;
    @BindView(R.id.tv_header) public TextView mTvHeader;
    @BindView(R.id.tv_menu)          TextView mTvMenu;

    @Override
    public void onComponentCreate(View rootView) {
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

    @Override
    public void onLoadParams(String params) {
        Params parsed = Params.parse(params);
        mTvHeader.setText(parsed.getString("title"));
        mTvBack.setText(parsed.getString("back"));
        mTvMenu.setText(parsed.getString("menu"));
        mTvHeader.setTextSize(parsed.getInt("headSize", 14));
    }
}

package xyz.rasp.laiquendi.sample.components;

import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import xyz.rasp.laiquendi.core.Component;
import xyz.rasp.laiquendi.core.Params;
import xyz.rasp.laiquendi.core.ParamsLoadListener;
import xyz.rasp.laiquendi.core.SuperClass;
import xyz.rasp.laiquendi.sample.R;

/**
 * Created by twiceYuan on 2017/3/24.
 * <p>
 * 加载时的底部控件
 */
@SuperClass(LinearLayout.class)
@Component(R.layout.component_load_footer)
public class LoadFooter implements ParamsLoadListener<LinearLayout> {

    @BindView(R.id.pb_load)    ProgressBar mPbLoad;
    @BindView(R.id.tv_message) TextView    mTvMessage;

    private String mLoadingMessage  = "正在加载...";
    private String mLoadableMessage = "继续滑动加载更多";
    private String mLoadedMessage   = "没有更多数据了";

    @Override
    public void onComponentCreate(LinearLayout rootView) {
        ButterKnife.bind(this, rootView);

        mPbLoad.setVisibility(View.VISIBLE);
        mTvMessage.setText(mLoadingMessage);
    }

    public void loading() {
        mPbLoad.setVisibility(View.VISIBLE);
        mTvMessage.setText(mLoadingMessage);
    }

    public void loaded() {
        mPbLoad.setVisibility(View.GONE);
        mTvMessage.setText(mLoadedMessage);
    }

    public void loadable() {
        mPbLoad.setVisibility(View.GONE);
        mTvMessage.setText(mLoadableMessage);
    }

    @Override
    public void onLoadParams(String params) {
        Params parsed = Params.parse(params);
        String loadingMessage = parsed.getString("loadingMessage");
        if (!TextUtils.isEmpty(loadingMessage)) {
            mLoadedMessage = loadingMessage;
        }

        String loadedMessage = parsed.getString("loadedMessage");
        if (!TextUtils.isEmpty(loadingMessage)) {
            mLoadedMessage = loadedMessage;
        }

        String loadableMessage = parsed.getString("loadableMessage");
        if (!TextUtils.isEmpty(loadableMessage)) {
            mLoadableMessage = loadableMessage;
        }

        String status = parsed.getString("status");
        if ("loading".equals(status)) {
            loading();
        } else if ("loaded".equals(status)) {
            loaded();
        } else if ("loadable".equals(status)) {
            loadable();
        }
    }
}

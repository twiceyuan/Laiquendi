package xyz.rasp.laiquendi.sample;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.Menu;
import android.view.MenuItem;

import butterknife.BindView;
import butterknife.ButterKnife;
import xyz.rasp.laiquendi.sample.components.Header;
import xyz.rasp.laiquendi.sample.components.HeaderView;
import xyz.rasp.laiquendi.sample.components.StateLayout;
import xyz.rasp.laiquendi.sample.components.StateLayoutView;

public class MainActivity extends Activity {

    @BindView(R.id.refresh) SwipeRefreshLayout mRefreshLayout;

    private Header      header;
    private StateLayout state;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        // Inject
        header = HeaderView.get(this, R.id.header);
        state = StateLayoutView.get(this, R.id.state);

        header.attach(this);

        final int[] seed = {0};
        mRefreshLayout.setOnRefreshListener(() -> {
            mRefreshLayout.setRefreshing(false);
            seed[0]++;
            if (seed[0] % 2 == 0) {
                state.showContent();
            } else {
                state.showEmpty();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Mock Menu
        menu.add("Item1");
        menu.add("Item2");
        menu.add("Item3");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        header.setTitle((String) item.getTitle());
        return true;
    }
}

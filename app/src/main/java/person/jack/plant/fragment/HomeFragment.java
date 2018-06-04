package person.jack.plant.fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import person.jack.plant.ui.pulltorefresh.PullToRefreshBase;
import person.jack.plant.ui.pulltorefresh.PullToRefreshListFragment;
import person.jack.plant.ui.pulltorefresh.PullToRefreshListView;

import java.util.Arrays;
import java.util.LinkedList;

/**
 * 首页-植物
 */
public class HomeFragment extends PullToRefreshListFragment {

    private PullToRefreshListView mPullRefreshListView;

    private String[] mStrings = {"发财树", "绿萝", "虎皮兰", "金钱树", "波斯顿蕨",
            "平安树", "巴西木", "常春藤", "富贵竹", "吊兰", "三色铁", "文竹",
            "万年青", "滴水观音", "龟背竹", "豆瓣绿", "鸭脚木", "袖珍椰子",
            "散尾葵", "橡皮树", "铁线蕨", "变叶木", "冷水花", "皱叶冷水花", "彩叶草",
            "网纹草"};

    private LinkedList<String> mListItems;
    private ArrayAdapter<String> mAdapter;

    public static HomeFragment newInstance(String content) {
        HomeFragment fragment = new HomeFragment();
        return fragment;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Get PullToRefreshListView from Fragment
        mPullRefreshListView = getPullToRefreshListView();

        // Set a listener to be invoked when the list should be refreshed.
        mPullRefreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                // Do work to refresh the list here.
                new GetDataTask().execute();
            }
        });

        // You can also just use mPullRefreshListFragment.getListView()
        ListView actualListView = mPullRefreshListView.getRefreshableView();

        mListItems = new LinkedList<String>();
        mListItems.addAll(Arrays.asList(mStrings));
        mAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, mListItems);

        // You can also just use setListAdapter(mAdapter) or
        // mPullRefreshListView.setAdapter(mAdapter)
        actualListView.setAdapter(mAdapter);
        setListShown(true);

    }

    private class GetDataTask extends AsyncTask<Void, Void, String[]> {

        @Override
        protected String[] doInBackground(Void... params) {
            // Simulates a background job.
            try {
                Thread.sleep(4000);
            } catch (InterruptedException e) {
            }
            return mStrings;
        }

        @Override
        protected void onPostExecute(String[] result) {
            mListItems.addFirst("Added after refresh...");
            mAdapter.notifyDataSetChanged();

            // Call onRefreshComplete when the list has been refreshed.
            mPullRefreshListView.onRefreshComplete();

            super.onPostExecute(result);
        }
    }

}

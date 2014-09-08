package com.example.slidingvertical;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnScrollChangedListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.TextView;

import com.example.com.example.slidingvertical.R;

public class MainActivity extends ActionBarActivity {
	SlidingVerticalLayout mLayout;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mLayout=(SlidingVerticalLayout)findViewById(R.id.root);
		ViewPager mPager = (ViewPager) findViewById(R.id.list);
		mPager.setAdapter(new SamplePagerAdapter(getSupportFragmentManager()));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private class SamplePagerAdapter extends FragmentStatePagerAdapter {
		private static final int PAGE_CNT = 2;
		private String[] data = new String[] { "评论", "喜欢" };
		private long checkinId;

		public SamplePagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public int getCount() {
			return PAGE_CNT;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			return data[position];
		}

		@Override
		public Fragment getItem(int pos) {
			switch (pos) {
			case 0:
				return new CheckinCommentFragment();
			case 1:
				return new CheckinCommentFragment();
			}
			return null;
		}
	}

	class CheckinCommentFragment extends Fragment {

		private ListView mListView;
		private List<String> mList = new ArrayList<String>();

		@Override
		public View onCreateView(LayoutInflater inflater,
				@Nullable ViewGroup container,
				@Nullable Bundle savedInstanceState) {
			mListView = new ListView(MainActivity.this);
			mListView.setLayoutParams(new LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
			for (int i = 0; i < 10; i++) {
				mList.add(i + 1 + "");
			}
			mListView.setAdapter(new CheckinCommentAdapter(getActivity()));
			mListView.setOnScrollListener(new OnScrollListener() {
				
				@Override
				public void onScrollStateChanged(AbsListView view, int scrollState) {
					
				}
				
				@Override
				public void onScroll(AbsListView view, int firstVisibleItem,
						int visibleItemCount, int totalItemCount) {
					int pos=view.getFirstVisiblePosition();
					System.out.println(mListView.getChildCount());
					if (pos==0&&mListView.getChildCount()>0){
						int top = mListView.getChildAt(pos).getTop();
						if (top ==0){
							mLayout.setSlidingVertical(true);
						}
					}else{
						mLayout.setSlidingVertical(false);
					}
				}
			});
			return mListView;
		}

		class CheckinCommentAdapter extends BaseAdapter {

			private LayoutInflater mInflater;

			public CheckinCommentAdapter(Context context) {
				mInflater = LayoutInflater.from(context);
			}

			@Override
			public int getCount() {
				return 10;
			}

			@Override
			public Object getItem(int position) {
				// TODO Auto-generated method stub
				return mList.get(position);
			}

			@Override
			public long getItemId(int position) {
				// TODO Auto-generated method stub
				return 0;
			}

			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				convertView = mInflater.inflate(R.layout.item_checkin_comment,
						null);
				((TextView) convertView.findViewById(R.id.comment))
						.setText(mList.get(position));
				return convertView;
			}
		}

	}

}

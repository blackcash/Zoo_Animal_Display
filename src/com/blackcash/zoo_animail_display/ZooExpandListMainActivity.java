package com.blackcash.zoo_animail_display;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.ExpandableListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ProgressBar;
import android.widget.Toast;

public class ZooExpandListMainActivity extends Activity implements
		OnChildClickListener, OnScrollListener {

	private ExpandableListView expandableListView;
	private ProgressBar bar;
	private List<ZooItem> list;
	private boolean bState = false;
	private MyExpandAdapter mListAdapter;
	List<Map<String, String>> groups;
	List<List<Map<String, ZooItem>>> childs;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		setContentView(R.layout.activity_zoo_expand_list_main);
		findViews();
		init();
	}

	private void findViews() {
		expandableListView = (ExpandableListView) findViewById(R.id.expandableListView1);
		expandableListView.setOnChildClickListener(this);
		expandableListView.setOnScrollListener(this);
		bar = (ProgressBar) findViewById(R.id.progressBar1);
	}

	private void init() {
		list = new ArrayList<ZooItem>();
		expandableListView.setVisibility(View.GONE);
		bar.setVisibility(View.VISIBLE);
		new Task()
				.execute("http://data.taipei/opendata/datalist/apiAccess?scope=resourceAquire&rid=a3e2b221-75e0-45c1-8f97-75acbd43d613");
	}

	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			if (msg.what == 1) {
				expandableListView.setVisibility(View.VISIBLE);
				mListAdapter = new MyExpandAdapter(
						ZooExpandListMainActivity.this, groups, childs);
				expandableListView.setAdapter(mListAdapter);
				bar.setVisibility(View.GONE);
			} else if (msg.what == 2) {
				Toast.makeText(ZooExpandListMainActivity.this, "請確定網路!!",
						Toast.LENGTH_SHORT).show();
				bar.setVisibility(View.GONE);
			}
		}

	};

	class Task extends AsyncTask<String, Void, String> {

		@Override
		protected String doInBackground(String... params) {
			String result = null;
			try {
				HttpClient client = new DefaultHttpClient();
				HttpGet get = new HttpGet(params[0]);
				HttpResponse response = client.execute(get);
				HttpEntity resEntity = response.getEntity();
				result = EntityUtils.toString(resEntity);
			} catch (Exception e) {
				result = null;
			}
			return result;
		}

		@Override
		protected void onPostExecute(String result) {
			if (result != null) {
				try {
					JSONObject json = new JSONObject(result);
					JSONObject json_result = json.getJSONObject("result");
					JSONArray json_results = json_result
							.getJSONArray("results");
					for (int i = 0; i < json_results.length(); i++) {
						ZooItem item = new ZooItem();
						JSONObject j = (JSONObject) json_results.get(i);
						item.setA_Name_Ch(j.getString("A_Name_Ch"));
						item.setA_Name_En(j.getString("A_Name_En"));
						item.setA_Location(j.getString("A_Location"));
						item.setA_Phylum(j.getString("A_Phylum"));
						item.setA_Class(j.getString("A_Class"));
						item.setA_Order(j.getString("A_Order"));
						item.setA_Family(j.getString("A_Family"));
						item.setA_Feature(j.getString("A_Feature"));
						item.setA_Habitat(j.getString("A_Habitat"));
						item.setA_Diet(j.getString("A_Diet"));
						item.setA_Interpretation(j
								.getString("A_Interpretation"));
						item.setA_Distribution(j.getString("A_Distribution"));
						item.setA_Pic01_ALT(j.getString("A_Pic01_ALT"));
						item.setA_Pic01_URL(j.getString("A_Pic01_URL"));
						item.setA_Pic02_ALT(j.getString("A_Pic02_ALT"));
						item.setA_Pic02_URL(j.getString("A_Pic02_URL"));
						item.setA_Pic03_ALT(j.getString("A_Pic03_ALT"));
						item.setA_Pic03_URL(j.getString("A_Pic03_URL"));
						item.setA_Pic04_ALT(j.getString("A_Pic04_ALT"));
						item.setA_Pic04_URL(j.getString("A_Pic04_URL"));
						list.add(item);
					}
					parserData();
					mHandler.sendEmptyMessage(1);
				} catch (JSONException e) {
					mHandler.sendEmptyMessage(2);
				}
			} else {
				mHandler.sendEmptyMessage(2);
			}
		}

		private void parserData() {
			groups = new ArrayList<Map<String, String>>();
			childs = new ArrayList<List<Map<String, ZooItem>>>();

			for (int i = 0; i < list.size(); i++) {
				String location = list.get(i).getA_Location();
				boolean isCheck = false;
				if (groups.size() == 0) {
					Map<String, String> map = new HashMap<String, String>();
					map.put("group", location);
					groups.add(map);
					childs.add(new ArrayList<Map<String, ZooItem>>());
					Log.d("group", location);
				} else {
					for (int j = 0; j < groups.size(); j++) {
						String text = groups.get(j).get("group");
						Log.d("groups", text);
						if (text.trim().equals(location.trim())) {
							isCheck = true;
							break;
						}
					}
					if (!isCheck) {
						Map<String, String> gmap = new HashMap<String, String>();
						gmap.put("group", location);
						groups.add(gmap);
						childs.add(new ArrayList<Map<String, ZooItem>>());
						Log.d("group", location);
					}
				}
				for (int z = 0; z < groups.size(); z++) {
					String text = groups.get(z).get("group");
					Log.d("childs", text);
					if (text.trim().equals(location.trim())) {
						Map<String, ZooItem> cmap = new HashMap<String, ZooItem>();
						cmap.put("child", list.get(i));
						Log.d("childs", z + "/" + childs.size());
						childs.get(z).add(cmap);
						break;
					}
				}
			}

		}

	}

	@Override
	public boolean onChildClick(ExpandableListView parent, View v,
			int groupPosition, int childPosition, long id) {
		Log.d("click", groupPosition + "/" + childPosition);
		ZooItem item = childs.get(groupPosition).get(childPosition)
				.get("child");
		Intent intent = new Intent(this, ViewPagerActivity.class);
		intent.putExtra("item", item);
		startActivity(intent);
		return false;
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		if (view.getId() == R.id.expandableListView1) {
			// Set scrolling to true only if the user has flinged the
			// ListView away, hence we skip downloading a series
			// of unnecessary bitmaps that the user probably
			// just want to skip anyways. If we scroll slowly it
			// will still download bitmaps - that means
			// that the application won't wait for the user
			// to lift its finger off the screen in order to
			// download.
			if (scrollState == SCROLL_STATE_FLING) {
				bState = true;
			} else {
				bState = false;				
			}
			mListAdapter.setState(bState);
			Log.d("======", bState + "");
		}
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		Log.d("=onScroll", visibleItemCount + "/" + totalItemCount);
	}
}

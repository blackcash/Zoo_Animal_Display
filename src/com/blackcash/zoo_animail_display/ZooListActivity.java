package com.blackcash.zoo_animail_display;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.blackcash.zoo_animail_display.R;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

public class ZooListActivity extends Activity implements OnItemClickListener {

	private ListView listview;
	private ProgressBar bar;
	private List<ZooItem> list;

	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			if (msg.what == 1) {
				listview.setAdapter(new MyAdapter(ZooListActivity.this, list));
				listview.setVisibility(View.VISIBLE);
				bar.setVisibility(View.GONE);
			} else if (msg.what == 2) {
				Toast.makeText(ZooListActivity.this, "請確定網路!!", Toast.LENGTH_SHORT).show();
				bar.setVisibility(View.GONE);
			}
		}

	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		setContentView(R.layout.activity_zoo_list);
		findViews();
		init();
	}

	private void findViews() {
		listview = (ListView) findViewById(R.id.listView1);
		bar = (ProgressBar) findViewById(R.id.progressBar1);
		listview.setOnItemClickListener(this);
	}

	private void init() {
		list = new ArrayList<ZooItem>();
		listview.setVisibility(View.GONE);
		bar.setVisibility(View.VISIBLE);
		new Task()
				.execute("http://data.taipei/opendata/datalist/apiAccess?scope=resourceAquire&rid=a3e2b221-75e0-45c1-8f97-75acbd43d613");
	}

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
						item.setA_Location(j.getString("A_Location"));
						item.setA_Phylum(j.getString("A_Phylum"));
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
					mHandler.sendEmptyMessage(1);
				} catch (JSONException e) {
					mHandler.sendEmptyMessage(2);
				}
			} else {
				mHandler.sendEmptyMessage(2);
			}
		}

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		ZooItem item = list.get(position);
		Intent intent = new Intent(this,ViewPagerActivity.class);
		intent.putExtra("item", item);
		startActivity(intent);
	}

}

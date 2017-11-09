package com.blackcash.zoo_animail_display;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.blackcash.zoo_animail_display.R;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.PageTransformer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ViewPagerActivity extends Activity implements OnInitListener,
		OnClickListener {

	private ViewPager mViewPager;
	private Button btnTTS;
	List<View> viewList;
	List<ItemData> list = new ArrayList<ItemData>();
	TextToSpeech mTts = null;
	String str_content = "";

	Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			if (msg.what == 1) {
				mViewPager.setAdapter(new MyPagerAdapter(viewList));
				mViewPager.setCurrentItem(0); // 扢离蘇�炵掏曼�
				// } else if (msg.what == 2) {
				// Toast.makeText(ViewPagerActivity.this, "沒有資料",
				// Toast.LENGTH_SHORT).show();
				// finish();
			}
		}

	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		setContentView(R.layout.activity_main);
		init();
		// 妗瞰趙巠饜ん
		mViewPager = (ViewPager) findViewById(R.id.viewpager);
		btnTTS = (Button) findViewById(R.id.btnTTS);
		btnTTS.setOnClickListener(this);
		mViewPager.setPageTransformer(true, new PageTransformer() {
			private static final float MIN_SCALE = 0.75f;

			@Override
			public void transformPage(View view, float position) {

				int pageWidth = view.getWidth();

				if (position < -1) { // [-Infinity,-1)
					// This page is way off-screen to the left.
					view.setAlpha(0);

				} else if (position <= 0) { // [-1,0]
					// Use the default slide transition when moving to the left
					// page
					view.setAlpha(1);
					view.setTranslationX(0);
					view.setScaleX(1);
					view.setScaleY(1);

				} else if (position <= 1) { // (0,1]
					// Fade the page out.
					view.setAlpha(1 - position);

					// Counteract the default slide transition
					view.setTranslationX(pageWidth * -position);

					// Scale the page down (between MIN_SCALE and 1)
					float scaleFactor = MIN_SCALE + (1 - MIN_SCALE)
							* (1 - Math.abs(position));
					view.setScaleX(scaleFactor);
					view.setScaleY(scaleFactor);

				} else { // (1,+Infinity]
					// This page is way off-screen to the right.
					view.setAlpha(0);
				}

			}
		});
		new Thread() {

			@Override
			public void run() {
				updateBitmap();
			}

		}.start();
	}

	private void init() {
		// ZooItem item1 = new ZooItem();
		// item1.setA_Name_Ch("大貓熊");
		// item1.setA_Pic01_ALT("大貓熊「團團」和「圓圓」");
		// item1.setA_Pic01_URL("http://www.zoo.gov.tw/iTAP/03_Animals/PandaHouse/Panda_Pic01.jpg");
		// item1.setA_Pic02_ALT("「圓仔」跟媽媽互動");
		// item1.setA_Pic02_URL("http://www.zoo.gov.tw/iTAP/03_Animals/PandaHouse/Panda_Pic02.jpg");
		// item1.setA_Pic03_ALT("「大貓熊「圓仔」");
		// item1.setA_Pic03_URL("http://www.zoo.gov.tw/iTAP/03_Animals/PandaHouse/Panda_Pic03.jpg");
		// item1.setA_Pic04_ALT("「大貓熊「團團」和「圓圓」");
		// item1.setA_Pic04_URL("http://www.zoo.gov.tw/iTAP/03_Animals/PandaHouse/Panda_Pic04.jpg");
		ZooItem item1 = (ZooItem) getIntent().getSerializableExtra("item");
		mTts = new TextToSpeech(ViewPagerActivity.this, ViewPagerActivity.this);
		viewList = new ArrayList<View>();
		StringBuffer sb = new StringBuffer();
		if (!item1.getA_Name_Ch().equals(""))
			sb.append("名字:" + item1.getA_Name_Ch() + "\n");
		if (!item1.getA_Location().equals(""))
			sb.append("館別:" + item1.getA_Location() + "\n");
		if (!item1.getA_Distribution().equals(""))
			sb.append("分佈:" + item1.getA_Distribution() + "\n");
		if (!item1.getA_Phylum().equals(""))
			sb.append("種類:" + item1.getA_Phylum() + item1.getA_Class()
					+ item1.getA_Order() + item1.getA_Family() + "\n");
		if (!item1.getA_Habitat().equals(""))
			sb.append("棲息地:" + item1.getA_Habitat() + "\n");
		if (!item1.getA_Diet().equals(""))
			sb.append("飲食:" + item1.getA_Diet() + "\n");
		if (!item1.getA_Interpretation().equals(""))
			sb.append("解釋:" + item1.getA_Interpretation() + "\n");
		str_content = sb.toString();

		if (!item1.getA_Pic01_ALT().equals("")) {
			list.add(new ItemData(item1.getA_Pic01_ALT(), item1
					.getA_Pic01_URL()));
		}
		if (!item1.getA_Pic02_ALT().equals("")) {
			list.add(new ItemData(item1.getA_Pic02_ALT(), item1
					.getA_Pic02_URL()));
		}
		if (!item1.getA_Pic03_ALT().equals("")) {
			list.add(new ItemData(item1.getA_Pic03_ALT(), item1
					.getA_Pic03_URL()));
		}
		if (!item1.getA_Pic04_ALT().equals("")) {
			list.add(new ItemData(item1.getA_Pic04_ALT(), item1
					.getA_Pic04_URL()));
		}
		updataContent();
	}

	private void updataContent() {
		LayoutInflater mInflater = getLayoutInflater().from(this);
		View v1 = mInflater.inflate(R.layout.content, null);
		TextView tv1 = (TextView) v1.findViewById(R.id.tvContent);
		tv1.setText(str_content);
		viewList.add(v1);
	}

	private void updateBitmap() {
		LayoutInflater mInflater = getLayoutInflater().from(this);
		if (list.size() > 0) {
			for (ItemData item : list) {
				try {
					View v1 = mInflater.inflate(R.layout.item, null);
					TextView tvTitle = (TextView) v1.findViewById(R.id.tvTitle);
					ImageView iv = (ImageView) v1.findViewById(R.id.ivPicture);
					URL newurl = new URL(item.url);
					InputStream is = newurl.openConnection().getInputStream();
					BufferedInputStream bis = new BufferedInputStream(is);
					Bitmap mIcon_val = BitmapFactory.decodeStream(bis);
					iv.setImageBitmap(mIcon_val);
					tvTitle.setText(item.title);
					viewList.add(v1);
				} catch (MalformedURLException e) {
					mHandler.sendEmptyMessage(2);
				} catch (IOException e) {
					mHandler.sendEmptyMessage(2);
				}
			}
			mHandler.sendEmptyMessage(1);
		} else {
			mHandler.sendEmptyMessage(2);
		}
	}

	class ItemData {
		String title;
		String url;

		public ItemData(String title, String url) {
			this.title = title;
			this.url = url;
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		if (mTts != null) {
			mTts.stop();
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		mTts.shutdown();
	}

	@Override
	public void onInit(int status) {
		if (status == TextToSpeech.SUCCESS) {
			int result = mTts.setLanguage(Locale.CHINESE);
			if (result == TextToSpeech.LANG_MISSING_DATA
					|| result == TextToSpeech.LANG_NOT_SUPPORTED) {
				btnTTS.setEnabled(false);
			} else {
				btnTTS.setEnabled(true);
			}
		}
	}

	@Override
	public void onClick(View v) {
		int index = mViewPager.getCurrentItem();
		String text = str_content;
		if (mTts != null) {
			mTts.stop();
		}
		mTts.speak(text, TextToSpeech.QUEUE_ADD, null);
	}
}

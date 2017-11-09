package com.blackcash.zoo_animail_display;

import java.util.List;

import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

public class MyPagerAdapter extends PagerAdapter {

	private List<View> mListView;
	
	public MyPagerAdapter(List<View> mListView) {
		super();
		this.mListView = mListView;
	}


    //����positionλ�õĽ���
    public void destroyItem(View arg0, int arg1, Object arg2) {
        // TODO Auto-generated method stub
        ((ViewGroup)arg0).removeView(mListView.get(arg1));
    }

    @Override
    public void finishUpdate(View arg0) {
        // TODO Auto-generated method stub
       
    }

    ////��ȡ��ǰ���������
    public int getCount() {
        // TODO Auto-generated method stub
        return mListView.size();
    }
  //��ʼ��positionλ�õĽ���
    @Override
    public Object instantiateItem(View arg0, int arg1) {
        // TODO Auto-generated method stub
        ((ViewGroup)arg0).addView(mListView.get(arg1), 0);
        return mListView.get(arg1);
    }

 // �ж��Ƿ��ɶ������ɽ���
    public boolean isViewFromObject(View arg0, Object arg1) {
        // TODO Auto-generated method stub
        return arg0==(arg1);
    }

    @Override
    public void restoreState(Parcelable arg0, ClassLoader arg1) {
        // TODO Auto-generated method stub
       
    }

    @Override
    public Parcelable saveState() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void startUpdate(View arg0) {
        // TODO Auto-generated method stub
       
    }
   
}


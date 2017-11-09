package com.blackcash.zoo_animail_display;

import java.util.List;

import com.blackcash.zoo_animail_display.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class MyAdapter extends BaseAdapter {
	
	private List<ZooItem> list;
	private LayoutInflater inflater;
	
	public MyAdapter(Context context,List<ZooItem> list){
		inflater = LayoutInflater.from(context);
		this.list = list;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Holder mHolder = null; 
		if(convertView == null){
			convertView = inflater.inflate(R.layout.list_item, null);
			mHolder = new Holder();
			mHolder.tv1 = (TextView) convertView.findViewById(R.id.tvGroup);
			mHolder.tv2 = (TextView) convertView.findViewById(R.id.tvChild2);
			convertView.setTag(mHolder);
		}else{
			mHolder = (Holder) convertView.getTag();
		}
		ZooItem item = list.get(position);
		mHolder.tv1.setText(item.getA_Name_Ch());
		mHolder.tv2.setText(item.getA_Phylum());
		return convertView;
	}
	
	class Holder{
		TextView tv1;
		TextView tv2;
	}

	
}

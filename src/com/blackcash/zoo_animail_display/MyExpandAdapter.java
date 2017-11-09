package com.blackcash.zoo_animail_display;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MyExpandAdapter extends BaseExpandableListAdapter {

	private Context context;
	List<Map<String, String>> groups;
	List<List<Map<String, ZooItem>>> childs;
	private boolean bState = false;

	public MyExpandAdapter(Context context, List<Map<String, String>> groups,
			List<List<Map<String, ZooItem>>> childs) {
		this.context = context;
		this.groups = groups;
		this.childs = childs;
	}

	@Override
	public int getGroupCount() {
		return groups.size();
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		return childs.get(groupPosition).size();
	}

	@Override
	public Object getGroup(int groupPosition) {
		return groups.get(groupPosition);
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		return childs.get(groupPosition).get(childPosition);
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	@Override
	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {

		convertView = LayoutInflater.from(context)
				.inflate(R.layout.group, null);
		ImageView iv = (ImageView) convertView.findViewById(R.id.imageView1);
		TextView tvGroup = (TextView) convertView.findViewById(R.id.tvGroup);
		tvGroup.setText(groups.get(groupPosition).get("group"));

		if (isExpanded) {
			iv.setImageResource(android.R.drawable.btn_minus);
		} else {
			iv.setImageResource(android.R.drawable.btn_plus);
		}
		return convertView;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		ZooItem item = null;
		Holder mHolder = null;
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(R.layout.child,
					null);
			mHolder = new Holder();
			mHolder.iv = (ImageView) convertView.findViewById(R.id.ivChild);
			mHolder.tv1 = (TextView) convertView.findViewById(R.id.tvChild1);
			mHolder.tv2 = (TextView) convertView.findViewById(R.id.tvChild2);
			convertView.setTag(mHolder);
		} else {
			mHolder = (Holder) convertView.getTag();
		}
		item = childs.get(groupPosition).get(childPosition).get("child");
		// if (!bState)
		Log.d("data", item.getA_Pic01_URL());

//		if (!item.getA_Pic01_URL().equals("")) {
//			Log.d("url",item.getA_Pic01_URL());
//			ImageLoader imageLoader = new ImageLoader(context);
//			imageLoader.DisplayImage(item.getA_Pic01_URL(), mHolder.iv);
//		} else {
//			mHolder.iv.setImageResource(R.drawable.ic_launcher);
//		}
		mHolder.iv.setVisibility(View.GONE);
		
		mHolder.tv1.setText(item.getA_Name_Ch());
		mHolder.tv2.setText(item.getA_Name_En());

		return convertView;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return true;
	}

	class Holder {
		ImageView iv;
		TextView tv1;
		TextView tv2;
	}


	public void setState(boolean bState) {
		this.bState = bState;
		if (!bState)
			notifyDataSetChanged();
	}
		
}

package com.tomato.fqsdk.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.tomato.fqsdk.utils.FindResHelper;

public class PayAdapter extends BaseAdapter {
	int[] imglistnotchoose={FindResHelper.RDrawable("hj_pay_01"),FindResHelper.RDrawable("hj_pay_02")};
	//FtnnRes.RDrawable("hj_pay_03"),FtnnRes.RDrawable("hj_pay_04")
	int[] imglistchoose={FindResHelper.RDrawable("hj_pay_01_active"),FindResHelper.RDrawable("hj_pay_02_active")};
	//FtnnRes.RDrawable("hj_pay_03_active"),FtnnRes.RDrawable("hj_pay_04_active")
	Context context;
	int seposition;
	public PayAdapter(Context context) {
		this.context = context;
	}

	public void selecter(int seposition) {
		this.seposition = seposition;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return imglistchoose.length;
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return imglistchoose[arg0];
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final Holder holder;

		if (convertView == null) {
			holder = new Holder();
			convertView = View.inflate(context,
					FindResHelper.RLayout("hj_pay_item_sec"), null);
			holder.iv = (ImageView) convertView.findViewById(FindResHelper
					.RId("item_image"));
			convertView.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();
		}
		holder.iv.setImageResource(imglistnotchoose[position]);

		if (seposition == position) {
			holder.iv.setImageResource(imglistchoose[position]);
		} else {
			holder.iv.setImageResource(imglistnotchoose[position]);
		}
		return convertView;
	}

	class Holder {
		private ImageView iv;
	}

}
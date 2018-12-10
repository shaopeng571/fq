
package com.tomato.fqsdk.adapter;

import java.util.ArrayList;
import java.util.List;

import com.tomato.fqsdk.utils.FindResHelper;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/** 
 * @ClassName OptionsAdapter 
 * @Description TODO   ѡ       б       
**/
public class HJAccountAdapter extends BaseAdapter {

	private List<String> list = new ArrayList<String>(); 
    private Context mContext; 

    public HJAccountAdapter(Context context, List<String> list){
    	this.mContext = context;
    	if (list!=null) {
    		this.list = list;
		}
    }
	
	@Override
	public int getCount() {
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
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null; 
        if (convertView == null) { 
            holder = new ViewHolder(); 
            //       
            convertView = LayoutInflater.from(mContext).inflate(FindResHelper.RLayout("hj_accountlist_item"), null); 
            holder.relativeLayout=(RelativeLayout)convertView.findViewById(FindResHelper.RId("option_item_layout")); 
            holder.textView = (TextView) convertView.findViewById(FindResHelper.RId("option_item_text")); 
            holder.delBtn = (ImageView) convertView.findViewById(FindResHelper.RId("option_item_del")); 
            
            convertView.setTag(holder); 
        } else { 
            holder = (ViewHolder) convertView.getTag(); 
        } 
        
        holder.textView.setText(list.get(position));
        
        //Ϊ      ѡ     ֲ        ¼       Ч   ǵ            䵽 ı   
        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mOnClicked.onItemSelected(position);
			}
		});
        
        //Ϊ      ѡ  ɾ  ͼ 겿       ¼       Ч   ǵ      ѡ  ɾ  
        holder.delBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mOnClicked.onItemDelete(position);
			}
		});
        
        return convertView; 
	}
	
	public OnClicked mOnClicked;
	public void setOnClicked(OnClicked onClicked) 
	{
		mOnClicked=onClicked;
	}
	public interface OnClicked
	{
		void onItemDelete(int index);
		void onItemSelected(int index);
	}

}

class ViewHolder { 
	RelativeLayout relativeLayout;
    TextView textView; 
    ImageView delBtn; 
} 


package com.tomato.fqsdk.widget;

import com.tomato.fqsdk.utils.FindResHelper;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.TextView;

public class CustomProgressDialog extends Dialog {
	private Context context = null;
	private static CustomProgressDialog cpDialog = null;
	public CustomProgressDialog(Context context){
		super(context);
		this.context = context;
	}
	
	public CustomProgressDialog(Context context, int theme) {
        super(context, theme);
    }
	
	public static CustomProgressDialog createDialog(Context context){
		cpDialog = new CustomProgressDialog(context,FindResHelper.RStyle("HJLoadingDialog"));
		cpDialog.setContentView(FindResHelper.RLayout("hj_loading_dialog"));
		cpDialog.getWindow().getAttributes().gravity = Gravity.CENTER;
		cpDialog.setCanceledOnTouchOutside(false);
		cpDialog.setCancelable(false);
		
		return cpDialog;
	}
	
    public void onWindowFocusChanged(boolean hasFocus){
    	
    	if (cpDialog == null){
    		return;
    	}
    	
        ImageView imageView = (ImageView) cpDialog.findViewById(FindResHelper.RId("loadingImageView"));
        AnimationDrawable animationDrawable = (AnimationDrawable) imageView.getBackground();
        animationDrawable.start();
    }
    
    /**
     *       setTitile     
     * @param strTitle
     * @return  
     */
    public CustomProgressDialog setTitile(String strTitle){
    	return cpDialog;
    }
    
    /**
     *       setMessage   ʾ    
     * @param strMessage
     * @return
     */
    public CustomProgressDialog setMessage(String strMessage){
    	TextView tvMsg = (TextView)cpDialog.findViewById(FindResHelper.RId("id_tv_loadingmsg"));
    	
    	if (tvMsg != null){
    		tvMsg.setText(strMessage);
    	}
    	
    	return cpDialog;
    }

    public void showDialog() {
      if ((this.context != null) && (this.context instanceof Activity)) {
        Activity localActivity = (Activity)this.context;
        if ((cpDialog != null) && (!(cpDialog.isShowing())) && (!(localActivity.isFinishing())))
        	cpDialog.show();
      }
    }

    public void closeDialog() {
      if ((this.context != null) && (this.context instanceof Activity)) {
        Activity localActivity = (Activity)this.context;
        if ((cpDialog != null) && (cpDialog.isShowing()) && (!(localActivity.isFinishing()))) {
        	cpDialog.dismiss();
        	cpDialog = null;
        }
      }
    }
}
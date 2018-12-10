package com.tomato.fqsdk;

import org.json.JSONObject;

import com.tomato.fqsdk.base.BaseActivity;
import com.tomato.fqsdk.data.PostUserInfo;
import com.tomato.fqsdk.fqutils.FLogger;
import com.tomato.fqsdk.data.HyApi.HttpCallback;
import com.tomato.fqsdk.models.CLCommon;
import com.tomato.fqsdk.utils.FindResHelper;
import com.tomato.fqsdk.utils.JsonParse;
import com.tomato.fqsdk.utils.Tools;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class HyBindPhoneActivity extends BaseActivity implements
		OnClickListener {
	public static final int UNBOUNDED = 0;
	public static final int BINDING = 1;
	public static final int BINDOK = 2;
	private ImageView imgBack;
	private LinearLayout btnOk;
	private ImageView imgClose;
	private EditText edAccount;
	private EditText edPwd;
	private EditText edTel;
	private EditText edCode;
	private Button btngetcode;
	private TextView tv_phone;
	private String code="";//,phone="";
	private String account="";
	private RelativeLayout mRelativeLayout;
	private CountDownTimer timer = new CountDownTimer(60000, 1000) {

		@Override
		public void onTick(long millisUntilFinished) {
			btngetcode.setBackgroundResource(FindResHelper.RDrawable("fq_okbtnshape_gray"));
			btngetcode.setText((millisUntilFinished / 1000)
					+ FindResHelper.RStringStr("hj_toast_regwait"));
			btngetcode.setEnabled(false);
		}

		@Override
		public void onFinish() {
			btngetcode.setBackgroundResource(FindResHelper.RDrawable("fq_okbtnshape"));
			btngetcode.setEnabled(true);
			btngetcode.setText(FindResHelper.RStringStr("hj_toast_reggetcode"));
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(FindResHelper.RLayout("hj_user_bindphone"));
		Bundle bundle = new Bundle();
		bundle=getIntent().getExtras();
		int startbind = bundle.getInt("BIND", 0);
		if (startbind == BINDING) {
			
				initViewVerifyTheOldCellPhone(bundle.getString("ACCOUNT"),bundle.getString("PWD"),bundle.getString("TEL"));
			
		}
		if (startbind == BINDOK) {
			initViewBindOk(bundle.getString("ACCOUNT"),bundle.getString("PWD"),bundle.getString("old_code"));
		}
		if (startbind==UNBOUNDED) {
			if (bundle.getString("ACCOUNT").contains("游客")) {
				account="";
			}else {
				account=bundle.getString("ACCOUNT");
			}
			
			initView();
		}
		imgBack = (ImageView) findViewById(FindResHelper.RId("btn_back"));
		imgClose = (ImageView) findViewById(FindResHelper.RId("img_btn_close"));
		imgClose.setOnClickListener(this);
		imgBack.setOnClickListener(this);
		
	}

	private void initViewBindOk(final String account,final String pwd,final String old_code) {
		mRelativeLayout=(RelativeLayout) findViewById(FindResHelper.RId("hj_bindphonere2"));
		mRelativeLayout.setVisibility(View.VISIBLE);
		edTel = (EditText) findViewById(FindResHelper.RId("edt_tel2"));
		edCode = (EditText) findViewById(FindResHelper.RId("edt_code2"));
		btngetcode = (Button) findViewById(FindResHelper.RId("hj_btn_getcode2"));
		btnOk = (LinearLayout) findViewById(FindResHelper.RId("btn_ok2"));
		btngetcode.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (!TextUtils.isEmpty(edTel.getText().toString())) {
					getCode(edTel.getText().toString(),CLCommon.CODETYPE_CHANGEBIND);
				}else {
					ShowToast(FindResHelper.RStringStr("hj_btn_bind_newphone"));
				}
				
			}
		});
		btnOk.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
//				if (edCode.getText().toString().equals(code)&&!TextUtils.isEmpty(edCode.getText().toString())&&phone.equals(edTel.getText().toString())) {
					if (!TextUtils.isEmpty(edCode.getText().toString())&&!TextUtils.isEmpty(edTel.getText().toString())) {
				UpdateBindTel(account,pwd,edTel.getText().toString(),old_code,edCode.getText().toString());
				}else {
					ShowToast(FindResHelper.RStringStr("hj_toast_findpwdpleasewirteall"));
				}

			}
		});
	}

	private void initViewVerifyTheOldCellPhone(final String account,final String pwd,final String tel) {
		mRelativeLayout=(RelativeLayout) findViewById(FindResHelper.RId("hj_bindphonere1"));
		mRelativeLayout.setVisibility(View.VISIBLE);
		tv_phone = (TextView) findViewById(FindResHelper.RId("hj_phone"));
		edCode = (EditText) findViewById(FindResHelper.RId("edt_code1"));
		btngetcode = (Button) findViewById(FindResHelper.RId("hj_btn_getcode1"));
		btnOk = (LinearLayout) findViewById(FindResHelper.RId("btn_ok1"));
		tv_phone.setText("已绑定手机:"+tel);
		btngetcode.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				getCode(tel,CLCommon.CODETYPE_CHANGEAFTER);
			}
		});
		btnOk.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (edCode.getText().toString().equals(code)&&!TextUtils.isEmpty(edCode.getText().toString())) {
					Intent intent = new Intent(HyBindPhoneActivity.this,
							HyBindPhoneActivity.class);
					Bundle bundle = new Bundle();
					bundle.putString("old_code", edCode.getText().toString());
					bundle.putInt("BIND", BINDOK);
					bundle.putString("ACCOUNT", account);
					bundle.putString("PWD", pwd);
					intent.putExtras(bundle);
					startActivity(intent);
					finish();
				}else {
					ShowToast(FindResHelper.RStringStr("hj_toast_writerightcode"));
				}

			}
		});
	}

	@Override
	public void initView() {
		mRelativeLayout=(RelativeLayout) findViewById(FindResHelper.RId("hj_bindphonere0"));
		mRelativeLayout.setVisibility(View.VISIBLE);
		edAccount = (EditText) findViewById(FindResHelper.RId("edt_account"));
		edPwd = (EditText) findViewById(FindResHelper.RId("edt_pwd"));
		edTel = (EditText) findViewById(FindResHelper.RId("edt_tel"));
		edCode = (EditText) findViewById(FindResHelper.RId("edt_code"));
		btngetcode = (Button) findViewById(FindResHelper.RId("hj_btn_getcode"));
		btnOk = (LinearLayout) findViewById(FindResHelper.RId("btn_ok"));
		try {
			edAccount.setText(account);
		} catch (Exception e) {
			edAccount.setText("");
		}

		btngetcode.setOnClickListener(this);
		btnOk.setOnClickListener(this);
		
	}

	@Override
	public void onClick(View v) {
		//   ص   ¼    
		if (v.getId() == FindResHelper.RId("btn_back")
				|| v.getId() == FindResHelper.RId("img_btn_close")) {
			startLogin();
		}
		//ȷ    ť     ֻ 
		if (v.getId() == FindResHelper.RId("btn_ok")) {
			QueryBind(edAccount.getText().toString(), edPwd.getText()
					.toString(), edTel.getText().toString(), edCode.getText()
					.toString());
		}
		//
		if (v.getId() == FindResHelper.RId("btn_okfaile")) {
			startLogin();
		}
		if (v.getId() == FindResHelper.RId("hj_btn_getcode")) {
			String mobile = edTel.getText().toString();
			if (TextUtils.isEmpty(mobile)) {
				ShowToast(FindResHelper
						.RStringStr("hj_toast_regpleasewritephone"));
				return;
			}
//			if (!Tools.isMobileNo(mobile)) {
//				ShowToast(FindResHelper
//						.RStringStr("hj_toast_regpleasewriteokphone"));
//				return;
//			}
			getCode(mobile,CLCommon.CODETYPE_BINDPHONE);
		}
//		if (v.getId() == FindResHelper.RId("hj_btn_getcode2")) {
//			String mobile = edTel.getText().toString();
//			if (TextUtils.isEmpty(mobile)) {
//				ShowToast(FindResHelper
//						.RStringStr("hj_toast_regpleasewritephone"));
//				return;
//			}
////			if (!Tools.isMobileNo(mobile)) {
////				ShowToast(FindResHelper
////						.RStringStr("hj_toast_regpleasewriteokphone"));
////				return;
////			}
//			getCode(mobile);
//		}
	}

	private void startLogin() {
		startActivity(new Intent(HyBindPhoneActivity.this,
				HyLoginActivity.class));
		finish();
	}
	/**
	 *   ȡ  ֤  
	 */
	private void getCode(final String mobile,final String type) {
		
		PostUserInfo.CLGetCode(HyBindPhoneActivity.this, mobile,type,
				new HttpCallback() {
			@Override
			public void onStart() {
				btngetcode.setBackgroundResource(FindResHelper
						.RDrawable("fq_okbtnshape_gray"));
				progressDialog.show();
				super.onStart();
			}
			@Override
			public void onSuccess(String responseString) {
				btngetcode.setBackgroundResource(FindResHelper
						.RDrawable("fq_okbtnshape"));
				progressDialog.dismiss();
				try {
					if (!TextUtils.isEmpty(responseString)) {
						FLogger.e(responseString);
						JSONObject jsonObject;
							jsonObject = new JSONObject(responseString);
							
							String ret =JsonParse.HJJsonGetRet(jsonObject);
							String msg = JsonParse.HJJsonGetMsg(jsonObject);
							String data=JsonParse.HJJsonGetData(jsonObject);
						if (ret.equals("1")) {
							HyBindPhoneActivity.this.code= new JSONObject(data).optString("verify_code");
//							phone=mobile;
							ShowToast(FindResHelper.RStringStr("hj_toast_reggetcodesuccess"));
							timer.start();
						} else {
							ShowToast(msg);
							
						}
					}else {
						ShowToast(FindResHelper.RStringStr("hj_toast_paychecknetwork"));
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
					
					
				
			}
		});
	}
	/**
	 *    İ 
	 */
	private void UpdateBindTel(final String account,final String pwd,final String mobile,final String old_code,final String newcode) {
		PostUserInfo.CLChangeBind(HyBindPhoneActivity.this,account ,pwd,mobile,old_code,newcode,
				new HttpCallback() {
			@Override
			public void onStart() {
				btnOk.setBackgroundResource(FindResHelper
						.RDrawable("fq_okbtnshape_gray"));
				progressDialog.show();
				super.onStart();
			}
			@Override
			public void onSuccess(String responseString) {
				btnOk.setBackgroundResource(FindResHelper
						.RDrawable("fq_okbtnshape"));
				progressDialog.dismiss();
						try {
							if (!TextUtils.isEmpty(responseString)) {
								JSONObject jsonObject;
									jsonObject = new JSONObject(responseString);
									String ret =JsonParse.HJJsonGetRet(jsonObject);
									String msg = JsonParse.HJJsonGetMsg(jsonObject);
//									String data=JsonParse.HJJsonGetData(jsonObject);
								if (ret.equals("1")) {
									ShowToast(FindResHelper.RStringStr("hj_toast_changebindsuccess"));
									startActivity(new Intent(HyBindPhoneActivity.this, HyLoginActivity.class));
									finish();
								} else {
									ShowToast(msg);

								}
							}else {
								ShowToast(FindResHelper.RStringStr("hj_toast_paychecknetwork"));
							}
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}

					
				});
	}

	/**
	 *   ѯ Ƿ 󶨹  ֻ 
	 * @param account
	 */
	private void QueryBind(final String account,final String pwd,final String phone,final String code) {
		if (TextUtils.isEmpty(account) || TextUtils.isEmpty(pwd)
				|| TextUtils.isEmpty(phone) || TextUtils.isEmpty(code)) {
			ShowToast(FindResHelper
					.RStringStr("hj_toast_findpwdpleasewirteall"));
			return;
		}
		PostUserInfo.CLBindisBind(HyBindPhoneActivity.this,account,pwd,
				new HttpCallback() {
			@Override
			public void onStart() {
				btnOk.setBackgroundResource(FindResHelper
						.RDrawable("fq_okbtnshape_gray"));
				progressDialog.show();
				super.onStart();
			}
			@Override
			public void onSuccess(String responseString) {
				btnOk.setBackgroundResource(FindResHelper
						.RDrawable("fq_okbtnshape"));
				progressDialog.dismiss();
						try {
							if (!TextUtils.isEmpty(responseString)) {
								JSONObject jsonObject;
									jsonObject = new JSONObject(responseString);
									String ret =JsonParse.HJJsonGetRet(jsonObject);
//									String msg = JsonParse.HJJsonGetMsg(jsonObject);
									String data=JsonParse.HJJsonGetData(jsonObject);
								if (ret.equals("1")) {
									Intent intent = new Intent(
											HyBindPhoneActivity.this,
											HyBindPhoneActivity.class);
									Bundle bundle = new Bundle();
									bundle.putInt("BIND", HyBindPhoneActivity.BINDING);
									bundle.putString("TEL", new JSONObject(data).optString("phone"));
									bundle.putString("ACCOUNT", account);
									bundle.putString("PWD", pwd);
									intent.putExtras(bundle);
									startActivity(intent);
									finish();
								} else {
									bindPhone(account, pwd, phone, code);
								}
							}else {
								ShowToast(FindResHelper.RStringStr("hj_toast_paychecknetwork"));
							}
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
					}
				});

	}
	/**
	 *    ֻ 
	 */
	private void bindPhone(String account, String pwd, String tel, String code) {
		if (TextUtils.isEmpty(account) || TextUtils.isEmpty(pwd)
				|| TextUtils.isEmpty(tel) || TextUtils.isEmpty(code)) {
			ShowToast(FindResHelper
					.RStringStr("hj_toast_findpwdpleasewirteall"));
			return;
		}
		if (TextUtils.isEmpty(code)) {
			ShowToast(FindResHelper.RStringStr("hj_toast_regpleasewritecode"));
			return;
		}
//		if (!phone.equals(tel)||!(this.code).equals(code)) {
//			ShowToast(FindResHelper.RStringStr("hj_toast_writerightcode"));
//			return;
//		}
		PostUserInfo.CLBindPhone(HyBindPhoneActivity.this, account, pwd, tel,code, 
				new HttpCallback() {
			@Override
			public void onStart() {
				btnOk.setBackgroundResource(FindResHelper
						.RDrawable("fq_okbtnshape_gray"));
				progressDialog.show();
				super.onStart();
			}
			@Override
			public void onSuccess(String responseString) {
				btnOk.setBackgroundResource(FindResHelper
						.RDrawable("fq_okbtnshape"));
				progressDialog.dismiss();

						try {
							if (!TextUtils.isEmpty(responseString)) {
								FLogger.e(responseString);
								JSONObject jsonObject;
									jsonObject = new JSONObject(responseString);
									String ret =JsonParse.HJJsonGetRet(jsonObject);
									String msg = JsonParse.HJJsonGetMsg(jsonObject);
//									String data=JsonParse.HJJsonGetData(jsonObject);
								if (ret.equals("1")) {
									ShowToast(FindResHelper
											.RStringStr("hj_toast_regbindsuccess"));
									startLogin();
								} else {
									Toast.makeText(HyBindPhoneActivity.this, msg,
											Toast.LENGTH_SHORT).show();
								}
							}else {
								ShowToast(FindResHelper.RStringStr("hj_toast_paychecknetwork"));
							}
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}

				});

	}

	private void ShowToast(String str) {
		Toast.makeText(HyBindPhoneActivity.this, str, Toast.LENGTH_SHORT)
				.show();
	}
}

package com.tomato.fqsdk;

import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tomato.fqsdk.base.BaseActivity;
import com.tomato.fqsdk.control.CLControlCenter;
import com.tomato.fqsdk.data.PostUserInfo;
import com.tomato.fqsdk.data.HyApi.HttpCallback;
import com.tomato.fqsdk.models.CLCommon;
import com.tomato.fqsdk.utils.CLNaviteHelper;
import com.tomato.fqsdk.utils.FindResHelper;
import com.tomato.fqsdk.utils.HJAccountDBHelper;
import com.tomato.fqsdk.utils.JsonParse;
import com.tomato.fqsdk.utils.Tools;
import com.tomato.fqsdk.utils.HJAccountDBHelper.UserAccount;

public class HyFindPwdActivity extends BaseActivity implements OnClickListener {
	private HyFindPwdActivity context;
	TextView tvPhone;
	EditText edAccount;
	TextView tv_finderror;
	EditText edPwd;
	EditText edCode;
	EditText edNewPwd;
	private LinearLayout btnok;
	private ImageView btnback;
	Button btngetcode;
	ImageView imgClose;
	ImageView imgBack;
	String _pwd = "";
	PopupWindow popupWindow;
	private String account="",tel="";
	private int num = 1;
	private RelativeLayout linFindPwdLayout;
	private HJAccountDBHelper hjAccountDBHelper;
//	private String code="",phone="";
	private CountDownTimer timer = new CountDownTimer(60000, 1000) {  
		  
	    @Override  
	    public void onTick(long millisUntilFinished) {  
	    	btngetcode.setBackgroundResource(FindResHelper
					.RDrawable("fq_okbtnshape_gray"));
	    	btngetcode.setText((millisUntilFinished / 1000) + FindResHelper.RStringStr("hj_toast_regwait"));  
	    	btngetcode.setEnabled(false);
	    }  

	    @Override  
	    public void onFinish() {  
	    	btngetcode.setBackgroundResource(FindResHelper
	    			.RDrawable("fq_okbtnshape"));
	    	btngetcode.setEnabled(true);  
	    	btngetcode.setText(FindResHelper.RStringStr("hj_toast_reggetcode"));  
	    }  
	};  
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initView();
	}
	
	@Override
	public void initView() {
		hjAccountDBHelper =HJAccountDBHelper.getInstance(HyFindPwdActivity.this);
		context=this;
		setContentView(FindResHelper.RLayout("hj_user_findpwd"));
		if (getIntent().getExtras().getInt("findpwd") == 1) {
			linFindPwdLayout = (RelativeLayout) findViewById(FindResHelper
					.RId("hj_find1re"));
			
			btnback = (ImageView) findViewById(FindResHelper.RId("btn_back1"));
			btnok = (LinearLayout) findViewById(FindResHelper.RId("btn_ok1"));
			edAccount = (EditText) findViewById(FindResHelper.RId("edt_account"));
			tv_finderror = (TextView) findViewById(FindResHelper.RId("hj_find1text"));
			try {
				if (getIntent().getExtras().getString("ACCOUNT").contains(" ο ")) {
					edAccount.setText("");
				}else {
					
					edAccount.setText(getIntent().getExtras().getString("ACCOUNT"));
				}
			} catch (Exception e) {
				edAccount.setText("");
			}
			setTextStandard(edAccount);
			num = 1;
			
		}
		if (getIntent().getExtras().getInt("findpwd") == 2) {
			linFindPwdLayout = (RelativeLayout) findViewById(FindResHelper
					.RId("hj_find2re"));
			btnback = (ImageView) findViewById(FindResHelper.RId("btn_back2"));
			btnok = (LinearLayout) findViewById(FindResHelper.RId("hj_btn_qq"));
			tv_finderror = (TextView) findViewById(FindResHelper.RId("hj_find2text"));
			String text=FindResHelper.RStringStr("hj_find2_text4");
//			 SpannableStringBuilder style=new SpannableStringBuilder(text); 
//			 int index[] = new int[1];  
//		       index[0] = text.indexOf("δ");  
//	           style.setSpan(new ForegroundColorSpan(getResources().getColor(FindResHelper.RColor("hj_new_orange"))),index[0],index[0]+5,Spannable.SPAN_EXCLUSIVE_INCLUSIVE);      
			tv_finderror.setText(text);
			num = 2;
			btnback.setVisibility(View.VISIBLE);
			btnback.setOnClickListener(this);
		}
		if (getIntent().getExtras().getInt("findpwd") == 3) {
			linFindPwdLayout = (RelativeLayout) findViewById(FindResHelper
					.RId("hj_find3re"));
			btnback = (ImageView) findViewById(FindResHelper.RId("btn_back3"));
			btnok = (LinearLayout) findViewById(FindResHelper.RId("btn_ok3"));
			edCode=(EditText) findViewById(FindResHelper.RId("edt_code"));
			btngetcode= (Button) findViewById(FindResHelper.RId("hj_btn_getcode"));
			edNewPwd=(EditText) findViewById(FindResHelper.RId("edt_newpwd"));
			tvPhone=(TextView) findViewById(FindResHelper.RId("hj_phone"));
			num = 3;
			tel=getIntent().getExtras().getString("tel");
			account=getIntent().getExtras().getString("account");
			tvPhone.setText(FindResHelper.RStringStr("hj_find3_text1")+tel);
			btnback.setVisibility(View.VISIBLE);
			btngetcode.setOnClickListener(this);
			btnback.setOnClickListener(this);
		}
		linFindPwdLayout.setVisibility(View.VISIBLE);
		
//		btnok.setVisibility(View.VISIBLE);
		imgClose = (ImageView) findViewById(FindResHelper
				.RId("img_btn_close"));
		imgClose.setOnClickListener(this);
		
		btnok.setOnClickListener(this);
		linFindPwdLayout.setVisibility(View.VISIBLE);

	}

	/**
	 *   ť ¼ 
	 */
	@Override
	public void onClick(View view) {
		if (view.getId() == FindResHelper.RId("img_btn_findclose")) {
			startLogin();
		}
		if (view.getId() == FindResHelper.RId("btn_ok1")) {
			findPwd1(edAccount.getText().toString());
		}
		if (view.getId() == FindResHelper.RId("hj_btn_qq")) {
//			String url11 = "mqqwpa://im/chat?chat_type=wpa&uin="+FindResHelper.RStringStr("hj_find2_qq")+"&version=1";  
//	        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url11))); 
			if (Tools.copy(FindResHelper.RStringStr("hj_find2_qq"), HyFindPwdActivity.this)) {
				ShowToast(FindResHelper.RStringStr("hj_toast_findpwdqq"));
				
			}else {
				
			}
			
		}
		if (view.getId() == FindResHelper.RId("btn_ok3")) {
			try {
				Resetpwd(account, edNewPwd.getText().toString(), edCode.getText().toString());
			} catch (Exception e) {
				ShowToast(FindResHelper.RStringStr("hj_toast_findpwdpleasewirteall"));
			}
			
		}
		if (view.getId() == FindResHelper.RId("btn_back1")) {
			startLogin();
		}
		if (view.getId() == FindResHelper.RId("btn_back2")) {
			startFind1();
		}
		if (view.getId() == FindResHelper.RId("btn_back3")) {
			startFind1();

		}
		if (view.getId() == FindResHelper.RId("img_btn_close")) {
			if (num == 1) {
				startLogin();
			}
			if (num == 2) {
				startLogin();
			}
			if (num == 3) {
				startLogin();
			}
		}
		if (view.getId() == FindResHelper.RId("hj_btn_getcode")) {
			if (TextUtils.isEmpty(tel)) {
				ShowToast(FindResHelper.RStringStr("hj_toast_regpleasewritephone"));
				return;
			}
//			if (!Tools.isMobileNo(tel)) {
//				ShowToast(FindResHelper.RStringStr("hj_toast_regpleasewriteokphone"));
//				return;
//			}
			getCode(tel,CLCommon.CODETYPE_CHANGEPWD);
		}

	}

	/**
	 *      һ     1
	 */
	private void startFind1() {
		Intent intent = new Intent(HyFindPwdActivity.this,
				HyFindPwdActivity.class);
		Bundle bundle = new Bundle();
		bundle.putInt("findpwd", 1);
		bundle.putString("ACCOUNT",account );
		intent.putExtras(bundle);
		startActivity(intent);
		finish();
	}

	/**
	 *      һ     2
	 */
	private void startFind2() {
		Intent intent = new Intent(HyFindPwdActivity.this,
				HyFindPwdActivity.class);
		Bundle bundle = new Bundle();
		bundle.putInt("findpwd", 2);
		intent.putExtras(bundle);
		startActivity(intent);
		finish();
	}

	/**
	 *  һ     3
	 */
	private void startFind3(String account,String tel) {
		Intent intent = new Intent(HyFindPwdActivity.this,
				HyFindPwdActivity.class);
		Bundle bundle = new Bundle();
		bundle.putInt("findpwd", 3);
		bundle.putString("account", account);
		bundle.putString("tel", tel);
		intent.putExtras(bundle);
		startActivity(intent);
		finish();
	}

	private void startLogin() {
		startActivity(new Intent(HyFindPwdActivity.this, HyLoginActivity.class));
		finish();
	}
	/**
	 *   ȡ  ֤  
	 */
	private void getCode(final String mobile,final String type) {
		
		PostUserInfo.CLGetCode(HyFindPwdActivity.this, mobile,type, 
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
						JSONObject jsonObject;
							jsonObject = new JSONObject(responseString);
							String ret =JsonParse.HJJsonGetRet(jsonObject);
							String msg = JsonParse.HJJsonGetMsg(jsonObject);
//							String data=JsonParse.HJJsonGetData(jsonObject);

						if (ret.equals("1")) {
//							String	code1 = new JSONObject(data).optString("verify_code");
//							HyFindPwdActivity.this.code=code1;
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
	 *      һ Step2(        )
	 */
	private void Resetpwd(final String account,final String newpwd,String code) {
		if (TextUtils.isEmpty(account)||TextUtils.isEmpty(newpwd)||TextUtils.isEmpty(code)) {
			ShowToast(FindResHelper.RStringStr("hj_toast_findpwdpleasewirteall"));
			return;
		}
		if (TextUtils.isEmpty(code)) {
			ShowToast(FindResHelper.RStringStr("hj_toast_regpleasewritecode"));
			return;
		}
//		if (!(this.code).equals(code)) {
//			ShowToast(FindResHelper.RStringStr("hj_toast_writerightcode"));
//			return;
//		}
		PostUserInfo.CLFindPwd(HyFindPwdActivity.this, account,newpwd,code,
				new HttpCallback() {
			@Override
			public void onStart() {
				btnok.setBackgroundResource(FindResHelper
						.RDrawable("fq_okbtnshape_gray"));
				progressDialog.show();
				super.onStart();
			}
			@Override
			public void onSuccess(String responseString) {
				btnok.setBackgroundResource(FindResHelper
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
									Long tsLong = System.currentTimeMillis() / 1000;
									String timez = tsLong.toString();
									storeAccount(account,newpwd,timez);
									ShowToast(msg);
									startLogin();
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
	 *      һ Step1( ύ ʺ )
	 */
	private void findPwd1(final String account) {
		if (TextUtils.isEmpty(account)) {
			ShowToast(FindResHelper.RStringStr("hj_toast_loginpleasepressaccount"));
			return;
		}
		PostUserInfo.CLIsBind(HyFindPwdActivity.this,account,
				new HttpCallback() {
			@Override
			public void onStart() {
				btnok.setBackgroundResource(FindResHelper
						.RDrawable("fq_okbtnshape_gray"));
				progressDialog.show();
				super.onStart();
			}
			@Override
			public void onSuccess(String responseString) {
				btnok.setBackgroundResource(FindResHelper
						.RDrawable("fq_okbtnshape"));
				progressDialog.dismiss();
				try {
							if (!TextUtils.isEmpty((responseString))) {
								JSONObject jsonObject;
									jsonObject = new JSONObject((responseString));
									String ret =JsonParse.HJJsonGetRet(jsonObject);
//									String msg = JsonParse.HJJsonGetMsg(jsonObject);
									String data=JsonParse.HJJsonGetData(jsonObject);
								if (ret.equals("1")) {
									startFind3(account,new JSONObject(data).optString("phone"));
								} else if (ret.equals("-21")) {
									tv_finderror.setVisibility(View.VISIBLE);
								}else {
									startFind2();
									
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

	//         back
	public void setOnKey(final EditText editText) {
		editText.setOnKeyListener(new OnKeyListener() {

			@Override
			public boolean onKey(View arg0, int arg1, KeyEvent arg2) {
				if (!editText.getText().toString()
						.equals(CLNaviteHelper.getRememberPwd())) {
					editText.setText("");
					editText.setOnKeyListener(null);
				}
				return false;
			}
		});
	}

	//         
	public void setTextStandard(final EditText editText) {
		editText.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {

				if (tv_finderror.getVisibility()==View.VISIBLE) {
					tv_finderror.setVisibility(View.INVISIBLE);

					} else {
					
					}

				
		
			}

			@Override
			public void beforeTextChanged(CharSequence s, int arg1, int arg2,
					int arg3) {
			
			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		});
	}

	private void ShowToast(String str) {
		Toast.makeText(HyFindPwdActivity.this, str, Toast.LENGTH_SHORT).show();
	}
	/**
	 *      û   ½  Ϣ(base64λ    )
	 **/
	public void storeAccount(String account, String pwd,String time) {
		CLControlCenter hjControlCenter=CLControlCenter.getInstance();
		//      ο  ʺţ  ȱ  浽   ݿ 
				if (hjControlCenter.isTemp(context)) {
					String tempAccount=hjControlCenter.getTempName(context);
					hjControlCenter.setTempName(context, "");
					Long tsLong = System.currentTimeMillis() / 1100;
					String timez = tsLong.toString();
					storeAccount(tempAccount,tempAccount,timez);
				}
		String enpwd = "";
		try {
		 enpwd=Tools.EncryptAsDoNet(pwd, CLNaviteHelper.getInstance().getSdkKey());
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		UserAccount userAccount = hjAccountDBHelper.new UserAccount();
		userAccount.setUserName(account);
		userAccount.setPassWord(enpwd);
		userAccount.setRembPwd(true);
		userAccount.setAutoLog(false);
		userAccount.setLastAcc(true);
		userAccount.setmTime(time);
		hjAccountDBHelper.open();
		hjAccountDBHelper.updateState(account);
		if (hjAccountDBHelper.isOneExist(account)) {
			hjAccountDBHelper.updateOne(userAccount.getUserName(), userAccount,
					time);
		} else {
			hjAccountDBHelper.insert(userAccount);
		}
		hjAccountDBHelper.close();

	}
//	private String getData(String data){
//		try {
//			return Tools.DecryptDoNet(data, HJNaviteHelper.getKey(HJFindPwdActivity.this));
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		return "";
//		
//	}
//	private void TempLoginSuccess(JSONObject response) {
//		//      û   Ϣ
//		HJAccountCenter.saveLogonInfo(HJFindPwdActivity.this, response);
//		ResponseList rl = JsonParse.parseResponseList(response);
//		rl.setIsLogin(true);
//		onLoginFinished(0, "success", rl);
//		Toast.makeText(HJFindPwdActivity.this, "  ¼ ɹ   ", Toast.LENGTH_SHORT)
//				.show();
//	}
	
}

package com.tomato.fqsdk;

import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tomato.fqsdk.base.BaseActivity;
import com.tomato.fqsdk.control.CLControlCenter;
import com.tomato.fqsdk.control.HySDK;
import com.tomato.fqsdk.data.PostUserInfo;
import com.tomato.fqsdk.fqutils.FLogger;
import com.tomato.fqsdk.data.HyApi.HttpCallback;
import com.tomato.fqsdk.models.CLCommon;
import com.tomato.fqsdk.models.HyLoginResult;
import com.tomato.fqsdk.utils.CLNaviteHelper;
import com.tomato.fqsdk.utils.FindResHelper;
import com.tomato.fqsdk.utils.HJAccountDBHelper;
import com.tomato.fqsdk.utils.JsonParse;
import com.tomato.fqsdk.utils.Tools;
import com.tomato.fqsdk.utils.HJAccountDBHelper.UserAccount;

@SuppressLint("NewApi")
public class HyRegActivity extends BaseActivity implements OnClickListener {
	public static int FQ_LOGIN_TO_REG = 0;
	CLControlCenter mControlCenter;
	HyRegActivity context;
	LinearLayout linRegtypeapp;
	LinearLayout linRegtypephone;
	LinearLayout linRegGetCode;
	private ImageView ivacticon, ivphoneicon, ivactleft, ivphoneleft;
	private TextView tv_act, tv_phone;
	EditText edAccount;
	EditText edPwd;
	TextView txtReg;
	TextView txtAgreement;
	TextView tv_ok;
	CheckBox cbAgreement;
	ImageView imgClose,hj_iv_back;
	LinearLayout btnReg;
	Button btngetcode;
	PopupWindow popupWindow;
	String _pwd;
	String _user_name, _password;
	private String tempAccount;
	private HJAccountDBHelper hjAccountDBHelper;
	public static final String ACCOUNT = "account";
	// ================================
	private RelativeLayout fq_register_step2_mainframe_relativelayout, fq_register_step1_mainframe_relativelayout;
	private LinearLayout fq_reg_step2_okbtn;
	private EditText fq_reg_step2_pwd_inputframe_edt, fq_reg_step2_verifypwd_inputframe_edt;
	private RelativeLayout fq_center_frame_account, fq_center_frame_phone;
	private EditText fq_reg_phonenum_edit, fq_reg_verifycode_edit;
	private Button fq_btn_backtologin_reg;
	private TextView cl_tvreg_regorbind;
	// =============        
	private HashMap<String, String> fq_CachePhoneRegData = new HashMap<String, String>();
	private CountDownTimer timer = new CountDownTimer(60000, 1000) {

		@Override
		public void onTick(long millisUntilFinished) {
			btngetcode.setBackgroundResource(FindResHelper.RDrawable("fq_okbtnshape_gray"));
			btngetcode.setText((millisUntilFinished / 1000) + FindResHelper.RStringStr("hj_toast_regwait"));
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
		super.onCreate(savedInstanceState);
		setContentView(FindResHelper.RLayout("hj_user_reg"));
		hjAccountDBHelper = HJAccountDBHelper.getInstance(HyRegActivity.this);
		mControlCenter = CLControlCenter.getInstance();
		context = this;
		Bundle bundle = new Bundle();
		bundle = getIntent().getExtras();
//		int reg_code = bundle.getInt("code", 0);
		_user_name = bundle.getString("user_name","");
		_password = bundle.getString("password","");
		initView();
	}

	@Override
	public void initView() {
		cl_tvreg_regorbind=(TextView) findViewById(FindResHelper.RId("cl_tvreg_regorbind"));
		fq_reg_step2_pwd_inputframe_edt=  (EditText) findViewById(FindResHelper.RId("fq_reg_step2_pwd_inputframe_edt"));
		fq_reg_step2_verifypwd_inputframe_edt= (EditText) findViewById(FindResHelper.RId("fq_reg_step2_verifypwd_inputframe_edt"));
		fq_reg_phonenum_edit = (EditText) findViewById(FindResHelper.RId("fq_reg_phonenum_edit"));
		fq_reg_verifycode_edit = (EditText) findViewById(FindResHelper.RId("fq_reg_verifycode_edit"));
		fq_center_frame_account = (RelativeLayout) findViewById(FindResHelper.RId("fq_center_frame_account"));
		fq_center_frame_phone = (RelativeLayout) findViewById(FindResHelper.RId("fq_center_frame_phone"));
		fq_register_step1_mainframe_relativelayout = (RelativeLayout) findViewById(
				FindResHelper.RId("fq_register_step1_mainframe_relativelayout"));
		fq_register_step2_mainframe_relativelayout = (RelativeLayout) findViewById(
				FindResHelper.RId("fq_register_step2_mainframe_relativelayout"));
		fq_reg_step2_okbtn = (LinearLayout) findViewById(FindResHelper.RId("fq_reg_step2_okbtn"));
		txtAgreement = (TextView) findViewById(FindResHelper.RId("txt_agreement"));
		imgClose = (ImageView) findViewById(FindResHelper.RId("img_btn_close"));
		hj_iv_back= (ImageView) findViewById(FindResHelper.RId("hj_iv_back"));
		edAccount = (EditText) findViewById(FindResHelper.RId("edt_account"));
		edPwd = (EditText) findViewById(FindResHelper.RId("edt_pwd"));
		btnReg = (LinearLayout) findViewById(FindResHelper.RId("btn_reg"));
		cbAgreement = (CheckBox) findViewById(FindResHelper.RId("cb_user_agreement"));
		linRegtypeapp = (LinearLayout) findViewById(FindResHelper.RId("hj_regtypeapp"));
		linRegtypephone = (LinearLayout) findViewById(FindResHelper.RId("hj_regtypephone"));
		linRegGetCode = (LinearLayout) findViewById(FindResHelper.RId("hj_reggetcodelin_phone"));
		btngetcode = (Button) findViewById(FindResHelper.RId("hj_btn_getcode_phone"));
		// ѡ   ɫ  Сͼ  
		ivacticon = (ImageView) findViewById(FindResHelper.RId("hj_imgacticon"));
		ivphoneicon = (ImageView) findViewById(FindResHelper.RId("hj_imgphoneicon"));
		ivactleft = (ImageView) findViewById(FindResHelper.RId("hj_imgactleft"));
		ivphoneleft = (ImageView) findViewById(FindResHelper.RId("hj_imgphoneleft"));
		tv_act = (TextView) findViewById(FindResHelper.RId("hj_textacttype"));
		tv_phone = (TextView) findViewById(FindResHelper.RId("hj_textphonetype"));
		tv_ok = (TextView) findViewById(FindResHelper.RId("cl_tvreg_regorbind"));
		fq_btn_backtologin_reg=(Button) findViewById(FindResHelper.RId("fq_btn_backtologin_reg"));
		Intent intent = getIntent();
		String type = intent.getStringExtra("regorbind");
		tempAccount = intent.getStringExtra("account");
		if (type.equals("bind")) {
			tv_act.setText(FindResHelper.RStringStr("hj_btn_bindtypeapp"));
			tv_phone.setText(FindResHelper.RStringStr("hj_btn_bindtypephone"));
			tv_ok.setText("绑定");
			btngetcode.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					String mobile = edAccount.getText().toString();
					if (TextUtils.isEmpty(mobile)) {
						ShowToast(FindResHelper.RStringStr("hj_toast_regpleasewritephone"));
						return;
					}
					getCode(mobile, CLCommon.CODETYPE_REGISTER);
				}
			});
		} else {
			btngetcode.setOnClickListener(this);
		}
		hj_iv_back.setOnClickListener(this);
		btnReg.setOnClickListener(this);
		fq_reg_step2_okbtn.setOnClickListener(this);
		txtAgreement.setOnClickListener(this);
		// imgBack.setOnClickListener(this); //      
		imgClose.setOnClickListener(this);
		linRegtypeapp.setOnClickListener(this);
		linRegtypephone.setOnClickListener(this);
		//     ˺ ע  
		edAccount.setText(_user_name);
		edPwd.setText(_password);
		//      Ϸ
		fq_btn_backtologin_reg.setOnClickListener(this);
		
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == FindResHelper.RId("hj_regtypeapp")) {
			cl_tvreg_regorbind.setText("注册并登陆");
			fq_center_frame_phone.setVisibility(View.GONE);
			fq_center_frame_account.setVisibility(View.VISIBLE);
			tv_act.setTextColor(this.getResources().getColor(FindResHelper.RColor("fq_main_orange")));
			tv_phone.setTextColor(this.getResources().getColor(FindResHelper.RColor("fq_main_gray")));
			ivacticon.setImageResource(FindResHelper.RDrawable("fq_accounticon"));
			ivactleft.setImageResource(FindResHelper.RDrawable("fq_rightarrow_orange"));
			ivphoneicon.setImageResource(FindResHelper.RDrawable("fq_phone_gray"));
			ivphoneleft.setImageResource(FindResHelper.RDrawable("fq_rightarrow_gray"));
//			edAccount.setInputType(InputType.TYPE_CLASS_TEXT);
//			edAccount.setText("");
//			edAccount.setHint(FindResHelper.RStringStr("hj_edt_reg_account"));
		}
		if (v.getId() == FindResHelper.RId("hj_regtypephone")) {
			cl_tvreg_regorbind.setText("确认注册");
			fq_center_frame_phone.setVisibility(View.VISIBLE);
			fq_center_frame_account.setVisibility(View.GONE);
			tv_phone.setTextColor(this.getResources().getColor(FindResHelper.RColor("fq_main_orange")));
			tv_act.setTextColor(this.getResources().getColor(FindResHelper.RColor("fq_main_gray")));
			ivacticon.setImageResource(FindResHelper.RDrawable("fq_accountgray"));
			ivactleft.setImageResource(FindResHelper.RDrawable("fq_rightarrow_gray"));
			ivphoneicon.setImageResource(FindResHelper.RDrawable("fq_phone_orange"));
			ivphoneleft.setImageResource(FindResHelper.RDrawable("fq_rightarrow_orange"));
//			edAccount.setInputType(InputType.TYPE_CLASS_PHONE);
//			edAccount.setText("");
//			edPwd.setText("");
//			edAccount.setHint(FindResHelper.RStringStr("hj_edt_reg_phone"));
		}
		if (v.getId() == FindResHelper.RId("txt_agreement")) {
			startActivity(new Intent(HyRegActivity.this, HyAgreementActivity.class));
		}
		if (v.getId() == FindResHelper.RId("hj_btn_getcode_phone")) {
			String mobile = fq_reg_phonenum_edit.getText().toString();
			if (TextUtils.isEmpty(mobile)) {
				ShowToast(FindResHelper.RStringStr("hj_toast_regpleasewritephone"));
				return;
			}
			getCode(mobile, CLCommon.CODETYPE_REGISTER);

		}
		if (v.getId() == FindResHelper.RId("img_btn_close")) {
			startActivity(new Intent(HyRegActivity.this, HyLoginActivity.class));
			finish();
		}
		if (v.getId() == FindResHelper.RId("btn_reg")) {
			String account = edAccount.getText().toString();
			String pwd = edPwd.getText().toString();
			if (account.isEmpty()) {
				ShowToast(FindResHelper.RStringStr("hj_toast_regpleaseac"));
				return;
			}
			if (pwd.isEmpty()) {
				ShowToast(FindResHelper.RStringStr("hj_toast_regpleasepwd"));
				return;
			}
			if (!Tools.IsAccount(account)) {
				ShowToast(FindResHelper.RStringStr("hj_toast_loginnotaccount"));
				return;
			}
			if (fq_center_frame_phone.getVisibility() == View.VISIBLE) {
				if (cbAgreement.isChecked()) {
					String phone_num = fq_reg_phonenum_edit.getText().toString();
					String code = fq_reg_verifycode_edit.getText().toString();

						//  ύע  
//						doTelReg(account, pwd, code);
						//     ȷ         
						fq_GoToVerify(phone_num, code);
					

				} else {
					ShowToast(FindResHelper.RStringStr("hj_toast_regpleaseread"));
				}
			} else {
				if (cbAgreement.isChecked()) {

					if (tempAccount.contains("游客")) {
						doTempBind(tempAccount, account, pwd);
					} else {
						//  ύע  
						doReg(account, pwd);
					}
				} else {
					ShowToast(FindResHelper.RStringStr("hj_toast_regpleaseread"));
				}
			}
		}
		// ȷ      ҳ   ȷ  
		if (v.getId() == FindResHelper.RId("fq_reg_step2_okbtn")) {
			fq_DoTelRealReg();
		}
		//ȷ            
		if (v.getId() == FindResHelper.RId("hj_iv_back")) {
			hj_iv_back.setVisibility(View.GONE);
			fq_register_step1_mainframe_relativelayout.setVisibility(View.VISIBLE);
			fq_register_step2_mainframe_relativelayout.setVisibility(View.GONE);
		}
		// ص   ¼ҳ  
		if (v.getId() == FindResHelper.RId("fq_btn_backtologin_reg")) {
			startActivity(new Intent(HyRegActivity.this, HyLoginActivity.class));
			finish();
		}
	}

	/**
	 *  ֻ ע   ȷ  ע   fsp 2018  9  6  
	 */
	private void fq_DoTelRealReg() {
		if (!TextUtils.equals(fq_reg_step2_pwd_inputframe_edt.getText().toString(),
				fq_reg_step2_verifypwd_inputframe_edt.getText().toString())) {
			HySDK.HyToast(FindResHelper.RStringStr("hj_toast_writerightcode"));
			return;
		}

		doTelReg(fq_CachePhoneRegData.get("phone_num"), fq_reg_step2_pwd_inputframe_edt.getText().toString(),
				fq_CachePhoneRegData.get("verify_code"));
	}


	private void fq_GoToVerify(final String phone_num, final String verify_code) {

		PostUserInfo.fq_PhoneVerifyCode(context, "1", phone_num, verify_code, new HttpCallback() {

			@Override
			public void onSuccess(String data) {
				btnReg.setBackgroundResource(FindResHelper.RDrawable("fq_okbtnshape"));
				progressDialog.dismiss();
				hj_iv_back.setVisibility(View.VISIBLE);
				fq_register_step1_mainframe_relativelayout.setVisibility(View.GONE);
				fq_register_step2_mainframe_relativelayout.setVisibility(View.VISIBLE);
				fq_CachePhoneRegData.put("phone_num", phone_num);
				fq_CachePhoneRegData.put("verify_code", verify_code);
				JSONObject jsonObject;
				try {
					jsonObject = new JSONObject(data);
					String ret = JsonParse.HJJsonGetRet(jsonObject);
					if (!ret.equals("1")) {
						ShowToast("验证码错误请重试");
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}

			@Override
			public void onStart() {
				// TODO Auto-generated method stub
				super.onStart();
				btnReg.setBackgroundResource(FindResHelper.RDrawable("fq_okbtnshape_gray"));
				progressDialog.show();
			}

			@Override
			public void onError(String msg) {
				// TODO Auto-generated method stub
				super.onError(msg);
				btnReg.setBackgroundResource(FindResHelper.RDrawable("fq_okbtnshape"));
				progressDialog.dismiss();
			}
		});

	}


	private void getCode(final String mobile, final String type) {

		PostUserInfo.CLGetCode(HyRegActivity.this, mobile, type, new HttpCallback() {
			@Override
			public void onStart() {
				btnReg.setBackgroundResource(FindResHelper.RDrawable("fq_okbtnshape_gray"));
				progressDialog.show();
				super.onStart();
			}

			@Override
			public void onSuccess(String responseString) {
				btnReg.setBackgroundResource(FindResHelper.RDrawable("fq_okbtnshape"));
				progressDialog.dismiss();
				try {

					if (!TextUtils.isEmpty(responseString)) {
						JSONObject jsonObject;
						jsonObject = new JSONObject(responseString);
						String ret = JsonParse.HJJsonGetRet(jsonObject);
						String msg = JsonParse.HJJsonGetMsg(jsonObject);
//									String data=JsonParse.HJJsonGetData(jsonObject);
						if (ret.equals("1")) {
//									String	code1 = new JSONObject(data).optString("verify_code");
//									HyRegActivity.this.code=code1;
//									phone=mobile;
							ShowToast(msg);
							timer.start();
						} else {
							ShowToast(msg);

						}
					} else {
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
	 * 
	 * 
	 * @param account
	 * @param account2
	 * @param pwd
	 */
	private void doTempBindTel(String Tempaccount, final String mobile, String pwd, String code) {
		_pwd = pwd;
		if (TextUtils.isEmpty(Tempaccount) || TextUtils.isEmpty(mobile) || TextUtils.isEmpty(pwd)) {
			ShowToast(FindResHelper.RStringStr("hj_toast_regtempbindnotempty"));
			return;
		}
		if (TextUtils.isEmpty(code)) {
			ShowToast(FindResHelper.RStringStr("hj_toast_regpleasewritecode"));
			return;
		}
//		if (!phone.equals(mobile)||!(this.code).equals(code)) {
//			ShowToast(FindResHelper.RStringStr("hj_toast_writerightcode"));
//			return;
//		}
		PostUserInfo.CLTempBindPhone(HyRegActivity.this, mobile, pwd, Tempaccount, "3", code, new HttpCallback() {
			@Override
			public void onStart() {
				btnReg.setBackgroundResource(FindResHelper.RDrawable("fq_okbtnshape_gray"));
				progressDialog.show();
				super.onStart();
			}

			@Override
			public void onSuccess(String responseString) {
				btnReg.setBackgroundResource(FindResHelper.RDrawable("fq_okbtnshape"));
				progressDialog.dismiss();
				try {
					if (!TextUtils.isEmpty(responseString)) {
						JSONObject jsonObject;
						jsonObject = new JSONObject(responseString);
						String ret = JsonParse.HJJsonGetRet(jsonObject);
						String msg = JsonParse.HJJsonGetMsg(jsonObject);
						String data = JsonParse.HJJsonGetData(jsonObject);
						if (ret.equals("1")) {
							//  󶨳ɹ 
							Bind(mobile, new JSONObject(data));
						} else {
							ShowToast(msg);
						}
					} else {
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
	 * 
	 * 
	 * @param account
	 * @param account2
	 * @param pwd
	 */
	private void doTempBind(String Tempaccount, final String Newaccount, String pwd) {
		_pwd = pwd;
		if (TextUtils.isEmpty(Tempaccount) || TextUtils.isEmpty(Newaccount) || TextUtils.isEmpty(pwd)) {
			ShowToast(FindResHelper.RStringStr("hj_toast_regtempbindnotempty"));
			return;
		}
		PostUserInfo.CLTempBind(HyRegActivity.this, Newaccount, pwd, Tempaccount, "1", new HttpCallback() {

			@Override
			public void onStart() {
				btnReg.setBackgroundResource(FindResHelper.RDrawable("fq_okbtnshape_gray"));
				progressDialog.show();
				super.onStart();
			}

			@Override
			public void onSuccess(String responseString) {
				btnReg.setBackgroundResource(FindResHelper.RDrawable("fq_okbtnshape"));
				progressDialog.dismiss();
				try {
					if (!TextUtils.isEmpty(responseString)) {
						JSONObject jsonObject;
						jsonObject = new JSONObject(responseString);
						String ret = JsonParse.HJJsonGetRet(jsonObject);
						String msg = JsonParse.HJJsonGetMsg(jsonObject);
						String data = JsonParse.HJJsonGetData(jsonObject);
						if (ret.equals("1")) {
							//  󶨳ɹ 
							Bind(Newaccount, new JSONObject(data));
						} else {
							ShowToast(msg);
						}
					} else {
						ShowToast(FindResHelper.RStringStr("hj_toast_paychecknetwork"));
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		});
	}

	// ע  
	private void doReg(final String account, String pwd) {
		_pwd = pwd;
		if (TextUtils.isEmpty(account) || TextUtils.isEmpty(pwd)) {
			ShowToast(FindResHelper.RStringStr("hj_toast_regnotempty"));
			return;
		}
		if (!Tools.IsAccount(account)) {
			ShowToast(FindResHelper.RStringStr("hj_toast_loginnotaccount"));
			return;
		}
		PostUserInfo.CLRegister(HyRegActivity.this, "1", account, pwd, "1", new HttpCallback() {
			@Override
			public void onStart() {
				btnReg.setBackgroundResource(FindResHelper.RDrawable("fq_okbtnshape_gray"));
				progressDialog.show();
				super.onStart();
			}

			@Override
			public void onSuccess(String responseString) {
				btnReg.setBackgroundResource(FindResHelper.RDrawable("fq_okbtnshape"));
				progressDialog.dismiss();
				try {
					if (!TextUtils.isEmpty(responseString)) {
						JSONObject jsonObject;
						jsonObject = new JSONObject(responseString);
						String ret = JsonParse.HJJsonGetRet(jsonObject);
						String msg = JsonParse.HJJsonGetMsg(jsonObject);
						String data = JsonParse.HJJsonGetData(jsonObject);
						if (ret.equals("1")) {
							Reg(account.toLowerCase(), data);
						} else {
							ShowToast(msg);
						}
					} else {
						ShowToast(FindResHelper.RStringStr("hj_toast_paychecknetwork"));
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});

	}

	//  ֻ       ʽע  
	private void doTelReg(final String mobile, String pwd, String code) {
		_pwd = pwd;
		if (TextUtils.isEmpty(mobile) || TextUtils.isEmpty(pwd)) {
			ShowToast(FindResHelper.RStringStr("hj_toast_regnotempty"));
			return;
		}
		if (TextUtils.isEmpty(code)) {
			ShowToast(FindResHelper.RStringStr("hj_toast_regpleasewritecode"));
			return;
		}
//		if (!phone.equals(mobile)||!(this.code).equals(code)) {
//			ShowToast(FindResHelper.RStringStr("hj_toast_writerightcode"));
//			return;
//		}
		PostUserInfo.CLPhoneRegister(HyRegActivity.this, "1", mobile, pwd, "3", code, new HttpCallback() {

			@Override
			public void onStart() {
				btnReg.setBackgroundResource(FindResHelper.RDrawable("fq_okbtnshape_gray"));
				progressDialog.show();
				super.onStart();
			}

			@Override
			public void onSuccess(String responseString) {
				btnReg.setBackgroundResource(FindResHelper.RDrawable("fq_okbtnshape"));
				progressDialog.dismiss();
				try {
					if (!TextUtils.isEmpty(responseString)) {
						JSONObject jsonObject;
						jsonObject = new JSONObject(responseString);
						String ret = JsonParse.HJJsonGetRet(jsonObject);
						String msg = JsonParse.HJJsonGetMsg(jsonObject);
						String data = JsonParse.HJJsonGetData(jsonObject);
						if (ret.equals("1")) {
							Reg(mobile, data);
						} else {
							ShowToast(msg);
						}
					} else {
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
	 *      û   ½  Ϣ
	 **/
	public void storeAccount(String account, String pwd, String time) {
		//       ο  ʺţ  ȱ  浽   ݿ 
//		if (mControlCenter.isTemp(context)) {
//			String tempAccount = mControlCenter.getTempName(context);
//			mControlCenter.setTempName(context, "");
//			Long tsLong = System.currentTimeMillis() / 1100;
//			String timez = tsLong.toString();
//			storeAccount(tempAccount, tempAccount, timez);
//		}
		String enpwd = "";
		try {
			enpwd = Tools.EncryptAsDoNet(pwd, CLNaviteHelper.getInstance().getSdkKey());
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
			hjAccountDBHelper.updateOne(userAccount.getUserName(), userAccount, time);
		} else {
			hjAccountDBHelper.insert(userAccount);
		}
		hjAccountDBHelper.close();
	}

	private void ShowToast(String str) {
		Toast.makeText(HyRegActivity.this, str, Toast.LENGTH_SHORT).show();
	}

	private void Bind(String newAccount, JSONObject response) {
		Long tsLong = System.currentTimeMillis() / 1000;
		String timez = tsLong.toString();
		//      û   Ϣ
		storeAccount(newAccount, _pwd, timez);
		//    浽  ʱ  Ϣ.
		mControlCenter.setCurrentName(newAccount);
//		hjControlCenter.setCurrentName(HJRegActivity.this, Newaccount);
		HyLoginResult rl = JsonParse.parseResponseList(response);
		rl.setBehavior(CLCommon.REGISTER);
		//     ο  ʺ     
		removeAccount(tempAccount);
		onLoginFinished(0, rl);
		ShowToast(FindResHelper.RStringStr("hj_toast_regbindsuccess"));
	}

	private void Reg(String account, String response) throws JSONException {
		//      û   Ϣ
		storeAccount(account, _pwd, new Long(System.currentTimeMillis() / 1000).toString());
		//    浽  ʱ  Ϣ
		mControlCenter.setCurrentName(account);
		HyLoginResult rl = JsonParse.parseResponseList(new JSONObject(response));
		rl.setBehavior(CLCommon.REGISTER);
		onLoginFinished(0, rl);
//		ShowToast(FindResHelper.RStringStr("hj_toast_regregsuccess"));
	}

	/**
	 *  Ƴ ָ   û   Ϣ
	 **/
	public void removeAccount(String username) {
		hjAccountDBHelper.open();
		hjAccountDBHelper.deleteOne(username);
		hjAccountDBHelper.close();
	}

	@Override
	public void onBackPressed() {
		startActivity(new Intent(HyRegActivity.this, HyLoginActivity.class));
		finish();
		super.onBackPressed();
	}
}

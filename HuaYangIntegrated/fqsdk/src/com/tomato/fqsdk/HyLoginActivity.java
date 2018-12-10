package com.tomato.fqsdk;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.Selection;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.tomato.fqsdk.adapter.HJAccountAdapter;
import com.tomato.fqsdk.base.BaseActivity;
import com.tomato.fqsdk.control.CLControlCenter;
import com.tomato.fqsdk.control.HySDK;
import com.tomato.fqsdk.data.PostUserInfo;
import com.tomato.fqsdk.data.HyApi.HttpCallback;
import com.tomato.fqsdk.fqutils.FLogger;
import com.tomato.fqsdk.models.CLCommon;
import com.tomato.fqsdk.models.HyLoginResult;
import com.tomato.fqsdk.utils.CLNaviteHelper;
import com.tomato.fqsdk.utils.FindResHelper;
import com.tomato.fqsdk.utils.HJAccountDBHelper;
import com.tomato.fqsdk.utils.JsonParse;
import com.tomato.fqsdk.utils.Tools;
import com.tomato.fqsdk.utils.HJAccountDBHelper.UserAccount;

public class HyLoginActivity extends BaseActivity implements OnClickListener {

	CLControlCenter mControlCenter;
	HyLoginActivity context;
	//      
	EditText edAccount, edPwd;
	//   ť
	LinearLayout btnReg, btnLogin, btnTempChange, btnTempReg, tvBindPhone,
			tvFindPwd;
	ImageView imgClose, imgaccList;
	String _pwd = "";
	PopupWindow popupWindow;
	private HJAccountDBHelper hjAccountDBHelper;
	private List<String> mUserNames = new ArrayList<String>();
	private HJAccountAdapter mOptionsAdapter;
	private View layout_option;
	private PopupWindow selectPopupWindow;
	private int mCount = 0;//  ʺ         ʼΪ0
	private UserAccount userAccount;
	private InputMethodManager inputMethodManager;
	private static final int CLOSEINPUT = 1;
	//  Ƿ    л  ʺ 
	private boolean ISCHANGE = false;
	public static int LOGINACTIVITY = 0;

	class InputHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case CLOSEINPUT:
				if (mCount > 0) {
					uploadOptionPop(true);
				}
				break;

			default:
				break;
			}
			super.handleMessage(msg);
		}
	}

	private InputHandler mHandler = new InputHandler();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LOGINACTIVITY = 1;
		hjAccountDBHelper = HJAccountDBHelper.getInstance(HyLoginActivity.this);
		mControlCenter = CLControlCenter.getInstance();
		context = this;
		inputMethodManager = (InputMethodManager) this
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		ISCHANGE = getIntent().getBooleanExtra("ISCHANGE", false);
		initView();

	}

	@Override
	public void initView() {
		//      ݿ     Ϣ
		mCount = restoreAccounts();
		userAccount = restoreLastAccount();

		GeneralLogin();

		btnReg = (LinearLayout) findViewById(FindResHelper.RId("btn_reg"));

		btnReg.setOnClickListener(this);

	}

	/**
	 *    ε ¼   ͨ  ¼
	 */
	private void GeneralLogin() {
		setContentView(FindResHelper.RLayout("hj_user_login_not_temp"));
		edAccount = (EditText) findViewById(FindResHelper.RId("edt_account"));
		edPwd = (EditText) findViewById(FindResHelper.RId("edt_pwd"));
		imgaccList = (ImageView) findViewById(FindResHelper
				.RId("iv_accountList"));
		tvBindPhone = (LinearLayout) findViewById(FindResHelper
				.RId("btn_bindphone"));
		tvFindPwd = (LinearLayout) findViewById(FindResHelper
				.RId("btn_findpwd"));
		btnLogin = (LinearLayout) findViewById(FindResHelper.RId("btn_login"));
		btnLogin.setOnClickListener(this);
		tvBindPhone.setOnClickListener(this);
		tvFindPwd.setOnClickListener(this);
		edPwd.setTypeface(Typeface.SANS_SERIF);
		//       ʺž   ʾ    
		if (mCount > 0 && userAccount != null) {
			edAccount.setText(userAccount.getUserName());
			edPwd.setText(CLNaviteHelper.getRememberPwd());
		}
		if (edPwd.getText().toString().equals(CLNaviteHelper.getRememberPwd())) {
			setOnKey(edPwd);
			setTextStandard(edPwd);
			setAccountLinstener(edAccount);
		}
		imgaccList.setOnClickListener(this);

		if (mCount > 0) {
			imgaccList.setVisibility(View.VISIBLE);
			imgaccList.setOnClickListener(this);
		} else {
			imgaccList.setVisibility(Button.INVISIBLE);
		}
		// PopupWindow         򲼾 
		layout_option = (View) this.getLayoutInflater().inflate(
				FindResHelper.RLayout("hj_accountlistlayout"), null);
		ListView listView = (ListView) layout_option.findViewById(FindResHelper
				.RId("layout_options_list"));

		//      Զ   Adapter
		mOptionsAdapter = new HJAccountAdapter(HyLoginActivity.this, mUserNames);
		mOptionsAdapter.setOnClicked(onItemClicked);

		listView.setAdapter(mOptionsAdapter);
		imgClose = (ImageView) findViewById(FindResHelper.RId("img_btn_close"));
		imgClose.setOnClickListener(this);
	}

	/**
	 *   ť ¼ 
	 */
	@Override
	public void onClick(View view) {
		//  ο ע  
		if (view.getId() == FindResHelper.RId("btn_fast_reg")) {
			// doTempReg();
		}
		//   ʽע  
		if (view.getId() == FindResHelper.RId("btn_reg")) {
			gotoRegActivity();
//			try {
//				Intent intent = new Intent(HyLoginActivity.this,
//						HyRegActivity.class);
//				intent.putExtra("regorbind", "reg");
//				intent.putExtra("account", "");
//				startActivity(intent);
//				finish();
//			} catch (Exception e) {
//				Intent intent = new Intent(HyLoginActivity.this,
//						HyRegActivity.class);
//				intent.putExtra("regorbind", "reg");
//				intent.putExtra("account", "");
//				startActivity(intent);
//				finish();
//			}

		}
		//  л  ʺ 
		if (view.getId() == FindResHelper.RId("btn_change")) {
			Intent intent = new Intent(HyLoginActivity.this,
					HyLoginActivity.class);
			intent.putExtra("ISCHANGE", true);
			startActivity(intent);
			this.finish();
		}
		//     ҳ     ¼ҳ  
		if (view.getId() == FindResHelper.RId("btn_tologin")) {
			Intent intent = new Intent(HyLoginActivity.this,
					HyLoginActivity.class);
			intent.putExtra("ISGUIDE", false);
			startActivity(intent);
			this.finish();
		}
		//   ¼
		if (view.getId() == FindResHelper.RId("btn_login")) {
			//  ο͵ ¼
			// if (!ISCHANGE&&hjControlCenter.isTemp(context)&&!(mCount > 0 &&
			// userAccount != null)) {
			// if (
			// || ISTEMP) {
			// doTempLogin(hjControlCenter.getTempName(context));
			// }
			// }
			// else {

			String account = edAccount.getText().toString();
			if (account.isEmpty()) {
				ShowToast(FindResHelper
						.RStringStr("hj_toast_loginpleasepressaccount"));
				return;
			}
			//   ¼
			else {
				_pwd = edPwd.getText().toString();
				if (_pwd.isEmpty()) {
					ShowToast(FindResHelper.RStringStr("hj_pleasepresspwd"));
					return;
				}
				doLogin(1, account, _pwd);

			}
		}
		// }
		//  ر ҳ  
		if (view.getId() == FindResHelper.RId("img_btn_close")) {
			if (ISCHANGE) {
				Intent intent = new Intent(HyLoginActivity.this,
						HyLoginActivity.class);
				startActivity(intent);
				this.finish();
			} else if (mCount == 0 && !mControlCenter.isTemp(context)) {
				Intent intent = new Intent(HyLoginActivity.this,
						HyLoginActivity.class);
				intent.putExtra("ISGUIDE", true);
				startActivity(intent);
				this.finish();
			} else {
				//  ¼ȡ  
				HyLoginResult rl = HyLoginResult.getInstance();
				onLoginFinished(1, rl);
			}

		}
		//    ʺŲ˵ 
		if (view.getId() == FindResHelper.RId("iv_accountList")) {
			inputMethodManager
					.hideSoftInputFromWindow(view.getWindowToken(), 0);
			Message msg = new Message();
			msg.what = CLOSEINPUT;
			mHandler.sendMessageDelayed(msg, 250);
		}
		//    ڰ  а  ֻ 
		if (view.getId() == FindResHelper.RId("btn_bindphone")) {
			doIsBind(edAccount.getText().toString(), edPwd.getText().toString());
		}
		//  һ     
		if (view.getId() == FindResHelper.RId("btn_findpwd")) {
			Intent intent = new Intent(HyLoginActivity.this,
					HyFindPwdActivity.class);
			Bundle bundle = new Bundle();
			bundle.putInt("findpwd", 1);
			bundle.putString("ACCOUNT", edAccount.getText().toString());
			intent.putExtras(bundle);
			startActivity(intent);
			finish();
		}

	}
	/**
	 * 去注册页面
	 */
	private void gotoRegActivity() {
		PostUserInfo.CLRandomRegister(HyLoginActivity.this,
				new HttpCallback() {
					@Override
					public void onStart() {
//						btnReg.setBackgroundResource(FindResHelper
//								.RDrawable("fq_okbtnshape_gray"));
						progressDialog.show();
						super.onStart();
					}
					@Override
					public void onError(String msg) {
						// TODO Auto-generated method stub
						super.onError(msg);
						Toast.makeText(HySDK.context, "网络开小差了，请稍后再试", Toast.LENGTH_SHORT).show();
						progressDialog.dismiss();
					}
					@Override
					public void onSuccess(String responseString) {
//						btnReg.setBackgroundResource(FindResHelper
//								.RDrawable("fq_okbtnshape"));
						FLogger.e(responseString);
						progressDialog.dismiss();
						try {
							if (!TextUtils.isEmpty(responseString)) {
								JSONObject jsonObject;
								FLogger.d(responseString);
									jsonObject = new JSONObject(responseString);
									
									String ret =JsonParse.HJJsonGetRet(jsonObject);
									String msg = JsonParse.HJJsonGetMsg(jsonObject);
									String data=JsonParse.HJJsonGetData(jsonObject);
									JSONObject jsondata=new JSONObject(data);
									String password=jsondata.optString("password");
									String user_name=jsondata.optString("user_name");
									Intent intent = new Intent(
											HyLoginActivity.this,
											HyRegActivity.class);
									intent.putExtra("regorbind", "reg");
									intent.putExtra("account", "");
									Bundle bundle = new Bundle();
									bundle.putString("user_name", user_name);
									bundle.putString("password", password);
									bundle.putInt("code",
											HyRegActivity.FQ_LOGIN_TO_REG);
									intent.putExtras(bundle);
									startActivity(intent);
									finish();
								if (ret.equals("1")) {
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
	 *  Ƿ ֮ǰ 󶨹 
	 */
	private void doIsBind(final String account, String pwd) {
		// Intent intent = new Intent(HJLoginActivity.this,
		// HJBindPhoneActivity.class);
		// if (TextUtils.isEmpty(account) || TextUtils.isEmpty(pwd)) {
		// Bundle bundle = new Bundle();
		// bundle.putString("ACCOUNT", edAccount.getText().toString());
		// bundle.putInt("BIND", HJBindPhoneActivity.UNBOUNDED);
		// intent.putExtras(bundle);
		// startActivity(intent);
		// finish();
		// return ;
		// }
		if (pwd.equals(CLNaviteHelper.getRememberPwd())) {
			// NATIVE
			try {
				pwd = Tools.DecryptDoNet(userAccount.getPassWord(),
						CLNaviteHelper.getInstance().getSdkKey());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		_pwd = pwd;
		PostUserInfo.CLIsBind(HyLoginActivity.this, account,
				new HttpCallback() {
					@Override
					public void onStart() {
						progressDialog.show();
						super.onStart();
					}
					@Override
					public void onSuccess(String responseString) {
						progressDialog.dismiss();
						FLogger.e(responseString);
						try {
							if (!TextUtils.isEmpty(responseString)) {
								JSONObject jsonObject;
								jsonObject = new JSONObject(responseString);
								String code = JsonParse
										.HJJsonGetRet(jsonObject);
								// String msg =
								// JsonParse.HJJsonGetMsg(jsonObject);
								String data = JsonParse
										.HJJsonGetData(jsonObject);
								Intent intent = new Intent(
										HyLoginActivity.this,
										HyBindPhoneActivity.class);
								if (code.equals("1")) {
									Bundle bundle = new Bundle();
									bundle.putInt("BIND",
											HyBindPhoneActivity.BINDING);
									bundle.putString("TEL",
											new JSONObject(data)
													.optString("phone"));
									bundle.putString("ACCOUNT", account);
									bundle.putString("PWD", _pwd);
									intent.putExtras(bundle);
									startActivity(intent);
									finish();
								} else {
									Bundle bundle = new Bundle();
									bundle.putString("ACCOUNT", edAccount
											.getText().toString());
									bundle.putInt("BIND",
											HyBindPhoneActivity.UNBOUNDED);
									intent.putExtras(bundle);
									startActivity(intent);
									finish();
								}
							} else {
								ShowToast(FindResHelper
										.RStringStr("hj_toast_paychecknetwork"));
							}
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				});

	}

	// /**
	// *  ο͵ ¼
	// */
	// private void doTempLogin(String account) {
	// PostUserInfo.HJNEWlogin(HyLoginActivity.this, "0",account,account,
	// new TextHttpResponseHandler() {
	// public void onStart() {
	// btnLogin.setBackgroundResource(FindResHelper
	// .RDrawable("fq_okbtnshape_gray"));
	// progressDialog.show();
	// super.onStart();
	// }
	//
	// public void onFinish() {
	// if (mCount > 0 && userAccount != null) {
	// btnLogin.setBackgroundResource(FindResHelper
	// .RDrawable("fq_okbtnshape"));
	// }else {
	// btnLogin.setBackgroundResource(FindResHelper
	// .RColor("hj_fastlogin_color"));
	// }
	//
	// progressDialog.dismiss();
	// super.onFinish();
	// }
	//
	// public void onSuccess(int statusCode, Header[] headers,
	// String responseString) {
	// try {
	// if (!TextUtils.isEmpty(responseString)) {
	// JSONObject jsonObject;
	// jsonObject = new JSONObject(responseString);
	// String code =JsonParse.HJJsonGetRet(jsonObject);
	// String msg = JsonParse.HJJsonGetMsg(jsonObject);
	// String data=JsonParse.HJJsonGetData(jsonObject);
	// if (code.equals("1")) {
	// //   ¼ ɹ 
	// TempLoginSuccess(CLCommon.LOGIN,new JSONObject(data));
	// } else if (code.equals("-13")) {
	// ShowToast("   οͺ  Ѱ󶨣    ð󶨵  ʺŵ ¼");
	// }else{
	// ShowToast(msg);
	// }
	// }else {
	// ShowToast(FindResHelper.RStringStr("hj_toast_paychecknetwork"));
	// }
	// } catch (Exception e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	// }
	//
	// public void onFailure(int statusCode,
	// Header[] paramArrayOfHeader, String paramString,
	// Throwable paramThrowable) {
	//
	// }
	// });
	//
	// }

	/**
	 *  û   ¼
	 */
	private void doLogin(int user_type, final String account, String pwd) {
		if (TextUtils.isEmpty(account) || TextUtils.isEmpty(pwd)) {
			ShowToast(FindResHelper.RStringStr("hj_toast_loginnotempty"));
			return;
		}

		// if (!edAccount.isEnabled()) {
		// //      ο  ʺž  ο͵ ¼
		// if (account.contains(" ο ")) {
		// doTempLogin(account);
		// return;
		// }
		// }
		if (!Tools.IsAccount(account)) {
			ShowToast(FindResHelper.RStringStr("hj_toast_loginnotaccount"));
			return;
		}

		if (pwd.equals(CLNaviteHelper.getRememberPwd())) {
			// NATIVE
			// pwd = new
			// String(Base64.decode(userAccount.getPassWord().getBytes(), 0));
			try {
				pwd = Tools.DecryptDoNet(userAccount.getPassWord(),
						CLNaviteHelper.getInstance().getSdkKey());
			} catch (Exception e) {
				pwd = "";
			}
		}
		_pwd = pwd;

		PostUserInfo.CLLogin(HyLoginActivity.this, user_type + "", account,
				pwd, new HttpCallback() {
			@Override
			public void onStart() {
				progressDialog.show();
				btnLogin.setBackgroundResource(FindResHelper
						.RDrawable("fq_okbtnshape_gray"));
				super.onStart();
			}
			public void onError(String msg) {
				progressDialog.dismiss();
			};
			@Override
			public void onSuccess(String responseString) {
				progressDialog.dismiss();
				btnLogin.setBackgroundResource(FindResHelper
						.RDrawable("fq_okbtnshape"));
						try {
							if (!TextUtils.isEmpty(responseString)) {
								JSONObject jsonObject;
								jsonObject = new JSONObject(responseString);
								String ret = JsonParse.HJJsonGetRet(jsonObject);
								String msg = JsonParse.HJJsonGetMsg(jsonObject);
								String data = JsonParse
										.HJJsonGetData(jsonObject);
								if (ret.equals("1")) {
									//      û   Ϣ
									storeAccount(account.toLowerCase(), _pwd,
											new Long(System.currentTimeMillis() / 1000).toString());
									//    浽  ʱ  Ϣ
									mControlCenter.setCurrentName(account);
									//  ص   Ϣ
									HyLoginResult rl = JsonParse
											.parseResponseList(new JSONObject(
													data));
									rl.setBehavior(CLCommon.LOGIN);
									//   ¼ ɹ 
									onLoginFinished(0, rl);
									// LoginToast(FindResHelper
									// .RStringStr("hj_toast_loginsuccess"));
//									LoginToast("  ¼ ɹ   ");
								} else if (ret.equals("11")) {
									ShowToast(FindResHelper
											.RStringStr("hj_btn_login_faile"));
								} else {
									edPwd.setText("");
									ShowToast(msg);
								}
							} else {
								ShowToast(FindResHelper
										.RStringStr("hj_toast_paychecknetwork"));
							}
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
				
				
				
				
//				new TextHttpResponseHandler() {
//					public void onStart() {
//						btnLogin.setBackgroundResource(FindResHelper
//								.RDrawable("fq_okbtnshape_gray"));
//						progressDialog.show();
//						super.onStart();
//					}
//
//					public void onFinish() {
//						btnLogin.setBackgroundResource(FindResHelper
//								.RDrawable("fq_okbtnshape"));
//						progressDialog.dismiss();
//						super.onFinish();
//					}
//
//					public void onSuccess(int statusCode, Header[] headers,
//							String responseString) {
//						try {
//							if (!TextUtils.isEmpty(responseString)) {
//								JSONObject jsonObject;
//								jsonObject = new JSONObject(responseString);
//								String ret = JsonParse.HJJsonGetRet(jsonObject);
//								String msg = JsonParse.HJJsonGetMsg(jsonObject);
//								String data = JsonParse
//										.HJJsonGetData(jsonObject);
//								if (ret.equals("1")) {
//									Long tsLong = System.currentTimeMillis() / 1000;
//									String timez = tsLong.toString();
//									//      û   Ϣ
//									storeAccount(account.toLowerCase(), _pwd,
//											timez);
//									// //    浽  ʱ  Ϣ
//									HJControlCenter.CurrentName = account;
//									// hjControlCenter.setCurrentName(HJLoginActivity.this,
//									// account);
//									//  ص   Ϣ
//									HyLoginResult rl = JsonParse
//											.parseResponseList(new JSONObject(
//													data));
//									rl.setBehavior(HJCommon.LOGIN);
//									//   ¼ ɹ 
//									onLoginFinished(0, rl);
//									// LoginToast(FindResHelper
//									// .RStringStr("hj_toast_loginsuccess"));
//									LoginToast("  ¼ ɹ   ");
//								} else if (ret.equals("11")) {
//									ShowToast(FindResHelper
//											.RStringStr("hj_btn_login_faile"));
//								} else {
//									edPwd.setText("");
//									ShowToast(msg);
//								}
//							} else {
//								ShowToast(FindResHelper
//										.RStringStr("hj_toast_paychecknetwork"));
//							}
//						} catch (Exception e) {
//							// TODO Auto-generated catch block
//							e.printStackTrace();
//						}
//					}
//
//					public void onFailure(int statusCode,
//							Header[] paramArrayOfHeader, String paramString,
//							Throwable paramThrowable) {
//
//					}
//				}
//		
				
				);

	}

	// /**
	// *  ο ע  
	// */
	// private void doTempReg() {
	// PostUserInfo.HJNEWregister(HJLoginActivity.this,"0","","","1",
	// new TextHttpResponseHandler() {
	// public void onStart() {
	// progressDialog.show();
	// super.onStart();
	// }
	//
	// public void onFinish() {
	// progressDialog.dismiss();
	// super.onFinish();
	// }
	//
	// public void onSuccess(int statusCode, Header[] headers,
	// String responseString) {
	// try {
	// if (!TextUtils.isEmpty(responseString)) {
	// JSONObject jsonObject;
	// jsonObject = new JSONObject(responseString);
	// String ret =JsonParse.HJJsonGetRet(jsonObject);
	// String msg = JsonParse.HJJsonGetMsg(jsonObject);
	// String data=JsonParse.HJJsonGetData(jsonObject);
	// if (ret.equals("1")) {
	// //   ¼ ɹ 
	// TempLoginSuccess(HJCommon.REGISTER,new JSONObject(data));
	// } else {
	// ShowToast(msg);
	// }
	// }else {
	// ShowToast(FindResHelper.RStringStr("hj_toast_paychecknetwork"));
	// }
	// } catch (Exception e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	// }
	//
	// public void onFailure(int statusCode,
	// Header[] paramArrayOfHeader, String paramString,
	// Throwable paramThrowable) {
	//
	// }
	// });
	//
	// }

	private void setAccountLinstener(final EditText editText) {
		editText.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
				hjAccountDBHelper.open();
				if (hjAccountDBHelper.isOneExist(editText.getText().toString())) {
					edPwd.setText(CLNaviteHelper.getRememberPwd());
					setTextStandard(edPwd);
					mOptionsAdapter.notifyDataSetChanged();
				} else {
					edPwd.setText("");
				}
				hjAccountDBHelper.close();
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable arg0) {
				// TODO Auto-generated method stub

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
			// int maxLen = 0;
			Editable editable = editText.getText();
			int len = editable.length();
			boolean isdefault = false;

			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {

				if (isdefault) {

					if (editText.getText().toString()
							.contains(CLNaviteHelper.getRememberPwd())) {
						editText.setOnKeyListener(null);
						int selEndIndex = Selection.getSelectionEnd(editable);
						String str = editable.toString();
						if (len < editable.toString().length()) {

							String text = "";
							if (editable.length() > 15) {
								//   ȡ   ַ   
								text = str.substring(editable.length() - 1,
										editable.length());
							}
							editText.setText(text);
							editable = editText.getText();
						}
						//    ַ    ĳ   
						int newLen = editable.length();
						//  ɹ  λ ó    ַ       
						if (selEndIndex > newLen) {
							selEndIndex = editable.length();
						}
						//      ¹     ڵ λ  
						Selection.setSelection(editable, selEndIndex);
						isdefault = false;
					} else {
						if (arg0.length() > 15) {
							editText.setOnKeyListener(null);
							editText.setText("");
						}
					}

				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int arg1, int arg2,
					int arg3) {
				len = editable.length();
				if (s.toString().equals(CLNaviteHelper.getRememberPwd())) {
					isdefault = true;

				}
			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		});
	}

	private HJAccountAdapter.OnClicked onItemClicked = new HJAccountAdapter.OnClicked() {

		@Override
		public void onItemSelected(int index) {
			// TODO Auto-generated method stub
			updateState(mUserNames.get(index));
			uploadOptionPop(false);
			if (edPwd.getText().toString()
					.equals(CLNaviteHelper.getRememberPwd())) {
				setOnKey(edPwd);
				setTextStandard(edPwd);
			}
		}

		@Override
		public void onItemDelete(int index) {
			// TODO Auto-generated method stub
			if (mUserNames.size() > 0) {
				if (edAccount.getText().toString()
						.equals(mUserNames.get(index).toString())) {
					edAccount.setText("");
					edPwd.setText("");
				}
				removeAccount(mUserNames.get(index));
				mUserNames.remove(index);
				// ˢ       б 
				mOptionsAdapter.notifyDataSetChanged();
			}
			uploadOptionPop(false);
			if (mUserNames.size() == 0) {
				imgaccList.setVisibility(Button.INVISIBLE);
			} else {

			}
		}
	};

	/**
	 *    ˺ ѡ  Popupwindow
	 **/
	@SuppressWarnings("deprecation")
	private void uploadOptionPop(boolean show_flag) {
		if (show_flag) {
			if (selectPopupWindow != null) {
				if (selectPopupWindow.isShowing()) {
					selectPopupWindow.dismiss();
					imgaccList.setImageResource(FindResHelper
							.RDrawable("hj_drop_down"));
				}
				selectPopupWindow = null;
			}
			RelativeLayout re_account = (RelativeLayout) findViewById(FindResHelper
					.RId("re_account"));
			selectPopupWindow = new PopupWindow(layout_option,
					re_account.getWidth(), LayoutParams.WRAP_CONTENT, true);
			selectPopupWindow.setBackgroundDrawable(new BitmapDrawable());//                 ʧ
			selectPopupWindow.showAsDropDown(re_account, 0, 0);
			// selectPopupWindow.setAnimationStyle(R.style.popxiala);
			selectPopupWindow.setFocusable(true);
			selectPopupWindow.setOutsideTouchable(true);
			selectPopupWindow.update();
			// imgaccList.setImageResource(FtnnRes.RDrawable("hj_drop_up"));
		} else {
			if (selectPopupWindow != null) {
				// imgaccList.setImageResource(FindResHelper
				// .RDrawable("hj_drop_down"));
				selectPopupWindow.dismiss();
				selectPopupWindow.setFocusable(false);
			}

		}
	}

	/**
	 *      û   ½  Ϣ(base64λ    )
	 **/
	public void storeAccount(String account, String pwd, String time) {
		//       ο  ʺţ  ȱ  浽   ݿ 
		if (mControlCenter.isTemp(context)) {
			String tempAccount = mControlCenter.getTempName(context);
			mControlCenter.setTempName(context, "");
			Long tsLong = System.currentTimeMillis() / 1100;
			String timez = tsLong.toString();
			storeAccount(tempAccount, tempAccount, timez);
		}
		// String enpwd = new String(Base64.encode(pwd.getBytes(), 0));
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
		try {
			hjAccountDBHelper.updateState(account);
			if (hjAccountDBHelper.isOneExist(account)) {
				hjAccountDBHelper.updateOne(userAccount.getUserName(),
						userAccount, time);
			} else {
				hjAccountDBHelper.insert(userAccount);
			}
		} catch (Exception e) {
			hjAccountDBHelper.close();
			ShowToast(FindResHelper.RStringStr("hj_toast_full"));
		}

		hjAccountDBHelper.close();

	}

	/**
	 *  Ƴ ָ   û   Ϣ
	 **/
	public void removeAccount(String username) {
		hjAccountDBHelper.open();
		hjAccountDBHelper.deleteOne(username);
		hjAccountDBHelper.close();
	}

	/**
	 *   ԭ  ¼ ˺   Ϣ
	 **/
	public int restoreAccounts() {
		hjAccountDBHelper.open();
		int count = hjAccountDBHelper.queryDataCount();
		if (count > 0) {
			UserAccount[] userAccounts = new UserAccount[count];
			userAccounts = hjAccountDBHelper.queryAllData();

			mUserNames.clear();

			for (UserAccount userAccount : userAccounts) {
				mUserNames.add(userAccount.getUserName());
			}
			hjAccountDBHelper.close();
		} else {
			hjAccountDBHelper.close();
		}
		return count;
	}

	/**
	 *    浱ǰ û  ˺ 
	 **/
	public void storeSelectedAccount(String name) {
		hjAccountDBHelper.open();
		hjAccountDBHelper.updateState(name);
		hjAccountDBHelper.close();
	}

	/**
	 *     ѡ   ˺ ״̬
	 **/
	public void updateState(String name) {
		userAccount = hjAccountDBHelper.new UserAccount();
		hjAccountDBHelper.open();
		userAccount = hjAccountDBHelper.queryOne(name);
		hjAccountDBHelper.close();

		edAccount.setText(userAccount.getUserName());
		edPwd.setText("");
		edPwd.setText(CLNaviteHelper.getRememberPwd());
		Long tsLong = System.currentTimeMillis() / 1000;
		String timez = tsLong.toString();
		String pwd = null;
		try {
			pwd = Tools.DecryptDoNet(userAccount.getPassWord(),
					CLNaviteHelper.getInstance().getSdkKey());
		} catch (Exception e) {
			pwd = "";
		}
		storeAccount(userAccount.getUserName(), pwd, timez);
	}

	/**
	 *  ָ  ϴμ ס û  ˺   Ϣ
	 **/
	public UserAccount restoreLastAccount() {
		UserAccount userAccount = hjAccountDBHelper.new UserAccount();
		hjAccountDBHelper.open();
		userAccount = hjAccountDBHelper.queryLastAcc();
		hjAccountDBHelper.close();
		return userAccount;
	}

	private void LoginToast(String msg) {
		if (HySDK.getInstance().LoginToast) {
			ShowToast(msg);
		}
	}

	private void ShowToast(String str) {
		Toast.makeText(HySDK.context, str, Toast.LENGTH_SHORT).show();
	}

	// private void TempLoginSuccess(String behavior,JSONObject response) {
	// if (mCount > 0 && userAccount != null) {
	// Long tsLong = System.currentTimeMillis() / 1000;
	// String timez = tsLong.toString();
	// storeAccount(response.optString("user_name"),
	// response.optString("user_name"),timez);
	// HJControlCenter.CurrentName=response.optString("user_name");
	// }else {
	// //      û   Ϣ
	// try {
	// HJControlCenter.CurrentName=response.optString("user_name");
	// // hjControlCenter.setCurrentName(HJLoginActivity.this,
	// response.optString("user_name"));
	// hjControlCenter.setTempName(HJLoginActivity.this,response.optString("user_name"));
	// } catch (Exception e1) {
	// // TODO Auto-generated catch block
	// e1.printStackTrace();
	// }
	// }
	//
	// HJLoginResult rl = JsonParse.parseResponseList(response);
	// rl.setBehavior(behavior);
	// onLoginFinished(0, "success", rl);
	// // LoginToast(FindResHelper.RStringStr("hj_toast_loginsuccess"));
	// LoginToast(" ο͵ ¼ ɹ   ");
	// }

	@Override
	public void onBackPressed() {
		// if (ISCHANGE) {
		// Intent intent = new Intent(HJLoginActivity.this,
		// HJLoginActivity.class);
		// startActivity(intent);
		// this.finish();
		// }else
		// if (mCount == 0 && !ISGUIDE) {
		// Intent intent = new Intent(HJLoginActivity.this,
		// HJLoginActivity.class);
		// intent.putExtra("ISGUIDE", true);
		// startActivity(intent);
		// this.finish();
		// } else {
		HyLoginResult rl = HyLoginResult.getInstance();
		//     أ   ¼ȡ  
		onLoginFinished(1,rl);
		// }
		super.onBackPressed();
	}

	// private void initLoginView(){
	// setContentView(FindResHelper.RLayout("hj_user_login_not_temp"));
	// edAccount = (EditText) findViewById(FindResHelper
	// .RId("edt_account"));
	// edPwd = (EditText) findViewById(FindResHelper.RId("edt_pwd"));
	// imgaccList = (ImageView) findViewById(FindResHelper
	// .RId("iv_accountList"));
	// tvBindPhone = (LinearLayout) findViewById(FindResHelper
	// .RId("btn_bindphone"));
	// tvFindPwd = (LinearLayout) findViewById(FindResHelper
	// .RId("btn_findpwd"));
	// btnLogin = (LinearLayout) findViewById(FindResHelper
	// .RId("btn_login"));
	// btnLogin.setOnClickListener(this);
	// tvBindPhone.setOnClickListener(this);
	// tvFindPwd.setOnClickListener(this);
	// edPwd.setTypeface(Typeface.SANS_SERIF);
	// if (mCount > 0 && userAccount != null) {
	// edAccount.setText(userAccount.getUserName());
	// edPwd.setText(HJNaviteHelper.getRememberPwd());
	// }
	// if (edPwd.getText().toString()
	// .equals(HJNaviteHelper.getRememberPwd())) {
	// setOnKey(edPwd);
	// setTextStandard(edPwd);
	// setAccountLinstener(edAccount);
	// }
	// imgaccList.setOnClickListener(this);
	//
	// if (mCount > 0) {
	// imgaccList.setVisibility(View.VISIBLE);
	// imgaccList.setOnClickListener(this);
	// } else {
	// imgaccList.setVisibility(Button.INVISIBLE);
	// }
	// // PopupWindow         򲼾 
	// layout_option = (View) this.getLayoutInflater().inflate(
	// FindResHelper.RLayout("hj_accountlistlayout"), null);
	// ListView listView = (ListView) layout_option
	// .findViewById(FindResHelper.RId("layout_options_list"));
	//
	// //      Զ   Adapter
	// mOptionsAdapter = new HJAccountAdapter(HJLoginActivity.this,
	// mUserNames);
	// mOptionsAdapter.setOnClicked(onItemClicked);
	//
	// listView.setAdapter(mOptionsAdapter);
	// imgClose = (ImageView) findViewById(FindResHelper
	// .RId("img_btn_close"));
	// imgClose.setOnClickListener(this);
	// }
	@Override
	protected void onPause() {
		LOGINACTIVITY = 0;
		super.onPause();
	}
}

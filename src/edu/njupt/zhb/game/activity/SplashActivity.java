/*
 * $filename: SplashActivity.java,v $
 * $Date: 2014-7-20  $
 * Copyright (C) ZhengHaibo, Inc. All rights reserved.
 * This software is Made by Zhenghaibo.
 */
package edu.njupt.zhb.game.activity;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Fullscreen;
import org.androidannotations.annotations.WindowFeature;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.Window;
import edu.njupt.zhb.game.callback.DialogCallBack;
import edu.njupt.zhb.game.utils.DialogUtils;
import edu.njupt.zhb.planegame.R;

/*
 *@author: ZhengHaibo  
 *blog:     http://blog.csdn.net/nuptboyzhb
 *mail:    zhb931706659@126.com
 *web:     http://www.mobctrl.net
 *2014-7-20  Nanjing,njupt,China
 */
@Fullscreen
@WindowFeature(Window.FEATURE_NO_TITLE)
@EActivity(R.layout.activity_splash)
public class SplashActivity extends Activity {

	@Bean
	DialogUtils dialogUtils;

	@AfterViews
	void afterViews() {
		initOther();
		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
				startMainActivity();
			}
		}, 2000);
	}

	private void initOther() {
		checkPackageName();
	}

	/**
	 * 校验安装包
	 * 
	 * @return
	 */
	private boolean checkPackageName() {
		String pkName = this.getPackageName();
		System.out.println("debug:pkName = " + pkName);
		StringBuffer realPkName = new StringBuffer("ed" + "u.n" + "jupt.z"
				+ "hb.");
		realPkName.append("planegame");
		System.out.println("debug:realPkName = " + realPkName);
		if (!pkName.equals(realPkName.toString())) {
			dialogUtils.showNormalDialog(this, getString(R.string.pk_error),
					new DialogCallBack() {
						@Override
						public void execute() {
							finish();
						}

						@Override
						public void cancel() {
							finish();
						}
					});
		}
		return true;
	}

	private void startMainActivity() {
		startActivity(new Intent(this, MainActivity_.class));
		finish();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		System.out.println("debug:onKeyDown");
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

}

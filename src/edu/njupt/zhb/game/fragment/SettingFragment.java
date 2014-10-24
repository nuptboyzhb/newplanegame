/*
 * $filename: SettingFragment.java,v $
 * $Date: 2014-7-6  $
 * Copyright (C) ZhengHaibo, Inc. All rights reserved.
 * This software is Made by Zhenghaibo.
 */
package edu.njupt.zhb.game.fragment;

import org.androidannotations.annotations.CheckedChange;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.SeekBarProgressChange;

import android.support.v4.app.Fragment;
import android.widget.CompoundButton;
import edu.njupt.zhb.game.callback.SettingCallback;
import edu.njupt.zhb.planegame.R;

/*
 *@author: ZhengHaibo  
 *blog:     http://blog.csdn.net/nuptboyzhb
 *mail:    zhb931706659@126.com
 *web:     http://www.mobctrl.net
 *2014-7-6  Nanjing,njupt,China
 */
@EFragment(R.layout.setting_fragment)
public class SettingFragment extends Fragment {
	private SettingCallback settingCallback;

	public void setSettingCallback(SettingCallback settingCallback) {
		this.settingCallback = settingCallback;
	}

	@CheckedChange
	void cb_voice(CompoundButton hello, boolean isChecked) {
		System.out.println("debug:isChecked = " + isChecked);
		if (null != settingCallback) {
			settingCallback.setVoiceOpen(isChecked);
		}
	}

	@SeekBarProgressChange
	void sb_enemy(int plane) {
		System.out.println("debug:plane = " + plane);
		if (null != settingCallback) {
			settingCallback.setPlaneNumber(plane + 10);
		}
	}

	@SeekBarProgressChange
	void sb_hard(int speed) {
		System.out.println("debug:hard = " + speed);
		if (null == settingCallback) {
			settingCallback.setPlaneSpeed(speed);
		}
	}

}

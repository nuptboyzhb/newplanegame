/*
 * $filename: SettingFragment.java,v $
 * $Date: 2014-7-6  $
 * Copyright (C) ZhengHaibo, Inc. All rights reserved.
 * This software is Made by Zhenghaibo.
 */
package edu.njupt.zhb.game.fragment;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.annotations.ViewById;

import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import edu.njupt.zhb.game.MyConstants;
import edu.njupt.zhb.game.callback.StartFragmentCallBack;
import edu.njupt.zhb.planegame.R;

/*
 *@author: ZhengHaibo  
 *blog:     http://blog.csdn.net/nuptboyzhb
 *mail:    zhb931706659@126.com
 *web:     http://www.mobctrl.net
 *2014-7-6  Nanjing,njupt,China
 */
@EFragment(R.layout.start_fragment)
public class StartFragment extends Fragment {
	@ViewById
	Button btn_start;
	@ViewById
	Button btn_get_coin;
	@ViewById
	Button btn_share;
	@ViewById
	Button btn_help;
	@ViewById
	Button btn_feedback;
	@ViewById
	Button btn_resume;
	@ViewById
	Button btn_rank;
	@ViewById
	Button btn_submit;
	@ViewById
	TextView tv_point;

	@FragmentArg
	int gameStatus;

	@FragmentArg
	int gamePoint;

	private StartFragmentCallBack startFragmentCallBack;

	public void setStartFragmentCallBack(
			StartFragmentCallBack startFragmentCallBack) {
		this.startFragmentCallBack = startFragmentCallBack;
	}

	@AfterViews
	void afterView() {
		showFragmentBtn();
		setPointText(gamePoint);
	}

	private void showFragmentBtn() {
		switch (gameStatus) {
		case MyConstants.GAME_STATUS_START:
			btn_rank.setVisibility(View.GONE);
			btn_resume.setVisibility(View.GONE);
			break;
		case MyConstants.GAME_STATUS_PAUSE:
			btn_start.setText(getString(R.string.btn_restart));
			btn_rank.setVisibility(View.VISIBLE);
			btn_resume.setVisibility(View.VISIBLE);
			break;
		case MyConstants.GAME_STATUS_RESUME:
			btn_start.setText(getString(R.string.btn_restart));
			btn_rank.setVisibility(View.VISIBLE);
			btn_resume.setVisibility(View.VISIBLE);
			break;

		default:
			break;
		}
	}

	/**
	 * 开始游戏
	 */
	@Click
	void btn_start() {
		if (gameStatus == MyConstants.GAME_STATUS_START) {
			startFragmentCallBack.onGameStart();
		} else {
			startFragmentCallBack.onGameRestart();
		}
	}

	@Click
	void btn_resume() {
		startFragmentCallBack.onGameStart();
	}

	@Click
	void btn_rank() {
		startFragmentCallBack.onRank();
	}

	/**
	 * 获取金币
	 */
	@Click({ R.id.btn_get_coin, R.id.rl_point })
	void btn_get_coin() {
		startFragmentCallBack.onGetCoins();
	}

	@Click
	void btn_share() {
		showToast("分享");
	}

	/**
	 * 游戏帮助
	 */
	@Click
	void btn_help() {
		showToast("帮助");
	}

	/**
	 * 意见反馈
	 */
	@Click
	void btn_feedback() {
		// ...
	}

	@Click
	void btn_exit() {
		startFragmentCallBack.onExit();
	}

	@Click
	void btn_submit() {
		startFragmentCallBack.onSubmit();
	}

	private void setPointText(int point) {
		if (null != tv_point) {
			tv_point.setText(point + "+");
		}
	}

	private void showToast(String text) {
		Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT).show();
	}
}

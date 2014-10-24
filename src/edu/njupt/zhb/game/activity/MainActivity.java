package edu.njupt.zhb.game.activity;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Fullscreen;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.WindowFeature;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;
import edu.njupt.zhb.game.MyConstants;
import edu.njupt.zhb.game.callback.DialogCallBack;
import edu.njupt.zhb.game.callback.GameOverCallback;
import edu.njupt.zhb.game.callback.PlaneViewCallback;
import edu.njupt.zhb.game.callback.SettingCallback;
import edu.njupt.zhb.game.callback.StartFragmentCallBack;
import edu.njupt.zhb.game.fragment.SettingFragment_;
import edu.njupt.zhb.game.fragment.StartFragment_;
import edu.njupt.zhb.game.utils.DialogUtils;
import edu.njupt.zhb.game.view.PlaneView;
import edu.njupt.zhb.planegame.R;

/*
 *@author: ZhengHaibo  
 *web:     http://blog.csdn.net/nuptboyzhb
 *mail:    zhb931706659@126.com
 *2013-6-7  Nanjing,njupt,China
 */
@Fullscreen
@WindowFeature(Window.FEATURE_NO_TITLE)
@EActivity(R.layout.activity_main)
public class MainActivity extends FragmentActivity implements
		PlaneViewCallback, GameOverCallback, SettingCallback,
		StartFragmentCallBack {
	protected static final int RANK_ID = 1098;
	protected static final int ONE_PLANE_POINT = 50;
	private static final int PLAY_NORMAL = 0;
	private static final int PLAY_BOSS = 1;
	@ViewById
	RelativeLayout rl_bubble;

	@ViewById
	TextView tv_bubble_count;
	@ViewById
	ImageView iv_lift1;
	@ViewById
	ImageView iv_lift2;
	@ViewById
	ImageView iv_lift3;
	@ViewById
	ImageView iv_media_open;
	@ViewById
	TextView tv_level;
	@ViewById
	ImageView iv_share;

	@Bean
	DialogUtils dialogUtils;

	private int gamePoint = 0;

	private FragmentManager fragmentManager;
	private SettingFragment_ fragment;

	private StartFragment_ startFragment;

	private PlaneView planeView;

	private boolean isSettingFragmentShow;

	private boolean isStartFragmentShow = false;

	private boolean isMediaOpen = false;

	private boolean isFirstAdd = true;
	private MediaPlayer bgPlayer = null;
	private MediaPlayer gameOverMediaPlayer = null;
	private MediaPlayer bossBgPlayer = null;
	private MediaPlayer switchBossMediaPlayer = null;
	private int randCount = 0;
	private int playType;

	@AfterViews
	void afterView() {
		fragmentManager = getSupportFragmentManager();
		isSettingFragmentShow = false;
		planeView = new PlaneView(this);
		planeView.setPlaneViewCallback(this);
		planeView.setGameOverCallback(this);
		planeView.isMediaOpen = this.isMediaOpen;
		LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT);
		rl_bubble.addView(planeView, lp);
		tv_level.setText("L:0");
		bgPlayer = MediaPlayer.create(this, R.raw.m1);
		bossBgPlayer = MediaPlayer.create(this, R.raw.bgm_jizhanboss2);
		gameOverMediaPlayer = MediaPlayer
				.create(this, R.raw.effcet_vo_gameover);
		switchBossMediaPlayer = MediaPlayer.create(this, R.raw.effcet_vo_haoye);
		bgPlayer.setLooping(true);
		bossBgPlayer.setLooping(true);
		if (isMediaOpen) {
			bgPlayer.start();
		}
		playType = PLAY_NORMAL;
	}

	@Override
	public void onLoadFinished() {
		System.out.println("debug:onLoadFinished:isFirstAdd = " + isFirstAdd);
		if (isFirstAdd) {
			addStartFragment(MyConstants.GAME_STATUS_START);
		}
		isFirstAdd = false;
	};

	/**
	 * 添加开始游戏的fragment
	 */
	private void addStartFragment(int gameStatus) {
		if (null != planeView) {
			planeView.isPause = true;
		}
		if (isStartFragmentShow) {
			System.out.println("debug:isStartFragmentShow = "
					+ isStartFragmentShow + ",return");
			return;
		}
		System.out.println("debug:addStartFragment...");
		isStartFragmentShow = true;
		startFragment = new StartFragment_();
		Bundle data = new Bundle();
		data.putInt("gameStatus", gameStatus);
		data.putInt("gamePoint", this.gamePoint);
		startFragment.setArguments(data);
		startFragment.setStartFragmentCallBack(this);
		FragmentTransaction fragmentTransaction = fragmentManager
				.beginTransaction();
		fragmentTransaction.setCustomAnimations(
				R.anim.fragment_slide_in_from_bottom,
				R.anim.fragment_slide_out_to_bottom,
				R.anim.fragment_slide_in_from_bottom,
				R.anim.fragment_slide_out_to_bottom);
		fragmentTransaction.add(R.id.rl_settings, startFragment);
		fragmentTransaction.addToBackStack(null);
		fragmentTransaction.commitAllowingStateLoss();
		System.out.println("debug:add fragment");
	}

	protected void showLiftImage(int lift) {
		// TODO Auto-generated method stub
		if (lift >= 3) {
			iv_lift1.setVisibility(View.VISIBLE);
			iv_lift2.setVisibility(View.VISIBLE);
			iv_lift3.setVisibility(View.VISIBLE);
		} else if (lift == 2) {
			iv_lift1.setVisibility(View.VISIBLE);
			iv_lift2.setVisibility(View.VISIBLE);
			iv_lift3.setVisibility(View.INVISIBLE);
		} else if (lift == 1) {
			iv_lift1.setVisibility(View.VISIBLE);
			iv_lift2.setVisibility(View.INVISIBLE);
			iv_lift3.setVisibility(View.INVISIBLE);
		} else if (lift <= 0) {
			iv_lift1.setVisibility(View.INVISIBLE);
			iv_lift2.setVisibility(View.INVISIBLE);
			iv_lift3.setVisibility(View.INVISIBLE);
		}
	}

	@Override
	public void onGameOver(int lift) {
		System.out.println("debug:lift = " + lift);
		showLiftImage(lift);
		if (lift == 0) {
			if (null != gameOverMediaPlayer && isMediaOpen) {
				gameOverMediaPlayer.start();
			}
			addStartFragment(MyConstants.GAME_STATUS_RESUME);
		}
	}

	@Override
	public void onClickCount(int count) {
		System.out.println("debug:count = " + count);
		tv_bubble_count.setText("G:" + 100 * count);
		this.randCount = 100 * count;
	}

	@Click
	void iv_media_open() {
		isMediaOpen = !isMediaOpen;
		if (isMediaOpen) {
			iv_media_open.setImageResource(R.drawable.ic_action_volume_on);
			if (playType == PLAY_NORMAL) {
				bgPlayer.start();
			} else {
				bossBgPlayer.start();
			}
		} else {
			bgPlayer.pause();
			bossBgPlayer.pause();
			iv_media_open.setImageResource(R.drawable.ic_action_volume_muted);
		}
		if (null != planeView) {
			planeView.isMediaOpen = this.isMediaOpen;
		}
	}

	@Click
	void iv_settings() {
		if (null != planeView) {
			planeView.isPause = !planeView.isPause;
		}
		if (isSettingFragmentShow) {
			System.out.println("debug:remove fragment");
			fragmentManager.popBackStack();
			new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {
					isSettingFragmentShow = false;
				}
			}, 500);
			return;
		}
		System.out.println("debug:remove fragment");
		fragment = new SettingFragment_();
		fragment.setSettingCallback(this);
		FragmentTransaction fragmentTransaction = fragmentManager
				.beginTransaction();
		fragmentTransaction.setCustomAnimations(
				R.anim.fragment_slide_in_from_bottom,
				R.anim.fragment_slide_out_to_bottom,
				R.anim.fragment_slide_in_from_bottom,
				R.anim.fragment_slide_out_to_bottom);
		fragmentTransaction.add(R.id.rl_settings, fragment);
		fragmentTransaction.addToBackStack(null);
		fragmentTransaction.commitAllowingStateLoss();
		isSettingFragmentShow = true;
		System.out.println("debug:add fragment");
	}

	private void restartGame() {
		System.out.println("debug:restartGame...");
		rl_bubble.removeAllViews();
		planeView = new PlaneView(this);
		planeView.setPlaneViewCallback(this);
		planeView.setGameOverCallback(this);
		planeView.isMediaOpen = this.isMediaOpen;
		LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT);
		rl_bubble.addView(planeView, lp);
		showLiftImage(3);
		if (isMediaOpen) {
			bgPlayer.start();
		}
		playType = PLAY_NORMAL;
		if (bossBgPlayer.isPlaying()) {
			bossBgPlayer.pause();
		}
	}

	@Override
	public void setPlaneNumber(int plane) {
		System.out.println("debug:m plane = " + plane);
		if (null != planeView) {
			planeView.maxNumber = plane;
		}
	}

	@Override
	public void setVoiceOpen(boolean isOpen) {
		System.out.println("debug:m isOpen = " + isOpen);
		if (null != planeView) {
			planeView.isMediaOpen = isOpen;
		}
	}

	@Override
	public void setPlaneSpeed(int speed) {
		System.out.println("debug:m speed = " + speed);
		if (null != planeView) {
			planeView.speedBase = 50;
		}
	}

	@Override
	public void setBulletPeroid(int peroid) {
		if (null != planeView) {
			planeView.bulletPeroid = peroid;
		}
	}

	@Override
	public void setBulletFire(int speed) {

	}

	@Override
	public void onLevelUp(int level) {
		tv_level.setText("L:" + level);
	}

	@Override
	public void onGameStart() {
		System.out.println("debug:onGameStart...");
		if (isStartFragmentShow) {
			if (planeView != null && planeView.lift <= 0) {
				dialogUtils.showNormalDialog(this,
						getString(R.string.get_cion_tips),
						new DialogCallBack() {
							@Override
							public void execute() {
								onGameRestart();
							}

							@Override
							public void cancel() {

							}
						});
				return;
			}
			System.out.println("debug:remove fragment");
			fragmentManager.popBackStack();
			new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {
					isStartFragmentShow = false;
					if (null != planeView) {
						planeView.isPause = false;
					} else {
						restartGame();
					}
				}
			}, 500);
			return;
		} else {
			if (isStartFragmentShow) {
				popStartFragment();
			}
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		System.out.println("debug:onKeyDown");
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (!isStartFragmentShow) {
				addStartFragment(MyConstants.GAME_STATUS_PAUSE);
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	private void popStartFragment() {
		System.out.println("debug:popStartFragment");
		if (isStartFragmentShow) {
			System.out.println("debug:remove fragment");
			fragmentManager.popBackStack();
			isStartFragmentShow = false;
			return;
		}
	}

	private void popSettingFragment() {
		System.out.println("debug:popSettingFragment");
		if (isSettingFragmentShow) {
			System.out.println("debug:remove fragment");
			fragmentManager.popBackStack();
			new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {
					isSettingFragmentShow = false;
				}
			}, 500);
			return;
		}
	}

	@Override
	public void onGameRestart() {
		System.out.println("debug:onGameRestart");
		if (isSettingFragmentShow) {
			popSettingFragment();
			isSettingFragmentShow = false;
		}
		if (isStartFragmentShow) {
			popStartFragment();
			isStartFragmentShow = false;
		}
		restartGame();
	}

	@Override
	public void onExit() {
		finish();
	}

	@Override
	public void onGamePause() {
		System.out.println("debug:onGamePause");
		addStartFragment(MyConstants.GAME_STATUS_PAUSE);
	}

	@Override
	protected void onDestroy() {
		bgPlayer.stop();
		bgPlayer.release();
		bossBgPlayer.release();
		gameOverMediaPlayer.release();
		switchBossMediaPlayer.release();
		super.onDestroy();
	}

	@Override
	protected void onPause() {
		if (bgPlayer.isPlaying()) {
			bgPlayer.pause();
		}
		super.onPause();
	}

	@Override
	protected void onResume() {
		if (isMediaOpen && !bgPlayer.isPlaying()) {
			bgPlayer.start();
		}
		super.onResume();
	}

	@Override
	public void onRank() {
		//...
	}

	@Override
	public void onSubmit() {
		//...
	}

	@Click
	void iv_share() {
		if (!isStartFragmentShow) {
			addStartFragment(MyConstants.GAME_STATUS_PAUSE);
		}
		// ...
	}

	@UiThread
	void showToast(CharSequence text) {
		Toast.makeText(this, text, Toast.LENGTH_LONG).show();
	}

	@Override
	public void onNormalToBoss() {
		playType = PLAY_BOSS;
		if (isMediaOpen && null != bgPlayer && null != bossBgPlayer) {
			switchBossMediaPlayer.start();
			bgPlayer.pause();
			bossBgPlayer.start();
		}
	}

	@Override
	public void onBossToNormal() {
		playType = PLAY_NORMAL;
		if (isMediaOpen && null != bgPlayer && null != bossBgPlayer) {
			switchBossMediaPlayer.start();
			bgPlayer.start();
			bossBgPlayer.pause();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		menu.add(0, 1, 1, getString(R.string.btn_exit));
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		if (item.getItemId() == 1) {
			finish();
		}
		return true;
	}

	@Override
	public void onGetCoins() {
		// TODO Auto-generated method stub

	}
}

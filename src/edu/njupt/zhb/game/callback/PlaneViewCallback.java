package edu.njupt.zhb.game.callback;
/*
 *@author: ZhengHaibo  
 *blog:     http://blog.csdn.net/nuptboyzhb
 *mail:    zhb931706659@126.com
 *web:     http://www.mobctrl.net
 *2014-7-6  Nanjing,njupt,China
 */
public interface PlaneViewCallback {
	public void onClickCount(int count);
	public void onLevelUp(int level);
	public void onLoadFinished();
	public void onGamePause();
	public void onNormalToBoss();
	public void onBossToNormal();
}

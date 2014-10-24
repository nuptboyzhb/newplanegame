/*
 * $filename: StartFragmentCallBack.java,v $
 * $Date: 2014-7-16  $
 * Copyright (C) ZhengHaibo, Inc. All rights reserved.
 * This software is Made by Zhenghaibo.
 */
package edu.njupt.zhb.game.callback;
/*
 *@author: ZhengHaibo  
 *blog:     http://blog.csdn.net/nuptboyzhb
 *mail:    zhb931706659@126.com
 *web:     http://www.mobctrl.net
 *2014-7-16  Nanjing,njupt,China
 */
public interface StartFragmentCallBack {
	public void onGameStart();
	public void onGameRestart();
	public void onExit();
	public void onRank();
	public void onSubmit();
	public void onGetCoins();
}

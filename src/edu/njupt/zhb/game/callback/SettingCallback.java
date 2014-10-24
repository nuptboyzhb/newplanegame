package edu.njupt.zhb.game.callback;
/*
 *@author: ZhengHaibo  
 *blog:     http://blog.csdn.net/nuptboyzhb
 *mail:    zhb931706659@126.com
 *web:     http://www.mobctrl.net
 *2014-7-6  Nanjing,njupt,China
 */
public interface SettingCallback {
	public void setVoiceOpen(boolean isOpen);
	
	public void setPlaneSpeed(int speed);
	
	public void setBulletPeroid(int peroid);
	
	public void setBulletFire(int speed);
	
	public void setPlaneNumber(int plane);
	
}

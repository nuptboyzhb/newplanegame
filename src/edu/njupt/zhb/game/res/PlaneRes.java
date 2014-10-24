package edu.njupt.zhb.game.res;

import android.graphics.Bitmap;

/*
 *@author: ZhengHaibo  
 *blog:     http://blog.csdn.net/nuptboyzhb
 *mail:    zhb931706659@126.com
 *web:     http://www.mobctrl.net
 *2014-7-6  Nanjing,njupt,China
 */
public class PlaneRes {
	private Bitmap bitmap;
	private PlaneColor color;

	public Bitmap getBitmap() {
		return bitmap;
	}

	public void setBitmap(Bitmap bitmap) {
		this.bitmap = bitmap;
	}

	public PlaneColor getColor() {
		return color;
	}

	public void setColor(PlaneColor color) {
		this.color = color;
	}

	public PlaneRes(Bitmap bitmap, PlaneColor color) {
		super();
		this.bitmap = bitmap;
		this.color = color;
	}

	public void resize(int width) {
		bitmap = Bitmap.createScaledBitmap(bitmap, width, width, false);
	}

}

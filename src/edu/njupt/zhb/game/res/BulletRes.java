/*
 * $filename: BulletRes.java,v $
 * $Date: 2014-7-6  $
 * Copyright (C) ZhengHaibo, Inc. All rights reserved.
 * This software is Made by Zhenghaibo.
 */
package edu.njupt.zhb.game.res;

import android.graphics.Bitmap;

/*
 *@author: ZhengHaibo  
 *blog:     http://blog.csdn.net/nuptboyzhb
 *mail:    zhb931706659@126.com
 *web:     http://www.mobctrl.net
 *2014-7-6  Nanjing,njupt,China
 */
public class BulletRes {
	private Bitmap bitmap;
	private BulletType bulletType;
	/**
	 * @return the bitmap
	 */
	public Bitmap getBitmap() {
		return bitmap;
	}
	/**
	 * @param bitmap the bitmap to set
	 */
	public void setBitmap(Bitmap bitmap) {
		this.bitmap = bitmap;
	}
	/**
	 * @return the bulletType
	 */
	public BulletType getBulletType() {
		return bulletType;
	}
	public BulletRes(Bitmap bitmap, BulletType bulletType) {
		super();
		this.bitmap = bitmap;
		this.bulletType = bulletType;
	}
	/**
	 * @param bulletType the bulletType to set
	 */
	public void setBulletType(BulletType bulletType) {
		this.bulletType = bulletType;
	}
}

/*
 * $filename: Bullet.java,v $
 * $Date: 2014-7-6  $
 * Copyright (C) ZhengHaibo, Inc. All rights reserved.
 * This software is Made by Zhenghaibo.
 */
package edu.njupt.zhb.game.objects;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import edu.njupt.zhb.game.res.BulletRes;
import edu.njupt.zhb.game.res.PlaneColor;

/*
 *@author: ZhengHaibo  
 *blog:     http://blog.csdn.net/nuptboyzhb
 *mail:    zhb931706659@126.com
 *web:     http://www.mobctrl.net
 *2014-7-6  Nanjing,njupt,China
 */
public class Bullet implements DrawObject {
	private int x;
	private int y;
	private BulletRes bulletRes;
	private int width;
	private int height;
	private int xSpeed;
	private int ySpeed;
	private int screenWidth;
	private int screenHeight;
	private int type;

	public void setType(int type) {
		this.type = type;
	}

	public Bullet(int x, int y, edu.njupt.zhb.game.res.BulletRes bulletRes,
			int xSpeed, int ySpeed, int screenWidth, int screenHeight, int type) {
		super();
		this.x = x;
		this.y = y;
		this.bulletRes = bulletRes;
		this.xSpeed = xSpeed;
		this.ySpeed = ySpeed;
		this.screenWidth = screenWidth;
		this.screenHeight = screenHeight;
		this.height = bulletRes.getBitmap().getHeight();
		this.width = bulletRes.getBitmap().getWidth();
		this.type = type;
	}

	@Override
	public boolean isInScreen() {
		if (y + height < 0 || x + width < 0 || x > screenWidth) {
			return false;
		}
		return true;
	}

	@Override
	public boolean isContainPoint(int pointX, int pointY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onDraw(Canvas canvas, Paint paint) {
		if (null != bulletRes) {
			canvas.drawBitmap(bulletRes.getBitmap(), x - width / 2, y, paint);
		}
	}

	@Override
	public int getType() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void updatePosition(int pointX, int pointY) {
		y -= ySpeed;
		x += xSpeed;
	}

	@Override
	public int getX() {
		// TODO Auto-generated method stub
		return this.x;
	}

	@Override
	public int getY() {
		// TODO Auto-generated method stub
		return this.y;
	}

	@Override
	public int getWidth() {
		// TODO Auto-generated method stub
		return this.width;
	}

	@Override
	public PlaneColor getPlaneColor() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setClicked(boolean isClicked) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onBlastDraw(Canvas canvas, Paint paint) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isClicked() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isBlastFrameEnd() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isCollide(DrawObject drawObject) {
		// TODO Auto-generated method stub
		for (KeyPoint keyPoint : drawObject.getKeyPoints()) {
			if (new Rect(x, y, x + width, y + height).contains(keyPoint.getX(),
					keyPoint.getY())) {
				return true;
			}
		}
		return false;
	}

	@Override
	public List<KeyPoint> getKeyPoints() {
		// TODO Auto-generated method stub
		KeyPoint keyPoint1 = new KeyPoint(x, y);
		KeyPoint keyPoint2 = new KeyPoint(x, y);
		List<KeyPoint> keyPoints = new ArrayList<KeyPoint>();
		keyPoints.add(keyPoint1);
		keyPoints.add(keyPoint2);
		return keyPoints;
	}

}

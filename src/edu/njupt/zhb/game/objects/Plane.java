package edu.njupt.zhb.game.objects;

import java.util.ArrayList;
import java.util.List;

import edu.njupt.zhb.game.res.PlaneBlastRes;
import edu.njupt.zhb.game.res.PlaneColor;
import edu.njupt.zhb.game.res.PlaneRes;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

/*
 *@author: ZhengHaibo  
 *blog:     http://blog.csdn.net/nuptboyzhb
 *mail:    zhb931706659@126.com
 *web:     http://www.mobctrl.net
 *2014-7-6  Nanjing,njupt,China
 */
public class Plane implements DrawObject {

	private int x;
	private int y;
	private PlaneRes planeRes;
	private PlaneBlastRes planeBlastRes;
	private int width;
	private int xSpeed;
	private int ySpeed;
	private int screenWidth;
	private boolean isClicked;
	private int blastFrame;
	private boolean isBlastFrameEnd;
	private final int edge = 10;
	private int screenHeight;
	private int type;

	public void setType(int type) {
		this.type = type;
	}

	public PlaneRes getPlaneRes() {
		return planeRes;
	}

	public boolean isBlastFrameEnd() {
		return isBlastFrameEnd;
	}

	public void setBlastFrameEnd(boolean isBlastFrameEnd) {
		this.isBlastFrameEnd = isBlastFrameEnd;
	}

	public boolean isClicked() {
		return isClicked;
	}

	public void setClicked(boolean isClicked) {
		this.isClicked = isClicked;
	}

	public Plane(int x, int y, PlaneRes planeRes, PlaneBlastRes planeBlastRes,
			int xSpeed, int ySpeed, int screenWidth, int screenHeight, int type) {
		this.x = x;
		this.y = y;
		this.planeRes = planeRes;
		this.planeBlastRes = planeBlastRes;
		this.xSpeed = xSpeed;
		this.ySpeed = ySpeed;
		this.screenWidth = screenWidth;
		this.screenHeight = screenHeight;
		this.width = planeRes.getBitmap().getWidth();
		this.isClicked = false;
		this.blastFrame = 0;
		this.isBlastFrameEnd = false;
		this.type = type;
	}

	@Override
	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	@Override
	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	@Override
	public int getWidth() {
		return width;
	}

	@Override
	public void onDraw(Canvas canvas, Paint paint) {
		if (null != planeRes) {
			canvas.drawBitmap(planeRes.getBitmap(), x, y, paint);
		}
	}

	@Override
	public boolean isContainPoint(int pointX, int pointY) {
		if (new Rect(x, y, x + width, y + width).contains(pointX, pointY)) {
			return true;
		}
		return false;
	}

	@Override
	public int getType() {
		return this.type;
	}

	@Override
	public void updatePosition(int pointX, int pointY) {
		if (pointX == 0 && pointY == 0) {// update by speed
			x += xSpeed;
			y += ySpeed;
			if (x < edge) {
				xSpeed = Math.abs(xSpeed);
			} else if (x + width + edge > screenWidth) {
				xSpeed = -Math.abs(xSpeed);
			}
		} else {
			x = pointX - width / 2;
			y = pointY - width;
		}
	}

	@Override
	public boolean isInScreen() {
		if (y > screenHeight) {
			return false;
		}
		return true;
	}

	@Override
	public PlaneColor getPlaneColor() {
		return planeRes.getColor();
	}

	@Override
	public void onBlastDraw(Canvas canvas, Paint paint) {
		if (blastFrame >= planeBlastRes.getBitmaps().length) {
			isBlastFrameEnd = true;
		} else {
			int alphaOffset = (planeBlastRes.getBitmaps()[blastFrame]
					.getWidth() - planeRes.getBitmap().getWidth()) / 2;
			canvas.drawBitmap(planeBlastRes.getBitmaps()[blastFrame], x
					- alphaOffset, y - alphaOffset, paint);
		}
		blastFrame++;
	}

	@Override
	public boolean isCollide(DrawObject drawObject) {
		// TODO Auto-generated method stub
		Rect r1 = new Rect(x, (int) (y + 0.33 * width), x + width,
				(int) (y + 0.67 * width));
		Rect r2 = new Rect((int) (x + 0.33 * width), y,
				(int) (x + 0.67 * width), y + width);
		for (KeyPoint keyPoint : drawObject.getKeyPoints()) {
			if (r1.contains(keyPoint.getX(), keyPoint.getY())
					|| r2.contains(keyPoint.getX(), keyPoint.getY())) {
				return true;
			}
		}
		return false;
	}

	@Override
	public List<KeyPoint> getKeyPoints() {
		// TODO Auto-generated method stub
		List<KeyPoint> points = new ArrayList<KeyPoint>();
		KeyPoint keyPoint1 = new KeyPoint((int) (x + 0.26 * width), y);
		KeyPoint keyPoint2 = new KeyPoint((int) (x + 0.74 * width), y);
		KeyPoint keyPoint3 = new KeyPoint(x, (int) (y + 0.24 * width));
		KeyPoint keyPoint4 = new KeyPoint(x + width, (int) (y + 0.24 * width));
		KeyPoint keyPoint5 = new KeyPoint((int) (x + 0.5 * width), y + width);
		points.add(keyPoint1);
		points.add(keyPoint2);
		points.add(keyPoint3);
		points.add(keyPoint4);
		points.add(keyPoint5);
		return points;
	}

}

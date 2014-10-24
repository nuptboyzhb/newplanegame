package edu.njupt.zhb.game.objects;

import java.util.List;

import android.graphics.Canvas;
import android.graphics.Paint;
import edu.njupt.zhb.game.res.PlaneColor;
/*
 *@author: ZhengHaibo  
 *blog:     http://blog.csdn.net/nuptboyzhb
 *mail:    zhb931706659@126.com
 *web:     http://www.mobctrl.net
 *2014-7-6  Nanjing,njupt,China
 */
public interface DrawObject {
	/**
	 * @param pointX
	 * @param pointY
	 * @return
	 */
	public boolean isContainPoint(int pointX,int pointY);

	public void onDraw(Canvas canvas,Paint paint);
	
	public int getType();
	
	public void updatePosition(int pointX,int pointY);
	
	public boolean isInScreen();
	
	public int getX();
	
	public int getY();
	
	public int getWidth();
	
	public PlaneColor getPlaneColor();
	
	public void setClicked(boolean isClicked);
	
	public void onBlastDraw(Canvas canvas,Paint paint);
	
	public boolean isClicked();
	
	public boolean isBlastFrameEnd();
	
	public boolean isCollide(DrawObject drawObject);
	
	public List<KeyPoint> getKeyPoints();
	
}

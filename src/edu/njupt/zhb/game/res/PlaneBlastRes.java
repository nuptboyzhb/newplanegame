package edu.njupt.zhb.game.res;

import android.graphics.Bitmap;
/**
 * 
 * @author zhenghaibo
 *
 */
public class PlaneBlastRes {

	private Bitmap []bitmaps;// 爆破图片
	private PlaneColor color;// 气泡颜色类型
	
	public Bitmap[] getBitmaps() {
		return bitmaps;
	}
	public void setBitmaps(Bitmap[] bitmaps) {
		this.bitmaps = bitmaps;
	}
	public PlaneColor getColor() {
		return color;
	}
	public void setColor(PlaneColor color) {
		this.color = color;
	}
	
	public void resize(int width) {
		// TODO Auto-generated method stub
		//bitmap = Bitmap.createScaledBitmap(bitmap, width, width, false);
	}
	public PlaneBlastRes(Bitmap[] bitmaps, PlaneColor color) {
		super();
		this.bitmaps = bitmaps;
		this.color = color;
	}

}

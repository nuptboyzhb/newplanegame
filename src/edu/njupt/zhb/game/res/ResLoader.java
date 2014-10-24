/*
 * $filename: ResLoader.java,v $
 * $Date: 2014-7-6  $
 * Copyright (C) ZhengHaibo, Inc. All rights reserved.
 * This software is Made by Zhenghaibo.
 */
package edu.njupt.zhb.game.res;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import edu.njupt.zhb.planegame.R;

/*
 *@author: ZhengHaibo  
 *blog:     http://blog.csdn.net/nuptboyzhb
 *mail:    zhb931706659@126.com
 *web:     http://www.mobctrl.net
 *2014-7-6  Nanjing,njupt,China
 */
public class ResLoader {

	public static List<PlaneRes> loadPlaneRes(Context context, int[] planeWidths) {
		int[] bitmapRes = new int[] { R.drawable.boss_plane, R.drawable.plane_y_r,
				R.drawable.hy2000_r };
		PlaneColor[] colors = new PlaneColor[] { PlaneColor.BLUE,
				PlaneColor.RED, PlaneColor.WHITE };
		List<PlaneRes> planesRes = new ArrayList<PlaneRes>();
		Options options = new Options();
		options.inDither = false;
		options.inSampleSize = 1;
		options.inJustDecodeBounds = false;
		options.inPreferredConfig = Config.RGB_565;
		options.inPurgeable = true;
		for (int i = 0; i < bitmapRes.length; i++) {
			Bitmap bitmap = BitmapFactory.decodeResource(
					context.getResources(), bitmapRes[i], options);
			for (int width : planeWidths) {
				PlaneRes planeRes = new PlaneRes(Bitmap.createScaledBitmap(
						bitmap, width, width, false), colors[i]);
				planesRes.add(planeRes);
			}
		}
		return planesRes;
	}

	public static PlaneRes loadBossPlaneRes(Context context, int width) {
		Options options = new Options();
		options.inDither = false;
		options.inSampleSize = 1;
		options.inJustDecodeBounds = false;
		options.inPreferredConfig = Config.RGB_565;
		Bitmap bitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(
				context.getResources(), R.drawable.boss_plane, options), width,
				width, false);
		return new PlaneRes(bitmap, PlaneColor.BLUE);
	}

	public static List<PlaneBlastRes> loadPlaneBlastRes(Context context,
			int[] planeWidths) {
		int[] bitmapBlastRes = new int[] { R.drawable.fire0, R.drawable.fire2,
				R.drawable.fire4, R.drawable.fire6, R.drawable.fire7,
				R.drawable.fire5, R.drawable.fire3, R.drawable.fire1, };
		List<PlaneBlastRes> planesBlastRes = new ArrayList<PlaneBlastRes>();
		Bitmap[] bitmaps = new Bitmap[bitmapBlastRes.length];
		Options options = new Options();
		options.inDither = false;
		options.inSampleSize = 1;
		options.inJustDecodeBounds = false;
		options.inPreferredConfig = Config.RGB_565;
		options.inPurgeable = true;
		for (int i = 0; i < bitmapBlastRes.length; i++) {
			Bitmap bitmap = BitmapFactory.decodeResource(
					context.getResources(), bitmapBlastRes[i], options);
			bitmaps[i] = Bitmap.createScaledBitmap(bitmap,
					planeWidths[0] + 100, planeWidths[0] + 100, false);
		}
		PlaneBlastRes planeBlastRes = new PlaneBlastRes(bitmaps,
				PlaneColor.WHITE);
		planesBlastRes.add(planeBlastRes);
		return planesBlastRes;
	}

	public static List<BulletRes> loadBulletRes(Context context, int bulletW,
			int bulletH) {
		int[] bitmapRes = new int[] { R.drawable.bullet_1, R.drawable.bullet_2,
				R.drawable.bullet_3 };
		BulletType[] types = new BulletType[] { BulletType.SMALL,
				BulletType.MID, BulletType.LARGE };
		List<BulletRes> bulletsRes = new ArrayList<BulletRes>();
		Options options = new Options();
		options.inDither = false;
		options.inSampleSize = 1;
		options.inJustDecodeBounds = false;
		options.inPreferredConfig = Config.RGB_565;
		options.inPurgeable = true;
		for (int i = 0; i < bitmapRes.length; i++) {
			Bitmap bitmap = BitmapFactory.decodeResource(
					context.getResources(), bitmapRes[i], options);
			BulletRes bulletRes = new BulletRes(Bitmap.createScaledBitmap(
					bitmap, bulletW, bulletH, false), types[i]);
			bulletsRes.add(bulletRes);
		}
		return bulletsRes;
	}

	public static PlaneRes loadMasterPlaneRes(Context context, int[] planeWidths) {
		PlaneColor[] colors = new PlaneColor[] { PlaneColor.BLUE,
				PlaneColor.RED, PlaneColor.WHITE };
		Options options = new Options();
		options.inDither = false;
		options.inSampleSize = 1;
		options.inJustDecodeBounds = false;
		options.inPreferredConfig = Config.RGB_565;
		options.inPurgeable = true;
		Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(),
				R.drawable.plane_g, options);
		PlaneRes planeRes = new PlaneRes(Bitmap.createScaledBitmap(bitmap,
				planeWidths[0], planeWidths[0], false), colors[0]);
		return planeRes;
	}

	public static Bitmap loadBackgroundBitmap(Context context) {
		Options options = new Options();
		options.inDither = false;
		options.inSampleSize = 1;
		options.inJustDecodeBounds = false;
		options.inPreferredConfig = Config.RGB_565;
		options.inPurgeable = true;
		Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(),
				R.drawable.bg2, options);
		return bitmap;
	}

	public static BulletRes loadBossBulletRes(Context context, int bulletW,
			int bulletH) {
		BulletType[] types = new BulletType[] { BulletType.SMALL,
				BulletType.MID, BulletType.LARGE };
		Options options = new Options();
		options.inDither = false;
		options.inSampleSize = 1;
		options.inJustDecodeBounds = false;
		options.inPreferredConfig = Config.RGB_565;
		options.inPurgeable = true;
		Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(),
				R.drawable.bullet_2_r, options);
		BulletRes bulletRes = new BulletRes(Bitmap.createScaledBitmap(bitmap,
				bulletW, bulletH, false), types[0]);
		return bulletRes;
	}

}

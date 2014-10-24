/*
 * $filename: DialogUtils.java,v $
 * $Date: 2014-7-6  $
 * Copyright (C) ZhengHaibo, Inc. All rights reserved.
 * This software is Made by Zhenghaibo.
 */
package edu.njupt.zhb.game.utils;

import org.androidannotations.annotations.EBean;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.widget.LinearLayout;
import android.widget.TextView;
import edu.njupt.zhb.game.callback.DialogCallBack;
import edu.njupt.zhb.planegame.R;

/*
 *@author: ZhengHaibo  
 *blog:     http://blog.csdn.net/nuptboyzhb
 *mail:    zhb931706659@126.com
 *web:     http://www.mobctrl.net
 *2014-7-6  Nanjing,njupt,China
 */
@EBean
public class DialogUtils {
	public void showOkDialog(Activity activity, String textString,
			final DialogCallBack callBack) {
		LinearLayout loginLayout1 = (LinearLayout) activity.getLayoutInflater()
				.inflate(R.layout.normal_dlg, null);
		TextView title = (TextView) loginLayout1
				.findViewById(R.id.tvTitleToast);
		title.setText("系统提示");
		TextView text1 = (TextView) loginLayout1.findViewById(R.id.tvTextToast);
		text1.setText(textString);
		AlertDialog.Builder builder = new AlertDialog.Builder(activity);
		builder.setView(loginLayout1);
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				callBack.execute();
				dialog.dismiss();
			}
		});
		builder.create().show();
	}

	public void showNormalDialog(Activity activity, String textString,
			final DialogCallBack callBack) {
		LinearLayout loginLayout1 = (LinearLayout) activity.getLayoutInflater()
				.inflate(R.layout.normal_dlg, null);
		TextView title = (TextView) loginLayout1
				.findViewById(R.id.tvTitleToast);
		title.setText("系统提示");
		TextView text1 = (TextView) loginLayout1.findViewById(R.id.tvTextToast);
		text1.setText(textString);
		AlertDialog.Builder builder = new AlertDialog.Builder(activity);
		builder.setCancelable(false);
		builder.setView(loginLayout1);
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				callBack.execute();
				dialog.dismiss();
			}
		});
		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				callBack.cancel();
				dialog.dismiss();
			}
		});
		builder.create().show();
	}
}

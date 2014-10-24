package edu.njupt.zhb.game.view;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import edu.njupt.zhb.game.MyConstants;
import edu.njupt.zhb.game.callback.GameOverCallback;
import edu.njupt.zhb.game.callback.PlaneViewCallback;
import edu.njupt.zhb.game.objects.BossPlane;
import edu.njupt.zhb.game.objects.Bullet;
import edu.njupt.zhb.game.objects.DrawObject;
import edu.njupt.zhb.game.objects.GameScreen;
import edu.njupt.zhb.game.objects.Plane;
import edu.njupt.zhb.game.res.BulletRes;
import edu.njupt.zhb.game.res.PlaneBlastRes;
import edu.njupt.zhb.game.res.PlaneRes;
import edu.njupt.zhb.game.res.ResLoader;
import edu.njupt.zhb.planegame.R;

/**
 * 
 * @author zhenghaibo
 * 
 */
@SuppressLint("WrongCall")
public class PlaneView extends SurfaceView implements Callback, Runnable {

	private SurfaceHolder surfaceHolder;
	private long sleep_time = 16;
	private int screenHeight;
	private int screenWidth;
	private Thread thread;
	private Canvas canvas;
	private Paint paint;
	private List<DrawObject> planes;
	private List<DrawObject> bullets;
	private List<DrawObject> bossBullets;
	private DrawObject masterPlane;
	private DrawObject bossPlane;
	private int[] planeWidths;
	public int maxNumber = 10;
	protected static final int UPDATE_COUNT = 0;
	protected static final int UPDATE_LEVEL = 1;
	private static final int GAME_OVER = 2;
	private static final int COUNT_LEVEL1 = 200;
	private static final int COUNT_LEVEL2 = 800;
	private static final int COUNT_LEVEL3 = 2000;
	private List<PlaneRes> planesRes;
	private List<PlaneBlastRes> planesBlastRes;
	private List<BulletRes> bulletsRes;
	private BulletRes bossBulletRes;
	private PlaneRes masterPlaneRes;
	private int[] planeSpeeds;
	private PlaneViewCallback planeViewCallback;
	private GameOverCallback gameOverCallback;
	private int count = 0;
	private int bulletW;
	private int bulletH;
	public int speedBase = 100;
	private int frameSeq = 0;
	private boolean isMove;
	public boolean isGameOver = false;
	public boolean isMediaOpen = false;// 控制是否播放音乐
	public boolean isPause = false;// 控制是否暂停
	public int bulletPeroid = 12;// 控制子弹的发送频率
	public int bossBulletPeroid = 20;// 控制boss的子弹发送频率
	public int lift = 3;
	private int bossLift = 3;// boss have three lift
	private int speedK = 1;
	private Bitmap backgroundBmp;
	private PlaneRes bossPlaneRes;
	private GameScreen currentScreen;
	private int level = 0;
	private int backgroundSpeed = 1;

	public PlaneView(Context context) {
		super(context);
		System.out.println("debug:PlaneView()");
		setWillNotDraw(false);// call onDraw
		surfaceHolder = this.getHolder();
		surfaceHolder.addCallback(this);
		surfaceHolder.setFormat(PixelFormat.TRANSLUCENT);
		paint = new Paint();
		paint.setAntiAlias(true);
		paint.setDither(true);
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		System.out.println("debug:surfaceCreated");
		setZOrderOnTop(false);
		isGameOver = false;
		if (isPause) {
			return;
		}
		screenHeight = this.getHeight();
		screenWidth = this.getWidth();
		planes = new ArrayList<DrawObject>();
		bullets = new ArrayList<DrawObject>();
		bossBullets = new ArrayList<DrawObject>();
		planeWidths = new int[] { screenWidth / 6, screenWidth / 8,
				screenWidth / 10 };
		bulletW = screenWidth / 40;
		bulletH = screenHeight / 16;
		initSpeedTypes();
		loadBitmapRes();
		currentScreen = GameScreen.NORMAL;
		initPlane();
		thread = new Thread(this);
		thread.start();
		if (screenHeight / 100 > 0) {
			backgroundSpeed = screenHeight / 100;
		}
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		System.out.println("debug:surfaceChanged");
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		System.out.println("debug:surfaceDestroyed");
		if (lift > 0) {
			planeViewCallback.onGamePause();
		}
		isPause = true;
	}

	private void initSpeedTypes() {
		planeSpeeds = new int[10];
		for (int i = 0; i < planeSpeeds.length; i++) {
			planeSpeeds[i] = screenWidth / (speedBase + 10 * i);
		}
	}

	private void loadBitmapRes() {
		planesRes = ResLoader.loadPlaneRes(getContext(), planeWidths);
		planesBlastRes = ResLoader.loadPlaneBlastRes(getContext(), planeWidths);
		bulletsRes = ResLoader.loadBulletRes(getContext(), bulletW, bulletH);
		masterPlaneRes = ResLoader
				.loadMasterPlaneRes(getContext(), planeWidths);
		backgroundBmp = Bitmap.createScaledBitmap(
				ResLoader.loadBackgroundBitmap(getContext()), screenWidth,
				screenHeight, false);
		bossPlaneRes = ResLoader.loadBossPlaneRes(getContext(),
				(int) (screenWidth * 0.5));
		bossBulletRes = ResLoader.loadBossBulletRes(getContext(), bulletW,
				bulletH);
	}

	private void initPlane() {
		addMasterPlane();// 添加主飞机
		if (currentScreen == GameScreen.BOSS) {
			addBossPlane();
		} else if (currentScreen == GameScreen.NORMAL) {
			if (null != planes) {
				planes.clear();
			}
			for (int i = 0; i < maxNumber; i++) {
				addOnePlane();
			}
		}
	}

	private void addOneBullet() {
		bullets.add(new Bullet(getBulletX(), getBulletY(), bulletsRes.get(0),
				getBulletSpeedX(), getBulletSpeedY(), screenWidth,
				screenHeight, MyConstants.TYPE_BULLET));
	}

	/**
	 * three bullets in one plane
	 */
	private void addThreeBullet() {
		bullets.add(new Bullet(getBulletX(), getBulletY(), bulletsRes.get(0),
				getBulletSpeedX(), getBulletSpeedY(), screenWidth,
				screenHeight, MyConstants.TYPE_BULLET));
		bullets.add(new Bullet(getBulletX1(true), getBulletY1(), bulletsRes
				.get(0), getBulletSpeedX(), getBulletSpeedY(), screenWidth,
				screenHeight, MyConstants.TYPE_BULLET));
		bullets.add(new Bullet(getBulletX1(false), getBulletY1(), bulletsRes
				.get(0), getBulletSpeedX(), getBulletSpeedY(), screenWidth,
				screenHeight, MyConstants.TYPE_BULLET));
	}

	/**
	 * five bullets in one plane
	 */
	private void addFiveBullet() {
		bullets.add(new Bullet(getBulletX(), getBulletY(), bulletsRes.get(0),
				getBulletSpeedX(), getBulletSpeedY(), screenWidth,
				screenHeight, MyConstants.TYPE_BULLET));
		bullets.add(new Bullet(getBulletX1(true), getBulletY1(), bulletsRes
				.get(0), getBulletSpeedX(), getBulletSpeedY(), screenWidth,
				screenHeight, MyConstants.TYPE_BULLET));
		bullets.add(new Bullet(getBulletX1(false), getBulletY1(), bulletsRes
				.get(0), getBulletSpeedX(), getBulletSpeedY(), screenWidth,
				screenHeight, MyConstants.TYPE_BULLET));
		bullets.add(new Bullet(getBulletX2(true), getBulletY2(), bulletsRes
				.get(0), getBulletSpeedX(), getBulletSpeedY(), screenWidth,
				screenHeight, MyConstants.TYPE_BULLET));
		bullets.add(new Bullet(getBulletX2(false), getBulletY2(), bulletsRes
				.get(0), getBulletSpeedX(), getBulletSpeedY(), screenWidth,
				screenHeight, MyConstants.TYPE_BULLET));
	}

	private int getBulletX1(boolean isLeft) {
		if (isLeft) {
			return (int) (masterPlane.getX() + masterPlane.getWidth() * 0.281);
		} else {
			return (int) (masterPlane.getX() + masterPlane.getWidth() * 0.719);
		}
	}

	private int getBulletX2(boolean isLeft) {
		if (isLeft) {
			return (int) (masterPlane.getX() + masterPlane.getWidth() * 0.084);
		} else {
			return (int) (masterPlane.getX() + masterPlane.getWidth() * 0.916);
		}
	}

	private int getBulletY1() {
		return (int) (masterPlane.getY() + masterPlane.getWidth() * 0.543);
	}

	private int getBulletY2() {
		return (int) (masterPlane.getY() + masterPlane.getWidth() * 0.625);
	}

	private int getBulletX() {
		return masterPlane.getX() + masterPlane.getWidth() / 2;
	}

	private int getBulletY() {
		return (masterPlane.getY());
	}

	private int getBulletSpeedX() {
		return 0;
	}

	private int getBulletSpeedY() {
		return screenHeight / speedBase;
	}

	private void addOnePlane() {
		planes.add(new Plane(getStartX(), getStartY(), getPlaneRes(),
				getPlaneBlastRes(), getSpeedX(), speedK * getSpeedY(),
				screenWidth, screenHeight, MyConstants.TYPE_PLANE));
	}

	private void addOneFastPlane() {
		if (isMediaOpen) {
			playMusic(R.raw.shandianqiu);
		}
		planes.add(new Plane(getStartX(), getStartY(), planesRes.get(planesRes
				.size() - 1), getPlaneBlastRes(), getSpeedX(), 4 * getSpeedY(),
				screenWidth, screenHeight, MyConstants.TYPE_PLANE));
	}

	/**
	 * 添加一个BossPlane
	 */
	private void addBossPlane() {
		bossLift = 3;
		if (null != bossBullets) {
			bossBullets.clear();
		}
		bossPlane = new BossPlane(getStartX(), 0, bossPlaneRes,
				getPlaneBlastRes(), screenWidth / speedBase, 0, screenWidth,
				screenHeight, MyConstants.TYPE_BULLET_BOSS);
	}

	/**
	 * 添加一个Boss子弹
	 */
	private void addOneBossBullet() {
		if (null != bossPlane) {
			bossBullets.add(new Bullet(getBossBulletX(), getBossBulletY()
					- bossBulletRes.getBitmap().getHeight(), bossBulletRes,
					getBulletSpeedX(), -1 * getBulletSpeedY(), screenWidth,
					screenHeight, MyConstants.TYPE_BULLET_BOSS));
		}
	}

	private int getBossBulletX() {
		// TODO Auto-generated method stub
		return bossPlane.getX() + bossPlane.getWidth() / 2;
	}

	private int getBossBulletY() {
		// TODO Auto-generated method stub
		return bossPlane.getY() + bossPlane.getWidth();
	}

	private void addMasterPlane() {
		masterPlane = new Plane(screenWidth / 2, screenHeight
				- masterPlaneRes.getBitmap().getHeight(), masterPlaneRes,
				getPlaneBlastRes(), 0, 0, screenWidth, screenHeight,
				MyConstants.TYPE_MASTER_PLANE);
	}

	private PlaneBlastRes getPlaneBlastRes() {

		return planesBlastRes.get(0);
	}

	private int getStartX() {
		return new Random().nextInt(screenWidth);
	}

	private int getStartY() {
		return -100;
	}

	private int getSpeedX() {
		return (int) (planeSpeeds[new Random().nextInt(planeSpeeds.length)] * 0.5);
	}

	private int getSpeedY() {
		return planeSpeeds[new Random().nextInt(planeSpeeds.length)];
	}

	private PlaneRes getPlaneRes() {
		return planesRes.get(new Random().nextInt(planesRes.size()));
	}

	public void setGameOverCallback(GameOverCallback gameOverCallback) {
		this.gameOverCallback = gameOverCallback;
	}

	public void setLift(int lift) {
		this.lift = lift;
	}

	@Override
	public void run() {
		planeViewCallback.onLoadFinished();
		while (!isGameOver) {
			if (isPause) {
				try {
					Thread.sleep(sleep_time);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				continue;
			}
			long starttime = System.currentTimeMillis();
			drawScreen();
			long time = System.currentTimeMillis() - starttime;
			// System.out.println("debug: time = " + time);
			if (time < sleep_time) {
				try {
					Thread.sleep(sleep_time - time);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 绘制场景
	 */
	private void drawScreen() {
		canvas = surfaceHolder.lockCanvas();
		if (null == canvas) {
			return;
		}
		canvas.drawColor(Color.WHITE, PorterDuff.Mode.CLEAR);
		paint.setAlpha(255);
		gameLogic();
		gameDraw();
		if (null != canvas) {
			surfaceHolder.unlockCanvasAndPost(canvas);
		}
	}

	/**
	 * 游戏逻辑
	 */
	private void gameLogic() {
		if (currentScreen == GameScreen.BOSS) {
			bossLogic();
		} else if (currentScreen == GameScreen.NORMAL) {
			normalLogic();
		}
	}

	private void normalLogic() {
		synchronized (planes) {
			planesWithBulletsAndMasterPlane();
			updateBullets();
			if (planes.size() < maxNumber) {
				addOnePlane();
			}
			if (frameSeq % bulletPeroid == 0) {
				addBulletByCount();
			}
			if (frameSeq % 100 == 0) {
				addOneFastPlane();
			}
			switchScreenByCount();
			frameSeq++;
		}
	}

	private void switchScreenByCount() {
		if (count < COUNT_LEVEL1) {
			if (count % 150 == 149) {
				switchNormalToBoss();
			}
		} else if (count >= COUNT_LEVEL1 && count < COUNT_LEVEL2) {
			if (count % 300 == 299) {
				switchNormalToBoss();
			}
		} else if (count >= COUNT_LEVEL2 && count < COUNT_LEVEL3) {
			if (count % 550 == 549) {
				switchNormalToBoss();
			}
		} else if (count >= COUNT_LEVEL3) {
			if (count % 650 == 649) {
				switchNormalToBoss();
			}
		}
	}

	private void switchNormalToBoss() {
		planeViewCallback.onNormalToBoss();
		currentScreen = GameScreen.BOSS;
		addBossPlane();
	}

	/**
	 * 打boss场景逻辑
	 */
	private void bossLogic() {
		masterPlaneWithBossBullets();
		bossPlaneWithBullets();
		updateBullets();
		updateBossPlane();
		updateBossBullets();
		if (frameSeq % bossBulletPeroid == 0) {
			addBulletByCount();
			addOneBossBullet();
		}
		frameSeq++;
	}

	private void planesWithBulletsAndMasterPlane() {
		for (int i = 0; i < planes.size(); i++) {
			DrawObject drawObject = planes.get(i);
			if (drawObject.isInScreen()) {// in the screen and not blast
				if (!drawObject.isClicked()
						&& masterPlane.isCollide(drawObject)) {
					if (isMediaOpen) {
						playMusic(R.raw.oh_my_god);
					}
					drawObject.setClicked(true);
					handler.sendEmptyMessage(GAME_OVER);
				}
				boolean isCollide = false;
				for (int j = 0; j < bullets.size(); j++) {
					DrawObject bullet = bullets.get(j);
					if (drawObject.isCollide(bullet)) {// collide
						if (isMediaOpen) {
							playMusic(R.raw.xiaobaozha);
						}
						i--;
						count++;
						drawObject.setClicked(true);
						handler.sendEmptyMessage(UPDATE_COUNT);
						isCollide = true;
						bullets.remove(j);
						j--;
						break;
					}
				}
				if (!isCollide) {// no collide
					drawObject.updatePosition(0, 0);
				}
			} else {// out of screen
				planes.remove(i);
				i--;
			}
		}
	}

	private void masterPlaneWithBossBullets() {
		if (bossPlane == null) {
			return;
		}
		for (int j = 0; j < bossBullets.size(); j++) {
			DrawObject bullet = bossBullets.get(j);
			if (masterPlane.isCollide(bullet)) {// collide
				if (isMediaOpen) {
					playMusic(R.raw.oh_my_god);
				}
				handler.sendEmptyMessage(GAME_OVER);
				bossBullets.remove(j);
				j--;
				break;
			}
		}
	}

	private void bossPlaneWithBullets() {
		if (null == bossPlane) {
			return;
		}
		for (int j = 0; j < bullets.size(); j++) {
			DrawObject bullet = bullets.get(j);
			if (bossPlane.isCollide(bullet)) {// collide
				if (isMediaOpen) {
					playMusic(R.raw.biggun1);
				}
				bossLift--;
				bossPlane.setClicked(true);
				count += 20;
				handler.sendEmptyMessage(UPDATE_COUNT);
				bullets.remove(j);
				j--;
				break;
			}
		}
		if (bossLift <= 0) {
			switchBossToNormal();
		}
	}

	private void switchBossToNormal() {
		planeViewCallback.onBossToNormal();
		bossPlane = null;
		currentScreen = GameScreen.NORMAL;
		initPlane();
		handler.sendEmptyMessage(UPDATE_LEVEL);
	}

	private void playMusic(int res) {
		MediaPlayer blastPlayer = MediaPlayer.create(getContext(), res);
		blastPlayer.start();
		blastPlayer.setOnCompletionListener(new OnCompletionListener() {
			@Override
			public void onCompletion(MediaPlayer mp) {
				mp.release();
			}
		});
	}

	private void updateBossPlane() {
		if (null != bossPlane) {
			bossPlane.updatePosition(0, 0);
		}
	}

	private void updateBullets() {
		for (int i = 0; i < bullets.size(); i++) {// draw bullet
			DrawObject drawObject = bullets.get(i);
			if (drawObject.isInScreen()) {
				drawObject.updatePosition(0, 0);
			} else {
				bullets.remove(i);
				i--;
			}
		}
	}

	private void updateBossBullets() {
		for (int i = 0; i < bossBullets.size(); i++) {// draw bullet
			DrawObject drawObject = bossBullets.get(i);
			if (drawObject.isInScreen()) {
				drawObject.updatePosition(0, 0);
			} else {
				bossBullets.remove(i);
				i--;
			}
		}
	}

	private void addBulletByCount() {
		if (count < COUNT_LEVEL1) {
			addOneBullet();
			speedK = 1;
		} else if (count >= COUNT_LEVEL1 && count < COUNT_LEVEL2) {
			addThreeBullet();
			maxNumber = 15;
			speedK = 2;
			//bulletPeroid = bulletPeroid - 2;
			//bossBulletPeroid = bossBulletPeroid - 5;
		} else if (count >= COUNT_LEVEL2 && count <COUNT_LEVEL3) {
			addFiveBullet();
			maxNumber = 30;
			speedK = 3;
			//bulletPeroid = bulletPeroid - 3;
			//bossBulletPeroid = bossBulletPeroid - 2;
		} else if( count >=COUNT_LEVEL3){
			addFiveBullet();
			maxNumber = 35;
			speedK = 4;
			//bulletPeroid = bulletPeroid - 5;
			//bossBulletPeroid = bossBulletPeroid - 2;
		}
	}

	private void gameDraw() {
		drawBackground(backgroundSpeed * frameSeq);
		if (currentScreen == GameScreen.NORMAL) {
			synchronized (planes) {
				drawPlanes();
				drawBullets();
				drawMasterPlane();
			}
		} else if (currentScreen == GameScreen.BOSS) {
			drawBullets();
			drawBossPlane();
			drawBossBullets();
			drawMasterPlane();
		}
	}

	private void drawMasterPlane() {
		if (null != masterPlane) {
			masterPlane.onDraw(canvas, paint);
		}
	}

	private void drawPlanes() {
		if (null == planes) {
			return;
		}
		for (int i = 0; i < planes.size(); i++) {
			DrawObject drawObject = planes.get(i);
			if (drawObject.isClicked()) {
				drawObject.onBlastDraw(canvas, paint);
				if (drawObject.isBlastFrameEnd()) {
					planes.remove(i);
					i--;
				}
			} else {
				drawObject.onDraw(canvas, paint);
			}
		}
	}

	private void drawBullets() {
		for (DrawObject bullet : bullets) {
			bullet.onDraw(canvas, paint);
		}
	}

	private void drawBossBullets() {
		for (DrawObject bullet : bossBullets) {
			bullet.onDraw(canvas, paint);
		}
	}

	private void drawBackground(int yOffset) {
		yOffset %= screenHeight;
		if (yOffset == 0) {
			canvas.drawBitmap(backgroundBmp, 0, 0, paint);
		} else {
			canvas.drawBitmap(backgroundBmp, new Rect(0,
					screenHeight - yOffset, screenWidth, screenHeight),
					new Rect(0, 0, screenWidth, yOffset + 1), paint);
			canvas.drawBitmap(backgroundBmp, new Rect(0, 0, screenWidth,
					screenHeight - yOffset), new Rect(0, yOffset, screenWidth,
					screenHeight), paint);
		}
	}

	private void drawBossPlane() {
		if (null != bossPlane) {
			if (bossPlane.isClicked()) {// draw blast img
				bossPlane.onBlastDraw(canvas, paint);
				if (bossPlane.isBlastFrameEnd()) {
					bossPlane.setClicked(false);
				}
			}
			bossPlane.onDraw(canvas, paint);
		}
	}

	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		System.out.println("debug:onDraw");
	}

	@Override
	protected void onDetachedFromWindow() {
		super.onDetachedFromWindow();
		System.out.println("debug:onDetachedFromWindow...");
		isPause = false;
		isGameOver = true;
		for (PlaneRes plane : planesRes) {
			plane.getBitmap().recycle();
		}
		for (BulletRes bulletRes : bulletsRes) {
			bulletRes.getBitmap().recycle();
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent e) {
		int x = (int) e.getX();
		int y = (int) e.getY();
		switch (e.getAction()) {
		case MotionEvent.ACTION_DOWN:
			// synchronized (planes) {
			// for (int i = 0; i < planes.size(); i++) {
			// DrawObject drawObject = planes.get(i);
			// if (drawObject.isContainPoint(x, y)) {//
			// i--;
			// count++;
			// handler.sendEmptyMessage(UPDATE_COUNT);
			// drawObject.setClicked(true);
			// return true;
			// }
			// }
			// }
			if (masterPlane.isContainPoint(x, y)) {
				isMove = true;
			}
			break;
		case MotionEvent.ACTION_MOVE:
			if (isMove) {
				synchronized (masterPlane) {
					masterPlane.updatePosition(x, y);
				}
			}
			break;
		case MotionEvent.ACTION_UP:
			isMove = false;
			break;
		}
		return true;
	}

	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			if (msg.what == UPDATE_COUNT) {
				planeViewCallback.onClickCount(count);
			} else if (msg.what == GAME_OVER) {
				lift--;
				gameOverCallback.onGameOver(lift);
				if (lift <= 0) {
					isPause = true;
				}
			} else if (msg.what == UPDATE_LEVEL) {
				level = level + 1;
				planeViewCallback.onLevelUp(level);
			}
		};
	};

	public void setPlaneViewCallback(PlaneViewCallback planeViewCallback) {
		this.planeViewCallback = planeViewCallback;
	}
}

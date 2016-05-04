package com.anxiangban.myView;

import com.anxiangban.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.View.OnTouchListener;

public class SlipButton extends View implements OnTouchListener {

	public static final String TAG = "SlipButton";
	private Bitmap slipImg;
	private Bitmap bg;
	private Bitmap slipBubble;
	private Matrix matrix = new Matrix();
	private Paint paint = new Paint();
	private float leftPosOfSlipImg;
	private int slipOnEndPos = 0;
	private int slipOffStartPos = 0;
	private int bgWidth = 0;
	private Rect on_Rect;
	private Rect off_Rect;
	private Rect src_Rect;
	private Rect dst_Rect;
	private boolean isSlipping = false;
	private boolean isSwitchOn = false;
	private float previousX, currentX;
	private OnSwitchListener onSwitchListener;
	private boolean isSwitchListenerOn = false;
	private VelocityTracker mVelocityTracker;
	private static final int SNAP_VELOCITY = 100;

	public SlipButton(Context context) {
		super(context);
		init();
	}

	public SlipButton(Context context, AttributeSet attrs) {
		super(context, attrs);

		TypedArray a = context.obtainStyledAttributes(attrs,
				R.styleable.SlipButton);

		boolean isOn = a.getBoolean(R.styleable.SlipButton_slipOn, false);

		int count = a.getIndexCount();

		// get the specified recourse id in xml file
		int bgId = 0;
		int slipBtId = 0;

		for (int i = 0; i < count; i++) {

			int attr = a.getIndex(i);
			switch (attr) {

			case R.styleable.SlipButton_bg:
				bgId = a.getResourceId(attr, 0);
				break;
			case R.styleable.SlipButton_slipImg:
				slipBtId = a.getResourceId(attr, 0);
				break;
			}
		}
		a.recycle();
		// decode the image defined in xml file
		setImageRes(bgId, slipBtId);
		setSwitchState(isOn);

		init();
	}

	/**
	 * set touch listener
	 */
	private void init() {

		setOnTouchListener(this);
	}

	public void setImageRes(int bgId, int slipId) {
		bg = BitmapFactory.decodeResource(getResources(), bgId);
		slipImg = BitmapFactory.decodeResource(getResources(), slipId);
//		slipBubble = BitmapFactory.decodeResource(getResources(),
//				R.drawable.sb_slip_bubble);
		slipBubble = BitmapFactory.decodeResource(getResources(),
				R.drawable.split_1);
		bgWidth = bg.getWidth();
		slipOnEndPos = slipImg.getWidth() / 2 + slipBubble.getWidth() / 2;
		slipOffStartPos = slipImg.getWidth() / 2 - slipBubble.getWidth() / 2;
		on_Rect = new Rect(0, 0, slipOnEndPos, slipImg.getHeight());
		off_Rect = new Rect(slipOffStartPos, 0, slipImg.getWidth(),
				slipImg.getHeight());
		src_Rect = new Rect(0, 0, bg.getWidth(), slipImg.getHeight());
		dst_Rect = new Rect(0, 0, bg.getWidth(), bg.getHeight());
		setDrawImgRect();
	}

	public void setSwitchState(boolean switchState) {
		isSwitchOn = switchState;
		if (isSwitchListenerOn) {
			onSwitchListener.onSwitched(isSwitchOn);
		}
		setDrawImgRect();
		invalidate();
	}

	public boolean getSwitchState() {
		return isSwitchOn;
	}

	public void updateSwitchState(boolean switchState) {
		isSwitchOn = switchState;
		invalidate();
	}

	@Override
	protected void onDraw(Canvas canvas) {

		super.onDraw(canvas);
		canvas.drawBitmap(slipImg, src_Rect, dst_Rect, paint);
		canvas.drawBitmap(bg, matrix, paint);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		setMeasuredDimension(bg.getWidth(), bg.getHeight());
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		if (mVelocityTracker == null) {
			mVelocityTracker = VelocityTracker.obtain();
		}
		mVelocityTracker.addMovement(event);
		boolean isUpAlreadyCatch = false;
		boolean previousState = false;
		switch (event.getAction()) {
		case MotionEvent.ACTION_MOVE:
			currentX = event.getX();
			previousState = isSwitchOn;
			changeSwitchState(event.getX());
			if (isSwitchListenerOn && (previousState != isSwitchOn)) {
				onSwitchListener.onSwitched(isSwitchOn);
			}
			break;
		case MotionEvent.ACTION_DOWN:
			if (event.getX() > bg.getWidth() || event.getY() > bg.getHeight()) {
				return false;
			}
			isSlipping = true;
			previousX = event.getX();
			currentX = previousX;
			break;
		case MotionEvent.ACTION_UP:
			isUpAlreadyCatch = true;
			previousState = isSwitchOn;
			changeSwitchState(event.getX());
		default:
			do {
				if (isUpAlreadyCatch) {
					break;
				}
				final VelocityTracker velocityTracker = mVelocityTracker;
				velocityTracker.computeCurrentVelocity(1000);
				int velocityX = (int) velocityTracker.getXVelocity();
				previousState = isSwitchOn;
				if (velocityX > SNAP_VELOCITY) {
					isSwitchOn = true;
					break;
				}
				if (velocityX < -SNAP_VELOCITY) {
					isSwitchOn = false;
					break;
				}
			} while (false);
			isSlipping = false;
			if (isSwitchListenerOn && (previousState != isSwitchOn)) {
				onSwitchListener.onSwitched(isSwitchOn);
			}
			if (mVelocityTracker != null) {
				mVelocityTracker.recycle();
				mVelocityTracker = null;
			}
			break;
		}
		setDrawImgRect();
		invalidate();
		return true;
	}

	private void changeSwitchState(float currentPos) {
		if (currentPos >= (bgWidth / 2)) {
			isSwitchOn = true;
		} else {
			isSwitchOn = false;
		}
	}

	private void setDrawImgRect() {
		do {
			if (isSlipping) {
				if (currentX > bgWidth) {
					leftPosOfSlipImg = on_Rect.left;
				} else {
					leftPosOfSlipImg = slipOffStartPos - currentX;
				}
			}
			if (isSlipping && leftPosOfSlipImg >= 0
					&& leftPosOfSlipImg <= bgWidth) {
				break;
			}
			if (isSwitchOn) {
				leftPosOfSlipImg = on_Rect.left;
			} else {
				leftPosOfSlipImg = off_Rect.left;
			}
		} while (false);
		int srcLeft = (int) leftPosOfSlipImg;
		src_Rect.left = srcLeft;
		src_Rect.right = srcLeft + bgWidth;
	}

	public void setOnSwitchListener(OnSwitchListener listener) {
		onSwitchListener = listener;
		isSwitchListenerOn = true;
	}

	public interface OnSwitchListener {
		abstract void onSwitched(boolean isSwitchOn);
	}
}
package com.linewell.core.view.draggridview;


import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Vibrator;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;

import com.linewell.core.view.BanSlidingGridView;
import com.linewell.utils.SystemUtils;

/**
 * 自定义Gridview，实现长按拖拽功能
 * @author lyixin@linewell.com
 * @since 2014年11月12日
 */
public class DragGridBaseView extends BanSlidingGridView {
	/**
	 * DragGridView的item长按响应的时间， 默认是1000毫秒，也可以自行设置
	 */
	private long dragResponseMS = 500;
	/**
	 * 点击的响应时间
	 */
	private long clickResponseMS = 200;

	/**
	 * 是否可以拖拽，默认不可以
	 */
	private boolean isDrag = false;

	/**
	 * 是否可用
	 */
	private boolean enable = true;

	private boolean drawLine = false;

	/**
	 * 是否有加载更多按钮（最后一个固定按钮，不可拖动，不参加排序）
	 */
	private boolean hasLoadMore = false;

	private int mDownX;
	private int mDownY;
	private int moveX;
	private int moveY;
	/**
	 * 正在拖拽的position
	 */
	private int mDragPosition;

	/**
	 * 隐藏的position
	 */
	private int mHidePosition;
	/**
	 * 刚开始拖拽的item对应的View
	 */
	private View mStartDragItemView = null;

	/**
	 * 用于拖拽的镜像，这里直接用一个ImageView
	 */
	private ImageView mDragImageView;

	/**
	 * 震动器
	 */
	private Vibrator mVibrator;

	private WindowManager mWindowManager;
	/**
	 * item镜像的布局参数
	 */
	private WindowManager.LayoutParams mWindowLayoutParams;

	/**
	 * 我们拖拽的item对应的Bitmap
	 */
	private Bitmap mDragBitmap;

	/**
	 * 按下的点到所在item的上边缘的距离
	 */
	private int mPoint2ItemTop ;

	/**
	 * 按下的点到所在item的左边缘的距离
	 */
	private int mPoint2ItemLeft;

	/**
	 * DragGridView距离屏幕顶部的偏移量
	 */
	private int mOffset2Top;

	/**
	 * DragGridView距离屏幕左边的偏移量
	 */
	private int mOffset2Left;

	/**
	 * 状态栏的高度
	 */
	private int mStatusHeight;

	/**
	 * DragGridView自动向下滚动的边界值
	 */
	private int mDownScrollBorder;

	/**
	 * DragGridView自动向上滚动的边界值
	 */
	private int mUpScrollBorder;

	/**
	 * DragGridView自动滚动的速度
	 */
	private static final int speed = 80;

	/**
	 * item发生变化回调的接口
	 */
	private OnChanageListener onChanageListener;

	private ViewGroup parent;

	/**
	 * 空白区域点击时间
	 */
	private long onNothingClickTime = 0;

	public DragGridBaseView(Context context) {
		this(context, null);
	}

	public DragGridBaseView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public DragGridBaseView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		mVibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
		mWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		mStatusHeight = getStatusHeight(context); //获取状态栏的高度
	}

	private Handler mHandler = new Handler();

	//用来处理是否为长按的Runnable
	private Runnable mLongClickRunnable = new Runnable() {

		@Override
		public void run() {
			if(!enable){
				return;
			}


			Log.e("", "mLongClickRunnable");
			if(onChanageListener!=null){
				onChanageListener.onLongClick(mDragPosition);
			}
			isDrag = true; //设置可以拖拽
//			mVibrator.vibrate(50); //震动一下
			if(mStartDragItemView!=null){
				mStartDragItemView.setVisibility(View.INVISIBLE);//隐藏该item
			}
			setSelector(android.R.color.transparent);

			//根据我们按下的点显示item镜像
			createDragImage(mDragBitmap, mDownX, mDownY);

		}
	};

	/**
	 * 设置回调接口
	 * @param onChanageListener
	 */
	public void setOnChangeListener(OnChanageListener onChanageListener){
		this.onChanageListener = onChanageListener;
	}

	public void setParent(ViewGroup parent) {
		this.parent = parent;
	}

	/**
	 * 设置响应拖拽的毫秒数，默认是1000毫秒
	 * @param dragResponseMS
	 */
	public void setDragResponseMS(long dragResponseMS) {
		this.dragResponseMS = dragResponseMS;
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		switch(ev.getAction()){
			case MotionEvent.ACTION_DOWN:
				//使用Handler延迟dragResponseMS执行mLongClickRunnable
				mHandler.postDelayed(mLongClickRunnable, dragResponseMS);

				mDownX = (int) ev.getX();
				mDownY = (int) ev.getY();

				if(mDragPosition>=0){
					View view = getChildAt(mDragPosition-getFirstVisiblePosition());
					if(view!=null){
						view.setVisibility(View.VISIBLE);
					}
				}
				//根据按下的X,Y坐标获取所点击item的position
				mDragPosition = pointToPosition(mDownX, mDownY);

				if(mDragPosition == AdapterView.INVALID_POSITION
						|| (hasLoadMore&&mDragPosition==getChildCount()-1)){

					onNothingClickTime = ev.getEventTime();
					mHandler.removeCallbacks(mLongClickRunnable);
					return super.dispatchTouchEvent(ev);
				}

				//根据position获取该item所对应的View
				mStartDragItemView = getChildAt(mDragPosition - getFirstVisiblePosition());

				//下面这几个距离大家可以参考我的博客上面的图来理解下
				mPoint2ItemTop = mDownY - mStartDragItemView.getTop();
				mPoint2ItemLeft = mDownX - mStartDragItemView.getLeft();

				mOffset2Top = (int) (ev.getRawY() - mDownY);
				mOffset2Left = (int) (ev.getRawX() - mDownX);

				//获取DragGridView自动向上滚动的偏移量，小于这个值，DragGridView向下滚动
				mDownScrollBorder = getHeight() /4;
				//获取DragGridView自动向下滚动的偏移量，大于这个值，DragGridView向上滚动
				mUpScrollBorder = getHeight() * 3/4;

				//开启mDragItemView绘图缓存
				mStartDragItemView.setDrawingCacheEnabled(true);
//			mStartDragItemView.setBackgroundColor(getContext().getResources().getColor(R.color.light_gray));
				//获取mDragItemView在缓存中的Bitmap对象
				mDragBitmap = Bitmap.createBitmap(mStartDragItemView.getDrawingCache());
				//这一步很关键，释放绘图缓存，避免出现重复的镜像
				mStartDragItemView.destroyDrawingCache();

				if(parent!=null){
					parent.requestDisallowInterceptTouchEvent(true);
				}

				break;
			case MotionEvent.ACTION_MOVE:
				int moveX = (int)ev.getX();
				int moveY = (int) ev.getY();

				//如果我们在按下的item上面移动，只要不超过item的边界我们就不移除mRunnable
//			if(!isTouchInItem(mStartDragItemView, moveX, moveY)){

				//根据按下的X,Y坐标获取所点击item的position
				if(Math.abs(moveX-mDownX)>SystemUtils.dip2px(getContext(),15)||Math.abs(moveY-mDownY)>SystemUtils.dip2px(getContext(),15)){
					mHandler.removeCallbacks(mLongClickRunnable);
					if(parent!=null && !isDrag){
						parent.requestDisallowInterceptTouchEvent(false);
					}
				}
				if(isDrag){
					onDragItem(moveX, moveY);
					return true;
				}
				break;
			case MotionEvent.ACTION_UP:
				mHandler.removeCallbacks(mLongClickRunnable);
				mHandler.removeCallbacks(mScrollRunnable);

				if(mStartDragItemView!=null){
//				mStartDragItemView.setBackgroundColor(getContext().getResources().getColor(R.color.white));
				}

				if(ev.getEventTime()-onNothingClickTime<=clickResponseMS){
					if(onChanageListener!=null){
						onChanageListener.onNothingClick();
					}
				}else{
					//长按时
					if(hasLoadMore&&mDragPosition==getChildCount()-1){
						//如果是最后一项，则拦截事件
						return true;
					}
				}
				onNothingClickTime=0;

				if(isDrag){
					onStopDrag((int)ev.getX(),(int)ev.getY());
					isDrag = false;
					if(parent!=null){
						parent.requestDisallowInterceptTouchEvent(false);
					}
					return true;
				}
				break;
		}
		return super.dispatchTouchEvent(ev);
	}


	/**
	 * 是否点击在GridView的item上面
	 * @param dragView
	 * @param x
	 * @param y
	 * @return
	 */
	private boolean isTouchInItem(View dragView, int x, int y){
		if(dragView==null){
			return false;
		}
		int leftOffset = dragView.getLeft();
		int topOffset = dragView.getTop();
		if(x < leftOffset || x > leftOffset + dragView.getWidth()){
			return false;
		}

		if(y < topOffset || y > topOffset + dragView.getHeight()){
			return false;
		}

		return true;
	}



	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		if(isDrag && mDragImageView != null){
			switch(ev.getAction()){
				case MotionEvent.ACTION_MOVE:
					moveX = (int) ev.getX();
					moveY = (int) ev.getY();
					//拖动item
//				onDragItem(moveX, moveY);
					if(isDrag){
						return true;
					}
					break;
				case MotionEvent.ACTION_UP:
					onStopDrag((int)ev.getX(),(int)ev.getY());
					isDrag = false;
					break;
			}
			return true;
		}
		return super.onTouchEvent(ev);
	}


	/**
	 * 创建拖动的镜像
	 * @param bitmap
	 * @param downX
	 * 			按下的点相对父控件的X坐标
	 * @param downY
	 * 			按下的点相对父控件的X坐标
	 */
	private void createDragImage(Bitmap bitmap, int downX , int downY){
		mWindowLayoutParams = new WindowManager.LayoutParams();
		mWindowLayoutParams.format = PixelFormat.TRANSLUCENT; //图片之外的其他地方透明
		mWindowLayoutParams.gravity = Gravity.TOP | Gravity.LEFT;
		mWindowLayoutParams.x = downX - mPoint2ItemLeft + mOffset2Left;
		mWindowLayoutParams.y = downY - mPoint2ItemTop + mOffset2Top - mStatusHeight;
		mWindowLayoutParams.alpha = 1f; //透明度
		mWindowLayoutParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
		mWindowLayoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
		mWindowLayoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
				| WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE ;

		mDragImageView = new ImageView(getContext());
		mDragImageView.setImageBitmap(bitmap);
		mDragImageView.setBackgroundColor(getContext().getResources().getColor(android.R.color.transparent));
		mWindowManager.addView(mDragImageView, mWindowLayoutParams);
	}

	/**
	 * 从界面上面移动拖动镜像
	 */
	private void removeDragImage(){
		if(mDragImageView != null){
			mWindowManager.removeView(mDragImageView);
			mDragImageView = null;
		}
	}

	/**
	 * 拖动item，在里面实现了item镜像的位置更新，item的相互交换以及GridView的自行滚动
	 * @param moveX
	 * @param moveY
	 */
	private void onDragItem(int moveX, int moveY){
		if(moveX==0&&moveY==0){
			return;
		}

		mWindowLayoutParams.x = moveX - mPoint2ItemLeft + mOffset2Left;
		mWindowLayoutParams.y = moveY - mPoint2ItemTop + mOffset2Top - mStatusHeight;
		mWindowManager.updateViewLayout(mDragImageView, mWindowLayoutParams); //更新镜像的位置
		onSwapItem(moveX, moveY);

		//GridView自动滚动
		mHandler.post(mScrollRunnable);
	}


	/**
	 * 当moveY的值大于向上滚动的边界值，触发GridView自动向上滚动
	 * 当moveY的值小于向下滚动的边界值，触犯GridView自动向下滚动
	 * 否则不进行滚动
	 */
	private Runnable mScrollRunnable = new Runnable() {

		@Override
		public void run() {
			int scrollY;
			if(moveY > mUpScrollBorder){
				scrollY = -speed;
				mHandler.postDelayed(mScrollRunnable, 25);
			}else if(moveY < mDownScrollBorder){
				scrollY = speed;
				mHandler.postDelayed(mScrollRunnable, 25);
			}else{
				scrollY = 0;
				mHandler.removeCallbacks(mScrollRunnable);
			}

			//当我们的手指到达GridView向上或者向下滚动的偏移量的时候，可能我们手指没有移动，但是DragGridView在自动的滚动
			//所以我们在这里调用下onSwapItem()方法来交换item
			onSwapItem(moveX, moveY);

			View view = getChildAt(mDragPosition - getFirstVisiblePosition());
			//实现GridView的自动滚动
//			smoothScrollToPositionFromTop(mDragPosition, view.getTop() + scrollY);
		}
	};

	/**
	 * 交换item,并且控制item之间的显示与隐藏效果
	 * @param moveX
	 * @param moveY
	 */
	private void onSwapItem(int moveX, int moveY){
		if(moveX==0&&moveY==0){
			return;
		}

		//获取我们手指移动到的那个item的position
		int tempPosition = pointToPosition(moveX, moveY);

		//假如tempPosition 改变了并且tempPosition不等于-1,则进行交换
		if(tempPosition != mDragPosition && tempPosition != AdapterView.INVALID_POSITION){
			if(hasLoadMore&&tempPosition==getChildCount()-1){
			}else{
//			slideViews(mDragPosition, tempPosition);

//			getChildAt(mDragPosition - getFirstVisiblePosition()).setVisibility(View.INVISIBLE);//之前的item显示出来
				//显示之前的item，再隐藏现在的item
//			if(mHidePosition != mDragPosition){
//				getChildAt(mHidePosition - getFirstVisiblePosition()).setVisibility(View.VISIBLE);
//				mHidePosition = mDragPosition;
//			}
//				mStartDragItemView.setBackgroundColor(getContext().getResources().getColor(R.color.white));

				if(onChanageListener != null){
					onChanageListener.onChange(mDragPosition, tempPosition);
				}
				//无加载更多按钮，并且不为最后一个
//				getChildAt(tempPosition - getFirstVisiblePosition()).setVisibility(View.INVISIBLE);//拖动到了新的item,新的item隐藏掉
				getChildAt(mDragPosition - getFirstVisiblePosition()).setVisibility(View.VISIBLE);//之前的item显示出来
				mDragPosition = tempPosition;
			}

		}
	}


	/**
	 * 停止拖拽我们将之前隐藏的item显示出来，并将镜像移除
	 */
	private void onStopDrag(int X, int Y){
		if(onChanageListener != null){
			onChanageListener.onFinish(-1);
		}
		int tempPosition = pointToPosition(X, Y);
		//如果是最后一个，不能拖动
		if(hasLoadMore&&tempPosition==getChildCount()-1){
			if(onChanageListener!=null){
				onChanageListener.onDragToLast(mDragPosition);
			}
		}
		if(getChildAt(mDragPosition - getFirstVisiblePosition())!=null){
			getChildAt(mDragPosition - getFirstVisiblePosition()).setVisibility(View.VISIBLE);
		}
		removeDragImage();
		mDragPosition = -1;
	}

	/**
	 * 获取状态栏的高度
	 * @param context
	 * @return
	 */
	private static int getStatusHeight(Context context){
		int statusHeight = 0;
		Rect localRect = new Rect();
		((Activity) context).getWindow().getDecorView().getWindowVisibleDisplayFrame(localRect);
		statusHeight = localRect.top;
		if (0 == statusHeight){
			Class<?> localClass;
			try {
				localClass = Class.forName("com.android.internal.R$dimen");
				Object localObject = localClass.newInstance();
				int i5 = Integer.parseInt(localClass.getField("status_bar_height").get(localObject).toString());
				statusHeight = context.getResources().getDimensionPixelSize(i5);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return statusHeight;
	}


	/**
	 *
	 * @author xiaanming
	 *
	 */
	public interface OnChanageListener{

		/**
		 * 当item交换位置的时候回调的方法，我们只需要在该方法中实现数据的交换即可
		 * @param form
		 * 			开始的position
		 * @param to
		 * 			拖拽到的position
		 */
		public void onChange(int form, int to);

		/**
		 * 完成拖拽
		 * @param inVisibleIndex
		 */
		public void onFinish(int inVisibleIndex);

		/**
		 * 点击空白区域
		 */
		public void onNothingClick();

		/**
		 * 长按
		 */
		public void onLongClick(int index);

		/**
		 * 拖动到最后
		 * @param dragIndex
		 */
		public void onDragToLast(int dragIndex);
	}

	public void setEnable(boolean enable) {
		this.enable = enable;
	}

	public void setHasLoadMore(boolean hasLoadMore) {
		this.hasLoadMore = hasLoadMore;
	}

	@Override
	protected void dispatchDraw(Canvas canvas) {
		super.dispatchDraw(canvas);
		if(getChildCount()==0){return;}
		View localView1 = getChildAt(0);
		// 判断画线
		if(drawLine){
			int column = getWidth() / localView1.getWidth();
			int childCount = getChildCount();
			Paint localPaint = new Paint();
			localPaint.setStyle(Paint.Style.STROKE);
			localPaint.setStrokeWidth(2);
			// 边框线的颜色
			localPaint.setColor(getContext().getResources().getColor(android.R.color.holo_green_light));

			for(int i = 0;i < childCount;i++){
				View cellView = getChildAt(i);
				if((i + 1) % column == 0){
					//每行最后一个元素，只画下边框
					canvas.drawLine(cellView.getLeft(), cellView.getBottom(), cellView.getRight(), cellView.getBottom(), localPaint);
				}else{
					//其他元素，画右边框和下边框
					canvas.drawLine(cellView.getRight(), cellView.getTop(), cellView.getRight(), cellView.getBottom(), localPaint);
					canvas.drawLine(cellView.getLeft(), cellView.getBottom(), cellView.getRight(), cellView.getBottom(), localPaint);
				}
			}
			if(childCount % column != 0){
				//画最后一行的边框
				for(int j = 0 ;j < (column-childCount % column)+1 ; j++){
					View lastView = getChildAt(childCount - 1);
					if(j<(column-childCount % column)){//最后一个元素，不画右边框
						canvas.drawLine(lastView.getRight() + lastView.getWidth() * j, lastView.getTop(), lastView.getRight() + lastView.getWidth()* j, lastView.getBottom(), localPaint);
					}
					//画下边框
					canvas.drawLine(lastView.getLeft() + lastView.getWidth() * j, lastView.getBottom(), lastView.getRight() + lastView.getWidth()* j, lastView.getBottom(), localPaint);
				}
			}
		}

	}

}

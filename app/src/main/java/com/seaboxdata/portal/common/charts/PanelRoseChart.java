package com.seaboxdata.portal.common.charts;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;

import com.linewell.utils.SystemUtils;

/**
 * 南丁格尔玫瑰图
 * https://blog.csdn.net/xcl168/article/details/23222417
 * Created by zjianning on 2018/5/19.
 */

public class PanelRoseChart extends View  {

    private int ScrWidth,ScrHeight;

    //演示用的百分比例,实际使用中，即为外部传入的比例参数
    private final float arrPer[] = new float[]{40f,50f,60f,35f,70f,80f,95f};
    //演示用标签
    private final String arrPerLabel[] = new String[]{"PostgreSQL","Sybase","DB2","国产及其它","MySQL","Ms Sql","Oracle"};
    //RGB颜色数组
    private final int arrColorRgb[][] = { {77, 83, 97},
            {148, 159, 181},
            {253, 180, 90},
            {52, 194, 188},
            {39, 51, 72},
            {255, 135, 195},
            {215, 124, 124}} ;


    public PanelRoseChart(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        DisplayMetrics dm = getResources().getDisplayMetrics();
        ScrHeight = SystemUtils.dip2px(context, 300);
        ScrWidth = dm.widthPixels;
    }
    public PanelRoseChart(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PanelRoseChart(Context context) {
        this(context, null);
    }


    public void onDraw(Canvas canvas){
        //画布背景
        canvas.drawColor(Color.WHITE);

        float cirX = ScrWidth / 2;
        float cirY = ScrHeight / 3 ;
        float radius = ScrHeight / 3 ;//150;

        float arcLeft = cirX - radius;
        float arcTop  = cirY - radius ;
        float arcRight = cirX + radius ;
        float arcBottom = cirY + radius ;
        RectF arcRF0 = new RectF(arcLeft ,arcTop,arcRight,arcBottom);

        //画笔初始化
        Paint PaintArc = new Paint();
        Paint PaintLabel = new Paint();
        PaintLabel.setColor(Color.BLACK);
        PaintLabel.setTextSize(28);

        PaintLabel.setAntiAlias(true);
        PaintArc.setAntiAlias(true);
        //位置计算类
        XChartCalc xcalc = new XChartCalc();

        float percentage = 0.0f;
        float currPer = 0.0f;
        float newRaidus = 0.0f;
        int i= 0;

        //将百分比转换为扇形半径长度
        percentage = 360 / arrPer.length;
//        Percentage = (float)(Math.round(Percentage *100))/100;

        for(i=0; i<arrPer.length; i++)
        {
            //将百分比转换为新扇区的半径
            newRaidus = radius * (arrPer[i]/ 100);
//            NewRaidus = (float)(Math.round(NewRaidus *100))/100;

            float NewarcLeft = cirX - newRaidus;
            float NewarcTop  = cirY - newRaidus ;
            float NewarcRight = cirX + newRaidus ;
            float NewarcBottom = cirY + newRaidus ;
            RectF NewarcRF = new RectF(NewarcLeft ,NewarcTop,NewarcRight,NewarcBottom);

            //分配颜色
            PaintArc.setARGB(255,arrColorRgb[i][0], arrColorRgb[i][1], arrColorRgb[i][2]);

            //在饼图中显示所占比例
            canvas.drawArc(NewarcRF, currPer, percentage, true, PaintArc);
            //计算百分比标签
            xcalc.CalcArcEndPointXY(cirX, cirY, radius - radius/2/2, currPer + percentage/2);
            //标识
            canvas.drawText(arrPerLabel[i],xcalc.getPosX(), xcalc.getPosY() ,PaintLabel);
            //下次的起始角度
            currPer += percentage;
        }
        //外环
        PaintLabel.setStyle(Paint.Style.STROKE);
        PaintLabel.setColor(Color.WHITE);
        canvas.drawCircle(cirX,cirY,radius,PaintLabel);

//        canvas.drawText("", 10, ScrHeight - 200, PaintLabel);

    }

}

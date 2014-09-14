package com.tobber.riding.util;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class MyView extends View {

	public MyView(Context context) {
		super(context);
	}
	public MyView(Context context, AttributeSet attrs) {  
        super(context, attrs);  
    } 
	
	@Override
	protected void onDraw(Canvas canvas) {
		//画笔
		Paint paint = new Paint();  
        paint.setColor(Color.RED);  
        //设置字体大小  
       // paint.setTextSize(100);  
          
        //让画出的图形是空心的  
        paint.setStyle(Paint.Style.STROKE);  
        //设置画出的线的 粗细程度  
        paint.setStrokeWidth(2);  
        //画圆  
        canvas.drawCircle(200, 200, 10, paint);  
	}

	
}

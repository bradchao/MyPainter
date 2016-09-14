package tw.brad.mypainter;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by brad on 2016/9/13.
 */
public class MyView extends View {
    private LinkedList<LinkedList<HashMap<String,Float>>> lines;
    private Resources res;
    private boolean isInit;
    private int viewW, viewH;
    private Bitmap bmpBall, bmpBg;
    private Matrix matrix;
    private Timer timer;
    private float ballX, ballY, ballW, ballH, dx, dy;
    private GestureDetector gd;

    public MyView(Context context, AttributeSet attrs) {
        super(context, attrs);
        lines = new LinkedList<>();
        res = context.getResources();
        matrix = new Matrix();
        timer = new Timer();

        gd = new GestureDetector(context, new MyGDListener());

    }

    private class MyGDListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onDown(MotionEvent e) {
            //Log.d("brad", "onDown");
            return true; //super.onDown(e);
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            Log.d("brad", "onFling:" + velocityX + "x" + velocityY);
            return super.onFling(e1, e2, velocityX, velocityY);
        }
    }




    Timer getTimer(){return timer;}
    private void init(){
        viewW = getWidth(); viewH = getHeight();
        ballW = viewW / 8f; ballH = ballW;

        bmpBg = BitmapFactory.decodeResource(res, R.drawable.bg);
        bmpBg = resizeBitmap(bmpBg, viewW,viewH);

        bmpBall = BitmapFactory.decodeResource(res, R.drawable.ball);
        bmpBall = resizeBitmap(bmpBall, ballW,ballH);

        dx = dy = 10;

        timer.schedule(new RefreshView(), 0, 40);
        timer.schedule(new BallTask(), 1000, 100);

        isInit = true;
    }

    private Bitmap resizeBitmap(Bitmap src, float newW, float newH){
        matrix.reset();
        matrix.postScale(newW/src.getWidth(), newH/src.getHeight());
        bmpBall = Bitmap.createBitmap(src,0,0,src.getWidth(),src.getHeight(),matrix, false);
        return bmpBall;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (!isInit) init();

        canvas.drawBitmap(bmpBg, 0,0,null);

        canvas.drawBitmap(bmpBall, ballX, ballY,null);


        Paint p = new Paint();
        p.setColor(Color.BLUE);
        p.setStrokeWidth(4);
        for (LinkedList<HashMap<String,Float>> line:lines) {
            for (int i = 1; i < line.size(); i++) {
                canvas.drawLine(line.get(i - 1).get("x"), line.get(i - 1).get("y"),
                        line.get(i).get("x"), line.get(i).get("y"), p);
            }
        }
    }

    private class RefreshView extends TimerTask {
        @Override
        public void run() {
            //invalidate();
            postInvalidate();
        }
    }
    private class BallTask extends TimerTask {
        @Override
        public void run() {
            if (ballX<0 || ballX + ballW > viewW) dx *= -1;
            if (ballY<0 || ballY + ballH > viewH) dy *= -1;
            ballX += dx; ballY += dy;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float ex = event.getX(), ey = event.getY();
        if (event.getAction() == MotionEvent.ACTION_DOWN){
            doTouchDown(ex, ey);
        }else if (event.getAction() == MotionEvent.ACTION_MOVE){
            doTouchMove(ex, ey);
        }
        //return true;
        return gd.onTouchEvent(event);
    }

    private void doTouchDown(float x, float y){
        LinkedList<HashMap<String,Float>> line =
                new LinkedList<>();
        lines.add(line);
        addPoint(x,y);
    }
    private void doTouchMove(float x, float y){
        addPoint(x,y);
    }
    private void addPoint(float x, float y){
        HashMap<String,Float> point =
                new HashMap<>();
        point.put("x", x); point.put("y", y);
        lines.getLast().add(point);
        invalidate();
    }


}

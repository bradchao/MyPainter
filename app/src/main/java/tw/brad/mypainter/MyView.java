package tw.brad.mypainter;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.HashMap;
import java.util.LinkedList;

/**
 * Created by brad on 2016/9/13.
 */
public class MyView extends View {
    private LinkedList<LinkedList<HashMap<String,Float>>> lines;

    public MyView(Context context, AttributeSet attrs) {
        super(context, attrs);
        lines = new LinkedList<>();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
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

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float ex = event.getX(), ey = event.getY();
        if (event.getAction() == MotionEvent.ACTION_DOWN){
            doTouchDown(ex, ey);
        }else if (event.getAction() == MotionEvent.ACTION_MOVE){
            doTouchMove(ex, ey);
        }
        return true;
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

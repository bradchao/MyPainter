package tw.brad.mypainter;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by brad on 2016/9/13.
 */
public class MyView extends View {
    public MyView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setBackgroundColor(Color.BLUE);
    }
}

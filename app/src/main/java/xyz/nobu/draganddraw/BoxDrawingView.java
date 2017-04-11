package xyz.nobu.draganddraw;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nobu on 4/10/17.
 */

public class BoxDrawingView extends View {

    private static final String TAG = "BoxDrawingView";

    private Box mCurrentBox;
    private List<Box> mBoxes = new ArrayList<>();
    private Paint mBoxPaint;
    private Paint mBackgroundPaint;

    /**
     * You write two constructors because your view could be instantiated
     * in code or from a layout file. Views instantiated from a layout
     * file receive an instance of AttributeSet containing the XML
     * attributes that were specified in XML. Even if you do not plan on
     * using both constructors, it is good practice to include them.
     */

    // Used when creating the view in code
    public BoxDrawingView(Context context) {
        this(context, null);
    }

    // Used when inflating the view from XML
    public BoxDrawingView(Context context, AttributeSet attrs) {
        super(context, attrs);

        // Paint the boxes a nice semitransparent red (ARGB)
        mBoxPaint = new Paint();
        mBoxPaint.setColor(0x22ff0000);

        // Paint the background off-white
        mBackgroundPaint = new Paint();
        mBackgroundPaint.setColor(0xfff8efe0);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // Fill the background
        canvas.drawPaint(mBackgroundPaint);

        for (Box box: mBoxes) {
            float left = Math.min(box.getOrigin().x, box.getCurrent().x);
            float right = Math.max(box.getOrigin().x, box.getCurrent().x);
            float top = Math.min(box.getOrigin().y, box.getCurrent().y);
            float bottom = Math.max(box.getOrigin().y, box.getCurrent().y);

            canvas.drawRect(left, top, right, bottom, mBoxPaint);
        }
    }

    /**
     * When your application is launched, all of its views are invalid. This
     * means that they have not drawn anything to the screen. To fix this
     * situation, Android calls the top-level View's draw() method. This
     * causes that view to draw itself, which causes its children to draw
     * themselves. Those children's children then draw themselves, and so on
     * down the hierarchy. When all the views in the hierarchy have drawn
     * themselves, the top-level View is no longer invalid.
     * The call to invalidate() that you make in response to ACTION_MOVE in
     * onTouchEvent(MotionEvent) makes the BoxDrawingView invalid again.
     * This causes it to redraw itself and will cause onDraw(Canvas) to be
     * called again.
     * Canvas and Paint are the two main drawing classes in Android.
     * The Canvas class has all the drawing operations you perform. The
     * methods you call on Canvas determine where and what you draw - a line,
     * a circle, a word, or a rectangle.
     * The Paint class determine how these operations are done. The methods
     * you call on Paint specify characteristics - whether shapes are filled,
     * which font text is drawn in, and what color lines are.
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        PointF current = new PointF(event.getX(), event.getY());
        String action = "";

        switch(event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                action = "ACTION_DOWN";
                // Reset drawing state
                mCurrentBox = new Box(current);
                mBoxes.add(mCurrentBox);
                break;
            case MotionEvent.ACTION_MOVE:
                action = "ACTION_MOVE";
                if (mCurrentBox != null) {
                    mCurrentBox.setCurrent(current);
                    invalidate();
                }
                break;
            case MotionEvent.ACTION_UP:
                action = "ACTION_UP";
                mCurrentBox = null;
                break;
            case MotionEvent.ACTION_CANCEL:
                action = "ACTION_CANCEL";
                mCurrentBox = null;
                break;
        }

        Log.i(TAG, action + " at x=" + current.x + ", y= " + current.y);
        return true;
    }
}

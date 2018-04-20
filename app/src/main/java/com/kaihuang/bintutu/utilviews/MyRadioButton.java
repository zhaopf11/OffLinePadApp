package com.kaihuang.bintutu.utilviews;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.widget.TintTypedArray;
import android.util.AttributeSet;
import android.widget.RadioButton;

import com.kaihuang.bintutu.R;


/**
 * Created by zhoux on 2017/8/14.
 */

public class MyRadioButton extends RadioButton {
    private float textSize;
    private float secondTextSize;
    private int textColor;
    private int selectedColor;
    private String first_text;
    private String second_text;
    private Rect rect = new Rect();
    private Context context;
    public MyRadioButton(Context context) {
        this(context, null);
    }


    public MyRadioButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        initAttrs(attrs);

    }

    public MyRadioButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);

    }


    private void initAttrs(AttributeSet attrs){
        TintTypedArray tintTypedArray =  TintTypedArray.obtainStyledAttributes(getContext(),attrs,
                R.styleable.Secondlinebutton);
        textSize = tintTypedArray.getDimension(R.styleable.Secondlinebutton_first_size, 36);
        secondTextSize = tintTypedArray.getDimension(R.styleable.Secondlinebutton_second_size, 60);
        textColor = tintTypedArray.getColor(R.styleable.Secondlinebutton_text_color, context.getResources().getColor(R.color.gray));
        selectedColor = tintTypedArray.getColor(R.styleable.Secondlinebutton_select_text_color, context.getResources().getColor(R.color.gray3));
        first_text = tintTypedArray.getString(R.styleable.Secondlinebutton_first_text);
        second_text = tintTypedArray.getString(R.styleable.Secondlinebutton_second_text);
        tintTypedArray.recycle();
        invalidate();

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint paint = new Paint();

        if (!isChecked()) {
            paint.setTextSize(textSize);
            paint.setColor(textColor);
            paint.getTextBounds(first_text, 0, first_text.length(), rect);
            float tagWidth =rect.width();
            int tagHeight = rect.height();
            int x = (int) (this.getWidth() - tagWidth) / 2;
            int y = this.getHeight() / 2;
            canvas.drawText(first_text, x, y, paint);
        } else {
            paint.setTextSize(secondTextSize);
            paint.setColor(selectedColor);
            paint.getTextBounds(first_text, 0, first_text.length(), rect);
            float tagWidth = paint.measureText(first_text);
            int tagHeight = rect.height();
            int x = (int) (this.getWidth() - tagWidth) / 2;
            int y = this.getHeight()/ 2;
            canvas.drawText(first_text, x, y, paint);

        }

    }

    public void setFirst_text(String str) {
        this.first_text = str;
    }

    public void setSecond_text(String str) {
        this.second_text = str;
    }

    public void setFirst_size(float first_size) {
        this.textSize = first_size;
    }

    public void setSecondTextSize(float secondTextSize) {
        this.secondTextSize = secondTextSize;
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
    }

    public void setSelectedColor(int textColor) {
        this.selectedColor = textColor;
    }

}

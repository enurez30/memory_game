package com.sera.memorygame.custom

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import com.sera.memorygame.R

class CircleThemeView : View {

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context) : super(context)

    private var paint: Paint = Paint().apply {
        this.color = ContextCompat.getColor(context, R.color.pink_200)
        this.isAntiAlias = true
        this.style = Paint.Style.FILL
        this.strokeWidth = 6F
    }
    private var paint2: Paint = Paint().apply {
        this.color = ContextCompat.getColor(context, R.color.pink_600Dark)
        this.isAntiAlias = true
        this.style = Paint.Style.FILL
        this.strokeWidth = 4F
    }

    private var rectangle: RectF = RectF()

    /**
     *
     */
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val viewWidth = MeasureSpec.getSize(widthMeasureSpec)
        val viewHeight = MeasureSpec.getSize(heightMeasureSpec)

        setMeasuredDimension(viewWidth, viewHeight)
    }

    /**
     *
     */
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        rectangle.set(10F, 10F, (width - 10).toFloat(), (height - 10).toFloat())
        canvas?.drawArc(rectangle, -45F, 180F, true, paint2)
        canvas?.drawArc(rectangle, -45F, -180F, true, paint)
    }

    /**
     *
     */
    fun setColors(mainColor: Int, secondaryColor: Int) {
        paint.color = mainColor
        paint2.color = secondaryColor
        invalidate()
    }
}
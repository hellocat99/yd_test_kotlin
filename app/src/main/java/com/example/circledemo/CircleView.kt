package com.example.circledemo

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

class CircleView : View {

    private var mPaint: Paint? = null
    private var oval: RectF? = null
    private var rectSetted = false

    private var degrees = 0f
    private var touchAngle: Double = 0.toDouble()

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    private fun init() {
        mPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mPaint!!.strokeWidth = 4f
        mPaint!!.style = Paint.Style.STROKE
        oval = RectF()
    }

    override fun onDraw(canvas: Canvas) {
        val w = width
        val h = height
        if (!rectSetted) {
            oval!!.left = (w / 2 - _R).toFloat()
            oval!!.top = (h / 2 - _R).toFloat()
            oval!!.right = (w / 2 + _R).toFloat()
            oval!!.bottom = (h / 2 + _R).toFloat()
            rectSetted = true
        }

        canvas.save()
        // 以 view 中间为圆点 旋转
        canvas.rotate(degrees, (w / 2).toFloat(), (h / 2).toFloat())

        mPaint!!.color = Color.BLUE
        // 3 点钟方向是 0 从 0 开始 画 180 度 顺时针转增长
        canvas.drawArc(oval!!, 0f, 180f, false, mPaint!!)
        mPaint!!.color = Color.RED
        // 从 180 开始 画 180 度
        canvas.drawArc(oval!!, 180f, 180f, false, mPaint!!)

        canvas.restore()
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val action = event.action
        when (action) {
            MotionEvent.ACTION_DOWN -> {
                touchAngle = getPositionAngle(event.x.toDouble(), event.y.toDouble())
                return true
            }
            MotionEvent.ACTION_MOVE -> {
                val angle = getPositionAngle(event.x.toDouble(), event.y.toDouble())
                if (touchAngle == angle) {
                    return true
                }
                degrees = (touchAngle - angle + degrees).toFloat() % 360
                touchAngle = angle
                invalidate()
                return true
            }
            MotionEvent.ACTION_UP -> return true
        }
        return super.onTouchEvent(event)
    }

    // 获取 touch 坐标的 角度
    private fun getPositionAngle(xTouch: Double, yTouch: Double): Double {
        // 基于 view 正中间坐标系的坐标
        val x = xTouch - width / 2
        val y = height.toDouble() - yTouch - (height / 2).toDouble()

        when (getPositionQuadrant(x, y)) {
            1 ->
                // hypot(x, y) = sqrt(x * x + y * y)
                // asin(直角边 / 斜边) = 直角边所对的角的弧度
                // 弧度转角度 = 180 / π × 弧度
                return Math.asin(y / Math.hypot(x, y)) * 180 / Math.PI
            2, 3 -> return 180 - Math.asin(y / Math.hypot(x, y)) * 180 / Math.PI
            4 -> return 360 + Math.asin(y / Math.hypot(x, y)) * 180 / Math.PI
            else ->
                // ignore, does not happen
                return 0.0
        }
    }

    //          ^
    //          |
    //    2     |    1
    //          |
    // x--------------------->
    //          |
    //    3     |    4
    //          |
    //          y
    //
    // 获取坐标象限
    private fun getPositionQuadrant(x: Double, y: Double): Int {
        return if (x >= 0) {
            if (y >= 0) 1 else 4
        } else {
            if (y >= 0) 2 else 3
        }
    }

    companion object  {
        private val _R = 200
    }

}

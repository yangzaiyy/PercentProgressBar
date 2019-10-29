package com.example.view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View



/**
 *  Created by yangzai
 *  on 2019-10-29 10:37
 *  Des:
 */
@Suppress("NAME_SHADOWING")
class ProgressBarView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : View(context, attrs, defStyleAttr)  {
    //绘制进度条和进度文字的画笔
    private var mProgressPaint: Paint? = null
    private var mTextPaint: Paint? = null


    //进度条的底色和完成进度的颜色
    private var mProgressBackColor: Int = 0
    private var mProgressForeColor: Int = 0

    //进度条上方现实的文字
    private var mProgressText: String? = null
    //进度文字的颜色
    private var mTextColor: Int = 0
    //进度文字的字体大小
    private var mTextSize: Int = 0

    //进度条的起始值，当前值和结束值
    private var currentProgress: Int = 0


    //进度条的高度
    private var mProgressBarHeight: Int = 0

    //view的上下内边距
    private var mPaddingTop: Int = 0
    private var mPaddingBottom: Int = 0

    //用于测量文字显示区域的宽度和高度
    private var mTextFontMetrics: Paint.FontMetricsInt? = null
    private var mTextBound: Rect? = null

    //进度条和进度文字显示框的间距
    private var mLine2TextDividerHeight: Int = 0

    //绘制进度条圆角矩形的圆角
    private var mRectCorn: Int = 0
    //计算文字显示区域的宽度
    private var textWidth: Int = 0

    init {
        if (attrs != null) {
            val attributes = context.obtainStyledAttributes(attrs, R.styleable.ProgressBarView)
            mProgressBackColor = attributes.getColor(R.styleable.ProgressBarView_firstColor, Color.RED)
            mProgressForeColor = attributes.getColor(R.styleable.ProgressBarView_secondColor, Color.BLUE)
            mTextColor = attributes.getColor(R.styleable.ProgressBarView_progressTextColor, Color.BLACK)
            currentProgress = attributes.getInt(R.styleable.ProgressBarView_currProgress, 0)
            mProgressText = "$currentProgress%"
            mTextSize = attributes.getDimensionPixelSize(R.styleable.ProgressBarView_progressTextSize, dp(8f).toInt())
            mProgressBarHeight = attributes.getDimensionPixelSize(R.styleable.ProgressBarView_progressHeight, dp(3f).toInt())
            mRectCorn = (mProgressBarHeight * 1.0f / 2 + 0.5f).toInt()
            mLine2TextDividerHeight = attributes.getDimensionPixelSize(R.styleable.ProgressBarView_progressMarginText, dp(2f).toInt())
            attributes.recycle()
        }
        init()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val measuredHeight = mPaddingTop + mTextFontMetrics!!.bottom - mTextFontMetrics!!.top + mLine2TextDividerHeight + mProgressBarHeight + mPaddingBottom
        setMeasuredDimension(measuredWidth, measuredHeight)
    }

    private fun init() {

        mTextBound = Rect()

        mProgressPaint = Paint()
        mProgressPaint!!.style = Paint.Style.FILL
        mProgressPaint!!.isAntiAlias = true
        mProgressPaint!!.strokeWidth = mProgressBarHeight.toFloat()

        mTextPaint = Paint()
        mTextPaint!!.color = mTextColor
        mTextPaint!!.isAntiAlias = true
        reCalculationTextSize()

        mPaddingTop = paddingTop
        mPaddingBottom = paddingBottom

    }

    private fun reCalculationTextSize() {
        mTextPaint!!.textSize = mTextSize.toFloat()
        mTextFontMetrics = mTextPaint!!.fontMetricsInt
        mTextPaint!!.getTextBounds(mProgressText, 0, mProgressText!!.length, mTextBound)

        textWidth = mTextBound!!.right - mTextBound!!.left + TEXT_LEFT_RIGHT_PADDING
    }

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        mProgressPaint!!.color = mProgressBackColor

        //计算开始绘制进度条的距离顶部的间距
        val progressBarMarginTop = mPaddingTop - mTextFontMetrics!!.top + mTextFontMetrics!!.bottom + mLine2TextDividerHeight

        //绘制进度条底部背景
        val rectF = RectF(0f,
            progressBarMarginTop.toFloat(), measuredWidth.toFloat(), (progressBarMarginTop + mProgressBarHeight).toFloat()
        )
        canvas.drawRoundRect(rectF, mRectCorn.toFloat(), mRectCorn.toFloat(), mProgressPaint!!)

        mProgressPaint!!.color = mProgressForeColor
        //绘制已经完成了的进度条
        val currProgress = (measuredWidth * currentProgress * 1.0f / 100)
        val rectProgress = RectF(0f,
            progressBarMarginTop.toFloat(), currProgress, (progressBarMarginTop + mProgressBarHeight).toFloat()
        )
        canvas.drawRoundRect(rectProgress, mRectCorn.toFloat(), mRectCorn.toFloat(),
            mProgressPaint!!
        )

        //绘制文字，存在textX小0的时候，绘制的文字将会看不见
        val textX = currProgress - textWidth
        mProgressText?.let {
            mTextPaint?.let { it1 ->
                canvas.drawText(it, if (textX > 0) textX-20 else 0f,
                    (mPaddingTop - mTextFontMetrics!!.top).toFloat(), it1
                )
            }
        }

    }


    fun resetLevelProgress(currentProgress: Int) {
        var currentProgress = currentProgress
        if (currentProgress >= 100) {
            this.currentProgress = currentProgress
            mProgressText = "100%"
            invalidate()
            return
        }
        this.currentProgress = currentProgress
        mProgressText = "$currentProgress%"
        reCalculationTextSize()
        invalidate()
    }

    private fun dp(dp: Float): Float {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, resources.displayMetrics)
    }

    companion object {
        //当进度条移动至最右端的时候给与一定的居左边距，因为API所计算出来的文字宽度有一定误差
        private const val TEXT_LEFT_RIGHT_PADDING = 5
    }
}
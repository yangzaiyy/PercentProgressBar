package com.example.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

/**
 *  Created by yangzai
 *  on 2019-10-28 17:12
 *  Des:
 */
class CicleView(context: Context?, attrs: AttributeSet?) : View(context, attrs) {
    private val mpaint: Paint = Paint()

    init {
        mpaint.color= Color.BLUE
        mpaint.strokeWidth= 1F
        mpaint.style=Paint.Style.FILL
    }

    override fun draw(canvas: Canvas?) {
        super.draw(canvas)
        canvas?.drawCircle(100f,100f,80f, mpaint)
    }

     fun drawLine(){
        mpaint.color= Color.RED
        invalidate()
    }

}
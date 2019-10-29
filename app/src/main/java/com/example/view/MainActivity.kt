package com.example.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    var isFirst = true
    var i = 0
    var mStartVideoHandler: Handler = Handler()

    var mStartVideoRunnable: Runnable = object : Runnable {
        override fun run() {
            i++
            isFirst=false
            if (i == 101) {
                i = 0
                isFirst=true
                return
            }
            levelView.resetLevelProgress(i)
            mStartVideoHandler.postDelayed(this, 100)
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        tv.setOnClickListener {
            if (isFirst) {
                mStartVideoRunnable.run()
            }

        }
    }

}

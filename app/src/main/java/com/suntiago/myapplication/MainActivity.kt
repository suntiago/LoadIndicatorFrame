package com.suntiago.myapplication

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.suntiago.baseui.ldc.LoadIndicatorFrame

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<LoadIndicatorFrame>(R.id.ldf_test).showEmpty()
    }
}

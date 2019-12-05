package com.example.project

import android.os.CountDownTimer
import java.util.*


data class Platform(var index: Int = 0,
                    var lvl: Int = 0,
                    val name: Array<String> = arrayOf("facebook","twitter","instagram","snapchat", "tiktok","tumblr","youtube"),
                    var time: Long = ((index+1)*15000).toLong(),
                    var generation: Int = ((index+1))*lvl,
                    var isFinished: Boolean = true,
                    var timer: CountDownTimer = object: CountDownTimer(0, 0) {
                        override fun onTick(millisUntilFinished: Long) {}

                        override fun onFinish() {}
                    }
)
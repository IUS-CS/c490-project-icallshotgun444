package com.example.project

import java.util.*


data class Platform(var index: Int = 0,
                    var lvl: Int = 0,
                    var name: Array<String> = arrayOf("facebook","twitter","instagram","snapchat", "tiktok","tumblr","youtube","linkedin","myspace"),
                    var time: String = "00:00"
)
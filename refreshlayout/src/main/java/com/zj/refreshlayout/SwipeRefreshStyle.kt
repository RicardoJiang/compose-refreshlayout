package com.zj.refreshlayout

enum class SwipeRefreshStyle {
    Translate,  //平移，即内容与Header一起向下滑动，Translate为固定样式
    FixedBehind, //固定在背后，即内容向下滑动，Header不动
    FixedContent //内容固定,Header向下滑动,即官方样式
}
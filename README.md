## QuickAF

[![Build Status](https://travis-ci.org/Jamling/QuickAF.svg?branch=master)](https://travis-ci.org/Jamling/QuickAF)
[![GitHub release](https://img.shields.io/github/release/jamling/QuickAF.svg?maxAge=3600)](https://github.com/Jamling/QuickAF)
[![Bintray](https://img.shields.io/bintray/v/jamling/maven/cn.ieclipse.af.svg?maxAge=86400)](https://bintray.com/jamling/maven/cn.ieclipse.af)


An Android framework library and demo to help you building your app quickly.

![screenshot](https://raw.githubusercontent.com/Jamling/QuickAF/master/screenshot/sample1.0.0.gif)


## Sample apk (scan or click to download sample apk)
[![sample apk](https://raw.githubusercontent.com/Jamling/QuickAF/master/screenshot/qr_quickaf.png)](https://github.com/Jamling/QuickAF/releases/download/v2.0.2/QuickAF2.0.2.2.-release.apk)

## Install

- Android Studio

```gradle
dependencies {
    compile 'cn.ieclipse.af:af-library:2.0.2'
}
android {
    // for target api >= 23 (Android 6.0)
    useLibrary('org.apache.http.legacy')
    ...
}
```

## Features
![architecture](https://raw.githubusercontent.com/Jamling/QuickAF/master/screenshot/struct.png)

### library

- Component
    - tab, tab+viewpager
    - cview, lots of custom views, e.g. Preference, FlowLayout, TableLayout, AutoPlayView
    - refresh, use RefreshLayout, support any RecyclerView, ListView and ScrollView
- Network
    - Volley+Gson, base on volley for data tranfer, gson for json parsing
    - Common Task, common REST API request task
    - Upload Task, compressed image upload task
- Utils, lots of utility tool class e.g.
    - DialogsUtils
    - AppUtils
    - FileUtils
    - SharedPrefsUtils

### 3rd library: (Use the existing wheels)

- DB, recommend to use orm library
    - Aorm see https://github.com/Jamling/Android-ORM
- Image
    - Universal-Image-Loader see https://github.com/nostra13/Android-Universal-Image-Loader
    - Fresco see https://github.com/facebook/fresco
- Pay 
    - af-pay see https://github.com/Jamling/af-pay

### In sample:

- Profile, sample user profile
    - login
    - register
    - forget password
- Common, common activity
    - BaseActivity
    - BaseFragment
    - H5Activity
    - BaseListFragment

## Blog
See http://www.ieclipse.cn/tags/QuickAF/

## Integrated library as source
SystemBarTint https://github.com/hexiaochun/SystemBarTint

SwipeMenuRecyclerView unkonwn

WheelView(@Deprecated) unkonwn

BadgeView https://github.com/stefanjauker/BadgeView

PagerSlidingTabStrip https://github.com/astuetz/PagerSlidingTabStrip

ScrollLayout(@Deprecated) http://blog.csdn.net/Yao_GUET

## Contributors

- [wangjiandett](https://github.com/wangjiandett)
- [HarryXR](https://github.com/HarryXR)

## Discuss
Join ![QQ群: 629153672](http://dl.ieclipse.cn/screenshots/quickaf_group.png)
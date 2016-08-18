## QuickAF
An Android framework library and demo to help you building your app quickly.

![screenshot](https://raw.githubusercontent.com/Jamling/QuickAF/master/screenshot/sample1.0.0.gif)


## Sample apk
![sample apk](https://raw.githubusercontent.com/Jamling/QuickAF/master/screenshot/qr_quickaf.png)

## Install

- Android Studio

```gradle
dependencies {
    compile 'cn.ieclipse.af:af-library:1.0.0'
}
android {
    // for target api >= 23 (Android 6.0)
    useLibrary('org.apache.http.legacy')
    ...
}
```

## Features
![architecture](https://raw.githubusercontent.com/Jamling/QuickAF/master/screenshot/struct.png)

library:

- Component
 - tab, tab+viewpager
 - cview, lots of custom views, e.g. Preference, FlowLayout, TableLayout, AutoPlayView
 - refresh, use RecycleView with pull to refresh, swipe menu, fix header features
- Network
 - Volley+Gson, base on volley for data tranfer, gson for json parsing
 - Common Task, common REST API request task
 - Upload Task, compressed image upload task
- Utils, lots of utility tool class e.g.
 - DialogsUtils
 - AppUtils
 - FileUtils
 - SharedPrefsUtils

3rd library: (Use the existing wheels)

- DB, recommend to use orm library
 - Aorm see https://github.com/Jamling/Android-ORM
- Image, 
 - Universal-Image-Loader see https://github.com/nostra13/Android-Universal-Image-Loader
 - Fresco see https://github.com/facebook/fresco

In sample:

- Profile, sample user profile 
 - login
 - register
 - forget
- Common, common activity
 - BaseActivity
 - BaseFragment
 - H5Activity
 - SelectPhotoActivity

## Blog
See http://www.ieclipse.cn/tags/QuickAF/

## Contributors

- [wangjiandett](https://github.com/wangjiandett)
- [HarryXR](https://github.com/HarryXR)

QuickAF中包含的组件

### 组件

- AfActivity/AfFragment：将界面分为上（标题栏），中（内容区域），下（底部固定栏）三部分。可定制状态栏及背景颜色，可插入操作引导层，可插入正在加载loading层，AfFragment支持懒加载。
- BaseListFragment：带下拉刷新和上拉加载的Fragment
- BasePagerTabFragment：Tab+ViewPager+Fragment的tab组件，tab组件集成了第三方的PagerSlidingTabStrip
- H5Activity/H5Fragment：多功能的h5页面
- FileChooserActivity：文件/文件夹选择组件
- ImageBrowserActivity：ViewPager+PhotoView的大图浏览组件
- MainBottomTab：RadioGroup+BadgeView实现的底部Tab导航，配合ViewPager+Fragment一起工作
- FilterTabHost：排序及过滤组件，点击弹出过滤及排序选项，配合FilterTabView一块使用。
- TableContainer：同时支持上下、左右滚动的表格，并且支持固定行和列。
- AutoPlayView：自动轮播组件，页面指示器可以自定义，当然也提供一套默认的

### 控件(自定义View)

- BadgeView：集成第三方的。
- BadgeView2：类似BadgeView，不过它不是一个View，可以在任意的地方(RadioButton)中使用达到BadgeView的效果
- FlowLayout：强大的流式布局，支持check，支持adapter，支持divider。
- RoundButton：圆角按钮，可设置圆角半径，边框厚度及颜色，背景颜色。有了它，几乎无需美工提供button类的切图了
- Preference：类似Android中的Preference，不过它是一个自定义view，可以在布局xml中任意使用，开发设置界面的利器
- RoundImageView：同其它第三方的RoundImageView，不推荐使用，第三方的图片加载库自带圆角功能。
- RoundMaskView：圆角狠起来连我自己都怕，不光是ImageView，任意的layout也可以圆角化。

### 列表及刷新
列表+刷新用得实在太广泛啦，所以特地拎出来。本框架中的列表及刷新要配合demo使用哦（demo中定义了外观，框架只是封装行为）这一块的耦合度比较高，不好单独拿出去用。

#### Adapter相关

- AfRecyclerAdapter：RecyclerView的adapter，需要注册AdapterDelegate，配合AfViewHolder。设计上略显复杂，建议参考示例。不过用上手之后会发现非常好用，展现数据只要向AfRecyclerAdapter注册delegate就行了。
- AfChoiceAdapter：继承自AfRecyclerAdapter，多了choice功能而已，支持单选/多选
- AfViewHolder：RecyclerView强行使用ViewHolder，我只好定义这么一个类，嗯，顺带着把RecyclerView的onItemClick和onItemClick实现了。
- AfBaseAdapter：ListView/GridView的adapter，没啥特点
- AfDataFilter：需要本地搜索/过滤列表中的数据时，可以设置到AfRecyclerAdapter或AfBaseAdapter以实现过滤功能。

#### RecyclerView

- RecyclerHelper：RecyclerView的辅助类，设置divider啊什么的
- ListDividerItemDecoration：与高版本的recycler包中的ListDividerItemDecoration一样，就当我剽窃自它吧。
- SwipeMenuLayout：Copy自第三方的，用于实现RecyclerView中的侧滑菜单。建议自己找第三方的swipe库。

#### 下拉刷新及上拉加载
本框架的下拉刷新/上拉加载功能很强大，支持扩展，配置也灵活，所以在使用上...呃，还是fork本项目，查看app中定义的那些empty view, footer view, base_refresh_xxx.xml吧。

- RefreshLayout：支持*任意*view的上拉加载。默认注册了RecyclerView、ScrollView、AbsListView(ListView/GridView)的上拉加载功能，这应该够用了吧，如果还不满足，自己注册一个上拉监听吧。
- VScrollView：继承自ScrollView，放在RefreshLayout中，就实现了ScrollView的上拉加载。
- EmptyView：无数据或加载失败时的view，在app中有common_ptr_empty_view.xml
- FooterView：上拉加载中，加载更多的view，配合recyclerview使用，在app中有common_ptr_footer_view.xml。

*注：如要独立使用，请参考app工程中的common包中的相关配置。建议使用BaseListFragment组件*




#UIAdapter01  -- UI 适配 横竖屏切换 values-land

## 界面介绍    
只有一个TextView, 横竖屏情况下距离顶端(layout_marginTop)的距离不同,需要在切换横竖屏时
自动切换

## 改动点    
1. AndroidManifest.xml    
configChange 值设置，是的activity在横竖屏切换时不重走生命周期

2. res/values res/values-land    
横竖屏给予不同的dimen值    

3. src/com.hanly/uiadapter01/UIAdapterActivity01.java    
**onConfigurationChanged** 重写    
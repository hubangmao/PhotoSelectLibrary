### 安卓图库选择

[点击查看视频](http://www.17sysj.com/video/lpds_11b08aa57785a)<br/> 
###使用
[使用](https://github.com/hubangmao/PhotoSelectLibrary/blob/master/app/src/main/java/com/hbm/hbm/MainActivity.java)<br/><br/> 

 //一个页面如果有多次选取图片 功能 需要先清除之前一次选中状态  SelectImgActivity.destroy(false);<br/> 
     ```java
    public void onClick(View view) {
        //第一步 图片选择成功回调
        SelectImgActivity.setOnImgSelectOkListener(new PhotoListener.OnImgSelectOkListener() {
            @Override
            public void onImgSelectOkListener(ArrayList<File> selectImagePathLists) {
                if (selectImagePathLists == null) {
                    return;
                }
                Log.i("main", TAG + "本次选择图片共" + selectImagePathLists.size() + "张");


            }
        });
        //第二步 设置最大选取图片数量 10
        SelectImgActivity.SET_SELECT_MAX_NUM = 10;
        //第三步 启动选择Activity
        startActivity(new Intent(this, SelectImgActivity.class));
        //外部 有取消选择图片功能需要调用此方法 更新选中状态
        //SelectImgActivity.setOnSelectImgDelete(new File(""));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //销毁时需要调用销毁
        SelectImgActivity.destroy(false);
    }
<br/> 
<br/> 
    ### 包结构介绍.<br/>
   #  1.<br/>
    activity包  =图片选择Activity+大图查看Activity<br/>
   #  2.<br/>
    adapter包   =图片选择适配器+相册分类适配器<br/>
   #  3.<br/>
    utils包     =图片缓存+拿到图片分类路径<br/>
   #  4.<br/>
    zoom包      =图片手势操作<br/>


  导入可能会遇到的异常<br/>
    该库的Theme如下 与使用者库不一需要调节两边为统一的主题哦<br/>

 ```xml
     <style name="AppTheme.NoActionBar">
            <item name="windowActionBar">false</item>
            <item name="windowNoTitle">true</item>
      </style>





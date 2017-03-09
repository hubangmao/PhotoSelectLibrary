### 安卓图库选择

[点击查看视频](http://www.17sysj.com/video/lpds_11b08aa57785a)<br/> 
### 三个页面如下
 ![image](https://github.com/hubangmao/PhotoSelectLibrary/blob/master/img1.jpg)
 ![image](https://github.com/hubangmao/PhotoSelectLibrary/blob/master/img2.jpg)
 ![image](https://github.com/hubangmao/PhotoSelectLibrary/blob/master/img3.jpg)

###使用


 //一个页面如果有多次选取图片 功能 需要先清除之前一次选中状态  SelectImgActivity.destroy(false);
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
        //SelectImgActivity.setOnSelectImgDelete();
    }
package com.imooc.service;

import com.imooc.pojo.Carousel;
import java.util.List;

/** @author afu */
public interface CarouselService {

  /**
   * 轮播图接口
   *
   * @param isShow 是否显示
   * @return 图片列表
   */
  public List<Carousel> queryAll(Integer isShow);
}

package com.imooc.service;

import com.imooc.utils.PagedGridResult;

/** @author afu */
public interface ItemsESService {

  /**
   * 分页查询商品
   *
   * @param keywords 关键字
   * @param sort 排序
   * @param page 页码
   * @param pageSize 每页数据数
   * @return 分页结果
   */
  PagedGridResult searhItems(String keywords, String sort, Integer page, Integer pageSize);
}

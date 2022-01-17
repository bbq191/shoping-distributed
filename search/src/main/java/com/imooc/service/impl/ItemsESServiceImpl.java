package com.imooc.service.impl;

import com.imooc.service.ItemsESService;
import com.imooc.utils.PagedGridResult;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ItemsESServiceImpl implements ItemsESService {
  @Autowired private RestHighLevelClient client;

  @Override
  public PagedGridResult searhItems(String keywords, String sort, Integer page, Integer pageSize) {
    // todo 将items映射到es中
    return null;
  }
}

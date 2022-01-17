package com.imooc.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** @author afu */
@RestController
@RequestMapping("items")
public class ItemsController {

  //  @Autowired private ItemsESService itemsESService;

  @GetMapping("/hello")
  public Object hello() {
    return "Hello Elasticsearch~";
  }

  //  @GetMapping("/es/search")
  //  public IMOOCJSONResult search(String keywords, String sort, Integer page, Integer pageSize) {
  //
  //    if (StringUtils.isBlank(keywords)) {
  //      return IMOOCJSONResult.errorMsg(null);
  //    }
  //
  //    if (page == null) {
  //      page = 1;
  //    }
  //
  //    if (pageSize == null) {
  //      pageSize = 20;
  //    }
  //
  //    page--;
  //
  //    PagedGridResult grid = itemsESService.searhItems(keywords, sort, page, pageSize);
  //
  //    return IMOOCJSONResult.ok(grid);
  //  }
}

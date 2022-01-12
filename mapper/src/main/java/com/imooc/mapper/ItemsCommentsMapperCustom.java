package com.imooc.mapper;

import com.imooc.my.mapper.MyMapper;
import com.imooc.pojo.ItemsComments;
import com.imooc.pojo.vo.MyCommentVo;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Param;

/** @author afu */
public interface ItemsCommentsMapperCustom extends MyMapper<ItemsComments> {

  /**
   * 保存评价
   *
   * @param map 参数列表 - 用户id，评价列表
   */
  void saveComments(Map<String, Object> map);

  /**
   * 查询评价列表
   *
   * @param map 参数列表 - 用户id
   * @return 评价列表
   */
  List<MyCommentVo> queryMyComments(@Param("paramsMap") Map<String, Object> map);
}

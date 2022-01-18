package com.imooc.service;

import org.springframework.web.multipart.MultipartFile;

/** @author afu */
public interface FdfsService {

  /**
   * 文件上传
   *
   * @param file 文件
   * @param fileExtName 扩展名
   * @return 报文
   * @throws Exception 异常
   */
  String upload(MultipartFile file, String fileExtName) throws Exception;

  /**
   * 文件上传oss
   *
   * @param file 文件
   * @param userId 用户id
   * @param fileExtName 扩展名
   * @return 报文
   * @throws Exception 异常
   */
  String uploadOSS(MultipartFile file, String userId, String fileExtName) throws Exception;
}

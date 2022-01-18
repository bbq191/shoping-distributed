package com.imooc.controller;

import com.imooc.pojo.Users;
import com.imooc.pojo.vo.UsersVo;
import com.imooc.resourse.FileResource;
import com.imooc.service.FdfsService;
import com.imooc.service.center.CenterUserService;
import com.imooc.utils.CookieUtils;
import com.imooc.utils.IMOOCJSONResult;
import com.imooc.utils.JsonUtils;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/** @author afu */
@RestController
@RequestMapping("fdfs")
public class CenterUserController extends BaseController {
  @Autowired private FileResource fileResource;
  @Autowired private CenterUserService centerUserService;
  @Autowired private FdfsService fdfsService;

  @PostMapping("uploadFace")
  public IMOOCJSONResult uploadFace(
      String userId, MultipartFile file, HttpServletRequest request, HttpServletResponse response)
      throws Exception {

    String path = "";
    // 开始文件上传
    if (file != null) {
      // 获得文件上传的文件名称
      String fileName = file.getOriginalFilename();
      if (StringUtils.isNotBlank(fileName)) {
        // 文件重命名  imooc-face.png -> ["imooc-face", "png"]
        String[] fileNameArr = fileName.split("\\.");
        // 获取文件的后缀名
        String suffix = fileNameArr[fileNameArr.length - 1];
        if (!"png".equalsIgnoreCase(suffix)
            && !"jpg".equalsIgnoreCase(suffix)
            && !"jpeg".equalsIgnoreCase(suffix)) {
          return IMOOCJSONResult.errorMsg("图片格式不正确！");
        }
        path = fdfsService.uploadOSS(file, userId, suffix);
        System.out.println(path);
      }
    } else {
      return IMOOCJSONResult.errorMsg("文件不能为空！");
    }
    if (StringUtils.isNotBlank(path)) {
      String finalUserFaceUrl = fileResource.getOssHost() + path;
      Users userResult = centerUserService.updateUserFace(userId, finalUserFaceUrl);
      UsersVo usersVo = conventUsersVo(userResult);
      CookieUtils.setCookie(request, response, "user", JsonUtils.objectToJson(usersVo), true);
    } else {
      return IMOOCJSONResult.errorMsg("上传头像失败");
    }
    return IMOOCJSONResult.ok();
  }
}

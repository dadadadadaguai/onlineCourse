package com.xuecheng.content.service;

import com.xuecheng.content.model.dto.CourseCategoryTreeDto;

import java.util.List;

/** 课程分类 */
public interface CourseCategoryService {

  /**
   * 课程树形分类结构查询
   *
   * @param id
   * @return
   */
  public List<CourseCategoryTreeDto> queryTreeNodes(String id);
}

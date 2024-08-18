package com.xuecheng.content.service.impl;

import com.xuecheng.content.mapper.CourseCategoryMapper;
import com.xuecheng.content.model.dto.CourseCategoryTreeDto;
import com.xuecheng.content.service.CourseCategoryService;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CourseCategoryServiceImpl implements CourseCategoryService {

  private final CourseCategoryMapper courseCategoryMapper;

  public CourseCategoryServiceImpl(CourseCategoryMapper courseCategoryMapper) {
    this.courseCategoryMapper = courseCategoryMapper;
  }

  /**
   * 课程分类树形结构查询
   *
   * @param id
   * @return
   */
  @Override
  public List<CourseCategoryTreeDto> queryTreeNodes(String id) {
    List<CourseCategoryTreeDto> courseCategoryTreeDtos = courseCategoryMapper.selectTreeNodes(id);
    // 将list转为map,并过滤掉根节点
    Map<String, CourseCategoryTreeDto> mapTemp =
        courseCategoryTreeDtos.stream()
            .filter(item -> !id.equals(item.getId()))
            .collect(Collectors.toMap(key -> key.getId(), value -> value, (key1, key2) -> key2));
    List<CourseCategoryTreeDto> categoryTreeDtos = new ArrayList<>();
    courseCategoryTreeDtos.stream()
        .filter(item -> !id.equals(item.getId()))
        .forEach(
            item -> {
              if (item.getParentid().equals(id)) {
                categoryTreeDtos.add(item);
              }
              // 找父节点
              CourseCategoryTreeDto courseCategoryTreeDto = mapTemp.get(item.getParentid());
              if (courseCategoryTreeDto != null) {
                if (courseCategoryTreeDto.getChildrenTreeNodes() == null) {
                  courseCategoryTreeDto.setChildrenTreeNodes(new ArrayList<CourseCategoryTreeDto>());
                }
                //子节点
                courseCategoryTreeDto.getChildrenTreeNodes().add(item);
              }
            });
    return categoryTreeDtos;
  }
}

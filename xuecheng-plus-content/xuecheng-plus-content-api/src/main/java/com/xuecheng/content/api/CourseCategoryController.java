package com.xuecheng.content.api;

import com.xuecheng.content.model.dto.CourseBaseInfoDto;
import com.xuecheng.content.model.dto.CourseCategoryTreeDto;
import com.xuecheng.content.service.CourseCategoryService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/** 数据字典、前端控制器 */
@RestController
@Slf4j
public class CourseCategoryController {

  private final CourseCategoryService categoryService;

  public CourseCategoryController(CourseCategoryService categoryService) {
    this.categoryService = categoryService;
  }

  @ApiOperation("查询课程分类信息")
  @GetMapping("/course-category/tree-nodes")
  public List<CourseCategoryTreeDto> queryTreeNodes() {
    return categoryService.queryTreeNodes("1");
  }
}

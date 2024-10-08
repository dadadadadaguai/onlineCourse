package com.xuecheng.content.api;

import com.xuecheng.base.model.PageParams;
import com.xuecheng.base.model.PageResult;
import com.xuecheng.content.model.dto.AddCourseDto;
import com.xuecheng.content.model.dto.CourseBaseInfoDto;
import com.xuecheng.content.model.dto.EditCourseDto;
import com.xuecheng.content.model.dto.QueryCourseParamsDto;
import com.xuecheng.content.model.po.CourseBase;
import com.xuecheng.content.service.CourseBaseInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;

/**
 * @author Mr.M
 * @version 1.0
 * @description TODO
 * @date 2023/2/11 15:44
 */
@Api(value = "课程信息管理接口", tags = "课程信息管理接口")
@RestController
public class CourseBaseInfoController {
  private final CourseBaseInfoService courseBaseInfoService;

  public CourseBaseInfoController(CourseBaseInfoService courseBaseInfoService) {
    this.courseBaseInfoService = courseBaseInfoService;
  }

  @ApiOperation("课程查询接口")
  @PostMapping("/course/list")
  public PageResult<CourseBase> list(
      PageParams pageParams,
      @RequestBody(required = false) QueryCourseParamsDto queryCourseParamsDto) {
    PageResult<CourseBase> courseBasePageResult =
        courseBaseInfoService.queryCourseBaseList(pageParams, queryCourseParamsDto);
    return courseBasePageResult;
  }

  @ApiOperation("新增课程")
  @PostMapping("/course")
  public CourseBaseInfoDto createCourseBase(@RequestBody @Validated AddCourseDto addCourseDto) {
    return courseBaseInfoService.createCourseBase(1232141425L, addCourseDto);
  }
  @ApiOperation("根据课程id查询课程信息")
  @GetMapping("/course/{courseId}")
  public  CourseBaseInfoDto getCourseBaseInfoById(@PathVariable @NotNull Long courseId){
      return courseBaseInfoService.getCourseBaseInfoById(courseId);
  }
  @ApiOperation("修改课程信息")
  @PutMapping("/course")
  public CourseBaseInfoDto modifyCourseBase(@RequestBody @Validated EditCourseDto editCourseDto){
    Long companyId = 1232141425L;
    return courseBaseInfoService.modifyCourseBase(companyId,editCourseDto);
  }

}

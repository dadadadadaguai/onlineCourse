package com.xuecheng.content.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xuecheng.base.model.PageParams;
import com.xuecheng.base.model.PageResult;
import com.xuecheng.content.mapper.CourseBaseMapper;
import com.xuecheng.content.model.dto.QueryCourseParamsDto;
import com.xuecheng.content.model.po.CourseBase;
import com.xuecheng.content.service.CourseBaseInfoService;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

@Service
public class CourseBaseInfoServiceImpl implements CourseBaseInfoService {
  private final CourseBaseMapper courseBaseMapper;

  public CourseBaseInfoServiceImpl(CourseBaseMapper courseBaseMapper) {
    this.courseBaseMapper = courseBaseMapper;
  }

  /**
   * 课程查询实现类
   *
   * @param params 分页参数
   * @param queryCourseParamsDto 查询参数
   * @return
   */
  @Override
  public PageResult<CourseBase> queryCourseBaseList(
      PageParams params, QueryCourseParamsDto queryCourseParamsDto) {
    // 查询条件
    LambdaQueryWrapper<CourseBase> queryWrapper = new LambdaQueryWrapper<>();
    queryWrapper.like(
        org.apache.commons.lang.StringUtils.isNotEmpty(queryCourseParamsDto.getCourseName()),
        CourseBase::getName,
        queryCourseParamsDto.getCourseName());
    // 根据课程审核状态
    queryWrapper.eq(
        StringUtils.isNotEmpty(queryCourseParamsDto.getAuditStatus()),
        CourseBase::getAuditStatus,
        queryCourseParamsDto.getAuditStatus());
    // TODO 根据课程发布状态
    queryWrapper.eq(
            StringUtils.isNotEmpty(queryCourseParamsDto.getPublishStatus()),
            CourseBase::getStatus,
            queryCourseParamsDto.getPublishStatus());

    // 分页参数
    Page<CourseBase> page = new Page<>(params.getPageNo(), params.getPageSize());
    // 分页查询E page 分页参数, @Param("ew") Wrapper<T> queryWrapper 查询条件
    Page<CourseBase> pageResult = courseBaseMapper.selectPage(page, queryWrapper);
    // 数据
    List<CourseBase> items = pageResult.getRecords();
    // 总记录数
    long total = pageResult.getTotal();

    // 准备返回数据 List<T> items, long counts, long page, long pageSize
    PageResult<CourseBase> courseBasePageResult =
        new PageResult<>(items, total, params.getPageNo(), params.getPageSize());
    return courseBasePageResult;
  }
}

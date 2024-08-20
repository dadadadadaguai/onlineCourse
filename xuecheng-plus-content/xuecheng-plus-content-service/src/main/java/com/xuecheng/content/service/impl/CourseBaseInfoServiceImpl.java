package com.xuecheng.content.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xuecheng.base.model.PageParams;
import com.xuecheng.base.model.PageResult;
import com.xuecheng.content.mapper.CourseBaseMapper;
import com.xuecheng.content.mapper.CourseMarketMapper;
import com.xuecheng.content.model.constant.CourseCharging;
import com.xuecheng.content.model.constant.CourseReleaseConstant;
import com.xuecheng.content.model.dto.AddCourseDto;
import com.xuecheng.content.model.dto.CourseBaseInfoDto;
import com.xuecheng.content.model.dto.QueryCourseParamsDto;
import com.xuecheng.content.model.po.CourseBase;
import com.xuecheng.content.model.po.CourseMarket;
import com.xuecheng.content.service.CourseBaseInfoService;
import com.xuecheng.content.model.constant.CourseAuditConstant;

import java.time.LocalDateTime;
import java.util.List;
import javax.validation.constraints.NotNull;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CourseBaseInfoServiceImpl implements CourseBaseInfoService {
  private final CourseBaseMapper courseBaseMapper;

  private final CourseMarketMapper CourseMarketMapper;
  private final CourseMarketMapper courseMarketMapper;

  public CourseBaseInfoServiceImpl(
      CourseBaseMapper courseBaseMapper, CourseMarketMapper courseMarketMapper) {
    this.courseBaseMapper = courseBaseMapper;
    CourseMarketMapper = courseMarketMapper;
    this.courseMarketMapper = courseMarketMapper;
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
    // 根据课程发布状态
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

  /**
   * 新增课程基本信息
   *
   * @param companyId
   * @param dto
   * @return
   */
  @Transactional
  @Override
  public CourseBaseInfoDto createCourseBase(Long companyId, @NotNull AddCourseDto dto) {
    if (StringUtils.isBlank(dto.getName())) {
      throw new RuntimeException("课程名称为空");
    }
    if (StringUtils.isBlank(dto.getMt())) {
      throw new RuntimeException("课程分类为空");
    }
    if (StringUtils.isBlank(dto.getSt())) {
      throw new RuntimeException("课程分类为空");
    }
    if (StringUtils.isBlank(dto.getUsers())) {
      throw new RuntimeException("适用人群为空");
    }
    if (StringUtils.isBlank(dto.getGrade())) {
      throw new RuntimeException("课程等级为空");
    }
    if (StringUtils.isBlank(dto.getTeachmode())) {
      throw new RuntimeException("教学模式为空");
    }
    if (StringUtils.isBlank(dto.getCharge())) {
      throw new RuntimeException("收费规则为空");
    }
    CourseBase courseBase = new CourseBase();
    BeanUtils.copyProperties(dto, courseBase);
    courseBase.setAuditStatus(CourseAuditConstant.COURSE_AUDIT_STATUS_UNSUBMITTED);
    courseBase.setStatus(CourseReleaseConstant.COURSE_RELEASE_STATUS_UNRELEASE);
    courseBase.setCreateDate(LocalDateTime.now());
    courseBase.setCompanyId(companyId);
    int insert = courseBaseMapper.insert(courseBase);
    if (insert <= 0) {
      throw new RuntimeException("新增课程基本信息失败");
    }
    // 新增课程营销信息
    CourseMarket courseMarket = new CourseMarket();
    BeanUtils.copyProperties(dto, courseMarket);
    courseMarket.setId(courseBase.getId());
    int insertCourseMarketResult = saveCourseMarket(courseMarket);
    if (insertCourseMarketResult <= 0) {
      throw new RuntimeException("新增课程营销信息失败");
    }
    return getCourseBaseInfoDto(courseBase.getId());
  }

  // 获取课程基本信息及课程营销信息
  private CourseBaseInfoDto getCourseBaseInfoDto(@NotNull Long id) {
    CourseBaseInfoDto courseBaseInfoDto = new CourseBaseInfoDto();
    CourseBase courseBase = courseBaseMapper.selectById(id);
    if (courseBase == null) {
      return null;
    }
    BeanUtils.copyProperties(courseBase, courseBaseInfoDto);
    CourseMarket courseMarket = courseMarketMapper.selectById(id);
    if (courseMarket != null) {
      BeanUtils.copyProperties(courseMarket, courseBaseInfoDto);
    }

    //设置大分类、小分类名称
    CourseBase courseBaseByMt = courseBaseMapper.selectById(courseBase.getMt());
    courseBaseInfoDto.setMt(courseBaseByMt.getMt());
    CourseBase courseBaseBySt = courseBaseMapper.selectById(courseBase.getSt());
    courseBaseInfoDto.setSt(courseBaseBySt.getSt());
    return courseBaseInfoDto;
  }

  /**
   * 新增课程营销信息
   *
   * @param courseMarket
   * @return
   */
  private int saveCourseMarket(CourseMarket courseMarket) {
    // 收费规则
    String charge = courseMarket.getCharge();
    if (StringUtils.isBlank(charge)) {
      throw new RuntimeException("收费规则为空");
    }
    // 收费
    if (charge.equals(CourseCharging.COURSE_CHARGE_CHARGE)) {
      Float price = courseMarket.getPrice();
      if (price == null || price.floatValue() <= 0) {
        throw new RuntimeException("课程价格不能为空且必须大于0");
      }
    }
    CourseMarket courseMarketNew = CourseMarketMapper.selectById(courseMarket.getId());
    if (courseMarketNew == null) {
      return courseMarketMapper.insert(courseMarket);
    } else {
      BeanUtils.copyProperties(courseMarket, courseMarketNew);
      courseMarketNew.setId(courseMarket.getId());
      return courseMarketMapper.updateById(courseMarketNew);
    }
  }
}

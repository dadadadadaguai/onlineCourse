package com.xuecheng.content;

import com.xuecheng.base.model.PageParams;
import com.xuecheng.content.model.dto.QueryCourseParamsDto;
import com.xuecheng.content.service.impl.CourseBaseInfoServiceImpl;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Log4j2
class CourseBaseInfoServiceTest {
  @Autowired
  private CourseBaseInfoServiceImpl courseBaseInfoService;

  /** 测试课程分页查询 */
  @Test
  void queryCourseBaseList() {
    PageParams pageParams = new PageParams().setPageNo(1L).setPageSize(3L);
    QueryCourseParamsDto queryCourseParamsDto =
        new QueryCourseParamsDto()
            .setCourseName("java")
            .setAuditStatus("202004")
            .setPublishStatus("203001");
    log.info(courseBaseInfoService.queryCourseBaseList(pageParams, queryCourseParamsDto));
  }
}

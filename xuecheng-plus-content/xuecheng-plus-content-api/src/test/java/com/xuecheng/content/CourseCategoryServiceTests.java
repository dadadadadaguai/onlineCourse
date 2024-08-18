package com.xuecheng.content;

import com.xuecheng.content.model.dto.CourseCategoryTreeDto;
import com.xuecheng.content.service.CourseCategoryService;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class CourseCategoryServiceTests {

  private static final Logger log = LoggerFactory.getLogger(CourseCategoryServiceTests.class);
  @Autowired CourseCategoryService courseCategoryService;

  @Test
  void testqueryTreeNodes() {
    List<CourseCategoryTreeDto> categoryTreeDtos = courseCategoryService.queryTreeNodes("1");
    log.info("categoryTreeDtos={}", categoryTreeDtos);
  }
}

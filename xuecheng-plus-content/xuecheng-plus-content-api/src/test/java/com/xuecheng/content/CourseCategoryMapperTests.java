package com.xuecheng.content;

import com.xuecheng.content.mapper.CourseCategoryMapper;
import com.xuecheng.content.model.dto.CourseCategoryTreeDto;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class CourseCategoryMapperTests {
  private static final Logger log = LoggerFactory.getLogger(CourseCategoryMapperTests.class);
  @Autowired private CourseCategoryMapper courseCategoryMapper;

  @Test
  public void testCategoryMapper() {
    List<CourseCategoryTreeDto> courseCategoryTreeDtos = courseCategoryMapper.selectTreeNodes("1");
    log.info("courseCategoryTreeDtos={}", courseCategoryTreeDtos);
  }
}

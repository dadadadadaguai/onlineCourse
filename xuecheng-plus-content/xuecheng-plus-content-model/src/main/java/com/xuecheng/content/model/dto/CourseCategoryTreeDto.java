package com.xuecheng.content.model.dto;

import com.xuecheng.content.model.po.CourseCategory;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.util.List;

@Data
public class CourseCategoryTreeDto extends CourseCategory implements Serializable {
    private static final long serialVersionUID = 1L;

    List<CourseCategoryTreeDto> childrenTreeNodes;
}

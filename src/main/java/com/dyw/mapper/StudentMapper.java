package com.dyw.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dyw.entity.Student;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface StudentMapper extends BaseMapper<Student> {
}

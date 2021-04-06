package com.dyw.service;

import com.dyw.entity.Student;
import com.dyw.utils.AjaxResult;

public interface StudentService {

    AjaxResult register(String jsonString);

    AjaxResult login(String jsonString);
}

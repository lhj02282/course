package com.dyw.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.dyw.entity.Student;
import com.dyw.mapper.StudentMapper;
import com.dyw.service.StudentService;
import com.dyw.utils.AjaxResult;
import com.dyw.utils.ConcurrentHashMapCacheUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;

@Service
@Transactional
public class StudentServiceImpl implements StudentService {

    @Autowired
    private StudentMapper studentMapper;

    @Override
    public AjaxResult register(String jsonString) {
        try {
            JSONObject jsonObject = JSON.parseObject(jsonString);
            String name = jsonObject.getString("name");
            String studentId = jsonObject.getString("studentId");
            String phone = jsonObject.getString("phone");
            String password = jsonObject.getString("password");
            String codeKey = jsonObject.getString("codeKey");
            String code = jsonObject.getString("code");
            //判断验证码是否正确
            AjaxResult ajaxResult = checkCode(codeKey, code);
            if (!ajaxResult.isState()){
                return ajaxResult;
            }
            //注册
            QueryWrapper<Student> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("studentId",studentId);
            Student studentDB = studentMapper.selectOne(queryWrapper);
            if (studentDB!=null){
                return new AjaxResult(false,"该学号已注册");
            }
            Student student = new Student();
            student.setName(name).setPhone(phone).setStudentId(studentId).setPassword(password);
            studentMapper.insert(student);
            return new AjaxResult(true,"注册成功");
        }catch (Exception e){
            e.printStackTrace();
            return new AjaxResult(false,"注册失败");
        }
    }

    @Override
    public AjaxResult login(String jsonString) {
        try {
            JSONObject jsonObject = JSON.parseObject(jsonString);
            String studentId = jsonObject.getString("studentId");
            String password = jsonObject.getString("password");
            String codeKey = jsonObject.getString("codeKey");
            String code = jsonObject.getString("code");
            //检查验证码
            AjaxResult checkResult = checkCode(codeKey, code);
            if (!checkResult.isState()){
                return checkResult;
            }
            //登录
            QueryWrapper<Student> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("studentId",studentId);
            Student studentDB = studentMapper.selectOne(queryWrapper);
            if (studentDB==null){
                return new AjaxResult(false,"该账号不存在");
            }
            if (!studentDB.getPassword().equalsIgnoreCase(password)){
                return new AjaxResult(false,"密码错误");
            }
            return new AjaxResult(true,"登录成功",studentDB);
        }catch (Exception e){
            e.printStackTrace();
            return new AjaxResult(false,"登录失败");
        }
    }

    private AjaxResult checkCode(String codeKey,String code){
        //删除过期的验证码
        ConcurrentHashMapCacheUtils.deleteTimeout();
        Object cache = ConcurrentHashMapCacheUtils.getCache(codeKey);
        if (cache==null){
            return new AjaxResult(false,"验证码已过期，请刷新");
        }
        String codeCache = (String) cache;
        if (!codeCache.equalsIgnoreCase(code)){
            return new AjaxResult(false,"验证码错误");
        }
        return new AjaxResult(true,"验证码正确");
    }
}

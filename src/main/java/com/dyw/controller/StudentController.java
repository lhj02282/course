package com.dyw.controller;

import com.alibaba.druid.util.StringUtils;
import com.dyw.service.StudentService;
import com.dyw.utils.AjaxResult;
import com.dyw.utils.ConcurrentHashMapCacheUtils;
import com.dyw.utils.VerifyCodeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Base64Utils;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.UUID;

@RestController
@RequestMapping("/stu")
public class StudentController {

    @Autowired
    private StudentService studentService;

    @GetMapping("/getCode")
    public AjaxResult getCode(String codeKey){
        try {
            String code = VerifyCodeUtils.generateVerifyCode(4);
            //后台测试接口时用，在控制台打印出正确验证码，减少测试时看验证码的无用功，直接在postman中输入就行
            System.out.println("正确的验证码是："+code);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            VerifyCodeUtils.outputImage(60,30,byteArrayOutputStream,code);
            String string = "data:image/png;base64,"+Base64Utils.encodeToString(byteArrayOutputStream.toByteArray());
            //根据codeKey保存验证码值到本地内存中(10分钟的有效时间)
            if (StringUtils.isEmpty(codeKey)){
                codeKey = UUID.randomUUID().toString();
            }
            ConcurrentHashMapCacheUtils.setCache(codeKey,code,600L);
            HashMap<String, Object> map = new HashMap<>();
            map.put("codeKey",codeKey);
            map.put("img",string);
            return new AjaxResult(true,"获取验证码成功",map);
        }catch (Exception e){
            e.printStackTrace();
            return new AjaxResult(false,"获取验证码失败");
        }
    }

    @PostMapping("/register")
    public AjaxResult register(@RequestBody String jsonString){
        AjaxResult result = studentService.register(jsonString);
        return result;
    }

    @PostMapping("/login")
    public AjaxResult login(@RequestBody String jsonString){
        AjaxResult result = studentService.login(jsonString);
        return result;
    }

    @PostMapping("/resetPassword")
    public AjaxResult resetPassword(@RequestBody String jsonString){
        AjaxResult result = studentService.resetPassword(jsonString);
        return result;
    }
}

package com.dyw.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//自定义一个缓存保存对象
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CacheObj {

    //缓存值
    private Object cacheValue;

    //缓存过期时间(-1代表永久保存)
    private Long ttlTime;
}

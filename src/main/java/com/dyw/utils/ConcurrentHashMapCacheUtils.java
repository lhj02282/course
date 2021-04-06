package com.dyw.utils;

import com.dyw.entity.CacheObj;

import com.dyw.entity.CacheObj;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ConcurrentHashMapCacheUtils {

    //当前缓存个数
    private static Integer CURRENT_SIZE = 0;

    //缓存保存对象
    private static Map<String, CacheObj> CACHE_OBJ_MAP = new ConcurrentHashMap<>(512);

    /**
     *  带过期时间的缓存
     * @param cacheKey
     * @param cacheValue
     * @param cacheTime   缓存的时间，单位：秒 , -1代表永久保存
     */
    public static void setCache(String cacheKey,Object cacheValue,Long cacheTime){
        if(CURRENT_SIZE>=350){//如果缓存大小大于350时，执行删除过期缓存操作
            deleteTimeout();
        }
        Long ttlTime = null;
        if (cacheTime>0){
            ttlTime = System.currentTimeMillis()+cacheTime*1000L;
            CacheObj cacheObj = new CacheObj(cacheValue, ttlTime);
            CACHE_OBJ_MAP.put(cacheKey,cacheObj);
            CURRENT_SIZE += 1;
        }else if (cacheTime == -1L){
            ttlTime = -1L;
            CacheObj cacheObj = new CacheObj(cacheValue, ttlTime);
            CACHE_OBJ_MAP.put(cacheKey,cacheObj);
            CURRENT_SIZE += 1;
        }
    }

    /**
     * 永久保存
     * @param cacheKey
     * @param cacheValue
     */
    public static void setCache(String cacheKey,Object cacheValue){
        setCache(cacheKey,cacheValue,-1L);
    }

    /**
     * 查看某个key是否存在(并且没有过期)
     * @param cacheKey
     * @return
     */
    public static boolean hasKey(String cacheKey){
        if (CACHE_OBJ_MAP.containsKey(cacheKey)){
            Long ttlTime = CACHE_OBJ_MAP.get(cacheKey).getTtlTime();
            if (ttlTime> System.currentTimeMillis()||ttlTime == -1L){
                return true;
            }
        }
        return false;
    }

    /**
     * 删除某个缓存
     * @param cacheKey
     */
    public static void deleteCache(String cacheKey){
        if (hasKey(cacheKey)){
            CACHE_OBJ_MAP.remove(cacheKey);
            CURRENT_SIZE -= 1;
        }
    }

    /**
     * 删除所有缓存
     */
    public static void deleteAll(){
        for (String key : CACHE_OBJ_MAP.keySet()){
            deleteCache(key);
        }
    }

    /**
     * 删除过期的缓存
     */
    public static void deleteTimeout(){
        for (Map.Entry<String,CacheObj> entry : CACHE_OBJ_MAP.entrySet()){
            Long ttlTime = entry.getValue().getTtlTime();
            if (ttlTime< System.currentTimeMillis() && ttlTime != -1L){
                deleteCache(entry.getKey());
            }
        }
    }

    /**
     * 根据key获取缓存
     * @param cacheKey
     * @return
     */
    public static Object getCache(String cacheKey){
        if(hasKey(cacheKey)){
            return CACHE_OBJ_MAP.get(cacheKey).getCacheValue();
        }
        return null;
    }
}

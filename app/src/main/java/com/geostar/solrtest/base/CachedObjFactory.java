package com.geostar.solrtest.base;

import java.util.HashMap;
import java.util.Map;

/**
 * 用于生成某一类的对象并缓存生成的对象，每个key对应的对象只能有一个
 *
 * @param <T> 对象类型
 * @author hanlyjiang on 2017/7/6-17:28.
 * @version 1.0
 */
public class CachedObjFactory<T> {

    /**
     * 封装对象生成方法
     */
    public interface ObjGenerator<O> {
        /**
         * 生成一个Fragment
         *
         * @return
         */
        O generateObj();
    }

    private Map<String, ObjGenerator<T>> generatorMap = new HashMap<>();
    private Map<String, T> objMap = new HashMap<>();

    /**
     * 添加对象生成器
     *
     * @param objKey       唯一 key
     * @param objGenerator 生成器对象
     */
    public void addGenerator(String objKey, ObjGenerator<T> objGenerator) {
        if (!generatorMap.containsKey(objKey)) {
            generatorMap.put(objKey, objGenerator);
        }
    }

    /**
     * 移除Generator和已经生成的对象
     *
     * @param objKey
     */
    public void remove(String objKey) {
        if (generatorMap.containsKey(objKey)) {
            generatorMap.remove(objKey);
        }
    }

    /**
     * 获得对象，如果有对象缓存，则使用缓存的对象，如果么有，则返回一个新的实例，并加入到缓存
     *
     * @param objKey
     * @return
     */
    public T get(String objKey) {
        return get(objKey, false);
    }


    /**
     * 获得对象，如果有对象缓存，则使用缓存的对象，如果么有，则返回一个新的实例，并加入到缓存
     *
     * @param objKey   页面KEY
     * @param recreate 强制 new 一个对象
     * @return
     */
    public T get(String objKey, boolean recreate) {
        T obj = null;
        if (objMap.containsKey(objKey) && !recreate) {
            return objMap.get(objKey);
        }

        if (generatorMap.containsKey(objKey)) {
            obj = generatorMap.get(objKey).generateObj();
        }
        objMap.put(objKey, obj);
        return obj;
    }

    /**
     * 清除所有已经生成的对象
     */
    public void clearGenObj() {
        objMap.clear();
    }

    /**
     * 清除所有已经生成的对象
     */
    public void removeObj(String objKey) {
        objMap.remove(objKey);
    }

}

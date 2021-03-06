# 调用Carousel服务

通过注入Carousel实现，可以快速访问注册到Carousel总线的服务：
```java
package org.lpw.ranch.util;

import com.alibaba.fastjson.JSONObject;

import java.util.Map;

/**
 * @author lpw
 */
public interface Carousel {
    /**
     * 根据ID获取Carousel服务数据。
     *
     * @param key 服务key。
     * @param id  ID值。
     * @return JSON数据，如果未找到则返回仅包含id属性的JSON数据。
     */
    JSONObject get(String key, String id);

    /**
     * 获取Carousel服务JSON.data数据/集。
     *
     * @param key       服务key。
     * @param header    头信息集。
     * @param parameter 参数集。
     * @param cacheable 是否缓存，如果为true则使用默认缓存时长。
     * @param jsonClass JSON类，JSONObject或JSONArray。
     * @param <T>       JSON类。
     * @return 服务JSON数据/集，获取失败或code不为0则返回空JSON数据/集。
     */
    <T> T service(String key, Map<String, String> header, Map<String, String> parameter, boolean cacheable, Class<T> jsonClass);

    /**
     * 获取Carousel服务JSON.data数据/集。
     *
     * @param key       服务key。
     * @param header    头信息集。
     * @param parameter 参数集。
     * @param cacheTime 缓存时长，单位：分钟。
     * @param jsonClass JSON类，JSONObject或JSONArray。
     * @param <T>       JSON类。
     * @return 服务JSON数据/集，获取失败或code不为0则返回空JSON数据/集。
     */
    <T> T service(String key, Map<String, String> header, Map<String, String> parameter, int cacheTime, Class<T> jsonClass);

    /**
     * 获取Carousel服务JSON数据。
     *
     * @param key       服务key。
     * @param header    头信息集。
     * @param parameter 参数集。
     * @param cacheable 是否缓存，如果为true则使用默认缓存时长。
     * @return 服务JSON数据，获取失败则返回空JSON数据。
     */
    JSONObject service(String key, Map<String, String> header, Map<String, String> parameter, boolean cacheable);

    /**
     * 获取Carousel服务JSON数据。
     *
     * @param key       服务key。
     * @param header    头信息集。
     * @param parameter 参数集。
     * @param cacheTime 缓存时长，单位：分钟。
     * @return 服务JSON数据，获取失败则返回空JSON数据。
     */
    JSONObject service(String key, Map<String, String> header, Map<String, String> parameter, int cacheTime);
}
```

> 关于Carousel的相关说明与使用，可参考[tephra-carousel](https://github.com/heisedebaise/tephra/tree/master/tephra-carousel)。

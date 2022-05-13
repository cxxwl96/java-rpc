/*
 * Copyright (c) 2021-2022, jad (cxxwl96@sina.com).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.cxxwl96.rpc.core.config;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONPath;
import com.alibaba.fastjson2.util.TypeUtils;

import cn.hutool.core.lang.Dict;
import cn.hutool.setting.yaml.YamlUtil;

/**
 * 配置
 *
 * @author cxxwl96
 * @since 2022/5/14 02:39
 */
public class ContextConfig {
    private static final String json;

    private static String host;

    private static Integer port;

    static {
        final Dict dict = YamlUtil.loadByPath("application.yaml");
        json = JSON.toJSONString(dict);
        host = getValue("rpc.host", String.class, "localhost");
        port = getValue("rpc.port", Integer.class, 5009);
    }

    private static <T> T getValue(String key, Class<T> clazz, T defaultValue) {
        final Object value = JSONPath.eval(json, key);
        if (value == null) {
            return defaultValue;
        }
        return TypeUtils.cast(value, clazz);
    }

    public static String getHost() {
        return host;
    }

    public static Integer getPort() {
        return port;
    }
}

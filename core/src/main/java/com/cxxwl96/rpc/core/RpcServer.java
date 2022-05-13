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

package com.cxxwl96.rpc.core;

import com.cxxwl96.rpc.core.annotation.RpcService;
import com.cxxwl96.rpc.core.config.ContextConfig;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ScheduledThreadPoolExecutor;

import cn.hutool.core.io.FileUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 服务端
 *
 * @author cxxwl96
 * @since 2022/5/13 22:15
 */
@Slf4j
public class RpcServer {
    public void start() {
        int port = ContextConfig.getPort();
        try (final ServerSocket socket = new ServerSocket(port)) {
            log.info("Rpc server start in {}", port);
            final Map<String, Object> classMap = scanRpcService();
            final ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(5);
            while (true) {
                final Socket accept = socket.accept();
                executor.execute(new RpcServerServiceThread(accept, classMap));
            }
        } catch (IOException | ClassNotFoundException | InstantiationException | IllegalAccessException exception) {
            log.error(exception.getMessage(), exception);
        }
    }

    /**
     * 扫描Rpc服务类
     *
     * @return rpc服务类, map< 接口名, 服务类实例 >
     */
    private Map<String, Object> scanRpcService()
        throws IOException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        final ClassLoader loader = Thread.currentThread().getContextClassLoader();
        final HashMap<String, Object> classMap = new HashMap<>();
        final URL url = loader.getResource("./");
        if (url == null) {
            throw new IOException("get resource error");
        }
        String parent = URLDecoder.decode(url.getPath(), Charset.defaultCharset().name());
        if (!FileUtil.exist(parent)) {
            throw new FileNotFoundException(parent);
        }
        // 得到所有的class文件
        final List<File> files = FileUtil.loopFiles(new File(parent));
        for (File file : files) {
            if (!FileUtil.pathEndsWith(file, ".class")) {
                continue;
            }
            // 加载类
            String absolutePath = file.getPath().replace(parent, "");
            final int length = absolutePath.length();
            absolutePath = absolutePath.startsWith(File.separator)
                ? absolutePath.substring(1, length - 6)
                : absolutePath.substring(0, length - 6);
            final String classStr = absolutePath.replace(File.separator, ".");
            final Class<?> clazz = loader.loadClass(classStr);
            // 过滤被RpcService注解的类
            if (clazz.isAnnotationPresent(RpcService.class)) {
                final Class<?> value = clazz.getDeclaredAnnotation(RpcService.class).value();
                classMap.put(value.getName(), clazz.newInstance());
                log.info("Loaded RPC service => interface: {}, implement: {}", value.getName(), clazz.getName());
            }
        }
        return classMap;
    }
}

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

import com.cxxwl96.rpc.core.lang.RpcRequest;
import com.cxxwl96.rpc.core.lang.RpcResponse;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Socket;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;

/**
 * 接收客户端请求并调用相应服务
 *
 * @author cxxwl96
 * @since 2022/5/13 23:17
 */
@Slf4j
public class RpcServerServiceThread implements Runnable {
    private final Socket socket;

    private final Map<String, Object> classMap;

    public RpcServerServiceThread(Socket socket, Map<String, Object> classMap) {
        this.socket = socket;
        this.classMap = classMap;
    }

    @Override
    public void run() {
        InputStream is = null;
        OutputStream os = null;
        ObjectInputStream ois = null;
        ObjectOutputStream oos = null;
        try {
            is = socket.getInputStream();
            ois = new ObjectInputStream(is);
            os = socket.getOutputStream();
            oos = new ObjectOutputStream(os);
            final RpcResponse response = new RpcResponse();
            // 获取请求数据
            final Object obj = ois.readObject();
            if (!(obj instanceof RpcRequest)) {
                final String error = obj.getClass().getName() + " is not instance of " + RpcRequest.class.getName();
                response.setError(new IOException(error));
                oos.writeObject(response);
                oos.flush();
                return;
            }
            // 查询服务类
            final RpcRequest request = (RpcRequest) obj;
            final Object service = classMap.get(request.getClazz());
            final Class<?> clazz = service.getClass();
            if (clazz == null) {
                throw new ClassNotFoundException(request.getClazz());
            }
            // 查询服务方法并执行
            final Method method = clazz.getMethod(request.getMethod(), request.getParamTypes());
            final Object result = method.invoke(service, request.getParams());
            // 返回结果
            response.setResult(result);
            oos.writeObject(response);
            oos.flush();
        } catch (IOException | ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InvocationTargetException exception) {
            log.error(exception.getMessage(), exception);
        } finally {
            try {
                if (ois != null) {
                    ois.close();
                }
                if (oos != null) {
                    oos.close();
                }
                if (is != null) {
                    is.close();
                }
                if (os != null) {
                    os.close();
                }
                if (socket != null) {
                    socket.close();
                }
            } catch (IOException exception) {
                log.error(exception.getMessage(), exception);
            }
        }
    }
}

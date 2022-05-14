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

import com.cxxwl96.rpc.core.config.ContextConfig;
import com.cxxwl96.rpc.core.lang.RpcRequest;
import com.cxxwl96.rpc.core.lang.RpcResponse;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;

import lombok.extern.slf4j.Slf4j;

/**
 * 发送请求调用服务
 *
 * @author cxxwl96
 * @since 2022/5/14 00:36
 */
@Slf4j
public class RpcClientService {
    public Object start(RpcRequest request) {
        String host = ContextConfig.getHost();
        int port = ContextConfig.getPort();
        Socket socket = null;
        InputStream is = null;
        OutputStream os = null;
        ObjectInputStream ois = null;
        ObjectOutputStream oos = null;
        try {
            // 发送请求
            socket = new Socket(host, port);
            os = socket.getOutputStream();
            oos = new ObjectOutputStream(os);
            oos.writeObject(request);
            oos.flush();
            // 获取请求数据
            is = socket.getInputStream();
            ois = new ObjectInputStream(is);
            final Object obj = ois.readObject();
            if (!(obj instanceof RpcResponse)) {
                final String error = obj.getClass().getName() + " is not instance of " + RpcResponse.class.getName();
                throw new RuntimeException(error);
            }
            final RpcResponse response = (RpcResponse) obj;
            // 判断是否异常
            if (response.getError() != null) {
                throw new RuntimeException(response.getError().getMessage(), response.getError());
            }
            // 获取结果
            return response.getResult();
        } catch (IOException | ClassNotFoundException exception) {
            log.error("Request rpc server[{}:{}]", host, port);
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
        return null;
    }
}

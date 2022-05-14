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

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * rpc请求动态代理
 *
 * @author cxxwl96
 * @since 2022/5/14 00:18
 */
public class RpcServiceProxy implements InvocationHandler {
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        final RpcRequest request = new RpcRequest().setClazz(method.getDeclaringClass().getName())
            .setMethod(method.getName())
            .setParamTypes(method.getParameterTypes())
            .setParams(args);
        return new RpcClientService().start(request);
    }

    @SuppressWarnings("unchecked")
    public static <T> T dynamicService(Class<T> clazz) {
        return (T) Proxy.newProxyInstance(clazz.getClassLoader(), new Class[] {clazz}, new RpcServiceProxy());
    }
}

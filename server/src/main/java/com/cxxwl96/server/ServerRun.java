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

package com.cxxwl96.server;

import com.cxxwl96.rpc.core.RpcServer;

/**
 * 服务端启动类
 *
 * @author cxxwl96
 * @since 2022/5/14 02:16
 */
public class ServerRun {
    public static void main(String[] args) {
        final RpcServer server = new RpcServer();
        server.start();
    }
}

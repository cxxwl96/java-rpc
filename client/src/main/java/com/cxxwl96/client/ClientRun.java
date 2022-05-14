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

package com.cxxwl96.client;

import com.cxxwl96.api.User;
import com.cxxwl96.api.UserService;
import com.cxxwl96.rpc.core.RpcServiceProxy;

import java.util.List;

import lombok.extern.slf4j.Slf4j;

/**
 * 客户端启动类
 *
 * @author cxxwl96
 * @since 2022/5/14 00:46
 */
@Slf4j
public class ClientRun {
    public static void main(String[] args) {
        final UserService service = RpcServiceProxy.dynamicService(UserService.class);
        final boolean success = service.save(new User().setName("cxxwl96").setAge(24));
        log.info("save user: {}", success);
        final List<User> list = service.list();
        log.info("user list: {}", list);
    }
}

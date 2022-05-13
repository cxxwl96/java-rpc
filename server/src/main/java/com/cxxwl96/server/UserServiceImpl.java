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

import com.cxxwl96.api.User;
import com.cxxwl96.api.UserService;
import com.cxxwl96.rpc.core.annotation.RpcService;

import java.util.ArrayList;
import java.util.List;

import lombok.extern.slf4j.Slf4j;

/**
 * UserServiceImpl
 *
 * @author cxxwl96
 * @since 2022/5/13 23:55
 */
@Slf4j
@RpcService(UserService.class)
public class UserServiceImpl implements UserService {
    @Override
    public boolean save(User user) {
        log.info("save: " + user.toString());
        return true;
    }

    @Override
    public List<User> list() {
        final List<User> users = new ArrayList<>();
        users.add(new User().setName("张三").setAge(20));
        users.add(new User().setName("李四").setAge(21));
        users.add(new User().setName("王五").setAge(22));
        return users;
    }
}

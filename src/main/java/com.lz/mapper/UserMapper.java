package com.lz.mapper;

import com.lz.entity.User;

import java.util.List;

/**
 * Created by inst1 on 2017/6/21.
 */
public interface UserMapper {
        List<User> selectAll();
        int count(Integer id);
        int countSuccess();
        User selectByPrimaryKey(int id);
}

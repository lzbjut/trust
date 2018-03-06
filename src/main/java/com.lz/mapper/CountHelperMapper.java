package com.lz.mapper;

import com.lz.entity.CountHelper;

import java.util.List;

/**
 * Created by inst1 on 2017/6/21.
 */
public interface CountHelperMapper {
    int insert(CountHelper countHelper);
    List<CountHelper> selectAll();
    int insertCountHelpers(List<CountHelper> list);

}

package com.lz.mapper;

import com.lz.entity.Ser;

import java.util.List;

/**
 * Created by inst1 on 2017/6/21.
 */
public interface ServiceMapper {
    List<Ser> selectByQuality(Integer low, Integer high);
    int count(Integer id);
    int countA(Integer id);
    Integer selectTotalGood();
    Integer selectTotalBad();
    Integer selectTotalRes();
}

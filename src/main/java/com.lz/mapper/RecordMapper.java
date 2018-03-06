package com.lz.mapper;

import com.lz.entity.Record;

import java.util.List;

/**
 * Created by inst1 on 2017/6/21.
 */
public interface RecordMapper {
    int insert(Record r);
    int clearAll();
    List<Record> selectByService(Integer se);
    int clearPart();
    List<Record> selectByUserAndService(Integer usr, Integer se);
}

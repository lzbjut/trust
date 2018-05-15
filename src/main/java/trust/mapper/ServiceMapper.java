package trust.mapper;

import trust.entity.Ser;

import java.util.List;

/**
 * Created by inst1 on 2017/6/21.
 */
public interface ServiceMapper {
    List<Ser> selectByQuality(Integer low, Integer high);
    int count(Integer id);
    int countA(Integer id);
}

package trust.mapper;

import trust.entity.Usage;

import java.util.List;

/**
 * Created by inst1 on 2017/11/6.
 */
public interface UsageMapper {
    int insertUsage(Usage usage);
    List<Integer> selectUserUsage(int se);
}

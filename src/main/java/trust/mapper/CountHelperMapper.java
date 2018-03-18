package trust.mapper;

import trust.entity.CountHelper;

import java.util.List;

/**
 * Created by inst1 on 2017/6/21.
 */
public interface CountHelperMapper {
    int insert(CountHelper countHelper);
    List<CountHelper> selectAll();

}

package trust.mapper;

import trust.entity.User;

import java.util.List;

/**
 * Created by inst1 on 2017/6/21.
 */
public interface UserMapper {
        List<User> selectAll();
        int count(Integer id);
        int countSuccess();
}

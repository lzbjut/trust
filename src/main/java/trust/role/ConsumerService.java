package trust.role;

import trust.entity.Ser;
import trust.entity.User;

import java.util.List;

/**
 * Created by inst1 on 2017/7/16.
 */
public interface ConsumerService {
    void UseService(List<Ser> services , User user, int gen);
}

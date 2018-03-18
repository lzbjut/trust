package trust.role;

import trust.entity.Ser;
import trust.entity.User;

import java.util.List;

/**
 * Created by inst1 on 2017/10/31.
 */
public interface PlatformService {
    List<Ser> getTrustedService(int ranLow, int ranHigh, User user,int choice);
}

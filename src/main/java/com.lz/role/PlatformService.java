package com.lz.role;

import com.lz.entity.Ser;
import com.lz.entity.User;

import java.util.List;

/**
 * Created by inst1 on 2017/10/31.
 */
public interface PlatformService {
    List<Ser> getTrustedService(int ranLow, int ranHigh, User user, int choice,int gen);
    double verify(int se,int gen);
}

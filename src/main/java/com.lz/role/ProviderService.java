package com.lz.role;

import com.lz.entity.Ser;

/**
 * Created by inst1 on 2017/7/16.
 */
public interface ProviderService {
    Ser ProduceServiceActualQuality(Ser se, int order);
}

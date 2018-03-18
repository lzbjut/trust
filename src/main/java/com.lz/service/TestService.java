package com.lz.service;

import com.lz.entity.Result;

import java.util.List;
import java.util.concurrent.BrokenBarrierException;

/**
 * Created by inst1 on 2017/6/21.
 */
public interface TestService {
    void Clear();
    List<Result> GetResult(int countCase);
    void Run(int gen,int laps,int choice) throws BrokenBarrierException, InterruptedException;


}

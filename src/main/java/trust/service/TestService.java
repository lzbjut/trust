package trust.service;

import trust.entity.Result;

import java.util.List;
import java.util.concurrent.BrokenBarrierException;

/**
 * Created by inst1 on 2017/6/21.
 */
public interface TestService {
    void Clear();
    List<Result> GetResult();
    void Run() throws BrokenBarrierException, InterruptedException;

}

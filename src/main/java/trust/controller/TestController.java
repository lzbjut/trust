package trust.controller;


import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import trust.entity.Result;
import trust.service.TestService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.concurrent.BrokenBarrierException;


@Controller
@RequestMapping("/trust")
public class TestController {

	@Autowired
	TestService testService;

	//开始进行模拟
	@RequestMapping(value="/test",method= RequestMethod.POST)
	public void Test() throws BrokenBarrierException, InterruptedException {
		testService.Run();
	}

	//获取实验的结果
	@RequestMapping(value="/result")
	public ModelAndView Result(HttpServletRequest request) throws BrokenBarrierException, InterruptedException {
		List<Result> results=testService.GetResult();
		Gson gson=new Gson();
		request.setAttribute("result", gson.toJson(results));
		return new ModelAndView("/result");
	}

	//清除当前测试的数据重新开始
	@RequestMapping(value="/clear",method= RequestMethod.POST)
	public void Clear() {
		testService.Clear();
	}
}

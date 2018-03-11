package com.lz.controller;


import com.google.gson.Gson;
import com.lz.entity.Result;
import com.lz.role.PlatformService;
import com.lz.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.concurrent.BrokenBarrierException;


@Controller
@RequestMapping("/trust")
public class TestController {


	@Autowired
	TestService testService;

	@Autowired
	PlatformService platformService;

	//开始进行模拟
	@RequestMapping(value="/test",method= RequestMethod.POST)
	public void Test(int gen,int laps,int choice) throws BrokenBarrierException, InterruptedException {

		testService.Run(gen,laps,choice);

	}

	//测试假设
	@ResponseBody
	@RequestMapping(value="/verify",method= RequestMethod.POST)
	public String verify(int se) throws BrokenBarrierException, InterruptedException {
		return platformService.verify(se,10)+"";
	}

	//获取实验的结果
	@RequestMapping(value="/result")
	public ModelAndView Result(HttpServletRequest request,int countCase) throws BrokenBarrierException, InterruptedException {
		List<Result> results=testService.GetResult(countCase);
		Gson gson=new Gson();
		request.setAttribute("result", gson.toJson(results));
		return new ModelAndView("/result");
	}

	//清除当前测试的数据重新开始
	@ResponseBody
	@RequestMapping(value="/clear",method= RequestMethod.POST)
	public String Clear() {
		testService.Clear();
		System.out.print("asdassdasd");
		return "asdasdjjfaasdasddfaaaasasdasd";
	}
}

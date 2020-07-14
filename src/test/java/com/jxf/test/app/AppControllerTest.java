package com.jxf.test.app;



import org.junit.Before;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;



import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import org.springframework.web.context.WebApplicationContext;


import com.jxf.test.BaseJunit4Test;


@WebAppConfiguration
public class AppControllerTest extends BaseJunit4Test {

	@Autowired
	private WebApplicationContext webApplicationContext;

	protected MockMvc mockMvc;

	// 方法执行前初始化数据
	@Before
	public void setUp() throws Exception {
		mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
	}

}

package com.jxf.test.app;


import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.alibaba.fastjson.JSON;
import com.jxf.web.model.wyjt.app.LoginSubmitRequestParam;



@WebAppConfiguration
public class LoginControllerTest extends AppControllerTest {

	@Test
	public void test() {
		MockHttpServletRequestBuilder mockHttpServletRequestBuilder = MockMvcRequestBuilders.post("/app/wyjt/login/submit" );
		LoginSubmitRequestParam reqParam = new LoginSubmitRequestParam();
		reqParam.setPhoneNo("18010196216");
		reqParam.setPassword("123456");

		mockHttpServletRequestBuilder.accept(MediaType.parseMediaType("application/json;charset=UTF-8"))
		                             .param("param", JSON.toJSONString(reqParam));
		try {
		ResultActions resultActions = mockMvc.perform(mockHttpServletRequestBuilder);			
//				.andDo(MockMvcResultHandlers.print());
//				.andExpect(MockMvcResultMatchers.status().isOk())
//				.andExpect(MockMvcResultMatchers.content().encoding("application/json;charset=UTF-8"))
//				.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE)); // 预期返回的媒体类型是JSON
		MockHttpServletResponse response = resultActions.andReturn().getResponse();
		response.setContentType("application/json;charset=UTF-8");
			
		System.out.println("返回JSON|" + response.getContentAsString());
		} catch (Exception e) {
            e.printStackTrace();
        }
	}

}

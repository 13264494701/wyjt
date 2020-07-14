package com.jxf.test.app;


import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;



@WebAppConfiguration
public class PollingControllerTest extends AppControllerTest {

	@Test
	public void test() {
		MockHttpServletRequestBuilder mockHttpServletRequestBuilder = MockMvcRequestBuilders.post("/app/wyjt/api" );

		mockHttpServletRequestBuilder.accept(MediaType.parseMediaType("application/json;charset=UTF-8")).header("x-memberToken", "gjjzeyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpZCI6MjU3Mjc5NTQ5MzU4ODcwNTI4LCJleHAiOjE1NDE1NzM0NjksImRldmljZVRva2VuIjpudWxsfQ.GnugiEa4IdgNcFLp0FrUdmFzsp632ZAWQA7Pg-TVkTw")
		.param("actionName", "polling").param("methodName", "notice");
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

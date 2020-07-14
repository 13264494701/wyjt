package com.jxf.test.app;


import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;



@WebAppConfiguration
public class PayPwdCheckControllerTest extends AppControllerTest {

	@Test
	public void test() {
		MockHttpServletRequestBuilder mockHttpServletRequestBuilder = MockMvcRequestBuilders.post("/app/wyjt/member/checkPwd" );

		mockHttpServletRequestBuilder.accept(MediaType.parseMediaType("application/json;charset=UTF-8")).header("x-memberToken", "gjjzeyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpZCI6MjY3NzI4OTk5NjQyNDM1NTg0LCJleHAiOjE1NDUzMTM1NzYsImRldmljZVRva2VuIjoiMTI0MyJ9._VXLswnxtS6UKCGxk2bjEu101dDQ6OSvv8wwuxRTe5I")
		.param("pwd", "123456");
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

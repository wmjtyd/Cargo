package main.controller;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import junit.framework.TestCase;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.io.UnsupportedEncodingException;
import java.util.Map;
import java.util.UUID;

@RunWith(SpringRunner.class)
@WebAppConfiguration
@SpringBootTest
@Slf4j
public class VehicleRoutingControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @Before
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void testSolve_test() {
        ObjectMapper objectMapper = new ObjectMapper();
        // 发送求解请求，直接使用 jsonString
        MvcResult solveResult = null;
        try {
            solveResult = mockMvc.perform(MockMvcRequestBuilders.post("/solve_test/routing")
                            .contentType(MediaType.APPLICATION_JSON)
                            //.content(jsonString)
                        )
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andReturn();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        // 解析响应数据
        String responseContent = null;
        try {
            responseContent = solveResult.getResponse().getContentAsString();
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        @SuppressWarnings("unchecked")
        Map<String, Object> solveResponseJsonMap = null;
        try {
            solveResponseJsonMap = objectMapper.readValue(responseContent, Map.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        JSONObject solveResponse = new JSONObject(solveResponseJsonMap);
        UUID taskId = UUID.fromString(solveResponse.getJSONObject("data").getString("uuid"));


        // 模拟时间延迟以等待异步处理
        try {
            Thread.sleep(60000); // 这里延迟时间依赖于实际的处理时间
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        // 请求结果
        JSONObject resultRequest = new JSONObject();
        resultRequest.put("uuid", taskId);
        MvcResult result = null;
        try {
            result = mockMvc.perform(MockMvcRequestBuilders.post("/result/routing")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(resultRequest.toString()))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andReturn();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        // 检查结果
        @SuppressWarnings("unchecked")
        Map<String, Object> resultResponsesonMap = null;
        try {
            resultResponsesonMap = objectMapper.readValue(result.getResponse().getContentAsString(), Map.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        JSONObject resultResponse = new JSONObject(resultResponsesonMap);
        System.out.println("Solution received: " + resultResponse.toString());
    }

    @Test
    public void testSolve() {

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = "";
        MvcResult solveResult = null;
        try {
            solveResult = mockMvc.perform(MockMvcRequestBuilders.post("/solve/routing")
                                    .contentType(MediaType.APPLICATION_JSON)
                            .content(jsonString)
                    )
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andReturn();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        // 解析响应数据
        String responseContent = null;
        try {
            responseContent = solveResult.getResponse().getContentAsString();
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        @SuppressWarnings("unchecked")
        Map<String, Object> solveResponseJsonMap = null;
        try {
            solveResponseJsonMap = objectMapper.readValue(responseContent, Map.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        JSONObject solveResponse = new JSONObject(solveResponseJsonMap);
        UUID taskId = UUID.fromString(solveResponse.getJSONObject("data").getString("uuid"));


        // 模拟时间延迟以等待异步处理
        try {
            Thread.sleep(60000); // 这里延迟时间依赖于实际的处理时间
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        // 请求结果
        JSONObject resultRequest = new JSONObject();
        resultRequest.put("uuid", taskId);
        MvcResult result = null;
        try {
            result = mockMvc.perform(MockMvcRequestBuilders.post("/result/routing")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(resultRequest.toString()))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andReturn();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        // 检查结果
        @SuppressWarnings("unchecked")
        Map<String, Object> resultResponsesonMap = null;
        try {
            resultResponsesonMap = objectMapper.readValue(result.getResponse().getContentAsString(), Map.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        JSONObject resultResponse = new JSONObject(resultResponsesonMap);
        System.out.println("Solution received: " + resultResponse.toString());
    }
}
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
        String jsonString = "{\"carList\":[{\"carId\":1,\"capacity\":5,\"location\":{\"address\":\"loc.2\",\"latitude\":99.71203098460948,\"longitude\":24.555912914642963}},{\"carId\":3,\"capacity\":5,\"location\":{\"address\":\"loc.4\",\"latitude\":59.171859496028524,\"longitude\":85.76866920177947}}],\"storageList\":[{\"storageId\":5,\"location\":{\"address\":\"loc.6\",\"latitude\":93.91541768024068,\"longitude\":1.2850921205032728}},{\"storageId\":7,\"location\":{\"address\":\"loc.8\",\"latitude\":83.53260936888563,\"longitude\":24.817525273441476}}],\"requestList\":[{\"orderId\":9,\"storageId\":5,\"weight\":8.624202093167108,\"location\":{\"address\":\"loc.10\",\"latitude\":71.17508168072064,\"longitude\":28.81207033950446}},{\"orderId\":11,\"storageId\":7,\"weight\":9.117341668664181,\"location\":{\"address\":\"loc.12\",\"latitude\":32.068963229861794,\"longitude\":7.0786614612769805}},{\"orderId\":13,\"storageId\":5,\"weight\":9.449975713456034,\"location\":{\"address\":\"loc.14\",\"latitude\":77.11760963604866,\"longitude\":20.960110479909734}},{\"orderId\":15,\"storageId\":5,\"weight\":12.917459959130861,\"location\":{\"address\":\"loc.16\",\"latitude\":66.73554577429968,\"longitude\":11.827915825354163}},{\"orderId\":17,\"storageId\":5,\"weight\":10.839805588345277,\"location\":{\"address\":\"loc.18\",\"latitude\":81.0901305187466,\"longitude\":55.546591657124566}},{\"orderId\":19,\"storageId\":7,\"weight\":14.737180213750285,\"location\":{\"address\":\"loc.20\",\"latitude\":54.437515780843526,\"longitude\":5.061489293552657}},{\"orderId\":21,\"storageId\":7,\"weight\":8.157569880273549,\"location\":{\"address\":\"loc.22\",\"latitude\":57.704997425656444,\"longitude\":69.22797497056783}},{\"orderId\":23,\"storageId\":5,\"weight\":13.072249266253671,\"location\":{\"address\":\"loc.24\",\"latitude\":34.019239060629204,\"longitude\":64.3052844883211}},{\"orderId\":25,\"storageId\":5,\"weight\":14.016534638797435,\"location\":{\"address\":\"loc.26\",\"latitude\":93.07987983625985,\"longitude\":82.42851326327477}},{\"orderId\":27,\"storageId\":5,\"weight\":14.192677955699196,\"location\":{\"address\":\"loc.28\",\"latitude\":24.565825004834863,\"longitude\":96.05579526707777}},{\"orderId\":29,\"storageId\":7,\"weight\":13.087677905371928,\"location\":{\"address\":\"loc.30\",\"latitude\":99.88781210930159,\"longitude\":28.142648596001507}},{\"orderId\":31,\"storageId\":7,\"weight\":13.474740123440315,\"location\":{\"address\":\"loc.32\",\"latitude\":85.92420613305059,\"longitude\":34.368673943501605}},{\"orderId\":33,\"storageId\":7,\"weight\":5.002050091763108,\"location\":{\"address\":\"loc.34\",\"latitude\":68.06044895738108,\"longitude\":64.41990187651841}},{\"orderId\":35,\"storageId\":7,\"weight\":6.075197992228414,\"location\":{\"address\":\"loc.36\",\"latitude\":13.281511713396998,\"longitude\":57.882162075107566}},{\"orderId\":37,\"storageId\":7,\"weight\":8.827311306544011,\"location\":{\"address\":\"loc.38\",\"latitude\":76.34284775503961,\"longitude\":9.13325203207953}},{\"orderId\":39,\"storageId\":5,\"weight\":14.35410721737319,\"location\":{\"address\":\"loc.40\",\"latitude\":68.1336289486027,\"longitude\":86.10981243761005}},{\"orderId\":41,\"storageId\":7,\"weight\":7.432854372151501,\"location\":{\"address\":\"loc.42\",\"latitude\":55.78494633245226,\"longitude\":47.25327233308243}},{\"orderId\":43,\"storageId\":5,\"weight\":13.93211665136431,\"location\":{\"address\":\"loc.44\",\"latitude\":12.713609515232083,\"longitude\":42.44057724859116}},{\"orderId\":45,\"storageId\":5,\"weight\":9.020288369267607,\"location\":{\"address\":\"loc.46\",\"latitude\":42.56580253153059,\"longitude\":62.16926077532479}},{\"orderId\":47,\"storageId\":7,\"weight\":5.144995725439079,\"location\":{\"address\":\"loc.48\",\"latitude\":14.700192234611498,\"longitude\":47.144298192821154}}]}";
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
            Thread.sleep(180000); // 这里延迟时间依赖于实际的处理时间
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
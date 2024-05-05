package org.example.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.exception.NotFoundException;
import org.example.model.Employee;
import org.example.model.Phone;
import org.example.model.Position;
import org.example.service.PhoneService;
import org.example.service.impl.PhoneServiceImpl;
import org.example.servlet.dto.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.testcontainers.shaded.org.hamcrest.Matchers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class PhoneServletTest {
    @Mock
    private PhoneService phoneNumberService;
    @Spy
    @InjectMocks
    private  PhoneServlet phoneNumberServlet;
    @Mock
    private MockMvc mvc;
    private String requestBody;
    @Mock
    private PhoneOutGoingDto phoneOutGoingDto;
    @Mock
    private EmployeeSmallOutGoingDto employeeSmallOutGoingDto;
    @Mock
    private Phone phone;


    @BeforeEach
    void setUp() {

        mvc = MockMvcBuilders
                .standaloneSetup(phoneNumberServlet)
                .build();
        Employee employee=new Employee();
        phone=new Phone(2L, "+7 772227 64 64",employee);
        employeeSmallOutGoingDto=new EmployeeSmallOutGoingDto();
        phoneOutGoingDto=new PhoneOutGoingDto(2L,"+7 772227 64 64",employeeSmallOutGoingDto);
        requestBody = "{\"id\":2,\"number\":\"+7 772227 64 64\",\"employee\":{\"id\":null,\"firstName\":null,\"lastName\":null}}";
    }

    @Test
    void doGetAll() throws Exception {
        requestBody = "[" + requestBody + "]";
        List<PhoneOutGoingDto> list=List.of(phoneOutGoingDto);
        Mockito.when(phoneNumberService.findAll()).thenReturn(list);

        String response = mvc.perform(MockMvcRequestBuilders.get("/phone/all")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(requestBody, response);
    }


    @Test
    void doGetPhoneById() throws Exception {
        Mockito.when(phoneNumberService.findById(phone.getId())).thenReturn(phoneOutGoingDto);

        String response = mvc.perform(MockMvcRequestBuilders.get("/phone/2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        assertEquals(requestBody, response);

    }

    @Test
    void doPost() throws Exception {

        Mockito.when(phoneNumberService.save(Mockito.any(PhoneIncomingDto.class),Mockito.anyLong())).thenReturn(phoneOutGoingDto);

        String response = mvc.perform(MockMvcRequestBuilders.post("/phone/2")
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(requestBody, response);
    }

    @Test
    void doPutPhone() throws Exception {

        Mockito
                .doNothing()
                .when(phoneNumberService).update(Mockito.any(PhoneUpdateDto.class));

        ObjectMapper objectMapper = new ObjectMapper();
        requestBody = objectMapper.writeValueAsString(phoneOutGoingDto);


        mvc.perform(MockMvcRequestBuilders.put("/phone/1")
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void doDeletePhone() throws Exception {

        Mockito
                .doNothing()
                .when(phoneNumberService).delete(1L);

        mvc.perform(MockMvcRequestBuilders.delete("/phone/1"))
                .andExpect(status().isOk());
    }
}
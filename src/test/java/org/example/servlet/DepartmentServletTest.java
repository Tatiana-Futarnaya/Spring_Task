package org.example.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.exception.NotFoundException;
import org.example.model.Department;
import org.example.model.Employee;
import org.example.model.Position;
import org.example.service.DepartmentService;
import org.example.service.impl.DepartmentServiceImpl;
import org.example.servlet.dto.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

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
class DepartmentServletTest {
    @Mock
    private DepartmentService departmentService;
    @Spy
    @InjectMocks
    private  DepartmentServlet departmentServlet;
    @Mock
    private MockMvc mvc;
    private String requestBody;
    @Mock
    private DepartmentOutGoingDto departmentOutGoingDto;
    @Mock
    private Department department;


    @BeforeEach
    void setUp() {

        mvc = MockMvcBuilders
                .standaloneSetup(departmentServlet)
                .build();
        List<Employee> list=List.of(new Employee());
        department=new Department(2L, "BackEnd",list);
        departmentOutGoingDto=new DepartmentOutGoingDto(2L, "BackEnd");

        requestBody = "{\"id\":2,\"name\":\"BackEnd\"}";
    }

    @Test
    void doGetAll() throws Exception {
        requestBody = "[" + requestBody + "]";
        List<DepartmentOutGoingDto> list=List.of(departmentOutGoingDto);
        Mockito.when(departmentService.findAll()).thenReturn(list);

        String response = mvc.perform(MockMvcRequestBuilders.get("/department/all")
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
    void doGetDepartmentById() throws Exception {
        Mockito.when(departmentService.findById(department.getId())).thenReturn(departmentOutGoingDto);

        String response = mvc.perform(MockMvcRequestBuilders.get("/department/2")
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

        Mockito.when(departmentService.save(Mockito.any(DepartmentIncomingDto.class))).thenReturn(departmentOutGoingDto);

        String response = mvc.perform(MockMvcRequestBuilders.post("/department/")
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
    void doPutDepartment() throws Exception {

        Mockito
                .doNothing()
                .when(departmentService).update(Mockito.any(DepartmentUpdateDto.class));

        ObjectMapper objectMapper = new ObjectMapper();
        requestBody = objectMapper.writeValueAsString(departmentOutGoingDto);


        mvc.perform(MockMvcRequestBuilders.put("/department/1")
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

    }

    @Test
    void doDeleteDepartment() throws Exception {

        Mockito
                .doNothing()
                .when(departmentService).delete(1L);

        mvc.perform(MockMvcRequestBuilders.delete("/department/1"))
                .andExpect(status().isOk());
    }

}
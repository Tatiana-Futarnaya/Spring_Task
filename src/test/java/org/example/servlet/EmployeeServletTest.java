package org.example.servlet;

import org.example.model.Employee;
import org.example.model.Position;
import org.example.service.EmployeeService;
import org.example.servlet.dto.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import com.fasterxml.jackson.databind.ObjectMapper;


import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class EmployeeServletTest {
    @Spy
    @InjectMocks
    private  EmployeeServlet servlet;
    @Mock
    private EmployeeService employeeService;
    @Mock
    private MockMvc mvc;
    private String requestBody;
    @Mock
    private Position position;
    @Mock
    private EmployeeOutGoingDto employeeOutGoingDto;
    @Mock
    private Employee employee;



    @BeforeEach
    void setUp() {

        mvc = MockMvcBuilders
                .standaloneSetup(servlet)
                .build();

        PositionUpdateDto positionUpdateDto=new PositionUpdateDto(4L,"Admin");

        position=new Position(4L,"Администратор");

        employee=new Employee(1L, "New firstName", "New LastName",
                position , List.of(),List.of());
        PositionOutGoingDto position=new PositionOutGoingDto(4L,"Admin");
        employeeOutGoingDto=new EmployeeOutGoingDto(1L, "New firstName", "New LastName",
                position , List.of(),List.of() );

        requestBody = "{\"id\":1,\"firstName\":\"New firstName\",\"lastName\":\"New LastName\"," +
                "\"position\":{\"id\":4,\"name\":\"Admin\"},\"phoneNumberList\":[],\"departmentList\":[]}";
    }


    @Test
    void doGetAll() throws Exception {
        requestBody = "[" + requestBody + "]";
        List<EmployeeOutGoingDto> list=List.of(employeeOutGoingDto);
        Mockito.when(employeeService.findAll()).thenReturn(list);

        String response = mvc.perform(MockMvcRequestBuilders.get("/employee/all")
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
    void doGetEmployeeById() throws Exception {
        Mockito.when(employeeService.findById(employee.getId())).thenReturn(employeeOutGoingDto);

        String response = mvc.perform(MockMvcRequestBuilders.get("/employee/1")
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

        Mockito.when(employeeService.save(Mockito.any(EmployeeIncomingDto.class))).thenReturn(employeeOutGoingDto);

        String response = mvc.perform(MockMvcRequestBuilders.post("/employee/")
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
    void doPutEmployee() throws Exception {

        Mockito.when(employeeService.updateEmployee(Mockito.any(EmployeeUpdateDto.class))).thenReturn(employeeOutGoingDto);

        ObjectMapper objectMapper = new ObjectMapper();
        requestBody = objectMapper.writeValueAsString(employeeOutGoingDto);


        mvc.perform(MockMvcRequestBuilders.put("/employee/1")
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

    }


    @Test
    void doDeleteEmployee() throws Exception {

        Mockito
                .doNothing()
                .when(employeeService).delete(1L);

        mvc.perform(MockMvcRequestBuilders.delete("/employee/1"))
                .andExpect(status().isOk());
    }



}
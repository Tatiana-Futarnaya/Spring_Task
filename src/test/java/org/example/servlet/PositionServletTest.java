package org.example.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.exception.NotFoundException;
import org.example.model.Employee;
import org.example.model.Position;
import org.example.service.PositionService;
import org.example.service.impl.PositionServiceImpl;
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
class PositionServletTest {
    @Mock
    private PositionService positionService;
    @Spy
    @InjectMocks
    private  PositionServlet positionServlet;
    @Mock
    private MockMvc mvc;
    private String requestBody;
    @Mock
    private PositionOutGoingDto positionOutGoingDto;
    @Mock
    private Position position;


    @BeforeEach
    void setUp() {

        mvc = MockMvcBuilders
                .standaloneSetup(positionServlet)
                .build();

        position=new Position(4L,"Admin");
        positionOutGoingDto=new PositionOutGoingDto(4L,"Admin");

        requestBody="{\"id\":4,\"name\":\"Admin\"}";

    }


    @Test
    void doGetAll() throws Exception {
        requestBody = "[" + requestBody + "]";
        List<PositionOutGoingDto> list=List.of(positionOutGoingDto);

        Mockito.when(positionService.findAll()).thenReturn(list);

        String response = mvc.perform(MockMvcRequestBuilders.get("/position/all")
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
    void doGetPositionById() throws Exception {
        Mockito.when(positionService.findById(position.getId())).thenReturn(positionOutGoingDto);

        String response = mvc.perform(MockMvcRequestBuilders.get("/position/4")
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

        Mockito.when(positionService.save(Mockito.any(PositionIncomingDto.class))).thenReturn(positionOutGoingDto);

        String response = mvc.perform(MockMvcRequestBuilders.post("/position/")
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
    void doPutPosition() throws Exception {

        Mockito
                .doNothing()
                .when(positionService).update(Mockito.any(PositionUpdateDto.class));

        ObjectMapper objectMapper = new ObjectMapper();
        requestBody = objectMapper.writeValueAsString(positionOutGoingDto);


        mvc.perform(MockMvcRequestBuilders.put("/position/1")
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

    }

    @Test
    void doDeletePosition() throws Exception {

        Mockito
                .doNothing()
                .when(positionService).delete(1L);

        mvc.perform(MockMvcRequestBuilders.delete("/position/1"))
                .andExpect(status().isOk());
    }

}
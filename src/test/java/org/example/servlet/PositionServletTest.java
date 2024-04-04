package org.example.servlet;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.exception.NotFoundException;
import org.example.service.PositionService;
import org.example.service.impl.PositionServiceImpl;
import org.example.servlet.dto.PositionIncomingDto;
import org.example.servlet.dto.PositionUpdateDto;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.lang.reflect.Field;

@ExtendWith(
        MockitoExtension.class
)
class PositionServletTest {
    private static PositionService mockPositionService;
    @InjectMocks
    private static PositionServlet positionServlet;
    private static PositionServiceImpl oldInstance;
    @Mock
    private HttpServletRequest mockRequest;
    @Mock
    private HttpServletResponse mockResponse;
    @Mock
    private BufferedReader mockBufferedReader;

    private static void setMock(PositionService mock) {
        try {
            Field instance = PositionServiceImpl.class.getDeclaredField("instance");
            instance.setAccessible(true);
            oldInstance = (PositionServiceImpl) instance.get(instance);
            instance.set(instance, mock);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @BeforeAll
    static void beforeAll() {
        mockPositionService = Mockito.mock(PositionService.class);
        setMock(mockPositionService);
        positionServlet = new PositionServlet();
    }

    @AfterAll
    static void afterAll() throws Exception {
        Field instance = PositionServiceImpl.class.getDeclaredField("instance");
        instance.setAccessible(true);
        instance.set(instance, oldInstance);
    }

    @BeforeEach
    void setUp() throws IOException {
        Mockito.doReturn(new PrintWriter(Writer.nullWriter())).when(mockResponse).getWriter();
    }

    @AfterEach
    void tearDown() {
        Mockito.reset(mockPositionService);
    }

    @Test
    void doGetAll() throws IOException {
        Mockito.doReturn("position/all").when(mockRequest).getPathInfo();

        positionServlet.doGet(mockRequest, mockResponse);

        Mockito.verify(mockPositionService).findAll();
    }

    @Test
    void doGetById() throws IOException, NotFoundException {
        Mockito.doReturn("position/2").when(mockRequest).getPathInfo();

        positionServlet.doGet(mockRequest, mockResponse);

        Mockito.verify(mockPositionService).findById(Mockito.anyLong());
    }

    @Test
    void doGetNotFoundException() throws IOException, NotFoundException {
        Mockito.doReturn("position/100").when(mockRequest).getPathInfo();
        Mockito.doThrow(new NotFoundException("not found")).when(mockPositionService).findById(100L);

        positionServlet.doGet(mockRequest, mockResponse);

        Mockito.verify(mockResponse).setStatus(HttpServletResponse.SC_NOT_FOUND);
    }

    @Test
    void doGetBadRequest() throws IOException {
        Mockito.doReturn("position/2q").when(mockRequest).getPathInfo();

        positionServlet.doGet(mockRequest, mockResponse);

        Mockito.verify(mockResponse).setStatus(HttpServletResponse.SC_BAD_REQUEST);
    }

    @Test
    void doDelete() throws IOException, NotFoundException {
        Mockito.doReturn("position/2").when(mockRequest).getPathInfo();
        Mockito.doReturn(true).when(mockPositionService).delete(Mockito.anyLong());

        positionServlet.doDelete(mockRequest, mockResponse);

        Mockito.verify(mockPositionService).delete(Mockito.anyLong());
        Mockito.verify(mockResponse).setStatus(HttpServletResponse.SC_NO_CONTENT);
    }

    @Test
    void doDeleteNotFound() throws IOException, NotFoundException {
        Mockito.doReturn("position/100").when(mockRequest).getPathInfo();
        Mockito.doThrow(new NotFoundException("not found")).when(mockPositionService).delete(100L);

        positionServlet.doDelete(mockRequest, mockResponse);

        Mockito.verify(mockResponse).setStatus(HttpServletResponse.SC_NOT_FOUND);
        Mockito.verify(mockPositionService).delete(Mockito.anyLong());
    }

    @Test
    void doDeleteBadRequest() throws IOException {
        Mockito.doReturn("position/a100").when(mockRequest).getPathInfo();

        positionServlet.doDelete(mockRequest, mockResponse);

        Mockito.verify(mockResponse).setStatus(HttpServletResponse.SC_BAD_REQUEST);
    }

    @Test
    void doPost() throws IOException {
        String expectedName = "New position Admin";
        Mockito.doReturn(mockBufferedReader).when(mockRequest).getReader();
        Mockito.doReturn(
                "{\"name\":\"" + expectedName + "\"}",
                (Object) null
        ).when(mockBufferedReader).readLine();

        positionServlet.doPost(mockRequest, mockResponse);

        ArgumentCaptor<PositionIncomingDto> argumentCaptor = ArgumentCaptor.forClass(PositionIncomingDto.class);
        Mockito.verify(mockPositionService).save(argumentCaptor.capture());

        PositionIncomingDto result = argumentCaptor.getValue();
        Assertions.assertEquals(expectedName, result.getName());
    }

    @Test
    void doPostBadRequest() throws IOException {
        Mockito.doReturn(mockBufferedReader).when(mockRequest).getReader();
        Mockito.doReturn(
                "{\"id\":1}",
                (Object) null
        ).when(mockBufferedReader).readLine();

        positionServlet.doPost(mockRequest, mockResponse);

        Mockito.verify(mockResponse).setStatus(HttpServletResponse.SC_BAD_REQUEST);
    }

    @Test
    void doPut() throws IOException, NotFoundException {
        String expectedName = "Update position Admin";
        Mockito.doReturn(mockBufferedReader).when(mockRequest).getReader();
        Mockito.doReturn(
                "{\"id\": 4,\"name\": \"" +
                expectedName + "\"}",
                (Object) null
        ).when(mockBufferedReader).readLine();

        positionServlet.doPut(mockRequest, mockResponse);

        ArgumentCaptor<PositionUpdateDto> argumentCaptor = ArgumentCaptor.forClass(PositionUpdateDto.class);
        Mockito.verify(mockPositionService).update(argumentCaptor.capture());

        PositionUpdateDto result = argumentCaptor.getValue();
        Assertions.assertEquals(expectedName, result.getName());
    }

    @Test
    void doPutBadRequest() throws IOException {
        Mockito.doReturn(mockBufferedReader).when(mockRequest).getReader();
        Mockito.doReturn(
                "{Bad json:1}",
                (Object) null
        ).when(mockBufferedReader).readLine();

        positionServlet.doPut(mockRequest, mockResponse);

        Mockito.verify(mockResponse).setStatus(HttpServletResponse.SC_BAD_REQUEST);
    }

    @Test
    void doPutNotFound() throws IOException, NotFoundException {
        Mockito.doReturn(mockBufferedReader).when(mockRequest).getReader();
        Mockito.doReturn(
                "{\"id\": 4,\"name\": \"Admin\"}",
                (Object) null
        ).when(mockBufferedReader).readLine();
        Mockito.doThrow(new NotFoundException("not found")).when(mockPositionService)
                .update(Mockito.any(PositionUpdateDto.class));

        positionServlet.doPut(mockRequest, mockResponse);

        Mockito.verify(mockResponse).setStatus(HttpServletResponse.SC_NOT_FOUND);
        Mockito.verify(mockPositionService).update(Mockito.any(PositionUpdateDto.class));
    }

}
package org.example.servlet;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.exception.NotFoundException;
import org.example.service.PhoneService;
import org.example.service.impl.PhoneServiceImpl;
import org.example.servlet.dto.PhoneIncomingDto;
import org.example.servlet.dto.PhoneUpdateDto;
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
class PhoneServletTest {
    private static PhoneService mockPhoneNumberService;
    @InjectMocks
    private static PhoneServlet phoneNumberServlet;
    private static PhoneServiceImpl oldInstance;
    @Mock
    private HttpServletRequest mockRequest;
    @Mock
    private HttpServletResponse mockResponse;
    @Mock
    private BufferedReader mockBufferedReader;

    private static void setMock(PhoneService mock) {
        try {
            Field instance = PhoneServiceImpl.class.getDeclaredField("instance");
            instance.setAccessible(true);
            oldInstance = (PhoneServiceImpl) instance.get(instance);
            instance.set(instance, mock);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @BeforeAll
    static void beforeAll() {
        mockPhoneNumberService = Mockito.mock(PhoneService.class);
        setMock(mockPhoneNumberService);
        phoneNumberServlet = new PhoneServlet();
    }

    @AfterAll
    static void afterAll() throws Exception {
        Field instance = PhoneServiceImpl.class.getDeclaredField("instance");
        instance.setAccessible(true);
        instance.set(instance, oldInstance);
    }

    @BeforeEach
    void setUp() throws IOException {
        Mockito.doReturn(new PrintWriter(Writer.nullWriter())).when(mockResponse).getWriter();
    }

    @AfterEach
    void tearDown() {
        Mockito.reset(mockPhoneNumberService);
    }

    @Test
    void doGetAll() throws IOException {
        Mockito.doReturn("phone/all").when(mockRequest).getPathInfo();

        phoneNumberServlet.doGet(mockRequest, mockResponse);

        Mockito.verify(mockPhoneNumberService).findAll();
    }

    @Test
    void doGetById() throws IOException, NotFoundException {
        Mockito.doReturn("phone/2").when(mockRequest).getPathInfo();

        phoneNumberServlet.doGet(mockRequest, mockResponse);

        Mockito.verify(mockPhoneNumberService).findById(Mockito.anyLong());
    }

    @Test
    void doGetNotFoundException() throws IOException, NotFoundException {
        Mockito.doReturn("phone/100").when(mockRequest).getPathInfo();
        Mockito.doThrow(new NotFoundException("not found")).when(mockPhoneNumberService).findById(100L);

        phoneNumberServlet.doGet(mockRequest, mockResponse);

        Mockito.verify(mockResponse).setStatus(HttpServletResponse.SC_NOT_FOUND);
    }

    @Test
    void doGetBadRequest() throws IOException {
        Mockito.doReturn("phone/2q").when(mockRequest).getPathInfo();

        phoneNumberServlet.doGet(mockRequest, mockResponse);

        Mockito.verify(mockResponse).setStatus(HttpServletResponse.SC_BAD_REQUEST);
    }

    @Test
    void doDelete() throws IOException {
        Mockito.doReturn("phone/2").when(mockRequest).getPathInfo();
        Mockito.doReturn(true).when(mockPhoneNumberService).delete(Mockito.anyLong());

        phoneNumberServlet.doDelete(mockRequest, mockResponse);

        Mockito.verify(mockPhoneNumberService).delete(Mockito.anyLong());
        Mockito.verify(mockResponse).setStatus(HttpServletResponse.SC_NO_CONTENT);
    }

    @Test
    void doDeleteBadRequest() throws IOException {
        Mockito.doReturn("phone/a100").when(mockRequest).getPathInfo();

        phoneNumberServlet.doDelete(mockRequest, mockResponse);

        Mockito.verify(mockResponse).setStatus(HttpServletResponse.SC_BAD_REQUEST);
    }

    @Test
    void doPost() throws IOException {
        String expectedNumber = "+1 123 123 1234";
        Mockito.doReturn(mockBufferedReader).when(mockRequest).getReader();
        Mockito.doReturn(
                "{\"number\":\"" + expectedNumber + "\"}",
                (Object) null
        ).when(mockBufferedReader).readLine();

        phoneNumberServlet.doPost(mockRequest, mockResponse);

        ArgumentCaptor<PhoneIncomingDto> argumentCaptor = ArgumentCaptor.forClass(PhoneIncomingDto.class);
        Mockito.verify(mockPhoneNumberService).save(argumentCaptor.capture());

        PhoneIncomingDto result = argumentCaptor.getValue();
        Assertions.assertEquals(expectedNumber, result.getNumber());
    }

    @Test
    void doPut() throws NotFoundException, IOException {
        String expectedNumber = "+1 123 123 1234";
        Mockito.doReturn(mockBufferedReader).when(mockRequest).getReader();
        Mockito.doReturn(
                "{\"id\": 4,\"number\": \"" +
                expectedNumber + "\"}",
                (Object) null
        ).when(mockBufferedReader).readLine();

        phoneNumberServlet.doPut(mockRequest, mockResponse);

        ArgumentCaptor<PhoneUpdateDto> argumentCaptor = ArgumentCaptor.forClass(PhoneUpdateDto.class);
        Mockito.verify(mockPhoneNumberService).update(argumentCaptor.capture());

        PhoneUpdateDto result = argumentCaptor.getValue();
        Assertions.assertEquals(expectedNumber, result.getNumber());

    }
}
package com.mastering.spring.springboot.controller;

import com.mastering.spring.springboot.bean.Todo;
import com.mastering.spring.springboot.service.TodoService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(TodoController.class)
class TodoControllerTest {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private TodoService service;

    @Test
    public void retrieveTodos() throws Exception {
        List<Todo> mockList = Arrays.asList(new Todo(1, "Jack",
                "Learn Spring MVC", new Date(), false), new Todo(2, "Jack",
                "Learn Struts", new Date(), false));
        when(service.retrieveTodos(anyString())).thenReturn(mockList);
        MvcResult result = mvc
                .perform(MockMvcRequestBuilders.get("/users/Jack/todos").accept(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk()).andReturn();

        String expected = "["
                + "{id:1,user:Jack,desc:\"Learn Spring MVC\",done:false}" +","
                + "{id:2,user:Jack,desc:\"Learn Struts\",done:false}" + "]";
        JSONAssert.assertEquals(expected, result.getResponse()
                .getContentAsString(), false);
    }
}
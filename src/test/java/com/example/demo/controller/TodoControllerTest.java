package com.example.demo.controller;

import com.example.demo.dto.request.TodoRequest;
import com.example.demo.dto.response.AllTodoResponse;
import com.example.demo.dto.response.TodoResponse;
import com.example.demo.security.JwtService;
import com.example.demo.service.TodoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class TodoControllerTest {

    @Mock
    private TodoService todoService;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private TodoController todoController;

    private TodoRequest todoRequest;
    private TodoResponse todoResponse;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        todoRequest = new TodoRequest();
        todoResponse = new TodoResponse();
    }

    @Test
    void get_ShouldReturnTodoResponse() {
        Long userId = 1L;
        Long todoId = 1L;
        when(jwtService.getUserId()).thenReturn(userId);
        when(todoService.getTodo(userId, todoId)).thenReturn(todoResponse);

        ResponseEntity<TodoResponse> response = todoController.get(todoId);

        assertEquals(200, response.getStatusCode().value());
        assertEquals(todoResponse, response.getBody());
    }

    @Test
    void post_ShouldAddTodo() {
        Long userId = 1L;
        when(jwtService.getUserId()).thenReturn(userId);
        todoRequest.setUserId(userId);
        when(todoService.addTodo(todoRequest)).thenReturn(todoResponse);

        ResponseEntity<TodoResponse> response = todoController.post(todoRequest);

        assertEquals(200, response.getStatusCode().value());
        assertEquals(todoResponse, response.getBody());
        verify(todoService, times(1)).addTodo(todoRequest);
    }

    @Test
    void delete_ShouldReturnNoContent() {
        Long userId = 1L;
        Long todoId = 1L;
        when(jwtService.getUserId()).thenReturn(userId);

        ResponseEntity<?> response = todoController.delete(todoId);

        assertEquals(204, response.getStatusCode().value());
        verify(todoService, times(1)).deleteTodo(userId, todoId);
    }

    @Test
    void getAll_ShouldReturnListOfTodos() {
        Long userId = 1L;
        List<TodoResponse> todoList = Collections.singletonList(todoResponse);
        when(jwtService.getUserId()).thenReturn(userId);
        when(todoService.getAllTodo(userId)).thenReturn(todoList);

        ResponseEntity<AllTodoResponse> response = todoController.getAll();

        assertEquals(200, response.getStatusCode().value());
        assertThat(response.getBody()).isNotNull();
        assertEquals(todoList, response.getBody().getTodos());
    }

    @Test
    void update_ShouldReturnUpdatedTodoResponse() {
        Long userId = 1L;
        Long todoId = 1L;
        todoRequest.setUserId(userId);
        when(jwtService.getUserId()).thenReturn(userId);
        when(todoService.update(todoRequest, todoId)).thenReturn(todoResponse);

        ResponseEntity<TodoResponse> response = todoController.update(todoId, todoRequest);

        assertEquals(200, response.getStatusCode().value());
        assertEquals(todoResponse, response.getBody());
        verify(todoService, times(1)).update(todoRequest, todoId);
    }
}
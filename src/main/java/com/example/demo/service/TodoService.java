package com.example.demo.service;

import com.example.demo.dto.request.TodoRequest;
import com.example.demo.dto.response.AllTodoResponse;
import com.example.demo.dto.response.TodoResponse;
import com.example.demo.exception.DatabaseAccessException;
import com.example.demo.exception.TodoDeletionException;
import com.example.demo.exception.TodoNotFoundException;
import com.example.demo.model.Todo;
import com.example.demo.repository.TodoRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TodoService {

    private final TodoRepository todoRepository;

    public TodoService(TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }

    public TodoResponse addTodo(TodoRequest addTodoRequest) {
        Todo todo = Todo.create(addTodoRequest);
        if (todo == null) {
            throw new TodoNotFoundException("Todo could not be added");
        }
        try {
            Todo savedTodo = todoRepository.save(todo);
            return TodoResponse.create(savedTodo);
        } catch (RuntimeException e) {
            throw new DatabaseAccessException("Error saving Todo to database", e);
        }
    }

    public TodoResponse getTodo(Long userId, Long todoId) {
        return todoRepository.findByIdAndUserId(todoId, userId)
                .map(TodoResponse::create)
                .orElseThrow(() -> new TodoNotFoundException("Todo with id " + todoId + " not found for user " + userId));
    }

    public void deleteTodo(Long userId, Long todoId) {
        try {
            todoRepository.deleteByIdAndUserId(todoId, userId);
        } catch (RuntimeException e) {
            throw new TodoDeletionException("Todo could not be deleted");
        }
    }

    public List<TodoResponse> getAllTodo(Long userId) {
        List<Todo> todoList = todoRepository.findAllByUserId(userId);
        return todoList.stream().map(TodoResponse::create).toList();
    }

    @Transactional
    public TodoResponse update(TodoRequest todoRequest, Long todoId) {
        return todoRepository.findByIdAndUserId(todoId, todoRequest.getUserId())
                .map(todo -> todo.update(todoRequest))
                .map(todoRepository::save)
                .map(TodoResponse::create)
                .orElseThrow(() -> new TodoNotFoundException(todoId + " Not found"));
    }

    public AllTodoResponse getTodoByPage(Long userId, int page, int size) {
        Pageable pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));
        try {
            Page<Todo> todoPage = todoRepository.findByUserId(userId, pageRequest);
            return AllTodoResponse.fromPage(todoPage);
        } catch (RuntimeException e) {
            throw new DatabaseAccessException("Error fetching Todos from database", e);
        }
    }

    public AllTodoResponse getTodoByPageAndSearch(Long userId, int page, int size, String searched) {
        Pageable pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));
        try {
            Page<Todo> todoPage = todoRepository.findByUserId(userId, searched, pageRequest);

            return AllTodoResponse.fromPage(todoPage);
        } catch (RuntimeException e) {
            throw new DatabaseAccessException("Error fetching Todos from database", e);
        }
    }

}

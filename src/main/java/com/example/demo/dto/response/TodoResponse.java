package com.example.demo.dto.response;

import com.example.demo.model.Todo;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
public class TodoResponse {
    private Long id;

    private String task;

    private String desc;

    private boolean isCompleted;

    public static TodoResponse create(Todo todo) {
        if (Objects.isNull(todo)) return null;

        TodoResponse todoResponse = new TodoResponse();
        todoResponse.setTask(todo.getTask());
        todoResponse.setDesc(todo.getDesc());
        todoResponse.setId(todo.getId());
        todoResponse.setCompleted(todo.isCompleted());
        return todoResponse;
    }
}

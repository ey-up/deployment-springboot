package com.example.demo.dto.response;

import com.example.demo.model.Todo;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
@Setter
public class AllTodoResponse {
    private List<TodoResponse> todos;
    private long totalCount;


    public static AllTodoResponse fromPage(Page<Todo> todoPage) {
        List<TodoResponse> todoResponses = todoPage.stream()
                .map(TodoResponse::create)
                .toList();

        AllTodoResponse allTodoResponse = new AllTodoResponse();
        allTodoResponse.setTodos(todoResponses);
        allTodoResponse.setTotalCount(todoPage.getTotalElements());
        return allTodoResponse;
    }
}

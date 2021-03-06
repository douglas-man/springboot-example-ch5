package com.mastering.spring.springboot.controller;

import com.mastering.spring.springboot.bean.ExceptionResponse;
import com.mastering.spring.springboot.bean.Todo;
import com.mastering.spring.springboot.bean.TodoNotFoundException;
import com.mastering.spring.springboot.service.TodoService;
//import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@RestController
public class TodoController {
    @Autowired
    private TodoService todoService;

    @Operation(
            summary = "Retrieve all todos for a user by passing in his name",
            description = "A list of matching todos is returned. Current pagination is not supported.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of Todos",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Todo.class))}),
            @ApiResponse(responseCode = "404", description = "Todo not found",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ExceptionResponse.class))})
    })
/*    ,
            response = Todo.class,
            responseContainer = "List",
            produces = "application/json")*/
    @GetMapping("/users/{name}/todos")
    public List<Todo> retrieveTodos(@PathVariable String name) {
        return todoService.retrieveTodos(name);
    }

    @GetMapping(path = "/users/{name}/todos/{id}")
    public Resource<Todo> retrieveTodo(@PathVariable String name, @PathVariable
            int id) {
        Todo todo = todoService.retrieveTodo(id);
        if (todo == null) {
            throw new TodoNotFoundException("Todo Not Found");
        }

        Resource<Todo> todoResource = new Resource<>(todo);
        ControllerLinkBuilder linkTo =
                linkTo(methodOn(this.getClass()).retrieveTodos(name));
        todoResource.add(linkTo.withRel("parent"));
        return todoResource;
    }

    @PostMapping("/users/{name}/todos")
    public ResponseEntity<?> add(@PathVariable String name,
                                 @Valid @RequestBody Todo todo) {
        Todo createdTodo = todoService.addTodo(name, todo.getDesc(),
                todo.getTargetDate(), todo.isDone());
        if (createdTodo == null) {
            return ResponseEntity.noContent().build();
        }
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(createdTodo.getId()).toUri();
        return ResponseEntity.created(location).build();
    }

    @GetMapping(path = "/users/dummy-service")
    public Todo errorService() {
        throw new RuntimeException("Some Exception Occured");
    }
}

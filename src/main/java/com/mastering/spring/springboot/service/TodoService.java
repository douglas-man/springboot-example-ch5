package com.mastering.spring.springboot.service;

import com.mastering.spring.springboot.bean.Todo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TodoService {

    private static final Logger logger = LogManager.getLogger(TodoService.class);

    private static List<Todo> todos = new ArrayList<>();
    private static int todoCount = 5;

/*    static {
        todos.add(new Todo(1, "Jack", "Learn Spring MVC",
                new Date(), false));
        todos.add(new Todo(2, "Jack", "Learn Struts", new Date(),
                false));
        todos.add(new Todo(3, "Jill", "Learn Hibernate", new Date(),
                false));
    }*/

    public List<Todo> retrieveTodos(String user) {

        logger.info(String.format("Minimum Priority: %s",
                Thread.MIN_PRIORITY));
        logger.info(String.format("Normal Priority: %s",
                Thread.NORM_PRIORITY));
        logger.info(String.format("Maximun Priority: %s",
                Thread.MAX_PRIORITY));


        Runnable runnable = () -> {
            long current = 1L;
            long max = 2000L;
            long numPrimes = 0L;

            logger.info(String.format("Thread '%s' with Priority: %d: %s",
                    Thread.currentThread().getName(), Thread.currentThread().getPriority(), Thread.currentThread().getState().toString()));

            while (current <= max) {
                if (isPrime(current))
                    numPrimes++;

                current++;
            }
            logger.info(String.format("Thread '%s': %s. Number of Primes: %d\n",
                    Thread.currentThread().getName(), Thread.currentThread().getState(), numPrimes));
        };

        Thread thread[];
        thread = new Thread[todoCount];

        for (int i = 0; i < todoCount; i++) {
            thread[i] = new Thread(runnable);
            if (i % 2 == 0)
                thread[i].setPriority(Thread.MAX_PRIORITY);
            else
                thread[i].setPriority(Thread.NORM_PRIORITY);

            thread[i].setName("Thread " + i);
        }

        Thread.State status[];
        status = new Thread.State[todoCount];

        for (int i = 0; i < todoCount; i++) {
        //    logger.info(String.format("Main : Status of Thread %d: %s with Priority: %d", i,
        //            thread[i].getState(), thread[i].getPriority()));
            status[i] = thread[i].getState();
        }

        for (int i = 0; i < todoCount; i++)
            thread[i].start();


        boolean finish = false;
        while (!finish) {
            for (int i = 0; i < todoCount; i++) {
                if (thread[i].getState() != status[i]) {
                    writeThreadInfo(thread[i], status[i]);
                    status[i] = thread[i].getState();
                }
            }

            finish = true;
            for (int i = 0; i < todoCount; i++) {
                finish = finish && (thread[i].getState() ==
                        Thread.State.TERMINATED);
            }
        }



        return todos.stream().filter(t -> t.getUser().equals(user)).collect(Collectors.toList());
    }

    public Todo addTodo(String name, String desc,
                        Date targetDate, boolean isDone) {
        Todo todo = new Todo(++todoCount, name, desc, targetDate,
                isDone);
        todos.add(todo);
        return todo;
    }

    public Todo retrieveTodo(int id) {
//        for (Todo todo : todos) {
//            if (todo.getId() == id)
//                return todo;
//        }


        return todos.stream().filter(t -> t.getId() == id).findAny().orElse(null);
    }

    private boolean isPrime(long number) {
        if (number <= 2) {
            return true;
        }
        for (long i = 2; i < number; i++) {
            if ((number % i) == 0) {
                return false;
            }
        }
        return true;
    }




    private static void writeThreadInfo(Thread thread,
                                        Thread.State state) {
        logger.info(String.format("Main : Id %d - %s", thread.getId(),
                thread.getName()));
        logger.info(String.format("Main : Priority: %d", thread.getPriority()));
        logger.info(String.format("Main : Old State: %s", state));
        logger.info(String.format("Main : New State: %s", thread.getState()));
        logger.info(String.format("Main : ************************************"));

        todos.add(new Todo((int) thread.getId(), "Jack", String.format("%s with Current State: %s.",
                thread.getName(),
                thread.getState().toString()),
                new Date(), false));
    }
}

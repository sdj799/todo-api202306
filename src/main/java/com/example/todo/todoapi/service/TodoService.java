package com.example.todo.todoapi.service;

import com.example.todo.todoapi.dto.request.TodoCreateRequestDTO;
import com.example.todo.todoapi.dto.request.TodoModifyRequestDTO;
import com.example.todo.todoapi.dto.response.TodoDetailResponseDTO;
import com.example.todo.todoapi.dto.response.TodoListResponseDTO;
import com.example.todo.todoapi.entity.Todo;
import com.example.todo.todoapi.repository.TodoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class TodoService {


    private final TodoRepository todoRepository;

    //할 일 목록 조회
    //요청에 따라 데이터 갱신, 삭제 등이 발생한 후
    //최신의 데이터 내용을 클라이언트에게 전달해서 렌더링 하기 위해
    //목록 리턴 메서드를 서비스에서 처리.
    public TodoListResponseDTO retrieve() {
        List<Todo> entityList = todoRepository.findAll();
        List<TodoDetailResponseDTO> dtoList = entityList.stream().map(TodoDetailResponseDTO::new)
                .collect(Collectors.toList());

        return TodoListResponseDTO.builder()
                .todos(dtoList)
                .build();
    }

    //할 일 삭제
    public TodoListResponseDTO delete(String todoId) {
        try {
            todoRepository.deleteById(todoId);
        } catch (Exception e) {
            log.error("id가 존재하지 않아 삭제에 실패했습니다. - ID: {}, err: {}"
                        ,todoId, e.getMessage());
            throw new RuntimeException("id가 존재하지 않아 삭제에 실패했습니다.");
        }
        return retrieve();
    }

    //할일 수정
    public TodoListResponseDTO modify(TodoModifyRequestDTO dto) throws RuntimeException {
        Optional<Todo> targetEntity = todoRepository.findById(dto.getId());
        targetEntity.ifPresent(entity -> {
            entity.setDone(dto.isDone());
            todoRepository.save(entity);
        });
        return retrieve();
    }

    //할일 작성
    public TodoListResponseDTO insert(final TodoCreateRequestDTO dto) throws Exception{
        todoRepository.save(dto.toEntity());
        log.info("할일 저장 완료! 제목: {}", dto.getTitle());
        return retrieve();
    }
}

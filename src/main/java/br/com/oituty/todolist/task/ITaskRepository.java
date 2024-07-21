package br.com.oituty.todolist.task;

import java.util.UUID;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ITaskRepository extends JpaRepository<TaskModel, UUID> {

    List<TaskModel> findAllByUserId(UUID userId);

}

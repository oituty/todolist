package br.com.oituty.todolist.task;

import org.springframework.web.bind.annotation.RestController;

import br.com.oituty.todolist.utils.Utils;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import java.util.UUID;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/tasks")
public class TaskControler {

    @Autowired
    private ITaskRepository taskRepository;

    @PostMapping("/")
    public ResponseEntity create(@RequestBody TaskModel taskModel, HttpServletRequest request) {
        var userId = request.getAttribute("userId");
        taskModel.setUserId((UUID) userId);
        var taskCreated = this.taskRepository.save(taskModel);

        var currentDate = LocalDateTime.now();
        if (currentDate.isAfter(taskModel.getStartAt())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("A data de inicio deve ser maior que a data atual");
        }

        if (taskModel.getEndAt().isBefore(taskModel.getStartAt())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("A data de fim deve ser maior que a data de início");
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(taskCreated);
    }

    @GetMapping("/")
    public ResponseEntity<List<TaskModel>> getAll(HttpServletRequest request) {
        var userId = request.getAttribute("userId");

        var tasks = this.taskRepository.findAllByUserId((UUID) userId);
        return ResponseEntity.ok(tasks);
    }

    @PutMapping("/{id}")
    public ResponseEntity update(@RequestBody TaskModel taskModel, HttpServletRequest request, @PathVariable UUID id) {
        var task = this.taskRepository.findById(id).orElse(null);

        if (task == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Tarefa não encontrada");
        }

        var userId = request.getAttribute("userId");

        if (!task.getUserId().equals(userId)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Você não tem permissão para alterar essa tarefa");
        }

        Utils.copyNonNullProperties(taskModel, task);

        var updatedTask = this.taskRepository.save(task);
        return ResponseEntity.status(HttpStatus.OK).body(updatedTask);
    }

}

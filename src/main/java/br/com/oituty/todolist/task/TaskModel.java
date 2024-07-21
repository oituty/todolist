package br.com.oituty.todolist.task;

import br.com.oituty.todolist.user.UserModel;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;

import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Data
@Entity(name = "tb_tasks")
public class TaskModel {
    @Id
    @GeneratedValue(generator = "UUID")
    private UUID id;

    private String description;

    @Column(length = 50)
    private String title;
    private LocalDateTime startAt;
    private LocalDateTime endAt;
    private String priority;

    @CreationTimestamp
    private LocalDateTime createdAt;

    private UUID userId;


    public void setTitle(String title) throws IllegalArgumentException {
        if (title.length() > 50) {
            throw new IllegalArgumentException("O título deve ter no máximo 50 caracteres");
        }
        this.title = title;
    }
}

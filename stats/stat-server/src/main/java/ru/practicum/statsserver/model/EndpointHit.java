package ru.practicum.statsserver.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "stats", schema = "public")
public class EndpointHit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // УИН запроса
    @Column(name = "app")
    private String app;  // УИН сервиса, о котором пишем инфу
    @Column(name = "uri")
    private String uri; // URI для которого был запрос
    @Column(name = "ip")
    private String ip; // IP-адрес пользователя с запросом
    @Column(name = "timestamp")
    private LocalDateTime timestamp; //время запроса
}

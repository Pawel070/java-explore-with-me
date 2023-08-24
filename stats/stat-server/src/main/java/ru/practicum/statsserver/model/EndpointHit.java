package ru.practicum.statsserver.model;

import javax.persistence.*;

import java.time.LocalDateTime;

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

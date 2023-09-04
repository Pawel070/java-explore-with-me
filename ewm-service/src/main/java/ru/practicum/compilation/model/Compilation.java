package ru.practicum.compilation.model;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.*;

import lombok.*;

import ru.practicum.event.model.Event;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name = "compilations")
public class Compilation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToMany
    @JoinTable(name = "events_compilations",
            joinColumns = @JoinColumn(name = "compilation_id"),
            inverseJoinColumns = @JoinColumn(name = "event_id"))

    private Set<Event> events = new HashSet<>();

    private Boolean pinned;

    private String title;

}
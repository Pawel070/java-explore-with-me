package ru.practicum.statsserver.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ViewStats {
    private String app; // название сервиса
    private String uri; // URI сервиса
    private Long hits; // кол-во просмотров

    public ViewStats(String app, String uri, Long hits) {
        this.app = app;
        this.uri = uri;
        this.hits = hits;
    }
}

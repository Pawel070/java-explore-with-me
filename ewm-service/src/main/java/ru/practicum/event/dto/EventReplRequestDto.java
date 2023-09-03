package ru.practicum.event.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import ru.practicum.event.service.MyConstantsEven;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class EventReplRequestDto extends EventCreateDto {

    private MyConstantsEven stateAction;

}
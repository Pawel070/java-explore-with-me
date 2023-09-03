package ru.practicum.location;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

import ru.practicum.event.model.Event;
import ru.practicum.location.dto.LocationDto;
import ru.practicum.location.model.Location;

@Mapper(componentModel = "spring", uses = {Event.class}, injectionStrategy = InjectionStrategy.CONSTRUCTOR)
 public interface LocationMapper {

    static Location toLocation(LocationDto locationDto) {
        return null;
    }

    static LocationDto toLocationDto(Location location) {
        return null;
    }

}

    /*
@UtilityClass
public class LocationMapper {


    public Location toLocation(LocationDto locationDto) {
        return Location.builder()
                .lon(locationDto.getLon())
                .lat(locationDto.getLat())
                .build();
    }


    public LocationDto toLocationDto(Location location) {
        return LocationDto.builder()
                .id(location.getId())
                .lon(location.getLon())
                .lat(location.getLat())
                .build();
    }

}

     */
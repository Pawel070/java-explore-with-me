package ru.practicum.location;

import lombok.experimental.UtilityClass;
import ru.practicum.location.dto.LocationDto;
import ru.practicum.location.model.Location;

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
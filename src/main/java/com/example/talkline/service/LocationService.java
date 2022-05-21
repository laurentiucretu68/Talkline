package com.example.talkline.service;

import com.example.talkline.entities.Location;
import com.example.talkline.repository.LocationRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class LocationService {

    private final LocationRepository locationRepository;

    public void addLocation(Location location){
        locationRepository.save(location);
    }
}

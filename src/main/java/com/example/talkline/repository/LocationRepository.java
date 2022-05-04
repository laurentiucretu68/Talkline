package com.example.talkline.repository;

import com.example.talkline.entities.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LocationRepository extends JpaRepository<Location, Integer> {

    @Query("select l from Location l where l.locationId=:id")
    Optional<Location> findLocationByLocationId(String id);
}

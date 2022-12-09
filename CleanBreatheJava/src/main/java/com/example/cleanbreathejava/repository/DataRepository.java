package com.example.cleanbreathejava.repository;

import com.example.cleanbreathejava.model.Pm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DataRepository extends JpaRepository<Pm,Long> {
}

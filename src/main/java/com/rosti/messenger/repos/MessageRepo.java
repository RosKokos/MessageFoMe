package com.rosti.messenger.repos;

import com.rosti.messenger.model.Messages;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface MessageRepo extends JpaRepository<Messages,Long> {
    List<Messages> findByTag(String tag);
}

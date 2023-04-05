package com.tishinserg.spreadbot.repository;

import com.tishinserg.spreadbot.repository.entity.GroupSub;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GroupSubRepository extends JpaRepository<GroupSub, Long> {

    List<GroupSub> findAll();
}

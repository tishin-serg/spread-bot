package com.tishinserg.spreadbot.service;

import com.tishinserg.spreadbot.repository.entity.GroupSub;

import java.util.List;
import java.util.Optional;

public interface GroupSubService {

    Optional<GroupSub> findById(Long id);

    GroupSub save(GroupSub groupSub);

    GroupSub subscribe(String chatId, Long groupId);

    GroupSub unsubscribe(String chatId, Long groupId);

    List<GroupSub> findAllGroups();

    void saveAll(List<GroupSub> groupSubList);

}

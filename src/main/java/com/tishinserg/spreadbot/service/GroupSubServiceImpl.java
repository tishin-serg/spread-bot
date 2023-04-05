package com.tishinserg.spreadbot.service;

import com.tishinserg.spreadbot.repository.GroupSubRepository;
import com.tishinserg.spreadbot.repository.entity.GroupSub;
import com.tishinserg.spreadbot.repository.entity.TelegramUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.ws.rs.NotFoundException;
import java.util.List;
import java.util.Optional;

@Service
public class GroupSubServiceImpl implements GroupSubService {
    private final GroupSubRepository groupSubRepository;
    private final TelegramUserService telegramUserService;

    @Autowired
    public GroupSubServiceImpl(GroupSubRepository groupSubRepository, TelegramUserService telegramUserService) {
        this.groupSubRepository = groupSubRepository;
        this.telegramUserService = telegramUserService;
    }

    @Override
    public Optional<GroupSub> findById(Long id) {
        return groupSubRepository.findById(id);
    }

    @Override
    public GroupSub save(GroupSub groupSub) {
        return groupSubRepository.save(groupSub);
    }

    @Override
    public GroupSub subscribe(String chatId, Long groupId) {
        TelegramUser telegramUser = telegramUserService.findByChatId(chatId).orElseThrow(NotFoundException::new);
        Optional<GroupSub> groupSub = groupSubRepository.findById(groupId);
        groupSub.ifPresentOrElse(group -> group.getUsers().add(telegramUser), NotFoundException::new);
        telegramUser.getGroupSubs().add(groupSub.get());
        telegramUserService.save(telegramUser);
        return groupSubRepository.save(groupSub.get());
    }

    @Override
    public GroupSub unsubscribe(String chatId, Long groupId) {
        TelegramUser telegramUser = telegramUserService.findByChatId(chatId).orElseThrow(NotFoundException::new);
        GroupSub groupSub = groupSubRepository.findById(groupId).orElseThrow(NotFoundException::new);
        groupSub.getUsers().remove(telegramUser);
        telegramUser.getGroupSubs().remove(groupSub);
        telegramUserService.save(telegramUser);
        return groupSubRepository.save(groupSub);
    }

    @Override
    public List<GroupSub> findAllGroups() {
        return groupSubRepository.findAll();
    }
}

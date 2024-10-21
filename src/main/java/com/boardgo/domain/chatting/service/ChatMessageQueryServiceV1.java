package com.boardgo.domain.chatting.service;

import com.boardgo.domain.chatting.repository.ChatRepository;
import com.boardgo.domain.chatting.repository.projection.LatestMessageProjection;
import com.boardgo.domain.chatting.service.response.LatestMessageResponse;
import com.boardgo.domain.mapper.ChatMapper;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatMessageQueryServiceV1 implements ChatMessageQueryUseCase {

    private final ChatRepository chatRepository;
    private final ChatMapper chatMapper;

    @Override
    public List<LatestMessageResponse> getLast(List<Long> roomIdList) {
        List<LatestMessageProjection> result =
                chatRepository.findLatestMessagesByRoomIds(roomIdList);
        for (LatestMessageProjection latestMessageProjection : result) {
            log.info("latestMessageProjection.roomId = {}", latestMessageProjection.roomId());
            log.info(
                    "latestMessageProjection.latestMessage.Content = {}",
                    latestMessageProjection.latestMessage().getContent());
        }
        return chatMapper.toLatestMessageResponse(result);
    }
}

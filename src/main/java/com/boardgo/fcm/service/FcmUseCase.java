package com.boardgo.fcm.service;

import com.boardgo.fcm.request.FcmMessageSendRequest;

public interface FcmUseCase {
    String sendFcmMessage(FcmMessageSendRequest request);
}

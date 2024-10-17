package com.boardgo.fcm.request;

import lombok.NonNull;

public record FcmMessageSendRequest(
        @NonNull String token, @NonNull String title, @NonNull String content, String pathUrl) {}

package com.boardgo.domain.home.controller.request;

import com.boardgo.common.validator.annotation.AllowedValues;
import jakarta.validation.constraints.NotNull;

public record SituationRequest(
        @NotNull @AllowedValues(values = {"TWO", "THREE", "MANY", "ALL"}) String situationType) {}

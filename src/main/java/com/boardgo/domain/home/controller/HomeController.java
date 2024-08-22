package com.boardgo.domain.home.controller;

import static com.boardgo.common.constant.HeaderConstant.API_VERSION_HEADER1;

import com.boardgo.domain.home.controller.request.SituationRequest;
import com.boardgo.domain.home.controller.response.SituationResponse;
import com.boardgo.domain.home.service.HomeQueryUseCase;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/home")
public class HomeController {

    private final HomeQueryUseCase homeQueryUseCase;

    @GetMapping(value = "/situation", headers = API_VERSION_HEADER1)
    public ResponseEntity<List<SituationResponse>> getSituationBoardGame(
            @RequestBody @Valid SituationRequest situationRequest) {
        return ResponseEntity.ok().body(homeQueryUseCase.getSituationBoardGame(situationRequest));
    }
}

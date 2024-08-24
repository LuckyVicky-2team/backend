package com.boardgo.domain.home.controller;

import static com.boardgo.common.constant.HeaderConstant.API_VERSION_HEADER1;

import com.boardgo.domain.boardgame.entity.SituationType;
import com.boardgo.domain.home.controller.response.SituationBoardGameResponse;
import com.boardgo.domain.home.service.HomeQueryUseCase;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/home")
public class HomeController {

    private final HomeQueryUseCase homeQueryUseCase;

    @GetMapping(value = "/situation", headers = API_VERSION_HEADER1)
    public ResponseEntity<List<SituationBoardGameResponse>> getSituationBoardGame(
            @RequestParam("situationType") @NotNull SituationType situationType) {
        return ResponseEntity.ok().body(homeQueryUseCase.getSituationBoardGame(situationType));
    }

    // FIXME: 보드게임 도메인으로 이동
    //    @GetMapping(value = "/cumulative-popularity", headers = API_VERSION_HEADER1)
    //    public ResponseEntity<List<CumulativePopularityResponse>> getCumulativePopularity() {
    //        return ResponseEntity.ok().body(homeQueryUseCase.getCumulativePopularity());
    //    }
}

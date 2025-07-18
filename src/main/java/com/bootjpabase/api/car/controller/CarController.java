package com.bootjpabase.api.car.controller;

import com.bootjpabase.api.car.domain.dto.request.CarListRequestDTO;
import com.bootjpabase.api.car.domain.dto.request.CarSaveRequestDTO;
import com.bootjpabase.api.car.domain.dto.request.CarUpdateRequestDTO;
import com.bootjpabase.api.car.domain.dto.response.CarResponseDTO;
import com.bootjpabase.api.car.service.CarService;
import com.bootjpabase.api.car.service.CarServiceTx;
import com.bootjpabase.global.annotation.common.CustomApiLogger;
import com.bootjpabase.global.domain.dto.BaseResponse;
import com.bootjpabase.global.domain.dto.BaseResponseFactory;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.bootjpabase.global.constant.SwaggerExampleConst.*;

@Tag(name = "Car Management API", description = "자동차 관리 API")
@CustomApiLogger
@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/api/car")
public class CarController {

    private final CarService carService;     // 조회 전용
    private final CarServiceTx carServiceTx; // 생성, 수정, 삭제 전용

    @Operation(summary = "자동차 저장", description = "단건 & 다건 저장 가능")
    @PostMapping("")
    public BaseResponse<List<CarResponseDTO>> saveCar(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "json",
                    content = @Content(examples = {
                            @ExampleObject(name = "저장 예제1", value = CAR_SAVE_EXAMPLE_1),
                            @ExampleObject(name = "목록 저장 예제1", value = CAR_SAVE_LIST_EXAMPLE_1)
                    })
            )
            @RequestBody @Valid List<CarSaveRequestDTO> dto
    ) {
        return BaseResponseFactory.success(carServiceTx.saveCar(dto));
    }

    @Operation(summary = "자동차 목록 조회", description = "카테고리, 제조사, 모델명, 생산년도, 대여가능여부로 조회 (페이징 처리)")
    @GetMapping("")
    public BaseResponse<List<CarResponseDTO>> getCarList(
            @ParameterObject CarListRequestDTO dto,
            @Parameter(
                    description = "페이징 정보 (가능한 정렬조건 : carSn, category, manufacturer, modelName, productionYear, createdDate)",
                    example = """
                            {
                              "page": 0,
                              "size": 10,
                              "sort": ["createdDate,desc"]
                            }
                            """
            )
            @PageableDefault Pageable pageable
    ) {
        return BaseResponseFactory.success(carService.getCarList(dto, pageable));
    }

    @Operation(summary = "자동차 수정", description = "자동차 수정 API")
    @PatchMapping("/{carSn}")
    public BaseResponse<CarResponseDTO> updateCar(
            @PathVariable("carSn") Long carSn,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "json",
                    content = @Content(examples = {
                            @ExampleObject(name = "수정 예제1", value = CAR_UPDATE_EXAMPLE_1)
                    })
            )
            @Valid @RequestBody CarUpdateRequestDTO dto
    ) {
        return BaseResponseFactory.success(carServiceTx.updateCar(carSn, dto));
    }
}

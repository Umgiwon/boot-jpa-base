package com.bootjpabase.api.car.controller;

import com.bootjpabase.api.car.domain.dto.request.CarListRequestDTO;
import com.bootjpabase.api.car.domain.dto.request.CarSaveRequestDTO;
import com.bootjpabase.api.car.domain.dto.request.CarUpdateRequestDTO;
import com.bootjpabase.api.car.domain.dto.response.CarResponseDTO;
import com.bootjpabase.api.car.service.CarService;
import com.bootjpabase.api.car.service.CarServiceTx;
import com.bootjpabase.global.domain.dto.BaseResponse;
import com.bootjpabase.global.domain.dto.BaseResponseFactory;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Car Management API", description = "자동차 관리 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/car")
public class CarController {

    private final CarService carService;
    private final CarServiceTx carServiceTx;

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "저장 성공", content = @Content(schema = @Schema(implementation = BaseResponse.class))),
            @ApiResponse(responseCode = "204", description = "저장 실패", content = @Content(schema = @Schema(implementation = BaseResponse.class)))
    })
    @Operation(summary = "자동차 단건 저장", description = "자동차 단건 저장 API")
    @PostMapping("")
    public BaseResponse<CarResponseDTO> saveCar(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "json",
                    content = @Content(schema = @Schema(implementation = CarSaveRequestDTO.class)))
            @RequestBody @Valid CarSaveRequestDTO dto
    ) throws Exception {
        return BaseResponseFactory.success(carServiceTx.saveCar(dto));
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "저장 성공", content = @Content(schema = @Schema(implementation = BaseResponse.class))),
            @ApiResponse(responseCode = "204", description = "저장 실패", content = @Content(schema = @Schema(implementation = BaseResponse.class)))
    })
    @Operation(summary = "자동차 다건 저장", description = "자동차 다건 저장 API")
    @PostMapping("/list")
    public BaseResponse<List<CarResponseDTO>> saveCarList(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "json", content = @Content(
                    examples = {
                            @ExampleObject(name = "예제1", value = """
                                            [
                                                {
                                                    "category": "준중형 SUV",
                                                    "manufacturer": "현대",
                                                    "modelName": "아이오닉",
                                                    "productionYear": "2024"
                                                },
                                                {
                                                    "category": "대형 RV",
                                                    "manufacturer": "현대",
                                                    "modelName": "스타리아",
                                                    "productionYear": "2022"
                                                }
                                            ]
                                    """)
                    }
            ))
            @RequestBody @Valid List<CarSaveRequestDTO> dto
    ) throws Exception {
        return BaseResponseFactory.success(carServiceTx.saveAllCar(dto));
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = CarResponseDTO.class))),
            @ApiResponse(responseCode = "204", description = "내용 없음", content = @Content(schema = @Schema(implementation = BaseResponse.class)))
    })
    @Operation(summary = "자동차 목록 조회", description = "자동차 목록 조회 API (검색 조건이 없을 경우 전체목록 조회)")
    @GetMapping("")
    public BaseResponse<List<CarResponseDTO>> getCarList(
            @ParameterObject CarListRequestDTO dto,
            @PageableDefault(page = 0, size = 10, sort = "createdDate", direction = Sort.Direction.DESC) Pageable pageable
    ) throws Exception {
        return BaseResponseFactory.successWithPagination(carService.getCarList(dto, pageable));
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "수정 성공", content = @Content(schema = @Schema(implementation = BaseResponse.class))),
            @ApiResponse(responseCode = "204", description = "수정 실패", content = @Content(schema = @Schema(implementation = BaseResponse.class)))
    })
    @Operation(summary = "자동차 수정", description = "자동차 수정 API")
    @PatchMapping("/{carSn}")
    public BaseResponse<CarResponseDTO> updateCar(
            @PathVariable("carSn") Long carSn,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "json",
                    content = @Content(schema = @Schema(implementation = CarUpdateRequestDTO.class)))
            @Valid @RequestBody CarUpdateRequestDTO dto
    ) throws Exception {
        return BaseResponseFactory.success(carServiceTx.updateCar(carSn, dto));
    }
}

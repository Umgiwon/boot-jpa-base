package com.bootjpabase.carmanager.api.car.controller;

import com.bootjpabase.carmanager.api.car.domain.dto.request.CarListRequestDTO;
import com.bootjpabase.carmanager.api.car.domain.dto.request.CarSaveRequestDTO;
import com.bootjpabase.carmanager.api.car.domain.dto.request.CarUpdateRequestDTO;
import com.bootjpabase.carmanager.api.car.domain.dto.response.CarResponseDTO;
import com.bootjpabase.carmanager.api.car.service.CarService;
import com.bootjpabase.carmanager.api.car.service.CarServiceTx;
import com.bootjpabase.carmanager.global.constant.ResponseMessageConst;
import com.bootjpabase.carmanager.global.domain.dto.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "자동차 관리 API", description = "Car Management - 자동차 관리 API")
@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/cars/")
public class CarController {

    private final CarService carService;
    private final CarServiceTx carServiceTx;

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = CarResponseDTO.class))),
            @ApiResponse(responseCode = "204", description = "내용 없음", content = @Content(schema = @Schema(implementation = BaseResponse.class)))
    })
    @Operation(summary = "자동차 목록 조회", description = "자동차 목록 조회 API (검색 조건이 없을 경우 전체목록 조회)")
    @GetMapping("carList")
    public BaseResponse getCarList(
            @Parameter(name = "category", description = "카테고리", example = "미니 SUV", in = ParameterIn.QUERY, schema = @Schema(implementation = String.class))
            @RequestParam(required = false) String category,
            @Parameter(name = "manufacturer", description = "제조사", example = "현대", in = ParameterIn.QUERY, schema = @Schema(implementation = String.class))
            @RequestParam(required = false) String manufacturer,
            @Parameter(name = "modelName", description = "모델명", example = "코나", in = ParameterIn.QUERY, schema = @Schema(implementation = String.class))
            @RequestParam(required = false) String modelName,
            @Parameter(name = "productionYear", description = "생산년도", example = "2024", in = ParameterIn.QUERY, schema = @Schema(implementation = Integer.class))
            @RequestParam(required = false) Integer productionYear,
            @Parameter(name = "rentalYn", description = "대여 가능 여부(N: 불가능, Y: 가능)", example = "Y", in = ParameterIn.QUERY, schema = @Schema(implementation = String.class))
            @RequestParam(required = false) String rentalYn
    ) throws Exception {
        BaseResponse baseResponse;

        // 조회용 dto set
        CarListRequestDTO dto = CarListRequestDTO.builder()
                .category(ObjectUtils.isEmpty(category) ? null : category)
                .manufacturer(ObjectUtils.isEmpty(manufacturer) ? null : manufacturer)
                .modelName(ObjectUtils.isEmpty(modelName) ? null : modelName)
                .productionYear(ObjectUtils.isEmpty(productionYear) ? null : productionYear)
                .rentalYn(ObjectUtils.isEmpty(rentalYn) ? null : rentalYn)
                .build();

        // Car 목록 조회
        List<CarResponseDTO> resultList = carService.getCarList(dto);

        // response set
        baseResponse = ObjectUtils.isEmpty(resultList)
                ? BaseResponse.getBaseResponseBuilder(HttpStatus.NO_CONTENT.value(), ResponseMessageConst.NO_CONTENT, 0, CarResponseDTO.builder().build())
                : BaseResponse.getBaseResponseBuilder(HttpStatus.OK.value(), ResponseMessageConst.SELECT_SUCCESS, resultList.size(), resultList);

        return baseResponse;
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "저장 성공", content = @Content(schema = @Schema(implementation = BaseResponse.class))),
            @ApiResponse(responseCode = "204", description = "저장 실패", content = @Content(schema = @Schema(implementation = BaseResponse.class)))
    })
    @Operation(summary = "자동차 단건 저장", description = "자동차 단건 저장 API")
    @PostMapping("car")
    public BaseResponse saveCar(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "json",
                    content = @Content(schema = @Schema(implementation = CarSaveRequestDTO.class)))
            @RequestBody @Validated CarSaveRequestDTO dto
    ) throws Exception {
        BaseResponse baseResponse;

        // Car 단건 저장
        boolean result = carServiceTx.saveCar(dto);

        // response set
        if(result) {
            baseResponse = BaseResponse.getBaseResponseBuilder(HttpStatus.OK.value(), ResponseMessageConst.SAVE_SUCCESS, 1 , true);
        } else {
            baseResponse = BaseResponse.getBaseResponseBuilder(HttpStatus.NO_CONTENT.value(), ResponseMessageConst.SAVE_FAIL, 0, false);
        }

        return baseResponse;
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "저장 성공", content = @Content(schema = @Schema(implementation = BaseResponse.class))),
            @ApiResponse(responseCode = "204", description = "저장 실패", content = @Content(schema = @Schema(implementation = BaseResponse.class)))
    })
    @Operation(summary = "자동차 다건 저장", description = "자동차 다건 저장 API")
    @PostMapping("carList")
    public BaseResponse saveCarList(
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
            @RequestBody @Validated List<CarSaveRequestDTO> dto
    ) throws Exception {
        BaseResponse baseResponse;

        // Car 다건 저장
        boolean result = carServiceTx.saveAllCar(dto);

        // response set
        if(result) {
            baseResponse = BaseResponse.getBaseResponseBuilder(HttpStatus.OK.value(), ResponseMessageConst.SAVE_SUCCESS, dto.size(), true);
        } else {
            baseResponse = BaseResponse.getBaseResponseBuilder(HttpStatus.NO_CONTENT.value(), ResponseMessageConst.SAVE_FAIL, 0, false);
        }

        return baseResponse;
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "수정 성공", content = @Content(schema = @Schema(implementation = BaseResponse.class))),
            @ApiResponse(responseCode = "204", description = "수정 실패", content = @Content(schema = @Schema(implementation = BaseResponse.class)))
    })
    @Operation(summary = "자동차 수정", description = "자동차 수정 API")
    @PatchMapping("car")
    public BaseResponse updateCar(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "json",
                    content = @Content(schema = @Schema(implementation = CarUpdateRequestDTO.class)))
            @RequestBody CarUpdateRequestDTO dto
    ) throws Exception {
        BaseResponse baseResponse;

        // Car 수정
        boolean result = carServiceTx.updateCar(dto);

        // response set
        if(result) {
            baseResponse = BaseResponse.getBaseResponseBuilder(HttpStatus.OK.value(), ResponseMessageConst.UPDATE_SUCCESS, 1, true);
        } else {
            baseResponse = BaseResponse.getBaseResponseBuilder(HttpStatus.NO_CONTENT.value(), ResponseMessageConst.UPDATE_FAIL, 0, false);
        }

        return baseResponse;
    }
}

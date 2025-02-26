package com.bootjpabase;

import com.bootjpabase.api.car.domain.dto.request.CarUpdateRequestDTO;
import com.bootjpabase.api.car.domain.entity.Car;
import com.bootjpabase.api.car.repository.CarRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@Transactional
//@SpringBootTest()
class APITest {

//    @Autowired
    private MockMvc mockMvc;

//    @Autowired
    private ObjectMapper objectMapper;

//    @Autowired
    private CarRepository carRepository;

    @BeforeEach
    void setUp() {

        // 테스트 실행 전 데이터 초기화
        carRepository.deleteAll();

        // 테스트 초기 데이터 생성
        List<Car> cars = List.of(
                createCar("미니 SUV", "현대", "코나", 2024, "Y"),
                createCar("준중형 SUV", "현대", "아이오닉", 2024, "Y"),
                createCar("대형 RV", "현대", "스타리아", 2022, "Y")
        );

        // 테스트 초기 데이터 저장
        carRepository.saveAll(cars);
    }

    /**
     * 초기화 Car 객체 builder 메소드
     * @param category
     * @param manufacturer
     * @param modelName
     * @param productionYear
     * @param rentalYn
     * @return
     */
    private Car createCar(String category, String manufacturer, String modelName, int productionYear, String rentalYn) {
        return Car.builder()
                .category(category)
                .manufacturer(manufacturer)
                .modelName(modelName)
                .productionYear(productionYear)
                .rentalYn(rentalYn)
                .build();
    }

    @DisplayName("자동차 등록 API 테스트")
    @Test
    void saveCarTest() throws Exception {

        // Given (저장할 테스트 자동차 entity 생성)
        Car testSaveCar = Car.builder()
                .category("중형 트럭")
                .manufacturer("현대")
                .modelName("포터")
                .productionYear(2024)
                .rentalYn("Y")
                .build();

        // When & Then
        mockMvc.perform(post("/cars/car")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testSaveCar)))
                .andExpect(status().isOk()) // API 응답 상태가 200인지 확인
                .andExpect(jsonPath("$.httpCode").value(200))
                .andDo(print()); // 요청/응답 출력
    }

    @DisplayName("자동차 전체 목록 조회 API 테스트")
    @Test
    void getCarListTest() throws Exception {

        // Given
        Car car = carRepository.findAll().get(0);

        // When & Then
        mockMvc.perform(get("/cars/carList"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].category").value(car.getCategory()))
                .andExpect(jsonPath("$.data[0].manufacturer").value(car.getManufacturer()))
                .andExpect(jsonPath("$.data[0].modelName").value(car.getModelName()))
                .andExpect(jsonPath("$.data[0].productionYear").value(car.getProductionYear()))
                .andExpect(jsonPath("$.data[0].rentalYn").value(car.getRentalYn()))
                .andDo(print());
    }

    @DisplayName("자동차 대여 가능 여부 조회 API 테스트")
    @Test
    void getCarByRentalYnTest() throws Exception {

        // Given
        Car car = carRepository.findAll().get(0);

        // When & Then
        mockMvc.perform(get("/cars/carList").param("rentalYn", car.getRentalYn()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].category").value(car.getCategory()))
                .andExpect(jsonPath("$.data[0].manufacturer").value(car.getManufacturer()))
                .andExpect(jsonPath("$.data[0].modelName").value(car.getModelName()))
                .andExpect(jsonPath("$.data[0].productionYear").value(car.getProductionYear()))
                .andExpect(jsonPath("$.data[0].rentalYn").value(car.getRentalYn()))
                .andDo(print());
    }

    @DisplayName("자동차 카테고리별 조회 API 테스트")
    @Test
    void getCarByCategoryTest() throws Exception {

        // Given
        Car car = carRepository.findAll().get(0);

        // When & Then
        mockMvc.perform(get("/cars/carList").param("category", car.getCategory()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].category").value(car.getCategory()))
                .andExpect(jsonPath("$.data[0].manufacturer").value(car.getManufacturer()))
                .andExpect(jsonPath("$.data[0].modelName").value(car.getModelName()))
                .andExpect(jsonPath("$.data[0].productionYear").value(car.getProductionYear()))
                .andExpect(jsonPath("$.data[0].rentalYn").value(car.getRentalYn()))
                .andDo(print());
    }

    @DisplayName("자동차 조건별 조회 API 테스트")
    @Test
    void getCarByConditionTest() throws Exception {

        // Given
        Car car = carRepository.findAll().get(0);

        // When & Then
        mockMvc.perform(get("/cars/carList")
                        .param("manufacturer", car.getManufacturer())
                        .param("modelName", car.getModelName())
                        .param("productionYear", String.valueOf(car.getProductionYear())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].category").value(car.getCategory()))
                .andExpect(jsonPath("$.data[0].manufacturer").value(car.getManufacturer()))
                .andExpect(jsonPath("$.data[0].modelName").value(car.getModelName()))
                .andExpect(jsonPath("$.data[0].productionYear").value(car.getProductionYear()))
                .andExpect(jsonPath("$.data[0].rentalYn").value(car.getRentalYn()))
                .andDo(print());
    }

    @DisplayName("자동차 수정 API 테스트")
    @Test
    void updateCarTest() throws Exception {

        // Given
        Car car = carRepository.findAll().get(0);

        // 수정할 데이터
        CarUpdateRequestDTO updateDto = CarUpdateRequestDTO.builder()
                .carSn(car.getCarSn())
                .rentalYn("N")
                .rentalDescription("수리로 인한 대여 중단")
                .build();

        // When & Then
        mockMvc.perform(patch("/cars/car")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isOk()) // API 응답 상태가 200인지 확인
                .andExpect(jsonPath("$.httpCode").value(200))
                .andDo(print()); // 요청/응답 출력
    }
}
package com.bootjpabase;

import com.bootjpabase.api.car.domain.dto.request.CarSaveRequestDTO;
import com.bootjpabase.api.car.domain.dto.request.CarUpdateRequestDTO;
import com.bootjpabase.api.car.domain.entity.Car;
import com.bootjpabase.api.car.repository.CarRepository;
import com.bootjpabase.api.user.domain.entity.User;
import com.bootjpabase.api.user.repository.UserRepository;
import com.bootjpabase.global.config.jwt.component.TokenProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@Transactional
@SpringBootTest()
class APITest {

    private final BCryptPasswordEncoder encoder;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private CarRepository carRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TokenProvider tokenProvider;
    private String token;

    @Autowired
    public APITest(BCryptPasswordEncoder encoder) {
        this.encoder = encoder;
    }

    @BeforeEach
    void setUp() throws Exception {

        // 테스트 실행 전 데이터 초기화
        carRepository.deleteAll();
        userRepository.deleteAll();

        // 테스트 유저 생성
        User testUser = User.builder()
                .userId("user")
                .userName("홍길동")
                .userPassword(encoder.encode("user1"))
                .userPhone("01011112222")
                .userEmail("test@test.com")
                .build();

        // 테스트 유저 저장
        userRepository.save(testUser);

        // 토큰 발급
        token = tokenProvider.createToken(testUser, "access");

        // 인증 컨텍스트 설정 (JPA Auditing을 위해)
        Authentication auth = tokenProvider.getAuthentication(token);
        SecurityContextHolder.getContext().setAuthentication(auth);

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
     *
     * @param category 차량 카테고리
     * @param manufacturer 제조사
     * @param modelName 모델명
     * @param productionYear 생산년도
     * @param rentalYn 대여가능여부
     * @return 생성된 Car 객체
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

        // given : 저장할 테스트 자동차 entity 생성
        CarSaveRequestDTO saveDto = CarSaveRequestDTO.builder()
                .category("중형 트럭")
                .manufacturer("현대")
                .modelName("포터")
                .productionYear(2024)
                .build();

        // When : API 호출 - 컨트롤러가 List<CarSaveRequestDTO>를 기대하므로 리스트로 감싸서 전송
        mockMvc.perform(post("/api/car")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + token)
                        .content(objectMapper.writeValueAsString(List.of(saveDto))))
                .andExpect(status().isOk()) // API 응답 상태가 200인지 확인
                .andDo(print()); // 요청/응답 출력

        // then : 저장된 데이터 검증
        Car savedCar = carRepository.findByModelName(saveDto.getModelName());
        assertThat(savedCar).isNotNull();
        assertThat(savedCar.getCategory()).isEqualTo(saveDto.getCategory());
        assertThat(savedCar.getManufacturer()).isEqualTo(saveDto.getManufacturer());
        assertThat(savedCar.getModelName()).isEqualTo(saveDto.getModelName());
        assertThat(savedCar.getProductionYear()).isEqualTo(saveDto.getProductionYear());
    }

    @DisplayName("자동차 전체 목록 조회 API 테스트")
    @Test
    void getCarListTest() throws Exception {

        // Given
        List<Car> cars = carRepository.findAll();
        assertThat(cars).isNotEmpty();
        Car car = cars.get(0);

        // When & Then
        mockMvc.perform(get("/api/car")
                        .header("Authorization", "Bearer " + token))
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
        List<Car> cars = carRepository.findAll();
        assertThat(cars).isNotEmpty();
        Car car = cars.get(0);

        // When & Then
        mockMvc.perform(get("/api/car")
                        .header("Authorization", "Bearer " + token)
                        .param("rentalYn", car.getRentalYn()))
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
        List<Car> cars = carRepository.findAll();
        assertThat(cars).isNotEmpty();
        Car car = cars.get(0);

        // When & Then
        mockMvc.perform(get("/api/car")
                        .header("Authorization", "Bearer " + token)
                        .param("category", car.getCategory()))
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
        List<Car> cars = carRepository.findAll();
        assertThat(cars).isNotEmpty();
        Car car = cars.get(0);

        // When & Then
        mockMvc.perform(get("/api/car")
                        .header("Authorization", "Bearer " + token)
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
        Long carSn = car.getCarSn();

        // 수정할 데이터
        CarUpdateRequestDTO updateDto = CarUpdateRequestDTO.builder()
                .rentalYn("N")
                .rentalDescription("수리로 인한 대여 중단")
                .build();

        // When : API 호출 (자동차 수정)
        mockMvc.perform(patch("/api/car/{carSn}", carSn)
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isOk()) // API 응답 상태가 200인지 확인
                .andDo(print()); // 요청/응답 출력

        // then : 수정된 데이터 검증
        Car updatedCar = carRepository.findById(carSn).orElse(null);
        assertThat(updatedCar).isNotNull();
        assertThat(updatedCar.getRentalYn()).isEqualTo(updateDto.getRentalYn());
        assertThat(updatedCar.getRentalDescription()).isEqualTo(updateDto.getRentalDescription());
    }
}

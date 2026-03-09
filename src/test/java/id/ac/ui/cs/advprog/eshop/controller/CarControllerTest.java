package id.ac.ui.cs.advprog.eshop.controller;

import java.util.Arrays;
import java.util.Collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import id.ac.ui.cs.advprog.eshop.model.Car;
import id.ac.ui.cs.advprog.eshop.service.CarService;

@WebMvcTest(CarController.class)
class CarControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CarService carService;

    private Car car;

    @BeforeEach
    void setUp() {
        car = new Car();
        car.setCarId("car-id-123");
        car.setCarName("Toyota Avanza");
        car.setCarColor("Black");
        car.setCarQuantity(10);
    }

    // CREATE TESTS

    @Test
    void testCreateCarPage() throws Exception {
        mockMvc.perform(get("/car/createCar"))
                .andExpect(status().isOk())
                .andExpect(view().name("createCar"))
                .andExpect(model().attributeExists("car"));
    }

    @Test
    void testCreateCarPost() throws Exception {
        when(carService.create(any(Car.class))).thenReturn(car);

        mockMvc.perform(post("/car/createCar")
                .param("carName", "Toyota Avanza")
                .param("carColor", "Black")
                .param("carQuantity", "10"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("listCar"));

        verify(carService, times(1)).create(any(Car.class));
    }

    // LIST TESTS

    @Test
    void testCarListPage() throws Exception {
        when(carService.findAll()).thenReturn(Arrays.asList(car));

        mockMvc.perform(get("/car/listCar"))
                .andExpect(status().isOk())
                .andExpect(view().name("carList"))
                .andExpect(model().attributeExists("cars"));
    }

    @Test
    void testCarListPage_Empty() throws Exception {
        when(carService.findAll()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/car/listCar"))
                .andExpect(status().isOk())
                .andExpect(view().name("carList"));
    }

    // EDIT TESTS

    @Test
    void testEditCarPage() throws Exception {
        when(carService.findById("car-id-123")).thenReturn(car);

        mockMvc.perform(get("/car/editCar/car-id-123"))
                .andExpect(status().isOk())
                .andExpect(view().name("editCar"))
                .andExpect(model().attributeExists("car"));
    }

    @Test
    void testEditCarPost() throws Exception {
        mockMvc.perform(post("/car/editCar")
                .param("carId", "car-id-123")
                .param("carName", "Honda Jazz")
                .param("carColor", "Red")
                .param("carQuantity", "5"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("listCar"));

        verify(carService, times(1)).update(eq("car-id-123"), any(Car.class));
    }

    // DELETE TESTS

    @Test
    void testDeleteCar() throws Exception {
        mockMvc.perform(post("/car/deleteCar")
                .param("carId", "car-id-123"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("listCar"));

        verify(carService, times(1)).deleteCarById("car-id-123");
    }
}

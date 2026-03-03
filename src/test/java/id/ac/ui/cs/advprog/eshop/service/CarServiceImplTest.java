package id.ac.ui.cs.advprog.eshop.service;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import id.ac.ui.cs.advprog.eshop.model.Car;
import id.ac.ui.cs.advprog.eshop.repository.CarRepository;

@ExtendWith(MockitoExtension.class)
class CarServiceImplTest {

    @Mock
    CarRepository carRepository;

    @InjectMocks
    CarServiceImpl carService;

    Car car;

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
    void testCreate() {
        when(carRepository.create(car)).thenReturn(car);

        Car result = carService.create(car);

        assertNotNull(result);
        assertEquals("Toyota Avanza", result.getCarName());
        verify(carRepository, times(1)).create(car);
    }

    // FIND ALL TESTS

    @Test
    void testFindAll() {
        List<Car> carList = new ArrayList<>();
        carList.add(car);
        when(carRepository.findAll()).thenReturn(carList.iterator());

        List<Car> result = carService.findAll();

        assertEquals(1, result.size());
        assertEquals("Toyota Avanza", result.get(0).getCarName());
        verify(carRepository, times(1)).findAll();
    }

    @Test
    void testFindAll_Empty() {
        List<Car> emptyList = new ArrayList<>();
        when(carRepository.findAll()).thenReturn(emptyList.iterator());

        List<Car> result = carService.findAll();

        assertTrue(result.isEmpty());
    }

    // FIND BY ID TESTS

    @Test
    void testFindById_CarExists() {
        when(carRepository.findById("car-id-123")).thenReturn(car);

        Car result = carService.findById("car-id-123");

        assertNotNull(result);
        assertEquals("car-id-123", result.getCarId());
        assertEquals("Toyota Avanza", result.getCarName());
        verify(carRepository, times(1)).findById("car-id-123");
    }

    @Test
    void testFindById_CarNotFound() {
        when(carRepository.findById("non-existent-id")).thenReturn(null);

        Car result = carService.findById("non-existent-id");

        assertNull(result);
        verify(carRepository, times(1)).findById("non-existent-id");
    }

    // UPDATE TESTS

    @Test
    void testUpdate() {
        Car updatedCar = new Car();
        updatedCar.setCarName("Honda Jazz");
        updatedCar.setCarColor("Red");
        updatedCar.setCarQuantity(5);

        carService.update("car-id-123", updatedCar);

        verify(carRepository, times(1)).update("car-id-123", updatedCar);
    }

    // DELETE TESTS

    @Test
    void testDeleteCarById() {
        carService.deleteCarById("car-id-123");

        verify(carRepository, times(1)).delete("car-id-123");
    }
}

package id.ac.ui.cs.advprog.eshop.repository;

import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import id.ac.ui.cs.advprog.eshop.model.Car;

class CarRepositoryTest {

    CarRepository carRepository;

    @BeforeEach
    void setUp() {
        carRepository = new CarRepository();
    }

    // CREATE TESTS

    @Test
    void testCreate() {
        Car car = new Car();
        car.setCarName("Toyota Avanza");
        car.setCarColor("Black");
        car.setCarQuantity(10);

        Car result = carRepository.create(car);

        assertNotNull(result);
        assertNotNull(result.getCarId());
        assertEquals("Toyota Avanza", result.getCarName());
        assertEquals("Black", result.getCarColor());
        assertEquals(10, result.getCarQuantity());
    }

    // FIND ALL TESTS

    @Test
    void testFindAll_Empty() {
        Iterator<Car> iterator = carRepository.findAll();
        assertFalse(iterator.hasNext());
    }

    @Test
    void testFindAll_MultipleCars() {
        Car car1 = new Car();
        car1.setCarName("Toyota Avanza");
        car1.setCarColor("Black");
        car1.setCarQuantity(10);

        Car car2 = new Car();
        car2.setCarName("Honda Jazz");
        car2.setCarColor("Red");
        car2.setCarQuantity(5);

        carRepository.create(car1);
        carRepository.create(car2);

        Iterator<Car> iterator = carRepository.findAll();
        assertTrue(iterator.hasNext());
        iterator.next();
        assertTrue(iterator.hasNext());
        iterator.next();
        assertFalse(iterator.hasNext());
    }

    // FIND BY ID TESTS

    @Test
    void testFindById_Found() {
        Car car = new Car();
        car.setCarName("Toyota Avanza");
        car.setCarColor("Black");
        car.setCarQuantity(10);
        carRepository.create(car);

        Car result = carRepository.findById(car.getCarId());

        assertNotNull(result);
        assertEquals(car.getCarId(), result.getCarId());
        assertEquals("Toyota Avanza", result.getCarName());
    }

    @Test
    void testFindById_NotFound() {
        Car result = carRepository.findById("non-existent-id");
        assertNull(result);
    }

    @Test
    void testFindById_NotFoundWithExistingCars() {
        Car car = new Car();
        car.setCarName("Toyota Avanza");
        car.setCarColor("Black");
        car.setCarQuantity(10);
        carRepository.create(car);

        Car result = carRepository.findById("different-id");
        assertNull(result);
    }

    // UPDATE TESTS

    @Test
    void testUpdate_Found() {
        Car car = new Car();
        car.setCarName("Toyota Avanza");
        car.setCarColor("Black");
        car.setCarQuantity(10);
        carRepository.create(car);

        Car updatedCar = new Car();
        updatedCar.setCarName("Honda Jazz");
        updatedCar.setCarColor("Red");
        updatedCar.setCarQuantity(5);

        Car result = carRepository.update(car.getCarId(), updatedCar);

        assertNotNull(result);
        assertEquals("Honda Jazz", result.getCarName());
        assertEquals("Red", result.getCarColor());
        assertEquals(5, result.getCarQuantity());
    }

    @Test
    void testUpdate_NotFound() {
        Car updatedCar = new Car();
        updatedCar.setCarName("Honda Jazz");
        updatedCar.setCarColor("Red");
        updatedCar.setCarQuantity(5);

        Car result = carRepository.update("non-existent-id", updatedCar);
        assertNull(result);
    }

    @Test
    void testUpdate_NotFoundWithExistingCars() {
        // Add a car first
        Car car = new Car();
        car.setCarName("Toyota Avanza");
        car.setCarColor("Black");
        car.setCarQuantity(10);
        carRepository.create(car);

        Car updatedCar = new Car();
        updatedCar.setCarName("Honda Jazz");
        updatedCar.setCarColor("Red");
        updatedCar.setCarQuantity(5);

        Car result = carRepository.update("different-id", updatedCar);
        assertNull(result);
    }

    // DELETE TESTS

    @Test
    void testDelete_Found() {
        Car car = new Car();
        car.setCarName("Toyota Avanza");
        car.setCarColor("Black");
        car.setCarQuantity(10);
        carRepository.create(car);

        carRepository.delete(car.getCarId());

        assertNull(carRepository.findById(car.getCarId()));
    }

    @Test
    void testDelete_NotFound() {
        // Should not throw when deleting non-existent car
        assertDoesNotThrow(() -> carRepository.delete("non-existent-id"));
    }
}

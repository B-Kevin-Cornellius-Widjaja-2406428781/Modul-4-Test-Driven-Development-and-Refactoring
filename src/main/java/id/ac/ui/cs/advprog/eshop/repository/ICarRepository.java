package id.ac.ui.cs.advprog.eshop.repository;

import java.util.Iterator;

import id.ac.ui.cs.advprog.eshop.model.Car;

public interface ICarRepository {
    Car create(Car car);

    Iterator<Car> findAll();

    Car findById(String carId);

    Car update(String id, Car updatedCar);

    void delete(String id);
}

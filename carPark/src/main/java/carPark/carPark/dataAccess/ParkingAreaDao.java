package carPark.carPark.dataAccess;

import org.springframework.data.jpa.repository.JpaRepository;

import carPark.carPark.entities.ParkingArea;


public interface ParkingAreaDao extends JpaRepository<ParkingArea, Integer>{
	
	ParkingArea getByName(String name);
	ParkingArea getById(int id);
}

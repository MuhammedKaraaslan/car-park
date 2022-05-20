package carPark.carPark.dataAccess;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import carPark.carPark.entities.ParkingRecord;
import carPark.carPark.entities.vehicles.Vehicle;


public interface ParkingRecordDao extends JpaRepository<ParkingRecord, Integer>{

	List<ParkingRecord> getByVehicle(Vehicle vehicle);
	ParkingRecord getByVehicleOrderByIdDesc(Vehicle vehicle);
	
}

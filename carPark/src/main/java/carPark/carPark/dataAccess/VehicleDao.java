package carPark.carPark.dataAccess;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import carPark.carPark.entities.dtos.VehicleWithParkingDetailDto;
import carPark.carPark.entities.vehicles.Vehicle;



public interface VehicleDao extends JpaRepository<Vehicle, String>{
	
	Vehicle getByLicensePlate(String licensePlate);
	
	@Query("Select new com.huawei.parkinglot.entity.dtos.VehicleWithParkingDetailDto(v.licensePlate, p.checkInDate, p.checkOutDate, p.fee) From Vehicle v Inner Join v.parkingRecords p where v.licensePlate=:licensePlate") 
	List<VehicleWithParkingDetailDto> getVehicleWithParkingRecords(String licensePlate);
	

}
 
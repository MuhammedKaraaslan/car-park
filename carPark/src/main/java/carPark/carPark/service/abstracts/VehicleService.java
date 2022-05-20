package carPark.carPark.service.abstracts;

import java.time.LocalDateTime;
import java.util.List;

import carPark.carPark.core.utilities.results.DataResult;
import carPark.carPark.core.utilities.results.Result;
import carPark.carPark.entities.dtos.VehicleDto;
import carPark.carPark.entities.dtos.VehicleWithParkingDetailDto;

public interface VehicleService {
	Result add(VehicleDto vehicle);
	Result checkIn(LocalDateTime checkInDate, VehicleDto vehicle, int parkingAreaId);
	Result checkOut(VehicleDto vehicle);
	DataResult<List<VehicleWithParkingDetailDto>> getVehicleWithParkingRecords(String licensePlate);
}

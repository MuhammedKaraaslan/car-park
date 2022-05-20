package carPark.carPark.service.abstracts;

import carPark.carPark.core.utilities.results.DataResult;
import carPark.carPark.core.utilities.results.Result;
import carPark.carPark.entities.dtos.VehicleDto;

public interface VehicleCheckService {
	DataResult<VehicleDto> getById(VehicleDto vehicle);
	Result checkLicensePlate(VehicleDto vehicle);
	Result checkSaveConditions(VehicleDto vehicle);
	Result checkCapacity(int parkingAreaId);
	Result checkIfVehicleAlreadyParking(String licensePlate);
	Result checkAllCheckInConditions(String licensePlate, int parkingAreaId);
}

package carPark.carPark.service.concretes;

import java.time.LocalDateTime;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import carPark.carPark.core.utilities.results.DataResult;
import carPark.carPark.core.utilities.results.Result;
import carPark.carPark.core.utilities.results.SuccessResult;
import carPark.carPark.dataAccess.VehicleDao;
import carPark.carPark.entities.dtos.VehicleDto;
import carPark.carPark.entities.dtos.VehicleWithParkingDetailDto;
import carPark.carPark.entities.vehicles.Vehicle;
import carPark.carPark.entities.vehicles.VehicleType;
import carPark.carPark.service.abstracts.VehicleCheckService;
import carPark.carPark.service.abstracts.VehicleCommonMethodsService;
import carPark.carPark.service.abstracts.VehicleService;


@Service
public class MinivanVehicleService implements VehicleService {

	private VehicleDao vehicleDao;
	private VehicleCheckService vehicleCheckService;
	private VehicleCommonMethodsService vehicleCommonMethodsService;
	private ModelMapper modelMapper;
	
	@Autowired
	public MinivanVehicleService(VehicleDao vehicleDao, VehicleCheckService vehicleCheckService,
			VehicleCommonMethodsService vehicleCommonMethodsService, ModelMapper modelMapper) {
		super();
		this.vehicleDao = vehicleDao;
		this.vehicleCheckService = vehicleCheckService;
		this.vehicleCommonMethodsService = vehicleCommonMethodsService;
		this.modelMapper = modelMapper;
	}


	//Save vehicle if checkSaveConditions method in the vehicleCheckService returns success
	@Override
	public Result add(VehicleDto vehicleDto) {
		// Check saving conditions for input dto
		if(! vehicleCheckService.checkSaveConditions(vehicleDto).isSuccess()) {
			throw new IllegalArgumentException(vehicleCheckService.checkSaveConditions(vehicleDto).getMessage());
		}
		vehicleDto.setFeeFactor(1.15);
		vehicleDto.setType(VehicleType.MINIVAN);
		Vehicle vehicle = modelMapper.map(vehicleDto, Vehicle.class);
		this.vehicleDao.save(vehicle);
		return new SuccessResult("Minivan vehicle added.");
	}
	
	//Call checkIn method in the vehicleCommonMethodsService
	@Override
	public Result checkIn(LocalDateTime checkInDate, VehicleDto vehicle, int parkingAreaId) {
		return this.vehicleCommonMethodsService.checkIn(checkInDate, vehicle, parkingAreaId, this);
	}

	//Call checkOut method in the vehicleCommonMethodsService
	@Override
	public Result checkOut(VehicleDto vehicle) {
		return this.vehicleCommonMethodsService.checkOut(vehicle);
	}
	
	//Call getVehicleWithParkingRecords method in the vehicleCommonMethodsService
	@Override
	public DataResult<List<VehicleWithParkingDetailDto>> getVehicleWithParkingRecords(String licensePlate) {
		return this.vehicleCommonMethodsService.getVehicleWithParkingRecords(licensePlate);
	}


	


}

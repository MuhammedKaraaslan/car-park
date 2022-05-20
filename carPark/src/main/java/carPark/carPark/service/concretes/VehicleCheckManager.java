package carPark.carPark.service.concretes;

import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import carPark.carPark.core.utilities.results.DataResult;
import carPark.carPark.core.utilities.results.ErrorDataResult;
import carPark.carPark.core.utilities.results.ErrorResult;
import carPark.carPark.core.utilities.results.Result;
import carPark.carPark.core.utilities.results.SuccessDataResult;
import carPark.carPark.core.utilities.results.SuccessResult;
import carPark.carPark.dataAccess.ParkingAreaDao;
import carPark.carPark.dataAccess.VehicleDao;
import carPark.carPark.entities.ParkingArea;
import carPark.carPark.entities.dtos.VehicleDto;
import carPark.carPark.entities.vehicles.Vehicle;
import carPark.carPark.service.abstracts.VehicleCheckService;

@Service
public class VehicleCheckManager implements VehicleCheckService{
	
	private VehicleDao vehicleDao;
	private ParkingAreaDao parkingAreaDao;
	private ModelMapper modelMapper;
	
	@Autowired
	public VehicleCheckManager(VehicleDao vehicleDao, ParkingAreaDao parkingAreaDao, ModelMapper modelMapper) {
		super();
		this.vehicleDao = vehicleDao;
		this.parkingAreaDao = parkingAreaDao;
		this.modelMapper = modelMapper;
	}

	//Check if the license plate is valid or not
	@Override
	public Result checkLicensePlate(VehicleDto vehicle) {
		if(vehicle.getLicensePlate().isEmpty()) {
			throw new IllegalArgumentException("License plate can not be empty");
		}
		return new SuccessResult();
	}
	
	//Check if vehicle is exist or not
	@Override
	public DataResult<VehicleDto> getById(VehicleDto vehicleDto) {
		Optional<Vehicle> vehicle = this.vehicleDao.findById(vehicleDto.getLicensePlate());
		if(! vehicle.isPresent()) {
			return new ErrorDataResult<>("Vehicle could not found.");
		}
		vehicleDto = modelMapper.map(vehicle.get(), VehicleDto.class);
		return new SuccessDataResult<>(vehicleDto, "Vehicle listed.");
	}

	//Check all conditions above
	@Override
	public Result checkSaveConditions(VehicleDto vehicle) {
		if(getById(vehicle).isSuccess()) {
			throw new IllegalArgumentException("Vehicle already exist.");
		}
		else if(! checkLicensePlate(vehicle).isSuccess()) {
			return new ErrorResult(checkLicensePlate(vehicle).getMessage());
		}
		return new SuccessResult();
	}
	
	//Check if the capacity of the parking area is full or not
	@Override
	public Result checkCapacity(int parkingAreaId) {
		Optional<ParkingArea> parkingArea = this.parkingAreaDao.findById(parkingAreaId);
		if(parkingArea.isPresent() && parkingArea.get().getCapacity() <= parkingArea.get().getCurrentVehicleNumber()) {
			throw new IllegalArgumentException("Parking area is full.");
		}
		return new SuccessResult();
	}

	//Check if the vehicle is currently parking
	@Override
	public Result checkIfVehicleAlreadyParking(String licensePlate) {
		if(this.vehicleDao.getByLicensePlate(licensePlate) != null && !this.vehicleDao.getByLicensePlate(licensePlate).getParkingRecords().isEmpty() &&
			this.vehicleDao.getByLicensePlate(licensePlate).getParkingRecords() != null &&
				this.vehicleDao.getByLicensePlate(licensePlate).getParkingRecords().get(this.vehicleDao.getByLicensePlate(licensePlate).getParkingRecords().size()-1).getCheckOutDate() == null) {
				throw new IllegalArgumentException("Vehicle is already parked");
			}

		return new SuccessResult();
	}
 
	//Check all check in conditions above
	@Override
	public Result checkAllCheckInConditions(String licensePlate, int parkingAreaId) {
		if(! checkCapacity(parkingAreaId).isSuccess()) {
			return new ErrorResult(checkCapacity(parkingAreaId).getMessage());
		}
		else if(! checkIfVehicleAlreadyParking(licensePlate).isSuccess()) {
			return new ErrorResult(checkIfVehicleAlreadyParking(licensePlate).getMessage());
		}
		return new SuccessResult();
	}

}

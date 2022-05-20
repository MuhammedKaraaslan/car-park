package carPark.carPark.service.concretes;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import carPark.carPark.core.utilities.results.DataResult;
import carPark.carPark.core.utilities.results.ErrorResult;
import carPark.carPark.core.utilities.results.Result;
import carPark.carPark.core.utilities.results.SuccessDataResult;
import carPark.carPark.core.utilities.results.SuccessResult;
import carPark.carPark.dataAccess.ParkingAreaDao;
import carPark.carPark.dataAccess.ParkingRecordDao;
import carPark.carPark.dataAccess.VehicleDao;
import carPark.carPark.entities.ParkingArea;
import carPark.carPark.entities.ParkingRecord;
import carPark.carPark.entities.PricePeriod;
import carPark.carPark.entities.dtos.VehicleDto;
import carPark.carPark.entities.dtos.VehicleWithParkingDetailDto;
import carPark.carPark.entities.vehicles.Vehicle;
import carPark.carPark.service.abstracts.VehicleCheckService;
import carPark.carPark.service.abstracts.VehicleCommonMethodsService;
import carPark.carPark.service.abstracts.VehicleService;

@Service
public class VehicleCommonMethodsManager implements VehicleCommonMethodsService{
	
	private VehicleDao vehicleDao;
	private VehicleCheckService vehicleCheckService;
	private ParkingRecordDao parkingRecordDao;
	private ParkingAreaDao parkingAreaDao;
	private ModelMapper modelMapper;
	
	@Autowired
	public VehicleCommonMethodsManager(VehicleDao vehicleDao, VehicleCheckService vehicleCheckService,
			ParkingRecordDao parkingRecordDao, ParkingAreaDao parkingAreaDao, ModelMapper modelMapper) {
		super();
		this.vehicleDao = vehicleDao;
		this.vehicleCheckService = vehicleCheckService;
		this.parkingRecordDao = parkingRecordDao;
		this.parkingAreaDao = parkingAreaDao;
		this.modelMapper = modelMapper;
	}

	//Check in if all the conditions fit
	@Override
	public Result checkIn(LocalDateTime checkInDate, VehicleDto vehicleDto, int parkingAreaId, VehicleService vehicleService) {
		if(! this.vehicleCheckService.checkAllCheckInConditions(vehicleDto.getLicensePlate(), parkingAreaId).isSuccess()) {
			return new ErrorResult();
		}
		if(this.vehicleDao.findById(vehicleDto.getLicensePlate()).isEmpty()) {
			if(! vehicleCheckService.checkSaveConditions(vehicleDto).isSuccess()) {
				throw new IllegalArgumentException(vehicleCheckService.checkSaveConditions(vehicleDto).getMessage());
			}
			vehicleService.add(vehicleDto);
		}
		Optional<ParkingArea> parkingArea = this.parkingAreaDao.findById(parkingAreaId);
		if(! parkingArea.isPresent()) {
			return new ErrorResult("Parking area could not found.");
		}
		parkingArea.get().setCurrentVehicleNumber(parkingArea.get().getCurrentVehicleNumber()+1);
		Vehicle vehicle = modelMapper.map(vehicleDto, Vehicle.class);
		this.parkingRecordDao.save(new ParkingRecord(checkInDate, vehicle, parkingArea.get()));
		return new SuccessResult(vehicle.getLicensePlate() + " checked in.");
	}

	//Check out if all the conditions fit
	@Override
	public Result checkOut(VehicleDto vehicleDto) {
		Vehicle vehicle = this.vehicleDao.getByLicensePlate(vehicleDto.getLicensePlate());
		if(this.parkingRecordDao.getByVehicle(vehicle) == null) {
			throw new IllegalArgumentException("Vehicle could not found.");
		} 

		List<PricePeriod> periods = this.parkingRecordDao.getByVehicleOrderByIdDesc(vehicle).getParkingArea().getPriceList().getPricePeriods();
		// Get the time difference between check in and check out
		long hourRange = ChronoUnit.HOURS.between(LocalDateTime.now(), this.parkingRecordDao.getByVehicleOrderByIdDesc(vehicle).getCheckInDate());
		for (int i = 0; i < periods.size(); i++) {
			// Check which time period fits with parking time
			if(periods.get(i).getStartHour() <= hourRange && periods.get(i).getEndHour() > hourRange) {
				// Multiply parking fee and the fee factor of the vehicle
				this.parkingRecordDao.getByVehicleOrderByIdDesc(vehicle).setFee(periods.get(i).getPrice()*this.parkingRecordDao.getByVehicleOrderByIdDesc(vehicle).getVehicle().getFeeFactor());
				// Reduce the current vehicle number of the parking area
				this.parkingRecordDao.getByVehicleOrderByIdDesc(vehicle).getParkingArea().setCurrentVehicleNumber(this.parkingRecordDao.getByVehicleOrderByIdDesc(vehicle).getParkingArea().getCurrentVehicleNumber()-1);
				this.parkingRecordDao.getByVehicleOrderByIdDesc(vehicle).setCheckOutDate(LocalDateTime.now());
				this.parkingRecordDao.save(this.parkingRecordDao.getByVehicleOrderByIdDesc(vehicle));
				break;
			}
		}
		return new SuccessResult(vehicle.getLicensePlate() + " checked out.");
	}
	
	@Override
	public DataResult<List<VehicleWithParkingDetailDto>> getVehicleWithParkingRecords(String licensePlate) {
		if(this.vehicleDao.getVehicleWithParkingRecords(licensePlate).isEmpty()) {
			throw new IllegalArgumentException("Vehicle could not found.");
		}
		return new SuccessDataResult<>(this.vehicleDao.getVehicleWithParkingRecords(licensePlate), "Vehicle listed with parking record details.");
	}

}

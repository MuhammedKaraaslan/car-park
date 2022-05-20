package carPark.carPark;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;

import carPark.carPark.core.utilities.results.ErrorResult;
import carPark.carPark.core.utilities.results.Result;
import carPark.carPark.core.utilities.results.SuccessResult;
import carPark.carPark.dataAccess.ParkingAreaDao;
import carPark.carPark.dataAccess.VehicleDao;
import carPark.carPark.entities.ParkingArea;
import carPark.carPark.entities.ParkingRecord;
import carPark.carPark.entities.PriceList;
import carPark.carPark.entities.dtos.VehicleDto;
import carPark.carPark.entities.vehicles.Vehicle;
import carPark.carPark.entities.vehicles.VehicleType;
import carPark.carPark.service.concretes.VehicleCheckManager;



@SpringBootTest
class VehicleCheckServiceTest {
	
	private VehicleCheckManager vehiclecheckService;

	private VehicleDao vehicleDao;
	private ParkingAreaDao parkingAreaDao;
	private ModelMapper modelMapper;;

	@BeforeEach
	public void setUp() {
		vehicleDao = Mockito.mock(VehicleDao.class);
		parkingAreaDao = Mockito.mock(ParkingAreaDao.class);
		modelMapper = Mockito.mock(ModelMapper.class);
		
		vehiclecheckService = new VehicleCheckManager(vehicleDao , parkingAreaDao, modelMapper);
	}
	
	@Test
	void testCheckLicensePlateWithValidInput_shouldReturnSuccess() {
		VehicleDto vehicleDto = new VehicleDto("example", VehicleType.MINIVAN, 1.15, new ArrayList<ParkingRecord>());
		
		Result result = vehiclecheckService.checkLicensePlate(vehicleDto);
		
		Assertions.assertEquals(new SuccessResult().isSuccess(), result.isSuccess());
	}
	
	@Test
	void testCheckLicensePlateWithInValidInput_shouldReturnError() {
		VehicleDto vehicleDto = new VehicleDto("", VehicleType.MINIVAN, 1.15, new ArrayList<ParkingRecord>());

		assertThrows(IllegalArgumentException.class ,() -> vehiclecheckService.checkLicensePlate(vehicleDto).isSuccess());
	}
	
	@Test
	void testGetByIdWithValidInput_shouldReturnSuccess() {
		VehicleDto vehicleDto = new VehicleDto("example", VehicleType.MINIVAN, 1.15, new ArrayList<ParkingRecord>());
		Vehicle vehicle = new Vehicle("example", VehicleType.MINIVAN, 1.15, new ArrayList<ParkingRecord>());
		
		Mockito.when(vehicleDao.findById("example")).thenReturn(Optional.of(vehicle));
		
		Result result = vehiclecheckService.getById(vehicleDto);
		
		Assertions.assertEquals(new SuccessResult().isSuccess(), result.isSuccess());
	}
	
	@Test
	void testGetByIdWithInValidInput_shouldReturnError() {
		VehicleDto vehicleDto = new VehicleDto("example", VehicleType.MINIVAN, 1.15, new ArrayList<ParkingRecord>());
		
		Mockito.when(vehicleDao.findById("example")).thenReturn(Optional.empty());

		Result result = vehiclecheckService.getById(vehicleDto);
		
		Assertions.assertEquals(new ErrorResult().isSuccess(), result.isSuccess());
	}
	
	@Test
	void testCheckSaveConditionsWithValidInput_shouldReturnSuccess() {
		VehicleDto vehicleDto = new VehicleDto("example", VehicleType.MINIVAN, 1.15, new ArrayList<ParkingRecord>());
		
		Mockito.when(vehicleDao.findById("example")).thenReturn(Optional.empty());
		
		Result result = vehiclecheckService.checkSaveConditions(vehicleDto);
		
		Assertions.assertEquals(new SuccessResult().isSuccess(), result.isSuccess());
	}
	
	@Test
	void testCheckSaveConditionsWithInValidInput_shouldReturnError() {
		VehicleDto vehicleDto = new VehicleDto("", VehicleType.MINIVAN, 1.15, new ArrayList<ParkingRecord>());
		
		Mockito.when(vehicleDao.findById("example")).thenReturn(Optional.empty());

		assertThrows(IllegalArgumentException.class ,() -> vehiclecheckService.checkSaveConditions(vehicleDto).isSuccess());
	}
	
	@Test
	void testCheckCapacityWithValidInput_shouldReturnSuccess() {
		ParkingArea parkingArea = new ParkingArea(1, "parking area", 10, 0, "Istanbul", new PriceList(), new ArrayList<ParkingRecord>());
		
		Mockito.when(parkingAreaDao.findById(1)).thenReturn(Optional.of(parkingArea));
		
		Result result = vehiclecheckService.checkCapacity(parkingArea.getId());
		
		Assertions.assertEquals(new SuccessResult().isSuccess(), result.isSuccess());
	}
	
	@Test
	void testCheckCapacityWithInValidInput_shouldReturnError() {
		ParkingArea parkingArea = new ParkingArea(1, "parking area", 10, 10, "Istanbul", new PriceList(), new ArrayList<ParkingRecord>());
		
		Mockito.when(parkingAreaDao.findById(1)).thenReturn(Optional.of(parkingArea));

		assertThrows(IllegalArgumentException.class ,() -> vehiclecheckService.checkCapacity(parkingArea.getId()).isSuccess());
	}
	
	@Test
	void testCheckIfVehicleAlreadyParkingWithValidInput_shouldReturnSuccess() {
		ParkingArea parkingArea = new ParkingArea(1, "parking area", 10, 0, "Istanbul", new PriceList(), new ArrayList<ParkingRecord>());
		Vehicle vehicle = new Vehicle("vehicle", VehicleType.MINIVAN, 1.15, new ArrayList<ParkingRecord>());
		
		Mockito.when(vehicleDao.getByLicensePlate("vehicle")).thenReturn(vehicle);
		Mockito.when(parkingAreaDao.findById(1)).thenReturn(Optional.of(parkingArea));
		
		Result result = vehiclecheckService.checkIfVehicleAlreadyParking("vehicle");
		
		Assertions.assertEquals(new SuccessResult().isSuccess(), result.isSuccess());
	}
	
	@Test
	void testCheckIfVehicleAlreadyParkingWithInValidInput_shouldReturnError() {
		List<ParkingRecord> parkingRecords = new ArrayList<ParkingRecord>();
		Vehicle vehicle = new Vehicle("vehicle", VehicleType.MINIVAN, 1.15, new ArrayList<ParkingRecord>());
		parkingRecords.add(new ParkingRecord(LocalDateTime.now(), vehicle, new ParkingArea()));
		vehicle.setParkingRecords(parkingRecords);
		
		Mockito.when(vehicleDao.getByLicensePlate("vehicle")).thenReturn(vehicle);
		
		assertThrows(IllegalArgumentException.class ,() -> vehiclecheckService.checkIfVehicleAlreadyParking("vehicle").isSuccess());
	}
	
	@Test
	void testCheckAllCheckInConditionsWithValidInput_shouldReturnSuccess() {
		ParkingArea parkingArea = new ParkingArea(1, "parking area", 10, 0, "Istanbul", new PriceList(), new ArrayList<ParkingRecord>());
		Vehicle vehicle = new Vehicle("vehicle", VehicleType.MINIVAN, 1.15, new ArrayList<ParkingRecord>());
		
		Mockito.when(vehicleDao.getByLicensePlate("vehicle")).thenReturn(vehicle);
		Mockito.when(parkingAreaDao.findById(1)).thenReturn(Optional.of(parkingArea));
		
		Result result = vehiclecheckService.checkAllCheckInConditions("vehicle", 1);
		
		Assertions.assertEquals(new SuccessResult().isSuccess(), result.isSuccess());
	}
	
	@Test
	void testCheckAllCheckInConditionsWithInValidInput_shouldReturnError() {
		ParkingArea parkingArea = new ParkingArea(1, "parking area", 10, 10, "Istanbul", new PriceList(), new ArrayList<ParkingRecord>());
		Vehicle vehicle = new Vehicle("vehicle", VehicleType.MINIVAN, 1.15, new ArrayList<ParkingRecord>());
		
		Mockito.when(vehicleDao.getByLicensePlate("vehicle")).thenReturn(vehicle);
		Mockito.when(parkingAreaDao.findById(1)).thenReturn(Optional.of(parkingArea));
		
		assertThrows(IllegalArgumentException.class ,() -> vehiclecheckService.checkAllCheckInConditions("vehicle", 1).isSuccess());
	}
	

}

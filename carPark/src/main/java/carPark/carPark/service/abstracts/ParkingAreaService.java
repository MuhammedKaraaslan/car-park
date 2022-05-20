package carPark.carPark.service.abstracts;

import java.util.List;

import carPark.carPark.core.utilities.results.DataResult;
import carPark.carPark.core.utilities.results.Result;
import carPark.carPark.entities.ParkingArea;
import carPark.carPark.entities.dtos.ParkingAreaDto;

public interface ParkingAreaService {
		
	DataResult<ParkingAreaDto> getById(int id);
	Result checkName(ParkingAreaDto parkingArea);
	Result checkCapacity(ParkingAreaDto parkingArea);
	Result checkCity(ParkingAreaDto parkingArea);
	Result checkAll(ParkingAreaDto parkingArea);
	Result add(ParkingAreaDto parkingArea);
	Result delete(ParkingAreaDto parkingArea);
	Result update(ParkingAreaDto parkingArea);
	DataResult<ParkingArea> getByName(String name);
	DataResult<List<ParkingArea>> getAll();
	Result getDailyIncome(int parkingAreaId, int year, int month, int day);

}

package carPark.carPark.service.concretes;

import java.util.List;

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
import carPark.carPark.entities.ParkingArea;
import carPark.carPark.entities.ParkingRecord;
import carPark.carPark.entities.PricePeriod;
import carPark.carPark.entities.dtos.ParkingAreaDto;
import carPark.carPark.entities.dtos.PriceListDto;
import carPark.carPark.service.abstracts.ParkingAreaService;
import carPark.carPark.service.abstracts.PriceListService;



@Service
public class ParkingAreaManager implements ParkingAreaService {

	private ParkingAreaDao parkingAreaDao;
	private PriceListService priceListService;
	private ModelMapper modelMapper;

	@Autowired
	public ParkingAreaManager(ParkingAreaDao parkingAreaDao, PriceListService priceListService,
			ModelMapper modelMapper) {
		super();
		this.parkingAreaDao = parkingAreaDao;
		this.priceListService = priceListService;
		this.modelMapper = modelMapper;
	}

	// Check if id is valid or not 
	@Override
	public DataResult<ParkingAreaDto> getById(int id) {
		if (this.parkingAreaDao.findById(id).isEmpty()) {
			return new ErrorDataResult<>("Parking area could not found.");
		}
		ParkingAreaDto parkingAreaDto = modelMapper.map(this.parkingAreaDao.findById(id).orElse(null),
				ParkingAreaDto.class);
		return new SuccessDataResult<>(parkingAreaDto, "Parking area listed.");
	}

	// Check if the name is empty or null
	@Override
	public Result checkName(ParkingAreaDto parkingArea) {
		if (parkingArea.getName().equals("") || parkingArea.getName().isEmpty()) {
			throw new IllegalArgumentException("Name can not be empty.");
		}
		return new SuccessResult();
	}

	// Check if the capacity is negative or not
	@Override
	public Result checkCapacity(ParkingAreaDto parkingArea) {
		if (parkingArea.getCapacity() <= 0) {
			throw new IllegalArgumentException("Capacity can not be less or equal than 0.");
		}
		return new SuccessResult();
	}

	// Check if the city is negative or not
	@Override
	public Result checkCity(ParkingAreaDto parkingArea) {
		if (parkingArea.getCity().equals("") || parkingArea.getCity().isEmpty()) {
			throw new IllegalArgumentException("City can not be empty.");
		}
		return new SuccessResult();
	}

	// Check all the conditions above and check saving conditions for priceList
	@Override
	public Result checkAll(ParkingAreaDto parkingArea) {
		PriceListDto priceListDto = modelMapper.map(parkingArea.getPriceList(), PriceListDto.class);
		if (getById(parkingArea.getId()).isSuccess()) {
			throw new IllegalArgumentException("Parking area already exists.");
		} else if (!checkName(parkingArea).isSuccess()) {
			return new ErrorResult(checkName(parkingArea).getMessage());
		} else if (!checkCapacity(parkingArea).isSuccess()) {
			return new ErrorResult(checkCapacity(parkingArea).getMessage());
		} else if (!checkCity(parkingArea).isSuccess()) {
			return new ErrorResult(checkCity(parkingArea).getMessage());
		} else if (!this.priceListService.checkAll(priceListDto).isSuccess()) {
			return new ErrorResult(this.priceListService.checkAll(priceListDto).getMessage());
		}
		return new SuccessResult();
	}

	// Save parking area if checkAll method returns success
	@Override
	public Result add(ParkingAreaDto parkingAreaDto) {
		if (!checkAll(parkingAreaDto).isSuccess()) {
			return new ErrorResult(checkAll(parkingAreaDto).getMessage());
		}
		ParkingArea parkingArea = modelMapper.map(parkingAreaDto, ParkingArea.class);
		for (PricePeriod period : parkingArea.getPriceList().getPricePeriods()) {
			period.setPriceList(parkingArea.getPriceList());
		}
		this.parkingAreaDao.save(parkingArea);

		return new SuccessResult("Parking area added.");
	}

	// Delete parking area
	@Override
	public Result delete(ParkingAreaDto parkingAreaDto) {
		if (!getById(parkingAreaDto.getId()).isSuccess()) {
			return new ErrorResult(getById(parkingAreaDto.getId()).getMessage());
		}
		ParkingArea parkingArea = modelMapper.map(parkingAreaDto, ParkingArea.class);
		this.parkingAreaDao.delete(parkingArea);
		return new SuccessResult("Parking area deleted.");
	}

	// Update parking area and its pricelist
	@Override
	public Result update(ParkingAreaDto parkingAreaDto) {
		if (!getById(parkingAreaDto.getId()).isSuccess()) {
			return new ErrorResult(getById(parkingAreaDto.getId()).getMessage());
		}
		ParkingArea parkingArea = modelMapper.map(parkingAreaDto, ParkingArea.class);
		PriceListDto priceListDto = modelMapper.map(parkingArea.getPriceList(), PriceListDto.class);
		this.priceListService.add(priceListDto);
		this.parkingAreaDao.save(parkingArea);
		return new SuccessResult("Parking area updated.");
	}


	@Override
	public DataResult<ParkingArea> getByName(String name) {
		return new SuccessDataResult<>(this.parkingAreaDao.getByName(name), "Parking area listed.");
	}

	@Override
	public DataResult<List<ParkingArea>> getAll() {
		return new SuccessDataResult<>(this.parkingAreaDao.findAll(), "Parking areas listed.");
	}

	// Get daily income of a parking area 
	@Override
	public Result getDailyIncome(int parkingAreaId, int year, int month, int day) {
		if (!getById(parkingAreaId).isSuccess()) {
			throw new IllegalArgumentException("Parking area could not found");
		}
		ParkingAreaDto parkingAreaDto = this.getById(parkingAreaId).getData();
		ParkingArea parkingArea = modelMapper.map(parkingAreaDto, ParkingArea.class);
		List<ParkingRecord> parkingRecords = parkingArea.getParkingRecords();
		int parkingYear = 0;
		int parkingMonth = 0;
		int parkingDay = 0;
		double fee = 0;
		// Find the sum of fees if date conditions are met
		for (ParkingRecord parkingRecord : parkingRecords) {
			if (parkingRecord.getCheckOutDate() != null) {
				parkingYear = parkingRecord.getCheckOutDate().getYear();
				parkingMonth = parkingRecord.getCheckOutDate().getMonthValue();
				parkingDay = parkingRecord.getCheckOutDate().getDayOfMonth();
				if (year == parkingYear && month == parkingMonth && day == parkingDay) {
					fee += parkingRecord.getFee();
				}
			}
		}
		return new SuccessResult("Daily income in " + parkingYear + "/" + parkingMonth + "/" + parkingDay + "/" + " is " + fee);
	}

}

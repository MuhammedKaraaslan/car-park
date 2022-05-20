package carPark.carPark.service.concretes;


import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import carPark.carPark.core.utilities.results.ErrorResult;
import carPark.carPark.core.utilities.results.Result;
import carPark.carPark.core.utilities.results.SuccessResult;
import carPark.carPark.dataAccess.PricePeriodDao;
import carPark.carPark.entities.PricePeriod;
import carPark.carPark.entities.dtos.PricePeriodDto;
import carPark.carPark.service.abstracts.PricePeriodService;


@Service
public class PricePeriodManager implements PricePeriodService{
	
	private PricePeriodDao pricePeriodDao;
	private ModelMapper modelMapper;
	
	@Autowired
	public PricePeriodManager(PricePeriodDao pricePeriodDao, ModelMapper modelMapper) {
		super();
		this.pricePeriodDao = pricePeriodDao;
		this.modelMapper = modelMapper;
	}
	

	// Check if the start our is positive and less than the end our
	@Override
	public Result checkStartHour(PricePeriodDto pricePeriod) {
		if(pricePeriod.getStartHour() < 0){
			throw new IllegalArgumentException("Start hour can not be less than 0.");
		}
		else if(pricePeriod.getStartHour() >= pricePeriod.getEndHour()) {
			throw new IllegalArgumentException("Start hour can not be equal or greater than end hour.");
		}
		return new SuccessResult();
	}

	//Check if the end our is positive
	@Override
	public Result checkEndHour(PricePeriodDto pricePeriod) {
		if(pricePeriod.getEndHour() < 0){
			throw new IllegalArgumentException("End hour can not be less than 0.");
		}
		return new SuccessResult();
	}

	//Check if the price is negative
	@Override
	public Result checkPrice(PricePeriodDto pricePeriod) {
		if(pricePeriod.getPrice() < 0){
			throw new IllegalArgumentException("Price can not be less than 0.");
		}
		return new SuccessResult();
	}

	//Check all the conditions above
	@Override
	public Result checkAll(PricePeriodDto pricePeriod) {
		if(! checkStartHour(pricePeriod).isSuccess()) {
			return new ErrorResult(checkStartHour(pricePeriod).getMessage());
		}
		else if(! checkEndHour(pricePeriod).isSuccess()) {
			return new ErrorResult(checkEndHour(pricePeriod).getMessage());
		}
		else if(! checkPrice(pricePeriod).isSuccess()) {
			return new ErrorResult(checkPrice(pricePeriod).getMessage());
		}
		return new SuccessResult();
	}

	//Save price period if checkAll method returns success
	@Override
	public Result add(PricePeriodDto pricePeriodDto) {
		if(! checkAll(pricePeriodDto).isSuccess()) {
			return new ErrorResult(checkAll(pricePeriodDto).getMessage());
		}
		PricePeriod pricePeriod = modelMapper.map(pricePeriodDto, PricePeriod.class);
		this.pricePeriodDao.save(pricePeriod);
		return new SuccessResult("Price period added.");
	}

	//Delete price period
	@Override
	public Result delete(PricePeriodDto pricePeriod) {
		if(! this.pricePeriodDao.findById(pricePeriod.getId()).isPresent()) {
			throw new IllegalArgumentException("Price period could not found.");
		}
		this.pricePeriodDao.deleteById(pricePeriod.getId());
		return new SuccessResult("Price period deleted.");
	}

}

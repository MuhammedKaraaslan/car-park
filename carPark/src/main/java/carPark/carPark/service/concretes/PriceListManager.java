package carPark.carPark.service.concretes;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import carPark.carPark.core.utilities.results.DataResult;
import carPark.carPark.core.utilities.results.ErrorDataResult;
import carPark.carPark.core.utilities.results.ErrorResult;
import carPark.carPark.core.utilities.results.Result;
import carPark.carPark.core.utilities.results.SuccessDataResult;
import carPark.carPark.core.utilities.results.SuccessResult;
import carPark.carPark.dataAccess.PriceListDao;
import carPark.carPark.entities.PriceList;
import carPark.carPark.entities.PricePeriod;
import carPark.carPark.entities.dtos.PriceListDto;
import carPark.carPark.entities.dtos.PricePeriodDto;
import carPark.carPark.service.abstracts.PriceListService;
import carPark.carPark.service.abstracts.PricePeriodService;


@Service
public class PriceListManager implements PriceListService{
	
	private PriceListDao priceListDao;
	private PricePeriodService pricePeriodService;
	private ModelMapper modelMapper;
	
	@Autowired
	public PriceListManager(PriceListDao priceListDao, PricePeriodService pricePeriodService,ModelMapper modelMapper) {
		super();
		this.priceListDao = priceListDao;
		this.pricePeriodService = pricePeriodService;
		this.modelMapper = modelMapper;
	}
	
	// Check if id is valid or not 
	@Override
	public DataResult<PriceListDto> getById(int id) {
		if(this.priceListDao.findById(id).isEmpty()) {
			return new ErrorDataResult<>("Price list could not found.");
		}
		PriceListDto priceList = modelMapper.map(this.priceListDao.findById(id).orElse(null), PriceListDto.class);
		return new SuccessDataResult<>(priceList, "Price list listed.");
	}

	// Check if there is empty spaces between periods
	@Override
	public Result checkPeriodLimits(PriceListDto priceList) {
		if(priceList.getPricePeriods().size() != 1) {
			for (int i = 0; i < priceList.getPricePeriods().size()-1; i++) {
				if(priceList.getPricePeriods().get(i).getEndHour() != priceList.getPricePeriods().get(i+1).getStartHour()) {
					throw new IllegalArgumentException("There can not be empty time spaces between price periods.");
				}
			}
		}
		return new SuccessResult();
	}

	// Check if pricelist covers 24 hours
	@Override
	public Result check24Hours(PriceListDto priceList) {
		if(priceList.getPricePeriods().get(0).getStartHour() != 0 || priceList.getPricePeriods().get(priceList.getPricePeriods().size()-1).getEndHour() != 24) {
			throw new IllegalArgumentException("24 hour range should be completely defined."); 
		}
		return new SuccessResult();
	}

	// Check if all conditions of period are met
	@Override
	public Result checkPeriodConditions(PriceListDto priceList) {
		for (PricePeriod period : priceList.getPricePeriods()) {
			PricePeriodDto periodDto = modelMapper.map(period, PricePeriodDto.class);
			if(! this.pricePeriodService.checkAll(periodDto).isSuccess()) {
				return new ErrorResult(this.pricePeriodService.checkAll(periodDto).getMessage());
			}
		}
		return new SuccessResult();
	}

	// Check all the conditions above
	@Override
	public Result checkAll(PriceListDto priceList) {
		if(! checkPeriodLimits(priceList).isSuccess()) {
			return new ErrorResult(checkPeriodLimits(priceList).getMessage());
		}
		else if(! check24Hours(priceList).isSuccess()) {
			return new ErrorResult(check24Hours(priceList).getMessage());
		}
		else if(! checkPeriodConditions(priceList).isSuccess()) {
			return new ErrorResult(checkPeriodConditions(priceList).getMessage());
		}
		return new SuccessResult();
	}

	// Save price list if checkAll method returns success
	@Override
	public Result add(PriceListDto priceListDto) {
		PriceList priceList = modelMapper.map(priceListDto, PriceList.class);
		if(! checkAll(priceListDto).isSuccess()) {
			return new ErrorResult(checkAll(priceListDto).getMessage());
		}
		for (int i = 0; i < priceList.getPricePeriods().size(); i++) {
			priceList.getPricePeriods().get(i).setPriceList(priceList);
		}
		this.priceListDao.save(priceList);
		return new SuccessResult("Price list added.");
	}

	// Delete price list
	@Override
	public Result delete(PriceListDto priceListDto) {
		if(! getById(priceListDto.getId()).isSuccess()) {
			throw new IllegalArgumentException("Price list could not found");
		}
		PriceList priceList = modelMapper.map(priceListDto, PriceList.class);
		this.priceListDao.delete(priceList);
		return new SuccessResult("Price list deleted.");
	}
	
	
	// Get all price list items from data base
	@Override
	public DataResult<List<PriceListDto>> getAll() {
		List<PriceList> lists = this.priceListDao.findAll();
		List<PriceListDto> priceListDtos = lists.stream().map(list -> modelMapper.map(list, PriceListDto.class)).collect(Collectors.toList());
		return new SuccessDataResult<>(priceListDtos, "Price lists listed.");
	}

	// Update price list
	@Override
	public Result update(PriceListDto priceListDto) {
		PriceList priceList = modelMapper.map(priceListDto, PriceList.class);
		this.priceListDao.save(priceList);
		return new SuccessResult("Pricelist updated.");
	}

	

}

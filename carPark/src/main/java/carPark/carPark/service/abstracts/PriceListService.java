package carPark.carPark.service.abstracts;

import java.util.List;

import carPark.carPark.core.utilities.results.DataResult;
import carPark.carPark.core.utilities.results.Result;
import carPark.carPark.entities.dtos.PriceListDto;



public interface PriceListService {
	
	DataResult<PriceListDto> getById(int id);
	Result checkPeriodLimits(PriceListDto priceList);
	Result check24Hours(PriceListDto priceList);
	Result checkPeriodConditions(PriceListDto priceList);
	Result checkAll(PriceListDto priceList);
	Result add(PriceListDto priceList);
	Result delete(PriceListDto priceList);
	Result update(PriceListDto priceList);
	DataResult<List<PriceListDto>> getAll();
}

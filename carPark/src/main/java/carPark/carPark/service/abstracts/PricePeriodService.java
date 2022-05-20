package carPark.carPark.service.abstracts;

import carPark.carPark.core.utilities.results.Result;
import carPark.carPark.entities.dtos.PricePeriodDto;

public interface PricePeriodService {

	Result checkStartHour(PricePeriodDto pricePeriod);
	Result checkEndHour(PricePeriodDto pricePeriod);
	Result checkPrice(PricePeriodDto pricePeriod);
	Result checkAll(PricePeriodDto pricePeriod);
	Result add(PricePeriodDto pricePeriod);
	Result delete(PricePeriodDto pricePeriod);
}

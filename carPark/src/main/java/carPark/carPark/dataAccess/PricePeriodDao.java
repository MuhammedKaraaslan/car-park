package carPark.carPark.dataAccess;

import org.springframework.data.jpa.repository.JpaRepository;

import carPark.carPark.entities.PricePeriod;


public interface PricePeriodDao extends JpaRepository<PricePeriod, Integer>{

}

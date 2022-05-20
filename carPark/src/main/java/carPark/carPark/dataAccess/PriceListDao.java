package carPark.carPark.dataAccess;

import org.springframework.data.jpa.repository.JpaRepository;

import carPark.carPark.entities.PriceList;

public interface PriceListDao extends JpaRepository<PriceList, Integer>{

}

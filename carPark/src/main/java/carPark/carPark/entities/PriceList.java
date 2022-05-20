package carPark.carPark.entities;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "price_list")
@Data
@AllArgsConstructor
@NoArgsConstructor

public class PriceList {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "price_list_id")
	private int id;
	
	@JsonIgnore
	@OneToMany(mappedBy = "priceList", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<ParkingArea> parkingAreas;

	@OneToMany(mappedBy = "priceList", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<PricePeriod> pricePeriods;
}


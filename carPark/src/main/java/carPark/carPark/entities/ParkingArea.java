package carPark.carPark.entities;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "parking_area")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ParkingArea {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "parking_area_id")
	private int id;
	
	@Column(name = "name")
	private String name;
	
	@Column(name = "capacity")
	private int capacity;
	
	@Column(name = "current_vehicle_number")
	private int currentVehicleNumber;
	
	@Column(name = "city")
	private String city;
	
	@ManyToOne
	@Cascade(CascadeType.ALL)
	@JoinColumn(name = "price_list_id")
	private PriceList priceList;
	
	@JsonIgnore
	@OneToMany(mappedBy = "parkingArea") 
	private List<ParkingRecord> parkingRecords;
	
}

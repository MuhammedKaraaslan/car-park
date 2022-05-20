package carPark.carPark.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "price_period")
@Data
@AllArgsConstructor
@NoArgsConstructor

public class PricePeriod {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "price_period_id")
	private int id;
	
	@Column(name = "start_hour")
	private int startHour;
	
	@Column(name = "end_hour")
	private int endHour;
	
	@Column(name = "price")
	private int price;
	
	@JsonIgnore
	@ManyToOne
	@Cascade(CascadeType.ALL)
	@JoinColumn(name = "price_list_id")
	private PriceList priceList;

} 

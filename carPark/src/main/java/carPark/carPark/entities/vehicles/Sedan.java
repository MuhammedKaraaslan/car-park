package carPark.carPark.entities.vehicles;

import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.NoArgsConstructor;

@Entity
@Table(name = "vehicle")
@NoArgsConstructor
public class Sedan extends Vehicle{

}

import scala.actors.Actor
import scala.actors.Actor._
import scala.collection.mutable.PriorityQueue

object ElevatorController extends Actor {
	// We want the currentDirection queue initialized so lower numbers have higher priority (we start on floor 1).
	var currentDirection = new PriorityQueue[Int]().reverse
	var oppositeDirection = new PriorityQueue[Int]()
	var futureDirection = new PriorityQueue[Int]().reverse

	def processRequest(direction:String,floor:Int) {
		if(direction == "up") {
			if(Elevator.direction == "up" && Elevator.location < floor) {
				currentDirection.enqueue(floor)
			} else if(Elevator.direction == "down") {
				oppositeDirection.enqueue(floor)
			} else {
				futureDirection.enqueue(floor)
			}
		}			
		if (direction == "down") {
			if (Elevator.direction == "down" && Elevator.location > floor) {
				currentDirection.enqueue(floor)
			} else if(Elevator.direction == "up") {
				oppositeDirection.enqueue(floor)
			} else {
				futureDirection.enqueue(floor)
			}			
		}
	}
	
	def moveElevator() {
		// should switch the queues
		if((Elevator.direction == "up" && Elevator.location == 3) ||
		(Elevator.direction == "down" && Elevator.location == 1)) {
			// We each queue "moves up" and gets reversed.  The futureDirection queue gets reversed AND cleared.
			currentDirection = oppositeDirection.reverse
			oppositeDirection = futureDirection.reverse
			futureDirection = futureDirection.reverse
			futureDirection.clear
    }
  }

	def act() {
		while(true) {
			receive {
				case (direction: String, floor: Int) => {
					/*
					if statement to make sure that no requests are added
					unless Elevator is functioning normally
					*/
					if(!Elevator.maintenance == true && !Elevator.alarm == true) { 
            processRequest(direction, floor)
					}
				}
				case "stopped" => {
            Elevator ! "stopped"	
				}
				case "Maintenance On" => {
        /*
        Also wasn't sure whether ElevatorController or Elevator should open
        the doors to let maintenance in.
        */
					if(Elevator.location != 1) {
					  Motor.down()
					}        
					Elevator ! "Maintenance On"
				}
				case "Maintenance Off" => {
					if(Elevator.location != 1) {
					  Motor.down()
					} else {
						Elevator ! "Maintenance Off" 
					}
				}
				case "Alarm On" => {
					currentDirection.clear
					oppositeDirection.clear
					futureDirection.clear
					Elevator ! "Alarm On"
				}
				case "Alarm Off" => {
					if(Elevator.location != 1) {
					  Motor.down()
					} else {
						/*
						  Can change, assumed that Elevator would have to be sent Alarm and Maintenance
						  information so it can open/close doors and turn on/off elevator lights
						*/
						Elevator ! "Alarm Off" 
					}
				}
			}
		}
	}
}
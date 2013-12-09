import scala.actors.Actor
import scala.actors.Actor._
import scala.collection.mutable.PriorityQueue

object ElevatorController extends Actor {
	// We want the currentDirection queue initialized so lower numbers have higher priority (we start on floor 1).
	var currentDirection = new PriorityQueue[Int]().reverse
	var oppositeDirection = new PriorityQueue[Int]()
	var futureDirection = new PriorityQueue[Int]().reverse
	var maintenance = false
	var alarm = false

	def processRequest(direction:String,floor:Int) {
		if(currentDirection.size == 0 && oppositeDirection.size == 0 
		   && futureDirection.size != 0) {
		     currentDirection = futureDirection
		     futureDirection.clear()	
		}
		if(currentDirection.size == 0 && oppositeDirection.size == 0 
		  && futureDirection.size == 0) {
         	if(Elevator.direction != Elevator.directionToFloor(floor)) {
         		changeQueues()
         		currentDirection.enqueue(floor)
         	} else currentDirection.enqueue(floor)
		} 
		if(direction == "Up") {
			if(Elevator.direction == "Up" && Elevator.location <= floor && (currentDirection.count(x => x == floor) != 0)) {
				currentDirection.enqueue(floor)
			} else if(Elevator.direction == "Down" && oppositeDirection.count(x => x == floor) != 0) {
				oppositeDirection.enqueue(floor)
			} else {
        if(futureDirection.count(x => x == floor) != 0) {				
				  futureDirection.enqueue(floor)
        }
			}
		}			
		if (direction == "Down") {
			if (Elevator.direction == "Down" && Elevator.location >= floor && (currentDirection.count(x => x == floor) != 0)) {
				currentDirection.enqueue(floor)
			} else if(Elevator.direction == "Up" && oppositeDirection.count(x => x == floor) != 0) {
				oppositeDirection.enqueue(floor)
			} else {
				if (futureDirection.count(x => x == floor) != 0) {	
				  futureDirection.enqueue(floor)
				}
			}			
		}
	}
	def changeQueues() {
	     // We need each queue "moves up" and gets reversed.  The futureDirection queue gets reversed AND cleared.
 	    currentDirection = oppositeDirection.reverse		
 	    oppositeDirection = futureDirection.reverse
			futureDirection = futureDirection.reverse
			futureDirection.clear
	}

	def act() {
		println("ElevatorController now acting!")
		while(true) {
			receive {
				case (direction: String, floor: Int) => {
					/*
					if statement to make sure that no requests are added
					unless Elevator is functioning normally
					*/
					if(!maintenance == true && !alarm == true) { 
            processRequest(direction, floor)
            println("Current direction queue = " + currentDirection.toString)
						println("Opposite direction queue = " + oppositeDirection.toString)
						println("Future direction queue = " + futureDirection.toString)
						println(Elevator.direction)
					}
				}
				case "Elevator Ready" => {
		      if((Elevator.direction == "Up" && Elevator.location == 3) ||
		        (Elevator.direction == "Down" && Elevator.location == 1)) {		
		   	     changeQueues()      
		      }
		       if (currentDirection.size != 0) {
					    Elevator ! currentDirection.dequeue                								
           }
				}
				case "stopped" => {
            Elevator ! "stop"	
				}
				case "Maintenance On" => {
					  futureDirection.enqueue(1)
					  maintenance = true					    
				}
				case "Maintenance Off" => {
					  maintenance = false
					  currentDirection.enqueue(1)
				}
				case "Alarm On" => {
					currentDirection.clear
					oppositeDirection.clear
					futureDirection.clear
					processRequest(Elevator.direction,Elevator.nearestFloor())
					alarm = true
				}
				case "Alarm Off" => {
					alarm = false
					if(Elevator.direction == "Up" && Elevator.location > 1) {
					  changeQueues()
					  currentDirection.enqueue(1)
					}
					if (Elevator.direction == "Down") {
						currentDirection.enqueue(1)
					}
				} 
			}
		}
	}
}
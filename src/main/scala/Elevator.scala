import scala.actors.TIMEOUT
import scala.actors.Actor
import scala.actors.Actor._

object Elevator extends Actor {
	var status = "active"
	var location = 1
	var destination = 1
	var direction = "Up"

	// Eases the control of doors.  Takes an action "open" or "close", and a floor. 
	// Performs that action on that floor's door.
	def controlDoor(action:String, floor:Int) {
		var open = false
		if (action == "open") open = true
		floor match {
			case 1 => SystemStatus.door1Open = open
			case 2 => SystemStatus.door2Open = open
			case 3 => SystemStatus.door3Open = open
		}
	}

	// Eases control of lights.  Takes an action "on" or "off", and a /ight identifier,
	// e.x. "elevator3" or "floor3Down". See SystemStatus.scala for all light names.
	def controlLight(action:String, light:String) {
		var on = false
		if (action == "on") on = true
		light match {
			case "floor3Down" => SystemStatus.floor3DownButtonLit = on
			case "floor3Up" => SystemStatus.floor3DownButtonLit = on
			case "floor2Up" => SystemStatus.floor2UpButtonLit = on
			case "floor2Down" => SystemStatus.floor2DownButtonLit = on
			case "floor1Up" => SystemStatus.floor1UpButtonLit = on
			case "floor1Down" => SystemStatus.floor1UpButtonLit = on
			case "elevator1" => SystemStatus.elevator1ButtonLit = on
			case "elevator2" => SystemStatus.elevator2ButtonLit = on
			case "elevator3" => SystemStatus.elevator3ButtonLit = on
			case "elevatorStop" => SystemStatus.elevatorStopButtonLit = on
			case "UpArrow" => SystemStatus.UpArrowOn = on
			case "DownArrow" => SystemStatus.DownArrowOn = on
		}
	}

	def ftForFloor(x:Int):Int = {
		x match {
			case 1 => 36
			case 2 => 20
			case 3 => 2
			case _ => 0
		}
	}

	def directionToFloor(floor:Int) {
		var delta = floor - location
		if(delta>0){
			"Up"
		} else if(delta<0) {
			"Down"
		} else "None"
	}

	def ftToFloor(floor:Int):Int = {
		var delta = ftForFloor(floor) - Motor.lineOut
		delta.abs
	}

	def nearestFloor():Int = {
		var distance = 100
		var floor = 0
		for (i <- List(1,2,3)) {
			if (ftToFloor(i) < distance) {
				distance = ftToFloor(i)
				floor = i
			}
		}
		floor
	}

	def currentFloor():Int = {
		val ft = Motor.lineOut()
		if(ft == 36) 1 else if(ft == 20) 2 else if(ft == 2) 3 else 0
	}

	def isStopped() {
		SystemStatus.elevatorStopButtonLit
	}

	def act {
		println("Elevator now acting!")
		while (true) {
			if (status != "stopped") {
				//Update location
				location = currentFloor()
				// Check if at destination
				if (location == destination) {
					Motor.stop
					controlDoor("open", location)
					controlLight("off", "elevator" + location.toString)
					controlLight("off", "floor" + location.toString + direction)
					ElevatorController ! "Elevator Ready"
				} else {
					if(direction == "Up") Motor.up else if(direction == "Down") Motor.down
				}
				if(location == 3) direction = "Down"
				else if(location == 1) direction = "Up"
			} else Motor.stop
			receiveWithin(100) {
				case TIMEOUT => status = status
				case "stop" => {
					if (status == "stopped") status = "active" else status = "stopped"
				}
				case floor:Int => {
					destination = floor
					controlDoor("close", location)
					controlLight("on", "elevator" + destination.toString)
					var delta = destination - location
					if(delta>0){
						direction = "Up"
					} else if(delta<0) {
						direction = "Down"
					}
				}
			}
		}
	}
}
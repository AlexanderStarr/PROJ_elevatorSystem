import scala.actors.Actor
import scala.actors.Actor._

object Elevator extends Actor {
	var direction = "stopped"
	var previousDirection = "stopped"
	var location = 1
	var alarm = false
	var maintenance = false

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
			case "floor2Up" => SystemStatus.floor2UpButtonLit = on
			case "floor2Down" => SystemStatus.floor2DownButtonLit = on
			case "floor1Up" => SystemStatus.floor1UpButtonLit = on
			case "elevator1" => SystemStatus.elevator1ButtonLit = on
			case "elevator2" => SystemStatus.elevator2ButtonLit = on
			case "elevator3" => SystemStatus.elevator3ButtonLit = on
			case "elevatorStop" => SystemStatus.elevatorStopButtonLit = on
			case "UpArrow" => SystemStatus.UpArrowOn = on
			case "DownArrow" => SystemStatus.DownArrowOn = on
		}
	}

	def controlMotor(action:String) {
		
	}

	def act {
		while (true) {
			receive {
				case "stop" => controlMotor("stop")
			}
		}
	}
}
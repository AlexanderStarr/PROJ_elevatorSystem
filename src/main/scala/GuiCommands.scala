import scala.actors.Actor._

object guiOutput {

	def Floor1Up()
	{
		//Place your code here for when the Up button is pressed on floor 1.
		println("Floor 1 Up Button Pressed")
		ElevatorController ! ("Up",1)		
	}
	
	def Floor2Up()
	{
		//Place your code here for when the Up button is pressed on floor 2.
		println("Floor 2 Up Button Pressed")
		ElevatorController ! ("Up",2)
	}

	def Floor2Down()
	{
		//Place your code here for when the Down button is pressed on floor2.
		println("Floor 2 Down Button Pressed")
		ElevatorController ! ("Down",2)		
	}

	def Floor3Down()
	{
		//Place your code here for when the Down button is pressed on floor3.
		println("Floor 3 Down Button Pressed")
		ElevatorController ! ("Down",3)
	}

	def elevFloor1()
	{
		//Place your code here for when the 1 button is pressed in the elevator.
		println("Elevator Button 1 Pressed")
		ElevatorController ! ("Down",1)
	}

	def elevFloor2()
	{
		//Place your code here for when the 2 button is pressed in the elevator
		println("Elevator Button 2 Pressed")
		if(Elevator.location == 3) {
		  ElevatorController ! ("Down",2)
		} else { 
			ElevatorController ! ("Up",2)
		}
	}

	def elevFloor3()
	{
		//Place your code here for when the 3 button is pressed in the elevator
		println("Elevator Button 3 Pressed")
		ElevatorController ! ("Up",3)
	}

	def elevStop()
	{
		//Place your code here for when the stop button is pressed in the elevator
		println("Elevator Stop Button Pressed")
		ElevatorController ! "stopped"
	}

	def MaintenanceModeOn()
	{
		//Place your code here for when the maintanence mode is switched to on.
		println("Maintenance Mode On")
		ElevatorController ! "Maintenance On"	
		
	}
	def MaintenanceModeOff()
	{
		//Place your code here for when the maintanence mode is switched to off.
		println("Maintenance Mode Off")
		ElevatorController ! "Maintenance Off"		
	}

	def AlarmModeOn()
	{
		//Place your code here for when the alarm mode is switched to on.
		println("Alarm On")
		ElevatorController ! "Alarm On"
	}

	def AlarmModeOff()
	{
		//Place your code here for when the alarm mode is switched to off.
		println("Alarm Off")
		ElevatorController ! "Alarm Off"
	}
}
import scala.actors.Actor
import scala.actors.Actor._
import guiGlobals._
class messenger() extends Actor
{
  var elevator1Button = false;
  var elevator2Button = false;
  var elevator3Button = false;
  var elevatorStopButton = false;
  var floor1UpButton = false;
  var floor2UpButton = false;
  var floor2DownButton = false;
  var floor3DownButton = false;
  var maintenanceModeOn = false;
  var maintenanceModeOff = false;
  var alarmModeOn = false;
  var alarmModeOff = false;

  def act()
  {
  		while(true)
  		{

  			if(elevator1Button)
  			{
  			 guiOutput.elevFloor1
  			 elevator1Button = false
  			}
  			if(elevator2Button)
  			{
  			 guiOutput.elevFloor2
  			 elevator2Button = false
  			}
  			if(elevator3Button)
  			{
  			 guiOutput.elevFloor3
  			 elevator3Button = false
  			}
  			if(elevatorStopButton)
  			{
  			 guiOutput.elevStop
  			 elevatorStopButton = false
  			}
  			if(floor1UpButton)
  			{
  				guiOutput.Floor1Up
  				floor1UpButton = false
  			}
  				if(floor2UpButton)
  			{
  				guiOutput.Floor2Up
  				floor2UpButton = false
  			}
  			if(floor2DownButton)
  			{
  				guiOutput.Floor2Down
  				floor2DownButton = false
  			}
  			if(floor3DownButton)
  			{
  				guiOutput.Floor3Down
  				floor3DownButton = false
  			}
  		 if(maintenanceModeOn)
  		 {
  		  	guiOutput.MaintenanceModeOn
  		  	maintenanceModeOn = false;
  		 }
  		 if(maintenanceModeOff)
  		 {
  		  	guiOutput.MaintenanceModeOff
  		   maintenanceModeOff = false;
  		 }
  		 if(alarmModeOn)
  		 {
  		  	guiOutput.AlarmModeOn
  		  	alarmModeOn = false;
  		 }
  		 if(alarmModeOff)
  		 {
  		  	guiOutput.AlarmModeOff
  		   alarmModeOff = false;
  		 }
  		 Thread.sleep(50);
  		}
  }
  
}
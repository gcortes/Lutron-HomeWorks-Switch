# Lutron-HomeWorks-Switch
This is a SmartThings Device Handler that will work with any server on a LAN local to it that supports its protocol. There is a [campanion project](https://github.com/gcortes/Lutronic) that provides a compatible web service.

This handler and it's companion service onluy support older Lutron Homeworks controllers. It does not support the new Homeworks QS series as it has a different interface. Funtionaly is also limitied to simulating the press of a key pad button and testing it's corresponding led indicator to determine if the funtion it supports is active or inactive. Since buttons can be programmed to do many different things, this isn't much of a limitation.

You will have to install this device handler and create devices for each button you wish to control. You will also have to install the web service unless you wish to write your own. The service is written in Ruby and may require an upgrade of the Ruby interupter. In addition Ruby gems mush be installed. The process is usually straight forward assuming you are comfortable using console access. Additionally, the web service has only been tested with OS X. It most likely will run fine under Linux. Windows may be more problematic.

## Installation

### Create the Device Handler

1. Sign in to your [SmartThings API](graph.api.smartthings.com)
2. Select My Device Handlers.
3. Click the green Create New Device Handler button at the right.
4. Click the From Code tab.
5. Copy the entire contents of ? from this reposorty into the code window and click the Create button.

### Create the Device

1. Sign in to your [SmartThings API](graph.api.smartthings.com) if you are not already logged in
2. Select the My Devices tab.
3. Clich the green New Device button at the right
4. Give your switch a name (e.g., Living Room Lights)
5. Enter the network id of your Lutron controller. This field is not useds by the app so doesn't have to be accurate.
6. On the Type list select Lutron Homeworks Swith, which should be at the bottom.
7. Enter optional fields as you see fit
8. Click create

### Set Up the Device

1. Open the SmartThings app on your phone.
2. Select the Things tab.
3. Find the Device you just created by the name you gave it above.
4. Click on the three dots in the upper right hand corner of the screen and select Edit Device.
5. Enter the IP address of the device running the web service.
6. Enter a free port number for this device. This must be unique for the server
7. Leave the Switch Type as K. This is for future expansion
8. Enter the Switch Address. See the Lutronic readme on how to obtain this
9. Enter the Button number. See the Lutronic readme on how to obtain this.
10. Click done.
 
### Set Up the Web Service

See the [Lutronic repository](https://github.com/gcortes/Lutronic) for instructions

### Testing

To test, click the Refresh button while watching the console screen you should see a message saying switch:on or switch:off

## Usage

Use this like any other switch in SmartThings.

## Contributing

1. Fork it!
2. Create your feature branch: `git checkout -b my-new-feature`
3. Commit your changes: `git commit -am 'Add some feature'`
4. Push to the branch: `git push origin my-new-feature`
5. Submit a pull request :D

## History

Version 0.1 - Beta release

## Credits

William (Curt) Rowe

## License

TODO: Write license

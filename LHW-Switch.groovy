/**
 *  Lutron HomeWorks Switch
 *
 *	This driver will support older HomeWorks controllers when used in conjunction with the
 *	Lutronic Web Service that I've also written. It does not work with HomeWorksQS as it has a
 *	different interface.
 *
 *  Copyright 2016 William Curtis Rowe
 *
 *  Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License. You may obtain a copy of the License at:
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software distributed under the License is distributed
 *  on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License
 *  for the specific language governing permissions and limitations under the License.
 *
 */
metadata {
	definition (name: "Lutron HomeWorks Switch", namespace: "gcortes", author: "William Curtis Rowe") {
    	capability "Refresh"
		capability "Switch"
        /* SmartThings suggests adding the followiing */
        capability "Sensor"
        capability "Actuator"
	}

	simulator {
		// TODO: define status and reply messages here
	}

    preferences {
        input("DeviceLocalLan", "string", title:"Server IP Address",
        	description:"Your server's I.P. address", defaultValue:"" ,
        	required: false, displayDuringSetup: true)
        input("DevicePort", "string", title:"Server Port",
    	    description:"The port that the server listens on", defaultValue:"1035",
    	    required: false, displayDuringSetup: true)
        input("SwitchType", "string", title:"Switch Type",
        	description:"The type of Lutron switch. K=keypad, D=dimmer", defaultValue:"K",
            required: true, displayDuringSetup: true)
        input("SwitchAddress", "string", title:"Switch Address",
        	description:"The switch address from the Lutron config", defaultValue:"01:01:01",
            required: true, displayDuringSetup: true)
        input("ButtonNumber", "string", title:"Button",
        	description:"The button number on the keypad", defaultValue:"1",
            required: true, displayDuringSetup: true)
    }

	tiles {
        standardTile("switch", "device.switch", width: 2, height: 2) {
            state "off", label:'off', action:'switch.on', icon:"st.switches.switch.off",
            	backgroundColor:"#ffffff"
        	state "on", label:'on', action:'switch.off', icon:"st.switches.switch.on",
            	backgroundColor:"#79b821"
        }
        // This came with the template and hasn't been implemented
        /*
		standardTile("indicator", "device.indicatorStatus", width: 1, height: 1, inactiveLabel: false, decoration: "flat") {
			state "when off", action:"indicator.indicatorWhenOn", icon:"st.indicators.lit-when-off"
			state "when on", action:"indicator.indicatorNever", icon:"st.indicators.lit-when-on"
			state "never", action:"indicator.indicatorWhenOff", icon:"st.indicators.never-lit"
		}*/
		standardTile("refresh", "device.switch", width: 1, height: 1, inactiveLabel: false, decoration: "flat") {
			state "default", label:'', action:"refresh.refresh", icon:"st.secondary.refresh"
		}

        main (["switch"]);
		details(["switch","refresh","indicator"])
    }
}

String getVersion() {return "1.0";}

// parse events into attributes
def parse(String description) {
	log.debug "Parsing '${description}'"
	// TODO: handle 'switch' attribute
    def msg = parseLanMessage(description);
	def headersAsString = msg.header // => headers as a string
    def headerMap = msg.headers      // => headers as a Map
    def body = msg.body              // => request body as a string
    def status = msg.status          // => http status code of the response

	log.debug "LHW - as LAN: " + msg;
    log.debug "LHW - message Body: " + body;
    if (body == "switch:on") {
		log.debug "Parse: switch on received. Sending event";
    	sendEvent(name: "switch", value: "on")
    }
    if (body == "switch:off") {
		log.debug "Parse: switch off received. Sending event";
    	sendEvent(name: "switch", value: "off")
    }

}

// handle commands
def on() {
	log.debug "LHW - executing 'on'"
    def command="?action=on&devicetype="+SwitchType+"&address="+SwitchAddress+"&button="+ButtonNumber
    sendCommand(command)
}

def off() {
	log.debug "LHW - executing 'off'"
    def command="?action=off&devicetype="+SwitchType+"&address="+SwitchAddress+"&button="+ButtonNumber
    sendCommand(command)
}

def refresh() {
	log.debug "LHW - executing 'refresh'"
    def command="?action=status&devicetype="+SwitchType+"&address="+SwitchAddress+"&button="+ButtonNumber
    sendCommand(command)
}

//********************************************************************************
private sendCommand(queryString) {
    log.info "YY - sending command "+ queryString+" to "+DeviceLocalLan+":"+DevicePort
    if (DeviceLocalLan?.trim()) {
        def hosthex = convertIPtoHex(DeviceLocalLan)
        def porthex = convertPortToHex(DevicePort)
        device.deviceNetworkId = "$hosthex:$porthex"

        def headers = [:] 
        headers.put("HOST", "$DeviceLocalLan:$DevicePort")

        def method = "GET"

        def hubAction = new physicalgraph.device.HubAction(
            method: method,
            path: "/"+queryString,
            headers: headers
            );

		log.debug hubAction
        hubAction;
    }
}
//********************************************************************************
private String convertIPtoHex(ipAddress) { 
    String hex = ipAddress.tokenize( '.' ).collect {  String.format( '%02X', it.toInteger() ) }.join()
    log.debug "IP address entered is $ipAddress and the converted hex code is $hex"
    return hex
}

private String convertPortToHex(port) {
    String hexport = port.toString().format( '%04X', port.toInteger() )
    log.debug hexport
    return hexport
}

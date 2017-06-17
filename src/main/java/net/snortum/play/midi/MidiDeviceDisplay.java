package net.snortum.play.midi;

import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Sequencer;
import javax.sound.midi.Synthesizer;

/**
 * Use this program to discover MIDI devices out your system.
 * 
 * @author Knute Snortum
 * @version 2017/06/16
 */
public class MidiDeviceDisplay {

	public static void main(String[] args) {
		new MidiDeviceDisplay().run();
	}

	private void run() {
		MidiDevice.Info[] devices = MidiSystem.getMidiDeviceInfo();
		if (devices.length == 0) {
		    System.out.println("No MIDI devices found");
		    return;
		} 

		for (MidiDevice.Info info : devices) {
			System.out.println("**********************");
			System.out.println("Device name: " + info.getName());
		    System.out.println("Description: " + info.getDescription());
		    System.out.println("Vendor: " + info.getVendor());
		    System.out.println("Version: " + info.getVersion());
		    
	        try {
	        	MidiDevice device = MidiSystem.getMidiDevice(info);
	        	printDeviceType(device);
	        	
				System.out.println("Maximum receivers: " + maxToString(device.getMaxReceivers()));
				System.out.println("Maximum transmitters: " + maxToString(device.getMaxTransmitters()));
			} catch (MidiUnavailableException e) {
				System.out.println("Can't get MIDI device");
				e.printStackTrace();
			}
		}
	}

	private void printDeviceType(MidiDevice device) {
		if (device instanceof Sequencer) {
    		System.out.println("This is a sequencer");
    	} else if (device instanceof Synthesizer) {
    		System.out.println("This is a synthesizer");
    	} else {
    		System.out.print("This is a MIDI port ");
    		if (device.getMaxReceivers() != 0) {
    			System.out.println("IN ");
    		} else if (device.getMaxTransmitters() != 0) {
    			System.out.println("OUT ");
    		} else {
    			System.out.println();
    		}
    	}
	}

	private String maxToString(int max) {
		return max == -1 ? "Unlimited" : String.valueOf(max);
	}
}

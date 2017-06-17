package net.snortum.play.midi;

import javax.sound.midi.MidiMessage;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Receiver;
import javax.sound.midi.Synthesizer;
import javax.sound.midi.Transmitter;

/**
 * Create a connection between a musical keyboard (transmitter) and an internal
 * synthesizer.  You should first run {@link MidiDeviceDisplay} to discover the 
 * device names for each of these.
 * 
 * @author Knute Snortum
 * @version 2017/06/16
 */
public class KeyboardToSynth {
	
	// Get these names from the MidiDeviceDisplay program, or leave empty for default
	private static final String TRANS_DEV_NAME = ""; // "USB Uno MIDI Interface";
	private static final String SYNTH_DEV_NAME = ""; // "Microsoft MIDI Mapper"; 

	// See https://docs.oracle.com/javase/8/docs/api/javax/sound/midi/MidiSystem.html 
	private static final String TRANS_PROP_KEY = "javax.sound.midi.Transmitter";
	private static final String SYNTH_PROP_KEY = "javax.sound.midi.Synthesizer";
	
	public static void main(String[] args) {
		new KeyboardToSynth().run();
	}

	private void run() {
		
		// Get a transmitter and synthesizer from their device names
		// using system properties or defaults
		Transmitter trans = getTransmitter();
		Synthesizer synth = getSynthesizer();
		
		if (trans == null || synth == null) {
			return;
		}
		
		// The synthesizer is your MIDI device, which needs to be opened
		if (! synth.isOpen()) {
			try {
				synth.open();
			} catch (MidiUnavailableException e) {
				System.err.println("Error opening synthesizer");
				e.printStackTrace();
				return;
			}
		}
		
		// You get your receiver from the synthesizer, then set it in
		// your transmitter.
		try {
			Receiver receiver = synth.getReceiver();
			DisplayReceiver displayReceiver = new DisplayReceiver(receiver);
			trans.setReceiver(displayReceiver); // or just "receiver"
			
			// You should be able to play on your musical keyboard (transmitter)
			// and hear sounds through your PC synthesizer (receiver)
			System.out.println("Play on your musical keyboard...");
		} catch (MidiUnavailableException e) {
			System.err.println("Error getting receiver from synthesizer");
			e.printStackTrace();
		}
	}

	/**
	 * @return a specific synthesizer object by setting the system property, otherwise the default
	 */
	private Synthesizer getSynthesizer() {
		if (! SYNTH_DEV_NAME.isEmpty() || ! "default".equalsIgnoreCase(SYNTH_DEV_NAME)) {
			System.setProperty(SYNTH_PROP_KEY, SYNTH_DEV_NAME);
		}
		
		try {
			return MidiSystem.getSynthesizer();
		} catch (MidiUnavailableException e) {
			System.err.println("Error getting synthesizer");
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * @return a specific transmitter object by setting the system property, otherwise the default
	 */
	private Transmitter getTransmitter() {
		if (! TRANS_DEV_NAME.isEmpty() && ! "default".equalsIgnoreCase(TRANS_DEV_NAME)) {
			System.setProperty(TRANS_PROP_KEY, TRANS_DEV_NAME);
		}
		
		try {
			return MidiSystem.getTransmitter();
		} catch (MidiUnavailableException e) {
			System.err.println("Error getting transmitter");
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Implementation of {@link Receiver} that will display the message
	 * before sending it.
	 * 
	 * @author Knute Snortum
	 * @version 2017/06/16
	 */
	private class DisplayReceiver implements Receiver {
		
		private Receiver receiver;
		
		public DisplayReceiver(Receiver receiver) {
			this.receiver = receiver;
		}

		@Override
		public void send(MidiMessage message, long timeStamp) {
			displayMessage(message);
			receiver.send(message, timeStamp);
		}

		@Override
		public void close() {
			receiver.close();
		}

		private void displayMessage(MidiMessage message) {
			byte[] bytes = message.getMessage();
			System.out.println("Status: " + message.getStatus());
			
			if (message.getLength() > 1){
    			for (int i = 1; i < bytes.length; i++) {
    				System.out.print((bytes[i] & 0xFF) + " ");
    			}
    			System.out.println();
			}
		}		
	}
}

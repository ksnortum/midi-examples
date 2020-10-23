package net.snortum.play.midi;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiEvent;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Track;

/**
 * Plays "Mary Had a Little Lamb" on the default software sequencer by
 * constructing each MIDI event by hand.
 * 
 * @author Knute Snortum
 * @version 2017-06-27
 */
public class PlaySequencer {

	/** 
	 * To use a specific sequencer, you can run {@link MidiDeviceDisplay} to
	 * discover the names of other sequencers on your system.
	 * 
	 * @see KeyboardToSynth
	 */
	private static final String SEQ_DEV_NAME = "default";
	private static final String SEQ_PROP_KEY = "javax.sound.midi.Sequence";

	public static void main(String[] args) {
		new PlaySequencer().run();
	}

	private void run() {
		
		// Get default sequencer, if it exists
		Sequencer sequencer = getSequencer();

		if (sequencer == null) {
			return;
		}

		try {
			sequencer.open();
		} catch (MidiUnavailableException e1) {
			e1.printStackTrace();
			return;
		}
		
		sequencer.setTempoInBPM(144.0f);

		// Input MIDI data
		try {
			sequencer.setSequence(getMidiInputData());
		} catch (InvalidMidiDataException e1) {
			e1.printStackTrace();
			return;
		}

		// Play sequence
		// Sleep, or first note is too long
		sleep(200);
		sequencer.start();

		while (sequencer.isRunning()) {
			sleep(1000);
		}

		// Sleep or last note is clipped
		sleep(200);
		sequencer.close();
	}

	// Create a sequence and set all MIDI events
	private Sequence getMidiInputData() {
		int ticksPerQuarterNote = 4;
		Sequence seq;
		try {
			seq = new Sequence(Sequence.PPQ, ticksPerQuarterNote);
			setMidiEvents(seq.createTrack());
		} catch (InvalidMidiDataException e) {
			e.printStackTrace();
			return null;
		}
		return seq;
	}

	// Set MIDI events to play "Mary Had a Little Lamb"
	private void setMidiEvents(Track track) {
		int channel = 0;
		int velocity = 64;
		int note = 61;
		int tick = 0;
		addMidiEvent(track, ShortMessage.NOTE_ON, channel, note, velocity, tick);
		addMidiEvent(track, ShortMessage.NOTE_OFF, channel, note, 0, tick + 3);
		addMidiEvent(track, ShortMessage.NOTE_ON, channel, note - 2, velocity, tick + 4);
		addMidiEvent(track, ShortMessage.NOTE_OFF, channel, note - 2, 0, tick + 7);
		addMidiEvent(track, ShortMessage.NOTE_ON, channel, note - 4, velocity, tick + 8);
		addMidiEvent(track, ShortMessage.NOTE_OFF, channel, note - 4, 0, tick + 11);
		addMidiEvent(track, ShortMessage.NOTE_ON, channel, note - 2, velocity, tick + 12);
		addMidiEvent(track, ShortMessage.NOTE_OFF, channel, note - 2, 0, tick + 15);
		addMidiEvent(track, ShortMessage.NOTE_ON, channel, note, velocity, tick + 16);
		addMidiEvent(track, ShortMessage.NOTE_OFF, channel, note, 0, tick + 19);
		addMidiEvent(track, ShortMessage.NOTE_ON, channel, note, velocity, tick + 20);
		addMidiEvent(track, ShortMessage.NOTE_OFF, channel, note, 0, tick + 23);
		addMidiEvent(track, ShortMessage.NOTE_ON, channel, note, velocity, tick + 24);
		addMidiEvent(track, ShortMessage.NOTE_OFF, channel, note, 0, tick + 31);
	}

	// Create a MIDI event and add it to the track
	private void addMidiEvent(Track track, int command, int channel, int data1,
			int data2, int tick) {
		ShortMessage message = new ShortMessage();
		try {
			message.setMessage(command, channel, data1, data2);
		} catch (InvalidMidiDataException e) {
			e.printStackTrace();
		}
		track.add(new MidiEvent(message, tick));
	}

	/**
	 * @return a specific sequencer object by setting the system property,
	 *         otherwise the default
	 */
	private Sequencer getSequencer() {
		if (!SEQ_DEV_NAME.isEmpty()
				|| !"default".equalsIgnoreCase(SEQ_DEV_NAME)) {
			System.setProperty(SEQ_PROP_KEY, SEQ_DEV_NAME);
		}

		try {
			return MidiSystem.getSequencer();
		} catch (MidiUnavailableException e) {
			System.err.println("Error getting sequencer");
			e.printStackTrace();
			return null;
		}
	}

	private void sleep(long millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}

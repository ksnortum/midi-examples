# midi-examples

This is a collection of Java examples for using MIDI written mainly for me to experiment but that may be useful to other people.

MIDI in Java is not intuitive.  The goal is to set (connect) a receiver (source of sound) to a transmitter (source of MIDI events like a synthesizer or a sequencer) and open a device (usually the transmitter).  You can get the transmitter from the open device.

A MIDI device can be a Transmitter, Receiver, Synthesizer, or Sequencer. The base class for all of these is [MidiDevice](https://docs.oracle.com/javase/8/docs/api/javax/sound/midi/MidiDevice.html).  You can discover what devices are on your system and what capabilities they have by running the command line program MidiDeviceDisplay.  A device that's not a Synthesizer or a Sequencer is a MIDI port.  You can tell whether a MIDI port is in or out by the maximum number of transmitters and receivers it can have.  The maximums are usually either 0 or -1 for unlimited.  MIDI in ports, synthesizers, and sequences have a nonzero number of receivers.  MIDI out ports and sequencers  have a nonzero number of transmitters.

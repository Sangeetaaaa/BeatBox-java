
import java.awt.*;
import javax.swing.*;
import javax.sound.midi.*;
import java.util.*;
import java.awt.event.*;

// my frame is the frame 
// background is Jpanel
// layout is ?

public class BeatBox {
    //Below we are making another panel so why this one ?
    //MAIN PANEL IS NOT THE PANEL BEHIND OR TOP OF BACKGROUND PANEL, IT COMES UNDER THE BACKGOUND PANEL IN THE CENTER
    JPanel mainPanel; // mainPanel is the center part where as background is the east and the weste part acc to me 
    ArrayList<JCheckBox> checkboxList; //Storing the all the checkboxs here 228's checkbox
    Sequencer sequencer;
    Sequence sequence;
    Track track;
    JFrame theFrame;

    String[] instrumentNames = {"Bass Drum", "Closed Hi-Hat", "Open Hi-Hat", "Acoustic Snaze", "Crash Cymbal", "Hand Clap"
    "High Tom", "Hi Bongo", "Maracass", "Whistle", "Low Congo", "Cowball", "Vibraslap", "Low-mid Tom", "High Agogo", "Open Hi congo"
    }
    //These are the actual drum keys like piano, except each keys on drum is different piano like 35 is key for bash drum 42 is closed Hi-Hat
    int[] instruments = {35, 42, 46, 38, 49, 39, 50, 60, 70, 72, 64, 56, 58, 47, 67, 63}

    public static void main(String[] args) {
        //not making an object here rather call it directly 
        //can we do like this ? its a static function but buildGUI is not 
        new BeatBox().buildGUI();
    }



    public void buildGUI() {
        theFrame = new JFrame("Cyber BeatBox");
        theFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // now what is this ? borderlayout is just a task managaer then why we need this thing in our program
        BorderLayout layout = new BorderLayout();
        //why we need layout to be passed here 
        JPanel background = new Jpanel(layout);
        //setting a empty margin in jpanel so that the components doesnot stick to the end border
        background.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        checkboxList = new ArrayList<JcheckBox>(); //we'll store 228 checkbox here 
        //the way they write this line is like panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        //In next few lines we are adding all the 4 buttons here 
        Box buttonBox = new Box(BoxLayout.Y_AXIS);

        //Here 4 buttons are created and then they are added to buttonBox and mightbe further it gets added to the screen 
        JButton start = new JButton("Start");
        start.addActiveListener(new MyStartListener());
        buttonBox.add(start);

        JButton stop = new JButton("Stop");
        stop.addActiveListener(new MyStopListener());
        buttonBox.add(stop);

        JButton upTempo = new JButton("Tempo Up");
        upTempo.addActiveListener(new MyUpTempoListener());
        buttonBox.add(upTempo);

        JButton downTempo = new JButton("Tempo Down");
        downTempo.addActiveListener(new MyDownTempoListener());
        buttonBox.add(downTempo);

        // after this another box type is created for name
        Box nameBox = new Box(BoxLayout.Y_AXIS);

        //adding the 16 label name one after the other in the screen.
        for (int i=0; i<16; i++) {
            nameBox.add(new label(instrumentNames[i]));
        }

        //got the ans 
        //so kindof they are creating boxes and inside they are adding buttons and in another box they are adding 
        //labels and then they are further aligning it in the panel
        background.add(BorderLayout.EAST, buttonBox);
        background.add(BorderLayout.WEST, nameBox);

        // so they add the panel background to the frame
        theFrame.getContentPane().add(background);

        //whats going on here ?
        //here we create grid of 16.16, add something related to vertical and horizotal gap
        GridLayout grid = new GridLayout(16, 16);
        grid.setVgap(1);
        grid.setHgap(2); //they create this grid for the adding the checkbox
        // then we pass it to the mainJpanel
        mainPanel = new Jpanel(grid);
        // i think they are doing something with the checkbox 16 16 
        background.add(BorderLayout.CENTER, mainPanel);

        //MAIN PANEL IS NOT THE PANEL BEHIND OR TOP OF BACKGROUND PANEL, IT COMES UNDER THE BACKGOUND PANEL IN THE CENTER

        // we created 256 checkboxes and set all of them to false so they are not checked and then we added it to 
        // checkboxList which was an array which we previously created and also added in teh main panel 
        for(int i=0; i<256; i++) {
            JCheckBox c = new JCheckBox();
            c.setSelected(false);
            checkboxList.add(c);
            mainPanel.add(c);
        }

        // why we call this methiod
        //we set up the midi here 
        setUpMidi();

        // what are setBounds and pack method doing here 
        theFrame.setBounds(50,50,300,300);
        theFrame.pack();
        theFrame.setVisible(true);
    }

    //we set up the midi here 
    public void setUpMidi(){
        try {
            sequencer = MidiSystem.getSequencer();
            sequencer.open();
            sequence = new Sequence(Sequence.PPQ,4);
            track = sequence.createTrack();
            sequencer.setTempoInBPM(120);
        } catch(Exception e) {e.printStackTrace();}
    }


    // This is where it all happens! Where we turn checkbox state into MIDI events, and add them to the Track.
    public void buildTrackAndStart() {
        // this holds the value for one instrument, all the 16beats
        // but what we will store here I dont know 
        int[] trackList = null;

        // acc to me i think there are these list of beats in the track which we can delete later 
        // so basically they get rid off the old track and make a fresh one 
        sequence.deleteTrack(track);
        track = sequence.createTrack();

        for (int i=0; i <16; i++) {
            //remember this is for the first row as soon as the first row completed we solve the second row
            trackList = new int[16];
            
            //key has the drum no only if i =0 and if 1 then  Closed Hi-Hat
            int key = instruments[i];

            for(int j=0; j<16; j++) {
                //why have they added this expression what it means ?
                //there are 256 elemets in checkboxlist 
                // j + 16*i  element no 
                // 0 + 16*1  16 
                // 1 + 16*1  17 
                // 2 + 16*1  18
                // 3 + 16*1  19
                // 4 ... 16
                // .get(elemtno)
                //so we check the whole checkboxes and if that single checkbox is selected then 
                //we add the key (instrument keys containing numbers 35, 46... which represt the instrument) 
                //we add that to trackList[]
                JCheckBox jc = checkboxList.get(j + 16*i);
                if(jc.isSelected()) {
                    trackList[j] = key;
                    // if 2ed and 7th checkbox in the first row is selected then
                    // [0, 0, 32, 0, 0, 0, 32, 0, 0, 0, 0, 0, 0, 0, 0, 0]
                } else {
                    trackList[j] = 0;
                }
            }

            // if 2ed and 7th checkbox in the first row is selected then
            // [0, 0, 32, 0, 0, 0, 32, 0, 0, 0, 0, 0, 0, 0, 0, 0]


            //what maketracks method does ?
            // For this instrument (for eg: if i=0 then the instrument is drum), and for all 16 beats,
            // make events and add them to the track
            makeTracks(trackList);
            // tracklist = [0, 0, 32, 0, 0, 0, 32, 0, 0, 0, 0, 0, 0, 0, 0, 0]

            //what exactly we do over here ? i need to come back when i'll make makeEvent method 
            track.add(makeEvent(176,1,127,0,16));
        }
        //figure out wht they do over here ?
        track.add(makeEvent(192,9,1,0,15));
        //Now playing the thing in a loop
        try {
            sequencer.setSequence(sequence);
            sequencer.setLoopCount(sequencer.LOOP_CONTINUOUSLY);
            sequencer.start();
            sequencer.setTempoInBPM(120);
        } catch (Exception e) {e.printStackTrace();}
    }


    public class MyStartListener implements ActionListener {
        public void actionPerformed(ActionEvent a ) {
            buildTrackAndStart();
        }
    }

    public class MyStopListener implements ActionListener {
        public void actionPerformed(ActionEvent a ) {
            sequencer.stop();
        }
    }

    public class MyUpTempoListener implements ActionListener {
        public void actionPerformed(ActionEvent a ) {
            float tempFactor = sequencer.getTempoFactor();
            sequencer.setTempoFactor((float) (tempoFactor*1.03));
        }
    }

    public class MyDownTempoListener implements ActionListener {
        public void actionPerformed(ActionEvent a) {
        float tempoFactor = sequencer.getTempoFactor();
        sequencer.setTempoFactor((float)(tempoFactor * .97));
        }
    }
    
    public void makeTracks(int[] list) {
        for(int i=0; i<16; i++) {
            int key = list[i];

            if(key != 0) {
                track.add(makeEvent(144,9,key,100,i));
                track.add(makeEvent(129,9,key,100,i+1));
            }
        }
    }

    public MidiEvent makeEvent(int comd, int chan, int one, int two, int tick) {
        MidiEvent event = null;
        try {
            ShortMessage a = new ShortMessage();
            a.setMessage(comd, chan, one, two);
            event = new MidiEvent(a, tick);
        }
        catch(Exception e) {e.printStackTrace(); }
        return event;
    }
}

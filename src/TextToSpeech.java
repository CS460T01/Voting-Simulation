import com.sun.speech.freetts.*;

public class TextToSpeech {

    public static void main(String[] args) {
        // Set property as Kevin Dictionary
        System.setProperty("freetts.voices",
                "com.sun.speech.freetts.en.us.cmu_us_kal.KevinVoiceDirectory");

        // Register Engine
        VoiceManager vm = VoiceManager.getInstance();

        // Finding a voice
        Voice voice = vm.getVoice("kevin16");

        if (voice != null) {
            // Allocate the resources for the voice
            voice.allocate();

            try {
                // Speak the given text until queue is empty.
                voice.speak("Hello, this is an example of text to speech conversion using FreeTTS in Java.");
            } catch (Exception e) {
                e.printStackTrace();
            }

            // Deallocate the resources for the voice
            voice.deallocate();
        } else {
            System.out.println("No voice named kevin16 found.");
        }
    }
}

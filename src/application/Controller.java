package application;

import java.io.*;
import java.net.URI;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Scanner;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Slider;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class Controller implements Initializable {
	
	@FXML
	private Pane pane;
	@FXML
	private Label songLabel;
	@FXML
	private Button playButton, pauseButton, prevButton, nextButton;
	@FXML
	private Slider volumeSlider;
	@FXML
	private ProgressBar songProgress;
	@FXML
	private Label volumeLabel;
	
	private Media media;
	private MediaPlayer mediaPlayer;
	
	private ArrayList<File> songs;
	private ArrayList<File> songsAdd;
	private File directory;
	private File[] files;
	private int songNumber;
	
	private Timer timer;
	private TimerTask task;
	private boolean running;
	//private StringBuilder bee;
	private String bee;
	private String oS;
	private List<String> preSongs;
	private List<String> pregSongs;
	// «–Œ¡»“» œ≈–≈¬≤– ” œŒ¬“Œ–≈Õ‹ œ≤ƒ ◊¿— ƒŒƒ¿¬¿ÕÕﬂ Ã”«» » ¬ ‘¿…À… Ã”«» » œ–» –≤«Õ»’ —≈¿Õ—¿’
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		oS = System.getProperty("os.name");
		preSongs = new ArrayList<String>();
		pregSongs = new ArrayList<String>();
		songs = new ArrayList<File>();
		songsAdd = new ArrayList<File>();
		// READER
		try {
		/*	Scanner nameScan = new Scanner(new File ("nameSong"));
			while(nameScan.hasNextLine()) {
				preSongs.add(nameScan.nextLine().toString());
				System.out.println(preSongs);
			}
			nameScan.close();   */ 
			repScan();
		    Set be = new HashSet<String>(preSongs);	
		    songsAdd.addAll(be);
			songNumber = 0;
			System.out.println(songsAdd.get(0));
			volumeLabel.setText("100%");
			volumeSlider.valueProperty().addListener(new ChangeListener<Number>() {
				@Override
				public void changed(ObservableValue<? extends Number> arg0, Number arg1, Number arg2) {
					mediaPlayer.setVolume(volumeSlider.getValue() * 0.01);
					Integer pch = (int)volumeSlider.getValue();
					volumeLabel.setText(pch.toString() + "%");
				}
			});
			songProgress.setStyle("-fx-accent:#1aa824");
			if(oS.startsWith("Windows")) {
				chSongWin();
				System.out.println("opa");
			}else if(oS.startsWith("Linux")) {
				chSongLin();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
	}
	public void playMedia() {
	//	media = new Media(bee.toString());
	//	mediaPlayer = new MediaPlayer(media);
		songLabel.setText(preSongs.get(songNumber).toString());
		beginTimer();
		mediaPlayer.play();
	}
	
	public void pauseMedia() {
		cancelTimer();
		mediaPlayer.pause();
	}
	
	public void repScan() throws FileNotFoundException {
		Scanner nameScan = new Scanner(new File ("nameSong"));
		while(nameScan.hasNextLine()) {
			pregSongs.add(nameScan.nextLine().toString());
			System.out.println(pregSongs);
		}
		Set beh = new HashSet<String>(pregSongs);
	    preSongs.addAll(beh);
		nameScan.close();    
	}
	
	public void previousMedia() {
		if(songNumber > 0) {
			songNumber--;
			if(this.mediaPlayer != null) {
				mediaPlayer.stop();
				if(running) {
					cancelTimer();
				}
			}
			if(oS.startsWith("Windows")) {
				chSongWin();
				System.out.println("opa");
			}else if(oS.startsWith("Linux")) {
				chSongLin();
			}
			songLabel.setText(preSongs.get(songNumber).toString());
			beginTimer();
			mediaPlayer.play();
		}
		System.out.println(songNumber);
	}
	
	public void nextMedia() {
		if(songNumber < preSongs.size()-1) {
			songNumber++;
			if(this.mediaPlayer != null) {
				mediaPlayer.stop();
				if(running) {
					cancelTimer();
				}
			}
			if(oS.startsWith("Windows")) {
				chSongWin();
				System.out.println("opa");
			}else if(oS.startsWith("Linux")) {
				chSongLin();
			}
			songLabel.setText(preSongs.get(songNumber).toString());
			beginTimer();
			mediaPlayer.play();
		}
		System.out.println(songNumber);
	}
	
	public void beginTimer() {
		timer = new Timer();
		task = new TimerTask() {
			public void run() {
				running = true;
				double cur = mediaPlayer.getCurrentTime().toSeconds();
				double endSong = media.getDuration().toSeconds();
				songProgress.setProgress(cur/endSong);
				double a = 1.0;
				if(cur/endSong == a || cur/endSong == 1) {
					System.out.println("canceled");
					cancelTimer();
				}
			}
			
		};
		timer.scheduleAtFixedRate(task, 1000, 1000);
	}	
	
	public void chSongWin() {
		String[] prev = preSongs.get(songNumber).toString().split("\\\\");
		String ya = Arrays.toString(prev).replace(",", "/").replace(" ", "").replace("[", "").replace("]", "").toString();
		StringBuilder sb = new StringBuilder(ya);
		bee = sb.insert(0, "file:///").toString();
		System.out.println(bee);
		media = new Media(bee);
		mediaPlayer = new MediaPlayer(media);
	}
	
	public void chSongLin() {
		String[] prev = preSongs.get(songNumber).toString().split("\\\\");
		String ya = Arrays.toString(prev).replace(",", "/").replace(" ", "").replace("[", "").replace("]", "").toString();
		StringBuilder sb = new StringBuilder(ya);
		bee = sb.insert(0, "file:").toString();
		System.out.println(bee);
		media = new Media(bee);
		mediaPlayer = new MediaPlayer(media);
	}
	
	public void cancelTimer() {
		running = false;
		timer.cancel();
	}
	
	public void addMedia() throws IOException {
		FileChooser fileChooser = new FileChooser();
		fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("mp3 file", "*.mp3"));
	    List<File> selectedFile = fileChooser.showOpenMultipleDialog(null);
	    Set se = new HashSet(selectedFile);	
	    songs.addAll(se);
	    Object[] songRemove = songs.toArray();
	    	for (Object indexS : songRemove) {
	    		if (songs.indexOf(indexS) != songs.lastIndexOf(indexS)) {
	    			songs.remove(songs.lastIndexOf(indexS));
	    		}
	    	}
	    // Create File
	    File file = new File("NameSong");
	    if(file.exists()) {
	    		FileWriter songWrite = new FileWriter("NameSong", true);
	    		int size = songs.size();
	    		for(int i=0; i<size;i++) {
	    			String str = songs.get(i).toString();
	    			songWrite.write(str + "\n");
	    		}
				repScan();
	    		songWrite.close();
	    }else {	    	
	    	file.createNewFile();
	    	FileWriter songWrite = new FileWriter("NameSong",true);
    		int size = songs.size();
    		for(int i=0; i<size;i++) {
    			String str = songs.get(i).toString();
    			songWrite.write(str + "\n");
    		}
			repScan();
    		songWrite.close();
	    }//end if(exits)
	}
}

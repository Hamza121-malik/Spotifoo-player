import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toList;

public class SpotifooModel {

    private static final String COMMA_DELIMITER = ",";
    public static final String SONGS_DIR = "assets/songs/";
    public static final String ALBUMS_DIR = "assets/albums/";
    public static final String FALL_BACK_ALBUM_ART = "assets/no-picture.png";
    public static final String DATA_TXT_FILE = "assets/data.txt";
    public static final String SONG = "Song";
    public static final String ARTIST = "Artist";
    public static final String ALBUM = "Album";
    public static final String GENRE = "Genre";
    public static final int MAIN_MENU_OPTIONS_COUNT = 6;
    private static List<Song> songs = new ArrayList<>();
    public static List<String> mainMenuOptions = new ArrayList<>();

    private SpotifooView spotifooView;

    public SpotifooModel() {
        this.spotifooView = spotifooView;
    }

    public List<Song> getSongs() {
        return songs;
    }
    

    public static void setMainMenuOptions() {
        mainMenuOptions.add("Songs");
        mainMenuOptions.add("Artists");
        mainMenuOptions.add("Albums");
        mainMenuOptions.add("Genres");
        mainMenuOptions.add("Search");
        mainMenuOptions.add("Quit");
    }

    public List<String> getMainMenuOptions() {
        return mainMenuOptions;
    }

    public void populateData() {
        // Reading Data.txt
        try (BufferedReader br = new BufferedReader(new FileReader(DATA_TXT_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] songRawData = line.split(COMMA_DELIMITER);
                songs.add(mapSongRawDataToSongObj(songRawData));
            }
        } catch (IOException e) {
            System.err.println("No Data File Found");
            System.exit(1);

        }
    }

    public List<String> mapOptionToIndex(List<String> options) {
        List<String> optionList = IntStream.range(0, options.size())
                .mapToObj(index -> "[" + (index + 1) + "]" + " " + options.get(index))
                .collect(toList());
        return optionList;
    }

    public static Song mapSongRawDataToSongObj(String[] values) {
        String songName = values[0];
        String artistName = values[1];
        String albumName = values[2];
        String genre = values[3];
        String fileName = values[4];
        String albumImage = values[5];
        return new Song(songName, artistName, albumName, genre, fileName, albumImage);
    }

    public List<String> mapMainMenuOptions() {
       return mapOptionToIndex(mainMenuOptions);
    }

    public static Map<String, List<Song>> groupSongListByFilter(List<Song> songList, String filterName) {
        LinkedHashMap<String, List<Song>> filteredSongs = new LinkedHashMap<>();
        switch(filterName) {
            case SONG:
                filteredSongs = songList.stream()
                        .collect(
                                Collectors.groupingBy(Song::getName, LinkedHashMap::new, toList()));
                break;
            case ARTIST:
                filteredSongs = songList.stream()
                        .collect(
                                Collectors.groupingBy(Song::getArtistName,LinkedHashMap::new, toList()));
                break;
            case ALBUM:
                filteredSongs = songList.stream()
                        .collect(
                                Collectors.groupingBy(Song::getAlbumName,LinkedHashMap::new, toList()));
                break;
            case GENRE:
                filteredSongs = songList.stream()
                        .collect(
                                Collectors.groupingBy(Song::getGenre,LinkedHashMap::new, toList()));
                break;
            default:
                SpotifooView.showWarning();
        }

        return filteredSongs;
    }

    public void showOptionListWithIndexes(List<String> songNames) {
        List<String> songListWithIndex = mapOptionToIndex(songNames);
        songListWithIndex.forEach(value -> this.spotifooView.print(value, true));
        SpotifooView.print("[" + 0 + "]" + " Back to Main Menu ", true);
    }

    public void openAlbumArt(Desktop desktop, String albumArt) throws IOException {
        File albumArtFile = new File(ALBUMS_DIR + albumArt);
        int extensionIndex = albumArtFile.toString().lastIndexOf('.');
        if (!albumArtFile.exists()  ||  extensionIndex < 0 ) {
            albumArtFile = new File(FALL_BACK_ALBUM_ART);
        }
        desktop.open(albumArtFile);
    }

   public String getUserInput() {
        Scanner scanner = new Scanner(System.in);
        return scanner.nextLine();
    }

    public boolean isUserInputValid(String userInput, int limit) {
        if (isInteger(userInput)) {
            int userInputInt = Integer.parseInt(userInput);
            return (userInputInt >= 0 && userInputInt <= limit);
        }
        return false;
    }

    public static boolean isInteger(String input) {
        return isInteger(input,10);
    }

    public static boolean isInteger(String input, int radix) {
        if(input.isEmpty()) return false;
        for(int i = 0; i < input.length(); i++) {
            if(i == 0 && input.charAt(i) == '-') {
                if(input.length() == 1) return false;
                else continue;
            }
            if(Character.digit(input.charAt(i),radix) < 0) return false;
        }
        return true;
    }
}

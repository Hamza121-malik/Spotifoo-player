import java.awt.*;
import java.io.*;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toList;

public class SpotifooRunner {
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

    public static void main(String[] args){

        populateData();
        
        displayWelcomeMessage();
        // Initialize the Main Menu
        init();


        // printRecords(songs);
    }


    private static String getUserInput() {
        Scanner scanner = new Scanner(System.in);
        return scanner.nextLine();
    }
    // Make a function to get prompted value from user and see if its correct option or not
    // String value, -negative value and positive value with wrong range

    private static boolean isUserInputValid(String userInput, int limit) {
        if (isInteger(userInput)) {
            int userInputInt = Integer.parseInt(userInput);
            return (userInputInt >= 0 && userInputInt <= limit);
        }
        return false;
    }

    private static void populateData() {
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

    public static Song mapSongRawDataToSongObj(String[] values) {
        String songName = values[0];
        String artistName = values[1];
        String albumName = values[2];
        String genre = values[3];
        String fileName = values[4];
        String albumImage = values[5];
        return new Song(songName, artistName, albumName, genre, fileName, albumImage);

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
                System.out.println("Wrong Filter selected");
        }

        return filteredSongs;
    }
    
    private static void printRecords(List<Song> records) {
        records.forEach(System.out::println);
    }

    public static void init() {
        displayMainMenu();
        displayOptionPrompt();
        processMenuOption(getUserInput());
    }

    private static void processMenuOption(String mainMenuSelectedOption) {
        int mainMenuSelectionOption = 0;
        if (isUserInputValid(mainMenuSelectedOption, MAIN_MENU_OPTIONS_COUNT)) {
            mainMenuSelectionOption = Integer.parseInt(mainMenuSelectedOption);
            switch (mainMenuSelectionOption) {
                case 1:
                    flushScreen();
                    System.out.println("Songs Menu");
                    displaySongList(songs);
                    // for (Map.Entry<String, List<Song>> entry : songMapBySearchInput.entrySet()) {
                    //   List<Song> list = entry.getValue();
                    // Do things with the list
                    //}
                    break;
                case 2:
                    flushScreen();
                    System.out.println("Artists Available");
                    final Map<String, List<Song>> songMapByArtist = groupSongListByFilter(songs, ARTIST);
                    final List<String> artistList = songMapByArtist.keySet().stream().collect(Collectors.toList());
                    showOptionListWithIndexes(artistList);
                    displayOptionPrompt();
                    String selectedArtist = getUserInput();
                    while (!isUserInputValid(selectedArtist, artistList.size())) {
                        System.out.println("!!!! Wrong Option Selected !!!!");
                        displayOptionPrompt();
                        selectedArtist = getUserInput();
                    }
                    int selectedOptionInArtistsMenu = Integer.parseInt(selectedArtist);
                    printSongList(songMapByArtist, artistList, selectedOptionInArtistsMenu);
                    break;
                case 3:
                    flushScreen();
                    System.out.println("Albums");
                    final Map<String, List<Song>> songMapByAlbum = groupSongListByFilter(songs, ALBUM);
                    final List<String> albumList = songMapByAlbum.keySet().stream().collect(Collectors.toList());
                    showOptionListWithIndexes(albumList);
                    displayOptionPrompt();
                    String selectedAlbum = getUserInput();
                    while (!isUserInputValid(selectedAlbum, albumList.size())) {
                        System.out.println("!!!! Wrong Option Selected !!!!");
                        displayOptionPrompt();
                        selectedAlbum = getUserInput();
                    }
                    int selectedOptionInAlbumsMenu = Integer.parseInt(selectedAlbum);
                    printSongList(songMapByAlbum, albumList, selectedOptionInAlbumsMenu);
                    break;
                case 4:
                    flushScreen();
                    System.out.println("Genres");
                    final Map<String, List<Song>> songMapByGenre = groupSongListByFilter(songs, GENRE);
                    final List<String> genreList = songMapByGenre.keySet().stream().collect(Collectors.toList());
                    showOptionListWithIndexes(genreList);
                    displayOptionPrompt();
                    String selectedGenre = getUserInput();
                    while (!isUserInputValid(selectedGenre, genreList.size())) {
                        System.out.println("!!!! Wrong Option Selected !!!!");
                        displayOptionPrompt();
                        selectedGenre = getUserInput();
                    }
                    int selectedOptionInGenresMenu = Integer.parseInt(selectedGenre);
                    printSongList(songMapByGenre, genreList, selectedOptionInGenresMenu);
                    break;
                case 5:
                    flushScreen();
                    System.out.println("Search song:");
                    System.out.print("Write the name of the song and press Enter:");
                    String searchQuery =  getUserInput();
                    final List<Song> songList = songs.stream().filter(song -> !searchQuery.isEmpty() && song.getName().toLowerCase().contains(searchQuery.toLowerCase())).collect(toList());
                    if (songList.size() > 0) {
                        displaySongList(songList);
                    } else {
                        System.out.println(" ! No Result Found !");
                        displaySongList(songList);
                    }
                    break;
                case 6:
                    System.out.println("Closing the Application!!!");
                    System.exit(0);
                default:
                    System.out.println("Wrong Option, ");
                    displayOptionPrompt();
                    processMenuOption(getUserInput());
            }
        } else {
            System.out.println("Wrong Option!");
            displayOptionPrompt();
            processMenuOption(getUserInput());
        }
    }

    private static void flushScreen() {
        System.out.print("\033[4H\033[0J");
        System.out.flush();
    }

    private static void displayMainMenu() {
        List<String> mainMenuOptions = new ArrayList<>() {
            {
                add("Songs");
                add("Artists");
                add("Albums");
                add("Genres");
                add("Search");
                add("Quit");
            }
        };

        List<String> optionList = mapOptionToIndex(mainMenuOptions);
        System.out.println("Main Menu Options:");
        optionList.forEach(System.out::println);
    }

    private static void displayWelcomeMessage() {
        System.out.print("\033[0;32m");
        System.out.println("Welcome to the Spotifoo Music Player!");
        System.out.print("\033[0m");
        System.out.println();
    }

    private static void displayOptionPrompt() {
        System.out.print("Choose an option and press Enter:");
    }

    private static void displaySongList(List<Song> songs) {
        final Map<String, List<Song>> songMapByName = groupSongListByFilter(songs, SONG);
        final List<String> songList = songMapByName.keySet().stream().collect(Collectors.toList());
        showOptionListWithIndexes(songList);
        displayOptionPrompt();
        String selectedOptionOnSongsMenu = getUserInput();
        while (!isUserInputValid(selectedOptionOnSongsMenu, songList.size())) {
            System.out.println("Wrong Choice");
            displayOptionPrompt();
            selectedOptionOnSongsMenu = getUserInput();
        }
        int selectedOptionOnSongsMenuInt = Integer.parseInt(selectedOptionOnSongsMenu);
        if (selectedOptionOnSongsMenuInt == 0) {
            flushScreen();
            init();
        }
        else {
            final String selectedSongName = songList.get(selectedOptionOnSongsMenuInt - 1);
            playSong(songMapByName.get(selectedSongName).get(0));
        }
    }

    private static void showOptionListWithIndexes(List<String> songNames) {
        List<String> songListWithIndex = mapOptionToIndex(songNames);
        songListWithIndex.forEach(System.out::println);
        System.out.println("[" + 0 + "]" + " Back to Main Menu ");
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
    
    private static void printSongList(Map<String, List<Song>> itemListMap, List<String> itemList, int selectedOptionInMenu) {
        if (selectedOptionInMenu == 0) {
            flushScreen();
            init();
        }  else {
            final String selectedOptionKey = itemList.get(selectedOptionInMenu - 1);
            final List<Song> songListBySelectedItem = itemListMap.get(selectedOptionKey);

           // Only to display song list and play selected song
            displaySongList(songListBySelectedItem);

        }
    }

    private static List<String> mapOptionToIndex(List<String> options) {
        List<String> optionList = IntStream.range(0, options.size())
                .mapToObj(index -> "[" + (index + 1) + "]" + " " + options.get(index))
                .collect(toList());
        return optionList;
    }

    private static void playSong(Song song) {
        try {
            Desktop desktop = null;
            if (Desktop.isDesktopSupported()) {
                desktop = Desktop.getDesktop();
            }

            // Get value from Song Object
            String mp3FileName = song.getFileName();
            String albumArt = song.getAlbumImage();
            
            File audioFile = new File(SONGS_DIR + mp3FileName);

            // Get Name from Audio file
            final String audioFileName = audioFile.getName();
            // Get dotIndex of last . to find extension
            int dotIndex = audioFileName.lastIndexOf('.');

            if (audioFile.exists() && dotIndex > 0) {
                desktop.open(audioFile);
                System.out.println("----Playing Song-----");

                // Add Interrup to stop music when close the file

                openAlbumArt(desktop, albumArt);
            } else {
                // No file or Not playable audio file
                System.out.println("Could not play the song");
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
   
    }

    private static void openAlbumArt(Desktop desktop, String albumArt) throws IOException {
        File albumArtFile = new File(ALBUMS_DIR + albumArt);
        int extensionIndex = albumArtFile.toString().lastIndexOf('.');
        if (!albumArtFile.exists()  ||  extensionIndex < 0 ) {
            albumArtFile = new File(FALL_BACK_ALBUM_ART);
        }
        desktop.open(albumArtFile);
    }
}

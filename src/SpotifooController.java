import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

public class SpotifooController {

    private SpotifooView spotifooView;
    private SpotifooModel spotifooModel;

    public SpotifooController(SpotifooModel spotifooModel, SpotifooView spotifooView) {
        this.spotifooModel = spotifooModel;
        this.spotifooView = spotifooView;
    }

    public void initialize() {
        this.spotifooModel.populateData();
        this.spotifooModel.setMainMenuOptions();
        this.spotifooView.showWelcomeMessage();
        this.initMainMenu();
    }

    private void initMainMenu() {
        final List<String> mainMenuOptions = this.spotifooModel.mapMainMenuOptions();
        this.spotifooView.showMainMenu(mainMenuOptions);
        this.spotifooView.showOptionPrompt();
        processMenuOption(this.spotifooModel.getUserInput());
    }

    private void processMenuOption(String mainMenuSelectedOption) {
        int mainMenuSelectionOption = 0;
        if (this.spotifooModel.isUserInputValid(mainMenuSelectedOption, SpotifooModel.MAIN_MENU_OPTIONS_COUNT)) {
            mainMenuSelectionOption = Integer.parseInt(mainMenuSelectedOption);
            switch (mainMenuSelectionOption) {
                case 1:
                    this.spotifooView.flushScreen();
                    this.spotifooView.print("Songs Menu", true);
                    displaySongList(this.spotifooModel.getSongs());
                    break;
                case 2:
                    this.spotifooView.flushScreen();
                    this.spotifooView.print("Artists Available", true);
                    final Map<String, List<Song>> songMapByArtist = this.spotifooModel.groupSongListByFilter(this.spotifooModel.getSongs(), SpotifooModel.ARTIST);
                    final List<String> artistList = songMapByArtist.keySet().stream().collect(toList());
                    this.spotifooModel.showOptionListWithIndexes(artistList);
                    this.spotifooView.showOptionPrompt();
                    String selectedArtist = this.spotifooModel.getUserInput();
                    while (!this.spotifooModel.isUserInputValid(selectedArtist, artistList.size())) {
                        this.spotifooView.showWarning();
                        this.spotifooView.showOptionPrompt();
                        selectedArtist = this.spotifooModel.getUserInput();
                    }
                    int selectedOptionInArtistsMenu = Integer.parseInt(selectedArtist);
                    printSongList(songMapByArtist, artistList, selectedOptionInArtistsMenu);
                    break;
                case 3:
                    this.spotifooView.flushScreen();
                    this.spotifooView.print("Albums", true);
                    final Map<String, List<Song>> songMapByAlbum = this.spotifooModel.groupSongListByFilter(this.spotifooModel.getSongs(), SpotifooModel.ALBUM);
                    final List<String> albumList = songMapByAlbum.keySet().stream().collect(toList());
                    this.spotifooModel.showOptionListWithIndexes(albumList);
                    this.spotifooView.showOptionPrompt();
                    String selectedAlbum = this.spotifooModel.getUserInput();
                    while (!this.spotifooModel.isUserInputValid(selectedAlbum, albumList.size())) {
                        this.spotifooView.showWarning();
                        this.spotifooView.showOptionPrompt();
                        selectedAlbum = this.spotifooModel.getUserInput();
                    }
                    int selectedOptionInAlbumsMenu = Integer.parseInt(selectedAlbum);
                    printSongList(songMapByAlbum, albumList, selectedOptionInAlbumsMenu);
                    break;
                case 4:
                    this.spotifooView.flushScreen();
                    this.spotifooView.print("Genres", true);
                    final Map<String, List<Song>> songMapByGenre = this.spotifooModel.groupSongListByFilter(this.spotifooModel.getSongs(), SpotifooModel.GENRE);
                    final List<String> genreList = songMapByGenre.keySet().stream().collect(toList());
                    this.spotifooModel.showOptionListWithIndexes(genreList);
                    this.spotifooView.showOptionPrompt();
                    String selectedGenre = this.spotifooModel.getUserInput();
                    while (!this.spotifooModel.isUserInputValid(selectedGenre, genreList.size())) {
                        this.spotifooView.showWarning();
                        this.spotifooView.showOptionPrompt();
                        selectedGenre = this.spotifooModel.getUserInput();
                    }
                    int selectedOptionInGenresMenu = Integer.parseInt(selectedGenre);
                    printSongList(songMapByGenre, genreList, selectedOptionInGenresMenu);
                    break;
                case 5:
                    this.spotifooView.flushScreen();
                    this.spotifooView.print("Search song:", true);
                    this.spotifooView.print("Write the name of the song and press Enter:", false);
                    String searchQuery =  this.spotifooModel.getUserInput();
                    final List<Song> songList = this.spotifooModel.getSongs().stream().filter(song -> !searchQuery.isEmpty() && song.getName().toLowerCase().contains(searchQuery.toLowerCase())).collect(toList());
                    if (songList.size() > 0) {
                        displaySongList(songList);
                    } else {
                        this.spotifooView.print(" ! No Result Found !", true);
                        displaySongList(songList);
                    }
                    break;
                case 6:
                    this.spotifooView.print("!!! Closing the Application !!!", true);
                    System.exit(0);
                default:
                    this.spotifooView.showWarning();
                    this.spotifooView.showOptionPrompt();
                    processMenuOption(this.spotifooModel.getUserInput());
            }
        } else {
            this.spotifooView.showWarning();
            this.spotifooView.showOptionPrompt();
            processMenuOption(this.spotifooModel.getUserInput());
        }
    }

    public void displaySongList(List<Song> songs) {
        final Map<String, List<Song>> songMapByName = this.spotifooModel.groupSongListByFilter(songs, SpotifooModel.SONG);
        final List<String> songList = songMapByName.keySet().stream().collect(Collectors.toList());
        this.spotifooModel.showOptionListWithIndexes(songList);
        this.spotifooView.showOptionPrompt();
        String selectedOptionOnSongsMenu = this.spotifooModel.getUserInput();
        while (!this.spotifooModel.isUserInputValid(selectedOptionOnSongsMenu, songList.size())) {
            SpotifooView.showWarning();
            this.spotifooView.showOptionPrompt();
            selectedOptionOnSongsMenu = this.spotifooModel.getUserInput();
        }
        int selectedOptionOnSongsMenuInt = Integer.parseInt(selectedOptionOnSongsMenu);
        if (selectedOptionOnSongsMenuInt == 0) {
            this.spotifooView.flushScreen();
            initMainMenu();
        }
        else {
            final String selectedSongName = songList.get(selectedOptionOnSongsMenuInt - 1);
            playSong(songMapByName.get(selectedSongName).get(0));
        }
    }

    public void printSongList(Map<String, List<Song>> itemListMap, List<String> itemList, int selectedOptionInMenu) {
        if (selectedOptionInMenu == 0) {
            this.spotifooView.flushScreen();
            initMainMenu();
        }  else {
            final String selectedOptionKey = itemList.get(selectedOptionInMenu - 1);
            final List<Song> songListBySelectedItem = itemListMap.get(selectedOptionKey);

            // Only to display song list and play selected song
            displaySongList(songListBySelectedItem);

        }
    }

    private void playSong(Song song) {
        try {
            Desktop desktop = null;
            if (Desktop.isDesktopSupported()) {
                desktop = Desktop.getDesktop();
            }

            // Get value from Song Object
            String mp3FileName = song.getFileName();
            String albumArt = song.getAlbumImage();

            File audioFile = new File(SpotifooModel.SONGS_DIR + mp3FileName);

            // Get Name from Audio file
            final String audioFileName = audioFile.getName();
            // Get dotIndex of last . to find extension
            int dotIndex = audioFileName.lastIndexOf('.');

            if (audioFile.exists() && dotIndex > 0) {
                desktop.open(audioFile);
                this.spotifooView.print("----Playing Song-----", true);

                // Add Interrup to stop music when close the file

                this.spotifooModel.openAlbumArt(desktop, albumArt);
            } else {
                // No file or Not playable audio file
                this.spotifooView.showError();
            }
        } catch (IOException ex) {
            this.spotifooView.showError();
        }

    }


}

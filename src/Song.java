import java.util.Objects;

public class Song {

    public String name;
    private String artistName;
    private String albumName;
    private String genre;
    private String fileName;
    private String albumImage;

    public void setName(String name) {
        this.name = name;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void setAlbumImage(String albumImage) {
        this.albumImage = albumImage;
    }

    public Song(String name, String artistName, String albumName, String genre, String fileName, String albumImage) {
        this.name = name;
        this.artistName = artistName;
        this.albumName = albumName;
        this.genre = genre;
        this.fileName = fileName;
        this.albumImage = albumImage;
    }

    public String getName() {
        return name;
    }

    public String getArtistName() {
        return artistName;
    }

    public String getAlbumName() {
        return albumName;
    }

    public String getGenre() {
        return genre;
    }

    public String getFileName() {
        return fileName;
    }

    public String getAlbumImage() {
        return albumImage;
    }

    @Override
    public String toString() {
        return "Song{" +
                "name='" + name + '\'' +
                ", artistName='" + artistName + '\'' +
                ", albumName='" + albumName + '\'' +
                ", genre='" + genre + '\'' +
                ", fileName='" + fileName + '\'' +
                ", albumImage='" + albumImage + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Song)) return false;
        Song song = (Song) o;
        return Objects.equals(name, song.name) && Objects.equals(artistName, song.artistName) && Objects.equals(albumName, song.albumName) && Objects.equals(genre, song.genre) && Objects.equals(fileName, song.fileName) && Objects.equals(albumImage, song.albumImage);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, artistName, albumName, genre, fileName, albumImage);
    }
}

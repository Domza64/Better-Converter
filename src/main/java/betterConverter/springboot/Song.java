package betterConverter.springboot;

public class Song {
    private final String title;
    private final int id;
    private final String contentType;
    private final byte[] data;

    public Song(String title, int id, String contentType, byte[] songFile) {
        this.title = title;
        this.id = id;
        this.contentType = contentType;
        this.data = songFile;
    }

    public String getTitle() {
        return title;
    }
    public int getId() {
        return id;
    }
    public String getContentType() {
        return contentType;
    }
    public byte[] getData() {
        return data;
    }
}

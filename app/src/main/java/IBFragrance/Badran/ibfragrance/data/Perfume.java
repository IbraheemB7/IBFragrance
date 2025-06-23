package IBFragrance.Badran.ibfragrance.data;

public class Perfume {
    private String id;
    private String name;
    private String price;
    private String imageUrl;

    // منشئ فارغ (مطلوب لـ Firebase)
    public Perfume() {}

    // منشئ كامل
    public Perfume(String id, String name, String price, String imageUrl) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
    }

    // Getters و Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}

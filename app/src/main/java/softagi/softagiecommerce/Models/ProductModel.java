package softagi.softagiecommerce.Models;

public class ProductModel
{
    private String title,price,img,id,brand_id;

    public ProductModel() {
    }

    public ProductModel(String title, String price, String img, String id, String brand_id) {
        this.title = title;
        this.price = price;
        this.img = img;
        this.id = id;
        this.brand_id = brand_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBrand_id() {
        return brand_id;
    }

    public void setBrand_id(String brand_id) {
        this.brand_id = brand_id;
    }
}

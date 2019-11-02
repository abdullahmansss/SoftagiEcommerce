package softagi.softagiecommerce.Models;

public class ProductModel
{
    private int img;
    private String title;

    public ProductModel() {
    }

    public ProductModel(int img, String title) {
        this.img = img;
        this.title = title;
    }

    public int getImg() {
        return img;
    }

    public void setImg(int img) {
        this.img = img;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}

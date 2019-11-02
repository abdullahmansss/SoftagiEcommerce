package softagi.softagiecommerce.Models;

import java.util.List;

public class BrandModel
{
    private String title,categ;
    private String img;
    private List<ProductModel> productModels;

    public BrandModel() {
    }

    public BrandModel(String title, String categ, String img, List<ProductModel> productModels) {
        this.title = title;
        this.categ = categ;
        this.img = img;
        this.productModels = productModels;
    }

    public BrandModel(String title, String categ, String img) {
        this.title = title;
        this.categ = categ;
        this.img = img;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCateg() {
        return categ;
    }

    public void setCateg(String categ) {
        this.categ = categ;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public List<ProductModel> getProductModels() {
        return productModels;
    }

    public void setProductModels(List<ProductModel> productModels) {
        this.productModels = productModels;
    }
}

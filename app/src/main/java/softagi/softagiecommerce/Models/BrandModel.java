package softagi.softagiecommerce.Models;

import java.util.List;

public class BrandModel
{
    private String title,categ,img,id;

    public BrandModel() {
    }

    public BrandModel(String title, String categ, String img, String id)
    {
        this.title = title;
        this.categ = categ;
        this.img = img;
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
}

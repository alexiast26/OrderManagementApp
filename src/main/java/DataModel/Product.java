package DataModel;

public class Product {
    private int id;
    private String name;
    private int price;
    private int stock;


    public Product() {}

    public Product(String name, int price, int stock) {
        this.name = name;
        this.price = price;
        this.stock = stock;
    }

    public Product(int id, String name, int price, int stock) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.stock = stock;
    }

    /**
     *
     * @return
     */
    public int getId() {
        return id;
    }

    /**
     *
     * @param id
     */

    public void setId(int id) {
        this.id = id;
    }

    /**
     *
     * @return
     */

    public String getName() {
        return name;
    }

    /**
     *
     * @param name
     */

    public void setName(String name) {
        this.name = name;
    }

    /**
     *
     * @return
     */
    public int getPrice() {
        return price;
    }

    /**
     *
     * @param price
     */
    public void setPrice(int price) {
        this.price = price;
    }

    /**
     *
     * @return
     */
    public int getStock() {
        return stock;
    }

    /**
     *
     * @param stock
     */
    public void setStock(int stock) {
        this.stock = stock;
    }

    /**
     *
     * @return
     */
    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", stock=" + stock +
                '}';
    }
}

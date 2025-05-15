package DataModel;

public class OrderTable {
    private int id;
    private int id_client;
    private int id_product;
    private int quantity;

    public OrderTable() {}

    public OrderTable(int id_client, int id_product, int quantity) {
        this.id_client = id_client;
        this.id_product = id_product;
        this.quantity = quantity;
    }

    public OrderTable(int id, int id_client, int id_product, int quantity) {
        this.id = id;
        this.id_client = id_client;
        this.id_product = id_product;
        this.quantity = quantity;
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
    public int getId_client() {
        return id_client;
    }

    /**
     *
     * @param id_client
     */
    public void setId_client(int id_client) {
        this.id_client = id_client;
    }

    /**
     *
     * @return
     */
    public int getId_product() {
        return id_product;
    }

    /**
     *
     * @param id_product
     */
    public void setId_product(int id_product) {
        this.id_product = id_product;
    }

    /**
     *
     * @return
     */
    public int getQuantity() {
        return quantity;
    }

    /**
     *
     * @param quantity
     */
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    /**
     *
     * @return
     */
    @Override
    public String toString() {
        return "OrderTable{" +
                "id=" + id +
                ", id_client=" + id_client +
                ", id_product=" + id_product +
                ", quantity=" + quantity +
                '}';
    }
}

package csv;

public class Supplier {

    private String supplierName;
    private String supplierAddress;

    public Supplier() {
    }

    public Supplier(String supplierName, String supplierAddress) {
        this.supplierName = supplierName;
        this.supplierAddress = supplierAddress;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public String getSupplierAddress() {
        return supplierAddress;
    }

    public void setSupplierAddress(String supplierAddress) {
        this.supplierAddress = supplierAddress;
    }
}

package utils;

import android.os.Parcel;
import android.os.Parcelable;
import android.widget.Toast;

public class Product implements Parcelable{

    private String product_name;
    private int product_icon;
    private String product_code;
    private String product_barcode;
    private String product_department;
    private String product_division;
    private String product_family;
    private String product_groupfamily;
    private String product_subfamily;
    private String product_grammage;
    private String product_mrp;
    private String product_bbprice;
    private String product_qty;
    private String product_value;
    private double product_rate;

    public Product() {

    }

    public Product(Parcel parcel){
        this.product_name = parcel.readString();
        this.product_code = parcel.readString();
        this.product_barcode = parcel.readString();
        this.product_department = parcel.readString();
        this.product_division = parcel.readString();
        this.product_family = parcel.readString();
        this.product_groupfamily = parcel.readString();
        this.product_subfamily = parcel.readString();
    }

    public void setProductName (String product_name)
    {
        this.product_name = product_name;
    }

    public String getProductName()
    {
        return product_name;
    }

    public void setProductIcon (int p_icon){ this.product_icon = p_icon; }
    public int getProductIcon(){return product_icon;}

    public void setProductGrammage (String product_grammage)
    {
        this.product_grammage = product_grammage;
    }

    public String getProductGrammage()
    {
        return product_grammage;
    }

    public void setProductRate(double product_rate){
        this.product_rate = product_rate;
    }
    public double getProductRate(){
        return this.product_rate;
    }

    public void setProductBarcode (String product_barcode)
    {
        this.product_barcode = product_barcode;
    }

    public String getProductBarcode()
    {
        return product_barcode;
    }

    public void setProductDivision (String product_division)
    {
        this.product_division = product_division;
    }

    public String getProductDivision()
    {
        return product_division;
    }

    public void setProductDepartment (String product_department)
    {
        this.product_department = product_department;
    }

    public String getProductDepartment()
    {
        return product_department;
    }

    public void setProductFamily (String product_family)
    {
        this.product_family = product_family;
    }

    public String getProductFamily()
    {
        return product_family;
    }

    public void setProductGroupFamily (String product_groupfamily)
    {
        this.product_groupfamily = product_groupfamily;
    }

    public String getProductGroupFamily()
    {
        return product_groupfamily;
    }

    public void setProductSubFamily (String product_subfamily)
    {
        this.product_subfamily = product_subfamily;
    }

    public String getProductSubFamily()
    {
        return product_subfamily;
    }

    public void setProductMRP (String product_mrp)
    {
        this.product_mrp = product_mrp;
    }

    public String getProductMRP()
    {
        return product_mrp;
    }

    public void setProductBBPrice (String product_bbprice)
    {
        this.product_bbprice = product_bbprice;
    }

    public String getProductBBPrice()
    {
        return product_bbprice;
    }

    public void setProductQty (String product_qty)
    {
        this.product_qty = product_qty;
    }

    public String getProductQty()
    {
        return product_qty;
    }

    public void setProductValue (String product_value)
    {
        this.product_value = product_value;
    }

    public String getProductValue()
    {
        return product_value;
    }

    public void setProductCode(String product_code)
    {
        this.product_code = product_code;
    }

    public String getProductCode()
    {
        return product_code;
    }

    @Override
    public int describeContents() {

        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(product_name);
        dest.writeString(product_code);
        dest.writeString(product_barcode);
        dest.writeString(product_department);
        dest.writeString(product_division);
        dest.writeString(product_family);
        dest.writeString(product_groupfamily);
        dest.writeString(product_subfamily);

    }

    public static final Creator<Product> CREATOR
            = new Creator<Product>() {
        @Override
        public Product createFromParcel(Parcel source) {
            return new Product(source);
        }

        @Override
        public Product[] newArray(int size) {
            return new Product[size];
        }
    };
}

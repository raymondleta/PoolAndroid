package com.tosh.poolandroid.view.adapter


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.squareup.picasso.Picasso
import com.tosh.poolandroid.R
import com.tosh.poolandroid.model.Product

class ProductAdapter(private val productModel: List<Product>): RecyclerView.Adapter<ProductAdapter.ProductView>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductView {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.product_item, parent, false)
        return ProductView(view)
    }

    override fun getItemCount(): Int {
        return productModel.size
    }

    override fun onBindViewHolder(holder: ProductView, position: Int) {
        var product = productModel[position]

        holder.foodName.text = product.productName
        holder.foodDesc.text = product.productDetails
        holder.foodPrice.text = product.price.toString()

        Picasso.get()
                .load(productModel[position].imgUrl)
                .fit()
                .centerCrop()
                .into(holder.foodImg)
    }

    class ProductView(itemView: View, var product: Product? = null) : RecyclerView.ViewHolder(itemView){
        val foodImg: ImageView = itemView.findViewById(R.id.foodImg)
        val foodName: TextView = itemView.findViewById(R.id.foodName)
        val foodDesc: TextView = itemView.findViewById(R.id.foodDesc)
        val foodPrice: TextView = itemView.findViewById(R.id.foodPrice)
        val foodCartBtn: MaterialButton = itemView.findViewById(R.id.foodCartBtn)
    }
}
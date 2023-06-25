package com.codepath.bestsellerlistapp

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.codepath.bestsellerlistapp.R.id


class BestSellerBooksRecyclerViewAdapter(
    private val books: List<BestSellerBook>,
    private val mListener: OnListFragmentInteractionListener?
) : RecyclerView.Adapter<BestSellerBooksRecyclerViewAdapter.BookViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_best_seller_book, parent, false)
        return BookViewHolder(view)
    }

    /**
     * This inner class lets us refer to all the different View elements
     */
    inner class BookViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        var mItem: BestSellerBook? = null
        val mBookTitle: TextView = mView.findViewById<View>(id.book_title) as TextView
        val mBookAuthor: TextView = mView.findViewById<View>(id.book_author) as TextView
        val mBookDescription: TextView = mView.findViewById<View>(id.book_description) as TextView
        val mBookImage: ImageView = mView.findViewById<View>(id.book_image) as ImageView
        val rank: TextView = mView.findViewById<View>(id.ranking) as TextView
        val mBuyButton: Button = mView.findViewById<View>(id.buy_button) as Button

        override fun toString(): String {
            return mBookTitle.toString() + " '" + mBookAuthor.text + "'"
        }
    }

    /**
     * This lets us "bind" each Views in the ViewHolder to its' actual data!
     */
    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        val book = books[position] // get the position of the book item on the view
        holder.mItem = book
        holder.mBookTitle.text = book.title
        holder.mBookAuthor.text = book.author
        holder.mBookDescription.text = book.description
        holder.rank.text = book.rank.toString()
        // load the image url into the view holder
        Glide.with(holder.mView).load(book.bookImageUrl).centerInside().into(holder.mBookImage)

        holder.mView.setOnClickListener {
            holder.mItem?.let { book ->
                mListener?.onItemClick(book)
            }
        }
        // go to amazon url for the book
        holder.mBuyButton.setOnClickListener {
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(book.amazonUrl))
            startActivity(it.context, browserIntent, null)

        }
    }

    override fun getItemCount(): Int {
        return books.size
    }
}
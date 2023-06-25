package com.codepath.bestsellerlistapp

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.ContentLoadingProgressBar
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.codepath.asynchttpclient.AsyncHttpClient
import com.codepath.asynchttpclient.RequestParams
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.Headers
import org.json.JSONObject

//private const val API_KEY = "tP2eP6pEbL6d4xNp902zViamY2RVWgDy"

var api_key: String = BuildConfig.API_KEY


//private const val SEARCH_API_KEY = API_KEY

/*
 * The class for the only fragment in the app, which contains the progress bar,
 * recyclerView, and performs the network calls to the NY Times API.
 */
class BestSellerBooksFragment : Fragment(), OnListFragmentInteractionListener {

    /*
     * Constructing the view
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflating the fragment's layout
        val view = inflater.inflate(R.layout.fragment_best_seller_books_list, container, false)
        // Finding and initializing the progress bar and recyclerView
        val progressBar = view.findViewById<View>(R.id.progress) as ContentLoadingProgressBar
        val recyclerView = view.findViewById<View>(R.id.list) as RecyclerView
        val context = view.context
        recyclerView.layoutManager = GridLayoutManager(context, 2)

        // Updating the adapter with new data
        updateAdapter(progressBar, recyclerView)
        return view
    }

    /*
     * Updates the RecyclerView adapter with new data.  Networking data
     */
    private fun updateAdapter(progressBar: ContentLoadingProgressBar, recyclerView: RecyclerView) {
        progressBar.show()

        // Creating and setting up an AsyncHTTPClient
        val client = AsyncHttpClient()
        val params = RequestParams()
        params["api-key"] = api_key

        client[
                "https://api.nytimes.com/svc/books/v3/lists/current/hardcover-fiction.json",
                params,
                object : JsonHttpResponseHandler()

        {
            /*
             * The onSuccess function gets called when
             * HTTP response status is "200 OK"
             */
            override fun onSuccess(
                statusCode: Int,
                headers: Headers,
                json: JsonHttpResponseHandler.JSON
            ) {
                // The wait for a response is over, hiding the progress bar
                progressBar.hide()
                val resultsJSON : JSONObject = json.jsonObject.get("results") as JSONObject
                val booksRawJSON : String = resultsJSON.get("books").toString()
                Log.i("GD", booksRawJSON)

                // Parsing the JSON data into a list of BestSellerBook objects using Gson
                val gson = Gson()
                val arrayBookType = object : TypeToken<List<BestSellerBook>>() {}.type
                val models : List<BestSellerBook> = gson.fromJson(booksRawJSON, arrayBookType)

                // Setting the adapter for the RecyclerView
                recyclerView.adapter = BestSellerBooksRecyclerViewAdapter(models, this@BestSellerBooksFragment)

                // Look for this in Logcat:
                Log.d("BestSellerBooksFragment", "response successful")
            }

            /*
             * The onFailure function gets called when
             * HTTP response status is "4XX" (eg. 401, 403, 404)
             */
            override fun onFailure(
                statusCode: Int,
                headers: Headers?,
                errorResponse: String,
                t: Throwable?
            ) {
                // The wait for a response is over
                progressBar.hide()
                // If the error is not null, log it!
                t?.message?.let {
                    Log.e("BestSellerBooksFragment", errorResponse)
                }
            }
        }]

    }

    /*
     * What happens when a particular book is clicked.
     */
    override fun onItemClick(item: BestSellerBook) {
        Toast.makeText(context, "test: " + item.title, Toast.LENGTH_LONG).show()
    }

}
